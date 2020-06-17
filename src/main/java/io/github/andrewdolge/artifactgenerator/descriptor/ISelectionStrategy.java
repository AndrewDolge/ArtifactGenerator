package io.github.andrewdolge.artifactgenerator.descriptor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * An ISelector instance should be able to select any number of items from a
 * given list.
 * 
 */
public interface ISelectionStrategy<T> {
    
    /**
     * Selects and returns any number of items from the given list.
     * 
     * @param list the original list to select from
     * @return any number of items from the given list.
     */
    public List<T> select(List<T> list);

    /**
     * A Selector that selects exactly one item from the list at random.
     * @return a SelectionStrategy that selects exactly one random item from the given list.
     */
    public static <T> ISelectionStrategy<T> OneRandomSelection(){

        //return a lambda expression (an anonymous function [a function without name]) 
        //that implements the ISelectionStrategy interface.
        return list -> {
           
            //create a new list to return
            List<T> selected = new LinkedList<T>();

            //if the input list is empty, return an empty list.
            if(list.isEmpty()){return selected;}

            //add to the new list a random element from the input list and return it.
            selected.add( 
                list.get(
                    new Random().nextInt(list.size()
                    )
                )
            );
            return selected;
        };
    }//static OneRandomSelection

    //TODO: Add SelectionStrategy for: Any Number of selections with replacement
    //TODO: Add SelectionStrategy for: Any Number of selections without replacement
    //TODO: Add SelectionStrategy for: Probability based selection (probably needs a new default method, or a separate Class)

}//interface