package io.github.andrewdolge.artifactgenerator.components.descriptors;

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
    public static <T> ISelectionStrategy<T> oneRandomSelection(){

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
    public static <T> ISelectionStrategy<T> anyMultiplicativeProbabilityRandomSelection(double initialProbability, double multipler){

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

    public static <T> ISelectionStrategy<T> customSelectionStrategy(int min, int max, double probability, double multiplier, boolean withReplacement){

        if(min > max){
            throw new IllegalArgumentException(String.format("ISelectionStrategy.CustomSelectionStrategy: min(%d) is greater than max(%d)", min,max));
        }else if(min < 0){
            throw new IllegalArgumentException(String.format("ISelectionStrategy.CustomSelectionStrategy: min(%d) must be nonnegative", min));
        }else if(max < 0){
            throw new IllegalArgumentException(String.format("ISelectionStrategy.CustomSelectionStrategy: max(%d) must be nonnegative", max));
        }else if(probability < 0){
            throw new IllegalArgumentException(String.format("ISelectionStrategy.CustomSelectionStrategy: probability(%f) must be nonnegative, and should be less than one", probability));
        }

        return list ->{

            //if the maximum amount of items possible is greater than the size of the input list, and we are selecting without replacement
            //we could get into an infinite loop, since the selector might try to pick another item when there are no more items to select, so throw an exception.
            if(max > list.size() && !withReplacement){
                throw new IllegalArgumentException(
                    String.format(
                        "ISelectionStrategy.CustomSelectionStrategy: max(%d) cannot be greater than list size(%d) when using 'Without Replacement.' ",
                        max,
                        list.size()
                    )
                );
            }

                         //create a new list to return
                         List<T> selected = new LinkedList<T>();
                         double currentProbability = probability;
             
                         //should this function select another item?
                         while( 
                            !(selected.size() >= max) &&//have we exceeded our maximum item count? then no
                            !(!withReplacement && selected.size() == list.size()) && //have we exhausted all items to select? then no.
                            (
                                selected.size() < min || //do we need more items to meet the minimum? then yes
                                currentProbability > Math.random() //did the probability succeed? then yes
                            )  
                         ){
                             T toSelect;
             
                            // if with replacement, select a random item from the list.
                             if(withReplacement){
                                toSelect = list.get(new Random().nextInt(list.size()));
                           
                             }else{ // else without replacement
                                //while selecting, do not select an item that has already been selected.

                                do{
                                    //select a random element from the list
                                    toSelect = list.get(new Random().nextInt(list.size()));
                                }while(selected.contains(toSelect));
                             }//else

                             //add the item and increment the probability by the multiplier
                             selected.add(toSelect);

                            // if we don't have enough items to satisfy the minimum, don't multiply the probability.
                            // The first selection that was determined by the random draw should use the initial probability.
                            if(selected.size() > min){
                                currentProbability = currentProbability * multiplier;
                            }
                            
                         }//while continue selecting
             
                         return selected;

        };
    }

    public static <T> ISelectionStrategy<T> all(){
        return list -> list;
    }
    
}//interface