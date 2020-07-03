package io.github.andrewdolge.artifactgenerator.components.filters;

import java.util.function.Predicate;

import io.github.andrewdolge.artifactgenerator.Artifact;
import io.github.andrewdolge.artifactgenerator.Description;

public interface IConditonalFilter {

    public default Predicate<Artifact> getCondition(){
        return FilterConditions.always();
    }

    public Predicate<Description> getFilter();
    
}