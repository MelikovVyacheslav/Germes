package org.slavik.entity.category;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDescription {
    private final int categoryId;
    private final String description;
    private final String meta;

    public CategoryDescription(int categoryId, String description, String meta) {
        this.categoryId = categoryId;
        this.description = description;
        this.meta = meta;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public String getMeta() {
        return meta;
    }

    static class Mapper implements RowMapper<CategoryDescription> {

        @Override
        public @Nullable CategoryDescription mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int categoryId = rs.getInt("category_id");
            String description = rs.getString("description");
            String meta = rs.getString("meta_title");
            return new CategoryDescription(
                categoryId,
                description,
                meta
            );
        }
    }
}
