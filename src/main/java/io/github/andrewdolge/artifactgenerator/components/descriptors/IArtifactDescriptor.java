/**
 *    Copyright 2020 Andrew Dolge
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package io.github.andrewdolge.artifactgenerator.components.descriptors;

import java.util.Collections;
import java.util.List;

import io.github.andrewdolge.artifactgenerator.Description;

/**
 * 
 * Represents a descriptor of an artifact.
 * 
 * An artifact descriptor should be able to create
 * {@link io.github.andrewdolge.artifactgenerator.Description Description}
 * objects that describe an Artifact.
 * 
 */
public interface IArtifactDescriptor {

    /**
     * Gets a description to describe an Artifact generated with this descriptor.
     * Each call to this method should be thought of as describing a new Artifact.
     * 
     * @return A description that describes that artifact, otherwise null.
     */
    public Description getDescription();

    /**
     * Gets a list of descriptions from this descriptor that is dependent on the
     * current descriptions. This method should expect to receive descriptions with
     * catogeries it specifies with the {@link #getDependentCategories()} method.
     * 
     * By default, this method returns a call to {@link #getDescription()}
     * 
     * @param dependents
     * @return
     */
    public default Description getDescription(List<Description> dependents) {
        return getDescription();
    }

    /**
     * returns a list of categories that this ArtifactDescriptor needs in order to
     * produce a selection.
     * 
     * Some Descriptors need the Descriptions set by other Descriptors in order to
     * produce a selction. A descriptor that needs a Description set is said to be
     * 'dependent' on a category (which is a part of a description).
     * 
     * This method specifies which categories the descriptor needs to function. By
     * default, the Descriptor needs no other descriptions to function.
     * 
     * @return a list of categories that the descriptor is dependent on. An empty
     *         list by default.
     */
    public default List<String> getDependentCategories() {
        return Collections.emptyList();
    }// getDependentCategories

    /**
     * returns a list of categories that this ArtifactDescriptor needs in order to
     * produce a selection.
     * 
     * Some Descriptors need the Descriptions set by other Descriptors in order to
     * produce a selction. A descriptor that needs a Description set is said to be
     * 'dependent' on a category (which is a part of a description).
     * 
     * This method determines if the Descriptor is a dependent descriptor. By
     * default, the Descriptor is dependent if the category has dependencies.
     * 
     * YOU SHOULD RARELY EVER OVERRIDE THIS METHOD.
     * 
     * @return true, if this descriptor is dependent. false otherwise.
     */
    public default boolean isDependent() {
        return !getDependentCategories().isEmpty();
    }// isDependent


}// interface