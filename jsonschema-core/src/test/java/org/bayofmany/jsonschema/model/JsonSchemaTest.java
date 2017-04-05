package org.bayofmany.jsonschema.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class JsonSchemaTest {

    @Test
    void testParse() throws IOException {
        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        JsonSchema schema = mapper.readValue(getClass().getResourceAsStream("simple.json"), JsonSchema.class);
        assertEquals("Example Schema", schema.title);
        assertEquals(SchemaType.OBJECT, schema.type[0]);

        Map<String, JsonSchema> additionalProperties = schema.properties.getAdditionalProperties();
        assertEquals(3, additionalProperties.size());

        JsonSchema ageProperty = additionalProperties.get("age");
        assertEquals(new Double(0), ageProperty.minimum);
        assertEquals(SchemaType.INTEGER, ageProperty.type[0]);
        assertEquals("Age in years", ageProperty.description);
    }
}
