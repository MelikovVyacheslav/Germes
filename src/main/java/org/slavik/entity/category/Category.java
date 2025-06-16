package org.slavik.entity.category;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Category {
    private final int categoryId;
    private final String image;
    private final int parentId;
    private final String dateAdded;
    private final String dateModify;

    public Category(int categoryId, String image, int parentId, String dateAdded, String dateModify) {
        this.categoryId = categoryId;
        this.image = image;
        this.parentId = parentId;
        this.dateAdded = dateAdded;
        this.dateModify = dateModify;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getImage() {
        return image;
    }

    public int getParentId() {
        return parentId;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getDateModify() {
        return dateModify;
    }

    public static class Mapper implements RowMapper<Category> {

        @Override
        public @Nullable Category mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int categoryId = rs.getInt("category_id");
            String image = rs.getString("image");
            int parentId = rs.getInt("parent_id");
            String dateAdded = rs.getString("date_added");
            String dateModify = rs.getString("date_modify");
            return new Category(
                    categoryId,
                    image,
                    parentId,
                    dateAdded,
                    dateModify
            );
        }
    }
}
