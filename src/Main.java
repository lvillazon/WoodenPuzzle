import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Wooden Puzzle Solver");

        int runs = 1;
        int totalGenerations =0;
        boolean uniqueElites = false;
        boolean pause = true;

        Viewer view = new Viewer(650, 350);

        Individual currentBest = null;
        for(int q=0; q<runs; q++) {

            // Create genetic algorithm
            GeneticAlgorithm ga = new GeneticAlgorithm(200, 0.05, .95, 1, 10);
            System.out.println("GA initialised");

            // initialise the population
            Population population = ga.initPopulation(true);
            System.out.println("population created");
            // Evaluate initial pop
            ga.evaluatePopulation(population);
            System.out.println("initial evaluation complete");
            int generation = 1;
            int oldFitness = -1;

            // start evolving
            currentBest = population.getFittest(0);
            while (!ga.isTerminationConditionMet(currentBest)) {
                // show best solution so far
                currentBest = population.getFittest(0);
                System.out.print("run " + q + ", gen " + generation);
                // show fitness of the fittest few individuals

                System.out.print(" ave fitness: " + Math.round(population.getPopulationFitness() * 100) / 100);
                System.out.print(". Best so far: (" + currentBest.getFitness() + "): ");
                System.out.println(currentBest);
                //            System.out.print(" worst so far: (" + population.getFittest(population.size()-1).getFitness() + "): ");
                //            System.out.println(population.getIndividual(population.size()-1));

                view.render(new Puzzle(currentBest), 0, 100, false);

                //crossover
                population = ga.crossover(population);
                // mutate
                population = ga.mutate(population);
                // revaluate
                ga.evaluatePopulation(population);
                generation++;

                if (pause && currentBest.getFitness() != oldFitness) {
                    Scanner input = new Scanner(System.in);
                    input.nextLine();
                    oldFitness = currentBest.getFitness();
                }

            }
            System.out.println("Finished after " + generation + " generations.");
            System.out.print("Final fitness: (" + currentBest.getFitness() + "): ");
            System.out.print(currentBest);
            totalGenerations = totalGenerations + generation;
        }

        Viewer solutionView = new Viewer(1950,350);
        solutionView.render(new Puzzle(currentBest), 0, 100, true);
        int ave_generations = totalGenerations/runs;
        System.out.println(" Average generations over " + runs + " trials = " + ave_generations);
    }
}
