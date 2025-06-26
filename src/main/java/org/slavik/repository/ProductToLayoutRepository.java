package org.slavik.repository;

import org.slavik.entity.product.ProductToLayout;

import java.util.List;

public interface ProductToLayoutRepository {
    List<ProductToLayout> findAll();
    ProductToLayout find(int productId);
    ProductToLayout create(ProductToLayout productToLayout);
}
