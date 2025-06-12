package org.slavik;

import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfiguration {
    private final ApiSourceConfiguration dioritConfiguration;
    private final ApiSourceConfiguration ocsConfiguration;

    public WebClientConfiguration(ApiSourceConfiguration diorbitConfiguration,
                                  ApiSourceConfiguration ocsConfiguration) {
        this.dioritConfiguration = diorbitConfiguration;
        this.ocsConfiguration = ocsConfiguration;
    }

    public WebClient diorbitWebClient() {
        return createClientBuilder(dioritConfiguration).build();
    }

    public WebClient ocsWebClient() {
        return createClientBuilder(ocsConfiguration).build();
    }

    private WebClient.Builder createClientBuilder(ApiSourceConfiguration configuration) {
        return WebClient.builder()
                .baseUrl(configuration.baseUrl())
                .defaultHeader(configuration.tokenHeaderKey(), configuration.token())
                .codecs(configure -> configure
                        .defaultCodecs()
                        .maxInMemorySize(configuration.maxInMemorySize()));
    }


    public record ApiSourceConfiguration(
            String baseUrl,
            String token,
            String tokenHeaderKey,
            int maxInMemorySize
    ) { }
}
