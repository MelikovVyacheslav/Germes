package org.slavik.repository.product;

import org.slavik.entity.product.ProductToCategory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbcProductToCategoryRepository implements ProductToCategoryRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcProductToCategoryRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<ProductToCategory> findAll() {
        String sql = """
                select * from oc_product_to_category;
                """;
        List<ProductToCategory> allProduct = jdbcOperations.query(sql, new ProductToCategory.Mapper());
        return allProduct;
    }

    @Override
    public ProductToCategory create(ProductToCategory productToCategory) {
        String sql = """
                insert into oc_product_to_category(product_id, category_id) values (
                :productId, :categoryId);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productToCategory.getProductId());
        params.addValue("categoryId", productToCategory.getCategoryId());
        jdbcOperations.update(sql, params);
        return productToCategory;
    }
}
