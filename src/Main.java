public class Main {

    public static int maxGenerations = 1000;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Wooden Puzzle Solver");

        // OLD TEST HARNESS CODE

        Block b1 = new Block(new int[][][] {
                {
                    {1,1,1,1},
                    {1,1,1,1}},
                {
                    {1,1,1,1},
                    {1,1,1,1}
                }
        });
        Block b2 = new Block(new int[][][] {
                {
                        {1,0,0,1},
                        {0,0,0,1}},
                {
                        {1,1,1,1},
                        {0,0,1,1}
                }
        });
        Block b3 = new Block(new int[][][] {
                {
                        {0,0,0,0},
                        {0,0,0,0}},
                {
                        {1,1,1,1},
                        {1,0,0,1}
                }
        });
        Block b4 = new Block(new int[][][] {
                {
                        {0,0,0,0},
                        {0,1,1,0}},
                {
                        {1,0,0,1},
                        {1,1,1,1}
                }
        });
        Block b5 = new Block(new int[][][] {
                {
                        {0,0,0,0},
                        {0,0,0,0}},
                {
                        {1,1,1,1},
                        {1,1,1,1}
                }
        });
        Block b6 = new Block(new int[][][] {
                {
                        {0,0,1,1},
                        {0,0,0,1}},
                {
                        {1,1,1,1},
                        {1,0,0,1}
                }
        });
        b3.rotateYZ();
        b4.rotateYZ();
        b5.rotateXZ();
        b6.rotateXZ();
        Puzzle p1 = new Puzzle(b1, b2, b3, b4, b5, b6);
//        Viewer view = new Viewer(650,350);
//        view.render(p1, 0, 100);
            //b1.rotateXZ();
//            Thread.sleep(800);

        // TEST individual code
        Individual i = new Individual();
        Puzzle p2 = new Puzzle(i);
        Viewer view = new Viewer(650,350);
        view.render(p2, 0, 100);

/*

        // Create genetic algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(200, 0.05, .5, 5, 10);

        // test fitness calculation
        //System.out.println("Tour fitness:");
        //System.out.println(ga.calculateFitness(test));

        // initialise the population
        Population population = ga.initPopulation(20);
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

            // TODO crossover
            //population = ga.crossover(population);
            // TODO mutate
            //population = ga.mutate(population);
            // revaluate
            ga.evaluatePopulation(population);
            generation++;
        }

 */
    }
}
