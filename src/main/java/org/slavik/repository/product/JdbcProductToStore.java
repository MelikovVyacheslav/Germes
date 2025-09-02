package org.slavik.repository.product;

import org.slavik.entity.product.ProductToStore;
import org.slavik.repository.OperationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbcProductToStore implements ProductToStoreRepository, OperationRepository {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final JdbcTemplate jdbcTemplate;

    public JdbcProductToStore(NamedParameterJdbcOperations jdbcOperations, JdbcTemplate jdbcTemplate) {
        this.jdbcOperations = jdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ProductToStore> findAll() {
        String sql = """
                select * from oc_product_to_store;
                """;
        List<ProductToStore> productToStoreList = jdbcOperations.query(sql, new MapSqlParameterSource(), new ProductToStore.Mapper());
        return productToStoreList;
    }

    @Override
    public ProductToStore find(int productId) {
        String sql = """
                select * from oc_product_to_store
                where product_id = :productId;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        ProductToStore productToStore = jdbcOperations.queryForObject(sql, params, new ProductToStore.Mapper());
        return productToStore;
    }

    @Override
    public ProductToStore create(ProductToStore productToStore) {
        String sql = """
                insert into oc_product_to_store(product_id, store_id) values
                (:productId, :storeId);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productToStore.getProductId());
        params.addValue("storeId", productToStore.getStoreId());
        jdbcOperations.update(sql, params);
        return null;
    }

    @Override
    public void createAllByRequest(List<Object[]> values) {
        String sql = "insert into oc_product_to_store(product_id, store_id) values (?, ?)";
        jdbcTemplate.batchUpdate(sql, values);
    }

    @Override
    public void updateAllByRequest(List<Object[]> values) {}
}
