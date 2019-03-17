package Interface;
import Data.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javafx.scene.text.Text;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Exceptions.*;

public class InterfaceController {



    private ObservableList<PhysicalRunway> runways = FXCollections.observableArrayList();
    private ObservableList<Obstacle> obstacles = FXCollections.observableArrayList();

    private PhysicalRunway selectedRunway =
            new PhysicalRunway(new Runway("null",0,0,0,0,0),
                    (new Runway("null",0,0,0,0,0)));
    private Obstacle selectedObstacle = new Obstacle("default", 0,0,0,0);

    @FXML
    private Button plusButton;
    @FXML
    private Button addObsButton;

    @FXML
    public ComboBox<PhysicalRunway> runwaySelection;
    @FXML
    public ComboBox<Obstacle> obstacleSelection;

    @FXML
    public TextArea warningBox = new TextArea();

    @FXML
    public Label calculationsLabel = new Label();

    /**
     * Labels in the display panel
     */

    /**
     * Original values
     */

    @FXML
    private Label originalLDA1 = new Label();
    @FXML
    private Label originalLDA2 = new Label();
    @FXML
    private Label originalTORA1 = new Label();
    @FXML
    private Label originalTORA2 = new Label();
    @FXML
    private Label originalTODA1 = new Label();
    @FXML
    private Label originalTODA2 = new Label();
    @FXML
    private Label originalASDA1 = new Label();
    @FXML
    private Label originalASDA2 = new Label();
    @FXML
    private Label originalDT1 = new Label();
    @FXML
    private Label originalDT2 = new Label();
    @FXML
    private Label originalStopway1 = new Label();
    @FXML
    private Label originalClearway1 = new Label();
    @FXML
    private Label originalStopway2 = new Label();
    @FXML
    private Label originalClearway2 = new Label();

    /**
     * Previous values
     */

    @FXML
    private Label recalcLDA1 = new Label();
    @FXML
    private Label recalcLDA2 = new Label();
    @FXML
    private Label recalcTORA1 = new Label();
    @FXML
    private Label recalcTORA2 = new Label();
    @FXML
    private Label recalcTODA1 = new Label();
    @FXML
    private Label recalcTODA2 = new Label();
    @FXML
    private Label recalcASDA1 = new Label();
    @FXML
    private Label recalcASDA2 = new Label();


    @FXML
    public Obstacle setSelectedObstacle(ActionEvent ae) {
        Obstacle obstacle = obstacleSelection.getValue();
        selectedObstacle = obstacle;
        System.out.println("Selected obstacle: " + selectedObstacle.getName());
        return obstacle;
    }

