package io.github.andrewdolge.artifactgenerator.descriptor;

import java.util.List;

import io.github.andrewdolge.artifactgenerator.Description;
/**
 * A class that implements IArtifactDescriptor by:
 *  taking in a string for a category, a list of strings for parts, and an implementation of {@link io.github.andrewdolge.artifactgenerator.Descriptors.Manual}
 * 
 */


public class SelectionStrategyManualDescriptor extends ManualDescriptor {

    private ISelectionStrategy<String> selector;

    public SelectionStrategyManualDescriptor(String category, List<String> parts, ISelectionStrategy<String> selector) {
        super(category, parts);

        this.selector = selector;
    }//constructor

    /**
     * select a description from the list with the given selector
     */
    @Override
    public Description getDescription() {
        return new Description(super.getCategory(), selector.select(super.getParts()));
    }//getDescription

    
    
}