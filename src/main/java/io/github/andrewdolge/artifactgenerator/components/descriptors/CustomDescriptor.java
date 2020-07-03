package io.github.andrewdolge.artifactgenerator.components.descriptors;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.github.andrewdolge.artifactgenerator.Description;

/**
 * A descriptor that is configureable with a builder pattern.
 * 
 * 
 */
public class CustomDescriptor implements IArtifactDescriptor {

    private String category;
    private List<String> data;

    private String dependentCategory;
    private Map<String, List<String>> dependentData;
   
    private ISelectionStrategy<String> selector;

    private CustomDescriptor(CustomDescriptorBuilder builder) {
        this.category = builder.getCategory();
        this.data = builder.getData() != null ? List.copyOf(builder.getData()): null;
        this.dependentCategory = builder.getDependentCategory();
        this.dependentData = builder.getDependentData() != null? Map.copyOf(builder.getDependentData()): null;
        this.selector = builder.getSelector();
        
    }
    @Override
    public Description getDescription() {
        if(data != null){
            return new Description(
                this.category,
                this.selector.select(data)
            );
        }else{
            return null;
        } 
    }//getDescription

    @Override
    public Description getDescription(List<Description> descriptions){
               /**
         * there must be:
         *  a not null list of descriptions
         *  exactly one description
         *  a description that is not null
         *  whose category is not null and matches our dependent category
         * and whose parts is not null or empty
         */
        if(descriptions != null &&
           descriptions.size() == 1 && 
           descriptions.get(0) != null &&
           descriptions.get(0).getCategory() != null &&
           descriptions.get(0).getCategory().equals(dependentCategory) &&
           descriptions.get(0).getParts() != null && !descriptions.get(0).getParts().isEmpty()
          ){ 

            List<String> toSelect = new LinkedList<String>();
 
            //for each part, check to see if it is in the hashmap of dependent data
            // add that data to the list to select from
            for(String key: descriptions.get(0).getParts()){
                if(this.dependentData.get(key) != null){
                    toSelect.addAll(this.dependentData.get(key));
                }//if
            }//for


            return new Description(
                this.category,
                selector.select( toSelect)
            );
        }else{
            //default value if null checks fail
            return getDescription();
        }//else
    }//getDescription(dependents)

    @Override
    public List<String> getDependentCategories(){
        if(this.dependentCategory != null){
            return List.of(dependentCategory);
        }else{
            return Collections.emptyList();
        }
    }


    public static class CustomDescriptorBuilder implements DescriptorBuilder {

        private String category;
        private List<String> data;
        
        private String dependentCategory;
        private HashMap<String, List<String>> dependentData;

        private ISelectionStrategy<String> selector;

        public CustomDescriptorBuilder() {
            selector = ISelectionStrategy.oneRandomSelection();
        }

        /**
         * builds and returns the descriptor
         * 
         * @return
         */
        public CustomDescriptor build() {
            return new CustomDescriptor(this);
        }

        public CustomDescriptorBuilder withCategory(String category) {
            this.category = category;
            return this;
        }

        public CustomDescriptorBuilder withIndependentData(String... data) {
            if (this.data == null) {
                this.data = new LinkedList<String>();
            }
            this.data.addAll(Arrays.asList(data));

            return this;
        }

        /**
         * Tells the builder to create the descriptor with dependent data, i.e. data
         * that relies on description parts for a given category of an artifact.
         * 
         * Do note: multiple calls to this method will overwrite each other, as this
         * implementation requires that a dependendent descriptor only has one dependent
         * category.
         * 
         * @return the builder, for method chaining.
         * 
         */
        public CustomDescriptorBuilder withDependentData(String dependentCategory, Map<String, List<String>> data) {

            this.dependentCategory = dependentCategory;
            // will write over existing dependent data
            this.dependentData = new HashMap<String, List<String>>();
            this.dependentData.putAll(data);

            return this;
        }

        public CustomDescriptorBuilder withSelectionStrategy(ISelectionStrategy<String> selector) {
            this.selector = selector;
            return this;
        }

        private String getCategory() {
            return category;
        }

        private List<String> getData() {
            return data;
        }

        private HashMap<String, List<String>> getDependentData() {
            return this.dependentData;
        }

        private ISelectionStrategy<String> getSelector() {
            return selector;
        }

        private String getDependentCategory() {
            return dependentCategory;
        }

        @Override
        public void reset() {
            this.category = null;
            this.data = null;
            this.dependentCategory = null;
            this.selector = ISelectionStrategy.oneRandomSelection();

        }
    }//inner builder class

}//class