package org.slavik.entity.product;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductToCategory {

    private final int productId;
    private final int categoryId;

    public ProductToCategory(int productId, int categoryId) {
        this.productId = productId;
        this.categoryId = categoryId;
    }

    public int getProductId() {
        return productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public static class Mapper implements RowMapper<ProductToCategory> {

        @Override
        public @Nullable ProductToCategory mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int productId = rs.getInt("product_id");
            int categoryId = rs.getInt("category_id");
            return new ProductToCategory(
                    productId,
                    categoryId
            );
        }
    }
}
