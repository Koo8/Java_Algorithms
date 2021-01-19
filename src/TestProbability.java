import java.util.*;

/**
 * to calculate the probability of (rand.nextInt() %7 <5), the 5 can be changed from 0 to 6 for different probability
 * 1 -> about 56%
 * 2 -> about 64%
 * 5 -> about 85%
 * -4 -> about 14%.
 */

public class TestProbability {
    static double averageProbability = 0;
    static double sum = 0;
    static List<Double> numArray = new ArrayList<>();
    static Thread[] threads = new Thread[100];
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i <100 ; i++) {
            CountingProbability toCount = new CountingProbability();

            threads[i] = new Thread(toCount,"T-" + i);
            threads[i].start();
            threads[i].join();
            // only after start()and after join(), the run() can finish calculating all thePros
            double num = toCount.thePro;

            numArray.add(num);
        }

        for(double n:numArray){
            //System.out.println("Num: "+ n);
            sum += n;

            averageProbability =sum / 100.0;       // highlight:use 100.0, otherwise will only return 0.
        }
        System.out.println("Sum is " + sum);
        System.out.println(averageProbability + " is the average probability");

    }

    static class CountingProbability implements Runnable {
        // NOTE: change this value to see the different probability
        private static final int THEREMAINDER = -4;
        Random rand = new Random();
        int count = 0;
        double thePro = 0;
        // test remainder value range
        Set<Integer> remainderSet = new HashSet<>();
        @Override
        public void run() {

            for (int i = 0; i <100 ; i++) {
                int num = rand.nextInt();  // without bound -> creates numbers from Max to Min Infinity
                //System.out.println("Random number is "+ num);
                int newNum = num%7; // the value is -6 to 6;
                remainderSet.add(newNum);
                //System.out.println("Modular is "+ newNum);
                if (newNum< THEREMAINDER){ // NOTE: there are negative num, that all will be smaller than THEREMAINDER
                    count++;
                }
            }
            thePro = count/100.0;  // highlight: to make double return, 100.0 is a must.
            System.out.println("Count is "+ count + " -> "+ Thread.currentThread().getName());
            //System.out.println("The probability is " + thePro + " => " + Thread.currentThread().getName());

           // check remainder value range
            remainderSet.stream().forEach(System.out::print);
            System.out.println();
        }
    }
}
