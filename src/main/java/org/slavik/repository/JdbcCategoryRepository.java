package org.slavik.repository;

import org.slavik.entity.category.Category;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.List;

public class JdbcCategoryRepository implements CategoryRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

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

}
