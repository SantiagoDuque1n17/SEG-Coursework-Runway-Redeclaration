public class Obstacle {
    private int height;
    private String name;
    private int distToThreshold, distToCentreline;

    public Obstacle(int height, String name, int distToThreshold, int distToCentreline) {
        this.height = height;
        this.name = name;
        this.distToThreshold = distToThreshold;
        this.distToCentreline = distToCentreline;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public int getDistToThreshold() {
        return distToThreshold;
    }

    public int getDistToCentreline() {
        return distToCentreline;
    }
}
