package genetic_Algorithm;

import java.util.*;

/**
 * This is a copied practice of SimpleDemoGA.java
 */
public class Produce_Fittest_Person {
    static int generationCounter = 0;
    static Random rand = new Random();


    public static void main(String[] args) {
       Community community = new Community(10);
        // test the community
        System.out.println("The persons in the group: ");
        for (int i = 0; i <10 ; i++) {
           Person p = community.group.get(i);
            System.out.println();
            for (int j = 0; j <p.genes.length ; j++) {
                System.out.print(p.genes[j]);
            }
        }
        System.out.println();
        // test ending
        community.findFather();
       // System.out.println( community.fatherIndex + " : " + community.fatherScore );


    }

    private static void toMutate() {

    }

    private static void toCrossOver() {

    }
}

/**
 * Person has a fitness, a genes that can calculate fitness, 11111 is the fittest, 00000 is the least fit person
 */
class Person {
    int[] genes = new int[5];
    private int fitScore = 0;

    Person() {
        Random rand = new Random();
        for (int i = 0; i < genes.length; i++) {
            genes[i] = Math.abs(rand.nextInt() % 2);
        }
        calculateFitScore();    // so that getFitScore can be used after instantiation
    }

    private void calculateFitScore() {
        for (int i = 0; i < genes.length; i++) {
            fitScore += genes[i];
        }
    }

    int getFitScore() {
        return fitScore;
    }
}

/**
 * 10 Person forms a community,
 */
class Community {
    int size;
    int fatherScore, motherScore;
    int fatherIndex, motherIndex;
    List<Person> group = new ArrayList<>();
    Community(int size){
        this.size = size;
        for (int i = 0; i <size ; i++) {
            group.add(new Person());
        }
       //System.out.println("GetPersonScore "+ getPersonScore(0));
    }

    int getPersonScore(int index){
        Person p = group.get(index);
     //   System.out.println("In GePersonScore: the person genes is : for index " + index);
//        for (int i = 0; i <5 ; i++) {
//            System.out.print(p.genes[i]);
//        }
//        System.out.println();
        return p.getFitScore();
    }

    int findFather(){
       int temp = Integer.MIN_VALUE;
       int tempIndex = 0;
       for (int i = 0; i < size; i++) {
            System.out.println(i + " -> " + getPersonScore(i));
            if(temp < getPersonScore(i)) {
               // System.out.println("in if loop");
                temp = getPersonScore(i);
                tempIndex = i;
            }
        }
        fatherScore = temp;
        fatherIndex = tempIndex;
        return fatherScore;
    }

    int fintMother(){
        return 0; // TODO:::::
    }



}
