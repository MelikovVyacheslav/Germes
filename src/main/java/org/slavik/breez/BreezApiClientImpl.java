package org.slavik.breez;

import org.slavik.AbstractApiClient;
import org.slavik.breez.model.*;
import org.slavik.dioritB2B.APISourceConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BreezApiClientImpl extends AbstractApiClient {

    private final APISourceConfiguration apiSourceConfiguration
            = new APISourceConfiguration(
            "https://api.breez.ru/v1",
            "Basic cmFib3RhLXM4N0Biay5ydTozYjY1ODkwMmZhYjNmYzJjODE1Yw==",
            "Authorization",
            100 * 1024 * 1024
    );

    public BreezApiClientImpl(WebClient webClient) {
        super(webClient);
    }

    public Map<String, BreezProductResponse> getAllProducts() throws IOException, InterruptedException {
        String json = getJson("/products");
        Converter<BreezProductResponse> converter = new Converter<>();
        Map<String, BreezProductResponse> response = converter.fromJsonString(json, BreezProductResponse.class);
        return response;
    }

    public BreezProductResponse getProductById(String productId) throws IOException, InterruptedException {
        String json = getJson("/products/?id=" + productId);
        Converter<BreezProductResponse> converter = new Converter<>();
        BreezProductResponse breezProductResponse = converter.fromJsonStringToObject(json, productId, BreezProductResponse.class);
        return breezProductResponse;
    }

    public Map<String, BreezBrand> getAllBrands() throws IOException, InterruptedException {
        String json = getJson("/brands");
        Converter<BreezBrand> converter = new Converter<>();
        Map<String, BreezBrand> brandMap = converter.fromJsonString(json, BreezBrand.class);
        return brandMap;
    }

    public BreezBrand getBrandById(String brandId) throws IOException, InterruptedException {
        String json = getJson("/brands/?id=" + brandId);
        Converter<BreezBrand> converter = new Converter<>();
        BreezBrand brand = converter.fromJsonStringToObject(json, brandId, BreezBrand.class);
        return brand;
    }

    public Map<String, BreezCategory> getAllCategories() throws IOException, InterruptedException {
        String json = getJson("/categories");
        Converter<BreezCategory> converter = new Converter<>();
        Map<String, BreezCategory> breezCategoryMap = converter.fromJsonString(json, BreezCategory.class);
        return breezCategoryMap;
    }

    public BreezCategory getCategoryById(String categoryId) throws IOException, InterruptedException {
        String json = getJson("/categories/?id=" + categoryId);
        Converter<BreezCategory> converter = new Converter<>();
        BreezCategory breezCategory = converter.fromJsonStringToObject(json, categoryId, BreezCategory.class);
        return breezCategory;
    }

    public BreezTech getCharacteristicsToProduct(String productId) throws IOException, InterruptedException {
        String json = getJson("/tech/?id=" + productId);
        Converter<BreezTech> converter = new Converter<>();
        BreezTech breezTech = converter.fromJsonStringToObject(json, productId, BreezTech.class);
        return breezTech;
    }

    public List<BreezStockInfo> gettingWarehousesWhereTheProductAreLocated(String nc) throws IOException, InterruptedException {
        List<BreezStockInfo> json = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + "/leftovers/?nc=" + nc)
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<BreezStockInfo>>() {})
                .block();
        return json;
    }

    private String getJson(String apiPath) throws InterruptedException {
        String json = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + apiPath)
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return json;
    }

    public WebClient getWebClient() {
        return webClient;
    }
}
