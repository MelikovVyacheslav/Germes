package org.slavik.DioritB2B;

import org.slavik.AbstractApiClient;
import org.slavik.DioritB2B.model.DioritCategory;
import org.slavik.entity.category.Category;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class DioritAPIClientImpl extends AbstractApiClient implements DioritApiClient {



    private final DioritAPISourceConfiguration apiSourceConfiguration
            = new DioritAPISourceConfiguration(
            "https://api.dioritb2b.ru",
            "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5ODE4YjUzMS00NDBhLTQ1ZWItYmU5Ni0xYTU0MDgwMTZiZWUiLCJqdGkiOiIzZTNmNTkwMDNlOTliZWVhODY3YjI2MzFiNWIyMGI1ZGI2MGQwM2E2MTVlZDIwNDlhMjM0YjZjZjFkOTFiYWJjMTRmYjEwNTY0YmE5MWQyZSIsImlhdCI6MTc0OTU1ODYyNy4yNjM4NywibmJmIjoxNzQ5NTU4NjI3LjI2Mzg3MSwiZXhwIjoxOTA3MzI1MDI3LjI1OTk4Miwic3ViIjoiOThjMzRjZDctY2FhOC00MWYxLTgxZDktMGRmZjg0ZDQwZDBkIiwic2NvcGVzIjpbIioiXX0.8zwiztyOEVi3XcIwjKDfR_raxzrQrUca1zO2FW0gPbqeCzJAh6_KkVEA8XimQaMfAQTUL6R5DDJY2MbmCw-TssLZvvsdDIDcGyEqcZmt8xCJxwyuffL4AResN077wJMUbSbquvvF2T4MQy8WeTy6zrTi5O7LiP7W-61aQyLsWzsV_c8i3uJU2_TrKaGbaBYPfS0p29Zsw09iVAKLJFHbrWVkXPXOGPgZiIic1_n0x6HD3cPcCP5KF_EwfBdjS7V0s88XZFc5V2N3fTHPhYong8NmpR7mtPlaB-Hd55LKuQg5rA9ZzNKg-Qcchiz76Vhd2JT9dm7o3qnYDA5d69mxqDXe60FjYl7FSExMRMawi2xsn2XAMPbvMTD4oUecJpqBX9IVeht3j6JyHGdpkcp4rif__EbszU7bysl5icWj-r4rcpcYpnPq03ME1PObiBtaabgfztSy5RyU0ZNiic28EJV2gxiF9SFDddo1AGjucoyngb_ziQDslSi4Y3n9-0L2sENhau5OLJO7PJ38AdP4JDjNK0gIee7XOnO8Vm5Voiyl7eA0nHVHjQKmqCJLeqsjf1ug2RiGdTmx6sSDrgzo2RHwYgFy452ap1ai_pjOHQjKBORxhKLRjx3ptyPfXm-0U4RrA1n_d-w8xLgRtOvRLoavavbxz0EEJIxSb5W0oEI",
            "Authorization",
            100 * 1024 * 1024
    );

    public DioritAPIClientImpl(WebClient webClient) {
        super(webClient);
    }

    public String gettingListOfProducts() {
        String responseFlux = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + "/api/products")
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

    public String viewProduct(String productId) {
        String responseFlux = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + "/api/products/" + productId)
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
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
    public List<DioritCategory> getAll() {
        List<DioritCategory> response = webClient.get()
                .uri(apiSourceConfiguration.baseUrl() + "/api/products")
                .header(apiSourceConfiguration.tokenHeaderKey(), apiSourceConfiguration.token())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<DioritCategory>>() {})
                .block();
//        System.out.println("Response: " + responseFlux);
        return response;
    }
}
