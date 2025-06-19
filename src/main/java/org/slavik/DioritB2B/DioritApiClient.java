package org.slavik.DioritB2B;

import org.slavik.ApiClient;
import org.slavik.DioritB2B.model.Datum;
import org.slavik.DioritB2B.model.DioritCategory;
import org.slavik.entity.product.Product;

import java.io.IOException;
import java.util.List;

public interface DioritApiClient extends ApiClient {
    List<Datum> getAllProduct() throws IOException;
}
