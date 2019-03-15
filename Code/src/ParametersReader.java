import Data.Obstacle;
import Interface.PhysicalRunway;
import Data.Runway;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import Data.*;
import Interface.*;

public class ParametersReader {

    public List<Obstacle> getObstaclesFromFile(File file) throws FileNotFoundException {
        List<Obstacle> obstacleList = new ArrayList<Obstacle>();

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                
                Obstacle obstacle = readObstacle(scanner.nextLine());

                for (Obstacle o : obstacleList) {
                    if (obstacle.getName().equals(o.getName())) {
                        obstacle.setName(obstacle.getName()+"'");
                    }
                }

                obstacleList.add(obstacle);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return obstacleList;
    }

    public List<PhysicalRunway> getRunwaysFromFile(File file) throws FileNotFoundException {
        List<PhysicalRunway> runwayList = new ArrayList<PhysicalRunway>();

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                runwayList.add(readRunway(scanner.nextLine()));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return runwayList;
    }

    private Obstacle readObstacle(String input) {
        String[] parameters = input.split(":");
        Obstacle obstacle = new Obstacle(parameters[0],Integer.parseInt(parameters[1]),
                Integer.parseInt(parameters[2]),Integer.parseInt(parameters[3]),Integer.parseInt(parameters[4]));

        return obstacle;
    }

    private PhysicalRunway readRunway(String input) {
        String[] parametersByRunway = input.split("::");

        String[] parameters1 = parametersByRunway[0].split(":");
        String[] parameters2 = parametersByRunway[1].split(":");

        Runway r1 = new Runway(parameters1[0], Integer.parseInt(parameters1[1]),
                Integer.parseInt(parameters1[2]),Integer.parseInt(parameters1[3]), Integer.parseInt(parameters1[4]), Integer.parseInt(parameters1[5]));

        Runway r2 = new Runway(parameters2[0], Integer.parseInt(parameters2[1]),
                Integer.parseInt(parameters2[2]),Integer.parseInt(parameters2[3]), Integer.parseInt(parameters2[4]), Integer.parseInt(parameters2[5]));

        PhysicalRunway runway = new PhysicalRunway(r1, r2);

        return runway;
    }
}
