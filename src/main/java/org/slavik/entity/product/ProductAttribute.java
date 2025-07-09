package org.slavik.entity.product;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductAttribute {

    private final int productId;
    private final int attributeId;
    private final int languageId;
    private final String text;

    public ProductAttribute(int productId, int attributeId, int languageId, String text) {
        this.productId = productId;
        this.attributeId = attributeId;
        this.languageId = languageId;
        this.text = text;
    }

    public int getProductId() {
        return productId;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public String getText() {
        return text;
    }

    public static class Mapper implements RowMapper<ProductAttribute> {

        @Override
        public @Nullable ProductAttribute mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int productId = rs.getInt("product_id");
            int attributeId = rs.getInt("attribute_id");
            int languageId = rs.getInt("language_id");
            String text = rs.getString("text");
            return new ProductAttribute(
                    productId,
                    attributeId,
                    languageId,
                    text
            );
        }
    }
}
