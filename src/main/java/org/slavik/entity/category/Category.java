package org.slavik.entity.category;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Category {
    private final int categoryId;
    private final int parentId;
    private final String dateAdded;
    private final String dateModified;

    public Category(int categoryId, int parentId, String dateAdded, String dateModified) {
        this.categoryId = categoryId;
        this.parentId = parentId;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getParentId() {
        return parentId;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getDateModified() {
        return dateModified;
    }

    public static class Mapper implements RowMapper<Category> {

        @Override
        public @Nullable Category mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int categoryId = rs.getInt("category_id");
            int parentId = rs.getInt("parent_id");
            String dateAdded = rs.getString("date_added");
            String dateModify = rs.getString("date_modified");
            return new Category(
                    categoryId,
                    parentId,
                    dateAdded,
                    dateModify
            );
        }
    }
}
