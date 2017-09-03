package org.bayofmany.jsonschema.testcases.ref;

import com.google.common.base.Predicate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.reflections.ReflectionUtils.*;

public class ReferenceTest {

    @Test
    public void testRelative() throws IOException {
        Class<?> foo = load("relative");
        Class<?> bar = load("relative", "bar.Bar");
        Class<?> baz = load("relative", "bar.baz.Baz");
        Class<?> qux = load("relative", "qux.Qux");
        assertNotNull(getField(foo, withName("bar"), withType(bar)));
        assertNotNull(getField(foo, withName("baz"), withType(baz)));
        assertNotNull(getField(foo, withName("qux"), withType(qux)));
        assertNotNull(getField(bar, withName("foo"), withType(foo)));
        assertNotNull(getField(bar, withName("baz"), withType(baz)));
        assertNotNull(getField(bar, withName("qux"), withType(qux)));
        assertNotNull(getField(baz, withName("foo"), withType(foo)));
        assertNotNull(getField(baz, withName("bar"), withType(bar)));
        assertNotNull(getField(baz, withName("qux"), withType(qux)));
        assertNotNull(getField(qux, withName("foo"), withType(foo)));
        assertNotNull(getField(qux, withName("bar"), withType(bar)));
        assertNotNull(getField(qux, withName("baz"), withType(baz)));
    }

    @Test
    public void testSelf() throws IOException {
        Class<?> foo = load("self");
        assertNotNull(getField(foo, withName("foo"), withType(foo)));
    }

    @SafeVarargs
    private final Field getField(Class<?> type, Predicate<? super Field>... predicates) {
        Set<Field> fields = getFields(type, predicates);
        return fields.isEmpty() ? null : fields.iterator().next();
    }

    private Class<?> load(String test) throws IOException {
        return forName("org.bayofmany.jsonschema.testcases.ref." + test.toLowerCase().replace("default", "defaultvalue") + ".Foo");
    }

    private Class<?> load(String test, String classname) throws IOException {
        return forName("org.bayofmany.jsonschema.testcases.ref." + test.toLowerCase().replace("default", "defaultvalue") + "." + classname);
    }

}
