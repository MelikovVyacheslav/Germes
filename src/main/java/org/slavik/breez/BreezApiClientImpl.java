package org.slavik.breez;

import org.slavik.AbstractApiClient;
import org.slavik.breez.model.BreezBrand;
import org.slavik.breez.model.BreezCategory;
import org.slavik.breez.model.BreezProductResponse;
import org.slavik.breez.model.BreezTech;
import org.slavik.dioritB2B.APISourceConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
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

    public Map<String, BreezProductResponse> getAllProducts() throws IOException {
        String json = getJson("/products");
        Converter<BreezProductResponse> converter = new Converter<>();
        Map<String, BreezProductResponse> response = converter.fromJsonString(json, BreezProductResponse.class);
        return response;
    }

    public Map<String, BreezProductResponse> getProductById(int productId) throws IOException {
        String json = getJson("/products/?id=" + productId);
        Converter<BreezProductResponse> converter = new Converter<>();
        Map<String, BreezProductResponse> breezProductResponse = converter.fromJsonString(json, BreezProductResponse.class);
        return breezProductResponse;
    }

    public Map<String, BreezBrand> getAllBrands() throws IOException {
        String json = getJson("/brands");
        Converter<BreezBrand> converter = new Converter<>();
        Map<String, BreezBrand> brandMap = converter.fromJsonString(json, BreezBrand.class);
        return brandMap;
    }

    public Map<String, BreezBrand> getBrandById(int brandId) throws IOException {
        String json = getJson("/brands/?id=" + brandId);
        Converter<BreezBrand> converter = new Converter<>();
        Map<String, BreezBrand> brand = converter.fromJsonString(json, BreezBrand.class);
        return brand;
    }

    public Map<String, BreezCategory> getAllCategories() throws IOException {
        String json = getJson("/categories");
        Converter<BreezCategory> converter = new Converter<>();
        Map<String, BreezCategory> breezCategoryMap = converter.fromJsonString(json, BreezCategory.class);
        return breezCategoryMap;
    }

    public Map<String, BreezCategory> getCategoryById(int categoryId) throws IOException {
        String json = getJson("/categories/?id=" + categoryId);
        Converter<BreezCategory> converter = new Converter<>();
        Map<String, BreezCategory> breezCategory = converter.fromJsonString(json, BreezCategory.class);
        return breezCategory;
    }

    public Map<String, BreezTech> getCharacteristicsToProduct(int productId) throws IOException {
        String json = getJson("/tech/?id=" + productId);
        Converter<BreezTech> converter = new Converter<>();
        Map<String, BreezTech> breezTech = converter.fromJsonString(json, BreezTech.class);
        return breezTech;
    }

    private String getJson(String apiPath) {
        String json = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + apiPath)
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return json;
    }
}
