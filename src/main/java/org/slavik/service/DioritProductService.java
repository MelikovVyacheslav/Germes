package org.slavik.service;

import org.slavik.DioritB2B.DioritAPIClientImpl;
import org.slavik.DioritB2B.model.DioritProduct;
import org.slavik.DioritB2B.model.ShortProduct;
import org.slavik.entity.manufacturer.Manufacturer;
import org.slavik.entity.product.*;
import org.slavik.repository.*;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public class DioritProductService implements ProductService {

    private final DioritAPIClientImpl apiClient;
    private final JdbcProductDescriptionRepository jdbcProductDescriptionRepository;
    private final JdbcProductRepository jdbcProductRepository;
    private final JdbcProductToCategoryRepository jdbcProductToCategory;
    private final JdbcManufacturerRepository jdbcManufacturerRepository;
    private final JdbcProductToStoreRepository jdbcProductToStoreRepository;
    private final JdbcProductToLayoutRepository jdbcProductToLayoutRepository;

    public DioritProductService(DioritAPIClientImpl apiClient,
                                JdbcProductDescriptionRepository jdbcProductDescriptionRepository,
                                JdbcProductRepository jdbcProductRepository,
                                JdbcProductToCategoryRepository jdbcProductToCategory,
                                JdbcManufacturerRepository jdbcManufacturerRepository, JdbcProductToStoreRepository jdbcProductToStoreRepository, JdbcProductToLayoutRepository jdbcProductToLayoutRepository) {
        this.apiClient = apiClient;
        this.jdbcProductDescriptionRepository = jdbcProductDescriptionRepository;
        this.jdbcProductRepository = jdbcProductRepository;
        this.jdbcProductToCategory = jdbcProductToCategory;
        this.jdbcManufacturerRepository = jdbcManufacturerRepository;
        this.jdbcProductToStoreRepository = jdbcProductToStoreRepository;
        this.jdbcProductToLayoutRepository = jdbcProductToLayoutRepository;
    }

    private final Date CURRENT_DATE = new Date(System.currentTimeMillis());
    private final int WEIGHT_CLASS_ID = 0;
    private final int LENGTH_CLASS_ID = 0;
    private final int STATUS_VALUE = 6;
    private final int DN_ID = 0;
<<<<<<< HEAD
=======
    private final int STOCK_STATUS = 6;
    private final int STORE_ID = 0;
    private final int LAYOUT_ID = 0;

>>>>>>> f12d2b8978d5ca5d16d766e4ea360f7fd86137d3

    @Override
    public void sync() {
        List<ShortProduct> allProductAPI = apiClient.getAllProduct();
        List<ProductDescription> allProductDescriptionDataBase = jdbcProductDescriptionRepository.findAll();
        boolean isThereProduct;
        int productId = 0;
        for (ShortProduct productAPI : allProductAPI) {
            isThereProduct = false;
            for (ProductDescription productDescription : allProductDescriptionDataBase) {
                if (productAPI.getName().equals(productDescription.getName())) {
                    productId = productDescription.getProductId();
                    isThereProduct = true;
                    break;
                }
            }
            if (isThereProduct) {
                jdbcProductRepository.update(new Product(
                        productId,
                        creatorOfSkuNumbers(productAPI.getSku()),
                        creatorOfSkuNumbers(productAPI.getSku()),
                        "dioritb2b",
                        productAPI.getStock(),
                        STOCK_STATUS,
                        productAPI.getMainPhoto(),
                        brandProductRatio(productAPI.getID()),
                        productAPI.getPrice(),
                        CURRENT_DATE,
                        productAPI.getWeight(),
                        WEIGHT_CLASS_ID,
                        productAPI.getLength(),
                        productAPI.getWidth(),
                        productAPI.getHeight(),
                        LENGTH_CLASS_ID,
                        productAPI.getStock(),
                        STATUS_VALUE,
                        CURRENT_DATE,
                        CURRENT_DATE,
                        DN_ID
                ));
                jdbcProductToLayoutRepository.create(new ProductToLayout(
                        productId,
                        STORE_ID,
                        LAYOUT_ID
                ));
                System.out.println("Update " + productId);
            } else {
                Product product = jdbcProductRepository.create(new Product(
                        0,
                        creatorOfSkuNumbers(productAPI.getSku()),
                        creatorOfSkuNumbers(productAPI.getSku()),
                        "dioritb2b",
                        productAPI.getStock(),
                        6,
                        productAPI.getMainPhoto(),
                        brandProductRatio(productAPI.getID()),
                        productAPI.getPrice(),
                        CURRENT_DATE,
                        productAPI.getWeight(),
                        WEIGHT_CLASS_ID,
                        productAPI.getLength(),
                        productAPI.getWidth(),
                        productAPI.getHeight(),
                        LENGTH_CLASS_ID,
                        productAPI.getStock(),
                        STATUS_VALUE,
                        CURRENT_DATE,
                        CURRENT_DATE,
                        DN_ID
                ));
                jdbcProductDescriptionRepository.create(new ProductDescription(
                        jdbcProductRepository.gettingProductIdForNewProduct(),
                        productAPI.getName(),
                        productAPI.getDescription()
                ));
                syncTableProductToCategory(product.getProductId());
                jdbcProductRepository.createProductImage(productAPI, product);
                jdbcProductToStoreRepository.create(new ProductToStore(
                        product.getProductId(),
                        STORE_ID
                ));
                jdbcProductToLayoutRepository.create(new ProductToLayout(
                        product.getProductId(),
                        STORE_ID,
                        LAYOUT_ID
                ));
                System.out.println("Create " + product.getProductId());
            }
        }
    }

    private int brandProductRatio(UUID productId) {
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

    public void syncTableProductToCategory(int productId) {
        List<ProductToCategory> allProductToCategory = jdbcProductToCategory.findAll();
        jdbcProductToCategory.create(new ProductToCategory(
                productId,
                2012
        ));
    }

    private String creatorOfSkuNumbers(String sku) {
        return sku + "30";
    }


}
