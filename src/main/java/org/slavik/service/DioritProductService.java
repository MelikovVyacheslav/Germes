package org.slavik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slavik.DioritB2B.DioritAPIClientImpl;
import org.slavik.DioritB2B.model.Datum;
import org.slavik.entity.product.Product;
import org.slavik.entity.product.ProductDescription;
import org.slavik.repository.JdbcProductDescriptionRepository;
import org.slavik.repository.JdbcProductRepository;
import org.slavik.repository.ProductDescriptionRepository;
import org.slavik.repository.ProductRepository;

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

    @Override
    public void sync() throws JsonProcessingException {
        List<Datum> allProductListAPI = apiClient.getAllProduct();
        List<ProductDescription> allProductListDataBase = jdbcProductDescriptionRepository.findAll();
        boolean isThereProduct = false;
        for (Datum productAPI : allProductListAPI) {
            for (ProductDescription productDescription : allProductListDataBase) {
                if (productDescription.getProductId() == productAPI.getProductId()) {
                    isThereProduct = true;
                    break;
                }
            }
        }
    }
}
