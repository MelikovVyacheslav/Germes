package org.slavik.dioritB2B;

import org.slavik.ApiClient;
import org.slavik.dioritB2B.model.ShortProduct;

import java.io.IOException;
import java.util.List;

public interface DioritApiClient extends ApiClient {
    List<ShortProduct> getAllProduct() throws IOException;
}
