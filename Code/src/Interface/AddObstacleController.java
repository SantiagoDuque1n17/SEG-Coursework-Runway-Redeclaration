package Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddObstacleController
{
    @FXML
    private Button addButton;

    @FXML
    private TextField obsName;
    @FXML
    private TextField obsHeight;

    public void handleCloseButtonAction(ActionEvent event)
    {
        if(!obsName.getText().trim().isEmpty() && !obsHeight.getText().trim().isEmpty())
        {

            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }
        else
        {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setContentText("No name/height was introduced. Please try again.");
            warningAlert.showAndWait();
        }
    }


    @FXML
    public void initialize()
    {
        addButton.setOnAction(this::handleCloseButtonAction);
    }
}
