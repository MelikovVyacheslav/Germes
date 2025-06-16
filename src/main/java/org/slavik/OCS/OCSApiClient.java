package org.slavik.OCS;

import org.slavik.ApiClient;

import java.util.List;

public interface OCSApiClient extends ApiClient {
    List<OCSCategory> getAll();
}
