package org.slavik.repository.product;

import org.slavik.entity.product.ProductAttribute;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;
import java.util.Map;

public class JdbcProductAttributeRepository implements ProductAttributeRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    public JdbcProductAttributeRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
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
                select * from oc_product_attribute
                where product_id = :productId and attribute_id = :attributeId;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productAttribute.getProductId());
        params.addValue("attributeId", productAttribute.getAttributeId());
        ProductAttribute newProductAttribute = jdbcOperations.queryForObject(
                sql,
                params,
                new ProductAttribute.Mapper()
        );
        return newProductAttribute;
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
}
