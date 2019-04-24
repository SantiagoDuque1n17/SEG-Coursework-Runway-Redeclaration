package Controller;
import Interface.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddObstacleOnRunwayController
{
    InterfaceController controller;
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
        if(!distToCenter.getText().trim().isEmpty() && !distToLeftT.getText().trim().isEmpty() && !distToRightT.getText().trim().isEmpty()) {
            try {
                controller.getObstacleFromComboBox().setDistToCentreline((Integer.parseInt(distToCenter.getText().trim())));
                controller.getObstacleFromComboBox().setDistToThreshold1((Integer.parseInt(distToLeftT.getText().trim())));
                controller.getObstacleFromComboBox().setDistToThreshold2((Integer.parseInt(distToRightT.getText().trim())));
                controller.executeCalculation();

                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");

                controller.setSystemLogText(sdf.format(cal.getTime()) + " Obstacle " + "\"" + controller.getObstacleFromComboBox().getName() + "\" added to runway.");
                controller.setSystemLogText(sdf.format(cal.getTime()) + " Obstacle height: " + controller.getObstacleFromComboBox().getHeight());
                controller.setSystemLogText(sdf.format(cal.getTime()) + " Distance to centreline: " + controller.getObstacleFromComboBox().getDistToCentreline());
                controller.setSystemLogText(sdf.format(cal.getTime()) + " Distance to threshold 1: " + controller.getObstacleFromComboBox().getDistToThreshold1());
                controller.setSystemLogText(sdf.format(cal.getTime()) + " Distance to threshold 2: " + controller.getObstacleFromComboBox().getDistToThreshold2());
                ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
            }
            catch(NumberFormatException e)
            {
                Alert warningAlert1 = new Alert(Alert.AlertType.WARNING);
                warningAlert1.setContentText("Introduce the parameters as a number. Please try again.");
                warningAlert1.showAndWait();
            }
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

    public void setController(InterfaceController controller)
    {
        this.controller = controller;
    }
}
