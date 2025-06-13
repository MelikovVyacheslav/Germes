package org.slavik.OCS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slavik.AbstractApiClient;
import org.slavik.ApiClient;
import org.slavik.ApiSourceConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class OCSApiClient extends AbstractApiClient implements ApiClient {

    private final ApiSourceConfiguration apiSourceConfiguration;

    public OCSApiClient(WebClient webClient, ApiSourceConfiguration apiSourceConfiguration) {
        super(webClient);
        this.apiSourceConfiguration = apiSourceConfiguration;
    }

    public void getInformationByCategory() {
        WebClient webClient = WebClient.create();
        String responseFlux = webClient.get()
                .uri("/catalog/categories")
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())  // Замени токен!
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus((code) -> {
                    if (code.is4xxClientError() || code.is5xxServerError()) {
                        System.out.println("Failed with code:" + code.value());
                        return false;
                    }
                    if (code.is2xxSuccessful()) return true;
                    System.out.println("Not 2xx and not 4xx, 5xx way.");
                    return false;
                }, (response) -> {
                    System.out.println(response);
                    return Mono.empty();
                })
                .bodyToMono(String.class)
                .block();
        System.out.println("Response: " + responseFlux);
    }

    public void gettingInformationAboutTheProduct(String itemId) {
        WebClient webClient = WebClient.builder()
                .codecs(configure -> configure
                        .defaultCodecs()
                        .maxInMemorySize(apiSourceConfiguration.maxInMemorySize())).build();
        String responseFlux = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + "/catalog/products/" + itemId)
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus((code) -> {
                    if (code.is4xxClientError() || code.is5xxServerError()) {
                        System.out.println("Failed with code:" + code.value());
                        return false;
                    }
                    if (code.is2xxSuccessful()) return true;
                    System.out.println("Not 2xx and not 4xx, 5xx way.");
                    return false;
                }, (response) -> {
                    System.out.println(response);
                    return Mono.empty();
                })
                .bodyToMono(String.class)
                .block();
        System.out.println("Response: " + responseFlux);
    }

    public void gettingInformationByProductCategory(String category) {
        WebClient webClient = WebClient.builder()
                .codecs(configure -> configure
                        .defaultCodecs()
                        .maxInMemorySize(apiSourceConfiguration.maxInMemorySize())
                ).build();
        String responseFlux = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + "/catalog/categories/" + category + "/products")
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus((code) -> {
                    if (code.is4xxClientError() || code.is5xxServerError()) {
                        System.out.println("Failed with code:" + code.value());
                        return false;
                    }
                    if (code.is2xxSuccessful()) return true;
                    System.out.println("Not 2xx and not 4xx, 5xx way.");
                    return false;
                }, (response) -> {
                    System.out.println(response);
                    return Mono.empty();
                })
                .bodyToMono(String.class)
                .block();
        System.out.println("Response: " + responseFlux);
    }

    public void gettingInformationByProductsCategories(List<String> batchCategories) throws JsonProcessingException {
        WebClient webClient = WebClient.builder()
                .codecs(configure -> configure
                        .defaultCodecs()
                        .maxInMemorySize(apiSourceConfiguration.maxInMemorySize())).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String categoriesRequestJson = objectMapper.writeValueAsString(batchCategories);

        String responseFlux = webClient.post()
                .uri(apiSourceConfiguration.baseUrl() + "/catalog/categories/batch/products")
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoriesRequestJson)
                .retrieve()
                .onStatus((code) -> {
                    if (code.is4xxClientError() || code.is5xxServerError()) {
                        System.out.println("Failed with code:" + code.value());
                        return false;
                    }
                    if (code.is2xxSuccessful()) return true;
                    System.out.println("Not 2xx and not 4xx, 5xx way.");
                    return false;
                }, (response) -> {
                    System.out.println(response);
                    return Mono.empty();
                })
                .bodyToMono(String.class)
                .block();
        System.out.println("Response: " + responseFlux);
    }

    public void gettingInformationAboutTheProducts(List<String> batchItemIds) throws JsonProcessingException {
        WebClient webClient = WebClient.builder()
                .codecs(configure -> configure
                        .defaultCodecs()
                        .maxInMemorySize(apiSourceConfiguration.maxInMemorySize())).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String categoriesRequestJson = objectMapper.writeValueAsString(batchItemIds);

        String responseFlux = webClient.post()
                .uri(apiSourceConfiguration.baseUrl() + "/catalog/categories/batch/products")
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(categoriesRequestJson)
                .retrieve()
                .onStatus((code) -> {
                    if (code.is4xxClientError() || code.is5xxServerError()) {
                        System.out.println("Failed with code:" + code.value());
                        return false;
                    }
                    if (code.is2xxSuccessful()) return true;
                    System.out.println("Not 2xx and not 4xx, 5xx way.");
                    return false;
                }, (response) -> {
                    System.out.println(response);
                    return Mono.empty();
                })
                .bodyToMono(String.class)
                .block();
        System.out.println("Response: " + responseFlux);
    }
}
