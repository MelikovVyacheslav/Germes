package org.slavik.repository;

import org.slavik.entity.product.Product;
import org.slavik.entity.product.ProductDescription;

import java.util.List;

public interface ProductDescriptionRepository {

    ProductDescription create(ProductDescription product);

    ProductDescription find(int id);

    List<ProductDescription> findAll();

    ProductDescription update(ProductDescription product);

}
