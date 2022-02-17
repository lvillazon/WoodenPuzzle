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
    private int top;        // numbered 0 to 5, these values provide the orientation of the block
    private int front;      // so we know what colour to use for the individual cubes
                            // the two ends of the block are 0 and 5
                            // and the long sides are numbered 1, 2, 3, 4
                            // when 0 or 5 are the front, the block is facing towards the camera

    public Block() {
        top = 2;
        front = 1;
        initialise();
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

    public Block(int[][][] shape) {
        /* the 2x2x4 shape array only represents the middle section of each block.
           This is the part that varies from block to block. There is a solid end-cap at the long
           end of each one. This 2x2x6 block is placed in the middle of
           a 6x6x6 array, to allow easy rotation by coordinate transposition
         */
        top = 2;
        front = 1;
        initialise();
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
        copy.front = front;
        copy.top = top;
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
                        cube.paint(g, top, front, solid);
                    }
                    if (blockArray[x][y][z] > 1){
                        // construct and paint a cube
                        Cube cube = new Cube(originX, originY, x+offsetX, y+offsetY, z+offsetZ);
                        cube.paint(g, top, front, true);
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
        // this array determines the new top face after rotation
        // which obviously depends on the current top and front faces
        // the new front is given by frontRotation[top][front]
        // a 9 means that this value is impossible for this orientation
        int[][] topRotation = {
        // front=0 1 2 3 4 5
                {9,4,1,2,3,9}, // top = 0
                {2,9,5,9,0,4}, // top = 1
                {3,0,9,5,9,1}, // top = 2
                {4,9,0,9,5,2}, // top = 3
                {1,5,9,0,9,3}, // top = 4
                {9,2,3,4,1,9}, // top = 5
        };
        int oldTop=top;
//        System.out.print("XY: top="+top+" front="+front+"rotating to...");
//        System.out.println("top="+topRotation[top][front]);
        top = topRotation[top][front];
        // front face doesn't change after XZ rotation
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
        // this array determines the new top face after rotation
        // which obviously depends on the current top and front faces
        // the new front/top is given by xxxRotation[top][front]
        // a 9 means that this value is impossible for this orientation
        int[][] topRotation = {
        // front=0 1 2 3 4 5
                {9,1,2,3,4,9}, // top = 0
                {0,9,2,9,4,5}, // top = 1
                {0,1,9,3,9,5}, // top = 2
                {0,9,2,9,4,5}, // top = 3
                {0,1,9,3,9,5}, // top = 4
                {9,1,2,3,4,9}, // top = 5
        };
        int[][] frontRotation = {
        // front=0 1 2 3 4 5
                {9,5,5,5,5,9}, // top = 0
                {3,9,3,9,3,3}, // top = 1
                {4,4,9,4,9,4}, // top = 2
                {1,9,1,9,1,1}, // top = 3
                {2,2,9,2,9,2}, // top = 4
                {9,0,0,0,0,9}, // top = 5
        };
        int oldFront = front;
        int oldTop = top;

//        System.out.print("YZ: top="+top+" front="+front+"rotating to...");
//        System.out.println("top="+topRotation[oldTop][oldFront]);
//        System.out.print("YZ: top="+top+" front="+front+"rotating to...");
//        System.out.println("front="+frontRotation[oldTop][oldFront]);

        top = topRotation[oldTop][oldFront];
        front = frontRotation[oldTop][oldFront];
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
        // this array determines the new front face after rotation
        // which obviously depends on the current top and front faces
        // the new front is given by frontRotation[top][front]
        // a 9 means that this value is impossible for this orientation
        int[][] frontRotation = {
                // front=0 1 2 3 4 5
                {9,2,3,4,1,9}, // top = 0
                {4,9,0,9,5,2}, // top = 1
                {1,5,9,0,9,3}, // top = 2
                {2,9,5,9,0,4}, // top = 3
                {3,0,9,5,9,1}, // top = 4
                {9,4,1,2,3,9}, // top = 5
        };
        int oldFront=front;
//        System.out.print("XZ: top="+top+" front="+front+"rotating to...");
//        System.out.println("front="+frontRotation[top][front]);
        front = frontRotation[top][front];
        // top face doesn't change after XZ rotation
    }

}
