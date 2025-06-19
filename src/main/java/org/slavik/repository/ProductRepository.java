package org.slavik.repository;

import org.slavik.entity.product.Product;

import java.util.List;

public interface ProductRepository {
    Product create(Product product);

    Product find(int id);

    List<Product> findAll();

    List<Product> findAll(int[] productIds);

    Product update(Product product);



}
