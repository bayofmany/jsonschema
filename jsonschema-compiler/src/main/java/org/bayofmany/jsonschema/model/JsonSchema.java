package org.bayofmany.jsonschema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.bayofmany.jsonschema.compiler.Dictionary;

import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"javaType", "javaName", "example", "nonUniqueArray", "uniqueArray", "complexTypesArray", "extends", "javaEnumNames", "javaJsonView", "booleanProperty", "stringProperty", "_name", "deserializationClassProperty", "enumProperty", "objectProperty", "anyProperty", "arrayProperty", "numberProperty", "integerProperty", "customDateTimePattern", "customTimezone", "extendsJavaClass", "smalsValidationClassName"})
public class JsonSchema {

    @JsonIgnore
    public MetaInformation meta;

    @JsonProperty
    private String id;

    @JsonProperty
    private String $schema;

    @JsonProperty
    public String title;

    @JsonProperty
    public String description;

    @JsonProperty
    private String $ref;

    @JsonProperty("default")
    private Object schemaDefault;

    @Min(1)
    @JsonProperty
    public Integer multipleOf;
    @JsonProperty
    public Double maximum;
    @JsonProperty
    public Boolean exclusiveMaximum;
    @JsonProperty
    public Double minimum;
    @JsonProperty
    public Boolean exclusiveMinimum;

    @Min(1)
    @JsonProperty
    public Integer maxLength;

    @Min(0)
    @JsonProperty
    public Integer minLength;

    @JsonProperty
    public String pattern;

    @JsonIgnore
    private JsonSchema additionalItemsSchema;

    @JsonIgnore
    private Boolean additionalItemsBoolean;

    @JsonProperty
    JsonSchema items;

    @Min(0)
    @JsonProperty
    public Integer maxItems;

    @JsonProperty
    public Integer minItems;

    @JsonProperty
    private Boolean uniqueItems;

    /*
                "contains": {
            "$ref": "#"
        },
             */

    @JsonProperty
    private Integer maxProperties;

    @JsonProperty
    private Integer minProperties;

    @JsonProperty
    public final Set<String> required = new HashSet<>();

    @JsonIgnore
    public JsonSchema additionalPropertiesSchema;

    @JsonIgnore
    public Boolean additionalPropertiesBoolean;

    @JsonProperty("definitions")
    public Extension definitions;

    @JsonProperty("properties")
    public Extension properties;

    @JsonProperty("patternProperties")
    private Extension patternProperties;

    @JsonProperty("dependencies")
    private Extension dependencies;

    /*
        }
    },
            "propertyNames": {
        "$ref": "#"
    },

           */

    @JsonProperty("const")
    private Object schemaConst;

    @JsonProperty("enum")
    public Object[] enumeration;

    @JsonProperty("x-bayofmany-enumLiterals")
    public String[] enumerationLiterals;

    @JsonProperty
    public SchemaType[] type;

    @JsonProperty
    private Media media;
    /*
            "type": {
        "anyOf": [
        {
            "$ref": "#/definitions/simpleTypes"
        },
        {
            "type": "array",
                "items": {
            "$ref": "#/definitions/simpleTypes"
        },
            "minItems": 1,
                "uniqueItems": true
        }
      ]
    },


    */

    @JsonProperty
    private String format;

    @JsonProperty
    private JsonSchema[] allOf;
    @JsonProperty
    private JsonSchema[] anyOf;
    @JsonProperty
    private JsonSchema[] oneOf;

    @JsonProperty
    private JsonSchema not;
    private String uniqueRef;


    @JsonProperty("additionalProperties")
    public void setAdditionalProperties(Object o) {
        if (o instanceof Boolean) {
            additionalPropertiesBoolean = (Boolean) o;
        } else {
            ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            additionalPropertiesSchema = mapper.convertValue(o, JsonSchema.class);
        }
    }

    @JsonProperty("additionalItems")
    public void setAdditionalItems(Object o) {
        if (o instanceof Boolean) {
            additionalItemsBoolean = (Boolean) o;
        } else {
            ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            additionalItemsSchema = mapper.convertValue(o, JsonSchema.class);
        }
    }

    public String getUniqueRef() {
        boolean onlyRef = $ref != null &&
                $schema == null &&
                additionalItemsBoolean == null &&
                additionalItemsSchema == null &&
                additionalPropertiesBoolean == null &&
                additionalPropertiesSchema == null &&
                allOf == null &&
                anyOf == null &&
                definitions == null &&
                dependencies == null &&
                description == null &&
                enumeration == null &&
                exclusiveMaximum == null &&
                exclusiveMinimum == null &&
                format == null &&
                id == null &&
                items == null &&
                maxItems == null &&
                maxLength == null &&
                maxProperties == null &&
                maximum == null &&
                media == null &&
                minItems == null &&
                minLength == null &&
                minProperties == null &&
                minimum == null &&
                multipleOf == null &&
                not == null &&
                oneOf == null &&
                pattern == null &&
                patternProperties == null &&
                properties == null &&
                schemaConst == null &&
                schemaDefault == null &&
                title == null &&
                type == null &&
                uniqueItems == null;

        return onlyRef ? $ref : null;
    }

