package org.bayofmany.jsonschema.generator;

import org.bayofmany.jsonschema.model.JsonSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary {

    private final Map<String, JsonSchema> schemas = new HashMap<>();
    private final List<JsonSchema> rootSchemas = new ArrayList<>();

    public void add(JsonSchema schema) {
        JsonSchema old = schemas.put(schema.meta.schemaRef, schema);
        if (old != null) {
            throw new IllegalArgumentException("override " + schema.meta.schemaRef);
        }
        if (schema.isObjectType() || (schema.isEnumeration() && schema.isRoot()) ) {
            if (schema.meta.name == null) {
                throw new IllegalArgumentException("no name for root schema");
            }
            this.rootSchemas.add(schema);
        }
    }

    public JsonSchema get(String ref) {
        if (ref.endsWith(".json")) {
            ref += "#";
        }
        return schemas.get(ref);
    }


    List<JsonSchema> getRootSchemas() {
        return rootSchemas;
    }
}
