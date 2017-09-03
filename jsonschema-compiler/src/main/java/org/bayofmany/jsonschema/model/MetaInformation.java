package org.bayofmany.jsonschema.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;
import org.bayofmany.jsonschema.compiler.Dictionary;
import org.bayofmany.jsonschema.compiler.Util;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bayofmany.jsonschema.compiler.Util.upperCaseFirst;
import static org.bayofmany.jsonschema.compiler.Util.uri;

public class MetaInformation {
    public final String packageName;

    public final String name;

    public final URI schemaRef;

    private final JsonSchema schema;

    private final JsonSchema parent;
    private final Dictionary dictionary;

    private TypeName typeName;
    public URI extendsRef;

    public MetaInformation(JsonSchema schema, JsonSchema parent, Dictionary dictionary, String name, String packageName, URI schemaRef) {
        this.schema = schema;
        this.parent = parent;

        this.dictionary = dictionary;
        this.name = name;
        this.packageName = packageName;
        this.schemaRef = schemaRef;

    }

    public TypeName getExtendsType() {
        if (extendsRef == null) {
            return null;
        }
        JsonSchema jsonSchema = dictionary.get(extendsRef);
        return jsonSchema.meta.getType();
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
            URI $ref = schema.getUniqueRef();
            if ($ref != null) {

                if ("#".equals($ref.toString())) {
                    typeName = parent.meta.getType();
                    return;
                }

                JsonSchema resolvedSchema = dictionary.get(schemaRef.resolve($ref));
                if (resolvedSchema != null) {
                    URI $ref2 = resolvedSchema.getUniqueRef();
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
                    if (resolvedSchema.isObjectType()) {
                        typeName = resolvedSchema.meta.getType();
                        return;
                    }
                    if ($ref.toString().startsWith("#/definitions/")) {
                        $ref = uri($ref.toString().substring(14));
                        typeName = ClassName.get(packageName, upperCaseFirst($ref.toString()));
                        return;
                    }

                    Matcher matcher = Pattern.compile("(.*).json[#]?$").matcher($ref.toString());
                    if (matcher.matches()) {
                        typeName = ClassName.get(packageName, upperCaseFirst(matcher.group(1)));
                        return;
                    }

                    matcher = Pattern.compile("(.*)#/definitions/([^/]*)$").matcher($ref.toString());
                    if (matcher.matches()) {
                        typeName = ClassName.get(packageName, upperCaseFirst(matcher.group(2)));
                        return;
                    }
                }
            }
        }

        if (schema.isPrimitiveType()) {
            typeName = getPrimitiveType();
            return;
        }

        if (schema.isExtendsType()) {
            typeName = ClassName.get(packageName, Util.upperCaseFirst(name));
            return;
        }

        if (schema.type == null) {
            typeName = TypeName.OBJECT;
            return;
        }

        switch (schema.type[0]) {
            case ARRAY:
                // @see http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.9
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

    private String normalizeRef(String $ref, String sourceRef) {
        if ($ref.contains(".json/#/")) {
            throw new IllegalArgumentException($ref);
            //$ref = $ref.replace(".json/#/", ".json#/");
        }

        if ($ref.contains(".json#")) {
            String sourceFile = StringUtils.substringBefore(sourceRef, ".json#");
            if (sourceFile.contains("/")) {
                return StringUtils.substringBeforeLast(sourceFile, "/") + "/" + $ref;
            } else {
                return $ref;
            }
        }

        if ($ref.startsWith("#") && parent != null) {
            URI parentRef = parent.meta.schemaRef;
            if (parentRef.toString().contains("#")) {
                return parentRef.toString().substring(0, parentRef.toString().indexOf("#")) + $ref;
            } else {
                return parentRef + "#" + $ref;
            }
        }

        return $ref;
    }


    @Override
    public String toString() {
        return schemaRef.toString();
    }

    public boolean isRoot() {
        return parent == null;
    }
}