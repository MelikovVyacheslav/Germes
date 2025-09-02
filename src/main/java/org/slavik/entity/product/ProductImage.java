package org.slavik.entity.product;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductImage {
    private final int productImageId;
    private final int productId;
    private final String image;
    private final int sortOrder;

    public ProductImage(int productImageId, int productId, String image, int sortOrder) {
        this.productImageId = productImageId;
        this.productId = productId;
        this.image = image;
        this.sortOrder = sortOrder;
    }

    public int getProductImageId() {
        return productImageId;
    }

    public int getProductId() {
        return productId;
    }

    public String getImage() {
        return image;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public static class Mapper implements RowMapper<ProductImage> {
        @Override
        public @Nullable ProductImage mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int productImageId = rs.getInt("product_image_id");
            int productId = rs.getInt("product_id");
            String image = rs.getString("image");
            int sortOrder = rs.getInt("sort_order");
            return new ProductImage(
                    productImageId,
                    productId,
                    image,
                    sortOrder
            );
        }
    }
}
