package org.bayofmany.jsonschema.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.bayofmany.jsonschema.compiler.Dictionary;
import org.bayofmany.jsonschema.compiler.Util;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bayofmany.jsonschema.compiler.Util.upperCaseFirst;

public class MetaInformation {
    public final String packageName;

    public final String name;

    public final String schemaRef;

    private final JsonSchema schema;

    private final JsonSchema parent;
    private final Dictionary dictionary;

    private TypeName typeName;

    public MetaInformation(JsonSchema schema, JsonSchema parent, Dictionary dictionary, String name, String packageName, String schemaRef) {
        this.schema = schema;
        this.parent = parent;

        this.dictionary = dictionary;
        this.name = name;
        this.packageName = packageName;
        this.schemaRef = schemaRef;

    }

    public TypeName getType() {
        if (typeName == null) {
            setType();
        }
        return typeName;
    }


    private void setType() {
        if (schema.isEnumeration() && schema.isRoot()) {
            typeName = ClassName.get(packageName, upperCaseFirst(name));
            return;
        }

        if (schema.type == null) {
            String $ref = schema.getUniqueRef();
            if ($ref != null) {
                $ref = normalizeRef($ref);
                JsonSchema resolvedSchema = dictionary.get($ref);
                if (resolvedSchema != null) {
                    String $ref2 = resolvedSchema.getUniqueRef();
                    if ($ref2 != null) {
                        JsonSchema resolvedSchema2 = dictionary.get($ref2);
                        typeName = resolvedSchema2.meta.getType();
                        return;
                    }
                    if (resolvedSchema.isArrayType()) {
                        typeName = resolvedSchema.meta.getType();
                        return;
                    }
                    if (resolvedSchema.isInlineType()) {
                        typeName = resolvedSchema.meta.getType();
                        return;
                    }
                    if ($ref.startsWith("#/definitions/")) {
                        $ref = $ref.substring(14);
                        typeName = ClassName.get(packageName, upperCaseFirst($ref));
                        return;
                    }

                    Matcher matcher = Pattern.compile("(.*).json[#]?$").matcher($ref);
                    if (matcher.matches()) {
                        typeName = ClassName.get(packageName, upperCaseFirst(matcher.group(1)));
                        return;
                    }

                    matcher = Pattern.compile("(.*)#/definitions/([^/]*)$").matcher($ref);
                    if (matcher.matches()) {
                        typeName = ClassName.get(packageName, upperCaseFirst(matcher.group(2)));
                        return;
                    }
                }
            }
            typeName = TypeName.OBJECT;
            return;
        }

        if (schema.isPrimitiveType()) {
            typeName = getPrimitiveType();
            return;
        }

        switch (schema.type[0]) {
            case ARRAY:
                // @see http://json-schema.org/latest/json-schema-validation.html#rfc.section.5.12
                TypeName itemType = schema.items == null ? TypeName.OBJECT : schema.items.meta.getType();
                typeName = ParameterizedTypeName.get(ClassName.get(schema.hasUniqueItems() ? Set.class : List.class), itemType);
                break;
            case NULL:
                throw new IllegalArgumentException("null");
            case OBJECT:
                if (schema.getUniqueType() == SchemaType.OBJECT) {
                    typeName = TypeName.OBJECT;
                } else {
                    typeName = ClassName.get(packageName, Util.upperCaseFirst(name));
                }
                break;
        }

        if (typeName == null) {
            typeName = TypeName.OBJECT;
        }
    }

    public TypeName getPrimitiveType() {
        switch (schema.type[0]) {
            case BOOLEAN:
                return TypeName.BOOLEAN.box();
            case INTEGER:
                return TypeName.LONG.box();
            case NUMBER:
                return TypeName.DOUBLE.box();
            case STRING:
                return TypeName.get(String.class);
            default:
                throw new IllegalArgumentException("Not a simple type " + schema.type[0]);
        }
    }

    private String normalizeRef(String $ref) {
        if ($ref.contains(".json/#/")) {
            $ref = $ref.replace(".json/#/", ".json#/");
        }
        if (!$ref.startsWith("#") || parent == null) {
            return $ref;
        }
        String parentRef = parent.meta.schemaRef;
        if (parentRef.contains("#")) {
            return parentRef.substring(0, parentRef.indexOf("#")) + $ref;
        } else {
            return parentRef + "#" + $ref;
        }
    }


    @Override
    public String toString() {
        return schemaRef;
    }

    public boolean isRoot() {
        return parent == null;
    }
}