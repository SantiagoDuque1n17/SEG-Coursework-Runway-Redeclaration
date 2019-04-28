package Interface;

import Controller.SelectAirportController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserInterface extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectAirport.fxml"));
        Parent root = loader.load();
        SelectAirportController controller = loader.getController();
        controller.setHostServices(getHostServices());
        primaryStage.setTitle("Runway Re-declaration tool");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
