package org.bayofmany.jsonschema.cli;

import org.bayofmany.jsonschema.compiler.Compiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class Generate {

    public static void main(String[] args) throws IOException {
        String basePackage = args[0];
        File inputDir = new File(args[1]);
        File outputDir = new File(args[2]);

        Compiler generator = new Compiler(outputDir);

        Path root = Paths.get(inputDir.toURI());
        generator.generate(root, basePackage);
    }

}
