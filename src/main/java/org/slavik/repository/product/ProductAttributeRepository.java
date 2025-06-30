package org.slavik.repository.product;

import org.slavik.entity.product.ProductAttribute;

import java.util.List;

public interface ProductAttributeRepository {
    List<ProductAttribute> findAll();
    ProductAttribute find(ProductAttribute productAttribute);
    ProductAttribute create(ProductAttribute productAttribute);
}
