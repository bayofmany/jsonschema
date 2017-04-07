package org.bayofmany.jsonschema;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.bayofmany.jsonschema.compiler.Compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "compile", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
class JsonSchemaCompilerMojo
        extends AbstractMojo {

    /**
     * Location of the Json schema files.
     */
    @Parameter(defaultValue = "${basedir}/src/main/resources/META-INF/schemas", property = "schemaDir", required = true)
    private File inputDirectory;

    /**
     * Location of the output directory.
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/jsc", property = "outputDir", required = true)
    private File outputDirectory;

    /**
     * Base package name from java class generation.
     */
    @Parameter(defaultValue = "org.bayofmany.jsonschema", property = "packageName", required = true)
    private String packageName;

    public void execute()
            throws MojoExecutionException {

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        FileWriter w = null;
        try {
            Compiler generator = new Compiler(outputDirectory);
            generator.generate(inputDirectory.toPath(), packageName);
        } catch (IOException e) {
            throw new MojoExecutionException("Error compiling json schemas", e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
