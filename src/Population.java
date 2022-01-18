import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class Population {
    private Individual[] everyone;
    private double totalFitness = -1;

    public Population(int size) {
        // create an empty population
        everyone = new Individual[size];
    }

    public Population(int size, boolean randomise) {
        // create a pop of random individuals
        everyone = new Individual[size];
        for (int i=0; i<size; i++) {
            Individual member = new Individual(randomise);
            everyone[i] = member;
        }
    }

    public Individual[] getEveryone() {
        return everyone;
    }

    public Individual getIndividual(int offset) {
        return everyone[offset];
    }

    public void setIndividual(int offset, Individual member) {
        everyone[offset] = member;
    }

    public double getPopulationFitness() {
        return (double)totalFitness / size();
    }

    public void setTotalFitness(double value) {
        totalFitness = value;
    }

    public double getTotalFitness() {
        return totalFitness;
    }

    public int size() {
        return everyone.length;
    }

    public void sortByFitness() {
        // bubble sort with fittest individuals first
        boolean swapped = true;
        int sorted = 0;
        // until the array is sorted
        while (swapped) {
            swapped = false;
            // iterate over the array
            for (int i=0; i<everyone.length-sorted-1; i++) {
                // swap out-of-order elements
                if (everyone[i].getFitness() < everyone[i+1].getFitness()) {
                    Individual temp = everyone[i];
                    everyone[i] = everyone[i + 1];
                    everyone[i + 1] = temp;
                    swapped = true;
                }
            }
            sorted++;
        }
    }

    public Individual getFittest(int n) {
        sortByFitness();
        return getIndividual(n);
    }

}


