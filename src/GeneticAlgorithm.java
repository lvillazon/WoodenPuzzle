import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    public int elitismCount;
    private int tournamentSize;

    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount, int tournamentSize) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
    }

    public Population initPopulation(boolean randomise) {
        return new Population(populationSize, randomise);
    }

    public int calculateFitness(Individual member) {
        // total number of overlapping cubes in the puzzle
        Puzzle p = new Puzzle(member);
        int fitness = 40-p.collisionCount(); // a collision count of 40 represents total overlap ie worst case
        member.setFitness(fitness);
        return fitness;
    }

    public void evaluatePopulation(Population p) {
        // average fitness of all individuals
        double totalFitness = 0;
        for (Individual i: p.getEveryone()) {
            totalFitness = totalFitness + calculateFitness(i);
        }
        p.setTotalFitness(totalFitness);
    }

    public boolean isTerminationConditionMet(Individual fittest) {
        // return true once we find an optimal solution
        return (fittest.fitness == 40);
    }

    // Roulette selection
    public Individual selectParentRoulette(Population p) {
        // spin the roulette wheel to give a value for 0 to total fitness
        double rouletteWheelPosition = Math.random() * p.getTotalFitness();
        // choose a parent with a chance proportional to the fitness
        double spin = 0.0;
        for (Individual member: p.getEveryone()) {
            spin = spin + member.getFitness();
            if (spin >= rouletteWheelPosition) {
                return member;
            }
        }
        // default is to return the last in the pop
        return p.getIndividual(p.size() - 1);
    }

    // Tournament selection
    public Individual selectParentTournament(Population p) {
        // create the tournament
        Population tournament = new Population(tournamentSize);
        // make a temp copy of the population in an ArrayList
        ArrayList<Individual> tempPop = new ArrayList<>(Arrays.asList(p.getEveryone()));
        // move elements randomly over to the tournament
        Random r = new Random();
        for (int i=0; i<tournamentSize; i++) {
            Individual randomPick = tempPop.remove(r.nextInt(tempPop.size())); // nextInt is exclusive on upper bound
            tournament.setIndividual(i, randomPick);
        }
        // now return the winner
        return tournament.getFittest(0);
    }

    private boolean isElite(Population p, int i) {
        return isNonUniqueElite(p, i);
    }

    private boolean isNonUniqueElite(Population p, int i) {
        if (i<= elitismCount) {
            return true;
        }
        return false;
    }

    private boolean isUniqueElite(Population p, int i) {
        /* calculates elitism in terms of the n fittest *unique* individuals
           rather than just the n fittest individuals, which may all be copies of each other
           This may help to prevent stagnation towards the end of the evolution process
         Assuming a pop sorted by fitness:
            start at the top and work down to i
                if individual is different from the predescessor:
                    increment unique count
                    if count > elitism count:
                        return false

            if i != the previous:
                return true
            else:
                return false
        */
        if (i == 0) {
            return true;  // index 0 is always a unique elite, by definition
        }
        Individual m = p.getIndividual(i);
        int numberOfElites = 1;
        // count the unique elites prior to i
        for (int popIndex=1; popIndex<i; popIndex++) {
            if (!p.getIndividual(popIndex).equals(m)) {
                numberOfElites++;
                if (numberOfElites > elitismCount) {
                    return false; // no need to check further
                }
            }
        }
        // is m actually a unique elite?
        if (!p.getIndividual(i-1).equals(m)) {
            return true;
        }
        return false;
    }


    public Population crossover(Population p) {
        // return a new generation using Order Crossover for the block chromosome
        // TODO: and uniform crossover for the rotation chromosome
        Population nextGeneration = new Population(p.size()); // start with an empty pop
        p.sortByFitness();
        // loop over the existing pop
        for (int popIndex=0; popIndex<p.size(); popIndex++) {
            Individual parent1 = p.getIndividual(popIndex);
            // will we crossover?
          if (Math.random() < crossoverRate && !isElite(p, popIndex)) {
//            if (Math.random() < crossoverRate && !isUniqueElite(p, popIndex)) {
                // find second parent
                Individual parent2 = selectParentTournament(p);
                // create a 'blank' child
                Individual offspring = new Individual(false);

                //********** OX for block chromosome *************
                // chose the start and end of the sub-sequence
                Random r = new Random();
                int point1 = r.nextInt(parent1.getBlockChromosomeLength()); // nextInt is exclusive on upper bound
                int point2 = r.nextInt(parent1.getBlockChromosomeLength());
                // if point2 is lower, swap them over
                if (point2 < point1) {
                    int temp = point1;
                    point1 = point2;
                    point2 = temp;
                }

                // copy the sub-sequence from parent1 to offspring
                for (int i=point1; i<=point2; i++) {
                    offspring.setBlockGene(i, parent1.getBlockGene(i));
                }

                // move to the next locus on the offspring chromosome
                int offspringLocus = (point2 + 1) % offspring.getBlockChromosomeLength();
                // loop through parent2's chromosome
                for (int i=0; i<parent2.getBlockChromosomeLength(); i++) {
                    int parent2Locus = (i + point2) % parent2.getBlockChromosomeLength();
                    // copy gene if not already in offspring
                    int gene = parent2.getBlockGene(parent2Locus);
                    if (!offspring.containsBlockGene(gene)){
                        offspring.setBlockGene(offspringLocus, gene);
                        // advance to next free locus
                        offspringLocus = (offspringLocus + 1) % offspring.getBlockChromosomeLength();
                    }
                }

                //********** Uniform X for rotation chromosome *************
                for (int i=0; i<parent1.getBlockChromosomeLength(); i++) {
                    if (r.nextInt(100) <50) {  //50% chance of choosing from p1
                        offspring.setRotationGene(i, parent1.getRotationGene(i));
                    } else {
                        offspring.setRotationGene(i, parent2.getRotationGene(i));
                    }
                }

                nextGeneration.setIndividual(popIndex, offspring);

            } else {
                // not chosen for crossover
                nextGeneration.setIndividual(popIndex, parent1);
            }
        }
        return nextGeneration;
    }

    public Population mutate(Population p) {
        // mutate the rotation chromosome using random bit flipping
        // and the blockChromosome using swapping
        Population mutatedPopulation = new Population(p.size()); // start with an empty pop
        p.sortByFitness();
        for (int popIndex=0; popIndex<p.size(); popIndex++) {
            // pluck the next member from the existing population
            Individual member = p.getIndividual(popIndex);
            // chance to mutate each gene in the rotation chromosome (but not for elites)
            for (int locus = 0; locus < member.getRotationChromosomeLength(); locus++) {
                if (Math.random() < mutationRate && !isElite(p, popIndex)) {
                        // flip gene at this locus
                    int newGene = (member.getRotationGene(locus) + 1) % 2;
                    member.setRotationGene(locus, newGene);
                }
            }
            // chance to mutate each gene in the block chromosome (but not for elites)
            for (int locus = 0; locus < member.getBlockChromosomeLength(); locus++) {
                if (Math.random() < mutationRate && !isElite(p, popIndex)) {
                    // swap this gene with another at random
                    int swapLocus = (int) (Math.random() * member.getBlockChromosomeLength());
                    int gene1 = member.getBlockGene(locus);
                    int gene2 = member.getBlockGene(swapLocus);
                    member.setBlockGene(locus, gene2);
                    member.setBlockGene(swapLocus, gene1);
                }
            }

            mutatedPopulation.setIndividual(popIndex, member);
        }
        return mutatedPopulation;
    }

/*  TEST mutation by cycling the possible alleles
    public Population mutate(Population p) {
        Population mutatedPopulation = new Population(p.size()); // start with an empty pop
        for (int popIndex=0; popIndex<p.size(); popIndex++) {
            // pluck the next member from the existing population
            Individual member = p.getIndividual(popIndex);

            // cycle rotation genes 1 & 2 for block 4
            // 0 1 2 = 0
            // 3 4 5 = 1
            int rotValue = member.getRotationGene(13) + member.getRotationGene(12)*2;
            rotValue = (rotValue+1)%4;
            member.setRotationGene(12, rotValue/2);
            member.setRotationGene(13, rotValue%2);
            // toggle the longitudinal rotation gene
            member.setRotationGene(14, (member.getRotationGene(14)+1)%2);
            mutatedPopulation.setIndividual(popIndex, member);
        }
        return mutatedPopulation;
    }

 */
}



