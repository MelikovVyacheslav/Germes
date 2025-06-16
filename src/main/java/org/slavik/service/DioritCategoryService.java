package org.slavik.service;

import org.slavik.DioritB2B.DioritAPIClientImpl;
import org.slavik.repository.CategoryRepository;

public class DioritCategoryService implements CategoryService {
    private final DioritAPIClientImpl apiClient;
    private final CategoryRepository categoryRepository;

    public DioritCategoryService(DioritAPIClientImpl apiClient, CategoryRepository categoryRepository) {
        this.apiClient = apiClient;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void sync() {

    }
}
