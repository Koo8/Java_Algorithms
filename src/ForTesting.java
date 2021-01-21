import java.util.Random;

public class ForTesting {

    public static void main(String[] args) {
        Random rand = new Random();
        for (int i = 0; i <10 ; i++) {
            int num = Math.abs(rand.nextInt(2));
            System.out.print(num );
        }
    }
}
