import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Wooden Puzzle Solver");

        /* TEST individual code
        Individual i = new Individual();
        Puzzle p2 = new Puzzle(i);
         */
        /*TEST collision counting with solid and empty blocks
        Block solidBlock = new Block(new int[][][] {
                {
                        {1,1,1,1},
                        {1,1,1,1}},
                {
                        {1,1,1,1},
                        {1,1,1,1}
                }
        });
        Block blankBlock = new Block();
        Puzzle solidP = new Puzzle(solidBlock, solidBlock, solidBlock, solidBlock, solidBlock, solidBlock);
        Puzzle testP = solidP; //new Puzzle(solidBlock, solidBlock, solidBlock, blankBlock, blankBlock, blankBlock);
        Viewer view = new Viewer(650,350);
        view.render(testP, 0, 100);
        System.out.println("Test collision counter:" + testP.collisionCount());
         */

        Viewer view = new Viewer(650, 350);

        // Create genetic algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(300, 0.05, .95, 10, 20);
        System.out.println("GA initialised");

        /* TEST fitness calculation
        System.out.println("Puzzle fitness:");
        System.out.println(ga.calculateFitness(i));
        */

        // initialise the population
        Population population = ga.initPopulation(true);
        System.out.println("population created");
        // Evaluate initial pop
        ga.evaluatePopulation(population);
        System.out.println("initial evaluation complete");
        int generation = 1;
        int oldFitness = -1;

        // start evolving
        Individual currentBest = population.getFittest(0);
        while (!ga.isTerminationConditionMet(currentBest)) {
            // show best solution so far
            currentBest = population.getFittest(0);
            System.out.print("gen " + generation);
            // show fitness of the fittest few individuals

            System.out.print(" ave fitness: " + Math.round(population.getPopulationFitness()*100)/100);
            System.out.print(". Best so far: (" + currentBest.getFitness() + "): ");
            System.out.println(currentBest);
//            System.out.print(" worst so far: (" + population.getFittest(population.size()-1).getFitness() + "): ");
//            System.out.println(population.getIndividual(population.size()-1));

            view.render(new Puzzle(currentBest), 0, 100, false);


            //crossover
            //population = ga.crossover(population);
            // mutate
            population = ga.mutate(population);
            // revaluate
            ga.evaluatePopulation(population);
            generation++;
            if (currentBest.getFitness() != oldFitness) {
                Scanner pause = new Scanner(System.in);
                pause.nextLine();
                oldFitness = currentBest.getFitness();
            }
        }
        Viewer solutionView = new Viewer(1950,350);
        solutionView.render(new Puzzle(currentBest), 0, 100, true);

        System.out.println("Finished after " + generation + " generations.");
        System.out.print("Final fitness: (" + currentBest.getFitness() + "): ");
        System.out.print(currentBest);
    }
}
