package org.slavik.repository.category;

import org.slavik.entity.category.CategoryDescription;

import java.util.List;


public interface CategoryDescriptionRepository {
    List <CategoryDescription> find(int id);
    List<CategoryDescription> findAll();
    List<CategoryDescription> findAll(int[] categoryDescriptionIds);
    CategoryDescription create(CategoryDescription categoryDescription);
    CategoryDescription update(CategoryDescription categoryDescription);
}
