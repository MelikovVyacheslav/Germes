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
    public List<ProductToCategory> find(int productId, int categoryId) {
        String sql = """
                select * from oc_product_to_category
                where product_id = :productId and category_id = :categoryId;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        params.addValue("categoryId", categoryId);
        List<ProductToCategory> result = jdbcOperations.query(sql, params, new ProductToCategory.Mapper());
        return result;
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

    public boolean existsByProductIdAndCategoryId(int productId, int categoryId) {
        String sql = """
                    SELECT COUNT(*) FROM oc_product_to_category
                    WHERE product_id = :productId AND category_id = :categoryId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        params.addValue("categoryId", categoryId);

        Integer count = jdbcOperations.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }


    public int createCategory(String name) {

        int newCategoryId = getNextCategoryId();

        String insertCategorySql = """
                    INSERT INTO oc_category (category_id) VALUES (:id)
                """;
        String insertCategoryDescriptionSql = """
                    INSERT INTO oc_category_description (category_id, name) VALUES (:id, :name)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", newCategoryId);
        params.addValue("name", name);

        jdbcOperations.update(insertCategorySql, params);
        jdbcOperations.update(insertCategoryDescriptionSql, params);

        return newCategoryId;
    }

    public int getNextCategoryId() {
        String sql = "SELECT MAX(category_id) + 1 FROM oc_category";
        Integer id = jdbcOperations.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        return id != null ? id : 1;
    }


}
