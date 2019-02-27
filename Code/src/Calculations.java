import RunwayRedeclaration.Exceptions;

public class Calculations {
    private static final int blastProtection = 300;
    private static final int RESA = 240;
    private static final int stripEnd = 60;
    private static final int sideSpace = 75;

    private boolean isRedeclarationNeeded(Obstacle o) {
        if (o.getDistToThreshold() > stripEnd || o.getDistToCentreline() > sideSpace) {
            return false;
        }
        return true;
    }

    public void landingOverObstacle(Runway r, Obstacle o) throws NegativeParameterException, DontNeedRedeclarationException {

        if (isRedeclarationNeeded(o)) {
            throw new DontNeedRedeclarationException();
        }

        int originalLDA = r.getOriginalLDA();
        int slopeCalc = RESA;
        if ((50*o.getHeight())>RESA)
            slopeCalc = 50*o.getHeight();

        int newLDA = originalLDA - o.getDistToThreshold() - slopeCalc - stripEnd;

        if (newLDA <= 0) {
            throw new NegativeParameterException();
        }
        r.setLDA(newLDA);
    }

    public void takeOffAwayObstacle(Runway r, Obstacle o) throws NegativeParameterException, DontNeedRedeclarationException {

        if (isRedeclarationNeeded(o)) {
            throw new DontNeedRedeclarationException();
        }

        int originalTORA = r.getOriginalTORA();
        int displacedThreshold = r.getDisplacedThreshold();
        int clearway = r.getClearway();
        int stopway = r.getStopway();
        int newTORA = originalTORA - blastProtection - o.getDistToThreshold() - displacedThreshold;
        int newTODA = newTORA + clearway;
        int newASDA = newTORA + stopway;

        if (newTORA <=0) {
            throw new NegativeParameterException();
        }
        r.setTORA(newTORA);
        r.setTODA(newTODA);
        r.setASDA(newASDA);
        //TODO: add better calculations
    }

    public void landingTowardsObstacle(Runway r, Obstacle o) throws DontNeedRedeclarationException {

        if (isRedeclarationNeeded(o)) {
            throw new DontNeedRedeclarationException();
        }

        int newLDA = o.getDistToThreshold() - RESA - stripEnd;
        if (r.getOriginalLDA()>newLDA)
            r.setLDA(newLDA);
    }

    public void takeOffTowardsObstacle(Runway r, Obstacle o) throws DontNeedRedeclarationException {

        if (isRedeclarationNeeded(o)) {
            throw new DontNeedRedeclarationException();
        }

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

        newTORA = o.getDistToThreshold() + displacedThreshold - tempThreshold - 60;
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
