package org.slavik.service;

import org.slavik.DioritB2B.DioritAPIClientImpl;
import org.slavik.DioritB2B.model.Datum;
import org.slavik.entity.product.Product;
import org.slavik.entity.product.ProductDescription;
import org.slavik.entity.product.ProductToCategory;
import org.slavik.repository.JdbcProductDescriptionRepository;
import org.slavik.repository.JdbcProductRepository;
import org.slavik.repository.JdbcProductToCategory;
import org.slavik.repository.ProductToCategoryRepository;

import java.sql.Date;
import java.util.List;

public class DioritProductService implements ProductService {

    private final DioritAPIClientImpl apiClient;
    private final JdbcProductDescriptionRepository jdbcProductDescriptionRepository;
    private final JdbcProductRepository jdbcProductRepository;
    private final JdbcProductToCategory jdbcProductToCategory;

    public DioritProductService(DioritAPIClientImpl apiClient,
                                JdbcProductDescriptionRepository jdbcProductDescriptionRepository,
                                JdbcProductRepository jdbcProductRepository, JdbcProductToCategory jdbcProductToCategory) {
        this.apiClient = apiClient;
        this.jdbcProductDescriptionRepository = jdbcProductDescriptionRepository;
        this.jdbcProductRepository = jdbcProductRepository;
        this.jdbcProductToCategory = jdbcProductToCategory;
    }

    private final int MANUFACTURER_ID = 1;
    private final Date CURRENT_DATE = new Date(System.currentTimeMillis());
    private final int WEIGHT_CLASS_ID = 0;
    private final int LENGTH_CLASS_ID = 0;
    private final int STATUS_VALUE = 6;
    private final int DN_ID = 0;
    private final int STOCK_STATUS = 6;
    private final double PERCENT_VALUE = 20;

    @Override
    public void sync() {
        List<Datum> allProductAPI = apiClient.getAllProduct();
        List<ProductDescription> allProductDescriptionDataBase = jdbcProductDescriptionRepository.findAll();
        boolean isThereProduct;
        int productId = 0;
        for (Datum productAPI : allProductAPI) {
            isThereProduct = false;
            for (ProductDescription productDescription : allProductDescriptionDataBase) {
                if (productAPI.getName().equals(productDescription.getName())) {
                    productId = productDescription.getProductId();
                    isThereProduct = true;
                    break;
                }
            }
            if (isThereProduct) {
                System.out.println("Update");
                jdbcProductRepository.update(new Product(
                        productId,
                        productAPI.getSku(),
                        productAPI.getSku(),
                        "dioritb2b",
                        productAPI.getStock(),
                        6,
                        productAPI.getMainPhoto(),
                        MANUFACTURER_ID,
                        calculationOfNewPrice(productAPI.getPrice()),
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
                jdbcProductDescriptionRepository.update(new ProductDescription(
                        productId,
                        productAPI.getName(),
                        productAPI.getDescription()
                ));
            } else {
                int newProductId = jdbcProductRepository.gettingProductIdForNewProduct();
                jdbcProductRepository.createProductImage(productAPI, newProductId);
                jdbcProductRepository.create(new Product(
                        newProductId,
                        productAPI.getSku(),
                        productAPI.getSku(),
                        "dioritb2b",
                        productAPI.getStock(),
                        STOCK_STATUS,
                        productAPI.getMainPhoto(),
                        MANUFACTURER_ID,
                        calculationOfNewPrice(productAPI.getPrice()),
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
                        newProductId,
                        productAPI.getName(),
                        productAPI.getDescription()
                ));
            }
        }
    }

    public void syncTableProductToCategory() {
        List<Product> allProduct = jdbcProductRepository.findAll();
        List<ProductToCategory> allProductToCategory = jdbcProductToCategory.findAll();
        for (Product product : allProduct) {
            if (product.getEan().equals("dioritb2b")) {
                jdbcProductToCategory.create(new ProductToCategory(
                        product.getProductId(),
                        2012
                ));
            }
        }
    }

    private int calculationOfNewPrice(int price) {
        return (int) (price + ((price * PERCENT_VALUE) /100));
    }
}
