package io.github.andrewdolge.artifactgenerator.descriptor;

import java.util.List;

import io.github.andrewdolge.artifactgenerator.Description;
/**
 * A class that implements IArtifactDescriptor by:
 *  taking in a string for a category, a list of strings for parts, and an implementation of {@link io.github.andrewdolge.artifactgenerator.Descriptors.Manual}
 * 
 */

//TODO: make this inherit from ManualDescriptor
public class ManualSelectionStrategyDescriptor implements IArtifactDescriptor {

    private String category;
    private List<String> parts;
    private ISelectionStrategy<String> selector;

    public ManualSelectionStrategyDescriptor(String category, List<String> parts, ISelectionStrategy<String> selector) {
        this.category = category;
        this.parts = List.copyOf(parts);
        this.selector = selector;
    }//constructor

    /**
     * select a description from the list with the given selector
     */
    @Override
    public Description getDescription() {
        return new Description(category, selector.select(parts));
    }//getDescription

    
    
}