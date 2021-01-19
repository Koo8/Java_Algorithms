package genetic_Algorithm;

import java.util.Random;

/**
 * This program is an example implementation of a genetic algorithm in Java
 * --> Given a set of 5 genes, each gene can hold the binary values 0 or 1.
 * The fitness value is the number of most 1s in the genome. The maximum fitness is 11111.
 * The least fitness is 00000.
 * This genetic algorithm program tries to maximize the fitness function to provide a population
 * consisting of the fittestUnit individual.
 * Note: after crossover and mutation, the least fit individual is replaced from the new fittestUnit offspring.
 * PseudoCode: -->
 * START oo
 *  Generate the initial population
 *  Compute fitness
 *  REPEAT
 *     Selection
 *     Crossover  => most important step in GA, a crossover point is chosen at random from
 *                   within the genes of both parents. Offspring are created by exchanging
 *                   the genes of parents among themselves until the crossover point is reached.
 *     Mutation   => In certain new offspring formed, some of their genes can be subjected to a
 *                   mutation with a low random probability. This implies that some of the bits
 *                   in the bit string can be flipped. The purpose is to maintain diversity of
 *                   population and prevent premature convergence.
 *     Compute fitness
 *  UNTIL population has converged
 *  STOP oo
 *  The population has a fixed size. Old unfitted individual will be replaced by new offspring with
 *  better fitness.
 *  The sequence is repeated to produce better next generations.
 */
public class SimpleDemoGA {
     // genetic algorithm
    Population population = new Population();
    Individual fittestUnit;
    Individual secondFittestUnit;
    int generationCount = 0;

    public static void main(String[] args) {

        Random rand = new Random();
        SimpleDemoGA demo = new SimpleDemoGA();

        //Initialize population
        demo.population.initializePopulation(10);

        //Calculate fitness of each individual
        demo.population.calculateFitness();  // this method update the fittest value in the population

        System.out.println("Generation: " + demo.generationCount + " Fittest: " + demo.population.fittest);

        /////// OOOO If the population fittest is less than 5, repeating the FIND PARENTS -> EXCHANGE GEENS -> MUTATE GENES -> ADD FITTEST OFFSPRINGS -> GET NEW FITTEST VALUE
        while (demo.population.fittest < 5) { // keep on repeating new generation till it can get a fittest of 5 individual
            ++demo.generationCount;

            //Select parents
            demo.selection();

            // Exchange genes among parents up till crossOverPoint
            demo.crossover();

            //Do mutation under a random probability
            // NOTE: to use different probability, check TestProbability.java class,
            // if use -1 instead of 5 will have much lower probability
            if (rand.nextInt()%7 < 5) {  // 85% of time
                demo.mutation();   // flip between 0 and 1 at randomly selected index for both parents
            }

            //Add fittestUnit offspring to population
            demo.addFittestOffspring();

            //Calculate new fitness value
            demo.population.calculateFitness(); /// this method will update fittest of individual

            System.out.println("Generation: " + demo.generationCount + " Fittest: " + demo.population.fittest);
            System.out.println();
        }

      //  System.out.println("\nSolution found in generation " + demo.generationCount);
        System.out.println("Fitness: "+demo.population.getFittest().fitness);
        System.out.print("Genes: ");
        for (int i = 0; i < 5; i++) {
            System.out.print(demo.population.getFittest().genes[i]);
        }

        System.out.println("");

    }

    //Selection - select the first two fittest individuals
    void selection() {

        //Select the most fittestUnit individual
        fittestUnit = population.getFittest();

        //Select the second most fittestUnit individual
        secondFittestUnit = population.getSecondFittest();
    }

    //Crossover - exchange digits among the parents til the crossOverPoint
    void crossover() {
        Random rn = new Random();

        //Select a random crossover point
        int crossOverPoint = rn.nextInt(population.individuals[0].geneLength); // bounded by the length of gene

        //Swap values among parents
        for (int i = 0; i < crossOverPoint; i++) {
            int temp = fittestUnit.genes[i];
            fittestUnit.genes[i] = secondFittestUnit.genes[i];
            secondFittestUnit.genes[i] = temp;

        }

    }

