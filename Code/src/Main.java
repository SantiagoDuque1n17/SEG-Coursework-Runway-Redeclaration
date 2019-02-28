import Exceptions.*;

import java.util.Scanner;

/* TODO:
Obstacle list
Runway list
Possibly airport list?
*/

public class Main {
    public static void main(String[] args) {

        PhysicalRunway testRunway = new PhysicalRunway(new Runway("09L",3595, 3902, 3902, 3902, 306),
                new Runway("27R",3884, 3884, 3962, 3884, 0));
        Obstacle testObstacle = new Obstacle(25, "big ass obstacle", -50, 3646, 40);

        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Enter distance to threshold: ");
        Integer dtt = scanner.nextInt();*/

        try {
            testRunway.addObstacle(testObstacle);
        } catch (DontNeedRedeclarationException | NegativeParameterException e) {
            e.printStackTrace();
        }


    }
}
