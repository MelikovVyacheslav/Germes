package org.slavik.DioritB2B;

import org.slavik.ApiClient;
import org.slavik.DioritB2B.model.ShortProduct;

import java.io.IOException;
import java.util.List;

public interface DioritApiClient extends ApiClient {
    List<ShortProduct> getAllProduct() throws IOException;
}
