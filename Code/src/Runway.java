public class Runway {
    
    //TODO: Not sure what else to put in here
    
    private int LDA; //Landing distance available
    private int TORA; // Take-Off Run Available
    private int TODA; // Take-Off Distance Available
    private int ASDA; // Landing distance available 

    public Runway(int LDA, int TORA, int TODA, int ASDA) {
        this.LDA = LDA;
        this.TORA = TORA;
        this.TODA = TODA;
        this.ASDA = ASDA;
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
}
