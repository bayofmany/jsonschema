package org.bayofmany.jsonschema.compiler;

import org.bayofmany.jsonschema.model.JsonSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bayofmany.jsonschema.compiler.Util.uri;

public class Dictionary {

    private static final Logger log = LoggerFactory.getLogger(Dictionary.class);

    private final Map<URI, JsonSchema> schemas = new HashMap<>();
    private final List<JsonSchema> rootSchemas = new ArrayList<>();

    public void add(JsonSchema schema) {
        URI uri = schema.meta.schemaRef;
        if (!uri.toString().contains("#")) {
            uri = uri(uri + "#");
        }

        JsonSchema old = schemas.put(uri, schema);
        if (old != null) {
            throw new IllegalArgumentException("override " + schema.meta.schemaRef);
        }
        if (schema.isObjectType() || (schema.isEnumeration() && schema.isRoot())) {
            if (schema.meta.name == null) {
                throw new IllegalArgumentException("no name for root schema");
            }
            this.rootSchemas.add(schema);
        }
    }

    public JsonSchema get(URI uri) {
        if (!uri.toString().contains("#")) {
            uri = uri(uri + "#");
        }

        JsonSchema jsonSchema = schemas.get(uri);
        if (jsonSchema != null) {
            return jsonSchema;
        }

        if (!uri.toString().contains(".json")) {
            URI tmp = uri(uri.toString().replace("#", ".json#"));
            jsonSchema = schemas.get(tmp);
            if (jsonSchema != null) {
                log.warn("Resolved {} by adding .json", uri);
                return jsonSchema;
            }
        }

        if (!uri.toString().contains(".schema.json")) {
            URI tmp = uri(uri.toString().replace("#", ".schema.json#"));
            jsonSchema = schemas.get(tmp);
            if (jsonSchema != null) {
                log.warn("Resolved {} with by .schema.json", uri);
                return jsonSchema;
            }
        }

        log.error("Cannot resolve {}", uri);

        return jsonSchema;
    }


    List<JsonSchema> getRootSchemas() {
        return rootSchemas;
    }
}
