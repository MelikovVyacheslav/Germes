package org.slavik;

import org.springframework.web.reactive.function.client.WebClient;

public interface ApiClient {

    WebClient getWebClient();
}
