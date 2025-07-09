package org.slavik.entity.category;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Category {
    private final int categoryId;
    private final int parentId;
    private final Date dateAdded;
    private final Date dateModified;

    public Category(int categoryId, int parentId, Date dateAdded, Date dateModified) {
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

    public Date getDateAdded() {
        return dateAdded;
    }

    public Date getDateModified() {
        return dateModified;
    }



    public static class Mapper implements RowMapper<Category> {

        @Override
        public @Nullable Category mapRow(@NotNull ResultSet rs, int rowNum) throws SQLException {
            int categoryId = rs.getInt("category_id");
            int parentId = rs.getInt("parent_id");
            Date dateAdded = rs.getDate("date_added");
            Date dateModify = rs.getDate("date_modified");
            return new Category(
                    categoryId,
                    parentId,
                    dateAdded,
                    dateModify
            );
        }
    }
}
