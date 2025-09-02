package org.slavik.repository.product;

import org.slavik.entity.product.ProductToCategory;
import org.slavik.repository.OperationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbcProductToCategoryRepository implements ProductToCategoryRepository, OperationRepository {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final JdbcTemplate jdbcTemplate;

    public JdbcProductToCategoryRepository(NamedParameterJdbcOperations jdbcOperations, JdbcTemplate jdbcTemplate) {
        this.jdbcOperations = jdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
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
    public List<ProductToCategory> find(int productId) {
        String sql = """
                select * from oc_product_to_category
                where product_id = :productId;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        List<ProductToCategory> productToCategoryList = jdbcOperations.query(sql, params, new ProductToCategory.Mapper());
        return productToCategoryList;
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

    @Override
    public void updateAllByRequest(List<Object[]> values) {}

    @Override
    public void createAllByRequest(List<Object[]> values) {
        String sql = "insert into oc_product_to_category(product_id, category_id) values (?, ?)";
        jdbcTemplate.batchUpdate(sql, values);
    }
}
