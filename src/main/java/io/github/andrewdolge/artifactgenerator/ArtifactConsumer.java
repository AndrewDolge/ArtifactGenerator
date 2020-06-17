package io.github.andrewdolge.artifactgenerator;

import java.util.function.Consumer;


/**
 * Static utilty class that contains implementations of the interface Consumer<Artifact>.
 * These methods should take in an artifact, and output them in some way.
 * 
 *{@link java.util.function.Consumer}
 * 
 */
public final class ArtifactConsumer {
    
    /**
     * Artifact consumer implementation that prints the Descriptions of the Artifact to the console.
     * Functionally equivalent to a toString() method.
     * @return
     */
    public static Consumer<Artifact> PrintToConsoleArtifactConsumer() {

        return artifact -> {
            System.out.println();
            for(Description d: artifact.getAllDescriptions()){
                System.out.println("--------");
                System.out.println(d.getCategory());
                for(String part: d.getParts()){
                    
                    System.out.println("  " + part);
                }
            }
        };

    }//ArtifactPrintConsumer
    


}