package io.github.andrewdolge.artifactgenerator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Represents an Artifact of chaotic origins.
 * 
 * An Artifact is a readonly mapping of descriptors that provide descriptions of the Artifact.
 * 
 */
public class Artifact {

    private Map<String, List<String>> nameToDescriptions;


    private Artifact(ArtifactBuilder builder){

    }//constructor

    public List<String> getDescriptors(){
        throw new RuntimeException("Not implemented.");
    }//getDescriptors

    public List<String> getDescriptions(String descriptor){
        throw new RuntimeException("Not implemented.");
    }//getDescriptions

    /**
     *Inner static builder class for Artifacts.
     * 
     * This Builder can take in any number of IArtifactDescriptors and use them to build any number of Artifact objects.
     * 
     */
    public static class ArtifactBuilder{

        private List<IArtifactDescriptor> descriptors;

        /**
         * Creates a new ArtifactBuilder.
         * Empty Constructor.
         */
        public ArtifactBuilder(){
            descriptors = new LinkedList<IArtifactDescriptor>();
        }//Constructor

        /**
         * adds the descriptor to the list of builders
         * @return this artifact builder.
         */
        public ArtifactBuilder add(IArtifactDescriptor descriptor){
            descriptors.add(descriptor);
            return this;
        }//add

        /**
         * builds and returns the artifact.
         * @return the created artifact.
         */
        public Artifact build(){
            return new Artifact(this);
        }//build

    }//inner static builder class


}//class