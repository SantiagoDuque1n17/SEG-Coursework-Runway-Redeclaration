package Model;
import Exceptions.NegativeParameterException;

public class Runway {

    private final int originalLDA; //Landing distance available
    private final int originalTORA; // Take-Off Run Available
    private final int originalTODA; // Take-Off Distance Available
    private final int originalASDA; // Accelerate-Stop Distance Available
    private final int stopway;
    private final int clearway;
    private final int displacedThreshold;
    private int LDA, TORA, TODA, ASDA;
    private int slopeCalc;
    private String ID;
    private String status = "NORMAL";

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

    /**
     * Getters and setters
     *
     */
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

    public int getSlopeCalc() { return slopeCalc; }

    public String getID() {
        return ID;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public int setSlopeCalc(int height) {
        int slopeCalc = RESA;
        if ((50*height)>RESA)
            slopeCalc = 50*height;
        this.slopeCalc = slopeCalc;
        return slopeCalc;
    }

    /**
     * Simulates landing over an obstacle. A new LDA is calculated
     * and depending on its value a commercial decision is made
     * whether the runway should close or restricted operations
     * should be enabled
     *
     * @param dtt distance from threshold
     * @param height height of obstacle
     * @throws NegativeParameterException
     */
    public void landingOverObstacle(int dtt, int height) throws NegativeParameterException {
        System.out.println("\n");
        System.out.println("***************************************************************");
        System.out.println("Calculating landing over the obstacle for runway "+this.getID());
        System.out.println("***************************************************************");

        int originalLDA = this.getOriginalLDA();
        int slopeCalc = setSlopeCalc(height);

        int newLDA = originalLDA - dtt - slopeCalc - stripEnd;

        if (newLDA <= 0) {
            throw new NegativeParameterException();
        }
        System.out.println("LDA: Original value: "+originalLDA+", Previous value: "+this.getLDA()+", New value: "+newLDA);
        System.out.println
                ("Formula: \n New LDA = Original LDA (" + originalLDA + ") " +
                        "- Distance from threshold (" + dtt + ") - Strip end (" + stripEnd +") - Slope calculation (50*" + height + " = " + slopeCalc + ")." +
                        "\n_________________________________________________________________" );

        this.setLDA(newLDA);

        System.out.println("The threshold value of the runway: " + displacedThreshold);

        if(newLDA<=1600)
        {
            status = "CLOSED";
        }
        else
        {
            status = "RESTRICTED OPERATIONS";
        }

        System.out.println("Runway status: " + status);
    }

    /**
     * Simulates taking off away from an obstacle. New values
     * for TORA, TODA and ASDA are  calculated
     * and depending on those value a commercial decision is made
     * whether the runway should close or restricted operations
     * should be enabled
     *
     * @param dtt distance from threshold
     *
     * @throws NegativeParameterException
     */
    public void takeOffAwayObstacle(int dtt) throws NegativeParameterException {
        System.out.println("\n");
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
        System.out.println("Formula:\n New TORA = Original TORA (" +
                        originalTORA + ") - Blast Protection (" + blastProtection + ") - Distance to threshold (" + dtt + ") - Displaced threshold (" + displacedThreshold + ") " +
                            "\n__________________________________________________________________________");
        System.out.println("TODA: Original value: "+this.getOriginalTODA()+", Previous value: "+this.getTODA()+", New value: "+newTODA);
        this.setTODA(newTODA);
        System.out.println("Formula:\n New TODA = New TORA (" + newTORA + ") + Clearway (" + clearway + ")." +
                            "\n__________________________________________________________________________");
        System.out.println("ASDA: Original value: "+this.getOriginalASDA()+", Previous value: "+this.getASDA()+", New value: "+newASDA);
        this.setASDA(newASDA);
        System.out.println("Formula:\n New ASDA = New TORA (" + newTORA + ") + Stopway (" + stopway + ")." +
                            "\n__________________________________________________________________________");

        System.out.println("The threshold value of the runway: " + displacedThreshold);

        if(newTORA<=1900)
        {
            status = "CLOSED";
        }
        else
        {
            status = "RESTRICTED OPERATIONS";
        }

        System.out.println("Runway status: " + status);
    }

    /**
     * Simulates landing towards an obstacle. A new LDA is calculated
     * and depending on its value a commercial decision is made
     * whether the runway should close or restricted operations
     * should be enabled
     *
     * @param dtt distance from threshold
     */
    public void landingTowardsObstacle(int dtt) throws NegativeParameterException
    {
        System.out.println("\n");
        System.out.println("***************************************************************");
        System.out.println("Calculating landing towards the obstacle for runway "+this.getID());
        System.out.println("***************************************************************");
        int newLDA = dtt - RESA - stripEnd;

        if(newLDA <= 0)
        {
            throw new NegativeParameterException();
        }

        System.out.println("LDA: Original value: "+  this.getOriginalLDA() +", Previous value: "+this.getLDA()+", New value: "+newLDA);
        this.setLDA(newLDA);
        System.out.println("Formula:\n New LDA = Distance to threshold (" + dtt + ") - RESA (" + RESA + ") - strip end (" + stripEnd + ").");
        System.out.println("__________________________________________________________________________");

        System.out.println("The threshold value of the runway: " + displacedThreshold);

        if(newLDA<=1600)
        {
            status = "CLOSED";
        }
        else
        {
            status = "RESTRICTED OPERATIONS";
        }

        System.out.println("Runway status: " + status);
    }

    /**
     * Simulates taking off towards an obstacle. New values
     * for TORA, TODA and ASDA are  calculated
     * and depending on those value a commercial decision is made
     * whether the runway should close or restricted operations
     * should be enabled
     *
     * @param dtt distance from threshold
     * @param height height of obstacle
     *
     * @throws NegativeParameterException
     */
    public void takeOffTowardsObstacle(int dtt, int height) throws NegativeParameterException {
        System.out.println("\n***************************************************************");
        System.out.println("Calculating take-off towards the obstacle for runway "+this.getID());
        System.out.println("***************************************************************");
        int displacedThreshold = this.getDisplacedThreshold();

        int newTORA;
        int newTODA;
        int newASDA;

        int slopeCalc = setSlopeCalc(height);

        newTORA = dtt + displacedThreshold - slopeCalc - stripEnd;
        newTODA = newTORA;
        newASDA = newTORA;

        if(newTORA<=0)
        {
            throw new NegativeParameterException();
        }
        System.out.println("TORA: Original value: "+this.getOriginalTORA()+", Previous value: "+this.getTORA()+", New value: "+newTORA);
        this.setTORA(newTORA);
        System.out.println
                ("Formula:\n New TORA = Original TORA (" +
                        getOriginalTORA() + ") + Displaced threshold ("
                        + displacedThreshold + ")  - Slope calculation (50*" + height + " = " + slopeCalc + ") - Visual Strip End (" + stripEnd + ")." +
                        "\n _________________________________________________________________");
        System.out.println("TODA: Original value: "+this.getOriginalTODA()+", Previous value: "+this.getTODA()+", New value: "+newTODA);
        this.setTODA(newTODA);
        System.out.println("Formula:\n New TODA = New TORA (" + newTORA + ").\n _________________________________________________________________");
        System.out.println("ASDA: Original value: "+this.getOriginalASDA()+", Previous value: "+this.getASDA()+", New value: "+newASDA);
        this.setASDA(newASDA);
        System.out.println("Formula:\n New ASDA = New TORA (" + newTORA + "). \n _________________________________________________________________");

        System.out.println("The threshold value of the runway: " + displacedThreshold);

        if(newTORA<=1900)
        {
            status = "CLOSED";
        }
        else
        {
            status = "RESTRICTED OPERATIONS";
        }

        System.out.println("Runway status: " + status);
    }

    public static int getBlastProtection() {
        return blastProtection;
    }

    public static int getRESA() {
        return RESA;
    }

    public static int getStripEnd() {
        return stripEnd;
    }
}

