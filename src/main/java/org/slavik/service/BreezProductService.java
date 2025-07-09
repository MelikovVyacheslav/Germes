package org.slavik.service;

import org.slavik.breez.BreezApiClientImpl;
import org.slavik.breez.model.BreezProductResponse;
import org.slavik.connector.JschSftpClient;
import org.slavik.repository.attribute.JdbcAttributeDescriptionRepository;
import org.slavik.repository.attribute.JdbcAttributeRepository;
import org.slavik.repository.manufacturer.JdbcManufacturerRepository;
import org.slavik.repository.product.JdbcProductAttributeRepository;
import org.slavik.repository.product.JdbcProductDescriptionRepository;
import org.slavik.repository.product.JdbcProductRepository;
import org.slavik.repository.product.JdbcProductToCategoryRepository;

import java.io.IOException;
import java.util.Map;

public class BreezProductService implements ProductService {

    private final BreezApiClientImpl apiClient;
    private final JdbcProductDescriptionRepository jdbcProductDescriptionRepository;
    private final JdbcProductRepository jdbcProductRepository;
    private final JdbcProductToCategoryRepository jdbcProductToCategory;
    private final JdbcManufacturerRepository jdbcManufacturerRepository;
    private final JdbcAttributeRepository jdbcAttributeRepository;
    private final JdbcAttributeDescriptionRepository jdbcAttributeDescriptionRepository;
    private final JdbcProductAttributeRepository jdbcProductAttributeRepository;
    private final JschSftpClient jschSftpClient;

    public BreezProductService(BreezApiClientImpl apiClient,
                                JdbcProductDescriptionRepository jdbcProductDescriptionRepository,
                                JdbcProductRepository jdbcProductRepository,
                                JdbcProductToCategoryRepository jdbcProductToCategory,
                                JdbcManufacturerRepository jdbcManufacturerRepository,
                                JdbcAttributeRepository jdbcAttributeRepository,
                                JdbcAttributeDescriptionRepository jdbcAttributeDescriptionRepository,
                                JdbcProductAttributeRepository jdbcProductAttributeRepository, JschSftpClient jschSftpClient) {
        this.apiClient = apiClient;
        this.jdbcProductDescriptionRepository = jdbcProductDescriptionRepository;
        this.jdbcProductRepository = jdbcProductRepository;
        this.jdbcProductToCategory = jdbcProductToCategory;
        this.jdbcManufacturerRepository = jdbcManufacturerRepository;
        this.jdbcAttributeRepository = jdbcAttributeRepository;
        this.jdbcAttributeDescriptionRepository = jdbcAttributeDescriptionRepository;
        this.jdbcProductAttributeRepository = jdbcProductAttributeRepository;
        this.jschSftpClient = jschSftpClient;
    }

    @Override
    public void sync() throws IOException {
        Map<String, BreezProductResponse> breezProductResponseHashMap = apiClient.getAllProducts();
        for (Map.Entry<String, BreezProductResponse> products : breezProductResponseHashMap.entrySet()) {

        }
    }
}
