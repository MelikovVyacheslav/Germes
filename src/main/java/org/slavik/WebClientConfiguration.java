package org.slavik;

import org.slavik.DioritB2B.DioritAPISourceConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfiguration {
    private final DioritAPISourceConfiguration dioritConfiguration;
    private final DioritAPISourceConfiguration ocsConfiguration;

    public WebClientConfiguration(DioritAPISourceConfiguration dioritConfiguration,
                                  DioritAPISourceConfiguration ocsConfiguration) {
        this.dioritConfiguration = dioritConfiguration;
        this.ocsConfiguration = ocsConfiguration;
    }

    public WebClient dioritWebClient() {
        return createClientBuilder(dioritConfiguration).build();
    }

    public WebClient ocsWebClient() {
        return createClientBuilder(ocsConfiguration).build();
    }

    private WebClient.Builder createClientBuilder(DioritAPISourceConfiguration configuration) {
        return WebClient.builder()
                .baseUrl(configuration.baseUrl())
                .defaultHeader(configuration.tokenHeaderKey(), configuration.token())
                .codecs(configure -> configure
                        .defaultCodecs()
                        .maxInMemorySize(configuration.maxInMemorySize()));
    }
}
