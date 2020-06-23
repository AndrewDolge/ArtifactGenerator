package io.github.andrewdolge.artifactgenerator.descriptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

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

        try (JsonReader reader = gson.newJsonReader(new InputStreamReader(in));) {

            reader.beginObject();

            while (reader.hasNext()) {
                String name = reader.nextName();

                switch (name) {
                    case "Category":
                        builder.withCategory(reader.nextString());
                        break;
                    case "Data":
                        if (reader.peek() == JsonToken.BEGIN_ARRAY) {
                            builder.withIndependentData(getIndependentDataFromJson(reader));
                        } else {
                            // TODO: implement dependent data
                        }
                        break;
                    default:
                        reader.skipValue();
                }// switch
            } // while
            reader.endObject();
        } catch (Exception e) {
            builder.reset();
            throw new RuntimeException("DescriptorDirector.builderWithJson", e);
        }

    }
    /**
     * Reads an array of independent data (strings) from the json reader
     * @param reader the reader to read from
     * @return a list of strings, representing the data
     * @throws IOException
     */
    private List<String> getIndependentDataFromJson(JsonReader reader) throws IOException {
        LinkedList<String> data = new LinkedList<String>();
        reader.beginArray();
        while(reader.hasNext()){
            data.add(reader.nextString());
        }
       reader.endArray();
       return data;
    }//getIndependendData

}//class