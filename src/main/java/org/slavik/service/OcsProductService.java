package org.slavik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slavik.DioritB2B.DioritAPIClientImpl;
import org.slavik.OCS.OCSAPIClientImpl;
import org.slavik.entity.product.Product;
import org.slavik.entity.product.ProductDescription;
import org.slavik.repository.JdbcProductDescriptionRepository;
import org.slavik.repository.JdbcProductRepository;

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

    @Override
    public void sync() throws JsonProcessingException {
//        List<Product> allProductListAPI = apiClient.getAll();
//        List<ProductDescription> allProductListDataBase = jdbcProductDescriptionRepository.findAll();
//        boolean isThereProduct = false;
//        for (Product productAPI : allProductListAPI) {
//            for (ProductDescription productDescription : allProductListDataBase) {
//                if (productAPI.getProductId() == productDescription.getProductId()) {
//                    isThereProduct = true;
//                    break;
//                }
//            }
//            if (!isThereProduct) {
//                Product newProduct = jdbcProductRepository.create(productAPI);
              /////  jdbcProductDescriptionRepository.create();
//            }
//        }
    }
}
