package org.slavik.repository;

import org.slavik.entity.product.ProductDescription;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbsProductDescription implements ProductDescriptionRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbsProductDescription(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public ProductDescription create(ProductDescription product) {
        String sql = """
                Insert into oc_product_description(productId,name,description) VALUES
                    (:productId,:name,:description);
                
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", product.getProductId());
        params.addValue("name", product.getName());
        params.addValue("description", product.getDescription());
        ProductDescription createProductDescription = jdbcOperations.queryForObject(sql, params, new ProductDescription.Mapper());
        return createProductDescription;
    }

    @Override
    public ProductDescription find(int id) {
        String sql = """
                SELECT * FROM oc_product_description
                WHERE product_id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        ProductDescription product = jdbcOperations.queryForObject(sql, params, new ProductDescription.Mapper());
        return product;
    }

    @Override
    public List<ProductDescription> findAll() {
        String sql = """
                Select * FROM oc_product_description
                """;
        List<ProductDescription> products = jdbcOperations.query(sql, new ProductDescription.Mapper());
        return products;
    }

    @Override
    public ProductDescription update(ProductDescription product) {
        String sql = """
                UPDATE oc_product_description SET
                    name = :name,
                    description = :description
                WHERE product_id = :productId;
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", product.getProductId());
        params.addValue("name", product.getName());
        params.addValue("description", product.getDescription());
        ProductDescription productDescriptionUpdate = jdbcOperations.queryForObject(sql, params, new ProductDescription.Mapper());
        return productDescriptionUpdate;
    }
}
