package org.slavik;

import org.springframework.web.reactive.function.client.WebClient;

public class AbstractApiClient implements ApiClient {

    protected final WebClient webClient;

    public AbstractApiClient(WebClient webClient) {
        this.webClient = webClient;
    }
}
