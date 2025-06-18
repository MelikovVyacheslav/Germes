package org.slavik.DioritB2B;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public record DioritAPISourceConfiguration (
        String baseUrl,
        String token,
        String tokenHeaderKey,
        int maxInMemorySize
) { }