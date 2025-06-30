package org.slavik.repository;

import org.slavik.entity.product.ProductToLayout;
import org.slavik.entity.product.ProductToStore;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbcProductToLayoutRepository implements ProductToLayoutRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcProductToLayoutRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public ProductToLayout create(ProductToLayout productToLayout) {
        String sql = """
                insert into oc_product_to_layout(product_id, store_id, layout_id) values 
                (:productId, :storeId, :layoutId);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productToLayout.getProductId());
        params.addValue("storeId", productToLayout.getStoreId());
        params.addValue("layoutId", productToLayout.getLayoutId());
        jdbcOperations.update(sql, params);
        ProductToLayout newProductToLayout = find(productToLayout.getProductId());
        return newProductToLayout;
    }

    @Override
    public ProductToLayout find(int productId) {
        String sql = """
                select * from oc_product_to_layout
                where product_id = :productId;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        ProductToLayout productToLayout = jdbcOperations.queryForObject(sql, params, new ProductToLayout.Mapper());
        return productToLayout;
    }

    @Override
    public List<ProductToLayout> findAll() {
        String sql = """
                select * from oc_product_to_layout;
                """;
        List<ProductToLayout> productToLayoutList = jdbcOperations.query(
                sql,
                new MapSqlParameterSource(),
                new ProductToLayout.Mapper()
        );
        return productToLayoutList;
    }
}
