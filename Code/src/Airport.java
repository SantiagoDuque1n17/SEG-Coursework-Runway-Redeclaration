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
    //TODO: Possibly a list of Runways as variable as well?
}
