package io.github.andrewdolge.artifactgenerator.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.andrewdolge.artifactgenerator.components.descriptors.ISelectionStrategy;

public class SerializedArtifactComponent {

    private String category;
    private List<String> data;
    
    private String dependentCategory;
    private Map<String, List<String>> dependentData;

    private List<String> exclusive;

    private SerializedCustomSelector selector;

    public SerializedArtifactComponent() {
    }

    public static SerializedArtifactComponent independentExample(){
        SerializedArtifactComponent component = new SerializedArtifactComponent();

        component.setCategory("Category");
        component.setData(List.of("Result 1", "Result 2"));

        return component;
    }

    public static SerializedArtifactComponent dependentExample(){
        SerializedArtifactComponent component = new SerializedArtifactComponent();
        Map<String,List<String>> map = new HashMap<String, List<String>>();
            map.put("Result 1", List.of("Result A","Result B"));
            map.put("Result 2", List.of("Result C","Result D"));

        component.setCategory("Dependent Category");
        component.setDependentCategory("Category");
        component.setDependentData(map);

        return component;
    }

    public static SerializedArtifactComponent exclusiveExample(){
        SerializedArtifactComponent component = new SerializedArtifactComponent();

        component.setCategory("Exclusive Category");

        component.setData(List.of("Result Q", "Result X"));
     
        component.setExclusive(List.of("Dependent Category", "Exclusive Category", "Selector Category"));

        return component;
    }

    public static SerializedArtifactComponent selectorExample(){

        SerializedArtifactComponent component = new SerializedArtifactComponent();
        SerializedCustomSelector selector = new SerializedCustomSelector();

        component.setCategory("Selector Category");
        component.setData(List.of("Result QRZ", "Result ABC", "Result DEF", "Result GHI"));

        selector.setMax(4);
        selector.setMultiplier(0.5);

        component.setSelector(selector);

        return component;

    }

    public boolean isExclusive(){
        return exclusive != null && !exclusive.isEmpty();
    }

    public boolean hasIndependentData(){
        return getCategory() != null && getData() != null && !getData().isEmpty();
    }

    public boolean hasDependentData() {
        return getCategory()          != null && 
               getDependentCategory() != null && 
               getDependentData()     != null && 
               !getDependentData().isEmpty();
	}

    public boolean hasSelectionStrategy(){
        return this.selector !=null;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getDependentCategory() {
        return dependentCategory;
    }

    public void setDependentCategory(String dependentCategory) {
        this.dependentCategory = dependentCategory;
    }

    public Map<String, List<String>> getDependentData() {
        return dependentData;
    }

    public void setDependentData(Map<String, List<String>> dependentData) {
        this.dependentData = dependentData;
    }

    public List<String> getExclusive() {
        return exclusive;
    }

    public void setExclusive(List<String> exclusive) {
        this.exclusive = exclusive;
    }

    @Override
    public String toString() {
        return "SerializedJsonComponent [category=" + category + ", data=" + data + ", dependentCategory="
                + dependentCategory + ", dependentData=" + dependentData + ", exclusive=" + exclusive + "]";
    }

    public SerializedCustomSelector getSelector() {
        return selector;
    }

    public void setSelector(SerializedCustomSelector selector) {
        this.selector = selector;
    }

    public ISelectionStrategy<String> getStrategyFromSelector(){
        if(selector != null){
            return selector.getSelectionStrategy();
        }else{
            return null;
        }
    }




}