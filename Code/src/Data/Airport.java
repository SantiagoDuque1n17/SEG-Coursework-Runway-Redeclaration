import java.util.ArrayList;
import java.util.List;

public class Airport {
    private String name;
    private String id;
    private List<PhysicalRunway> runways;

    public Airport(String name, String id) {
        this.name = name;
        this.id = id;
        runways = new ArrayList<>();
    }

    public void addRunway(PhysicalRunway runway) {
        runways.add(runway);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PhysicalRunway> getRunways() {
        return runways;
    }

    public void setRunways(List<PhysicalRunway> runways) {
        this.runways = runways;
    }
}
