package org.slavik.ocs;

import org.springframework.context.annotation.Configuration;

@Configuration
public record OCSAPISourceConfiguration(
        String baseUrl,
        String token,
        String tokenHeaderKey,
        int maxInMemorySize
) { }