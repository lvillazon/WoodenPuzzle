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
                    {1,0,0,1},
                    {1,1,1,1}
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
    private static Block b5 = new Block(new int[][][]{
            {
                    {0, 0, 1, 1},
                    {0, 0, 0, 1}},
            {
                    {1, 1, 1, 1},
                    {1, 0, 0, 1}
            }
    });
    private static Block blank = new Block(new int[][][] {
            {
                    {0,0,0,0},
                    {0,0,0,0}},
            {
                    {0,0,0,0},
                    {0,0,0,0}
            }
    });
    private static Block test = new Block(new int[][][] {
            {
                    {1,0,1,0},
                    {0,1,0,1}},
            {
                    {0,1,0,1},
                    {1,0,1,0}
            }
    });
    private static Block[] blockSet = {b0, b1, b2, b3, b4, b5};
//    private static Block[] blockSet = {test,test,test,test,test,test,};

    private Block[] blocks;
    private Block combined;
    private int collisions;  // number of overlapping cubes in the assembled puzzle

    /*
    public Puzzle(Block b1, Block b2, Block b3, Block b4, Block b5, Block b6) {
        blocks = new Block[] {
                b1.clone(),
                b2.clone(),
                b3.clone(),
                b4.clone(),
                b5.clone(),
                b6.clone()};
        blocks[2].rotateYZ();
        blocks[3].rotateYZ();
        blocks[4].rotateXZ();
        blocks[5].rotateXZ();
        assemblePuzzle();
    }
     */

    public Puzzle(Individual solution) {
        // create a puzzle arrangement from a GA solution
        blocks = new Block[solution.getBlockChromosomeLength()];
        for(int i=0; i<solution.getBlockChromosomeLength(); i++) {
           blocks[i] = blockSet[solution.getBlockGene(i)].clone();
        }
        // rotate the blocks into their initial orientation, so that there are 2 blocks for each axis
        blocks[2].rotateYZ();
        blocks[3].rotateYZ();
        blocks[4].rotateXZ();
        blocks[5].rotateXZ();
        // now rotate the blocks according to the rotation chromosome
        // each block's rotation is described by 3 genes:
        // the first 2 encode a 2-bit number to describe the number of times to rotate the block
        // around its long axis. The 3rd bit states whether the block is also flipped 180-degrees
        // about one of the other axes (it doesn't really matter which).

        // blocks 0 & 1
        for (int b=0; b<=1; b++) {
            int offset = b * 3;
            int axialRotations = solution.getRotationGene(offset) * 2 + solution.getRotationGene(offset+1); // 0 - 3 rotations
            int longitudinal_rotations = solution.getRotationGene(offset+2) * 2; // 2 90 rotations is he same as a 180 flip
            for (int i = 0; i < axialRotations; i++) {
                blocks[b].rotateXY();
            }
            for (int i = 0; i < longitudinal_rotations; i++) {
                blocks[b].rotateXZ();
            }
        }

        // blocks 2 & 3
        for (int b=2; b<=3; b++) {
            int offset = b * 3;
            int axialRotations = solution.getRotationGene(offset) * 2 + solution.getRotationGene(offset+1); // 0 - 3 rotations
            int longitudinal_rotations = solution.getRotationGene(offset+2) * 2; // 2 90 rotations is he same as a 180 flip
            for (int i = 0; i < axialRotations; i++) {
                blocks[b].rotateXZ();
            }
            for (int i = 0; i < longitudinal_rotations; i++) {
                blocks[b].rotateXY();
            }
        }

        // blocks 4 & 5
        for (int b=4; b<=5; b++) {
            int offset = b * 3;
            int axialRotations = solution.getRotationGene(offset) * 2 + solution.getRotationGene(offset + 1); // 0 - 3 rotations
            int longitudinal_rotations = solution.getRotationGene(offset + 2) * 2; // 2 90 rotations is he same as a 180 flip
            for (int i = 0; i < axialRotations; i++) {
                blocks[b].rotateYZ();
            }
            for (int i = 0; i < longitudinal_rotations; i++) {
                blocks[b].rotateXY();
            }
        }


        // finally, superimpose all the blocks to create a single 3D array for the whole puzzle
        assemblePuzzle();
    }

    public int collisionCount() {
        // return the number of times two block cubes share the same point in the puzzle array
        int collisions = 0;
        if (combined == null) {
            assemblePuzzle();
        }
        for(int x=0; x<blocks.length; x++) {
            for(int y=0; y<blocks.length; y++) {
                for(int z=0; z< blocks.length; z++) {
                    if (combined.get(x, y, z) > 1) {
                        collisions = collisions + combined.get(x, y, z) -1;
                    }
                }
            }
        }
        return collisions;
    }

    private void assemblePuzzle() {
        // copy every block into a single 3D array to create the superimposed puzzle arrangement
        // TODO: set a flag if a collision between blocks is detected
        combined = new Block();
        // copy the 2 blocks in the Z axis, blocks[0] and blocks[1]
        for(int x=0; x<blocks.length-1; x++) {
            for (int y = 1; y < blocks.length - 1; y++) {
                for (int z = 0; z < blocks.length; z++) {
                    int v1 = blocks[0].get(x, y, z) + combined.get(x, y-1, z);
                    int v2 = blocks[1].get(x, y, z) + combined.get(x, y + 1, z);
                    combined.set(x, y-1, z, v1);
                    combined.set(x, y + 1, z, v2);
                }
            }
        }

        // copy the 2 blocks in the Y axis, blocks[2] and blocks[3]
        for(int x=1; x<blocks.length-1; x++) {
            for(int y=0; y<blocks.length; y++) {
                for(int z=1; z<blocks.length; z++) {
                    int v1 = blocks[2].get(x, y, z) + combined.get(x-1,y,z);
                    int v2 = blocks[3].get(x, y, z) + combined.get(x+1,y,z);
                    combined.set(x-1, y, z, v1);
                    combined.set(x+1, y, z, v2);
                }
            }
        }

        // copy the 2 blocks in the X axis, blocks[4] and blocks[5]
        for(int x=0; x<blocks.length; x++) {
            for(int y=0; y<blocks.length; y++) {
                for(int z=1; z<blocks.length-1; z++) {
                    int v1 = blocks[4].get(x, y, z) + combined.get(x,y,z+1);
                    int v2 = blocks[5].get(x, y, z) + combined.get(x,y,z-1);
                    combined.set(x, y, z+1, v1);
                    combined.set(x, y, z-1, v2);
                }
            }
        }
    }

    public void paint(Graphics g, int originX, int originY, boolean exploded) {
        if (exploded) {
            // draw and exploded diagram representation of the puzzle blocks
            // this allows the final solution to be viewed, so that you can reproduce it in real life
            for (int i=0; i<blocks.length; i++) {
                for (int j=0; j<blocks.length; j++) {
                    blocks[j].paint(g, originX + i * 250, originY, 0, 0, 0, j==i);
                }
            }
        } else {
            // draw the solid puzzle
            // and a wireframe version showing the collisions
            combined.paint(g, originX, originY, 0, 0, 0, true);
            combined.paint(g, originX + 300, originY, 0, 0, 0, false);
        }
    }

}
