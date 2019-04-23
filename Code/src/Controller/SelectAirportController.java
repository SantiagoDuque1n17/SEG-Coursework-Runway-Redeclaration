package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SelectAirportController
{
    @FXML
    private Button selectHeathrowButton;

    public void handleSelectedAirportButtonAction(ActionEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader((getClass().getResource("UserInterface.fxml")));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Runway Re-declaration tool");
            stage.setScene(new Scene(root, 1280, 800));
            stage.setResizable(false);

            InterfaceController controller = (InterfaceController) fxmlLoader.getController();
            controller.setController(this);

            controller.setAirportName(new Label("Heathrow Airport"));

            stage.show();
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }
        catch(Exception e)
        {
            System.out.println("Can't load new window");
        }
    }

    @FXML
    public void initialize()
    {
        selectHeathrowButton.setOnAction(this::handleSelectedAirportButtonAction);
    }
}
