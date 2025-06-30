package org.slavik.OCS;

import org.slavik.ApiClient;
import org.slavik.OCS.model.OCSProduct;
import org.slavik.OCS.model.Result;

import java.util.List;

public interface OCSApiClient extends ApiClient {
    List<Result> getAll();
}
