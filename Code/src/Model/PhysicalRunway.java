package Model;
import Model.*;
import Exceptions.*;


public class PhysicalRunway {

    private Runway runway1;
    private Runway runway2;
    private String name;
    private Obstacle obstacle = null;

    private static final int stripEnd = 60;
    private static final int sideSpace = 75;

    /**
     * Constructor
     *
     * A physical runway can be used in both directions,
     * creating two logical runways with a difference of 18 points
     * (180 degrees)
     *
     * @param runway1  first logical runway - clock-wise
     * @param runway2  second logical runway
     */
    public PhysicalRunway(Runway runway1, Runway runway2) {
        this.runway1 = runway1;
        this.runway2 = runway2;
        name = runway1.getID()+"/"+runway2.getID();
    }

    /**
     *
     * Adds an obstacle to the physical runway and depending on the
     * distances to thresholds performs landing and takeoff in
     * the right direction
     *
     * @param o obstacle added to the runway
     * @throws DontNeedRedeclarationException
     * @throws NegativeParameterException
     */
    public int addObstacle(Obstacle o) throws DontNeedRedeclarationException, NegativeParameterException {

        int dtt1 = o.getDistToThreshold1();
        int dtt2 = o.getDistToThreshold2();
        if (o.getDistToCentreline()>sideSpace||dtt1<-stripEnd||dtt2<-stripEnd) throw new DontNeedRedeclarationException();
        int h = o.getHeight();
        obstacle = o;
        if (dtt1>dtt2) {
            runway1.takeOffTowardsObstacle(dtt1, h);
            runway1.landingTowardsObstacle(dtt1);
            runway2.takeOffAwayObstacle(dtt2);
            runway2.landingOverObstacle(dtt2, h);
            return 1;
        } else {
            runway2.takeOffTowardsObstacle(dtt2, h);
            runway2.landingTowardsObstacle(dtt2);
            runway1.takeOffAwayObstacle(dtt1);
            runway1.landingOverObstacle(dtt1, h);
            return 2;
        }
    }

    public String getName()
    {
        return name;
    }

    public void setObstacle(Obstacle obstacle){
        this.obstacle = obstacle;

    }
    public Obstacle getObstacle(){
        return obstacle;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public Runway getRunway1() {
        return runway1;
    }

    public Runway getRunway2() {
        return runway2;
    }

    public void switchRunways() {
        Runway runway = runway1;
        runway1 = runway2;
        runway2 = runway;
    }


}
