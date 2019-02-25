public class Calculations {

    private void landingOverObstacle(Runway r, Obstacle o, int distanceToThreshold) {

        //TODO: Proper calculations

        int currentLDA = r.getLDA();
        int newLDA = currentLDA - distanceToThreshold - (50*o.getHeight()) - 60;
        r.setLDA(newLDA);
    }

}
