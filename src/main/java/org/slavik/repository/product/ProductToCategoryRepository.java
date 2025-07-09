package org.slavik.repository.product;

import org.slavik.entity.product.ProductToCategory;

import java.util.List;

public interface ProductToCategoryRepository {
    List<ProductToCategory> findAll();

    List<ProductToCategory> find(int productId, int categoryId);

    ProductToCategory create(ProductToCategory productToCategory);
}
