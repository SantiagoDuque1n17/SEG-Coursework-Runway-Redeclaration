public class Obstacle {
    private int height;
    private int width;
    private int length;
    private String name;

    public Obstacle(int height, int width, int length, String name) {
        this.height = height;
        this.width = width;
        this.length = length;
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public String getName() {
        return name;
    }
}