    //Mutation  - swap 0 to 1 and vice verse for the random index selected for both parents.
    void mutation() {
        Random rn = new Random();

        //Select a random mutation point
        int mutationPoint = rn.nextInt(population.individuals[0].geneLength);

        //Flip values at the mutation point
        if (fittestUnit.genes[mutationPoint] == 0) {
            fittestUnit.genes[mutationPoint] = 1;
        } else {
            fittestUnit.genes[mutationPoint] = 0;
        }

        mutationPoint = rn.nextInt(population.individuals[0].geneLength);

        if (secondFittestUnit.genes[mutationPoint] == 0) {
            secondFittestUnit.genes[mutationPoint] = 1;
        } else {
            secondFittestUnit.genes[mutationPoint] = 0;
        }
    }

    //Get fittestUnit offspring
    Individual getFittestOffspring() {
        if (fittestUnit.fitness > secondFittestUnit.fitness) {
            return fittestUnit;
        }
        return secondFittestUnit;
    }


    //Replace least fittestUnit individual from most fittestUnit offspring
    void addFittestOffspring() {

        //Update fitness values of offspring
        fittestUnit.calcFitness();
        secondFittestUnit.calcFitness();

        //Get index of least fit individual
        int leastFittestIndex = population.getLeastFittestIndex();

        //Replace least fittestUnit individual from most fittestUnit offspring
        population.individuals[leastFittestIndex] = getFittestOffspring();
    }

}


//Individual class
// set each individual a genes of 5 digits composed of 0 or 1;
// calculate its fitness
class Individual {

    int fitness;
    int[] genes = new int[5];
    int geneLength = 5;
    // constructor to create random genes
    public Individual() {
        Random rn = new Random();

        //Set genes randomly for each individual
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Math.abs(rn.nextInt() % 2); // absolute value of 0 and 1;
        }

        fitness = 0;
    }

    //Calculate fitness
    public void calcFitness() {
        // TODO: reset fitness to 0 ????
        fitness = 0;
        for (int i = 0; i < 5; i++) {
            if (genes[i] == 1) {
                ++fitness;   // fitness is from 1 to 5 scale;
            }
        }
    }
}

//Population class
class Population {

   // int popSize;
   Individual[] individuals;
    int fittest = 0;

    //Initialize population
    // lazily start Population as an Array of Individual with a specified sizes
    public void initializePopulation(int size) {
        individuals = new Individual[size];
        for (int i = 0; i < individuals.length; i++) {
            individuals[i] = new Individual();
        }
    }

    //Get the fittestUnit individual
    public Individual getFittest() {
        int maxFit = Integer.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (maxFit <= individuals[i].fitness) {
                maxFit = individuals[i].fitness;
                maxFitIndex = i;
            }
        }
        fittest = individuals[maxFitIndex].fitness;
        return individuals[maxFitIndex];
    }

    //Get the second most fittestUnit individual
    public Individual getSecondFittest() {
        int maxFit1 = 0;
        int maxFit2 = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (individuals[i].fitness > individuals[maxFit1].fitness) {
                maxFit2 = maxFit1;
                maxFit1 = i;
            } else if (individuals[i].fitness > individuals[maxFit2].fitness) {
                maxFit2 = i;
            }
        }
        return individuals[maxFit2];
    }

    //Get index of least fittestUnit individual
    public int getLeastFittestIndex() {
        int minFitVal = Integer.MAX_VALUE;
        int minFitIndex = 0;
        for (int i = 0; i < individuals.length; i++) {
            if (minFitVal >= individuals[i].fitness) {
                minFitVal = individuals[i].fitness;
                minFitIndex = i;
            }
        }
        return minFitIndex;
    }

    //Calculate fitness of each individual
    public void calculateFitness() {

        for (int i = 0; i < individuals.length; i++) {
            individuals[i].calcFitness();
        }
        getFittest();
    }

}
