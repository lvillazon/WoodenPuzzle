import java.awt.*;

public class Puzzle {
    // comprises 6 blocks, 2 side-by side on each axis.
    // crossing in the middle

    // DOESN'T WORK
    /* I can't simply make a puzzle out of an array of blocks,
        because the z-order of drawing the cubes within each block is important to preserve the isometric 3D
        Instead, the puzzle should inherit from Block and be its own 3D array of cubes,
        comprising the union of the cubes in the 6 blocks
     */

    private Block[] blocks;
    private Block combined;

    public Puzzle(Block b1, Block b2, Block b3, Block b4, Block b5, Block b6) {
        blocks = new Block[] {b1, b2, b3, b4, b5, b6};
        combined = new Block();
    }

    private void assemblePuzzle() {
        // copy every block into a single 3D array to create the superimposed puzzle arrangement
        // TODO: set a flag if a collision between blocks is detected
        for (int b=0; b< blocks.length; b++) {
            for(int x=0; x<blocks.length; x++) {

            }
        }
    }

    public void paint(Graphics g, int originX, int originY) {
        // superimpose all the blocks onto a new block grid,
        // with the correct offsets to assemble the puzzle shape
        for (int b=0; b<blocks.length; b++) {

            blocks[b].paint(g, originX, originY, 0 , 0 , 0);
        }
    }
}
