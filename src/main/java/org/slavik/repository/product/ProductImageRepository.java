package org.slavik.repository.product;

import org.slavik.entity.product.ProductImage;

import java.util.List;

public interface ProductImageRepository {
    List<ProductImage> findAll();
    List<ProductImage> find(int productId);
    void create(ProductImage productImage);
    ProductImage update(ProductImage productImage);
}
