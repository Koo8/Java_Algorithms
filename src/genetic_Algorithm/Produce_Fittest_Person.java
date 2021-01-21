package genetic_Algorithm;

import java.util.Random;

/**
 * This is a copied practice of SimpleDemoGA.java
 */
public class Produce_Fittest_Person {
    static final int size = 5;
    Community community = new Community(size);
    Random rand = new Random();
    int generationNum = 0;

    public static void main(String[] args) {
        Produce_Fittest_Person program = new Produce_Fittest_Person();
        program.community.getEachFitScore();
        while (program.community.father.fitScore < 5) {
            int count = ++program.generationNum;
            program.toCrossOver();
            if (program.rand.nextInt() % 7 < 5) {  // 85% possibility
                program.toMutate();
            }
            program.replaceWithOffspring();
//            // recalculate scores of every member, find mother and father,
            program.community.getEachFitScore(); // this return father and mother info too.
            program.community.displayAllPersons(count);
            //System.out.println(program.community.father.fitScore + " is father's score in generation "  + count);
        }

        // show result
        System.out.printf("%d generation : Father score is %d\n ", program.generationNum, program.community.father.fitScore);
        program.community.father.showGenes();

    }

    private void replaceWithOffspring() {
        Person offspring;
        // without redo calculateFitScore, the score won't get updated
        community.father.calculateFitScore();
        community.mother.calculateFitScore();
        // compare new mother and father which one is more fit
        if (community.father.fitScore > community.mother.fitScore) {
            offspring = community.father;
        } else {
            offspring = community.mother;
        }
        // get the least fit person in the array
        community.personArray[community.getLeastFitPersonIndex()] = offspring;
    }

    private void toMutate() {
        // mutate the father (may not be the fittest after crossOver)
        int mutatePoint = rand.nextInt(community.father.genes.length);
      //  System.out.println("mutatePoint for father is " + mutatePoint);
        if (community.father.genes[mutatePoint] == 0) {
            community.father.genes[mutatePoint] = 1;
        } else {
            community.father.genes[mutatePoint] = 0;
        }
      //  community.father.showGenes();
        // mutate mother (may not be the second fittest after crossover)
        int mutatePoint1 = rand.nextInt(community.mother.genes.length);
      //  System.out.println("mutatePoint for father is " + mutatePoint1);
        if (community.mother.genes[mutatePoint1] == 0) {
            community.mother.genes[mutatePoint1] = 1;
        } else {
            community.mother.genes[mutatePoint1] = 0;
        }

        //community.mother.showGenes();
    }

    // father and mother exchange some genes
    void toCrossOver() {
        //System.out.println("before crossOver");
//        community.father.showGenes();
//        community.mother.showGenes();
        int temp = 0;
        int crossPoint = rand.nextInt(community.father.genes.length);
        //System.out.println("crossOver point "+ crossPoint);
        for (int i = 0; i < crossPoint; i++) {
           // System.out.println("father at "+ i + " is "+community.father.genes[i]) ;
            temp = community.father.genes[i];
           // System.out.println("temp is " + temp);
            community.father.genes[i] = community.mother.genes[i];
           // System.out.println("Father at " + i + " is "+community.father.genes[i] );
            community.mother.genes[i] = temp;
           // System.out.println("Mother at " + i + " is " +community.mother.genes[i] ) ;
            // NOTE:  community.personArray conent is changed -> basically, father and mother genes are changed
        }
//        System.out.println("after crossOver");
//        community.father.showGenes();
//        community.mother.showGenes();
    }
}

///////
class Person {
    int fitScore;
    int[] genes = new int[5];
    Random rand = new Random();

    Person() {
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Math.abs(rand.nextInt(2));  // create 5 digits of 1 and 0
        }
        fitScore = 0;  // each person start with a fitScore of 0
    }

    void calculateFitScore() {
        fitScore = 0;  // clear all previous score
        for (int i : genes) {
            fitScore += i;
        }
    }

    void showGenes(){
        for (int i = 0; i <genes.length ; i++) {
            System.out.print(genes[i]);
        }
        System.out.println();
    }
}


////////
class Community {
    Person[] personArray;
    int size;
    Person father, mother;
    int fatherIndex, motherIndex;

    Community(int size) {
        this.size = size;
        personArray = new Person[size];
        for (int i = 0; i < personArray.length; i++) {
            personArray[i] = new Person();   // fitScores are all 0 when community is established
        }
    }

    void getEachFitScore() {
        for (Person p : personArray) {
            p.calculateFitScore();
        }
        getFather();
        getMother();
    }

    // NOTE: needs to call getEachFitScore() first before using this.
    Person getFather() {
        int score = Integer.MIN_VALUE;
        for (int i = 0; i < personArray.length; i++) {
            if (score < personArray[i].fitScore) {
                score = personArray[i].fitScore;
                fatherIndex = i;
            }
        }
        father = personArray[fatherIndex];
        return father;
    }

    Person getMother() {
        int first = 0, second = 0;
        for (int i = 0; i < personArray.length; i++) {
            if (personArray[i].fitScore > personArray[first].fitScore) {
                // pass first place to second and take the i place as first place
                second = first;
                first = i;
            } else  // first-Score is larger then compare with second
                if (personArray[i].fitScore > personArray[second].fitScore) {
                    second = i;
                }
        }
        motherIndex = second;
        mother = personArray[second];
        return mother;
    }

    int getLeastFitPersonIndex() {
        int min = Integer.MAX_VALUE;
        int minIndex = 0;
        for (int i = 0; i < personArray.length; i++) {
            if (min > personArray[i].fitScore) {
                minIndex = i;
                min = personArray[i].fitScore;
            }
        }
        return minIndex;
    }

    void displayAllPersons(int num){
        System.out.println("In generation of " + num );
        for (int i = 0; i <personArray.length ; i++) {
            for(int j:personArray[i].genes) {
                System.out.print(j);
            }
            System.out.print(", ");
        }
        System.out.println();
    }
}
