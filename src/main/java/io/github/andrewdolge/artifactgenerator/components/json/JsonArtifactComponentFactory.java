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
package io.github.andrewdolge.artifactgenerator.components.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import io.github.andrewdolge.artifactgenerator.components.IArtifactComponentFactory;
import io.github.andrewdolge.artifactgenerator.components.SerializedArtifactComponent;
import io.github.andrewdolge.artifactgenerator.components.descriptors.CustomDescriptor.CustomDescriptorBuilder;
import io.github.andrewdolge.artifactgenerator.components.descriptors.IArtifactDescriptor;
import io.github.andrewdolge.artifactgenerator.components.filters.ConditionalDescriptorFilter;
import io.github.andrewdolge.artifactgenerator.components.filters.DescriptionFilters;
import io.github.andrewdolge.artifactgenerator.components.filters.FilterConditions;
import io.github.andrewdolge.artifactgenerator.components.filters.IConditonalFilter;

public class JsonArtifactComponentFactory implements IArtifactComponentFactory {

    private List<IArtifactDescriptor> descriptors;

    private List<IConditonalFilter> filters;

    public JsonArtifactComponentFactory(InputStream... ins) {

        Gson gson = new GsonBuilder().create();
        this.descriptors = new LinkedList<IArtifactDescriptor>();
        this.filters = new LinkedList<IConditonalFilter>();

        for (InputStream in : ins) {
            try (JsonReader reader = new JsonReader(new InputStreamReader(in))) {

                if (reader.peek() == JsonToken.BEGIN_ARRAY) {

                    Type componentList = new TypeToken<LinkedList<SerializedArtifactComponent>>() {
                    }.getType();

                    List<SerializedArtifactComponent> components = gson.fromJson(reader, componentList);
                    processComponent(components.toArray(new SerializedArtifactComponent[components.size()]));

                } else if (reader.peek() == JsonToken.BEGIN_OBJECT) {

                    SerializedArtifactComponent component = gson.fromJson(reader, SerializedArtifactComponent.class);
                    processComponent(component);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } // catch

        } // for
    }// constructor

    /**
     * adds descriptors and filters from the given Json Component
     * 
     * @param components
     */
    private void processComponent(SerializedArtifactComponent... components) {
        CustomDescriptorBuilder builder = new CustomDescriptorBuilder();

        for (SerializedArtifactComponent component : components) {
            builder.reset();

            if (component != null) {
                // if the component has a given category and independent data, add independent
                // data to the descriptor
                if (component.hasIndependentData()) {
                    builder.withIndependentData(component.getData().toArray(new String[component.getData().size()]))
                            .withCategory(component.getCategory());
                }
                // if the component has dependent data, add dependent data to the descriptor
                if (component.hasDependentData()) {
                    builder.withDependentData(component.getDependentCategory(), component.getDependentData())
                            .withCategory(component.getCategory());
                }
                if (component.hasSelectionStrategy()) {
                    builder.withSelectionStrategy(component.getStrategyFromSelector());
                }

                // create the descriptor and add it to the list of descriptors.
                this.descriptors.add(builder.build());
                // if the component is exclusive, add a filter
                if (component.isExclusive()) {
                    filters.add(
                            new ConditionalDescriptorFilter(FilterConditions.isCategoryPresent(component.getCategory()),
                                    DescriptionFilters.acceptOnly(component.getExclusive())));
                } // isExclusive
            } // if
        } // for
    }// processComponent

    @Override
    public List<IArtifactDescriptor> createDescriptors() {
        return this.descriptors;
    }

    @Override
    public List<IConditonalFilter> createFilters() {
        return this.filters;
    }

}// class