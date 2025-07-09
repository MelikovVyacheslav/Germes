package org.slavik.dioritB2B.model;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;

@JsonDeserialize(using = Attribute.Deserializer.class)
@JsonSerialize(using = Attribute.Serializer.class)
public class Attribute {
    public Double doubleValue;
    public String stringValue;

    static class Deserializer extends JsonDeserializer<Attribute> {
        @Override
        public Attribute deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            Attribute value = new Attribute();
            switch (jsonParser.currentToken()) {
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                    value.doubleValue = jsonParser.readValueAs(Double.class);
                    break;
                case VALUE_STRING:
                    String string = jsonParser.readValueAs(String.class);
                    value.stringValue = string;
                    break;
                default: throw new IOException("Cannot deserialize Attribute");
            }
            return value;
        }
    }

    static class Serializer extends JsonSerializer<Attribute> {
        @Override
        public void serialize(Attribute obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (obj.doubleValue != null) {
                jsonGenerator.writeObject(obj.doubleValue);
                return;
            }
            if (obj.stringValue != null) {
                jsonGenerator.writeObject(obj.stringValue);
                return;
            }
            throw new IOException("Attribute must not be null");
        }
    }
}
