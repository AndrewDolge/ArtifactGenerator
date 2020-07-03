package io.github.andrewdolge.artifactgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.function.Consumer;

/**
 * Static utilty class that contains implementations of the interface
 * Consumer<Artifact>. These methods should take in an artifact, and output them
 * in some way.
 * 
 * {@link java.util.function.Consumer}
 * 
 */
public final class ArtifactConsumer {

    /**
     * Artifact consumer implementation that prints the Descriptions of the Artifact
     * to the console. Functionally equivalent to a toString() method.
     * 
     * @return
     */
        public static Consumer<Artifact> PrintToConsole() {

        return artifact -> {
            System.out.println();
            for (Description d : artifact.getAllDescriptions()) {
                System.out.println("--------");
                System.out.println(d.getCategory());
                for (String part : d.getParts()) {

                    System.out.println("  " + part);
                }
            }
        };

    }// ArtifactPrintConsumer

    /**
     * Writes out the artifact to a markdown file.
     * @param directory
     * @param nameCategory
     * @return
     */
    public static Consumer<Artifact> WriteToMarkdown(File directory, String nameCategory){

        if(!directory.exists()){
            System.out.format("Making directory for markdown at: %s", directory.getPath());
            directory.mkdir();
        }
        if(!directory.isDirectory()){
            throw new IllegalArgumentException(String.format("WriteToMarkdown: parameter directory is not a directory! path: %s", directory));
        }

        return artifact ->{
            String artifactName;
        
            if(!artifact.getDescription(nameCategory).isEmpty()){
                artifactName = artifact.getDescription(nameCategory).getParts().get(0);
            }else{
                artifactName = String.valueOf(artifact.hashCode());
            }//else

            try (Writer writer =
             new BufferedWriter(
                 new FileWriter(
                     new File(directory, artifactName+ ".md")
                    )
             )
            ) {
                
                writer.append("# " + artifactName + "\n");

                for(Description d: artifact.getAllDescriptions()){
                   writer.append("---\n");
                   
                   writer.append("## " + d.getCategory()+ "\n");
                  for(String s: d.getParts()){
                      writer.append("    - " + s+ "\n");
                  }//for parts
                }//for description

            } catch (Exception e) {
                //TODO: handle exception
                e.printStackTrace();
            }//catch
        };//lambda
    }//WriteToMarkdown


}