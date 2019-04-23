package Interface;

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
        Parent root = FXMLLoader.load(getClass().getResource("SelectAirport.fxml"));
        primaryStage.setTitle("Runway Re-declaration tool");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
