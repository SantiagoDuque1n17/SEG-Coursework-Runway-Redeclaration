import RunwayRedeclaration.Exceptions.NegativeParameterException;

import java.util.Scanner;

/* TODO:
Obstacle list
Runway list
Possibly airport list?
*/

public class Main {
    public static void main(String[] args) {

        final Calculations calculations = new Calculations();
        Runway testRunway[] = {new Runway("09L",3595, 3902, 3902, 3902, 306),
                new Runway("27R",3884, 3884, 3962, 3884, 0)};
        Obstacle testObstacle = new Obstacle(25, "big ass obstacle", 50, 40);

        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Enter distance to threshold: ");
        Integer dtt = scanner.nextInt();*/

        int[] dtt = {-50, 3646};

        try {
            int x = 1;
            int y = 0;
            if (dtt[0]<testRunway[0].getOriginalLDA()/2) {
                x = 0;
                y = 1;
            }
            calculations.landingOverObstacle(testRunway[x], testObstacle, dtt[x]);
            calculations.takeOffAwayObstacle(testRunway[x], dtt[x]);
            calculations.takeOffTowardsObstacle(testRunway[y], testObstacle, dtt[y]);
            calculations.landingTowardsObstacle(testRunway[y], dtt[y]);
        } catch (NegativeParameterException e) {
            e.printStackTrace();
        }

        System.out.println("Recalculated runway parameters:");
        System.out.println("TORA = " + testRunway[0].getTORA());
        System.out.println("TODA = " + testRunway[0].getTODA());
        System.out.println("ASDA = " + testRunway[0].getASDA());
        System.out.println("LDA = " + testRunway[0].getLDA());

    }
}
