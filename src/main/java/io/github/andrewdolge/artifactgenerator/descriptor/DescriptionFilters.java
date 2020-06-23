package io.github.andrewdolge.artifactgenerator.descriptor;

import java.util.List;
import java.util.function.Predicate;

import io.github.andrewdolge.artifactgenerator.Description;

/**
 * Represents a filter that includes/excludes descriptions from an artifact.
 * 
 * A predicate whose boolean test(Description desc) returns true will keep the
 * description in the artifact, and returning false will exclude the description
 * from the artifact.
 * 
 */
public class DescriptionFilters {

    /**
     * Returns a description filter that will include all the given categories (if they are present), and excludes all other categories.
     * @param categories the accepted categories
     * @return a description filter that accepts only the given categories.
     */
    public static Predicate<Description> acceptOnly(List<String> categories){
        return (description) -> categories.contains(description.getCategory());
    }//acceptOnly
    
}//interface