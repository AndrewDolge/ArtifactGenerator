package io.github.andrewdolge.artifactgenerator.components.filters;

import java.util.function.Predicate;

import io.github.andrewdolge.artifactgenerator.Artifact;
import io.github.andrewdolge.artifactgenerator.Description;

/**
 * Represents an IConditionalFilter on Descriptors.
 * 
 * 
 */
public class ConditionalDescriptorFilter implements IConditonalFilter {

    private final Predicate<Artifact> condition;
    private final Predicate<Description> filter;

    public ConditionalDescriptorFilter(Predicate<Artifact> condition, Predicate<Description> filter) {
        this.condition = condition;
        this.filter = filter;

    }// constructor

    /**
     * getter method for the condition.
     */
    @Override
    public Predicate<Artifact> getCondition() {
        return this.condition;
    }// getCondition

    /**
     * getter method for the filter.
     */
    @Override
    public Predicate<Description> getFilter() {
        return this.filter;
    }// getFilter

}// class