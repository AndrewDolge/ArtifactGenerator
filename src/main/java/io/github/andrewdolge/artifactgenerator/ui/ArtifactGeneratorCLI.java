package io.github.andrewdolge.artifactgenerator.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.andrewdolge.artifactgenerator.Artifact;
import io.github.andrewdolge.artifactgenerator.Artifact.ArtifactBuilder;
import io.github.andrewdolge.artifactgenerator.ArtifactConsumer;
import io.github.andrewdolge.artifactgenerator.components.IArtifactComponentFactory;
import io.github.andrewdolge.artifactgenerator.components.SerializedArtifactComponent;
import io.github.andrewdolge.artifactgenerator.components.descriptors.CustomDescriptor.CustomDescriptorBuilder;
import io.github.andrewdolge.artifactgenerator.components.descriptors.IArtifactDescriptor;
import io.github.andrewdolge.artifactgenerator.components.descriptors.ISelectionStrategy;
import io.github.andrewdolge.artifactgenerator.components.json.JsonArtifactComponentFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "artifactgenerator", mixinStandardHelpOptions = true, version = "1.0", description = "generates artifacts from the shadowy origins...")
public class ArtifactGeneratorCLI implements Callable<Integer> {

    @Option(names = { "-d",
            "--descriptor-directory" },
             description = "The directory of files where descriptors and other modifiers live."
             )
    private File descriptorDirectory = new File("./descriptors/");

    @Option(names = { "-n", "--number" },description = "Specifies the number of artifacts to generate.")
    private int numberOfArtifacts = 1;

    @Option(names = { "-m","--markdown" },description = "Tells the generator to create artifacts and output them as Markdown (.md) files in the given directory")
    private File markdownDirectory;

    @Option(names = {"-c, --custom"},description = "Specify a custom descriptor with a given category and part.\nExample: -c \"Color=Red,White,Blue;Shape=Circle;\")")
     private String customDescriptors;

    private Consumer<Artifact> consumer = ArtifactConsumer.PrintToConsole();

    public static void main(String... args) {
        int exitCode = new CommandLine(new ArtifactGeneratorCLI()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {

        /*------------------------------------ Secure the Input configurations --------------------------------------------*/
        if (!descriptorDirectory.exists()) {
            System.out.println("The descriptor directory doesn't exist!");
            System.out.format("Creating a new descriptor directory at: %s", descriptorDirectory.getAbsolutePath());

            File exampleJsonFile = new File(descriptorDirectory, "Example.json");
            try {

                // attempt to create the descriptor directory
                descriptorDirectory.mkdir();

                // attempt to create an example json file
                try (FileWriter writer = new FileWriter(exampleJsonFile)) {
                    System.out.format("Creating a new Json example file at: %s", exampleJsonFile.getPath());

                    exampleJsonFile.createNewFile();
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();

                    SerializedArtifactComponent[] components = { 
                            SerializedArtifactComponent.independentExample(),
                            SerializedArtifactComponent.dependentExample(),
                            SerializedArtifactComponent.exclusiveExample(),
                            SerializedArtifactComponent.selectorExample()
                        };

                    gson.toJson(components, writer);

                } catch (Exception e) {
                    System.out.format("Couldn't create an example Json file at: %s", exampleJsonFile.getPath());
                }
                // INSERT OTHER FORMATTED FILES HERE!

            } catch (Exception e) {
                System.out.format("Couldn't create an descriptor directory at: %s", descriptorDirectory.getPath());
                System.out.println(e.getMessage());
            }
        }
        // throw an error if the directory path provided doesn't point to a directory.
        if (!descriptorDirectory.isDirectory()) {
            System.out.format("The descriptor directory is not a directory!\n Path given: %s",
                    descriptorDirectory.getAbsolutePath());
            return -1;
        } // if not directory

        // set the markdown output consumer if a markdown directory is provided
        if (markdownDirectory != null) {

            this.consumer = ArtifactConsumer.WriteToMarkdown(markdownDirectory, "Name");

        } // if markdownDirectory is not null

        /*------------------------------------------ Build the Artifact ----------------------------------------------------*/
        ArtifactBuilder artifactBuilder = new ArtifactBuilder();

        for (File f : descriptorDirectory.listFiles()) {
            if (f.getName().endsWith(".json")) {

                try (FileInputStream fin = new FileInputStream(f)) {

                    IArtifactComponentFactory jsonFactory = new JsonArtifactComponentFactory(fin);
                    artifactBuilder.withComponentFactory(jsonFactory);

                } catch (Exception e) {
                    System.out.format("Could not read file: %s", f.getName());
                    System.exit(-1);
                }
            } // if
        } // for
        //add custom descriptors from the command line. This should always be the last thing to be added to the artifact.
        if(customDescriptors != null){
            List<IArtifactDescriptor> cliDescriptors = parseCLIDescriptor(customDescriptors);
            for(IArtifactDescriptor cliDescriptor: cliDescriptors){
                artifactBuilder.withDescriptor(cliDescriptor);
            }//for
        }//if 

/*
    * ------------------------------------ output the Artifact---------------------------------------------------
    */
        artifactBuilder.withArtifactConsumer(consumer);

        for (int i = 0; i < numberOfArtifacts; i++) {
            artifactBuilder.build().output();
        }

        return 0;
    }// call

    private static List<IArtifactDescriptor> parseCLIDescriptor(String cliString) {
        List<IArtifactDescriptor> toReturn = new LinkedList<IArtifactDescriptor>();
        CustomDescriptorBuilder builder = new CustomDescriptorBuilder();
        String[] strDescriptors = cliString.split(";");

        for (String strDescriptor : strDescriptors) {
            builder.reset();

            String category = strDescriptor.split("=")[0];
            String[] parts  = strDescriptor.split("=")[1].split(",");

            builder.withCategory(category);
            builder.withIndependentData(parts);
            builder.withSelectionStrategy(ISelectionStrategy.all());

            toReturn.add(builder.build());

        }//for
        return toReturn;

    }// parseCLIDescriptor
}// class