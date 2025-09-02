package org.slavik.repository.category;

import org.slavik.entity.category.Category;
import org.slavik.repository.OperationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Arrays;
import java.util.List;

public class JdbcCategoryRepository implements CategoryRepository, OperationRepository {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final JdbcTemplate jdbcTemplate;

    private final int TOP_VALUE = 1;
    private final int COLUMN_VALUE = 1;
    private final int SORT_ORDER = 0;
    private final int STATUS_VALUE = 1;
    private final int NOINDEX_VALUE = 1;

    public JdbcCategoryRepository(NamedParameterJdbcOperations jdbcOperations, JdbcTemplate jdbcTemplate) {
        this.jdbcOperations = jdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
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
        String ids = Arrays.toString(categoryIds);
        String sql = """
                SELECT * FROM oc_category
                where category_id in (:ids);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
        List<Category> categories = jdbcOperations.query(sql, new Category.Mapper());
        return categories;
    }

    private Integer getLastId() {
        String sql = """
                select max(category_id) from oc_category
                """;
        Integer maxCategoryId = jdbcOperations.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
        return maxCategoryId;
    }

    String NOW_VALUE = "NOW()";

    @Override
    public Category create(Category category) {
        String sql = """
                insert into oc_category(parent_id, top, `column`, sort_order, status,
                date_added, date_modified, noindex) values (
                :parentId, :top, :column, :sortOrder, :status,""" + NOW_VALUE + ", " + NOW_VALUE + ", :noindex);";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("parentId", category.getCategoryId());
        params.addValue("top", TOP_VALUE);
        params.addValue("column", COLUMN_VALUE);
        params.addValue("sortOrder", SORT_ORDER);
        params.addValue("status", STATUS_VALUE);
        params.addValue("noindex", NOINDEX_VALUE);
        jdbcOperations.update(sql, params);
        Category createdCategory = find(getLastId());
        return createdCategory;
    }

    @Override
    public Category update(Category category) {
        String sql = """
                update oc_category
                set parent_id = :parentId,
                date_modified = :dateModified
                where category_id = :id;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(":parentId", category.getParentId());
        params.addValue("dateModified", category.getDateModified());
        params.addValue("id", category.getCategoryId());
        jdbcOperations.update(sql, params);
        return category;
    }

    @Override
    public void createAllByRequest(List<Object[]> values) {

    }

    @Override
    public void updateAllByRequest(List<Object[]> values) {

    }
}
