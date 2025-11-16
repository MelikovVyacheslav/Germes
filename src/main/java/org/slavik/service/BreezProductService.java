package org.slavik.service;

import com.jcraft.jsch.SftpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.slavik.breez.BreezApiClientImpl;
import org.slavik.breez.model.*;
import org.slavik.builder.SqlBuilder;
import org.slavik.connector.JschSftpClient;
import org.slavik.connector.SftpClientProperties;
import org.slavik.entity.attribute.Attribute;
import org.slavik.entity.attribute.AttributeDescription;
import org.slavik.entity.category.CategoryDescription;
import org.slavik.entity.manufacturer.Manufacturer;
import org.slavik.entity.product.Product;
import org.slavik.entity.product.ProductImage;
import org.slavik.entity.product.ProductToProductDescription;
import org.slavik.repository.attribute.JdbcAttributeDescriptionRepository;
import org.slavik.repository.attribute.JdbcAttributeRepository;
import org.slavik.repository.category.JdbcCategoryDescriptionRepository;
import org.slavik.repository.manufacturer.JdbcManufacturerRepository;
import org.slavik.repository.product.*;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class BreezProductService implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(BreezProductService.class);

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
    private final int STORE_ID_VALUE = 0;

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
        logger.info("Breez sync started.");

        Map<String, BreezProductResponse> allProductAPI = apiClient.getAllProducts();
        Map<String, BreezBrand> breezBrandMap = apiClient.getAllBrands();
        Map<String, BreezCategory> breezCategoryMap = apiClient.getAllCategories();

        List<BreezProductResponse> addedProducts = new ArrayList<>();
        List<String> addedKeys = new ArrayList<>();
        List<ProductToProductDescription> productToProductDescriptionList = jdbcProductToProductDescription.findAll();


        logger.info("Loaded {} products from API", allProductAPI.size());
        logger.info("Loaded {} products from DB", productToProductDescriptionList.size());

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
                    if (productToProductDescription.getEan().equals(EAN_VALUE)) {
                        updateBuilder.addRequest(createProductForUpdate(productToProductDescription.getProductId(), productAPI.getValue()));
                        logger.info("Update product id {}", productToProductDescription.getProductId());
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
                    logger.debug("Skipping product {} due to missing data.", productAPI.getValue().getArticul());
                    continue;
                }
                insertBuilder.addRequest(request);
                addedProducts.add(productAPI.getValue());
                addedKeys.add(productAPI.getKey());
                logger.info("Create product {}", productAPI.getValue().getTitle());
            }
        }

        if (!insertBuilder.isEmpty()) {
            insertBuilder.insert();
            logger.info("Inserted {} new products.", addedProducts.size());
        }
        if (!updateBuilder.isEmpty()) {
            updateBuilder.update();
            logger.info("Updated existing products.");
        }

        List<Integer> productIds = jdbcProductRepository.getNewProductIdsByEAN(EAN_VALUE);
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
                });
                addAllPhoto(product.getImages(), productIds.get(i));
                logger.info("Added description and photos for product id {}", productIds.get(i));
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

        jdbcProductRepository.deleteAllByEanWhereQuantityZero(EAN_VALUE);
        logger.info("Breez sync finished.");
    }

    // --- методы с логами для BreezProductService ---

    private Object[] createRequestProduct(BreezProductResponse product, Map<String, BreezBrand> breezBrandMap) throws Exception {
        int brandId = brandProductRatio(product, breezBrandMap);
        if (brandId == 0) {
            logger.debug("Brand not found for product {}", product.getArticul());
            return null;
        }

        int price = product.getPrice().getRic();
        if (price == 0) {
            logger.debug("Price is 0 for product {}", product.getArticul());
            return null;
        }

        String image = uploadPhoto(product.getImages().get(0));
        if (image == null) {
            logger.debug("Failed to upload main photo for product {}", product.getArticul());
            return null;
        }

        int[] stock = assignmentOfStockStatus(product.getNc());
        if (stock.length == 0 || stock[0] == 0) {
            logger.debug("Stock unavailable for product {}", product.getArticul());
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
        List<BreezStockInfo> breezStockInfoList;
        try {
            breezStockInfoList = apiClient.gettingWarehousesWhereTheProductAreLocated(nc);
        } catch (Exception e) {
            logger.warn("API connection error for NC: {}, reinitializing client. Error: {}", nc, e.getMessage());
            try {
                BreezApiClientImpl newApiClient = new BreezApiClientImpl(apiClient.getWebClient());
                apiClient = newApiClient;
                breezStockInfoList = apiClient.gettingWarehousesWhereTheProductAreLocated(nc);
                logger.info("Successfully reinitialized API client for NC: {}", nc);
            } catch (Exception retryException) {
                logger.error("Failed after reconnection for NC: {}, error: {}", nc, retryException.getMessage());
                return assignmentOfStockStatus(nc);
            }
        }
        if (breezStockInfoList.isEmpty()) {
            return new int[]{0, 0};
        }
        for (BreezStockInfo breezStockInfo : breezStockInfoList) {
            if (breezStockInfo.getQuantity() > 0) {
                if (breezStockInfo.getStock().equals("РРЦ Бриз Ростов LV")) {
                    return new int[]{6, breezStockInfo.getQuantity()};
                } else if (breezStockInfo.getStock().equals("Бриз Пятигорск")) {
                    return new int[]{7, breezStockInfo.getQuantity()};
                } else return new int[]{9, breezStockInfo.getQuantity()};
            }
        }
        return new int[]{};
    }

    private Object[] createProductForUpdate(int productId, BreezProductResponse product) throws Exception {
        int[] stock = assignmentOfStockStatus(product.getNc());
        if (stock.length == 0) {
            logger.info("Product {} stock is 0, setting quantities to 0", product.getArticul());
            return new Object[]{0, 0, productId};
        }
        return new Object[]{stock[1], stock[0], productId};
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
                logger.info("Reinitialized API client for product characteristics.");
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
                            logger.info("Added attribute {} for product id {}", techEntry.getValue().getTitle(), productIds.get(i));
                        }
                    } else {
                        Attribute newAttribute = jdbcAttributeRepository.create(new Attribute(0, 1, 0));
                        AttributeDescription newAttributeDescription =
                                jdbcAttributeDescriptionRepository.create(new AttributeDescription(
                                        newAttribute.getAttributeId(),
                                        LANGUAGE_ID,
                                        techEntry.getValue().getTitle()
                                ));
                        insertBuilderForAttribute.addRequest(createRequestAttributeToProduct(
                                techEntry.getValue().getValue() + " " + techEntry.getValue().getUnit(),
                                newAttributeDescription.getAttributeId(),
                                productIds.get(i)
                        ));
                        attributeList.add(techEntry.getValue().getValue());
                        logger.info("Created new attribute {} for product id {}", techEntry.getValue().getTitle(), productIds.get(i));
                    }
                }
                i++;
            } catch (NullPointerException e) {
                jdbcProductRepository.delete(productIds.get(i));
                jdbcProductDescriptionRepository.delete(productIds.get(i));
                logger.warn("Deleted product id {} due to missing attributes", productIds.get(i));
                i++;
            }
            attributeList = new ArrayList<>();
        }

        if (!insertBuilderForAttribute.isEmpty()) {
            insertBuilderForAttribute.insert();
            logger.info("Inserted all attributes for products.");
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
            logger.info("Assigned category for product id {}", productIds.get(i));
            i++;
        }
        insertBuilder.insert();
    }

    private int brandProductRatio(BreezProductResponse product, Map<String, BreezBrand> brandList) throws IOException, InterruptedException {
        if (product == null || brandList == null) return 0;

        BreezBrand brand = brandList.get(product.getBrand());
        if (brand == null) return 0;

        Manufacturer manufacturer = jdbcManufacturerRepository.find(brand.getTitle());
        if (manufacturer == null) {
            Manufacturer newManufacturer = jdbcManufacturerRepository.create(new Manufacturer(0, brand.getTitle()));
            logger.info("Created new manufacturer {}", brand.getTitle());
            return newManufacturer.getManufacturerId();
        }
        return manufacturer.getManufacturerId();
    }

    private void addAllPhoto(List<String> images, int productId) throws Exception {
        if (images.size() == 1) return;

        int sortOrder = 1;
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductImageRepository);
        for (int i = 1; i < images.size(); i++) {
            String image = uploadPhoto(images.get(i));
            if (image == null) continue;
            insertBuilder.addRequest(new Object[]{productId, image, sortOrder});
            logger.info("Uploaded photo {} for product id {}", image, productId);
            sortOrder++;
        }
        if (!insertBuilder.isEmpty()) {
            insertBuilder.insert();
        }
    }

    private void assignmentOfStore(List<Integer> productIds) {
        SqlBuilder insertBuilder = new SqlBuilder(jdbcProductToStore);
        for (int productId : productIds) {
            insertBuilder.addRequest(new Object[]{productId, STORE_ID_VALUE});
            logger.info("Assigned store for product id {}", productId);
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
                    logger.debug("File {} already exists on server.", fileName);
                    return "catalog/breez/" + fileName;
                }
                jschSftpClient.uploadFile(inputStream, locationPath, fileName);
                logger.info("Uploaded file {}", fileName);
            } catch (Exception e) {
                JschSftpClient newJschSftpClient = new JschSftpClient(configuration);
                jschSftpClient = newJschSftpClient;
                if (jschSftpClient.isFileExist(fileName, locationPath)) {
                    return "catalog/breez/" + fileName;
                }
                jschSftpClient.uploadFile(inputStream, locationPath, fileName);
                logger.info("Reconnected SFTP and uploaded file {}", fileName);
            }
        } catch (IOException e) {
            logger.error("Failed to read URL {}", fileUrl);
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
        boolean isDelete;
        int productIdForDelete = 0;
        int i = 0;
        for (Map.Entry<String, BreezProductResponse> productAPI : allProductAPI.entrySet()) {
            isDelete = false;
            for (ProductToProductDescription productToProductDescription : productToProductDescriptionList) {
                if (productList.get(i).getQuantity() == 0 || productList.get(i).getStockStatusId() == 0) {
                    isDelete = true;
                    productIdForDelete = productToProductDescription.getProductId();
                    break;
                }
                i++;
            }
            if (isDelete) {
                productIdsToDelete.add(productIdForDelete);
                List<ProductImage> currentProductImageList = jdbcProductImageRepository.find(productIdForDelete);
                productImageList.addAll(currentProductImageList);
            }
            i = 0;
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
