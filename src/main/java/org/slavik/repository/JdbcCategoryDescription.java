package org.slavik.repository;

import org.slavik.entity.category.Category;
import org.slavik.entity.category.CategoryDescription;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.ArrayList;
import java.util.List;

public class JdbcCategoryDescription implements CategoryDescriptionRepository {
    private final NamedParameterJdbcOperations jdbcOperations;

    private final int LANGUAGE_ID_VALUE = 1;

    public JdbcCategoryDescription(NamedParameterJdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
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
    public List<CategoryDescription> findAll(int[] categoryIds) {
        List<CategoryDescription> categoryDescriptions = new ArrayList<>();
        String sql = """
                SELECT * FROM oc_category
                where category_id = :id;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        for (int id : categoryIds) {
            params.addValue("id", id);
            CategoryDescription categoryDescription
                    = jdbcOperations.queryForObject(sql, params, new CategoryDescription.Mapper());
            categoryDescriptions.add(categoryDescription);
        }
        return categoryDescriptions;
    }

    @Override
    public CategoryDescription create(CategoryDescription categoryDescription) {
        String sql = """
                insert into oc_product_description(category_id, language_id, description, meta_title, 
                meta_description, meta_keyword, meta_h1) 
                values (
                :categoryId,
                :languageId
                :description,
                :meta,
                :meta,
                :meta,
                :meta
                );
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("categoryId", categoryDescription.getCategoryId());
        params.addValue("language", LANGUAGE_ID_VALUE);
        params.addValue("description", categoryDescription.getDescription());
        params.addValue("meta", categoryDescription.getMeta());
        CategoryDescription createdCategoryDescription
                = jdbcOperations.queryForObject(sql, params, new CategoryDescription.Mapper());
        return createdCategoryDescription;
    }

    @Override
    public CategoryDescription update(CategoryDescription categoryDescription) {
        String sql = """
                update oc_category_description
                set description = :description,
                meta_title = :meta,
                meta_description = :meta,
                meta_keyword = :meta,
                meta_h1 = :meta
                where category_id = :categoryId;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("description", categoryDescription.getDescription());
        params.addValue("meta", categoryDescription.getMeta());
        params.addValue("categoryId", categoryDescription.getCategoryId());
        CategoryDescription updatedCategoryDescription
                = jdbcOperations.queryForObject(sql, params, new CategoryDescription.Mapper());
        return updatedCategoryDescription;
    }
}
