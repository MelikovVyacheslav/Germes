package org.slavik.OCS;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.sql.Connection;
import java.util.*;

public class GettingInformationByProductsCategories {

    private final Connection con;

    public GettingInformationByProductsCategories(Connection connection) {
        this.con = connection;
    }

    public void receiving(List<String> batch_categories) {
        WebClient webClient = WebClient.builder()
                .codecs(configure -> configure
                        .defaultCodecs()
                        .maxInMemorySize(99999999)).build();

        Mono<String> responseFlux = webClient.post()
                .uri("https://connector.b2b.ocs.ru/api/v2/catalog/categories/batch/products")
                .header("X-API-Key", "TSWJXggwvt59l9nuYVvtSM?iyea0DR")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(batch_categories)
                .retrieve()
                .bodyToMono(String.class);

        responseFlux.subscribe(
                json -> System.out.println("Received: " + json),
                error -> System.err.println("FULL ERROR: " + error),
                () -> System.out.println("Done")
        );
    }
}
