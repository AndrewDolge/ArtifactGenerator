package io.github.andrewdolge.artifactgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import io.github.andrewdolge.artifactgenerator.descriptor.IArtifactDescriptor;


/**
 * Represents an Artifact of chaotic origins.
 * 
 * An Artifact is a collection of categorized descriptions that describe an Artifact.
 * 
 */
public class Artifact {

    private Map<String, Description> categoryToDescription;
    private Consumer<Artifact> consumer;

    /**
     * creates a new Artifact from the given builder object
     * @param builder
     */
    private Artifact(ArtifactBuilder builder){

        this.consumer = builder.getConsumer();
        this.categoryToDescription = new HashMap<String,Description>();

        for(IArtifactDescriptor descriptor : builder.getIndependentDescriptors()){

                //Ask each descriptor for a Description object, and add it to the mapping
                Description toAdd = descriptor.getDescription();
                addDescription(toAdd);

        }//for every independentDescriptor
           
        //set remaining dependent descriptors to 0 to allow the while loop to start
        int remainingDependentDescriptors = 0;
        //copy the dependent descriptors
        List<IArtifactDescriptor> dependentDescriptors = new ArrayList<IArtifactDescriptor>();
        dependentDescriptors.addAll(builder.getDependentDescriptors());

        // while there are some dependent descriptors are left,
        // and the size HAS changed (meaning that the category could have been added by another dependent descriptor that has since been removed)
        
        boolean whileCondition =  !dependentDescriptors.isEmpty();
        boolean whileCondition2 = remainingDependentDescriptors != dependentDescriptors.size();
        
        while(!dependentDescriptors.isEmpty() && remainingDependentDescriptors != dependentDescriptors.size()){

            // update the remaining dependent descriptors
            // this should reflect the number of remaining descriptors
            remainingDependentDescriptors = dependentDescriptors.size();

            Iterator<IArtifactDescriptor> iter = dependentDescriptors.iterator();
            while(iter.hasNext()){
                IArtifactDescriptor currentDescriptor = iter.next();

                //if all dependencies exist in the hashmap
                if(currentDescriptor.getDependentCategories().stream()
                    .allMatch( desc -> 
                    categoryToDescription.containsKey(desc)
                    )   
                ){

                    //get all dependent descriptions that the Descriptor needs from the hashmap
                    //using the categories (from the descriptor) as the key
                    List<Description> dependentDescriptions =
                        currentDescriptor.getDependentCategories().stream()
                        .map(category -> categoryToDescription.get(category))
                        .collect(Collectors.toList());

                    //add the new description to the Artifact's HashMap
                    this.addDescription(currentDescriptor.getDescription(dependentDescriptions));

                    //remove the dependent descriptor from the list, it no longer needs to be added
                    iter.remove();
                    //update the remaining number of dependent descriptors 
                    remainingDependentDescriptors = dependentDescriptors.size();

                }//if
            }//while iter has next
        }//while


        //TODO: when the dependent IArtifactDescriptors feature, this method should ask all the indepenedent descriptors first, and then provide the dependent descriptors with the descriptions they need.

        
    }//constructor


    private void addDescription(Description toAdd){


        //If the category already exists, append the descriptions to the category
        if(toAdd!= null){
            //TODO: return this to a function call instead of a variable
            String targetCategory = toAdd.getCategory();

            if(this.categoryToDescription.get(targetCategory) != null){

                //TODO: reevaluate for efficency later. Lots of List copying.
                //concatenate the existing description parts with the ones to add
                List<String> concatenated = new LinkedList<String>(toAdd.getParts());
                concatenated.addAll(this.categoryToDescription.get(targetCategory).getParts()  );

                categoryToDescription.put(toAdd.getCategory(), new Description(targetCategory, concatenated));

            }else{
                //add the new description
                categoryToDescription.put(targetCategory,toAdd );
            }//else
        }//if toAdd is not null

    }

    /**
     * returns an umodifiable list of all categories that can be used to describe this Artifact.
     * @return a list of strings of the categories.
     */
    public List<String> getCategories(){

        return List.copyOf(categoryToDescription.keySet());
        
    }//getDescriptors

    /**
     * Returns a Description for the given category of this Artifact.
     * 
     * @param category the category of the description. This should be taken from the {@link #getCategories()} method. 
     * @return a Description that describe the object for the given category, or a description with an empty list if there is none.
     */
    public Description getDescription(String category){

        //NOTE: relies on Description constructor with default empty list
        return categoryToDescription.getOrDefault(category, new Description(category));

        
    }//getDescriptions

    /**
     * returns an immutable list of descriptions of the artifact.
     * @return a list of descriptions of the artifact
     */
    public List<Description> getAllDescriptions(){
        return List.copyOf(categoryToDescription.values());
    }

    /**
     * Calls the given Artifact consumer, passing this as an argument to accept();
     * Useful for outputting an artifact with various methods.
     * 
     * 
     */
    public void output(){
        if(consumer!= null){
            consumer.accept(this);
        }
    }//output

    /**
     * sets the consumer to be used for output.
     * @param consumer the consumer of the artifact
     */
    public void setConsumer(Consumer<Artifact> consumer){
        this.consumer = consumer;
    }//setConsumer

    /**
     *Inner static builder class for Artifacts.
     * 
     * This Builder can take in any number of IArtifactDescriptors and use them to build any number of Artifact objects.
     * 
     */
    public static class ArtifactBuilder{

        private List<IArtifactDescriptor> independentDescriptors;
        private List<IArtifactDescriptor> dependentDescriptors;
        private Consumer<Artifact> consumer;

        /**
         * Creates a new ArtifactBuilder.
         * Empty Constructor.
         */
        public ArtifactBuilder(){
            independentDescriptors = new LinkedList<IArtifactDescriptor>();
            dependentDescriptors = new LinkedList<IArtifactDescriptor>();
        }// Constructor

        /**
         * adds the descriptor to the list of builders
         * 
         * @return this artifact builder.
         */
        public ArtifactBuilder add(IArtifactDescriptor descriptor) {

            if(descriptor.isDependent()){
                dependentDescriptors.add(descriptor);
            }else{
                independentDescriptors.add(descriptor);
            }

           
            return this;
        }// add

        /**
         * builds and returns the artifact.
         * 
         * @return the created artifact.
         */
        public Artifact build() {
            return new Artifact(this);
        }// build

        /**
         * adds the specified Artifact Consumer to be used with
         * {@link io.github.andrewdolge.artifactgenerator.Artifact#output()}
         * 
         * @param consumer the Artifact Consumer
         * @return this, for method chaining
         */
        public ArtifactBuilder withArtifactConsumer(Consumer<Artifact> consumer) {
            this.consumer = consumer;
            return this;
        }

        /**
         * private getter method for the consumer
         * 
         * @return
         */
        private Consumer<Artifact> getConsumer() {
            return this.consumer;
        }

        /**
         * internal getter method for the list of Independent ArtifactDescriptors
         * 
         * @return
         */
        private List<IArtifactDescriptor> getIndependentDescriptors() {
            return this.independentDescriptors;
        }

        /**
         * internal getter method for the list of dependent ArtifactDescriptors
         * 
         * @return
         */
        private List<IArtifactDescriptor> getDependentDescriptors() {
            return this.dependentDescriptors;
        }



    }//inner static builder class


}//class