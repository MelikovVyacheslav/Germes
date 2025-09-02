package org.slavik.breez;

import java.io.IOException;
import com.fasterxml.jackson.databind.*;

import java.util.*;

public class Converter<V> {

    public Map<String, V> fromJsonString(String json, Class<V> valueType) throws IOException {
        final ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, valueType);
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            return null;
        }
    }

    public V fromJsonStringToObject(String json, String id, Class<V> valueType) throws IOException {
        Map<String, V> response = fromJsonString(json, valueType);
        if (response != null) {
            return response.get(id);
        } else {
            return null;
        }
    }
}