    public void executeCalculation(ActionEvent ae) {
        if (selectedRunway.equals(null) || selectedObstacle.equals(null)) {
            System.out.println("Please select an obstacle and a runway from the list");
            // TODO : Make this a pop up possibly
        }

        System.out.println("Selected runway: " + selectedRunway.getName());
        System.out.println("Selected obstacle: " + selectedObstacle.getName());

        int whichRunway = 0;

        try {
            if (selectedObstacle.getName().equals("default")) { selectedObstacle = obstacleSelection.getValue(); }
            whichRunway = selectedRunway.addObstacle(selectedObstacle);
        } catch (DontNeedRedeclarationException e) {
            warningBox.setText("Calculation invalid: Redeclaration not needed");
            System.err.println("Calculation invalid: Redeclaration not needed");
        } catch (NegativeParameterException e) {
            warningBox.setText("Calculation invalid: Negative parameters, can't redeclare");
            System.err.println("Calculation invalid: Negative parameters, can't redeclare");

            //TODO: Make these pop-ups as well?
        }

        originalLDA1.setText(String.valueOf(selectedRunway.getRunway1().getOriginalLDA()));
        originalTORA1.setText(String.valueOf(selectedRunway.getRunway1().getOriginalTORA()));
        originalTODA1.setText(String.valueOf(selectedRunway.getRunway1().getOriginalTODA()));
        originalASDA1.setText(String.valueOf(selectedRunway.getRunway1().getOriginalASDA()));
        originalDT1.setText(String.valueOf(selectedRunway.getRunway1().getDisplacedThreshold()));
        originalStopway1.setText(String.valueOf(selectedRunway.getRunway1().getStopway()));
        originalClearway1.setText(String.valueOf(selectedRunway.getRunway1().getClearway()));

        originalLDA2.setText(String.valueOf(selectedRunway.getRunway2().getOriginalLDA()));
        originalTORA2.setText(String.valueOf(selectedRunway.getRunway2().getOriginalTORA()));
        originalTODA2.setText(String.valueOf(selectedRunway.getRunway2().getOriginalTODA()));
        originalASDA2.setText(String.valueOf(selectedRunway.getRunway2().getOriginalASDA()));
        originalDT2.setText(String.valueOf(selectedRunway.getRunway2().getDisplacedThreshold()));
        originalStopway2.setText(String.valueOf(selectedRunway.getRunway2().getStopway()));
        originalClearway2.setText(String.valueOf(selectedRunway.getRunway2().getClearway()));


        recalcLDA1.setText(String.valueOf(selectedRunway.getRunway1().getLDA()));
        recalcTORA1.setText(String.valueOf(selectedRunway.getRunway1().getTORA()));
        recalcTODA1.setText(String.valueOf(selectedRunway.getRunway1().getTODA()));
        recalcASDA1.setText(String.valueOf(selectedRunway.getRunway1().getASDA()));

        recalcLDA2.setText(String.valueOf(selectedRunway.getRunway2().getLDA()));
        recalcTORA2.setText(String.valueOf(selectedRunway.getRunway2().getTORA()));
        recalcTODA2.setText(String.valueOf(selectedRunway.getRunway2().getTODA()));
        recalcASDA2.setText(String.valueOf(selectedRunway.getRunway2().getASDA()));

        //TODO: Make the calculations breakdown a pop up

        if (whichRunway == 1) {
            calculationsLabel.setText("Runway 1 (" + selectedRunway.getRunway1().getID() + "): \n " +
                    "Take Off Towards: \n" +
                    "TORA = Distance from threshold " + selectedObstacle.getDistToThreshold1() +
                    "- Slope calculation (" + selectedRunway.getRunway1().getSlopeCalc() + ") " +
                    "- Strip end (" + Runway.getStripEnd() + ") = " + selectedRunway.getRunway1().getTORA() +")\n"+

                    "ASDA: TORA (" + selectedRunway.getRunway1().getTORA() + ") " +
                    " + Clearway (" + selectedRunway.getRunway1().getClearway() + ") \n" +

                    "TODA : TORA (" + selectedRunway.getRunway1().getTORA() + ") " +
                    " + Clearway (" + selectedRunway.getRunway1().getClearway() + ") \n" +

                    "Landing Towards: \n +" +
                    "LDA = Distance from threshold " + selectedObstacle.getDistToThreshold1() +
                    " - RESA (" + Runway.getRESA() + ") - Strip End (" + Runway.getStripEnd() + "). \n +" +


                                "Runway 2 (" + selectedRunway.getRunway2().getID() + "): \n " +
                    "Take Off Away: \n" +
                    "TORA = Original TORA (" + selectedRunway.getRunway2().getOriginalTORA() + ") \n" +
                    "- Blast protection (" + Runway.getBlastProtection() + ") - Distance from threshold (" +
                    selectedObstacle.getDistToThreshold2() + ") - Displaced threshold (" + selectedRunway.getRunway2().getDisplacedThreshold() + ") \n" +
                    "ASDA = TORA (" + selectedRunway.getRunway2().getTORA() + ") + Clearway (" + selectedRunway.getRunway2().getClearway() + ") \n" +
                    "TODA = TORA (" + selectedRunway.getRunway2().getTORA() + ") + Clearway (" + selectedRunway.getRunway2().getClearway() + ") \n" +

                    "Landing Over: \n +" +
                    "LDA = Original LDA (" + selectedRunway.getRunway2().getOriginalLDA() + ") - Distance from threshold (" +
                    selectedObstacle.getDistToThreshold2() + ") - Strip end (" + Runway.getStripEnd() + ") \n" +
                    "- Slope calculation (" + selectedRunway.getRunway2().getSlopeCalc() + ").");
        } else if (whichRunway == 2) {
            calculationsLabel.setText("Runway 1 (" + selectedRunway.getRunway1().getID() + "): \n " +
                    "Take Off Away: \n" +
                    "TORA = Original TORA (" + selectedRunway.getRunway1().getOriginalTORA() + ") \n" +
                    "- Blast protection (" + Runway.getBlastProtection() + ") - Distance from threshold (" +
                    selectedObstacle.getDistToThreshold1() + ") - Displaced threshold (" + selectedRunway.getRunway1().getDisplacedThreshold() + ") \n" +
                    "ASDA = TORA (" + selectedRunway.getRunway1().getTORA() + ") + Clearway (" + selectedRunway.getRunway1().getClearway() + ") \n" +
                    "TODA = TORA (" + selectedRunway.getRunway1().getTORA() + ") + Clearway (" + selectedRunway.getRunway1().getClearway() + ") \n" +

                    "Landing Over: \n +" +
                    "LDA = Original LDA (" + selectedRunway.getRunway1().getOriginalLDA() + ") - Distance from threshold (" +
                    selectedObstacle.getDistToThreshold1() + ") - Strip end (" + Runway.getStripEnd() + ") \n" +
                    "- Slope calculation (" + selectedRunway.getRunway1().getSlopeCalc() + "). \n" +


                    "Runway 1 (" + selectedRunway.getRunway2().getID() + "): \n " +
                    "Take Off Towards: \n" +
                    "TORA = Distance from threshold " + selectedObstacle.getDistToThreshold1() +
                    "- Slope calculation (" + selectedRunway.getRunway2().getSlopeCalc() + ") " +
                    "- Strip end (" + Runway.getStripEnd() + ") = " + selectedRunway.getRunway2().getTORA() +")\n"+

                    "ASDA: TORA (" + selectedRunway.getRunway2().getTORA() + ") " +
                    " + Clearway (" + selectedRunway.getRunway2().getClearway() + ") \n" +

                    "TODA : TORA (" + selectedRunway.getRunway2().getTORA() + ") " +
                    " + Clearway (" + selectedRunway.getRunway2().getClearway() + ") \n" +

                    "Landing Towards: \n +" +
                    "LDA = Distance from threshold " + selectedObstacle.getDistToThreshold2() +
                    " - RESA (" + Runway.getRESA() + ") - Strip End (" + Runway.getStripEnd() + ").");
        }

    }



