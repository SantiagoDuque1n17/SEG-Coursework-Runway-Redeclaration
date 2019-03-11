import javafx.application.Application;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.border.TitledBorder;

public class GUI extends Application {
    @Override
    public void start(Stage stage) {
        Label runwaySelectionLabel = new Label("Runway selection");
        Label obstacleSelectionLabel = new Label("Obstacle selection");
        Label controlPanelLabel = new Label("Control panel");
        Label oiginalValuesLabel = new Label("Original values");
        Label previousValuesLabel = new Label("Previous values");
        Label recalcValuesLabel = new Label("Recalculated values");

        BorderPane bpane = new BorderPane();


        GridPane right = new GridPane();

        //GridPane titlePane = new GridPane();
        //titlePane.setMinSize();

        right.setPadding(new Insets(3, 10, 3, 10));
        right.setMinSize(500, 900);
        right.setVgap(5);
        right.setHgap(5);
        right.setAlignment(Pos.CENTER);

        right.setGridLinesVisible(true);
        right.setStyle("-fx-background-color: PINK;");

        GridPane runwaySelection = new GridPane();
        runwaySelection.setMinSize(500,145);
        runwaySelection.add(runwaySelectionLabel, 0,0);

        GridPane obstacleSelection = new GridPane();
        obstacleSelection.setMinSize(500,145);
        obstacleSelection.add(obstacleSelectionLabel, 0,0);

        GridPane controlPanel = new GridPane();
        controlPanel.setMinSize(500,145);
        controlPanel.add(controlPanelLabel, 0,0);

        GridPane originalValues = new GridPane();
        originalValues.setMinSize(500,145);
        originalValues.add(oiginalValuesLabel, 0,0);

        GridPane previousValues = new GridPane();
        previousValues.setMinSize(500,145);
        previousValues.add(previousValuesLabel, 0,0);

        GridPane recalcValues = new GridPane();
        recalcValues.setMinSize(500,145);
        recalcValues.add(recalcValuesLabel, 0,0);

        right.add(runwaySelection, 0,0);
        right.add(obstacleSelection, 0,1);
        right.add(controlPanel, 0,2);
        right.add(originalValues, 0,3);
        right.add(previousValues, 0,4);
        right.add(recalcValues, 0,5);

        BorderPane centre = new BorderPane();
        Label topViewLabel = new Label("Top-Down view");


        BorderPane sideView = new BorderPane();
        sideView.setPadding(new Insets(3, 10, 3, 10));
        sideView.setMinSize(1100, 300);

        Label sideViewLabel = new Label("Side view");
        sideViewLabel.setFont(new Font("Arial", 20));

        sideView.setStyle("-fx-background-color: CYAN;");
        sideView.setTop(sideViewLabel);

        centre.setBottom(sideView);
        centre.setTop(topViewLabel);

        bpane.setRight(right);
        bpane.setCenter(centre);

        bpane.setStyle("-fx-background-color: BEIGE;");


        //Creating a scene object
        Scene scene = new Scene(bpane, 1600, 900);

        //Setting title to the Stage
        stage.setTitle("Runway Redeclaration Tool v.02");

        //Adding scene to the stage
        stage.setScene(scene);
        stage.setResizable(false);

        //Displaying the contents of the stage
        stage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}
