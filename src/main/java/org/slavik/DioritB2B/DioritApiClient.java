package org.slavik.DioritB2B;

import org.slavik.AbstractApiClient;
import org.slavik.ApiClient;
import org.slavik.ApiSourceConfiguration;
import org.slavik.WebClientConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class DioritApiClient extends AbstractApiClient implements ApiClient {

    private final ApiSourceConfiguration apiSourceConfiguration;

    public DioritApiClient(WebClient webClient, ApiSourceConfiguration apiSourceConfiguration) {
        super(webClient);
        this.apiSourceConfiguration = apiSourceConfiguration;
    }

    public void gettingListOfProducts() {
        Flux<String> responseFlux = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + "/api/products")
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(String.class);

        responseFlux.subscribe(
                json -> System.out.println("Received: " + json),
                error -> System.err.println("FULL ERROR: " + error),
                () -> System.out.println("Done")
        );
    }

    public void viewProduct(String productId) {
        Flux<String> responseFlux = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + "/api/products/" + productId)
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(String.class);

        responseFlux.subscribe(
                json -> System.out.println("Received: " + json),
                error -> System.err.println("FULL ERROR: " + error),
                () -> System.out.println("Done")
        );
    }
}
