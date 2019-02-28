import Exceptions.*;

public class PhysicalRunway {

    private Runway runway1;
    private Runway runway2;
    private String name;

    private static final int blastProtection = 300;
    private static final int RESA = 240;
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
            takeOffTowardsObstacle(runway1, dtt1, h);
            landingTowardsObstacle(runway1, dtt1);
            takeOffAwayObstacle(runway2, dtt2);
            landingOverObstacle(runway2, dtt2, h);
        } else {
            takeOffTowardsObstacle(runway2, dtt2, h);
            landingTowardsObstacle(runway2, dtt2);
            takeOffAwayObstacle(runway1, dtt1);
            landingOverObstacle(runway1, dtt1, h);
        }
    }

    private void landingOverObstacle(Runway r, int dtt, int height) throws NegativeParameterException {
        System.out.println("Calculating landing over the obstacle for runway "+r.getID());
        int originalLDA = r.getOriginalLDA();
        int slopeCalc = RESA;
        if ((50*height)>RESA)
            slopeCalc = 50*height;

        int newLDA = originalLDA - dtt - slopeCalc - stripEnd;

        if (newLDA <= 0) {
            throw new NegativeParameterException();
        }
        System.out.println("LDA: Original value: "+originalLDA+", Previous value: "+r.getLDA()+", New value: "+newLDA);
        r.setLDA(newLDA);
    }

    private void takeOffAwayObstacle(Runway r, int dtt) throws NegativeParameterException {
        System.out.println("Calculating take-off away from the obstacle for runway "+r.getID());
        int originalTORA = r.getOriginalTORA();
        int displacedThreshold = r.getDisplacedThreshold();
        int clearway = r.getClearway();
        int stopway = r.getStopway();
        int newTORA = originalTORA - blastProtection - dtt - displacedThreshold;
        int newTODA = newTORA + clearway;
        int newASDA = newTORA + stopway;

        if (newTORA <=0) {
            throw new NegativeParameterException();
        }
        System.out.println("TORA: Original value: "+originalTORA+", Previous value: "+r.getTORA()+", New value: "+newTORA);
        r.setTORA(newTORA);
        System.out.println("TODA: Original value: "+r.getOriginalTODA()+", Previous value: "+r.getTODA()+", New value: "+newTODA);
        r.setTODA(newTODA);
        System.out.println("ASDA: Original value: "+r.getOriginalASDA()+", Previous value: "+r.getASDA()+", New value: "+newASDA);
        r.setASDA(newASDA);
    }

    private void landingTowardsObstacle(Runway r, int dtt) throws DontNeedRedeclarationException {
        System.out.println("Calculating landing towards the obstacle for runway "+r.getID());
        int newLDA = dtt - RESA - stripEnd;
        if (r.getOriginalLDA()>newLDA) {
            System.out.println("LDA: Original value: "+r.getOriginalLDA()+", Previous value: "+r.getLDA()+", New value: "+newLDA);
            r.setLDA(newLDA);
        }
        throw new DontNeedRedeclarationException();
    }

    private void takeOffTowardsObstacle(Runway r, int dtt, int height) throws NegativeParameterException {
        System.out.println("Calculating take-off towards the obstacle for runway "+r.getID());
        int displacedThreshold = r.getDisplacedThreshold();

        int newTORA;
        int newTODA;
        int newASDA;

        int slopeCalc = RESA;
        if ((50*height)>RESA)
            slopeCalc = 50*height;

        newTORA = dtt + displacedThreshold - slopeCalc - 60;
        newTODA = newTORA;
        newASDA = newTORA;

        if(newTORA<=0)
        {
            throw new NegativeParameterException();
        }
        System.out.println("TORA: Original value: "+r.getOriginalTORA()+", Previous value: "+r.getTORA()+", New value: "+newTORA);
        r.setTORA(newTORA);
        System.out.println("TODA: Original value: "+r.getOriginalTODA()+", Previous value: "+r.getTODA()+", New value: "+newTODA);
        r.setTODA(newTODA);
        System.out.println("ASDA: Original value: "+r.getOriginalASDA()+", Previous value: "+r.getASDA()+", New value: "+newASDA);
        r.setASDA(newASDA);

    }
}
