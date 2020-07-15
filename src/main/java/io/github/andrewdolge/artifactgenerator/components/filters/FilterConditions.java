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
package io.github.andrewdolge.artifactgenerator.components.filters;

import java.util.function.Predicate;

import io.github.andrewdolge.artifactgenerator.Artifact;

/**
 * A filter condition returns a function that checks the state of an artifact.
 * 
 * This should always be paired with a list of
 * {@link io.github.andrewdolge.artifactgenerator.descriptor.DescriptionFilter},
 * so the artifact builder can decide if a filter should be applied.
 * 
 */
public class FilterConditions {

    /**
     * returns a filter condition that always returns true. This condition will
     * always apply a filter paired up with it.
     * 
     * @return a filter condition that always returns true.
     */
    public static Predicate<Artifact> always() {
        return (artifact) -> true;
    }// always

    /**
     * returns a filter condition that will return true, if the given category is
     * present in the artifact.
     * 
     * @param category the category to check for
     * @return a Predicate<Artifact>
     */
    public static Predicate<Artifact> isCategoryPresent(String category) {
        return (artifact) -> artifact.getCategories().contains(category);
    }// isCategoryPresent

}