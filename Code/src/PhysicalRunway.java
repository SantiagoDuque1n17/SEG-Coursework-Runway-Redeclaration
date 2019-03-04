import Exceptions.*;

public class PhysicalRunway {

    private Runway runway1;
    private Runway runway2;
    private String name;

    private static final int stripEnd = 60;
    private static final int sideSpace = 75;

    public PhysicalRunway(Runway runway1, Runway runway2) {
        this.runway1 = runway1;
        this.runway2 = runway2;
        name = runway1.getID()+"/"+runway2.getID();
    }

    public void addObstacle(Obstacle o) throws DontNeedRedeclarationException, NegativeParameterException {

        int dtt1 = o.getDistToThreshold1();
        int dtt2 = o.getDistToThreshold2();
        if (o.getDistToCentreline()>sideSpace||dtt1<-stripEnd||dtt2<-stripEnd) throw new DontNeedRedeclarationException();
        int h = o.getHeight();
        if (dtt1>dtt2) {
            runway1.takeOffTowardsObstacle(dtt1, h);
            runway1.landingTowardsObstacle(dtt1);
            runway2.takeOffAwayObstacle(dtt2);
            runway2.landingOverObstacle(dtt2, h);
        } else {
            runway2.takeOffTowardsObstacle(dtt2, h);
            runway2.landingTowardsObstacle(dtt2);
            runway1.takeOffAwayObstacle(dtt1);
            runway1.landingOverObstacle(dtt1, h);
        }
    }
    
    public String getName()
    {
        return name;
    }
}
