
package org.slavik.service;

import com.jcraft.jsch.SftpException;
import org.slavik.builder.SqlBuilder;
import org.slavik.connector.SftpClientProperties;
import org.slavik.dioritB2B.DioritAPIClientImpl;
import org.slavik.dioritB2B.model.DioritProduct;
import org.slavik.dioritB2B.model.ShortProduct;
import org.slavik.connector.JschSftpClient;
import org.slavik.entity.attribute.Attribute;
import org.slavik.entity.attribute.AttributeDescription;
import org.slavik.entity.manufacturer.Manufacturer;
import org.slavik.entity.product.*;
import org.slavik.repository.product.JdbcProductToProductDescription;
import org.slavik.repository.attribute.JdbcAttributeDescriptionRepository;
import org.slavik.repository.attribute.JdbcAttributeRepository;
import org.slavik.repository.manufacturer.JdbcManufacturerRepository;
import org.slavik.repository.product.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DioritProductService implements ProductService {

    private final DioritAPIClientImpl apiClient;
    private final JdbcProductDescriptionRepository jdbcProductDescriptionRepository;
    private final JdbcProductRepository jdbcProductRepository;
    private final JdbcProductToCategoryRepository jdbcProductToCategory;
    private final JdbcProductToProductDescription jdbcProductToProductDescription;
    private final JdbcProductToStore jdbcProductToStore;
    private final JdbcManufacturerRepository jdbcManufacturerRepository;
    private final JdbcAttributeRepository jdbcAttributeRepository;
    private final JdbcAttributeDescriptionRepository jdbcAttributeDescriptionRepository;
    private final JdbcProductAttributeRepository jdbcProductAttributeRepository;
    private final JdbcProductImageRepository jdbcProductImageRepository;
    private final SftpClientProperties configuration;
    private JschSftpClient jschSftpClient;

    public DioritProductService(DioritAPIClientImpl apiClient,
                                JdbcProductDescriptionRepository jdbcProductDescriptionRepository,
                                JdbcProductRepository jdbcProductRepository,
                                JdbcProductToCategoryRepository jdbcProductToCategory, JdbcProductToProductDescription jdbcProductToProductDescription, JdbcProductToStore jdbcProductToStore,
                                JdbcManufacturerRepository jdbcManufacturerRepository,
                                JdbcAttributeRepository jdbcAttributeRepository,
                                JdbcAttributeDescriptionRepository jdbcAttributeDescriptionRepository,
                                JdbcProductAttributeRepository jdbcProductAttributeRepository,
                                JdbcProductImageRepository jdbcProductImageRepository, SftpClientProperties configuration,
                                JschSftpClient jschSftpClient) {
        this.apiClient = apiClient;
        this.jdbcProductDescriptionRepository = jdbcProductDescriptionRepository;
        this.jdbcProductRepository = jdbcProductRepository;
        this.jdbcProductToCategory = jdbcProductToCategory;
        this.jdbcProductToProductDescription = jdbcProductToProductDescription;
        this.jdbcProductToStore = jdbcProductToStore;
        this.jdbcManufacturerRepository = jdbcManufacturerRepository;
        this.jdbcAttributeRepository = jdbcAttributeRepository;
        this.jdbcAttributeDescriptionRepository = jdbcAttributeDescriptionRepository;
        this.jdbcProductAttributeRepository = jdbcProductAttributeRepository;
        this.jdbcProductImageRepository = jdbcProductImageRepository;
        this.configuration = configuration;
        this.jschSftpClient = jschSftpClient;
    }

    private final String EAN_VALUE = "dioritb2b";
    private final int WEIGHT_CLASS_ID = 0;
    private final int LENGTH_CLASS_ID = 0;
    private final int STATUS_VALUE = 1;
    private final int SUBTRACT_VALUE = 1;
    private final int DN_ID = 0;
    private final int LANGUAGE_ID = 1;
    private final String UPC_VALUE = "";
    private final String JAN_VALUE = "";
    private final String ISBN_VALUE = "";
    private final String MPN_VALUE = "";
    private final String LOCATION_VALUE = "";
    private final String VIDEO_VALUE = "";
    private final int COST_VALUE = 0;
    private final int POINTS_VALUE = 0;
    private final int TAX_CLASS_ID_VALUE = 0;
    private final String SUPPLIER_VALUE = "";
    private final String TAG_VALUE = "";
    private final String DESCRIPTION_VALUE = "";
    private final String META_KEYWORD_VALUE = "";

    @Override
    public void sync() throws Exception {
        List<ShortProduct> addedProducts = new ArrayList<>();
        List<ProductToProductDescription> productToProductDescriptions = jdbcProductToProductDescription.findAll();
        List<ShortProduct> allProductAPI = apiClient.getAllProduct();
        boolean isThereProduct;
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductRepository);
        SqlBuilder updateBuilder = new SqlBuilder(jdbcProductRepository);
        for (ShortProduct productAPI : allProductAPI) {
            if (productAPI.getStock() == -1) {
                continue;
            }
            isThereProduct = false;
            for (ProductToProductDescription productToProductDescription : productToProductDescriptions) {
                if (productAPI.getName().equals(productToProductDescription.getName())) {
                    if (productAPI.getPhotos().isEmpty()) {
                        break;
                    }
                    isThereProduct = true;
                    if (productToProductDescription.getEan().equals("dioritb2b")) {
                        updateBuilder.addRequest(createProductForUpdate(productToProductDescription.getProductId(), productAPI));
                        System.out.println("Update " + productToProductDescription.getProductId());
                    }
                    break;
                }
            }
            if (!isThereProduct) {
                if (productAPI.getPhotos().isEmpty()) {
                    continue;
                }
                if (productAPI.getStock() == 0) {
                    continue;
                }
                insertBuilder.addRequest(createRequestProduct(productAPI));
                addedProducts.add(productAPI);
                System.out.println("Create");
            }
        }

        if (!insertBuilder.isEmpty()) {
            insertBuilder.insert();
        }
        if (!updateBuilder.isEmpty()) {
            updateBuilder.update();
        }
        List<Integer> productIds = jdbcProductRepository.getNewProductIdsByEAN("dioritb2b");
        insertBuilder = new SqlBuilder(jdbcProductDescriptionRepository);
        if (!addedProducts.isEmpty()) {
            int i = 0;
            for (ShortProduct product : addedProducts) {
                insertBuilder.addRequest(
                        new Object[] {
                                productIds.get(i),
                                LANGUAGE_ID,
                                product.getName(),
                                DESCRIPTION_VALUE,
                                TAG_VALUE,
                                product.getName(),
                                product.getName(),
                                META_KEYWORD_VALUE,
                                product.getName()
                        }
                );
                addAllPhoto(product.getPhotos(), productIds.get(i));
                i++;
            }
        }
        if (!insertBuilder.isEmpty()) {
            insertBuilder.insert();
        }
        if (!addedProducts.isEmpty()) {
            assignmentOfStore(productIds);
            assignmentOfCategory(productIds);
            createAttribute(addedProducts);
        }
        checkForDelete(allProductAPI);
    }

    private void assignmentOfCategory(List<Integer> productIds) {
        if (productIds.isEmpty()) {
            return;
        }
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductToCategory);
        for (Integer productId : productIds) {
            insertBuilder.addRequest(new Object[]{productId, 2012});
        }
        insertBuilder.insert();
    }

    private int brandProductRatio(UUID productId) throws InterruptedException {
        DioritProduct product = apiClient.viewProduct(productId);
        Manufacturer manufacturer = jdbcManufacturerRepository.find(product.getBrand().getName());
        if (manufacturer == null) {
            manufacturer = jdbcManufacturerRepository.create(new Manufacturer(
                    0,
                    product.getBrand().getName()
            ));
        }
        return manufacturer.getManufacturerId();
    }

    private void createAttribute(List<ShortProduct> products) {
        List<Integer> productIds = jdbcProductRepository.getNewProductIdsByEAN("dioritb2b");
        List<String> attributeList = new ArrayList<>();
        SqlBuilder insertBuilderForAttribute = new SqlBuilder(jdbcProductAttributeRepository);
        int i = 0;
        for (ShortProduct product : products) {
            Map<String, Object> attributeMap = product.extractAttributesExceptGroup(product);
            for (Map.Entry<String, Object> entry : attributeMap.entrySet()) {
                List<AttributeDescription> attributeDescriptions =
                        jdbcAttributeDescriptionRepository.findByName(entry.getKey());
                if (!attributeDescriptions.isEmpty()) {
                    AttributeDescription attributeDesc = attributeDescriptions.get(0);
                    boolean isThereAttribute = false;
                    for (String attribute : attributeList) {
                        if (attributeDesc.getName().equals(attribute)) {
                            isThereAttribute = true;
                            break;
                        }
                    }
                    if (!isThereAttribute) {
                        insertBuilderForAttribute.addRequest(createRequestAttributeToProduct(entry.getValue().toString(), attributeDesc.getAttributeId(), productIds.get(i)));
                        attributeList.add(attributeDesc.getName());
                    }
                } else {
                    Attribute newAttribute = jdbcAttributeRepository.create(new Attribute(
                            0,
                            1,
                            0
                    ));
                    AttributeDescription newAttributeDescription =
                            jdbcAttributeDescriptionRepository.create(new AttributeDescription(
                                    newAttribute.getAttributeId(),
                                    LANGUAGE_ID,
                                    entry.getValue().toString()
                            ));
                    insertBuilderForAttribute.addRequest(createRequestAttributeToProduct(entry.getValue().toString(), newAttributeDescription.getAttributeId(), productIds.get(i)));
                    attributeList.add(entry.getValue().toString());
                }
            }
            i++;
        }
        if (!insertBuilderForAttribute.isEmpty()) {
            insertBuilderForAttribute.insert();
        }
    }

    private Object[] createRequestProduct(ShortProduct product) throws Exception {
        return new Object[]{
                creatorOfSkuNumbers(product.getSku()),
                creatorOfSkuNumbers(product.getSku()),
                UPC_VALUE,
                EAN_VALUE,
                JAN_VALUE,
                ISBN_VALUE,
                MPN_VALUE,
                LOCATION_VALUE,
                product.getStock(),
                determineStockStatus(product.getStock()),
                uploadPhoto(product.getMainPhoto()),
                VIDEO_VALUE,
                brandProductRatio(product.getID()),
                product.getPrice(),
                COST_VALUE,
                POINTS_VALUE,
                TAX_CLASS_ID_VALUE,
                new java.sql.Date(System.currentTimeMillis()),
                product.getWeight(),
                WEIGHT_CLASS_ID,
                product.getLength(),
                product.getWidth(),
                product.getHeight(),
                LENGTH_CLASS_ID,
                SUBTRACT_VALUE,
                STATUS_VALUE,
                new java.sql.Timestamp(System.currentTimeMillis()),
                new java.sql.Timestamp(System.currentTimeMillis()),
                DN_ID,
                SUPPLIER_VALUE
        };
//        return "(" + creatorOfSkuNumbers(product.getSku()) + ", " +
//                creatorOfSkuNumbers(product.getSku()) + ", " +
//                UPC_VALUE + ", " +
//                EAN_VALUE + ", " +
//                JAN_VALUE + ", " +
//                ISBN_VALUE + ", " +
//                MPN_VALUE + ", " +
//                LOCATION_VALUE + ", " +
//                product.getStock() + ", " +
//                determineStockStatus(product.getStock()) + ", '" +
//                uploadPhoto(product.getMainPhoto()) + "', " +
//                VIDEO_VALUE + ", " +
//                brandProductRatio(product.getID()) + ", " +
//                product.getPrice() + ", " +
//                COST_VALUE + ", " +
//                POINTS_VALUE + ", " +
//                TAX_CLASS_ID_VALUE + ", " +
//                "CURRENT_DATE()" + ", " +
//                product.getWeight() + ", " +
//                WEIGHT_CLASS_ID + ", " +
//                product.getLength() + ", " +
//                product.getWidth() + ", " +
//                product.getHeight() + ", " +
//                LENGTH_CLASS_ID + ", " +
//                SUBTRACT_VALUE + ", " +
//                STATUS_VALUE + ", " +
//                "NOW()" + ", " +
//                "NOW()" + ", " +
//                DN_ID + ", " +
//                SUPPLIER_VALUE + "),";
    }

    private int determineStockStatus(int quantity) {
        if (quantity == 0) {
            return 9;
        } else {
            return 7;
        }
    }

    private Object[] createProductForUpdate(int productId, ShortProduct product) {
        return new Object[]{
                product.getStock(),
                determineStockStatus(product.getStock()),
                productId
        };
//        return " UPDATE oc_product SET " +
//                "quantity = " + product.getStock() + ", " +
//                "stock_status_id = " + determineStockStatus(product.getStock()) + ", " +
//                "date_modified = NOW()" +
//                "WHERE product_id = " + productId + ";";
    }

    private Object[] createRequestAttributeToProduct(String text, int attributeId, int productId) {
        return new Object[]{
                productId,
                attributeId,
                LANGUAGE_ID,
                text
        };
//        return "(" + productId + ", " +
//                attributeId + ", " +
//                LANGUAGE_ID + ", '" +
//                text + "'),";
    }

    private final int STORE_ID_VALUE = 0;

    private void assignmentOfStore(List<Integer> productIds) {
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductToStore);
        for (int productId : productIds) {
            insertBuilder.addRequest(new Object[]{productId, STORE_ID_VALUE});
        }
        insertBuilder.insert();
    }

    private void addAllPhoto(List<String> photos, int productId) throws Exception {
        if (photos.size() == 1) {
            return;
        }
        int sortOrder = 1;
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductImageRepository);
        for (int i = 1; i < photos.size(); i++) {
            insertBuilder.addRequest(new Object[]{productId, uploadPhoto(photos.get(i)), sortOrder});
            sortOrder++;
        }
        if (!insertBuilder.isEmpty()) {
            insertBuilder.insert();
        }
    }

    private String uploadPhoto(String fileUrl) throws Exception {
        String locationPath = "/var/www/u3045843/data/www/germes.vip/image/catalog/b2b";
        String fileName = extractFileNameFromUrl(fileUrl);
        InputStream inputStream;
        try {
            inputStream = new URL(fileUrl).openStream();
            try {
                if (jschSftpClient.isFileExist(fileName, locationPath)) {
                    return "catalog/b2b/" + fileName;
                }
                jschSftpClient.uploadFile(inputStream, locationPath, fileName);
            } catch (Exception e) {
                JschSftpClient newJschSftpClient = new JschSftpClient(configuration);
                jschSftpClient = newJschSftpClient;
                jschSftpClient.uploadFile(inputStream, locationPath, fileName);
            }
        } catch (IOException e) {
            return null;
        }
        return "catalog/b2b/" + fileName;
    }


    public static String extractFileNameFromUrl(String url) {
        if (url == null || url.isEmpty()) return "";
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private String creatorOfSkuNumbers(String sku) {
        return sku + "30";
    }

    private void checkForDelete(List<ShortProduct> allProductAPI) throws Exception {
        List<ProductImage> productImageList = new ArrayList<>();
        List<ProductToProductDescription> productToProductDescriptionList = jdbcProductToProductDescription.findAllByEAN("dioritb2b");
        List<Integer> productIdsToDelete = new ArrayList<>();
        boolean isThereProduct;
        for (ProductToProductDescription productToProductDescription : productToProductDescriptionList) {
            isThereProduct = false;
            for (ShortProduct product : allProductAPI) {
                if (productToProductDescription.getName().equals(product.getName())) {
                    isThereProduct = true;
                    break;
                }
            }
            if (!isThereProduct) {
                productIdsToDelete.add(productToProductDescription.getProductId());
                List<ProductImage> currentProductImageList = jdbcProductImageRepository.find(productToProductDescription.getProductId());
                productImageList.addAll(currentProductImageList);
            }
        }
        jdbcProductRepository.deleteAllByRequest(productIdsToDelete);
        jdbcProductDescriptionRepository.deleteAllByRequest(productIdsToDelete);
        deleteAllPhoto(productImageList);
    }

    private void deleteAllPhoto(List<ProductImage> allProductImage) throws Exception {
        String localPath = "/var/www/u3045843/data/www/germes.vip/image/catalog/b2b";
        for (ProductImage productImage : allProductImage) {
            String fileName = extractFileNameFromUrl(productImage.getImage());
            try {
                jschSftpClient.removeFile(fileName, localPath);
            } catch (Exception e) {
                JschSftpClient newJschSftpClient = new JschSftpClient(configuration);
                jschSftpClient = newJschSftpClient;
                try {
                    jschSftpClient.removeFile(fileName, localPath);
                } catch (Exception ex) {
                    continue;
                }
            }
        }
    }
}
