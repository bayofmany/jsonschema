package org.bayofmany.jsonschema.cli;

import org.bayofmany.jsonschema.compiler.Compiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

class Generate {

    public static void main(String[] args) throws IOException {
        String basePackage = args[0] + ".";
        File inputDir = new File(args[1]);
        File outputDir = new File(args[2]);

        Compiler generator = new Compiler(outputDir);

        Path root = Paths.get(inputDir.toURI());

        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                System.out.println(dir);
                generator.generate(dir, basePackage + root.relativize(dir).toString().replace("/", ".").replace("\\", ".").replace("default", "defaultvalue").replace("enum", "enumeration").toLowerCase());
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
