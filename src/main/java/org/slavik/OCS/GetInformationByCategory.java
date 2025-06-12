package org.slavik.OCS;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import java.sql.Connection;

public class GetInformationByCategory {

    private final Connection con;

    public GetInformationByCategory(Connection connection) {
        this.con = connection;
    }

    public void receiving() {
        WebClient webClient = WebClient.create();
        Flux<String> responseFlux = webClient.get()
                .uri("https://connector.b2b.ocs.ru/api/v2/catalog/categories")
                .header("X-API-Key", "TSWJXggwvt59l9nuYVvtSM?iyea0DR")  // Замени токен!
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(String.class);

        responseFlux.subscribe(
                json -> System.out.println("Received: " + json),
                error -> System.err.println("FULL ERROR: " + error),  // Выведет полный stacktrace
                () -> System.out.println("Done")
        );
    }
}
