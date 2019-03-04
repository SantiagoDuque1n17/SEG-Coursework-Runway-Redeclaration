import Exceptions.DontNeedRedeclarationException;
import Exceptions.NegativeParameterException;

import java.util.*;

public class Interface {


    private static final Scanner scanner = new Scanner(System.in);
    private List<PhysicalRunway> runwayList;
    private List<Obstacle> obstacleList;

    public Interface(List<PhysicalRunway> runwayList, List<Obstacle> obstacleList) {
        this.runwayList = runwayList;
        this.obstacleList = obstacleList;
    }

    /**
     * Main execution method
     */
    public void run() {
        Obstacle obstacle = null;
        PhysicalRunway runway = null;

        /**
         * Selection of obstacle (either one from the list or a custom one)
         */
        System.out.println("Type 1 to select an obstacle from a predefined list, and 2 to define a custom obstacle");
        obstacle = obstacleSelection();
        System.out.println("Obstacle name: " + obstacle.getName());
        System.out.println("Obstacle distance to centreline: " + obstacle.getDistToCentreline());
        System.out.println("Obstacle distance to threshold 1: " + obstacle.getDistToThreshold1());
        System.out.println("Obstacle distance to threshold 2: " + obstacle.getDistToThreshold2());

        /**
         * Selection of runway
         */
        for (int i = 0; i<runwayList.size(); i++) {
            System.out.println("Runway " + (i+1) + ": " + runwayList.get(i).getName());
        }
        runway = runwaySelection();
        System.out.println("Selected runway: " + runway.getName());

        try {
            runway.addObstacle(obstacle);
        } catch (DontNeedRedeclarationException | NegativeParameterException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to select a runway from the list
     * @return
     */
    private PhysicalRunway runwaySelection() {
        PhysicalRunway pr = null;

        HashMap<PhysicalRunway, String> runwayMap = new HashMap<PhysicalRunway, String>();
        for (PhysicalRunway p : runwayList) {
            runwayMap.put(p, p.getName());
        }

        String runwayName = scanner.nextLine();
        while (!runwayMap.containsValue(runwayName)) {
            System.out.println("Please select a runway from the list.");
            runwayName = scanner.next();
        }

        for (Map.Entry<PhysicalRunway, String> entry : runwayMap.entrySet()) {
            if (entry.getValue().equals(runwayName))
                pr = entry.getKey();
        }

        return pr;
    }


    /**
     * Method to decide whether the obstacle is a predefined one or will be a custom one
     * @return
     */
    private Obstacle obstacleSelection() {
        int choice = scanner.nextInt();
        while (true) {
            if (choice == 1) {
                return predObstacle();
            } else if (choice == 2) {
                return customObstacle();
            } else {
                System.err.println("Please input either 1 or 2");
                choice = scanner.nextInt();
            }
        }
    }

    /**
     * Method for selection of an obstacle from the list
     * @return
     */
    private Obstacle predObstacle() {

        Obstacle obstacle = null;


        HashMap<Obstacle, String> obstacleNames = new HashMap<Obstacle, String>();
        for (Obstacle o : obstacleList) {
            obstacleNames.put(o, o.getName());
        }

        for (Obstacle o : obstacleList ) {
            System.out.println("Name: " + o.getName());
            System.out.println("Height: " + o.getHeight());
        }


        System.out.println("Introduce obstacle name:");
        scanner.nextLine();

        /**
         * Some nonsense to ensure use input validation
         */
        String oName = scanner.nextLine();
        while (!obstacleNames.containsValue(oName)) {
            System.err.println("Please select an obstacle from the list.");
            oName = scanner.next();
        }

        for (Map.Entry<Obstacle, String> entry : obstacleNames.entrySet()) {
            if (entry.getValue().equals(oName))
                obstacle = entry.getKey();
        }

        System.out.println("Introduce obstacle distance to centreline:");
        int dtc = scannerValidation();
        System.out.println("Introduce obstacle distance to left threshold:");
        int dttL = scannerValidation();
        System.out.println("Introduce obstacle distance to right threshold:");
        int dttR = scannerValidation();


        obstacle.setDistToCentreline(dtc);
        obstacle.setDistToThreshold1(dttL);
        obstacle.setDistToThreshold2(dttR);

        return obstacle;
    }

    /**
     * Method to encapsulate the integer validation for each integer input prompt
     */
    private int scannerValidation() {
        while (!scanner.hasNextInt()) {
            System.err.println("Please input a valid integer.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    /**
     * Method to create a custom obstacle
     * @return
     */
    private Obstacle customObstacle() {
        System.out.println("Introduce obstacle name:");
        String name = scanner.nextLine();
        scanner.next();
        System.out.println("Introduce obstacle height:");
        int height = scannerValidation();
        System.out.println("Introduce obstacle distance to left threshold:");
        int dttL = scannerValidation();
        System.out.println("Introduce obstacle distance to right threshold:");
        int dttR = scannerValidation();
        System.out.println("Introduce obstacle distance to centreline:");
        int dtc = scannerValidation();

        return new Obstacle(height, name, dttL, dttR, dtc);
    }
}
