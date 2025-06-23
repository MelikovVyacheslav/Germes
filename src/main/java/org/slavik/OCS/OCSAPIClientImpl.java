package org.slavik.OCS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slavik.AbstractApiClient;
import org.slavik.ApiClient;
import org.slavik.DioritB2B.DioritAPISourceConfiguration;
import org.slavik.OCS.model.OCSProduct;
import org.slavik.OCS.model.OCSProductResponse;
import org.slavik.OCS.model.Result;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class OCSAPIClientImpl extends AbstractApiClient implements ApiClient, OCSApiClient {

    private final DioritAPISourceConfiguration apiSourceConfiguration
            = new DioritAPISourceConfiguration(
            "https://connector.b2b.ocs.ru/api/v2",
            "TSWJXggwvt59l9nuYVvtSM?iyea0DR",
            "X-API-Key",
            100 * 1024 * 1024
    );

    public OCSAPIClientImpl(WebClient webClient) {
        super(webClient);
    }

    public String getInformationByCategory() {
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
        return responseFlux;
    }

    public String viewProduct(String productId) {
        WebClient webClient = WebClient.builder()
                .codecs(configure -> configure
                        .defaultCodecs()
                        .maxInMemorySize(apiSourceConfiguration.maxInMemorySize())).build();
        String responseFlux = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + "/catalog/products/" + productId)
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
        return responseFlux;
    }

    public String gettingInformationByProductCategory(String category) {
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
        return responseFlux;
    }

    public String gettingInformationByProductsCategories(List<String> batchCategories) throws JsonProcessingException {
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
        return responseFlux;
    }

    public String gettingInformationAboutTheProducts(List<String> batchItemIds) throws JsonProcessingException {
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
        return responseFlux;
    }

    @Override
    public List<OCSProduct> getAll() {
        List<OCSProduct> products = new ArrayList<>();
        OCSProductResponse productResponse = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + "/catalog/categories/V02/products") // Уточните endpoint
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OCSProductResponse.class)
                .block();

            for (Result result : productResponse.getResult()) {
                products.add(result.getProduct());

        }
        return products;
    }


}