package org.slavik.OCS;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.sql.Connection;

public class GettingInformationByProductCategory {

    private final Connection con;

    public GettingInformationByProductCategory(Connection connection) {
        this.con = connection;
    }

    public void receiving(String category) {
        WebClient webClient = WebClient.builder()
                .codecs(configure -> configure
                        .defaultCodecs()
                        .maxInMemorySize(99999999)
                ).build();
        Flux<String> responseFlux = webClient.get()
                .uri("https://connector.b2b.ocs.ru/api/v2/catalog/categories/" + category + "/products")
                .header("X-API-Key", "TSWJXggwvt59l9nuYVvtSM?iyea0DR")
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
