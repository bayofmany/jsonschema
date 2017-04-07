package org.bayofmany.jsonschema.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SchemaType {

    @JsonProperty("array") ARRAY,
    @JsonProperty("boolean") BOOLEAN,
    @JsonProperty("integer") INTEGER,
    @JsonProperty("null") NULL,
    @JsonProperty("number") NUMBER,
    @JsonProperty("object") OBJECT,
    @JsonProperty("string") STRING

}
