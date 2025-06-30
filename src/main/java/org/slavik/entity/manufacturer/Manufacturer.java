package org.slavik.entity.manufacturer;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.slavik.entity.product.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Manufacturer {
    private final int manufacturerId;
    private final String name;

    public Manufacturer(int manufacturerId, String name) {
        this.manufacturerId = manufacturerId;
        this.name = name;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public String getName() {
        return name;
    }

    public static class Mapper implements RowMapper<Manufacturer> {

        @Override
        public @Nullable Manufacturer mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int manufacturerId = rs.getInt("manufacturer_id");
            String name = rs.getString("name");
            return new Manufacturer(
                    manufacturerId,
                    name
            );
        }
    }
}
