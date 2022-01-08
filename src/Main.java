public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Wooden Puzzle Solver");

        /* The sticking out parts at the end of the block aren't important.
         All we need to model is the middle section of gaps and filled-in parts.
         These are cut on a 4 x 2 grid in the middle of each block.
         So each block can be represented as a 2 x 2 x 4 array.
         */
        Block b1 = new Block(new int[][][] {
                {
                    {1,1,1,0},
                    {1,1,1,1}},
                {
                    {1,1,1,1},
                    {1,1,1,1}
                }
        });
        Block b2 = new Block(new int[][][] {
                {
                        {1,1,1,0},
                        {1,1,1,1}},
                {
                        {1,1,1,1},
                        {1,1,1,1}
                }
        });
        b2.rotateYZ();
        Puzzle p = new Puzzle(b1, b2, b1, b1, b1, b1);
        Viewer view = new Viewer(500,500);
        while (true) {
            view.render(p, 0, 100);
            //b1.rotateXZ();
            Thread.sleep(800);
        }
    }
}
