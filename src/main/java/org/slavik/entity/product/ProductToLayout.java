package org.slavik.entity.product;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductToLayout {

    private final int productId;
    private final int storeId;
    private final int layoutId;

    public ProductToLayout(int productId, int storeId, int layoutId) {
        this.productId = productId;
        this.storeId = storeId;
        this.layoutId = layoutId;
    }

    public int getProductId() {
        return productId;
    }

    public int getStoreId() {
        return storeId;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public static class Mapper implements RowMapper<ProductToLayout> {

        @Override
        public @Nullable ProductToLayout mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int productId = rs.getInt("product_id");
            int storeId = rs.getInt("store_id");
            int layoutId = rs.getInt("layout_id");
            return new ProductToLayout(
                    productId,
                    storeId,
                    layoutId
            );
        }
    }
}
