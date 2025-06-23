package org.slavik.repository;

import org.slavik.entity.product.ProductToCategory;

import java.util.List;

public interface ProductToCategoryRepository {
    List<ProductToCategory> findAll();
    ProductToCategory create(ProductToCategory productToCategory);
}
