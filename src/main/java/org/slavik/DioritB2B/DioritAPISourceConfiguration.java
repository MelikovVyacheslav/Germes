package org.slavik.DioritB2B;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public record DioritAPISourceConfiguration (
        @Value("${diorit.api.url}")
        String baseUrl,
        @Value("${diorit.api.token}")
        String token,
        @Value("${diorit.api.headerTokenKey}")
        String tokenHeaderKey,
        @Value("${maxInMemorySize}")
        int maxInMemorySize
) { }