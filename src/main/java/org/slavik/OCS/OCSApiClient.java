package org.slavik.OCS;

import org.slavik.ApiClient;
import org.slavik.OCS.model.OCSProduct;
import org.slavik.entity.product.Product;

import java.util.List;

public interface OCSApiClient extends ApiClient {
    List<OCSProduct> getAll();
}
