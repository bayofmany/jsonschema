package org.bayofmany.jsonschema.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Extension {

    @JsonIgnore
    private final Map<String, JsonSchema> additionalProperties = new HashMap<>();

    @JsonIgnore
    private final Map<String, String[]> additionalProperties2 = new HashMap<>();

    @JsonAnyGetter
    public Map<String, JsonSchema> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        if (value instanceof Map) {
            ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            JsonSchema jsonSchema = mapper.convertValue(value, JsonSchema.class);
            additionalProperties.put(name, jsonSchema);
        } else if (value instanceof List) {
            List<String> list = (List<String>) value;
            additionalProperties2.put(name, list.toArray(new String[list.size()]));
        }
    }


}
