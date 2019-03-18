package Controller;
import Interface.*;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SeeBreakdownController {

    @FXML
    private Label breakdownLabel = new Label();

    public void initialize() {

    }

    @FXML
    public void setBreakdownLabel(String breakdown) {
        breakdownLabel.setText(breakdown);
    }
}
