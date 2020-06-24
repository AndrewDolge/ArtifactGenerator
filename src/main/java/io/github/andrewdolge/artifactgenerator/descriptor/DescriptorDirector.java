package io.github.andrewdolge.artifactgenerator.descriptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class DescriptorDirector {

    private DescriptorBuilder builder;

    public DescriptorDirector(DescriptorBuilder builder) {
        this.builder = builder;
    }

    public DescriptorBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(DescriptorBuilder builder) {
        this.builder = builder;
    }

    public void buildWithJson(InputStream in) {
        builder.reset();
        Gson gson = new Gson();

        // read the json from the input stream
        try (JsonReader reader = gson.newJsonReader(new InputStreamReader(in));) {

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName().toLowerCase();

                switch (name) {
                    case "category":
                        builder.withCategory(reader.nextString());
                        break;
                    case "data":
                        if (reader.peek() == JsonToken.BEGIN_ARRAY) {
                            builder.withIndependentData(getDataFromJson(reader));
                        } else {
                            // TODO: implement dependent data
                            DependentDescriptorFromJson dependent = getDependentDescriptor(reader);

                            builder.withDependentData(dependent.getDependentCategory(), dependent.getData());
                        }
                        break;
                    case "selector":
                        builder.withSelectionStrategy(getSelectionStrategyFromJson(reader));
                        break;
                    default:
                        reader.skipValue();
                }// switch
            } // while
            reader.endObject();
        } catch (Exception e) {
            builder.reset();
            // TODO: reconsider runtime exception?
            throw new RuntimeException("DescriptorDirector.builderWithJson", e);
        }
    }

    /**
     * Gets a selection strategy from Json
     * 
     * @param reader the reader
     * @return a selection strategy from json
     * @throws IOException if the reader throws an error
     */
    private ISelectionStrategy<String> getSelectionStrategyFromJson(JsonReader reader) throws IOException {

        int min = 1;
        int max = 1;
        double probability = 0.0;
        double multiplier = 0.0;
        boolean withReplacement = true;

        reader.beginObject();

        while (reader.hasNext()) {

            String name = reader.nextName().toLowerCase();
            switch (name) {
                case "min":
                case "minimum":
                    min = reader.nextInt();
                    break;
                case "max":
                case "maximum":
                    max = reader.nextInt();
                    break;
                case "probability":
                case "prob":
                case "p":
                    probability = reader.nextDouble();
                    break;
                case "multiplier":
                case "multi":
                    multiplier = reader.nextDouble();
                    break;
                case "replacement":
                    withReplacement = reader.nextBoolean();
                    break;

                default:
                    reader.skipValue();
            }// switch
        } // while
        reader.endObject();

        return ISelectionStrategy.CustomSelectionStrategy(min, max, probability, multiplier, withReplacement);
    }

    /**
     * Reads an array of independent data (strings) from the json reader
     * 
     * @param reader the reader to read from
     * @return a list of strings, representing the data
     * @throws IOException
     */
    private List<String> getDataFromJson(JsonReader reader) throws IOException {
        LinkedList<String> data = new LinkedList<String>();
        reader.beginArray();
        while (reader.hasNext()) {
            data.add(reader.nextString());
        }
        reader.endArray();
        return data;
    }// getIndependendData

    private DependentDescriptorFromJson getDependentDescriptor(JsonReader reader) throws IOException{
        
        String dependentCategory = null;
        HashMap<String, List<String>> data = new HashMap<String, List<String>>();

        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            switch (name) {
                case "DependentCategory":
                case "dependentcategory":
                case "dependent category":
                case "Dependent Category":
                    dependentCategory = reader.nextString();
                    break;
            
                default:
                if(reader.peek() == JsonToken.BEGIN_ARRAY){
                    data.put(name, getDataFromJson(reader));
                }else{
                    reader.skipValue();
                }
                    break;
            }//switch
        }//while
        reader.endObject();

        if(dependentCategory == null){
            throw new IOException("Couldn't find the name of the dependent category! data.dependentcategory is not set.");
        }
        return new DependentDescriptorFromJson(dependentCategory, data);
    }

    /**
     * private inner class that represents dependent descriptor data from json
     */
    private class DependentDescriptorFromJson {

        private String dependentCategory;
        private HashMap<String, List<String>> data;

        public DependentDescriptorFromJson(String dependentCategory, HashMap<String, List<String>> data) {
            this.dependentCategory = dependentCategory;
            this.data = data;
        }

        public String getDependentCategory() {
            return dependentCategory;
        }

        public Map<String, List<String>> getData() {
            return data;
        }



    }

    
}//class