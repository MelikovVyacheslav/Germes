package org.slavik.entity.product;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductToStore {
    private final int productId;
    private final int storeId;

    public ProductToStore(int productId, int storeId) {
        this.productId = productId;
        this.storeId = storeId;
    }

    public int getProductId() {
        return productId;
    }

    public int getStoreId() {
        return storeId;
    }

    public static class Mapper implements RowMapper<ProductToStore> {

        @Override
        public @Nullable ProductToStore mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int productId = rs.getInt("product_id");
            int storeId = rs.getInt("store_id");
            return new ProductToStore(
                    productId,
                    storeId
            );
        }
    }
}
