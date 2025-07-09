package org.slavik.entity.category;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDescription {

    private final int categoryId;
    private final String name;
    private final String description;
    private final String meta;

    public CategoryDescription(int categoryId, String name, String description, String meta) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.meta = meta;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMeta() {
        return meta;
    }

    public static class Mapper implements RowMapper<CategoryDescription> {

        @Override
        public @Nullable CategoryDescription mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int categoryId = rs.getInt("category_id");
            String name = rs.getString("name");
            String description = rs.getString("description");
            String meta = rs.getString("meta_title");
            return new CategoryDescription(
                    categoryId,
                    name,
                    description,
                    meta
            );
        }
    }
}