    SchemaType getUniqueType() {
        boolean onlyType = $ref == null &&
                $schema == null &&
                additionalItemsBoolean == null &&
                additionalItemsSchema == null &&
                additionalPropertiesBoolean == null &&
                additionalPropertiesSchema == null &&
                allOf == null &&
                anyOf == null &&
                definitions == null &&
                dependencies == null &&
                description == null &&
                enumeration == null &&
                exclusiveMaximum == null &&
                exclusiveMinimum == null &&
                format == null &&
                id == null &&
                items == null &&
                maxItems == null &&
                maxLength == null &&
                maxProperties == null &&
                maximum == null &&
                media == null &&
                minItems == null &&
                minLength == null &&
                minProperties == null &&
                minimum == null &&
                multipleOf == null &&
                not == null &&
                oneOf == null &&
                pattern == null &&
                patternProperties == null &&
                properties == null &&
                schemaConst == null &&
                schemaDefault == null &&
                title == null &&
                type != null &&
                uniqueItems == null;

        return onlyType && type.length == 1 ? type[0] : null;

    }

    public boolean isRoot() {
        return meta.isRoot();
    }

    public boolean isInlineType() {
        return isSimpleType() && enumeration == null;
    }

    public boolean isArrayType() {
        return isType(SchemaType.ARRAY);
    }

    public boolean isNumericType() {
        return isType(SchemaType.INTEGER) || isType(SchemaType.NUMBER);
    }

    public boolean isStringType() {
        return isType(SchemaType.STRING);
    }

    public boolean isObjectType() {
        if (type == null && properties != null) {
            type = new SchemaType[]{SchemaType.OBJECT};
        }

        return isType(SchemaType.OBJECT);
    }

    private boolean isType(SchemaType t) {
        return type != null && type.length == 1 && type[0] == t;
    }


    public boolean isEnumeration() {
        return enumeration != null;
    }

    private boolean isSimpleType() {
        if (type != null && type.length == 1) {
            switch (type[0]) {
                case INTEGER:
                case STRING:
                case BOOLEAN:
                case NUMBER:
                    return true;
                case OBJECT:
                    return getUniqueType() == SchemaType.OBJECT;
                case ARRAY:
                    if (items != null && items.type != null && items.type.length == 1) {
                        switch (items.type[0]) {
                            case INTEGER:
                            case STRING:
                            case BOOLEAN:
                            case NUMBER:
                                return true;
                            case OBJECT:
                                return getUniqueType() == SchemaType.OBJECT;
                        }
                    }
            }
        }
        return false;
    }

    public boolean isPrimitiveType() {
        if (type != null && type.length == 1) {
            switch (type[0]) {
                case INTEGER:
                case STRING:
                case BOOLEAN:
                case NUMBER:
                    return true;
            }
        }
        return false;
    }

    public void visitSchemaDefinitions(String fileName, String packageName, Dictionary dictionary) {
        String name = StringUtils.substringAfterLast(StringUtils.removeEnd(StringUtils.removeEnd(fileName, ".schema.json"), ".json"), "/");
        meta = new MetaInformation(this, null, dictionary, name, packageName, fileName + "#");
        visitSchemaDefinitions(this, this, fileName + "#", packageName, dictionary);
        dictionary.add(this);
    }


    private static void visitSchemaDefinitions(JsonSchema schema, JsonSchema parent, String schemaRef, String packageName, Dictionary dictionary) {
        if (schema.definitions != null) {
            schema.definitions.getAdditionalProperties().forEach((key, s) -> {
                String ref = schemaRef + "/definitions/" + key;

                s.meta = new MetaInformation(s, parent, dictionary, key, packageName, ref);
                visitSchemaDefinitions(s, parent, ref, packageName, dictionary);
                dictionary.add(s);
            });
        }
        if (schema.items != null) {
            String ref = schemaRef + "/items";

            schema.items.meta = new MetaInformation(schema.items, parent, dictionary, schema.meta.name + "Item", packageName, ref);
            visitSchemaDefinitions(schema.items, parent, ref, packageName, dictionary);
            dictionary.add(schema.items);
        }
        if (schema.properties != null) {
            schema.properties.getAdditionalProperties().forEach((key, s) -> {
                String ref = schemaRef + "/properties/" + key;

                s.meta = new MetaInformation(s, parent, dictionary, key, packageName, ref);
                visitSchemaDefinitions(s, parent, ref, packageName, dictionary);
                dictionary.add(s);
            });
        }

        if (schema.additionalPropertiesSchema != null) {
            schema.additionalPropertiesSchema.meta = new MetaInformation(schema.additionalPropertiesSchema, parent, dictionary, "additionalProperties", packageName, schemaRef + "#/additionalProperties");
            visitSchemaDefinitions(schema.additionalPropertiesSchema, parent, "tmp", packageName, dictionary);
            dictionary.add(schema.additionalPropertiesSchema);
        }
    }

    /*-public void visitSchemas(Consumer<JsonSchema> c) {
        this.definitions.getAdditionalProperties().values().forEach(c);
        this.properties.getAdditionalProperties().values().forEach(c);
        c.accept(this.additionalItemsSchema);
        Arrays.stream(this.allOf).forEach(c);
        Arrays.stream(this.anyOf).forEach(c);
        Arrays.stream(this.oneOf).forEach(c);
        c.accept(this.not);
        c.accept(this.items);
    }*/

    boolean hasUniqueItems() {
        return uniqueItems == Boolean.TRUE;
    }

    @Override
    public String toString() {
        return meta == null ? "$ref unknown" : meta.schemaRef;
    }

}
