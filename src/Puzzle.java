import java.awt.*;

public class Puzzle {
    // comprises 6 blocks, 2 side-by side on each axis.
    // These are superimposed into a 7th 6x6x6 block array
    // to produce the shape of the assembled puzzle.
    // Each pair of blocks must be offset from each other by one block-width.
    // The puzzle is a valid solution if none of the filled array elements in any of the blocks collide.

    private Block[] blocks;
    private Block combined;
    private int collisions;  // number of overlapping cubes in the assembled puzzle


    public Puzzle(Block b1, Block b2, Block b3, Block b4, Block b5, Block b6) {
        blocks = new Block[] {b1, b2, b3, b4, b5, b6};
        combined = new Block();
    }

    private void assemblePuzzle() {
        // copy every block into a single 3D array to create the superimposed puzzle arrangement
        // TODO: set a flag if a collision between blocks is detected
        // copy the 2 blocks in the Z axis, blocks[0] and blocks[1]

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

        // copy the 2 blocks in the Y axis, blocks[2] and blocks[3]
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

        // copy the 2 blocks in the X axis, blocks[4] and blocks[5]
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
        // superimpose all the blocks onto a new block grid,
        // with the correct offsets to assemble the puzzle shape
        assemblePuzzle();
        combined.paint(g, originX, originY, 0 , 0 , 0, true);
        combined.paint(g, originX + 300, originY, 0 , 0 , 0, false);
    }
}
