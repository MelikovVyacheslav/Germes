package org.slavik;

import org.slavik.dioritB2B.APISourceConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfiguration {
    private final APISourceConfiguration apiSourceConfiguration;

    public WebClientConfiguration(APISourceConfiguration apiSourceConfiguration) {
        this.apiSourceConfiguration = apiSourceConfiguration;
    }

    public WebClient getAPIWebClient() {
        return createClientBuilder(apiSourceConfiguration).build();
    }

    private WebClient.Builder createClientBuilder(APISourceConfiguration configuration) {
        return WebClient.builder()
                .baseUrl(configuration.baseUrl())
                .defaultHeader(configuration.tokenHeaderKey(), configuration.token())
                .codecs(configure -> configure
                        .defaultCodecs()
                        .maxInMemorySize(configuration.maxInMemorySize()));
    }
}
