package org.slavik.repository;

import org.slavik.entity.category.Category;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.ArrayList;
import java.util.List;

public class JdbcCategoryRepository implements CategoryRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    final int TOP_VALUE = 0;
    final int COLUMN_VALUE = 1;
    final int SORT_ORDER = 0;
    final int STATUS_VALUE = 1;
    final int NOINDEX_VALUE = 1;

    public JdbcCategoryRepository(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Category find(int id) {
        String sql = """
                SELECT * FROM oc_category
                WHERE category_id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        Category category = jdbcOperations.queryForObject(sql, params, new Category.Mapper());
        return category;
    }

    @Override
    public List<Category> findAll() {
        String sql = """
                SELECT * FROM oc_category;
                """;
        List<Category> categories = jdbcOperations.query(sql, new Category.Mapper());
        return categories;
    }

    @Override
    public List<Category> findAll(int[] categoryIds) {
        List<Category> categories = new ArrayList<>();
        String sql = """
                SELECT * FROM oc_category
                where category_id = :id;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        for (int id : categoryIds) {
            params.addValue("id", id);
            Category createdCategory = jdbcOperations.queryForObject(sql, params, new Category.Mapper());
            categories.add(createdCategory);
        }
        return categories;
    }

    @Override
    public Category create(Category category) {
        String sql = """
                insert into oc_category(parent_id, top, `column`, sort_order, status,
                date_added, date_modified, noindex) values (
                :parentId, :top, :column, :sortOrder, :status, :dateAdded, :dateModified, :noindex
                );
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("parentId", category.getCategoryId());
        params.addValue("top", TOP_VALUE);
        params.addValue("column", COLUMN_VALUE);
        params.addValue("sortOrder", SORT_ORDER);
        params.addValue("status", STATUS_VALUE);
        params.addValue("dateAdded", category.getDateAdded());
        params.addValue("dateModified", category.getDateModified());
        params.addValue("noindex", NOINDEX_VALUE);
        Category createdCategory = jdbcOperations.queryForObject(sql, params, new Category.Mapper());
        return createdCategory;
    }

    @Override
    public Category update(Category category) {
        String sql = """
                update oc_category
                set parent_id = :parentId,
                date_modified = :dateModified
                where category_id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(":parentId", category.getParentId());
        params.addValue("dateModified", category.getDateModified());
        params.addValue("id", category.getCategoryId());
        Category updatedCategory = jdbcOperations.queryForObject(sql, params, new Category.Mapper());
        return null;
    }
}
