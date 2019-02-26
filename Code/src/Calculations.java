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

}
