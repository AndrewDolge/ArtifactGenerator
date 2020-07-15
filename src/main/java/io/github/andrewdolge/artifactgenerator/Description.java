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
package io.github.andrewdolge.artifactgenerator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Represents a description of an Artifact.
 * 
 * Each description has a category that it represents and a list of parts of the description.
 * 
 * For example, a Description of an Artifact's color would have the category of "Color" and a 
 * list of colors, like "Red","Blue".
 * 
 * 
 */
public class Description {

    private final  String category;
    private List<String> parts;

    /**
     * Constructor for an Empty, Default Description. Use this constructor instead of returning a Null Description.
     */
    public Description(String category){
        this(category,Collections.emptyList());
    }

    public Description(String category, List<String> parts) {

        if(category==null){throw new IllegalArgumentException("Description.Constructor: category is null.");}
        if(parts==null){throw new IllegalArgumentException("Description.Constructor: parts is null.");}

        this.category = category;
        this.parts = new LinkedList<String>();


        this.parts.addAll(parts);
    }// constructor

    /**
     * Merges the other description into this description
     * @return
     */
    public void merge(Description other){
        
    }
    /**
     * Gets the name of this description's category.
     * @return a string identifying the category of this description.
     */
    public String getCategory() {
        return category;
    }//getCategory

    /**
     * determines whether this description has no parts
     * @return true, if the description has no parts, false otherwise.
     */
    public boolean isEmpty(){
        return this.parts.isEmpty();
    }//isEmpty

    /**
     * returns an immutable list of strings that describe this Description.
     * 
     * @return a list of strings of the description
     */
    public List<String> getParts() {
        return List.copyOf(this.parts);
    }//getParts
 

    
}