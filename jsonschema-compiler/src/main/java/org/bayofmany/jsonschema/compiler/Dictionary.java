package org.bayofmany.jsonschema.compiler;

import org.bayofmany.jsonschema.model.JsonSchema;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bayofmany.jsonschema.compiler.Util.uri;

public class Dictionary {

    private final Map<URI, JsonSchema> schemas = new HashMap<>();
    private final List<JsonSchema> rootSchemas = new ArrayList<>();

    public void add(JsonSchema schema) {
        URI uri = schema.meta.schemaRef;
        if (uri.toString().endsWith(".json#")) {
            uri = uri(uri.toString().substring(0, uri.toString().length() - 1));
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

    public JsonSchema get(URI ref) {
        if (ref.toString().endsWith(".json#")) {
            ref = uri(ref.toString().substring(0, ref.toString().length() - 1));
        }
        return schemas.get(ref);
    }


    List<JsonSchema> getRootSchemas() {
        return rootSchemas;
    }
}
