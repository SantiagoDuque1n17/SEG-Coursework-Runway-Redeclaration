import java.io.File;
import java.io.FileNotFoundException;
import Exceptions.*;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        List <PhysicalRunway> runwayList = new ArrayList<PhysicalRunway>();
        List<Obstacle> obstacleList = new ArrayList<Obstacle>();

        ParametersReader pr = new ParametersReader();

        try {
            obstacleList = pr.getObstaclesFromFile(new File(
                    "/* FILE PATH IN HERE */"));
            runwayList = pr.getRunwaysFromFile(new File(
                    "/* FILE PATH IN HERE */"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Interface i = new Interface(runwayList, obstacleList);
        i.run();
    }
}

