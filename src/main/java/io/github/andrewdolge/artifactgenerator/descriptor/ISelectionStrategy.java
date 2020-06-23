package io.github.andrewdolge.artifactgenerator.descriptor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * An ISelector instance should be able to select any number of items from a
 * given list.
 * 
 * An ISelectionStrategy should decide both which elements to select from the list, as well as how many to select.
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


    /**
     * Returns any number of items from the list, randomly selected (without replacement) from the list.
     * The number of selections is determined by the provided probabilities.
     * 
     * For example, the chance of drawing one item from the list is given by the initial probability.
     * The probability of drawing a second item from the list is initialProbability * multipler.
     * 
     * The probability of drawing X number of items from the list is initialProbability * (multiplier)^(x-1) for x > 0.
     * 
     * @param <T> The type of the list
     * @param initialProbability the probability of drawing the first item. Must be between 0 and 1, inclusive.
     * @param multipler the multiplier to be applied to the draw probability, applied for each additional draw. Must be nonnegative.
     * @return a selection strategy with the desired probability.
     */
    public static <T> ISelectionStrategy<T> AnyMultiplicativeProbabilityRandomSelection(double initialProbability, double multipler){

        return list ->{

             //create a new list to return
             List<T> selected = new LinkedList<T>();

            double currentProbability = initialProbability;

            //should this function select another item?
            while( selected.size() < list.size() && currentProbability > Math.random()   ){
                T toSelect;

                //while selecting, do not select an item that has already been selected.
                do{
                    //select a random element from the list
                    toSelect = list.get(new Random().nextInt(list.size()));
                }while(selected.contains(toSelect));

                //add the item and increment the probability by the multiplier
                selected.add(toSelect);
                currentProbability = currentProbability * multipler;
            }//while continue selecting

            return selected;
        };

    }


    //TODO: Add SelectionStrategy for: Any Number of selections with replacement
    //TODO: Add SelectionStrategy for: Any Number of selections without replacement
    //TODO: Add SelectionStrategy for: Probability based selection (probably needs a new default method, or a separate Class)

}//interface