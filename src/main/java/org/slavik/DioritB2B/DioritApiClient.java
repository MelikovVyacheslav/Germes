package org.slavik.DioritB2B;

import org.slavik.ApiClient;
import org.slavik.DioritB2B.model.DioritCategory;
import org.slavik.entity.product.Product;

import java.util.List;

public interface DioritApiClient extends ApiClient {
    List<Product> getAllProduct();

    List<DioritCategory> getAllCategory();
}
