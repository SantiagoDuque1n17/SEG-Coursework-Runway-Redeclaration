import java.util.Scanner;

/* TODO: 
Obstacle list
Runway list
Possibly airport list?
*/

public class Main {
    public static void main(String[] args) {

        final Calculations calculations = new Calculations();
        Runway testRunway = new Runway("27R",3884, 3884, 3962, 3884);
        Obstacle testObstacle = new Obstacle(25, 50, 50, "big ass obstacle");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter distance to threshold: ");
        Integer dtt = scanner.nextInt();

        calculations.landingOverObstacle(testRunway, testObstacle, dtt);

        System.out.println("Recalculated runway parameters:");
        System.out.println("TORA = " + testRunway.getTORA());
        System.out.println("TODA = " + testRunway.getTODA());
        System.out.println("ASDA = " + testRunway.getASDA());
        System.out.println("LDA = " + testRunway.getLDA());

    }
}

