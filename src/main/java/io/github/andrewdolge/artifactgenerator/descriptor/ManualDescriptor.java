package io.github.andrewdolge.artifactgenerator.descriptor;

import java.util.List;

import io.github.andrewdolge.artifactgenerator.Description;

public class ManualDescriptor implements IArtifactDescriptor {

    
private String category;
private List<String> parts;

    /**
     * returns a Description  with all the parts specified in the constructor
     */
    @Override
    public Description getDescription() {
       return new Description(category, parts);
    }

    public ManualDescriptor(String category, List<String> parts) {
        this.category = category;
        this.parts = List.copyOf(parts);
    }

    
    
}