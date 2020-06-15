package io.github.andrewdolge.artifactgenerator;

/**
 * An interface for outputting artifacts into various formats.
 * 
 * An IArtifactOutputFormatter is expected to take in an Artifact and output it in some way.
 * 
 */
public interface IArtifactOutputFormatter {

    /**
     * Outputs the given artifact into some format.
     * @param artifact the artifact to output.
     */
    public void output(Artifact artifact);

}//interface
