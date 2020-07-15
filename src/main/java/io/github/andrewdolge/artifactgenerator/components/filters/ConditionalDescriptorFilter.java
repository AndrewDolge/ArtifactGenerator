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