package org.slavik.repository;

import org.slavik.entity.category.Category;

import java.util.List;

public interface CategoryRepository {
    Category find(int id);
    List<Category> findAll();
    List<Category> findAll(int[] categoryIds);
    Category create(Category category);
    Category update(Category category);
}
