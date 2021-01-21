package genetic_Algorithm;

import java.util.Random;

/**
 * This program is an example implementation of a genetic algorithm in Java
 * --> Given a set of 5 genes, each gene can hold the binary values 0 or 1.
 * The fitness value is the number of most 1s in the genome. The maximum fitness is 11111.
 * The least fitness is 00000.
 * This genetic algorithm program tries to maximize the fitness function to provide a population
 * consisting of the dad individual.
 * Note: after crossover and mutation, the least fit individual is replaced from the new dad offspring.
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
    Individual dad;
    Individual mom;
    int generationCount = 0;

    public static void main(String[] args) {

        Random rand = new Random();
        SimpleDemoGA demo = new SimpleDemoGA();

        //Initialize population , all with 0 fitness
        demo.population.initializePopulation(10);

        //Calculate fitness of each individual
        demo.population.calculateFitness();  // this method update the father_Fit_Score value in the population

        System.out.println("Generation: " + demo.generationCount + " Fittest: " + demo.population.father_Fit_Score);

        /////// OOOO If the population father_Fit_Score is less than 5, repeating the FIND PARENTS -> EXCHANGE GEENS -> MUTATE GENES -> ADD FITTEST OFFSPRINGS -> GET NEW FITTEST VALUE
        while (demo.population.father_Fit_Score < 5) { // keep on repeating new generation till it can get a father_Fit_Score of 5 individual
            ++demo.generationCount;

            //each loop will have its own dad and mom
            demo.selection();

            // Exchange genes among parents up till crossOverPoint
            demo.crossover();

            //Do mutation under a random probability
            // NOTE: to use different probability, check TestProbability.java class,
            // if use -1 instead of 5 will have much lower probability
            if (rand.nextInt()%7 < 5) {  // 85% of time
                demo.mutation();   // flip between 0 and 1 at randomly selected index for both parents
            }

            //Add dad offspring to population
            demo.addFittestOffspring();

            //Calculate new fitness value
            demo.population.calculateFitness(); /// this method will update father_Fit_Score of individual

            System.out.println("Generation: " + demo.generationCount + " Fittest: " + demo.population.father_Fit_Score);
            System.out.println();
        }

      //  System.out.println("\nSolution found in generation " + demo.generationCount);
        System.out.println("Fitness: "+demo.population.getTheFather().fitness);
        System.out.print("Genes: ");
        for (int i = 0; i < 5; i++) {
            System.out.print(demo.population.getTheFather().genes[i]);
        }

        System.out.println("");

    }

    //Selection - select the first two father_Fit_Score individualArray
    void selection() {

        //Select the most dad individual
        dad = population.getTheFather();

        //Select the second most dad individual
        mom = population.getTheMother();
    }

    //Crossover - exchange digits among the parents til the crossOverPoint
    void crossover() {
        Random rn = new Random();
//        System.out.println("Before crossover");
//        System.out.println("father is "+ );
        //Select a random crossover point
        int crossOverPoint = rn.nextInt(population.individualArray[0].geneLength); // bounded by the length of gene
        System.out.println("CrossPoint "+ crossOverPoint);
        //Swap values among parents , after this both dad and mom individuals have been changed
        for (int i = 0; i < crossOverPoint; i++) {
            int temp = dad.genes[i];
            dad.genes[i] = mom.genes[i];
            mom.genes[i] = temp;
        }
    }

    //Mutation  - swap 0 to 1 and vice verse for the random index selected for both parents.
    void mutation() {
        Random rn = new Random();

        //Select a random mutation point
        int mutationPoint = rn.nextInt(population.individualArray[0].geneLength);

        //Flip values at the mutation point
        if (dad.genes[mutationPoint] == 0) {
            dad.genes[mutationPoint] = 1;
        } else {
            dad.genes[mutationPoint] = 0;
        }

        mutationPoint = rn.nextInt(population.individualArray[0].geneLength);

        if (mom.genes[mutationPoint] == 0) {
            mom.genes[mutationPoint] = 1;
        } else {
            mom.genes[mutationPoint] = 0;
        }
    }

    //Get dad offspring
    Individual getFittestOffspring() {
        if (dad.fitness > mom.fitness) {
            return dad;
        }
        return mom;
    }


    //Replace least dad individual from most dad offspring
    void addFittestOffspring() {

        //Update fitness values of new dad and mom after crossOver and mutation
        dad.calcFitness();
        mom.calcFitness();

        //Get index of least fit individual among the updated individual array
        int leastFittestIndex = population.getLeastFitPersonIndex();

        //Replace least fit individual with the better new Parent
        population.individualArray[leastFittestIndex] = getFittestOffspring();
//        for (Individual i: population.individualArray) {
//
//            for(int j: i.genes){
//                System.out.print(j);
//            }
//            System.out.println();
//        }
//        System.out.println(" End ");
    }

}


//Individual class
// set each individual a genes of 5 digits composed of 0 or 1;
// calculate its fitness
class Individual {

    int fitness;
    int[] genes = new int[5];
    int geneLength = 5;
    // constructor to create random genes of 5 digits and 0 fitness
    public Individual() {
        Random rn = new Random();

        //Set genes randomly for each individual
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Math.abs(rn.nextInt() % 2); // absolute value of 0 and 1;
        }
        fitness = 0;
    }

    //callig this -  now this individual's fitness is updated
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
    int fatherIndex;

   // int popSize;
   Individual[] individualArray;
    int father_Fit_Score = 0;

    //Initialize population
    // lazily start Population as an Array of Individual with a specified sizes , all fitness are set to 0 to start
    public void initializePopulation(int size) {
        individualArray = new Individual[size];
        for (int i = 0; i < individualArray.length; i++) {
            individualArray[i] = new Individual();
        }
    }

    //calling this update 3 variables - father_fit_Score, fatherIndex and the father individual is returned
    public Individual getTheFather() {
        int maxFitness = Integer.MIN_VALUE;
        int maxFitIndex = 0;
        for (int i = 0; i < individualArray.length; i++) {
            if (maxFitness <= individualArray[i].fitness) {
                maxFitness = individualArray[i].fitness;
                maxFitIndex = i;
            }
        }
        father_Fit_Score = individualArray[maxFitIndex].fitness;
        fatherIndex = maxFitIndex;
        return individualArray[maxFitIndex];
    }

    //Get the second most dad individual
    public Individual getTheMother() {
        int maxFit1 = 0;
        int maxFit2 = 0;
        for (int i = 0; i < individualArray.length; i++) {
            if (individualArray[i].fitness > individualArray[maxFit1].fitness) {
                maxFit2 = maxFit1; // hand out father index to mother,
                maxFit1 = i;   // father use the bigger value of this index
            } else if (individualArray[i].fitness > individualArray[maxFit2].fitness) {
                maxFit2 = i;
            }
        }
        return individualArray[maxFit2];
    }

    //Get index of least dad individual
    public int getLeastFitPersonIndex() {
        int minFitVal = Integer.MAX_VALUE;
        int minFitIndex = 0;
        for (int i = 0; i < individualArray.length; i++) {
            if (minFitVal >= individualArray[i].fitness) {
                minFitVal = individualArray[i].fitness;
                minFitIndex = i;
            }
        }
        return minFitIndex;
    }

    //Calculate fitness of each individual and find which individual is the father
    public void calculateFitness() {

        for (int i = 0; i < individualArray.length; i++) {
            individualArray[i].calcFitness();  // update each fitness, so that individualArray[i].fitness has values other than 0.
        }
        getTheFather();
    }

}
