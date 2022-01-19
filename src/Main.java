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
        GeneticAlgorithm ga = new GeneticAlgorithm(200, 0.5, .95, 5, 10);

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
        while (ga.isTerminationConditionMet(generation, maxGenerations) == false) {
            // show best solution so far
            Individual fittest = population.getFittest(0);
            System.out.print("gen " + generation);
            System.out.print(" ave fitness: " + population.getPopulationFitness());
            System.out.print(" Best so far: (" + fittest.getFitness() + "): ");
            System.out.println(fittest.toString());
            view.render(new Puzzle(fittest), 0, 100);

            // TODO crossover
            //population = ga.crossover(population);
            // TODO mutate
            population = ga.mutate(population);
            // revaluate
            ga.evaluatePopulation(population);
            generation++;
            //Thread.sleep(1000);
        }
    }
}
