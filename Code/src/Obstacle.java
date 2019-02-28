public class Obstacle {
    private int height;
    private String name;
    private int distToThreshold1, distToThreshold2, distToCentreline;

    public Obstacle(int height, String name, int distToThreshold1, int distToThreshold2, int distToCentreline) {
        this.height = height;
        this.name = name;
        this.distToThreshold1 = distToThreshold1;
        this.distToThreshold2 = distToThreshold2;
        this.distToCentreline = distToCentreline;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public int getDistToThreshold1() {
        return distToThreshold1;
    }

    public int getDistToThreshold2() {
        return distToThreshold2;
    }

    public int getDistToCentreline() {
        return distToCentreline;
    }
}
