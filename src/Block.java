import java.awt.*;

public class Block {
    /* models a 3D cuboid with cutouts
       each block is one piece of the puzzle that must be rotated into the
       correct arrangement to assemble the finished puzzle
       The cuboid is abstracted as a 3D array.
       A 1 is a solid portion, a 0 is a gap.
    */

    private int[][][] blockArray;
    private int blockSize;
    private Color blockColor;

    public Block() {
        initialise();
        blockColor = Color.gray;
    }

    private void initialise() {
        // create an empty 6x6x6 array
        blockSize = 6;
        blockArray = new int[blockSize][blockSize][blockSize];
        // initialise empty array
        for(int x=0; x<blockSize; x++) {
            for(int y=0; y<blockSize; y++) {
                for(int z=0; z<blockSize; z++) {
                    blockArray[x][y][z] = 0;
                }
            }
        }
    }

    public Block(int[][][] shape, Color c) {
        /* the 2x2x4 shape array only represents the middle section of each block.
           This is the part that varies from block to block. There is a solid end-cap at the long
           end of each one. This 2x2x6 block is placed in the middle of
           a 6x6x6 array, to allow easy rotation by coordinate transposition
         */
        initialise();
        blockColor = c;
        // copy the shape array into the middle of the empty block array
        for(int x=0; x<2; x++) {
            for(int y=0; y<2; y++) {
                for(int z=0; z<blockSize; z++) {
                    if (z==0 || z==blockSize-1) {
                        // add end-cap
                        blockArray[x+2][y+2][z] = 1;
                    } else {
                        blockArray[x+2][y+2][z] = shape[x][y][z-1];
                    }
                }
            }
        }
    }

    public Block clone() {
        // create a copy of this block
        Block copy = new Block();
        for(int x=0; x<blockSize; x++) {
            for(int y=0; y<blockSize; y++) {
                for(int z=0; z<blockSize; z++) {
                    copy.set(x,y,z, get(x,y,z));
                }
            }
        }
        copy.blockColor = blockColor;
        return copy;
    }

    public int get(int x, int y, int z) {
        return blockArray[x][y][z];
    }

    public void set(int x, int y, int z, int value) {
        blockArray[x][y][z] = value;
    }

    public void paint(Graphics g, int originX, int originY, int offsetX, int offsetY, int offsetZ, boolean solid) {
        for(int z=blockSize-1; z>=0; z--) {
            for (int x = 0; x < blockSize; x++) {
                for (int y = blockSize-1; y>=0; y--) {
                    if (blockArray[x][y][z] == 1){
                        // construct and paint a cube
                        Cube cube = new Cube(originX, originY, x+offsetX, y+offsetY, z+offsetZ);
                        cube.paint(g, blockColor, solid);
                    }
                    if (blockArray[x][y][z] > 1){
                        // construct and paint a cube
                        Cube cube = new Cube(originX, originY, x+offsetX, y+offsetY, z+offsetZ);
                        cube.paint(g, blockColor, true);
                    }
                }
            }
        }
    }

    public void rotateXY() {
        // rotate the shape 90-degrees in the XY plane by transposing indices
        int[][] temp = new int[blockSize][blockSize];
        for(int z=0; z<blockSize; z++) {
            for(int x=0; x<blockSize; x++) {
                for(int y=0; y<blockSize; y++) {
                    temp[y][x] = blockArray[x][y][z];
                }
            }
            // reverse each column as we copy back, to complete the rotation
            for(int x=0; x<blockSize; x++) {
                for(int y=0; y<blockSize; y++) {
                    blockArray[x][blockSize-y-1][z] = temp[x][y];
                }
            }
        }
    }

    public void rotateYZ() {
        // rotate the shape 90-degrees in the YZ plane by transposing indices
        int[][] temp = new int[blockSize][blockSize];
        for(int x=0; x<blockSize; x++) {
            for(int y=0; y<blockSize; y++) {
                for(int z=0; z<blockSize; z++) {
                    temp[z][y] = blockArray[x][y][z];
                }
            }
            // reverse each column as we copy back, to complete the rotation
            for(int y=0; y<blockSize; y++) {
                for(int z=0; z<blockSize; z++) {
                    blockArray[x][y][blockSize-z-1] = temp[y][z];
                }
            }
        }
    }

    public void rotateXZ() {
        // rotate the shape 90-degrees in the XZ plane by transposing indices
        int[][] temp = new int[blockSize][blockSize];
        for(int y=0; y<blockSize; y++) {
            for(int x=0; x<blockSize; x++) {
                for(int z=0; z<blockSize; z++) {
                    temp[z][x] = blockArray[x][y][z];
                }
            }
            // reverse each column as we copy back, to complete the rotation
            for(int x=0; x<blockSize; x++) {
                for(int z=0; z<blockSize; z++) {
                    blockArray[x][y][blockSize-z-1] = temp[x][z];
                }
            }
        }
    }

}
