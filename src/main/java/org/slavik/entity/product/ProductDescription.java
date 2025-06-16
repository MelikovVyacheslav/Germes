package org.slavik.entity.product;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDescription {
    private final int productId;
    private final String name;
    private final String description;

    public ProductDescription(int productId, String name, String description) {
        this.productId = productId;
        this.name = name;
        this.description = description;
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

    static class Mapper implements RowMapper<ProductDescription> {

        @Override
        public @Nullable ProductDescription mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int productId = rs.getInt("product_id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            return new ProductDescription(
                    productId,
                    name,
                    description
            );
        }
    }
}
