package org.slavik.ocs;

import org.slavik.ApiClient;
import org.slavik.ocs.model.Result;

import java.util.List;

public interface OCSApiClient extends ApiClient {
    List<Result> getAll();
}
