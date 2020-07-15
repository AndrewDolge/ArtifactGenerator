/**
 *    Copyright 2020 Andrew Dolge
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package io.github.andrewdolge.artifactgenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

import io.github.andrewdolge.artifactgenerator.Artifact.ArtifactBuilder;
import io.github.andrewdolge.artifactgenerator.components.IArtifactComponentFactory;
import io.github.andrewdolge.artifactgenerator.components.json.JsonArtifactComponentFactory;

public class JsonArtifactTest {
    @Test
    public void testJsonArtifact() throws FileNotFoundException {

        ArtifactBuilder artifactBuilder = new ArtifactBuilder();

        IArtifactComponentFactory factory = new JsonArtifactComponentFactory(
                new FileInputStream("src/test/resources/SerializedIndie.json"));

        artifactBuilder.withComponentFactory(factory).withArtifactConsumer(ArtifactConsumer.PrintToConsole());
        Artifact actual = artifactBuilder.build();
        System.out.println("Independent Data Test");
        actual.output();

    }

    @Test
    public void testJsonArtifactSelector() throws FileNotFoundException {
        ArtifactBuilder artifactBuilder = new ArtifactBuilder();

        IArtifactComponentFactory factory = new JsonArtifactComponentFactory(
                new FileInputStream("src/test/resources/SerializedSelector.json"));

        artifactBuilder.withComponentFactory(factory).withArtifactConsumer(ArtifactConsumer.PrintToConsole());
        Artifact actual = artifactBuilder.build();
        System.out.println("Selector Data Test");
        actual.output();
    }

    @Test
    public void testJsonArtifactExclusive() throws FileNotFoundException {
        ArtifactBuilder artifactBuilder = new ArtifactBuilder();

        IArtifactComponentFactory factory = new JsonArtifactComponentFactory(
                new FileInputStream("src/test/resources/SerializedIndie.json"),
                new FileInputStream("src/test/resources/SerializedExclusive.json"),
                new FileInputStream("src/test/resources/SerializedSelector.json"));

        artifactBuilder.withComponentFactory(factory).withArtifactConsumer(ArtifactConsumer.PrintToConsole());
        Artifact actual = artifactBuilder.build();
        System.out.println("Selector Exclusive Test");
        actual.output();
    }

    @Test
    public void testJsonArtifactDependent() throws FileNotFoundException {
        ArtifactBuilder artifactBuilder = new ArtifactBuilder();

        IArtifactComponentFactory factory = new JsonArtifactComponentFactory(
                new FileInputStream("src/test/resources/SerializedIndie.json"),
                new FileInputStream("src/test/resources/SerializedDependent.json"));

        artifactBuilder.withComponentFactory(factory).withArtifactConsumer(ArtifactConsumer.PrintToConsole());
        Artifact actual = artifactBuilder.build();
        System.out.println("Selector Dependent Test");
        actual.output();
    }

    @Test
    public void testJsonArtifactAll() throws FileNotFoundException {

        ArtifactBuilder artifactBuilder = new ArtifactBuilder();

        IArtifactComponentFactory factory = new JsonArtifactComponentFactory(
                new FileInputStream("src/test/resources/SerializedIndie.json"),
                new FileInputStream("src/test/resources/SerializedDependent.json"),
                new FileInputStream("src/test/resources/SerializedExclusive.json"),
                new FileInputStream("src/test/resources/SerializedSelector.json"));

        artifactBuilder.withComponentFactory(factory).withArtifactConsumer(ArtifactConsumer.PrintToConsole());
        Artifact actual = artifactBuilder.build();
        System.out.println("Selector All Test");
        actual.output();

    }

    @Test
    public void testJsonArtifactArray() throws FileNotFoundException {

        ArtifactBuilder artifactBuilder = new ArtifactBuilder();

        IArtifactComponentFactory factory = new JsonArtifactComponentFactory(
                new FileInputStream("src/test/resources/Example.json"));

        artifactBuilder.withComponentFactory(factory).withArtifactConsumer(ArtifactConsumer.PrintToConsole());
        Artifact actual = artifactBuilder.build();
        System.out.println("Array Data Test");
        actual.output();

    }

    @Test
    public void testJsonDanglingDependent() throws FileNotFoundException {

        ArtifactBuilder artifactBuilder = new ArtifactBuilder();

        artifactBuilder
                .withComponentFactory(
                        new JsonArtifactComponentFactory(new FileInputStream("src/test/resources/Empty.json")))
                .withComponentFactory(new JsonArtifactComponentFactory(
                        new FileInputStream("src/test/resources/SerializedDependent.json")))
                .withArtifactConsumer(ArtifactConsumer.PrintToConsole());
        Artifact actual = artifactBuilder.build();
        System.out.println("SerializedDependent Dangling");
        actual.output();
    }

}