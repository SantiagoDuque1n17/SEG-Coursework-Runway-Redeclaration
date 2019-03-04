import Exceptions.DontNeedRedeclarationException;
import Exceptions.NegativeParameterException;

public class Runway {

    //TODO: Not sure what else to put in here

    private final int originalLDA; //Landing distance available
    private final int originalTORA; // Take-Off Run Available
    private final int originalTODA; // Take-Off Distance Available
    private final int originalASDA; // Accelerate-Stop Distance Available
    private final int stopway;
    private final int clearway;
    private final int displacedThreshold;
    private int LDA, TORA, TODA, ASDA;
    private String ID;

    private static final int blastProtection = 300;
    private static final int RESA = 240;
    private static final int stripEnd = 60;

    public Runway(String ID, int LDA, int TORA, int TODA, int ASDA, int displacedThreshold) {
        this.ID = ID;
        this.originalLDA = LDA;
        this.LDA = LDA;
        this.originalTORA = TORA;
        this.TORA = TORA;
        this.originalTODA = TODA;
        this.TODA = TODA;
        this.originalASDA = ASDA;
        this.ASDA = ASDA;
        this.stopway = ASDA - TORA;
        this.clearway = TODA - TORA;
        this.displacedThreshold = displacedThreshold;
    }

    public int getOriginalLDA() {
        return originalLDA;
    }

    public int getOriginalTORA() {
        return originalTORA;
    }

    public int getOriginalTODA() {
        return originalTODA;
    }

    public int getOriginalASDA() {
        return originalASDA;
    }

    public int getLDA() {
        return LDA;
    }

    public int getTORA() {
        return TORA;
    }

    public int getTODA() {
        return TODA;
    }

    public int getASDA() {
        return ASDA;
    }

    public int getClearway() {
        return clearway;
    }

    public int getStopway() {
        return stopway;
    }

    public int getDisplacedThreshold() {
        return displacedThreshold;
    }

    public void setLDA(int LDA) {
        this.LDA = LDA;
    }

    public void setTORA(int TORA) {
        this.TORA = TORA;
    }

    public void setTODA(int TODA) {
        this.TODA = TODA;
    }

    public void setASDA(int ASDA) {
        this.ASDA = ASDA;
    }

    public String getID() {
        return ID;
    }

    public void landingOverObstacle(int dtt, int height) throws NegativeParameterException {
        System.out.println("***************************************************************");
        System.out.println("Calculating landing over the obstacle for runway "+this.getID());
        System.out.println("***************************************************************");

        int originalLDA = this.getOriginalLDA();
        int slopeCalc = RESA;
        if ((50*height)>RESA)
            slopeCalc = 50*height;

        int newLDA = originalLDA - dtt - slopeCalc - stripEnd;

        if (newLDA <= 0) {
            throw new NegativeParameterException();
        }
        System.out.println("LDA: Original value: "+originalLDA+", Previous value: "+this.getLDA()+", New value: "+newLDA);
        this.setLDA(newLDA);
    }

    void takeOffAwayObstacle(int dtt) throws NegativeParameterException {
        System.out.println("***************************************************************");
        System.out.println("Calculating take-off away from the obstacle for runway "+this.getID());
        System.out.println("***************************************************************");

        int originalTORA = this.getOriginalTORA();
        int displacedThreshold = this.getDisplacedThreshold();
        int clearway = this.getClearway();
        int stopway = this.getStopway();
        int newTORA = originalTORA - blastProtection - dtt - displacedThreshold;
        int newTODA = newTORA + clearway;
        int newASDA = newTORA + stopway;

        if (newTORA <=0) {
            throw new NegativeParameterException();
        }
        System.out.println("TORA: Original value: "+originalTORA+", Previous value: "+this.getTORA()+", New value: "+newTORA);
        this.setTORA(newTORA);
        System.out.println("TODA: Original value: "+this.getOriginalTODA()+", Previous value: "+this.getTODA()+", New value: "+newTODA);
        this.setTODA(newTODA);
        System.out.println("ASDA: Original value: "+this.getOriginalASDA()+", Previous value: "+this.getASDA()+", New value: "+newASDA);
        this.setASDA(newASDA);
    }

    void landingTowardsObstacle(int dtt) {
        System.out.println("***************************************************************");
        System.out.println("Calculating landing towards the obstacle for runway "+this.getID());
        System.out.println("***************************************************************");
        int newLDA = dtt - RESA - stripEnd;
        System.out.println("LDA: Original value: "+this.getOriginalLDA()+", Previous value: "+this.getLDA()+", New value: "+newLDA);
        this.setLDA(newLDA);
    }

    void takeOffTowardsObstacle(int dtt, int height) throws NegativeParameterException {
        System.out.println("***************************************************************");
        System.out.println("Calculating take-off towards the obstacle for runway "+this.getID());
        System.out.println("***************************************************************");
        int displacedThreshold = this.getDisplacedThreshold();

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
        System.out.println("TORA: Original value: "+this.getOriginalTORA()+", Previous value: "+this.getTORA()+", New value: "+newTORA);
        this.setTORA(newTORA);
        System.out.println("TODA: Original value: "+this.getOriginalTODA()+", Previous value: "+this.getTODA()+", New value: "+newTODA);
        this.setTODA(newTODA);
        System.out.println("ASDA: Original value: "+this.getOriginalASDA()+", Previous value: "+this.getASDA()+", New value: "+newASDA);
        this.setASDA(newASDA);

    }
}
