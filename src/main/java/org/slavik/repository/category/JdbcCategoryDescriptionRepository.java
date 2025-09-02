package org.slavik.repository.category;

import org.slavik.entity.category.CategoryDescription;
import org.slavik.repository.OperationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Arrays;
import java.util.List;

public class JdbcCategoryDescriptionRepository implements CategoryDescriptionRepository, OperationRepository {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final JdbcTemplate jdbcTemplate;

    private final int LANGUAGE_ID_VALUE = 1;

    public JdbcCategoryDescriptionRepository(NamedParameterJdbcOperations jdbcOperations, JdbcTemplate jdbcTemplate) {
        this.jdbcOperations = jdbcOperations;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CategoryDescription find(int id) {
        String sql = """
                select * from oc_category_description
                where category_id = :id;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        CategoryDescription categoryDescription
                = jdbcOperations.queryForObject(sql, params, new CategoryDescription.Mapper());
        return categoryDescription;
    }

    @Override
    public List<CategoryDescription> findAll() {
        String sql = """
                SELECT * FROM oc_category_description;
                """;
        List<CategoryDescription> categoryDescriptions = jdbcOperations.query(sql, new CategoryDescription.Mapper());
        return categoryDescriptions;
    }

    @Override
    public List<CategoryDescription> findAll(int[] categoryDescriptionIds) {
        String ids = Arrays.toString(categoryDescriptionIds);
        String sql = """
                SELECT * FROM oc_category
                where category_id in (:ids);
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);
        List<CategoryDescription> categoryDescriptions
                = jdbcOperations.query(sql, new CategoryDescription.Mapper());
        return categoryDescriptions;
    }

    public List<CategoryDescription> findByName(String name) {
        String sql = """
                select * from oc_category_description
                where name = :name;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);
        try {
            List<CategoryDescription> categoryDescription = jdbcOperations.query(sql, params, new CategoryDescription.Mapper());
            return categoryDescription;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CategoryDescription create(CategoryDescription categoryDescription) {
        String sql = """
                insert into oc_category_description(category_id, language_id, name, description, meta_title,
                meta_description, meta_keyword, meta_h1)
                values (
                :categoryId,
                :languageId,
                :name,
                '',
                :meta,
                :meta,
                :meta,
                :meta
                );
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("categoryId", categoryDescription.getCategoryId());
        params.addValue("languageId", LANGUAGE_ID_VALUE);
        params.addValue("name", categoryDescription.getName());
        params.addValue("meta", categoryDescription.getMeta());
        jdbcOperations.update(sql, params);
        List<CategoryDescription> createdCategoryDescription
                = List.of(find(categoryDescription.getCategoryId()));
        return createdCategoryDescription.get(0);
    }

    @Override
    public CategoryDescription update(CategoryDescription categoryDescription) {
        String sql = """
                update oc_category_description
                set name = name,
                description = :description,
                meta_title = :meta,
                meta_description = :meta,
                meta_keyword = :meta,
                meta_h1 = :meta
                where category_id = :categoryId;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", categoryDescription.getName());
        params.addValue("description", categoryDescription.getDescription());
        params.addValue("meta", categoryDescription.getMeta());
        params.addValue("categoryId", categoryDescription.getCategoryId());
        CategoryDescription updatedCategoryDescription
                = jdbcOperations.queryForObject(sql, params, new CategoryDescription.Mapper());
        return updatedCategoryDescription;
    }

    @Override
    public void createAllByRequest(List<Object[]> values) {

    }

    @Override
    public void updateAllByRequest(List<Object[]> values) {

    }
}
