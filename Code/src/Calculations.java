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
        int limitation = 300 + distanceToThreshold;
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

    public void takeOffTowardsObstacle(Runway r, Obstacle o, int distanceToThreshold)
    {
        int currentTORA = r.getTORA();
        int currentTODA = r.getTODA();
        int currentASDA = r.getASDA();
        int displacedThreshold = r.getDisplacedThreshold();
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
