package org.slavik.repository.product;

import org.slavik.entity.product.ProductAttribute;
import org.slavik.repository.OperationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbcProductAttributeRepository implements ProductAttributeRepository, OperationRepository {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final JdbcTemplate jdbcTemplate;

    public JdbcProductAttributeRepository(NamedParameterJdbcOperations jdbcOperations, JdbcTemplate jdbcTemplate) {
        this.jdbcOperations = jdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ProductAttribute> findAll() {
        String sql = """
                select * from oc_product_attribute;
                """;
        List<ProductAttribute> productAttributeList = jdbcOperations.query(
                sql,
                new MapSqlParameterSource(),
                new ProductAttribute.Mapper()
        );
        return productAttributeList;
    }

    @Override
    public ProductAttribute find(ProductAttribute productAttribute) {
        String sql = """
            SELECT * FROM oc_product_attribute
            WHERE product_id = :productId
            AND attribute_id = :attributeId
            AND language_id = :languageId""";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productAttribute.getProductId());
        params.addValue("attributeId", productAttribute.getAttributeId());
        params.addValue("languageId", productAttribute.getLanguageId());

        List<ProductAttribute> results = jdbcOperations.query(
                sql,
                params,
                new ProductAttribute.Mapper()
        );

        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public ProductAttribute create(ProductAttribute productAttribute) {
        String sql = """
                insert into oc_product_attribute(product_id, attribute_id, language_id, text) values
                (:productId, :attributeId, :languageId, :text);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productAttribute.getProductId());
        params.addValue("attributeId", productAttribute.getAttributeId());
        params.addValue("languageId", productAttribute.getLanguageId());
        params.addValue("text", productAttribute.getText());
        jdbcOperations.update(sql, params);
        ProductAttribute newProductAttribute = find(productAttribute);
        return newProductAttribute;
    }

    @Override
    public void createAllByRequest(List<Object[]> values) {
        String sql = "insert into oc_product_attribute(product_id, attribute_id, language_id, text) values (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, values);
    }

    @Override
    public void updateAllByRequest(List<Object[]> values) {}
}
