package org.bayofmany.jsonschema.testcases.metadata;

import com.google.common.base.Predicate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;

import static org.reflections.ReflectionUtils.forName;
import static org.reflections.ReflectionUtils.getFields;

public class MetadataKeywordsTest {

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.1
     */
    @Test
    public void testTitleAndDescription() throws IOException {
        load("title");
    }

    /**
     * @apiNote http://json-schema.org/latest/json-schema-validation.html#rfc.section.6.2
     */
    @Test
    public void testDefault() throws IOException {
        load("default");
    }

    @SafeVarargs
    private final Field getField(Class<?> type, Predicate<? super Field>... predicates) {
        Set<Field> fields = getFields(type, predicates);
        return fields.isEmpty() ? null : fields.iterator().next();
    }

    private Class<?> load(String test) throws IOException {
        return forName("org.bayofmany.jsonschema.testcases.metadata." + test.toLowerCase().replace("default", "defaultvalue") + ".Foo");
    }


}
