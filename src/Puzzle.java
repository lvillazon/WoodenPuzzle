import java.awt.*;

public class Puzzle {
    // comprises 6 blocks, 2 side-by side on each axis.
    // These are superimposed into a 7th 6x6x6 block array
    // to produce the shape of the assembled puzzle.
    // Each pair of blocks must be offset from each other by one block-width.
    // The puzzle is a valid solution if none of the filled array elements in any of the blocks collide.

    /* The sticking out parts at the end of the block aren't important.
 All we need to model is the middle section of gaps and filled-in parts.
 These are cut on a 4 x 2 grid in the middle of each block.
 So each block can be represented as a 2 x 2 x 4 array.
 */
    private static Block b0 = new Block(new int[][][] {
            {
                    {1,1,1,1},
                    {1,1,1,1}},
            {
                    {1,1,1,1},
                    {1,1,1,1}
            }
    });
    private static Block b1 = new Block(new int[][][] {
            {
                    {1,0,0,1},
                    {0,0,0,1}},
            {
                    {1,1,1,1},
                    {0,0,1,1}
            }
    });
    private static Block b2 = new Block(new int[][][] {
            {
                    {0,0,0,0},
                    {0,0,0,0}},
            {
                    {1,1,1,1},
                    {1,0,0,1}
            }
    });
    private static Block b3 = new Block(new int[][][] {
            {
                    {0,0,0,0},
                    {0,1,1,0}},
            {
                    {1,0,0,1},
                    {1,1,1,1}
            }
    });
    private static Block b4 = new Block(new int[][][] {
            {
                    {0,0,0,0},
                    {0,0,0,0}},
            {
                    {1,1,1,1},
                    {1,1,1,1}
            }
    });
    private static Block b5 = new Block(new int[][][] {
            {
                    {0,0,1,1},
                    {0,0,0,1}},
            {
                    {1,1,1,1},
                    {1,0,0,1}
            }
    });
    private static Block[] blockSet = {b0, b1, b2, b3, b4, b5};

    private Block[] blocks;
    private Block combined;
    private int collisions;  // number of overlapping cubes in the assembled puzzle

    public Puzzle(Block b1, Block b2, Block b3, Block b4, Block b5, Block b6) {
        blocks = new Block[] {b1, b2, b3, b4, b5, b6};
        combined = new Block();
    }

    public Puzzle(Individual solution) {
        // create a puzzle arrangement from a GA solution
        blocks = new Block[solution.getBlockChromosomeLength()];
        combined = new Block();
        for(int i=0; i<solution.getBlockChromosomeLength(); i++) {
            blocks[i] = blockSet[i].clone();
        }
        //TODO: rotate the blocks according to the rotation chromosome
        blocks[2].rotateYZ();
        blocks[3].rotateYZ();
        blocks[4].rotateXZ();
        blocks[5].rotateXZ();

    }

    public int collisionCount() {
        // return the number of times two block cubes share the same point in the puzzle array
        int collisions = 0;
        for(int x=0; x<blocks.length; x++) {
            for(int y=0; y<blocks.length; y++) {
                for(int z=0; z< blocks.length; z++) {
                    if (combined.get(x, y, z) > 1) {
                        collisions = collisions + combined.get(x, y, z)-1;
                    }
                    System.out.println("block "+x+","+y+","+z+"="+ combined.get(x,y,z));
                }
            }
        }
        return collisions;
    }

    private void assemblePuzzle() {
        // copy every block into a single 3D array to create the superimposed puzzle arrangement
        // TODO: set a flag if a collision between blocks is detected
        // 1. copy the 2 blocks in the Z axis, blocks[0] and blocks[1]
        //combined = new Block();
        for(int x=0; x<blocks.length-1; x++) {
            for(int y=0; y<blocks.length-2; y++) {
                for(int z=0; z<blocks.length; z++) {
                    if (blocks[0].get(x, y, z) == 1) {
                        combined.set(x+1, y, z, combined.get(x+1,y,z)+1);
                    }
                    if (blocks[1].get(x, y, z) == 1) {
                        combined.set(x + 1, y+2, z, combined.get(x+1,y+2,z) +1);
                    }
                }
            }
        }

        // 2.copy the 2 blocks in the Y axis, blocks[2] and blocks[3]
        for(int x=0; x<blocks.length-2; x++) {
            for(int y=0; y<blocks.length; y++) {
                for(int z=1; z<blocks.length; z++) {
                    if (blocks[2].get(x, y, z) == 1) {
                        combined.set(x, y, z-1, combined.get(x, y, z-1)+1);
                    }
                    if (blocks[3].get(x, y, z) == 1) {
                        combined.set(x+2, y, z-1, combined.get(x+2,y,z-1)+1);
                    }
                }
            }
        }

        // 3.copy the 2 blocks in the X axis, blocks[4] and blocks[5]
        for(int x=0; x<blocks.length; x++) {
            for(int y=0; y<blocks.length-1; y++) {
                for(int z=2; z<blocks.length; z++) {
                    if (blocks[4].get(x, y, z) == 1) {
                        combined.set(x, y+1, z, combined.get(x,y+1,z)+1);
                    }
                    if (blocks[5].get(x, y, z) == 1) {
                        combined.set(x, y+1, z-2, combined.get(x,y+1,z-2)+1);
                    }
                }
            }
        }
    }

    public void paint(Graphics g, int originX, int originY) {
        // draw the solid puzzle
        // and a wireframe version showing the collisions
        assemblePuzzle();
        combined.paint(g, originX, originY, 0 , 0 , 0, true);
        combined.paint(g, originX + 300, originY, 0 , 0 , 0, false);
    }
}
