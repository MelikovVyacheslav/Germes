package org.slavik;

import org.slavik.breez.BreezApiClientImpl;
import org.slavik.breez.model.BreezProductResponse;
import org.slavik.dioritB2B.APISourceConfiguration;
import org.slavik.entity.category.Category;
import org.slavik.ocs.OCSAPIClientImpl;
import org.slavik.repository.category.JdbcCategoryDescriptionRepository;
import org.slavik.repository.category.JdbcCategoryRepository;
import org.slavik.repository.product.JdbcProductDescriptionRepository;
import org.slavik.repository.product.JdbcProductRepository;
import org.slavik.repository.product.JdbcProductToCategoryRepository;
import org.slavik.service.OcsProductService;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {

        ConnectionManager connectionManager = new ConnectionManager(
                "jdbc:mysql://localhost:3306/Test_db",
                "root",
                "12345678"
        );
        connectionManager.connection();

        WebClientConfiguration webClientConfiguration = new WebClientConfiguration(new APISourceConfiguration(
                "https://connector.b2b.ocs.ru/api/v2",
                "TSWJXggwvt59l9nuYVvtSM?iyea0DR",
                "X-API-Key",
                100 * 1024 * 1024
        ));


        DataSource dataSource = connectionManager.connection();
        NamedParameterJdbcOperations jdbcOperations = new NamedParameterJdbcTemplate(dataSource);
        OCSAPIClientImpl apiClient = new OCSAPIClientImpl(webClientConfiguration.getAPIWebClient());
        JdbcCategoryDescriptionRepository jdbcCategoryDescriptionRepository = new JdbcCategoryDescriptionRepository(jdbcOperations);
        JdbcCategoryRepository jdbcCategoryRepository = new JdbcCategoryRepository(jdbcOperations);
        JdbcProductDescriptionRepository descriptionRepo = new JdbcProductDescriptionRepository(jdbcOperations);
        JdbcProductRepository productRepo = new JdbcProductRepository(jdbcOperations);
        JdbcProductToCategoryRepository ProductToCategory = new JdbcProductToCategoryRepository(jdbcOperations);
        OcsProductService ocsProductService = new OcsProductService(apiClient, descriptionRepo, productRepo, ProductToCategory, jdbcCategoryRepository, jdbcCategoryDescriptionRepository);// бери конкретный, который хочешь
        ocsProductService.sync();

    }
}