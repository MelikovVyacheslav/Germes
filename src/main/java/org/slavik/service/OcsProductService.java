package org.slavik.service;

import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slavik.builder.SqlBuilder;
import org.slavik.connector.JschSftpClient;
import org.slavik.connector.SftpClientProperties;
import org.slavik.entity.attribute.Attribute;
import org.slavik.entity.attribute.AttributeDescription;
import org.slavik.entity.category.Category;
import org.slavik.entity.category.CategoryDescription;
import org.slavik.entity.manufacturer.Manufacturer;
import org.slavik.entity.product.ProductImage;
import org.slavik.entity.product.ProductToProductDescription;
import org.slavik.ocs.OCSAPIClientImpl;
import org.slavik.ocs.model.*;
import org.slavik.repository.product.JdbcProductToProductDescription;
import org.slavik.repository.attribute.JdbcAttributeDescriptionRepository;
import org.slavik.repository.attribute.JdbcAttributeRepository;
import org.slavik.repository.category.JdbcCategoryDescriptionRepository;
import org.slavik.repository.category.JdbcCategoryRepository;
import org.slavik.repository.manufacturer.JdbcManufacturerRepository;
import org.slavik.repository.product.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OcsProductService implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(OcsProductService.class);

    private final OCSAPIClientImpl apiClient;
    private final JdbcCategoryRepository jdbcCategoryRepository;
    private final JdbcCategoryDescriptionRepository jdbcCategoryDescriptionRepository;
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

    public OcsProductService(OCSAPIClientImpl apiClient,
                             JdbcCategoryRepository jdbcCategoryRepository,
                             JdbcCategoryDescriptionRepository jdbcCategoryDescriptionRepository,
                             JdbcProductDescriptionRepository jdbcProductDescriptionRepository,
                             JdbcProductRepository jdbcProductRepository,
                             JdbcProductToCategoryRepository jdbcProductToCategory,
                             JdbcProductToProductDescription jdbcProductToProductDescription,
                             JdbcProductToStore jdbcProductToStore,
                             JdbcManufacturerRepository jdbcManufacturerRepository,
                             JdbcAttributeRepository jdbcAttributeRepository,
                             JdbcAttributeDescriptionRepository jdbcAttributeDescriptionRepository,
                             JdbcProductAttributeRepository jdbcProductAttributeRepository,
                             JdbcProductImageRepository jdbcProductImageRepository, SftpClientProperties configuration,
                             JschSftpClient jschSftpClient) {
        this.apiClient = apiClient;
        this.jdbcCategoryRepository = jdbcCategoryRepository;
        this.jdbcCategoryDescriptionRepository = jdbcCategoryDescriptionRepository;
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

    private final String EAN_VALUE = "ocs";
    private final int WEIGHT_CLASS_ID = 0;
    private final int LENGTH_CLASS_ID = 0;
    private final int STATUS_VALUE = 1;
    private final int SUBTRACT_VALUE = 0;
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
    private final String META_KEYWORD_VALUE = "";
    private final String DESCRIPTION_VALUE = "";
    private final int STORE_ID_VALUE = 0;

    @Override
    public void sync() throws Exception {
        logger.info("OCS sync started");

        List<Result> allProductAPI = apiClient.getAll();
        OCSProductCharacteristics ocsProductCharacteristics = apiClient.postingAllProductCharacteristics(allProductAPI);

        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductRepository);
        SqlBuilder updateBuilder = new SqlBuilder(jdbcProductRepository);
        SqlBuilder insertBuilderForProductDescription = new SqlBuilder(jdbcProductDescriptionRepository);

        List<OCSProduct> addedProducts = new ArrayList<>();
        List<ProductToProductDescription> productToProductDescriptionList = jdbcProductToProductDescription.findAll();
        boolean isThereProduct;
        int i = 0;

        logger.info("Loaded {} products from API", allProductAPI.size());
        logger.info("Loaded {} products from DB", productToProductDescriptionList.size());

        for (Result productAPI : allProductAPI) {
            isThereProduct = false;
            for (ProductToProductDescription productToProductDescription : productToProductDescriptionList) {
                if (productToProductDescription.getName().equals(productAPI.getProduct().getItemName())) {
                    isThereProduct = true;
                    if (productToProductDescription.getEan().equals(EAN_VALUE)) {
                        try {
                            updateBuilder.addRequest(createProductForUpdate(productToProductDescription.getProductId(), productAPI));
                            logger.info("Update product id {}", productToProductDescription.getProductId());
                        } catch (Exception e) {
                            logger.info("Failed update product id {}", productToProductDescription.getProductId());
                        }
                    }
                    i++;
                    break;
                }
            }

            if (!isThereProduct) {
                List<Image> imageList = ocsProductCharacteristics.getResult().get(i).getMediumImages();
                if (imageList == null || imageList.isEmpty() || productAPI.getPrice() == null) {
                    i++;
                    continue;
                }
                String image = uploadPhoto(imageList.get(0).getURL());
                if (image == null) {
                    i++;
                    continue;
                }
                List<Location> locationList = productAPI.getLocations();
                if (locationList == null || locationList.isEmpty()) {
                    i++;
                    continue;
                }
                if (productAPI.getPrice().getOrder() == null || productAPI.getPrice().getOrder().getValue() == 0) {
                    i++;
                    continue;
                }
                insertBuilder.addRequest(createProductForInsert(productAPI, image));
                addedProducts.add(productAPI.getProduct());
                i++;
                logger.info("Create product {}", productAPI.getProduct().getItemName());
            }
        }

        if (!insertBuilder.isEmpty()) {
            insertBuilder.insert();
            logger.info("Inserted new products");
        }
        if (!updateBuilder.isEmpty()) {
            updateBuilder.update();
            logger.info("Updated existing products");
        }

        List<Integer> productIds = jdbcProductRepository.getNewProductIdsByEAN(EAN_VALUE);
        i = 0;
        if (!addedProducts.isEmpty()) {
            for (OCSProduct product : addedProducts) {
                String itemName = product.getItemName().length() > 255 ? product.getItemName().substring(0, 255) : product.getItemName();
                insertBuilderForProductDescription.addRequest(
                        new Object[]{
                                productIds.get(i),
                                LANGUAGE_ID,
                                itemName,
                                product.getProductDescription(),
                                TAG_VALUE,
                                itemName,
                                itemName,
                                META_KEYWORD_VALUE,
                                itemName
                        }
                );
                addAllPhoto(ocsProductCharacteristics.getResult().get(i).getMediumImages(), productIds.get(i));
                logger.info("Added description and photos for product id {}", productIds.get(i));
                i++;
            }
        }

        if (!insertBuilderForProductDescription.isEmpty()) {
            insertBuilderForProductDescription.insert();
        }

        if (!productIds.isEmpty()) {
            assignmentOfStore(productIds);
            assignmentOfCategory(addedProducts, productIds);
            createAttribute(addedProducts, productIds, ocsProductCharacteristics);
        }

        checkForDelete(allProductAPI);
        logger.info("OCS sync finished");
    }

    // --- Все методы после sync() оставляем, добавляем logger ---
    private Object[] createProductForInsert(Result productAPI, String image) throws Exception {
        Object[] request = new Object[]{
                productAPI.getProduct().getPartNumber(),
                creatorOfSkuNumbers(productAPI.getProduct().getProductKey()),
                UPC_VALUE,
                EAN_VALUE,
                JAN_VALUE,
                ISBN_VALUE,
                MPN_VALUE,
                LOCATION_VALUE,
                productAPI.getLocations().get(0).getQuantity().getValue(),
                productAPI.getProduct().getStockStatus(productAPI.getLocations().get(0).getDescription()),
                image,
                VIDEO_VALUE,
                brandProductRatio(productAPI.getProduct().getProducer()),
                productAPI.getPrice().getOrder().getValue(),
                COST_VALUE,
                POINTS_VALUE,
                TAX_CLASS_ID_VALUE,
                new java.sql.Date(System.currentTimeMillis()),
                productAPI.getProduct().extractDimension("weight"),
                WEIGHT_CLASS_ID,
                productAPI.getProduct().extractDimension("depth"),
                productAPI.getProduct().extractDimension("width"),
                productAPI.getProduct().extractDimension("height"),
                LENGTH_CLASS_ID,
                SUBTRACT_VALUE,
                STATUS_VALUE,
                new java.sql.Timestamp(System.currentTimeMillis()),
                new java.sql.Timestamp(System.currentTimeMillis()),
                DN_ID,
                SUPPLIER_VALUE
        };
        logger.debug("Prepared insert request for product: {}", productAPI.getProduct().getItemName());
        return request;
    }

    private Object[] createProductForUpdate(int productId, Result productAPI) {
        Object[] request = new Object[]{
                productAPI.getLocations().get(0).getQuantity().getValue(),
                productAPI.getProduct().getStockStatus(productAPI.getLocations().get(0).getDescription()),
                productId
        };
        logger.debug("Prepared update request for product id: {}", productId);
        return request;
    }

    private int brandProductRatio(String name) {
        Manufacturer manufacturer = jdbcManufacturerRepository.find(name);
        if (manufacturer == null) {
            manufacturer = jdbcManufacturerRepository.create(new Manufacturer(0, name));
            logger.info("Created new manufacturer: {}", name);
        }
        return manufacturer.getManufacturerId();
    }

    private void createAttribute(List<OCSProduct> productList, List<Integer> productIds, OCSProductCharacteristics productCharacteristics) {
        if (productIds.isEmpty() || productList.isEmpty()) {
            return;
        }
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductAttributeRepository);
        int i = 0;
        for (OCSProduct product : productList) {
            List<OCSProperty> propertyList = findOCSPropertyByItemId(product.getItemID(), productCharacteristics);
            assert propertyList != null;
            for (OCSProperty property : propertyList) {
                List<AttributeDescription> attributeDescriptions = jdbcAttributeDescriptionRepository.findByName(product.getItemID());
                int attributeId;
                if (!attributeDescriptions.isEmpty()) {
                    attributeId = attributeDescriptions.get(0).getAttributeId();
                } else {
                    Attribute newAttribute = jdbcAttributeRepository.create(new Attribute(0, 1, 0));
                    AttributeDescription newAttributeDescription = jdbcAttributeDescriptionRepository.create(
                            new AttributeDescription(newAttribute.getAttributeId(), LANGUAGE_ID, property.getName()));
                    attributeId = newAttributeDescription.getAttributeId();
                    logger.info("Created new attribute: {} for product id {}", property.getName(), productIds.get(i));
                }
                String value = property.getUnit() != null ? property.getName() + " " + property.getUnit() : property.getValue();
                insertBuilder.addRequest(createRequestAttributeToProduct(value, attributeId, productIds.get(i)));
                logger.debug("Added attribute {} for product id {}", value, productIds.get(i));
            }
            i++;
        }
        if (!insertBuilder.isEmpty()) {
            insertBuilder.insert();
            logger.info("Inserted all attributes for products");
        }
    }

    private Object[] createRequestAttributeToProduct(String text, int attributeId, int productId) {
        return new Object[]{productId, attributeId, LANGUAGE_ID, text};
    }

    private void assignmentOfCategory(List<OCSProduct> productList, List<Integer> productIds) {
        if (productIds.isEmpty() || productList.isEmpty()) return;
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductToCategory);
        int i = 0;
        for (OCSProduct product : productList) {
            boolean isThereRequiredCategory = false;
            for (CatalogPath catalogPath : product.getCatalogPath()) {
                List<CategoryDescription> categoryDescriptionList = jdbcCategoryDescriptionRepository.findByName(catalogPath.getName());
                if (!categoryDescriptionList.isEmpty()) {
                    isThereRequiredCategory = true;
                    insertBuilder.addRequest(new Object[]{productIds.get(i), categoryDescriptionList.get(0).getCategoryId()});
                    logger.debug("Assigned product id {} to category {}", productIds.get(i), catalogPath.getName());
                    break;
                }
            }
            if (!isThereRequiredCategory) {
                insertBuilder.addRequest(new Object[]{productIds.get(i), 2106});
                logger.debug("Assigned product id {} to default category 2106", productIds.get(i));
            }
            i++;
        }
        insertBuilder.insert();
        logger.info("All categories assigned for products");
    }

    private Category createCategoryAndCategoryDescription(int parent_id, String name) {
        Category newCategory = jdbcCategoryRepository.create(new Category(0, parent_id, "", ""));
        jdbcCategoryDescriptionRepository.create(new CategoryDescription(
                newCategory.getCategoryId(),
                DESCRIPTION_VALUE,
                name,
                name
        ));
        return newCategory;
    }

    private void addAllPhoto(List<Image> imageList, int productId) throws Exception {
        if (imageList.size() <= 1) return;
        int sortOrder = 1;
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductImageRepository);
        for (int i = 1; i < imageList.size(); i++) {
            String image = uploadPhoto(imageList.get(i).getURL());
            if (image == null) continue;
            insertBuilder.addRequest(new Object[]{productId, image, sortOrder});
            logger.debug("Added image {} for product id {}", image, productId);
            sortOrder++;
        }
        if (!insertBuilder.isEmpty()) {
            insertBuilder.insert();
        }
    }

    private String uploadPhoto(String fileUrl) throws Exception {
        String locationPath = "/var/www/u3045843/data/www/germes.vip/image/catalog/ocs";
        String fileName = extractFileNameFromUrl(fileUrl);
        InputStream inputStream;
        try {
            inputStream = new URL(fileUrl).openStream();
            try {
                if (jschSftpClient.isFileExist(fileName, locationPath)) {
                    logger.debug("Image already exists: {}", fileName);
                    return "catalog/ocs/" + fileName;
                }
                jschSftpClient.uploadFile(inputStream, locationPath, fileName);
                logger.info("Uploaded image: {}", fileName);
            } catch (Exception e) {
                JschSftpClient newJschSftpClient = new JschSftpClient(configuration);
                jschSftpClient = newJschSftpClient;
                jschSftpClient.uploadFile(inputStream, locationPath, fileName);
                logger.info("Uploaded image after reconnect: {}", fileName);
            }
        } catch (IOException e) {
            logger.warn("Failed to load image from URL: {}", fileUrl);
            return null;
        }
        return "catalog/ocs/" + fileName;
    }

    private List<OCSProperty> findOCSPropertyByItemId(String itemId, OCSProductCharacteristics ocsProductCharacteristics) {
        for (ResultCharacteristics resultCharacteristics : ocsProductCharacteristics.getResult()) {
            if (resultCharacteristics.getItemID().equals(itemId)) {
                return resultCharacteristics.getProperties();
            }
        }
        return null;
    }

    private void assignmentOfStore(List<Integer> productIds) {
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductToStore);
        for (int productId : productIds) {
            insertBuilder.addRequest(new Object[]{productId, STORE_ID_VALUE});
            logger.debug("Assigned product id {} to store {}", productId, STORE_ID_VALUE);
        }
        insertBuilder.insert();
    }

    public static String extractFileNameFromUrl(String url) {
        if (url == null || url.isEmpty()) return "";
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private String creatorOfSkuNumbers(String sku) {
        return sku + "40";
    }

    private void checkForDelete(List<Result> allProductAPI) throws Exception {
        List<ProductImage> productImageList = new ArrayList<>();
        List<ProductToProductDescription> productToProductDescriptionList = jdbcProductToProductDescription.findAllByEAN(EAN_VALUE);
        List<Integer> productIdsToDelete = new ArrayList<>();
        boolean isThereProduct;

        for (ProductToProductDescription productToProductDescription : productToProductDescriptionList) {
            isThereProduct = false;
            for (Result result : allProductAPI) {
                if (productToProductDescription.getName().equals(result.getProduct().getProductName()) &&
                        productToProductDescription.getEan().equals(EAN_VALUE)) {
                    isThereProduct = true;
                    break;
                }
            }
            if (!isThereProduct) {
                productIdsToDelete.add(productToProductDescription.getProductId());
                List<ProductImage> currentProductImageList = jdbcProductImageRepository.find(productToProductDescription.getProductId());
                productImageList.addAll(currentProductImageList);
                logger.info("Marked product id {} for deletion", productToProductDescription.getProductId());
            }
        }

        jdbcProductRepository.deleteAllByRequest(productIdsToDelete);
        jdbcProductDescriptionRepository.deleteAllByRequest(productIdsToDelete);
        logger.info("Deleted products: {}", productIdsToDelete);

        try {
            deleteAllPhoto(productImageList);
        } catch (SftpException sftpException) {
            JschSftpClient newJschSftpClient = new JschSftpClient(configuration);
            jschSftpClient = newJschSftpClient;
            deleteAllPhoto(productImageList);
        }
    }

    private void deleteAllPhoto(List<ProductImage> allProductImage) throws Exception {
        String localPath = "/var/www/u3045843/data/www/germes.vip/image/catalog/ocs";
        for (ProductImage productImage : allProductImage) {
            String fileName = extractFileNameFromUrl(productImage.getImage());
            if (jschSftpClient.isFileExist(fileName)) {
                jschSftpClient.removeFile(fileName, localPath);
                logger.info("Deleted image: {}", fileName);
            }
//            try {
//                jschSftpClient.removeFile(fileName, localPath);
//            } catch (SftpException e) {
//                JschSftpClient newJschSftpClient = new JschSftpClient(configuration);
//                jschSftpClient = newJschSftpClient;
//                try {
//                    jschSftpClient.removeFile(fileName, localPath);
//                } catch (Exception ex) {
//                    continue;
//                }
//            }
        }
    }
}
