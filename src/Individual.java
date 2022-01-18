import java.util.Random;

public class Individual {
    /*
    Represents a single puzzle solution
    In other words: a specific, valid, arrangement of the puzzle blocks
    There are 6 blocks and 6 valid block positions
    So this is a basic permutation:
        The positions are represented by the position in the chromosome,
        and which block is in that position is represented by a number 0 to 5
     Each block can also exist in 4 axial rotations and 2 longitudinal rotations,
        So the rotational arrangement of each block has 8 states - a 3-bit number
        and the total rotational description of the puzzle is 6 x 3 = 18 bits
     This means we need 2 chromosomes and 2 different crossover methods:
        order crossover for the block number chromosome
        uniform crossover for the rotation chromosome

     Fitness is an int since we just measure the total number of colliding cubes
     */
    private int[] blockChromosome;
    private int[] rotationChromosome;
    private final int BLOCK_CHROMOSOME_LENGTH = 6;
    private final int ROTATION_CHROMOSOME_LENGTH = 18;
    int fitness;

    public Individual(int[] blockChromosome, int[] rotationChromosome) {
        this.blockChromosome = blockChromosome;
        this.rotationChromosome = rotationChromosome;
        fitness = -1;
    }

    public Individual(boolean randomise) {
        // initialise with random genes
        blockChromosome = new int[BLOCK_CHROMOSOME_LENGTH];
        for (int i=0; i<BLOCK_CHROMOSOME_LENGTH; i++) {
            blockChromosome[i] = i;
        }
        rotationChromosome = new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        if (randomise) {
            randomiseChromosomes();
        }
        fitness = -1;
    }

    public int getBlockGene(int offset) {
        return blockChromosome[offset];
    }

    public int getRotationGene(int offset) {
        return rotationChromosome[offset];
    }

    public void setBlockGene(int offset, int value) {
        blockChromosome[offset] = value;
    }

    public void setRotationGene(int offset, int value) {
        rotationChromosome[offset] = value;
    }

    public int[] getBlockChromosome() {
        return blockChromosome;
    }

    public int[] getRotationChromosome() {
        return rotationChromosome;
    }

    public int getBlockChromosomeLength() {
        return BLOCK_CHROMOSOME_LENGTH;
    }

    public int getRotationChromosomeLength() {
        return ROTATION_CHROMOSOME_LENGTH;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int newValue) {
        fitness = newValue;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        for(int gene: blockChromosome) {
            output.append(gene);
        }
        output.append(' ');
        for(int gene: rotationChromosome) {
            output.append(gene);
        }
        return output.toString();
    }

    public boolean containsBlockGene(int gene) {
        for (int i=0; i<BLOCK_CHROMOSOME_LENGTH; i++) {
            if (blockChromosome[i] == gene) {
                return true;
            }
        }
        return false;
    }


    private void randomiseChromosomes() {
        // block chromosomes must be swapped around
        for (int i=0; i<BLOCK_CHROMOSOME_LENGTH; i++) {
            // swap 2 genes at random
            Random r = new Random();
            int point1 = r.nextInt(BLOCK_CHROMOSOME_LENGTH); // nextInt is exclusive on upper bound
            int gene1 = getBlockGene(point1);
            int point2 = r.nextInt(BLOCK_CHROMOSOME_LENGTH);
            int gene2 = getBlockGene(point2);
            setBlockGene(point1, gene2);
            setBlockGene(point2, gene1);
        }
        // the rotation chromosome can be simply randomised for each bit independently
        for (int i=0; i<ROTATION_CHROMOSOME_LENGTH; i++) {
            if (Math.random() <0.5) {
                setRotationGene(i, 0);
            } else {
                setRotationGene(i, 1);
            }
        }
    }
}
