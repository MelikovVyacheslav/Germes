package org.slavik.repository;

import org.slavik.entity.product.ProductDescription;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Arrays;
import java.util.List;

public class JdbcProductDescriptionRepository implements ProductDescriptionRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcProductDescriptionRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    private final String TAG_VALUE = "";
    private final String DESCRIPTION_VALUE = "";
    private final String META_KEYWORD_VALUE = "";
    private final int LANGUAGE_ID_VALUE = 1;
    @Override
    public ProductDescription create(ProductDescription product) {
        String sql = """
                INSERT INTO oc_product_description(product_id, language_id, name, description, tag, meta_title,
                meta_description, meta_keyword, meta_h1) VALUES
                    (:productId, :languageId, :name, :description, :tag, :name, :name, :meta_keyword, :name);
                
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", product.getProductId());
        params.addValue("name", product.getName());
        params.addValue("languageId", LANGUAGE_ID_VALUE);
        params.addValue("description", DESCRIPTION_VALUE);
        params.addValue("tag", TAG_VALUE);
        params.addValue("meta_keyword", META_KEYWORD_VALUE);
        jdbcOperations.update(sql, params);
        return product;
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
                SELECT * FROM oc_product_description
                """;
        List<ProductDescription> products = jdbcOperations.query(sql, new ProductDescription.Mapper());
        return products;
    }

    @Override
    public List<ProductDescription> findAll(int[] productDescriptionIds) {
        String ids = Arrays.toString(productDescriptionIds);
        String sql = """
                SELECT * FROM oc_category_description
                WHERE product_id in (:ids);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
        List<ProductDescription> productDescriptions
                = jdbcOperations.query(sql, new ProductDescription.Mapper());
        return productDescriptions;
    }

    @Override
    public ProductDescription update(ProductDescription product) {
        String sql = """
                UPDATE oc_product_description SET
                product_id = :productId,
                language_id = :languageId,
                name = :name,
                description = :description,
                tag = :tag,
                meta_title  = :name,
                meta_description  = :name,
                meta_keyword = :meta_keyword,
                meta_h1 = :name
                WHERE product_id = :productId;
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", product.getProductId());
        params.addValue("name", product.getName());
        params.addValue("languageId", LANGUAGE_ID_VALUE);
        params.addValue("description", DESCRIPTION_VALUE);
        params.addValue("tag", TAG_VALUE);
        params.addValue("meta_keyword", META_KEYWORD_VALUE);
        jdbcOperations.update(sql, params);
        return product;
    }
}