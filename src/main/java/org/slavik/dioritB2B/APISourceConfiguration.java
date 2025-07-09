package org.slavik.dioritB2B;

import org.springframework.context.annotation.Configuration;

@Configuration
public record APISourceConfiguration(
        String baseUrl,
        String token,
        String tokenHeaderKey,
        int maxInMemorySize
) { }