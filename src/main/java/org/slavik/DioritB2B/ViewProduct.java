package org.slavik.DioritB2B;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.sql.Connection;

public class ViewProduct {

    private Connection con;

    public ViewProduct(Connection connection) {
        this.con = connection;
    }

    public void receiving(String product_id) {
        WebClient webClient = WebClient.create();
        Flux<String> responseFlux = webClient.get()
                .uri("https://api.dioritb2b.ru/api/products/" + product_id)
                .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5ODE4YjUzMS00NDBhLTQ1ZWItYmU5Ni0xYTU0MDgwMTZiZWUiLCJqdGkiOiIzZTNmNTkwMDNlOTliZWVhODY3YjI2MzFiNWIyMGI1ZGI2MGQwM2E2MTVlZDIwNDlhMjM0YjZjZjFkOTFiYWJjMTRmYjEwNTY0YmE5MWQyZSIsImlhdCI6MTc0OTU1ODYyNy4yNjM4NywibmJmIjoxNzQ5NTU4NjI3LjI2Mzg3MSwiZXhwIjoxOTA3MzI1MDI3LjI1OTk4Miwic3ViIjoiOThjMzRjZDctY2FhOC00MWYxLTgxZDktMGRmZjg0ZDQwZDBkIiwic2NvcGVzIjpbIioiXX0.8zwiztyOEVi3XcIwjKDfR_raxzrQrUca1zO2FW0gPbqeCzJAh6_KkVEA8XimQaMfAQTUL6R5DDJY2MbmCw-TssLZvvsdDIDcGyEqcZmt8xCJxwyuffL4AResN077wJMUbSbquvvF2T4MQy8WeTy6zrTi5O7LiP7W-61aQyLsWzsV_c8i3uJU2_TrKaGbaBYPfS0p29Zsw09iVAKLJFHbrWVkXPXOGPgZiIic1_n0x6HD3cPcCP5KF_EwfBdjS7V0s88XZFc5V2N3fTHPhYong8NmpR7mtPlaB-Hd55LKuQg5rA9ZzNKg-Qcchiz76Vhd2JT9dm7o3qnYDA5d69mxqDXe60FjYl7FSExMRMawi2xsn2XAMPbvMTD4oUecJpqBX9IVeht3j6JyHGdpkcp4rif__EbszU7bysl5icWj-r4rcpcYpnPq03ME1PObiBtaabgfztSy5RyU0ZNiic28EJV2gxiF9SFDddo1AGjucoyngb_ziQDslSi4Y3n9-0L2sENhau5OLJO7PJ38AdP4JDjNK0gIee7XOnO8Vm5Voiyl7eA0nHVHjQKmqCJLeqsjf1ug2RiGdTmx6sSDrgzo2RHwYgFy452ap1ai_pjOHQjKBORxhKLRjx3ptyPfXm-0U4RrA1n_d-w8xLgRtOvRLoavavbxz0EEJIxSb5W0oEI")  // Замени токен!
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
