package org.slavik.service;

import org.slavik.DioritB2B.DioritAPIClientImpl;
import org.slavik.DioritB2B.model.Datum;
import org.slavik.entity.product.Product;
import org.slavik.entity.product.ProductDescription;
import org.slavik.repository.JdbcProductDescriptionRepository;
import org.slavik.repository.JdbcProductRepository;

import java.util.List;

public class DioritProductService implements ProductService {

    private final DioritAPIClientImpl apiClient;
    private final JdbcProductDescriptionRepository jdbcProductDescriptionRepository;
    private final JdbcProductRepository jdbcProductRepository;

    public DioritProductService(DioritAPIClientImpl apiClient,
                                JdbcProductDescriptionRepository jdbcProductDescriptionRepository,
                                JdbcProductRepository jdbcProductRepository) {
        this.apiClient = apiClient;
        this.jdbcProductDescriptionRepository = jdbcProductDescriptionRepository;
        this.jdbcProductRepository = jdbcProductRepository;
    }

    private final int MANUFACTURER_ID = 1;
    private final java.sql.Date CURRENT_DATE = new java.sql.Date(System.currentTimeMillis());
    private final int WEIGHT_CLASS_ID = 0;
    private final int LENGTH_CLASS_ID = 0;
    private final int STATUS_VALUE = 6;
    private final int DN_ID = 0;
    @Override
    public void sync() {
        List<Datum> allProductAPI = apiClient.getAllProduct();
        List<ProductDescription> allProductDescriptionDataBase = jdbcProductDescriptionRepository.findAll();
        boolean isThereProduct;
        for (Datum productAPI : allProductAPI) {
            isThereProduct = false;
            for (ProductDescription productDescription : allProductDescriptionDataBase) {
                if (productAPI.getName().equals(productDescription.getName())) {
                    isThereProduct = true;
                    break;
                }
            }
            if (!isThereProduct) {
                int idNewProduct = jdbcProductRepository.gettingProductIdForNewProduct();
                jdbcProductRepository.create(new Product(
                        idNewProduct,
                        productAPI.getSku(),
                        productAPI.getSku(),
                        "dioritb2b",
                        productAPI.getStock(),
                        6,
                        productAPI.getMainPhoto(),
                        MANUFACTURER_ID,
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
                        idNewProduct,
                        productAPI.getName(),
                        productAPI.getDescription()
                ));
            }
        }
    }
}
