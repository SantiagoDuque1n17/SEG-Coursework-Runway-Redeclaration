public class Calculations {

    public void landingOverObstacle(Runway r, Obstacle o, int distanceToThreshold) {

        //TODO: Proper calculations

        int currentLDA = r.getLDA();
        int newLDA = currentLDA - distanceToThreshold - (50*o.getHeight()) - 60;

        if (newLDA <= 0) {
            //TODO: Create new exception
        }
        r.setLDA(newLDA);
    }

    public void takeoffAwayObstacle(Runway r, Obstacle o, int distanceToThreshold) {
        int currentTORA = r.getTORA;
        int limitation = 300 + distanceToThreshold
        int newTORA = currentTORA - limitation;
        int

        if (newTORA <=0) {
            //TODO: exceptions
        }
        r.setTORA(newTORA);
        r.setTODA(r.getTODA-limitation);
        r.setASDA(r.getASDA-limitation);
        //TODO: add better calculations
    }
}
