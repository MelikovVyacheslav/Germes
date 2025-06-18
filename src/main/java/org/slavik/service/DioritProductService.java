package org.slavik.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slavik.DioritB2B.DioritAPIClientImpl;
import org.slavik.repository.CategoryRepository;

public class DioritProductService implements ProductService {

    private final DioritAPIClientImpl apiClient;
    private final CategoryRepository productRepository;

    public DioritProductService(DioritAPIClientImpl apiClient, CategoryRepository productRepository) {
        this.apiClient = apiClient;
        this.productRepository = productRepository;
    }

    @Override
    public void sync() throws JsonProcessingException {
        
    }
}
