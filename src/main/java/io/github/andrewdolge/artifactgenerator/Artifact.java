package io.github.andrewdolge.artifactgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import io.github.andrewdolge.artifactgenerator.components.IArtifactComponentFactory;
import io.github.andrewdolge.artifactgenerator.components.descriptors.IArtifactDescriptor;
import io.github.andrewdolge.artifactgenerator.components.filters.IConditonalFilter;


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

        //after the descriptions have been set, apply the filters.
        //builder.getConditionToFilter().forEach((condition, filter) -> applyConditionalFilter(condition, filter));

        
        for (Predicate<Artifact> condition : builder.getConditionToFilter().keySet()){
            this.applyConditionalFilter(condition, builder.getConditionToFilter().get(condition));
        }

    }//constructor
    
    
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((categoryToDescription == null) ? 0 : categoryToDescription.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Artifact other = (Artifact) obj;
        if (categoryToDescription == null) {
            if (other.categoryToDescription != null)
                return false;
        } else if (!categoryToDescription.equals(other.categoryToDescription))
            return false;
        return true;
    }

    /**
     * overloaded helper method that adds the description, with appending descriptions as a default.
     * see {@link #addDescription(Description, boolean)}.
     * @param toAdd the description to add
     */
    private void addDescription(Description toAdd){
        addDescription(toAdd, true);
    }
    /**
     * private helper method that adds the description to the artifact.
     * @param toAdd the description to add
     * @param append whether or not to append descriptions together if they share the same category.
     */
    private void addDescription(Description toAdd, boolean append){

       //if toAdd and it's fields are not null, and it has some description parts, add it to the artifact
        if(toAdd!= null && toAdd.getParts()!= null && toAdd.getCategory() != null && !toAdd.getParts().isEmpty()){
           
            
            String targetCategory = toAdd.getCategory();

            //if we are appending descriptions, and the category has already been set
            if(append && this.categoryToDescription.get(targetCategory) != null){

                //TODO: reevaluate for efficency later. Lots of List copying.
                //concatenate the existing description parts with the ones to add
                List<String> concatenated = new LinkedList<String>(toAdd.getParts());
                concatenated.addAll(this.categoryToDescription.get(targetCategory).getParts()  );

                //add the combined descriptions together
                categoryToDescription.put(toAdd.getCategory(), new Description(targetCategory, concatenated));

            }else{
                //add the new description
                categoryToDescription.put(targetCategory,toAdd );
            }//else
        }//if toAdd is not null

    }


    /**
     * Applies the given filter to the artifact.
     * @param filter the filter to be applied to each description.
     */
    private void applyFilter(Predicate<Description> filter){
 
        //we use an iterator because that allows us to remove keys and entries from the map safely.
        //iterate over each key in the hashmap
        Iterator<String> iter = this.categoryToDescription.keySet().iterator();

        while(iter.hasNext()){
            //if the description does not pass the test, remove it.
            String key = iter.next();
            Description desc = categoryToDescription.get(key);
            if(desc != null && !filter.test(desc)){
                iter.remove();
            }//if
        }//while
        /* old implementation. Only removed the values from the list
        this.categoryToDescription.replaceAll( 
            (category, description) ->{
                
                if(filter.test(description)){
                    return description;
                }else{
                    return null;
                }
            }
          );
          */

    }//applyFilter
    
    private void applyConditionalFilter(Predicate<Artifact> condition, Predicate<Description> filter){
        if(condition.test(this)){
            applyFilter(filter);
        }
    }
    


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
        private HashMap<Predicate<Artifact>, Predicate<Description>> conditionToFilter;

        /**
         * Creates a new ArtifactBuilder.
         * Empty Constructor.
         */
        public ArtifactBuilder(){
            independentDescriptors = new LinkedList<IArtifactDescriptor>();
            dependentDescriptors = new LinkedList<IArtifactDescriptor>();

            //multimap of conditions to filters
            conditionToFilter = new HashMap<Predicate<Artifact>, Predicate<Description>>();
        }// Constructor

        /**
         * adds the descriptor to the list of builders
         * 
         * @return this artifact builder.
         */
        public ArtifactBuilder withDescriptor(IArtifactDescriptor descriptor) {

            if(descriptor == null){
                throw new IllegalArgumentException("ArtifactBuilder.withDescriptor: descriptor is null");
            }

            if(descriptor.isDependent()){
                dependentDescriptors.add(descriptor);
            }else{
                independentDescriptors.add(descriptor);
            }

           
            return this;
        }// withDescriptor


        /**
         * Adds filters and descriptors from the given IArtifactComponentFactory.
         * 
         * @param factory
         * @return
         * @throws IllegalArgumentException if the factory is null, or the descriptors in the factory are null. 
         */
        public ArtifactBuilder withComponentFactory(IArtifactComponentFactory factory){
            if(factory == null){
                throw new IllegalArgumentException("ArtifactBuilder.withComponentFactory: factory is null");
            }

            List<IArtifactDescriptor> descriptors = factory.createDescriptors();
            List<IConditonalFilter> filters = factory.createFilters();

            if(descriptors != null){
                for (IArtifactDescriptor descriptor : descriptors) {
                    this.withDescriptor(descriptor);
                }
            }else{
                throw new IllegalArgumentException("ArtifactBuilder.withComponentFactory: a descriptor from createDescriptors() is null!");
            }

            if(filters != null){
                for (IConditonalFilter filter : filters) {
                    this.withFilter(filter.getCondition(), filter.getFilter());
                }
            }else{
                throw new IllegalArgumentException("ArtifactBuilder.withComponentFactory: a filter from createFilters() is null!");
            }
            
            return this;
        }

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
            if(consumer == null){
                throw new IllegalArgumentException("ArtifactBuilder.withArtifactConsumer: consumer is null");
            }
            this.consumer = consumer;
            return this;
        }//withArtifactConsumer

        /**
         * Adds a filter to the builder.
         * @see {@link io.github.andrewdolge.artifactgenerator.descriptor.Predicate<Description>} and
         * @see {@link io.github.andrewdolge.artifactgenerator.descriptor.Predicate<Artifact>}
         * 
         * @param condition the condition that determines if the filter should apply
         * @param filter the filter to apply to the artifact's list of descriptions.
         * @return this, for method chaining.
         */
        public ArtifactBuilder withFilter(Predicate<Artifact> condition, Predicate<Description> filter){

            if(condition == null){
                throw new IllegalArgumentException("ArtifactBuilder.withFilter: condition is null");
            }

            if(filter == null){
                throw new IllegalArgumentException("ArtifactBuilder.withFilter: filter is null");
            }
            //if the condition already has a filter, add the filter to the condition (by joining the predicates with 'and')
           if(this.conditionToFilter.get(condition) != null){

                this.conditionToFilter.put(
                    condition,
                    conditionToFilter.get(condition).and(filter));

            }else{
                this.conditionToFilter.put(condition, filter );
            }

            return this;
        }//withFilter


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
         * @return the list of dependent descriptors
         */
        private List<IArtifactDescriptor> getDependentDescriptors() {
            return this.dependentDescriptors;
        }
        /**
         * internal getter method for the conditional filters
         * @return the mapping of conditions to filters
         */
        private HashMap<Predicate<Artifact>, Predicate<Description>> getConditionToFilter() {
            return conditionToFilter;
        }


    }//inner static builder class




}//class