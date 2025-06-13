package org.slavik;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public record ApiSourceConfiguration(
        @Value("diorit.api.url")
        String baseUrl,
        String token,
        String tokenHeaderKey,
        int maxInMemorySize
) { }