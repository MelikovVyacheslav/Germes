package org.slavik.DioritB2B;

import org.slavik.AbstractApiClient;
import org.slavik.ApiClient;
import org.springframework.web.reactive.function.client.WebClient;

public class DioritApiClient extends AbstractApiClient implements ApiClient {

    public DioritApiClient(WebClient webClient) {
        super(webClient);
    }

}
