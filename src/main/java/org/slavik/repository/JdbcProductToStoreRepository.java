package org.slavik.repository;

import org.slavik.entity.product.Product;
import org.slavik.entity.product.ProductToCategory;
import org.slavik.entity.product.ProductToStore;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;
import java.util.UUID;

public class JdbcProductToStoreRepository implements ProductToStoreRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcProductToStoreRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<ProductToStore> findAll() {
        String sql = """
                select * from oc_product_to_store
                """;
        List<ProductToStore> productToStoreList = jdbcOperations.query(
                sql,
                new MapSqlParameterSource(),
                new ProductToStore.Mapper());
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
        ProductToStore newProductToStore = jdbcOperations.queryForObject(sql, params, new ProductToStore.Mapper());
    return newProductToStore;
    }

    private final int STORE_ID = 0;
    @Override
    public ProductToStore create(ProductToStore productToStore) {
        String sql = """
                insert into oc_product_to_store(product_id, store_id) values
                (:productId, :storeId);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productToStore.getProductId());
        params.addValue("storeId", STORE_ID);
        jdbcOperations.update(sql, params);
        ProductToStore newProductToStore = find(productToStore.getProductId());
        return newProductToStore;
    }
}
