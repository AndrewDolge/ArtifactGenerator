package io.github.andrewdolge.artifactgenerator.components.descriptors;

import java.util.List;
import java.util.Map;

/**
 * Interface for a generic descriptor builder.
 */
public interface DescriptorBuilder {

    /**
     * Specifies the category that this descriptor will use in its descriptions
     * @param category the category to use
     * @return this, for method chaining
     */
    public DescriptorBuilder withCategory(String category);

    /**
     * specifies a list of dependent data that this descriptor will use in its descriptions.
     * This data is for data that is not dependent on other categories.
     * @param data the data to be used.
     * @return this, for method chaining.
     */
    public DescriptorBuilder withIndependentData(String... data);

    /**
     * Specifies a mapping of description parts to lists of dependend data, to be used in descriptions.
     * This data is dependent on descriptions with the given dependent category.
     * 
     * For example, if a dependent Category "Shape" is used, 
     * then the mapping variable might contain mappings of
     * "Rectangle" -> "Square", "Long and Thin",
     * "Triangle" -> "Equilateral", "Obtuse", "Isosceles"
     * 
     * 
     * @param dependentCategory the category of the description the mapping depends on.
     * @param data a mapping of description parts to data lists
     * @return this, for method chaining
     */
    public DescriptorBuilder withDependentData(String dependentCategory, Map<String, List<String>> data);

    /**
     * Specifies the selection strategy to use.
     * {@see} {@link io.github.andrewdolge.artifactgenerator.ISelectionStrategy}
     * @param selector the selection strategy to use.
     * @return this, for method chaining.
     */
    public DescriptorBuilder withSelectionStrategy(ISelectionStrategy<String> selector);

    /**
     * resets the builder.
     */
    public void reset();

}