package Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class InterfaceController
{
    @FXML
    private Button myButton;

    public void handleButtonAction(ActionEvent event)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddObstacle.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add obstacle");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e)
        {
            System.out.println("Cant load new window");
        }
    }

    public void initialize()
    {
        myButton.setOnAction(this::handleButtonAction);
    }
}
