package io.github.andrewdolge.artifactgenerator.descriptor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import io.github.andrewdolge.artifactgenerator.Description;

public class DepenedentManualDescriptor implements IArtifactDescriptor {

    private HashMap<String, List<String>> dependentPartsToDescriptionParts;

    private String category;
    //TODO: consider changing to list
    private String dependentCategory;
    private ISelectionStrategy<String> selector;

    @Override
    public Description getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public Description getDescription(List<Description> descriptions){

        /**
         * there must be:
         *  a not null list of descriptions
         *  exactly one description
         *  a description that is not null
         *  whose category is not null and matches our dependent category
         */
        if(descriptions != null &&
           descriptions.size() == 1 && 
           descriptions.get(0) != null &&
           descriptions.get(0).getCategory() != null &&
           descriptions.get(0).getCategory() == dependentCategory
          ){

            return new Description(
                this.category,
                    selector.select(
                        descriptions.get(0).getParts().stream()
                        .filter(str -> dependentPartsToDescriptionParts.containsKey(str))
                        .flatMap(str-> dependentPartsToDescriptionParts.get(str).stream())
                        .collect(Collectors.toList())
                        )
            );
        }else{
            //default value if null checks fail
            return getDescription();
        }
    }

    @Override
    public List<String> getDependentCategories(){
        return List.of(dependentCategory);
    }

    public DepenedentManualDescriptor(HashMap<String, List<String>> dependentPartsToDescriptionParts, String category,String dependentCategory,ISelectionStrategy<String> strategy) {
        this.dependentPartsToDescriptionParts = dependentPartsToDescriptionParts;
        this.category = category;
        this.dependentCategory = dependentCategory;
        this.selector = strategy;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDependentCategory() {
        return dependentCategory;
    }

    public void setDependentCategory(String dependentCategory) {
        this.dependentCategory = dependentCategory;
    }

    public ISelectionStrategy<String> getSelector() {
        return selector;
    }

    public void setSelector(ISelectionStrategy<String> selector) {
        this.selector = selector;
    }
    
}