package io.github.andrewdolge.artifactgenerator.components;

import io.github.andrewdolge.artifactgenerator.components.descriptors.ISelectionStrategy;

public class SerializedCustomSelector {

    private int min;
    private int max;
    private double probability;
    private double multiplier;
    private boolean withReplacement;

    public SerializedCustomSelector() {
        // set defaults to emulate oneRandomDraw.
        min = 1;
        max = 1;
        probability = 1.0;
        multiplier = 1.0;
        withReplacement = true;
    }

    public ISelectionStrategy<String> getSelectionStrategy() {

        return ISelectionStrategy.customSelectionStrategy(min, max, probability, multiplier, withReplacement);
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public boolean isWithReplacement() {
        return withReplacement;
    }

    public void setWithReplacement(boolean withReplacement) {
        this.withReplacement = withReplacement;
    }

}