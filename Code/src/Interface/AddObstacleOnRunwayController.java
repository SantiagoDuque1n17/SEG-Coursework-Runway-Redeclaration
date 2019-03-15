package Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddObstacleOnRunwayController
{
    @FXML
    private Button addRunwayButton;

    @FXML
    private TextField distToCenter;
    @FXML
    private TextField distToLeftT;
    @FXML
    private TextField distToRightT;

    public void handleCloseButtonAction(ActionEvent event)
    {
        if(!distToCenter.getText().trim().isEmpty() && !distToLeftT.getText().trim().isEmpty() && !distToRightT.getText().trim().isEmpty())
        {

            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }
        else
        {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setContentText("There are spaces left empty. Please try again.");
            warningAlert.showAndWait();
        }
    }


    @FXML
    public void initialize()
    {
        addRunwayButton.setOnAction(this::handleCloseButtonAction);
    }
}
