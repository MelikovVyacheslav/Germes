package org.slavik.ocs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slavik.AbstractApiClient;
import org.slavik.dioritB2B.APISourceConfiguration;
import org.slavik.ocs.model.OCSProduct;
import org.slavik.ocs.model.OCSProductCharacteristics;
import org.slavik.ocs.model.OCSProductResponse;
import org.slavik.ocs.model.Result;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

public class OCSAPIClientImpl extends AbstractApiClient implements OCSApiClient {

    private final APISourceConfiguration apiSourceConfiguration
            = new APISourceConfiguration(
            "https://connector.b2b.ocs.ru/api/v2",
            "TSWJXggwvt59l9nuYVvtSM?iyea0DR",
            "X-API-Key",
            100 * 1024 * 1024
    );
    List<String> itemIds = new ArrayList<>();

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
                .bodyToMono(String.class)
                .block();
        System.out.println("Response: " + responseFlux);
        return responseFlux;
    }


    public String viewProduct(String productId) {
        String responseFlux = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + "/catalog/products/" + productId)
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
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
                .bodyToMono(String.class)
                .block();
        System.out.println("Response: " + responseFlux);
        return responseFlux;
    }

    @Override
    public List<Result> getAll() {
        List<Result> products = new ArrayList<>();

        try {
            String url = apiSourceConfiguration.baseUrl() + "/catalog/categories/all/products";
            String jsonResponse = webClient.get()
                    .uri(url)
                    .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (jsonResponse == null || jsonResponse.isEmpty()) {
                System.out.println("Пустой ответ от API.");
                return products;
            }
            ObjectMapper mapper = new ObjectMapper();
            OCSProductResponse response = mapper.readValue(jsonResponse, OCSProductResponse.class);
            if (response.getResult() != null) {
                products = response.getResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    public OCSProductCharacteristics gettingProductCharacteristics(String itemId) {
        OCSProductCharacteristics jsonResponse = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + "/content/" + itemId)
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(OCSProductCharacteristics.class)
                .block();
        return jsonResponse;
    }

    public OCSProductCharacteristics postingAllProductCharacteristics(List<Result> allProductAPI) {
        OCSProductCharacteristics finalJsonResponse = new OCSProductCharacteristics();
        for (Result productAPI : allProductAPI) {
            addToBody(productAPI.getProduct().getItemID());
            if (itemIds.size() == 2500) {
                OCSProductCharacteristics currentJsonResponse = webClient.post()
                        .uri(apiSourceConfiguration.baseUrl() + "/content/batch")
                        .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBuild())
                        .retrieve()
                        .bodyToMono(OCSProductCharacteristics.class)
                        .block();
                assert currentJsonResponse != null;
                finalJsonResponse.getResult().addAll(currentJsonResponse.getResult());
                itemIds = new ArrayList<>();
            }
        }
        return finalJsonResponse;
    }

    public void addToBody(String itemId) {
        itemIds.add(itemId);
    }

    public void clearBody() {
        itemIds = new ArrayList<>();
    }

    private String requestBuild() {
        StringBuilder stringBuilder = new StringBuilder("[");
        char quotes = '"';
        for (String itemId : itemIds) {
            stringBuilder.append(quotes + itemId + quotes + ",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
