package org.slavik.service;

import com.jcraft.jsch.SftpException;
import org.jetbrains.annotations.NotNull;
import org.slavik.WebClientConfiguration;
import org.slavik.breez.BreezApiClientImpl;
import org.slavik.breez.model.*;
import org.slavik.builder.SqlBuilder;
import org.slavik.connector.JschSftpClient;
import org.slavik.connector.SftpClientProperties;
import org.slavik.dioritB2B.APISourceConfiguration;
import org.slavik.dioritB2B.DioritAPIClientImpl;
import org.slavik.dioritB2B.DioritApiClient;
import org.slavik.entity.attribute.Attribute;
import org.slavik.entity.attribute.AttributeDescription;
import org.slavik.entity.category.Category;
import org.slavik.entity.category.CategoryDescription;
import org.slavik.entity.manufacturer.Manufacturer;
import org.slavik.entity.product.Product;
import org.slavik.entity.product.ProductImage;
import org.slavik.entity.product.ProductToProductDescription;
import org.slavik.ocs.model.OCSProduct;
import org.slavik.repository.product.JdbcProductToProductDescription;
import org.slavik.repository.attribute.JdbcAttributeDescriptionRepository;
import org.slavik.repository.attribute.JdbcAttributeRepository;
import org.slavik.repository.category.JdbcCategoryDescriptionRepository;
import org.slavik.repository.category.JdbcCategoryRepository;
import org.slavik.repository.manufacturer.JdbcManufacturerRepository;
import org.slavik.repository.product.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class BreezProductService implements ProductService {

    private BreezApiClientImpl apiClient;
    private final JdbcCategoryDescriptionRepository jdbcCategoryDescriptionRepository;
    private final JdbcProductDescriptionRepository jdbcProductDescriptionRepository;
    private final JdbcProductRepository jdbcProductRepository;
    private final JdbcProductToCategoryRepository jdbcProductToCategoryRepository;
    private final JdbcProductToProductDescription jdbcProductToProductDescription;
    private final JdbcProductToStore jdbcProductToStore;
    private final JdbcManufacturerRepository jdbcManufacturerRepository;
    private final JdbcAttributeRepository jdbcAttributeRepository;
    private final JdbcAttributeDescriptionRepository jdbcAttributeDescriptionRepository;
    private final JdbcProductAttributeRepository jdbcProductAttributeRepository;
    private final JdbcProductImageRepository jdbcProductImageRepository;
    private final SftpClientProperties configuration;
    private JschSftpClient jschSftpClient;

    private final String EAN_VALUE = "breez";
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
    private final int WEIGHT_VALUE = 0;
    private final int LENGTH_VALUE = 0;
    private final int WIDTH_VALUE = 0;
    private final int HEIGHT_VALUE = 0;
    private final String TAG_VALUE = "";
    private final String DESCRIPTION_VALUE = "";
    private final String META_KEYWORD_VALUE = "";

    public BreezProductService(BreezApiClientImpl apiClient,
                               JdbcCategoryDescriptionRepository jdbcCategoryDescriptionRepository,
                               JdbcProductDescriptionRepository jdbcProductDescriptionRepository,
                               JdbcProductRepository jdbcProductRepository,
                               JdbcProductToCategoryRepository jdbcProductToCategoryRepository,
                               JdbcProductToProductDescription jdbcProductToProductDescription,
                               JdbcProductToStore jdbcProductToStore,
                               JdbcManufacturerRepository jdbcManufacturerRepository,
                               JdbcAttributeRepository jdbcAttributeRepository,
                               JdbcAttributeDescriptionRepository jdbcAttributeDescriptionRepository,
                               JdbcProductAttributeRepository jdbcProductAttributeRepository,
                               JdbcProductImageRepository jdbcProductImageRepository,
                               SftpClientProperties configuration,
                               JschSftpClient jschSftpClient) {
        this.apiClient = apiClient;
        this.jdbcCategoryDescriptionRepository = jdbcCategoryDescriptionRepository;
        this.jdbcProductDescriptionRepository = jdbcProductDescriptionRepository;
        this.jdbcProductRepository = jdbcProductRepository;
        this.jdbcProductToCategoryRepository = jdbcProductToCategoryRepository;
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

    @Override
    public void sync() throws Exception {
        Map<String, BreezProductResponse> allProductAPI = apiClient.getAllProducts();
        Map<String, BreezBrand> breezBrandMap = apiClient.getAllBrands();
        Map<String, BreezCategory> breezCategoryMap = apiClient.getAllCategories();
        List<BreezProductResponse> addedProducts = new ArrayList<>();
        List<String> addedKeys = new ArrayList<>();
        List<ProductToProductDescription> productToProductDescriptionList = jdbcProductToProductDescription.findAll();
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductRepository);
        SqlBuilder updateBuilder = new SqlBuilder(jdbcProductRepository);
        boolean isThereProduct;
        for (Map.Entry<String, BreezProductResponse> productAPI : allProductAPI.entrySet()) {
            isThereProduct = false;
            for (ProductToProductDescription productToProductDescription : productToProductDescriptionList) {
                if (productToProductDescription.getName().equals(productAPI.getValue().getTitle())) {
                    if (productAPI.getValue().getImages().isEmpty()) {
                        break;
                    }
                    isThereProduct = true;
                    if (productToProductDescription.getEan().equals("breez")) {
                        updateBuilder.addRequest(createProductForUpdate(productToProductDescription.getProductId(), productAPI.getValue()));
                        System.out.println("Update " + productToProductDescription.getProductId());
                    }
                    break;
                }
            }
            if (!isThereProduct) {
                if (productAPI.getValue().getImages().isEmpty()) {
                    continue;
                }
                Object[] request = createRequestProduct(productAPI.getValue(), breezBrandMap);
                if (request == null) {
                    continue;
                }
                insertBuilder.addRequest(request);
                addedProducts.add(productAPI.getValue());
                addedKeys.add(productAPI.getKey());
                System.out.println("Create");
            }
        }

        if (!insertBuilder.isEmpty()) {
            insertBuilder.insert();
        }
        if (!updateBuilder.isEmpty()) {
            updateBuilder.update();
        }
        List<Integer> productIds = jdbcProductRepository.getNewProductIdsByEAN("breez");
        insertBuilder = new SqlBuilder(jdbcProductDescriptionRepository);
        int i = 0;
        if (!addedProducts.isEmpty()) {
            for (BreezProductResponse product : addedProducts) {
                insertBuilder.addRequest(new Object[]{
                                productIds.get(i),
                                LANGUAGE_ID,
                                product.getTitle(),
                                DESCRIPTION_VALUE,
                                TAG_VALUE,
                                product.getTitle(),
                                product.getTitle(),
                                META_KEYWORD_VALUE,
                                product.getTitle()
                        }
                );
                addAllPhoto(product.getImages(), productIds.get(i));
                i++;
            }
        }
        if (!insertBuilder.isEmpty()) {
            insertBuilder.insert();
        }
        if (!addedProducts.isEmpty()) {
            assignmentOfStore(productIds);
            assignmentOfCategory(addedProducts, productIds, breezCategoryMap);
            createAttribute(addedKeys, productIds);
        }
        checkForDelete(allProductAPI);
    }

    private Object[] createRequestProduct(BreezProductResponse product, Map<String, BreezBrand> breezBrandMap) throws Exception {
        int brandId = brandProductRatio(product, breezBrandMap);
        if (brandId == 0) {
            return null;
        }
        int price = product.getPrice().getRic();
        if (price == 0) {
            return null;
        }
        String image = uploadPhoto(product.getImages().get(0));
        if (image == null) {
            return null;
        }
        int[] stock = assignmentOfStockStatus(product.getNc());
        if (stock.length > 0) {
            if (stock[0] == 0) {
                return null;
            }
        } else {
            return null;
        }
        return new Object[]{
                product.getArticul(),
                creatorOfSkuNumbers(product.getNc()),
                UPC_VALUE,
                EAN_VALUE,
                JAN_VALUE,
                ISBN_VALUE,
                MPN_VALUE,
                LOCATION_VALUE,
                stock[1],
                stock[0],
                image,
                VIDEO_VALUE,
                brandId,
                price,
                COST_VALUE,
                POINTS_VALUE,
                TAX_CLASS_ID_VALUE,
                new java.sql.Date(System.currentTimeMillis()),
                WEIGHT_VALUE,
                WEIGHT_CLASS_ID,
                LENGTH_VALUE,
                WIDTH_VALUE,
                HEIGHT_VALUE,
                LENGTH_CLASS_ID,
                SUBTRACT_VALUE,
                STATUS_VALUE,
                new java.sql.Timestamp(System.currentTimeMillis()),
                new java.sql.Timestamp(System.currentTimeMillis()),
                DN_ID,
                SUPPLIER_VALUE
        };
    }

    private int[] assignmentOfStockStatus(String nc) throws IOException, InterruptedException {
        List<BreezStockInfo> breezStockInfoList = apiClient.gettingWarehousesWhereTheProductAreLocated(nc);
        if (breezStockInfoList != null) {
            for (BreezStockInfo breezStockInfo : breezStockInfoList) {
                if (breezStockInfo.getQuantity() > 0) {
                    if (breezStockInfo.getStock().equals("РРЦ Бриз Ростов LV")) {
                        return new int[]{6, breezStockInfo.getQuantity()};
                    } else if (breezStockInfo.getStock().equals("Бриз Пятигорск")) {
                        return new int[]{7, breezStockInfo.getQuantity()};
                    } else return new int[]{9, breezStockInfo.getQuantity()};
                }
            }
        }
        return new int[]{};
    }

    private Object[] createProductForUpdate(int productId, BreezProductResponse product) throws SftpException, IOException, InterruptedException {
        int[] stock = assignmentOfStockStatus(product.getNc());
        return new Object[]{
                stock[1],
                stock[0],
                productId
        };
    }

    private void createAttribute(List<String> keys, List<Integer> productIds) throws IOException, InterruptedException {
        if (keys.isEmpty()) {
            return;
        }
        List<String> attributeList = new ArrayList<>();
        SqlBuilder insertBuilderForAttribute = new SqlBuilder(jdbcProductAttributeRepository);
        int i = 0;
        for (String key : keys) {
            BreezTech breezTech;
            try {
                breezTech = apiClient.getCharacteristicsToProduct(key);
            } catch (Exception e) {
                BreezApiClientImpl newApiClient = new BreezApiClientImpl(apiClient.getWebClient());
                apiClient = newApiClient;
                breezTech = apiClient.getCharacteristicsToProduct(key);
            }
            try {
                for (Map.Entry<String, Tech> techEntry : breezTech.getTechs().entrySet()) {
                    if (techEntry.getValue().getTitle().equals("Бренд") || techEntry.getValue().getUnit().equals("дюйм") || techEntry.getValue().getTitle().equals("Модель")) {
                        continue;
                    }
                    List<AttributeDescription> attributeDescriptions =
                            jdbcAttributeDescriptionRepository.findByName(techEntry.getValue().getTitle());
                    if (!attributeDescriptions.isEmpty()) {
                        AttributeDescription attributeDesc = attributeDescriptions.get(0);
                        if (!isThereAttribute(attributeList, attributeDesc.getName())) {
                            insertBuilderForAttribute.addRequest(createRequestAttributeToProduct(techEntry.getValue().getValue() + " " + techEntry.getValue().getUnit(), attributeDesc.getAttributeId(), productIds.get(i)));
                            attributeList.add(techEntry.getValue().getTitle());
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
                                        techEntry.getValue().getTitle()
                                ));
                        insertBuilderForAttribute.addRequest(createRequestAttributeToProduct(techEntry.getValue().getValue() + " " + techEntry.getValue().getUnit(), newAttributeDescription.getAttributeId(), productIds.get(i)));
                        attributeList.add(techEntry.getValue().getValue());
                    }
                }
                i++;
            } catch (NullPointerException nullPointerException) {
                jdbcProductRepository.delete(productIds.get(i));
                jdbcProductDescriptionRepository.delete(productIds.get(i));
                i++;
            }
            attributeList = new ArrayList<>();
        }
        if (!insertBuilderForAttribute.isEmpty()) {
            insertBuilderForAttribute.insert();
        }
    }

    private boolean isThereAttribute(List<String> attributeList, String attributeDesc) {
        for (String attribute : attributeList) {
            if (attribute.equals(attributeDesc)) {
                return true;
            }
        }
        return false;
    }

    private Object[] createRequestAttributeToProduct(String text, int attributeId, int productId) {
        return new Object[]{
                productId,
                attributeId,
                LANGUAGE_ID,
                text
        };
    }

    private void assignmentOfCategory(List<BreezProductResponse> productResponseList, List<Integer> productIds, Map<String, BreezCategory> breezCategoryMap) throws IOException, InterruptedException {
        if (productIds.isEmpty()) {
            return;
        }
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductToCategoryRepository);
        int i = 0;
        for (BreezProductResponse productResponse : productResponseList) {
            BreezCategory breezCategory = breezCategoryMap.get(productResponse.getCategoryID());
            List<CategoryDescription> categoryDescriptionList = jdbcCategoryDescriptionRepository.findByName(breezCategory.getTitle());
            if (categoryDescriptionList.size() == 1) {
                insertBuilder.addRequest(new Object[]{productIds.get(i), categoryDescriptionList.get(0).getCategoryId()});
            } else {
                insertBuilder.addRequest(new Object[]{productIds.get(i), 2107});
            }
            i++;
        }
        insertBuilder.insert();
    }

    private int brandProductRatio(BreezProductResponse product, Map<String, BreezBrand> brandList) throws IOException, InterruptedException {
        if (product == null || brandList == null) {
            return 0;
        }
        BreezBrand brand = brandList.get(product.getBrand());
        if (brand == null) {
            return 0;
        }
        Manufacturer manufacturer = jdbcManufacturerRepository.find(brand.getTitle());
        if (manufacturer == null) {
            Manufacturer newManufacturer = jdbcManufacturerRepository.create(new Manufacturer(
                    0,
                    brand.getTitle()
            ));
            return newManufacturer.getManufacturerId();
        }
        return manufacturer.getManufacturerId();
    }

    private void addAllPhoto(List<String> images, int productId) throws Exception {
        if (images.size() == 1) {
            return;
        }
        int sortOrder = 1;
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductImageRepository);
        for (int i = 1; i < images.size(); i++) {
            String image = uploadPhoto(images.get(i));
            if (image == null) {
                return;
            }
            insertBuilder.addRequest(new Object[]{productId, image, sortOrder});
            sortOrder++;
        }
        if (!insertBuilder.isEmpty()) {
            insertBuilder.insert();
        }
    }

    private final int STORE_ID_VALUE = 0;

    private void assignmentOfStore(List<Integer> productIds) {
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductToStore);
        for (int productId : productIds) {
            insertBuilder.addRequest(new Object[]{productId, STORE_ID_VALUE});
        }
        insertBuilder.insert();
    }

    private String uploadPhoto(String fileUrl) throws Exception {
        String locationPath = "/var/www/u3045843/data/www/germes.vip/image/catalog/breez";
        String fileName = extractFileNameFromUrl(fileUrl);
        InputStream inputStream;
        try {
            inputStream = new URL(fileUrl).openStream();
            try {
                if (jschSftpClient.isFileExist(fileName, locationPath)) {
                    return "catalog/breez/" + fileName;
                }
                jschSftpClient.uploadFile(inputStream, locationPath, fileName);
            } catch (Exception e) {
                JschSftpClient newJschSftpClient = new JschSftpClient(configuration);
                jschSftpClient = newJschSftpClient;
                if (jschSftpClient.isFileExist(fileName, locationPath)) {
                    return "catalog/breez/" + fileName;
                }
                jschSftpClient.uploadFile(inputStream, locationPath, fileName);
            }
        } catch (IOException e) {
            return null;
        }
        return "catalog/breez/" + fileName;
    }

    public static String extractFileNameFromUrl(String url) {
        if (url == null || url.isEmpty()) return "";
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private String creatorOfSkuNumbers(String sku) {
        return sku + "35";
    }

    private void checkForDelete(Map<String, BreezProductResponse> allProductAPI) throws Exception {
        List<ProductImage> productImageList = new ArrayList<>();
        List<ProductToProductDescription> productToProductDescriptionList = jdbcProductToProductDescription.findAllByEAN("breez");
        List<Integer> productIdsToDelete = new ArrayList<>();
        List<Product> productList = jdbcProductRepository.findByEAN("breez");
        boolean isThereProduct;
        int i = 0;
        for (ProductToProductDescription productToProductDescription : productToProductDescriptionList) {
            isThereProduct = false;
            for (Map.Entry<String, BreezProductResponse> productAPI : allProductAPI.entrySet()) {
                if (productToProductDescription.getName().equals(productAPI.getValue().getTitle()) || productList.get(i).getQuantity() == 0) {
                    isThereProduct = true;
                    i++;
                    break;
                }
                i++;
            }
            if (!isThereProduct) {
                productIdsToDelete.add(productToProductDescription.getProductId());
                List<ProductImage> currentProductImageList = jdbcProductImageRepository.find(productToProductDescription.getProductId());
                productImageList.addAll(currentProductImageList);
            }
        }
        jdbcProductRepository.deleteAllByRequest(productIdsToDelete);
        jdbcProductDescriptionRepository.deleteAllByRequest(productIdsToDelete);
        try {
            deleteAllPhoto(productImageList);
        } catch (SftpException sftpException) {
            JschSftpClient newJschSftpClient = new JschSftpClient(configuration);
            jschSftpClient = newJschSftpClient;
            deleteAllPhoto(productImageList);
        }
        deleteAllPhoto(productImageList);
    }


    private void deleteAllPhoto(List<ProductImage> allProductImage) throws SftpException {
        String localPath = "/var/www/u3045843/data/www/germes.vip/image/catalog/breez";
        for (ProductImage productImage : allProductImage) {
            String fileName = extractFileNameFromUrl(productImage.getImage());
            if (jschSftpClient.isFileExist(fileName)) {
                jschSftpClient.removeFile(fileName, localPath);
            }
        }
    }
}

