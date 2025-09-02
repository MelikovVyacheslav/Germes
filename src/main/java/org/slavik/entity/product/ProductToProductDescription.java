package org.slavik.entity.product;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductToProductDescription {
    private final int productId;
    private final String name;
    private final String description;
    private final String ean;

    public ProductToProductDescription(int productId, String name, String description, String ean) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.ean = ean;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEan() {
        return ean;
    }

    public static class Mapper implements RowMapper<ProductToProductDescription> {

        @Override
        public @Nullable ProductToProductDescription mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int productId = rs.getInt("product_id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            String ean = rs.getString("ean");
            return new ProductToProductDescription(
                    productId,
                    name,
                    description,
                    ean
            );
        }
    }
}
