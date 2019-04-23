package Controller;
import Interface.*;
import Model.Obstacle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddObstacleController
{
    Obstacle obstacle;
    InterfaceController controller;
    @FXML
    private Button addButton;
    @FXML
    private TextField obsName;
    @FXML
    private TextField obsHeight;

    public void handleCloseButtonAction(ActionEvent event)
    {
        if (!obsName.getText().trim().isEmpty() && !obsHeight.getText().trim().isEmpty())
        {
            try
            {
                obstacle = new Obstacle(obsName.getText().trim(), Integer.parseInt(obsHeight.getText().trim()), 0, 0, 0);
                controller.addObs(obstacle);

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
                controller.setSystemLogText(sdf.format(cal.getTime()) + " Obstacle " + "\"" + obstacle.getName() + "\" added to list.");

                ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
            }
            catch(NumberFormatException e)
            {
                Alert warningAlert1 = new Alert(Alert.AlertType.WARNING);
                warningAlert1.setContentText("Introduce the height as a number. Please try again.");
                warningAlert1.showAndWait();
            }
        }
        else
            {
            Alert warningAlert2 = new Alert(Alert.AlertType.WARNING);
            warningAlert2.setContentText("No name/height was introduced. Please try again.");
            warningAlert2.showAndWait();
        }
    }

    @FXML
    public void initialize()
    {
        addButton.setOnAction(this::handleCloseButtonAction);
    }

    public void setController(InterfaceController controller)
    {
        this.controller = controller;
    }
}