    private void loadRunways(){
        runways.removeAll();
        createRunwaysList();
    }

    private void loadObstacles(){
        obstacles.removeAll();
        loadObstaclesList();
    }


    public void handlePlusButtonAction(ActionEvent event)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddObstacle.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add obstacle");
            stage.setScene(new Scene(root));

            AddObstacleController controller = (AddObstacleController) fxmlLoader.getController();
            controller.setController(this);

            stage.show();
        }
        catch(Exception e)
        {
            System.out.println("Cant load new window");
        }
    }

    public void handleAddObsButtonAction(ActionEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader((getClass().getResource("AddObstacleOnRunway.fxml")));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add obstacle");
            stage.setScene(new Scene(root));

            AddObstacleOnRunwayController controller = (AddObstacleOnRunwayController) fxmlLoader.getController();
            controller.setController(this);

            stage.show();
        }
        catch(Exception e)
        {
            System.out.println("Cant load new window");
        }
    }

    @FXML
    public void initialize(){
        runwaySelection.getItems().removeAll(runwaySelection.getItems());
        loadRunways();
        runwaySelection.getItems().addAll(runways);
        runwaySelection.getSelectionModel().select(runways.get(0));

        obstacleSelection.getItems().removeAll(obstacleSelection.getItems());
        loadObstacles();
        obstacleSelection.getItems().addAll(obstacles);
        obstacleSelection.getSelectionModel().select(obstacles.get(0));

        plusButton.setOnAction(this::handlePlusButtonAction);
        plusButton.requestLayout();
        addObsButton.setOnAction(this::handleAddObsButtonAction);

        runwaySelected();
    }

    public void createRunwaysList(){
        File file = new File("runways.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        try{
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        NodeList physicalRunways = document.getElementsByTagName("PhysicalRunway");

        for(int i = 0; i < physicalRunways.getLength(); i++)
        {
            Element physicalRunway = (Element)physicalRunways.item(i);
            NodeList logicalRunways = physicalRunway.getElementsByTagName("LogicalRunway");
            Element logicalRunway = (Element) logicalRunways.item(0);
            String ID = logicalRunway.getElementsByTagName("ID").item(0).getTextContent();
            int LDA = Integer.parseInt(logicalRunway.getElementsByTagName("LDA").item(0).getTextContent());
            int TORA = Integer.parseInt(logicalRunway.getElementsByTagName("TORA").item(0).getTextContent());
            int TODA = Integer.parseInt(logicalRunway.getElementsByTagName("TODA").item(0).getTextContent());
            int ASDA = Integer.parseInt(logicalRunway.getElementsByTagName("ASDA").item(0).getTextContent());
            int displacedThreshold=  Integer.parseInt(logicalRunway.getElementsByTagName("Displaced_Threshold").item(0).getTextContent());
            Runway runway1 = new Runway(ID, LDA, TORA, TODA, ASDA, displacedThreshold);

            logicalRunway = (Element) logicalRunways.item(1);

            ID = logicalRunway.getElementsByTagName("ID").item(0).getTextContent();
            LDA = Integer.parseInt(logicalRunway.getElementsByTagName("LDA").item(0).getTextContent());
            TORA = Integer.parseInt(logicalRunway.getElementsByTagName("TORA").item(0).getTextContent());
            TODA = Integer.parseInt(logicalRunway.getElementsByTagName("TODA").item(0).getTextContent());
            ASDA = Integer.parseInt(logicalRunway.getElementsByTagName("ASDA").item(0).getTextContent());
            displacedThreshold=  Integer.parseInt(logicalRunway.getElementsByTagName("Displaced_Threshold").item(0).getTextContent());
            Runway runway2 = new Runway(ID, LDA, TORA, TODA, ASDA, displacedThreshold);

            PhysicalRunway runway = new PhysicalRunway(runway1, runway2);
            runways.add(runway);

        }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }

    public void loadObstaclesList(){
        File file = new File("obstacles.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            NodeList obstaclesList = document.getElementsByTagName("Obstacle");

            for(int i = 0; i<obstaclesList.getLength(); i ++){
                Element obstacle = (Element)obstaclesList.item(i);
                String name = obstacle.getElementsByTagName("Name").item(0).getTextContent();
                int height = Integer.parseInt(obstacle.getElementsByTagName("Height").item(0).getTextContent());
                Obstacle obstacle1 = new Obstacle(name,height,0,0,0);
                obstacles.add(obstacle1);

            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    public void runwaySelected() {
        PhysicalRunway runway = runwaySelection.getValue();
        String[] runwayNames = runway.getName().split("/");
        runwayLabel1.setText(runwayNames[0]);
        runwayLabel2.setText(runwayNames[1]);
        runwayG.setScaleX(runway.getWidth()/3800.0);

        selectedRunway = runwaySelection.getValue();
    }

    @FXML
    public Text runwayLabel1;
    @FXML
    public Text runwayLabel2;
    @FXML
    public Group runwayG;
    @FXML
    public Slider rotationSlider;

    public void rotate() {
        runwayGroup.setRotate(rotationSlider.getValue());
    }

    @FXML
    public Group runwayGroup;
    @FXML
    public Slider zoomSlider;

    public void zoom() {
        runwayGroup.setScaleX(zoomSlider.getValue()/50);
        runwayGroup.setScaleY(zoomSlider.getValue()/50);
    }

    private double x,y;

    public void move(MouseEvent mouseEvent) {
        runwayGroup.setTranslateX(mouseEvent.getX()-x);
        runwayGroup.setTranslateY(mouseEvent.getY()-y);
    }

    public void getPos(MouseEvent mouseEvent) {
        x = mouseEvent.getX()-runwayGroup.getTranslateX();
        y = mouseEvent.getY()-runwayGroup.getTranslateY();
    }

    public void scroll(ScrollEvent scrollEvent) {
        zoomSlider.setValue(zoomSlider.getValue()+(scrollEvent.getDeltaY()/10));
        zoom();
    }

    public void addObs(Obstacle obs){
        obstacles.add(obs);
        obstacleSelection.getItems().add(obs);
    }

    public Obstacle getObstacleFromComboBox()
    {
        return obstacleSelection.getValue();
    }
}
