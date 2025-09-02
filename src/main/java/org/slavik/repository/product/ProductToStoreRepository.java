package org.slavik.repository.product;

import org.slavik.entity.product.ProductToStore;
import org.slavik.repository.OperationRepository;

import java.util.List;

public interface ProductToStoreRepository {
    List<ProductToStore> findAll();
    ProductToStore find(int productId);
    ProductToStore create(ProductToStore productToStore);
}
