package org.slavik.OCS;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public record OCSAPISourceConfiguration(
        String baseUrl,
        String token,
        String tokenHeaderKey,
        int maxInMemorySize
) { }