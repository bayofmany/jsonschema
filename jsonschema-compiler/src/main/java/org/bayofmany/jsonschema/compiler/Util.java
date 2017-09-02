package org.bayofmany.jsonschema.compiler;

import java.net.URI;
import java.net.URISyntaxException;

public class Util {

    public static String upperCaseFirst(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static URI uri(String value) {
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI: " + value);
        }
    }

    public static URI uri(URI parent, String relative) {
        if (parent.toString().contains("#")) {
            return uri(parent.toString() + "/" + relative);
        } else {
            return uri(parent.toString() + "#" + relative);
        }
    }
}
