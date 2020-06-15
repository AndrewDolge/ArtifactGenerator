package io.github.andrewdolge.artifactgenerator;

import java.util.List;

/**
 * 
 * Represents a descriptor of an artifact.
 * 
 * An artifact descriptor should be able to identify itself by name,
 * and generate any number of descriptions that it can assign to an Artifact.
 * 
 * For example, a component might return the name "Shape", and have an internal list of data
 * such as "sphere" or "cube".  If the "Shape" component wishes to return exactly one type of shape,
 * randomly decided, the component should decide when "getDescriptions()" is called.
 * 
 * 
 */
public interface IArtifactDescriptor {
    
    /**
     * Gets the name of the Component. This should always return the exact same name with repeated calls.
     * @return the name of the component
     */
    public String getName();

    /**
     * Gets a list of descriptors to describe an Artifact generated with this component. 
     * Each call to this method should be thought of as describing a new Artifact.
     * 
     * @return a list of descriptions that describe a new Artifact.
     */
    public List<String> getDescriptions();

}//interface