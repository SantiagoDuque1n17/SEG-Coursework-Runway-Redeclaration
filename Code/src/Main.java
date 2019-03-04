import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        List <PhysicalRunway> runwayList = new ArrayList<PhysicalRunway>();
        runwayList.add( new PhysicalRunway(
                            new Runway("27R", 3884, 3884, 3962, 3884, 0),
                            new Runway("09L", 3595, 3902, 3902, 3902, 306)
                      )
        );
        runwayList.add( new PhysicalRunway(
                        new Runway("36R", 3884, 3884, 3962, 3884, 0),
                        new Runway("18L", 3684, 3684, 3952, 3884, 0)
                )
        );
        runwayList.add( new PhysicalRunway(
                        new Runway("27L", 3844, 3844, 3862, 3284, 0),
                        new Runway("09R", 3684, 3684, 3952, 3884, 0)
                )
        );


        List<Obstacle> obstacleList = new ArrayList<Obstacle>();
        obstacleList.add(new Obstacle(12, "Bus", 3646,-50,0));
        obstacleList.add(new Obstacle(20, "Crashed plane", 0,0,0));
        obstacleList.add(new Obstacle(10, "Debris", 0,0,0));

        Interface i = new Interface(runwayList, obstacleList);
        i.run();
    }
}

