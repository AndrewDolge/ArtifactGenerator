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
     * Returns a description filter that will include all the given categories (if
     * they are present), and excludes all other categories.
     * 
     * @param categories the accepted categories
     * @return a description filter that accepts only the given categories.
     */
    public static Predicate<Description> acceptOnly(List<String> categories) {
        return (description) -> categories.contains(description.getCategory());
    }// acceptOnly

}// interface