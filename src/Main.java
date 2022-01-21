public class Main {

    public static int maxGenerations = 1000;

    public static void main(String[] args) throws InterruptedException {
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
        GeneticAlgorithm ga = new GeneticAlgorithm(200, 0.05, .95, 0, 10);

        /* TEST fitness calculation
        System.out.println("Puzzle fitness:");
        System.out.println(ga.calculateFitness(i));
        */

        // initialise the population
        Population population = ga.initPopulation(true);
        // Evaluate initial pop
        ga.evaluatePopulation(population);
        int generation = 1;

        // start evolving
        Individual currentBest = population.getFittest(0);
        while (ga.isTerminationConditionMet(currentBest) == false) {
            // show best solution so far
            currentBest = population.getFittest(100);
            System.out.print("gen " + generation);
            System.out.print(" ave fitness: " + population.getPopulationFitness());
            System.out.print(" Best so far: (" + currentBest.getFitness() + "): ");
            System.out.print(currentBest);
            System.out.print(" worst so far: (" + population.getFittest(population.size()-1).getFitness() + "): ");
            System.out.println(population.getIndividual(population.size()-1));
            view.render(new Puzzle(currentBest), 0, 100);

            //TODO crossover - block crossover implemented but not rotation crossover
            //population = ga.crossover(population);
            // mutate
            population = ga.mutate(population);
            // revaluate
            ga.evaluatePopulation(population);
            generation++;
            //Thread.sleep(1000);
        }
    }
}
