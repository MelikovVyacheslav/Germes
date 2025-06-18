package org.slavik.repository;

import org.slavik.entity.category.Category;
import org.slavik.entity.product.Product;
import org.springframework.web.reactive.result.condition.ProducesRequestCondition;

import java.util.List;

public interface ProductRepository {
    Product create(Product product);

    Product find(int id);

    List<Product> findAll();

    Product update(Product product);

}
