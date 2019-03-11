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
        Parent root = FXMLLoader.load(getClass().getResource("UserInterface.fxml"));
        primaryStage.setTitle("Runway Re-declaration tool");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
