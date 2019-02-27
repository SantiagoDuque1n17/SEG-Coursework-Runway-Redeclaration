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
}
