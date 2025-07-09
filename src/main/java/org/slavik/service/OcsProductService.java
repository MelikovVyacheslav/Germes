package org.slavik.service;


import org.slavik.ocs.OCSAPIClientImpl;
import org.slavik.ocs.model.*;
import org.slavik.entity.product.Product;
import org.slavik.entity.product.ProductDescription;
import org.slavik.repository.JdbcProductDescriptionRepository;
import org.slavik.repository.JdbcProductRepository;

import java.sql.Date;
import java.util.List;

public class OcsProductService implements ProductService {
    private final OCSAPIClientImpl apiClient;
    private final JdbcProductDescriptionRepository jdbcProductDescriptionRepository;
    private final JdbcProductRepository jdbcProductRepository;

    public OcsProductService(OCSAPIClientImpl apiClient,
                             JdbcProductDescriptionRepository jdbcProductDescriptionRepository,
                             JdbcProductRepository jdbcProductRepository) {

        this.apiClient = apiClient;
        this.jdbcProductDescriptionRepository = jdbcProductDescriptionRepository;
        this.jdbcProductRepository = jdbcProductRepository;
    }

    private final int MANUFACTURER_ID = 1;
    private final Date CURRENT_DATE = new Date(System.currentTimeMillis());
    private final int WEIGHT_CLASS_ID = 0;
    private final int LENGTH_CLASS_ID = 0;
    private final int STATUS_VALUE = 6;
    private final int DN_ID = 0;

    @Override
<<<<<<< Updated upstream
    public void sync() throws JsonProcessingException {
=======
    public void sync() {
>>>>>>> Stashed changes
        List<Result> allProductAPI = apiClient.getAll();
        List<ProductDescription> allProductDescriptionDataBase = jdbcProductDescriptionRepository.findAll();
        boolean isThereProduct;
        int productId = 0;
        for (Result productAPI : allProductAPI) {
            if (productAPI.getLocations() == null || productAPI.getLocations().isEmpty()) {
                continue;
            }
            Location location = productAPI.getLocations().getFirst();
            String description = location.getDescription();
            isThereProduct = false;
            for (ProductDescription productDescription : allProductDescriptionDataBase) {
                if (productAPI.getProduct().getProductName().equals(productDescription.getName())) {
                    productId = productDescription.getProductId();
                    isThereProduct = true;
                    break;
                }
            }
            if (productAPI.getPrice() == null) {
                continue;
            }
            int price = (int) productAPI.getPrice().getPriceList().getValue();

            if (isThereProduct) {
                System.out.println("Update");
                jdbcProductRepository.update(new Product(
                        productId,
                        productAPI.getProduct().getProductKey(),
                        productAPI.getProduct().getProductKey(),
                        "OCS",
                        location.getQuantity().getValue(),
                        productAPI.getProduct().getStockStatus(description),
                        null,
                        MANUFACTURER_ID,
                        price,
                        CURRENT_DATE,
                        productAPI.getPackageInformation().getWeight(),
                        WEIGHT_CLASS_ID,
                        productAPI.getPackageInformation().getDepth(),
                        productAPI.getPackageInformation().getWidth(),
                        productAPI.getPackageInformation().getHeight(),
                        LENGTH_CLASS_ID,
                        0,
                        STATUS_VALUE,
                        CURRENT_DATE,
                        CURRENT_DATE,
                        DN_ID
                ));
                jdbcProductDescriptionRepository.update(new ProductDescription(
                        productId,
                        productAPI.getProduct().getProductName(),
                        productAPI.getProduct().getProductDescription()
                ));
            } else {
                int newProductId = jdbcProductRepository.gettingProductIdForNewProduct();
                jdbcProductRepository.create(new Product(
                        newProductId,
                        productAPI.getProduct().getProductKey(),
                        productAPI.getProduct().getProductKey(),
                        "OCS",
                        location.getQuantity().getValue(),
                        productAPI.getProduct().getStockStatus(description),
                        null,
                        MANUFACTURER_ID,
                        price,
                        CURRENT_DATE,
                        productAPI.getPackageInformation().getWeight(),
                        WEIGHT_CLASS_ID,
                        productAPI.getPackageInformation().getDepth(),
                        productAPI.getPackageInformation().getWidth(),
                        productAPI.getPackageInformation().getHeight(),
                        LENGTH_CLASS_ID,
                        0,
                        STATUS_VALUE,
                        CURRENT_DATE,
                        CURRENT_DATE,
                        DN_ID
                ));
                jdbcProductDescriptionRepository.create(new ProductDescription(
                        newProductId,
                        productAPI.getProduct().getProductName(),
                        productAPI.getProduct().getProductDescription()
                ));
            }

        }
    }
}
