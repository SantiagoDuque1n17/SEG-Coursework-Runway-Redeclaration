import RunwayRedeclaration.Exceptions.NegativeParameterException;

public class Calculations {
    private static final int blastProtection = 300;

    public void landingOverObstacle(Runway r, Obstacle o, int distanceToThreshold) throws NegativeParameterException {

        //TODO: Refine calculations

        int currentLDA = r.getLDA();
        int newLDA = currentLDA - distanceToThreshold - (50*o.getHeight()) - 60;

        if (newLDA <= 0) {
            throw new NegativeParameterException("Obstacle dimensions too great, can't redeclare, can't use runway.");
        }
        r.setLDA(newLDA);
    }

    public void takeOffAwayObstacle(Runway r, Obstacle o, int distanceToThreshold) {
        int currentTORA = r.getTORA();
        int displacedThreshold = r.getDisplacedThreshold();
        int clearway = r.getClearway();
        int stopway = r.getStopway();
        int newTORA = currentTORA - blastProtection - distanceToThreshold - displacedThreshold;
        int newTODA = newTORA + clearway;
        int newASDA = newTORA + stopway;

        if (newTORA <=0) {
            //TODO: exceptions
        }
        r.setTORA(newTORA);
        r.setTODA(newTODA);
        r.setASDA(newASDA);
        //TODO: add better calculations
    }

    public void takeOffTowardsObstacle(Runway r, Obstacle o, int distanceToThreshold)
    {
        int currentTORA = r.getTORA();
        int currentTODA = r.getTODA();
        int currentASDA = r.getASDA();
        int displacedThreshold = r.getDisplacedThreshold();
        int clearway = r.getClearway();
        int stopway = r.getStopway();

        int newTORA;
        int newTODA;
        int newASDA;

        newTORA = distanceToThreshold + displacedThreshold - o.getHeight()*50 - 60;
        newTODA = newTORA + clearway;
        newASDA = newTORA + stopway;

        if(newTORA<=0)
        {

        }

        r.setTORA(newTORA);
        r.setTODA(newTODA);
        r.setASDA(newASDA);
    }
}
