import RunwayRedeclaration.Exceptions.NegativeParameterException;

public class Calculations {
    private static final int blastProtection = 300;
    private static final int RESA = 240;
    private static final int stripEnd = 60;

    public void landingOverObstacle(Runway r, Obstacle o, int distanceToThreshold) throws NegativeParameterException {

        //TODO: Refine calculations

        int originalLDA = r.getOriginalLDA();
        int slopeCalc = RESA;
        if ((50*o.getHeight())>RESA)
            slopeCalc = 50*o.getHeight();
        int newLDA = originalLDA - distanceToThreshold - slopeCalc - stripEnd;

        if (newLDA <= 0) {
            throw new NegativeParameterException("Obstacle dimensions too great, can't redeclare, can't use runway.");
        }
        r.setLDA(newLDA);
    }

    public void takeOffAwayObstacle(Runway r, int distanceToThreshold) {
        int originalTORA = r.getOriginalTORA();
        int displacedThreshold = r.getDisplacedThreshold();
        int clearway = r.getClearway();
        int stopway = r.getStopway();
        int newTORA = originalTORA - blastProtection - distanceToThreshold - displacedThreshold;
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

    public void landingTowardsObstacle(Runway r, int distanceToThreshold) {
        int newLDA = distanceToThreshold - RESA - stripEnd;
        if (r.getOriginalLDA()>newLDA)
            r.setLDA(newLDA);
    }

    public void takeOffTowardsObstacle(Runway r, Obstacle o, int distanceToThreshold)
    {
        int currentTORA = r.getTORA();
        int currentTODA = r.getTODA();
        int currentASDA = r.getASDA();
        int displacedThreshold = r.getDisplacedThreshold();

        int newTORA;
        int newTODA;
        int newASDA;

        int tempThreshold = RESA;
        if ((50*o.getHeight())>RESA)
            tempThreshold = 50*o.getHeight();

        newTORA = distanceToThreshold + displacedThreshold - tempThreshold - 60;
        newTODA = newTORA;
        newASDA = newTORA;

        if(newTORA<=0)
        {

        }

        r.setTORA(newTORA);
        r.setTODA(newTODA);
        r.setASDA(newASDA);
    }
}
