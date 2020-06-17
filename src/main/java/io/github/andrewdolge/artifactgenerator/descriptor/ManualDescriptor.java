package io.github.andrewdolge.artifactgenerator.descriptor;

import java.util.List;

import io.github.andrewdolge.artifactgenerator.Description;

public class ManualDescriptor implements IArtifactDescriptor {

    
private String category;
private List<String> parts;

    public ManualDescriptor(String category, List<String> parts) {
       //TODO: possibly get read of list copies
        this.category = category;
        this.parts = List.copyOf(parts);
    }

    /**
     * returns a Description  with all the parts specified in the constructor
     */
    @Override
    public Description getDescription() {
       return new Description(category, parts);
    }

    public String getCategory(){
        return this.category;
    }

    public List<String> getParts(){
        return this.parts;
    }

    
    
}