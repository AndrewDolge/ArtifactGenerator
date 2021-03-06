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
package io.github.andrewdolge.artifactgenerator.components;

import java.util.List;

import io.github.andrewdolge.artifactgenerator.components.descriptors.IArtifactDescriptor;
import io.github.andrewdolge.artifactgenerator.components.filters.IConditonalFilter;

/**
 * Abstract factory interface for making Artifact Components.
 * 
 * Artifact components currently consist of two components: descriptors and
 * filters.
 * 
 * A descriptor is an object that implements
 * {@link io.github.andrewdolge.artifactgenerator.descriptors.IArtifactDescriptor}
 * that generates
 * {@io.github.andrewdolge.artifactgenerator.Description Description} objects.
 * 
 * A filter is an object that implements
 * {@link io.github.andrewdolge.artifactgenerator.descriptors.filters.IConditionalFilters},
 * which can modify an Artifact after it is has processed descriptors, but
 * before it has finished construction.
 * 
 * Factories that implement this interface should provide a list of descriptors
 * and filters. If they do not create any of the given components, then they
 * should return an empty list instead.
 * 
 * A typical implementation of this factory would take in some form of input
 * that can be translated to any number of descriptors and filters.
 * 
 * 
 */
public interface IArtifactComponentFactory {

    /**
     * Creates any number of descriptors.
     * 
     * @return a list of descriptors, or an empty list if there are no descriptors.
     */
    public List<IArtifactDescriptor> createDescriptors();

    /**
     * Creates any number of filters.
     * 
     * @return a list of filters, or an empty list if there are no descriptors.
     */
    public List<IConditonalFilter> createFilters();

}