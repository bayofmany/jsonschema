package org.bayofmany.jsonschema;

import java.net.URI;
import java.net.URISyntaxException;

public class Snippets {

    public static void main(String[] args) throws URISyntaxException {
        URI uri = new URI("file:///C:/dev/prj/jsonschema/jsonschema-compiler/src/test/resources/schemas/ref/bar/bar.json#/properties/qux");
        URI uri2 = new URI("../qux/qux.json#blabla");
        URI uri3 = new URI("file:///C:/dev/prj/jsonschema/jsonschema-compiler/src/test/resources/schemas/ref/qux/qux.json#");
        System.out.println(uri3.normalize());

        System.out.println(uri.resolve(uri2));
        System.out.println(uri2.relativize(uri));
        System.out.println(uri3.relativize(uri));
        System.out.println(uri.relativize(uri3));
        System.out.println(uri.isOpaque());
        System.out.println(uri2.isOpaque());
        System.out.println(uri3.isOpaque());

        System.out.println(uri3);
    }
}
