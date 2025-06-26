package org.slavik.repository;

import org.slavik.entity.product.ProductToStore;

import java.util.List;

public interface ProductToStoreRepository {
    List<ProductToStore> findAll();
    ProductToStore find(int productId);
    ProductToStore create(ProductToStore productToStore);
}
