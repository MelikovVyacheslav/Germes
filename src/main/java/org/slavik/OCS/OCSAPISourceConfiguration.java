package org.slavik.OCS;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public record OCSAPISourceConfiguration(
        @Value("ocs.api.url")
        String baseUrl,
        @Value("ocs.api.token")
        String token,
        @Value("ocs.api.headerTokenKey")
        String tokenHeaderKey,
        @Value("maxInMemorySize")
        int maxInMemorySize
) { }