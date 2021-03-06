package org.bayofmany.jsonschema.testcases.validation;

import com.google.common.base.Predicate;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.bayofmany.jsonschema.validation.constraints.MaxItems;
import org.bayofmany.jsonschema.validation.constraints.MinItems;
import org.bayofmany.jsonschema.validation.constraints.MultipleOf;
import org.junit.jupiter.api.Test;
import org.reflections.ReflectionUtils;

import javax.validation.constraints.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.reflections.ReflectionUtils.*;

@SuppressWarnings("WeakerAccess")
public class ValidationKeywordsTest {

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.1
     */
    @Test
    public void testMultipleOf() throws IOException, URISyntaxException {
        Class<?> subject = load("multipleOf");

        Field field = getField(subject, withName("bar"), withAnnotation(MultipleOf.class));
        assertNotNull(field);
        assertEquals(3, field.getAnnotation(MultipleOf.class).value());
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.2
     */
    @Test
    public void testMaximum() throws IOException, URISyntaxException {
        Class<?> subject = load("maximum");

        Field field = getField(subject, withName("bar"), withAnnotation(Max.class));
        assertNotNull(field);
        assertEquals(10, field.getAnnotation(Max.class).value());

        field = getField(subject, withName("baz"), withAnnotation(DecimalMax.class));
        assertNotNull(field);
        assertEquals("10.5", field.getAnnotation(DecimalMax.class).value());

        field = getField(subject, withName("qux"), withAnnotation(DecimalMax.class));
        assertNull(field);
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.3
     */
    @Test
    public void testExclusiveMaximum() throws IOException, URISyntaxException {
        Class<?> subject = load("exclusiveMaximum");

        Field field = getField(subject, withName("bar"), withAnnotation(DecimalMax.class));
        assertNotNull(field);
        assertEquals(false, field.getAnnotation(DecimalMax.class).inclusive());

        field = getField(subject, withName("baz"), withAnnotation(Max.class));
        assertNotNull(field);

        field = getField(subject, withName("qux"), withAnnotation(Max.class));
        assertNotNull(field);
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.4
     */
    @Test
    public void testMinimum() throws IOException, URISyntaxException {
        Class<?> subject = load("minimum");

        Field field = getField(subject, withName("bar"), withAnnotation(Min.class));
        assertNotNull(field);
        assertEquals(10, field.getAnnotation(Min.class).value());

        field = getField(subject, withName("baz"), withAnnotation(DecimalMin.class));
        assertNotNull(field);
        assertEquals("10.5", field.getAnnotation(DecimalMin.class).value());

        field = getField(subject, withName("qux"), withAnnotation(DecimalMin.class));
        assertNull(field);
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.5
     */
    @Test
    public void testExclusiveMinimum() throws IOException, URISyntaxException {
        Class<?> subject = load("exclusiveMinimum");

        Field field = getField(subject, withName("bar"), withAnnotation(DecimalMin.class));
        assertNotNull(field);
        assertEquals(false, field.getAnnotation(DecimalMin.class).inclusive());

        field = getField(subject, withName("baz"), withAnnotation(Min.class));
        assertNotNull(field);

        field = getField(subject, withName("qux"), withAnnotation(Min.class));
        assertNotNull(field);
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.6
     */
    @Test
    public void testMaxLength() throws IOException, URISyntaxException {
        Class<?> subject = load("maxLength");

        Field field = getField(subject, withName("bar"), withAnnotation(Size.class));
        assertNotNull(field);
        assertEquals(10, field.getAnnotation(Size.class).max());

        field = getField(subject, withName("baz"), withAnnotation(Size.class));
        assertNull(field);
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.7
     */
    @Test
    public void testMinLength() throws IOException, URISyntaxException {
        Class<?> subject = load("minLength");

        Field field = getField(subject, withName("bar"), withAnnotation(Size.class));
        assertNotNull(field);
        assertEquals(10, field.getAnnotation(Size.class).min());

        field = getField(subject, withName("baz"), withAnnotation(Size.class));
        assertNull(field);
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.8
     */
    @Test
    public void testPattern() throws IOException, URISyntaxException {
        Class<?> subject = load("pattern");

        Field field = getField(subject, withName("bar"), withAnnotation(Pattern.class));
        assertNotNull(field);
        assertEquals("[0-9]+", field.getAnnotation(Pattern.class).regexp());

        field = getField(subject, withName("baz"), withAnnotation(Pattern.class));
        assertNull(field);
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.9
     */
    @Test
    public void testItems() {
        Class<?> subject = load("items");

        Field field = getField(subject, withName("bar"));
        assertNotNull(field);
        assertEquals("java.util.List<java.lang.String>", field.getGenericType().getTypeName());

        field = getField(subject, withName("baz"));
        assertNotNull(field);
        assertEquals("java.util.List<org.bayofmany.jsonschema.testcases.validation.items.BazItem>", field.getGenericType().getTypeName());

        field = getField(subject, withName("qux"));
        assertNotNull(field);
        assertEquals("java.util.List<org.bayofmany.jsonschema.testcases.validation.items.Qux>", field.getGenericType().getTypeName());
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.10
     */
    @Test
    public void testAdditionalItems() throws IOException, URISyntaxException {
        load("additionalItems");
        // TODO
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.11
     */
    @Test
    public void testMaxItems() throws IOException, URISyntaxException {
        Class<?> subject = load("maxItems");

        Field field = getField(subject, withName("bar"), withAnnotation(MaxItems.class));
        assertNotNull(field);
        assertEquals(10, field.getAnnotation(MaxItems.class).value());
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.12
     */
    @Test
    public void testMinItems() throws IOException, URISyntaxException {
        Class<?> subject = load("minItems");

        Field field = getField(subject, withName("bar"), withAnnotation(MinItems.class));
        assertNotNull(field);
        assertEquals(10, field.getAnnotation(MinItems.class).value());
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.13
     */
    @Test
    public void testUniqueItems() throws IOException, URISyntaxException {
        Class<?> subject = load("uniqueItems");

        Field field = getField(subject, withName("bar"));
        assertNotNull(field);
        assertEquals("java.util.Set<java.lang.Object>", field.getGenericType().getTypeName());

        field = getField(subject, withName("baz"));
        assertNotNull(field);
        assertEquals("java.util.List<java.lang.Object>", field.getGenericType().getTypeName());
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.14
     */
    @Test
    @Ignore
    public void testContains() throws IOException, URISyntaxException {
        // Validation rule not mapped to generated class
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.15
     */
    @Test
    @Ignore
    public void testMaxProperties() throws IOException, URISyntaxException {
        // Validation rule not mapped to generated class
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.16
     */
    @Test
    @Ignore
    public void testMinProperties() throws IOException, URISyntaxException {
        // Validation rule not mapped to generated class
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.17
     */
    @Test
    public void testRequired() throws IOException, URISyntaxException {
        Class<?> subject = load("required");

        assertNotNull(getField(subject, withName("bar"), withAnnotation(NotNull.class)));
        assertNotNull(getField(subject, withName("qux"), withAnnotation(NotNull.class)));
        assertNull(getField(subject, withName("baz"), withAnnotation(NotNull.class)));
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.18
     */
    @Test
    public void testProperties() throws IOException, URISyntaxException {
        Class<?> subject = load("required");

        assertNotNull(getField(subject, withName("bar")));
        assertNotNull(getMethod(subject, withName("setBar"), withParameters(Long.class)));
        assertNotNull(getMethod(subject, withName("getBar"), withReturnType(Long.class)));

        assertNotNull(getField(subject, withName("qux")));
        assertNotNull(getMethod(subject, withName("setQux"), withParameters(String.class)));
        assertNotNull(getMethod(subject, withName("getQux"), withReturnType(String.class)));

        assertNotNull(getField(subject, withName("baz")));
        assertNotNull(getMethod(subject, withName("setBaz"), withParameters(Double.class)));
        assertNotNull(getMethod(subject, withName("getBaz"), withReturnType(Double.class)));
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.19
     */
    @Test
    public void testPatternProperties() throws IOException, URISyntaxException {
        // TODO
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.20
     */
    @Test
    public void testAdditionalProperties() throws IOException, URISyntaxException {
        Class<?> subject = load("additionalProperties");

        //noinspection unchecked
        assertTrue(getFields(subject).isEmpty());

        Field field = getField(load("additionalProperties", "Bar"), withName("additionalProperties"));
        assertNotNull(field);
        assertEquals("java.util.Map<java.lang.String, java.lang.Object>", field.getGenericType().getTypeName());

        field = getField(load("additionalProperties", "Baz"), withName("additionalProperties"));
        assertNotNull(field);
        assertEquals("java.util.Map<java.lang.String, java.lang.String>", field.getGenericType().getTypeName());

        field = getField(load("additionalProperties", "Qux"), withName("_additionalProperties"));
        assertNotNull(field);
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.21
     */
    @Test
    public void testDependencies() {
        // TODO
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.22
     */
    @Test
    @Ignore
    public void testPropertyNames() {
        // Validation rule not mapped to generated class
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.23
     */
    @Test
    public void testEnum() throws IOException, URISyntaxException {
        Class<?> foo = load("enum");
        assertTrue(foo.isEnum());

        //noinspection unchecked
        assertEquals(3, getFields(foo, withType(foo)).size());

        assertNotNull(getField(foo, withName("one")));
        assertNotNull(getField(foo, withName("two")));
        assertNotNull(getField(foo, withName("three")));

        Class<?> bar = load("enum", "Bar");
        assertTrue(bar.isEnum());

        //noinspection unchecked
        assertEquals(4, getFields(bar, withType(bar)).size());

        assertNotNull(getField(bar, withName("_1")));
        assertNotNull(getField(bar, withName("_2_TWO")));
        assertNotNull(getField(bar, withName("_3_THREE")));
        assertNotNull(getField(bar, withName("NULL")));
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.24
     */
    @Test
    public void testConst() {
        // TODO
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.25
     */
    @Test
    public void testType() throws IOException, URISyntaxException {
        Class<?> foo = load("type");

        assertNotNull(getField(foo, withName("aNumber"), withType(Double.class)));
        assertNotNull(getField(foo, withName("anInteger"), withType(Long.class)));
        assertNotNull(getField(foo, withName("aBoolean"), withType(Boolean.class)));
        assertNotNull(getField(foo, withName("anObject"), withType(Object.class)));
        assertNotNull(getField(foo, withName("aString"), withType(String.class)));

        Field anArray = getField(foo, withName("anArray"));
        assertNotNull(anArray);
        assertEquals("java.util.List<java.lang.Object>", anArray.getGenericType().getTypeName());
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.26
     */
    @Test
    public void testAllOf() throws IOException, URISyntaxException {
        Class<?> baz = load("allOf", "Baz");
        assertEquals("org.bayofmany.jsonschema.testcases.validation.allof.Foo", baz.getSuperclass().getName());
    //    assertNotNull(getField(baz, withName("qux"), withType(String.class)));
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.27
     */
    @Test
    public void testAnyOf() throws IOException, URISyntaxException {
        // TODO
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.28
     */
    @Test
    public void testOneOf() throws IOException, URISyntaxException {
        // TODO
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.29
     */
    @Test
    public void testNot() throws IOException, URISyntaxException {
        // TODO
    }

    @SafeVarargs
    @SuppressWarnings("Guava")
    private final Field getField(Class<?> type, Predicate<? super Field>... predicates) {
        Set<Field> fields = ReflectionUtils.getFields(type, predicates);
        return fields.isEmpty() ? null : fields.iterator().next();
    }

    @SafeVarargs
    @SuppressWarnings("Guava")
    private final Method getMethod(Class<?> type, Predicate<? super Method>... predicates) {
        Set<Method> methods = getMethods(type, predicates);
        return methods.isEmpty() ? null : methods.iterator().next();
    }


    private Class<?> load(String test) {
        return forName("org.bayofmany.jsonschema.testcases.validation." + test.toLowerCase().replace("enum", "enumeration") + ".Foo");
    }

    private Class<?> load(String test, String className) {
        return forName("org.bayofmany.jsonschema.testcases.validation." + test.toLowerCase().replace("enum", "enumeration") + "." + className);
    }


}
