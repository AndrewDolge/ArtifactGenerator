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

}