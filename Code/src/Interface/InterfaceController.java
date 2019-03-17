package Interface;
import Data.Obstacle;
import Data.Runway;
import Exceptions.DontNeedRedeclarationException;
import Exceptions.NegativeParameterException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class InterfaceController {

    private ObservableList<PhysicalRunway> runways = FXCollections.observableArrayList();
    private ObservableList<Obstacle> obstacles = FXCollections.observableArrayList();

    private PhysicalRunway selectedRunway =
            new PhysicalRunway(new Runway("null",0,0,0,0,0),
                    (new Runway("null",0,0,0,0,0)));
    private Obstacle selectedObstacle;

    private String breakdown = "No calculations";
    @FXML
    private Button plusButton;
    @FXML
    private Button addObsButton;
    @FXML
    private Button resetViewButton;
    @FXML
    private Button brekdownButton;
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
    public Obstacle setSelectedObstacle() {
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



        if (whichRunway == 1) {
            breakdown = "Runway 1 (" + selectedRunway.getRunway1().getID() + "): \n " +
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
                    "- Slope calculation (" + selectedRunway.getRunway2().getSlopeCalc() + ").";
        } else if (whichRunway == 2) {
            breakdown =  "Runway 1 (" + selectedRunway.getRunway1().getID() + "): \n " +
                    "Take Off Away: \n" +
                    "TORA = Original TORA (" + selectedRunway.getRunway1().getOriginalTORA() + ") " +
                    "- Blast protection (" + Runway.getBlastProtection() + ") - Distance from threshold (" +
                    selectedObstacle.getDistToThreshold1() + ") \n - Displaced threshold (" + selectedRunway.getRunway1().getDisplacedThreshold() + ") " +
                    "= " +  selectedRunway.getRunway1().getTORA() + "\n " +
                    "ASDA = TORA (" + selectedRunway.getRunway1().getTORA() + ") + Clearway (" + selectedRunway.getRunway1().getClearway() + ") = " +  selectedRunway.getRunway1().getASDA() + "\n" +
                    "TODA = TORA (" + selectedRunway.getRunway1().getTORA() + ") + Clearway (" + selectedRunway.getRunway1().getClearway() + ") = " +  selectedRunway.getRunway1().getASDA() + "\n" +

                    "Landing Over: \n " +
                    "LDA = Original LDA (" + selectedRunway.getRunway1().getOriginalLDA() + ") - Distance from threshold (" +
                    selectedObstacle.getDistToThreshold1() + ") - Strip end (" + Runway.getStripEnd() + ") " +
                    "- Slope calculation (" + selectedRunway.getRunway1().getSlopeCalc() + ") = " + selectedRunway.getRunway1().getLDA() + "\n " +


                    "Runway 2 (" + selectedRunway.getRunway2().getID() + "): \n " +
                    "Take Off Towards: \n" +
                    "TORA = Distance from threshold " + selectedObstacle.getDistToThreshold1() +
                    "- Slope calculation (" + selectedRunway.getRunway2().getSlopeCalc() + ") " +
                    "- Strip end (" + Runway.getStripEnd() + ") = " + selectedRunway.getRunway2().getTORA() +"\n"+

                    "ASDA = Recalculated TORA (" + selectedRunway.getRunway2().getTORA() + ") " +
                    " + Clearway (" + selectedRunway.getRunway2().getClearway() + ") = " + selectedRunway.getRunway2().getASDA() + "\n" +

                    "TODA = Recalculated TORA (" + selectedRunway.getRunway2().getTORA() + ") " +
                    " + Clearway (" + selectedRunway.getRunway2().getClearway() + ") = " + selectedRunway.getRunway2().getTODA() + "\n" +

                    "Landing Towards: \n " +
                    "LDA = Distance from threshold " + selectedObstacle.getDistToThreshold2() +
                    " - RESA (" + Runway.getRESA() + ") - Strip End (" + Runway.getStripEnd() + ") = " + selectedRunway.getRunway2().getLDA() + "\n";
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
        setSelectedObstacle();
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

    @FXML
    public void seeBreakdown(ActionEvent ae) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CalculationsBreakdown.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Breakdown of calculations");
            stage.setScene(new Scene(root));

            SeeBreakdownController controller = fxmlLoader.getController();
            controller.setBreakdownLabel(breakdown);

            stage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    public Text runwayLabel1;
    @FXML
    public Text runwayLabel2;
    @FXML
    public Text runwayLabel1L;
    @FXML
    public Text runwayLabel2L;
    @FXML
    public Group runwayG;
    @FXML
    public Slider rotationSlider;
    @FXML
    public Group compass;
    public Line thresholdLineLDA11;
    public Text LDAText1;
    public Line LDAArr11;
    public Line LDAArr21;
    public Line thresholdLineLDA21;
    public Line LDALine1;
    public Line thresholdLineLDA12;
    public Text LDAText2;
    public Line LDAArr12;
    public Line LDAArr22;
    public Line thresholdLineLDA22;
    public Line LDALine2;
    public Group disThr1;
    public Group disThr2;
    public Rectangle clearway1;
    public Rectangle clearway2;
    public Rectangle stopway1;
    public Rectangle stopway2;
    public Line thresholdLineTORA11;
    public Text TORAText1;
    public Line TORALine1;
    public Line TORAArr11;
    public Line TORAArr21;
    public Line thresholdLineTORA21;
    public Line thresholdLineTORA12;
    public Text TORAText2;
    public Line TORALine2;
    public Line TORAArr12;
    public Line TORAArr22;
    public Line thresholdLineTORA22;
    public Line thresholdLineASDA11;
    public Text ASDAText1;
    public Line ASDALine1;
    public Line ASDAArr11;
    public Line ASDAArr21;
    public Line thresholdLineASDA21;
    public Line thresholdLineASDA12;
    public Text ASDAText2;
    public Line ASDALine2;
    public Line ASDAArr12;
    public Line ASDAArr22;
    public Line thresholdLineASDA22;
    public Line thresholdLineTODA11;
    public Text TODAText1;
    public Line TODALine1;
    public Line TODAArr11;
    public Line TODAArr21;
    public Line thresholdLineTODA21;
    public Line thresholdLineTODA12;
    public Text TODAText2;
    public Line TODALine2;
    public Line TODAArr12;
    public Line TODAArr22;
    public Line thresholdLineTODA22;

    @FXML
    public Rectangle obstacleTop;

    public void runwaySelected() {
        PhysicalRunway runway = runwaySelection.getValue();
        Runway runway1 = runway.getRunway1();
        Runway runway2 = runway.getRunway2();
        if (runway1.getLDA()>runway2.getLDA()) {
            runway.switchRunways();
            Runway temp = runway1;
            runway1 = runway2;
            runway2 = temp;
        }

        if (runway1.getClearway()>0) {
            clearway2.setVisible(true);
            TODALine1.setEndX(767);
            TODAArr11.setLayoutX(767);
            TODAArr21.setLayoutX(767);
            thresholdLineTODA21.setLayoutX(767);
        } else {
            clearway2.setVisible(false);
            TODALine1.setEndX(737);
            TODAArr11.setLayoutX(737);
            TODAArr21.setLayoutX(737);
            thresholdLineTODA21.setLayoutX(737);
        }

        if (runway1.getStopway()>0) {
            stopway2.setVisible(true);
            ASDALine1.setEndX(757);
            ASDAArr11.setLayoutX(757);
            ASDAArr21.setLayoutX(757);
            thresholdLineASDA21.setLayoutX(757);
        } else {
            stopway2.setVisible(false);
            ASDALine1.setEndX(737);
            ASDAArr11.setLayoutX(737);
            ASDAArr21.setLayoutX(737);
            thresholdLineASDA21.setLayoutX(737);
        }

        if (runway2.getClearway()>0) {
            clearway1.setVisible(true);
            TODALine2.setStartX(0);
            TODAArr12.setLayoutX(0);
            TODAArr22.setLayoutX(0);
            thresholdLineTODA22.setLayoutX(0);
        } else {
            clearway1.setVisible(false);
            TODALine2.setStartX(30);
            TODAArr12.setLayoutX(30);
            TODAArr22.setLayoutX(30);
            thresholdLineTODA22.setLayoutX(30);
        }

        if (runway2.getStopway()>0) {
            stopway1.setVisible(true);
            ASDALine2.setStartX(0);
            ASDAArr12.setLayoutX(0);
            ASDAArr22.setLayoutX(0);
            thresholdLineASDA22.setLayoutX(0);
        } else {
            stopway1.setVisible(false);
            ASDALine2.setStartX(20);
            ASDAArr12.setLayoutX(20);
            ASDAArr22.setLayoutX(20);
            thresholdLineASDA22.setLayoutX(20);
        }

        int dt = runway1.getDisplacedThreshold();
        if (dt > 0) {
            disThr1.setVisible(true);
            int x = 740 * dt / runway1.getTORA();
            disThr1.setTranslateX(x);
            thresholdLineLDA11.setTranslateX(x);
            LDAText1.setTranslateX(x);
            LDALine1.setStartX(32 + x);
        } else disThr1.setVisible(false);

        dt = runway2.getDisplacedThreshold();
        if (dt > 0) {
            disThr2.setVisible(true);
            int x = 740 * dt / runway2.getTORA();
            disThr2.setTranslateX(x);
            thresholdLineLDA12.setTranslateX(x);
            LDAText2.setTranslateX(x);
            LDALine2.setStartX(32 + x);
        } else disThr2.setVisible(false);



        char[] runwayNames = runway.getName().toCharArray();
        runwayLabel1.setText(Character.toString(runwayNames[0])+Character.toString(runwayNames[1]));
        runwayLabel1L.setText(Character.toString(runwayNames[2]));
        runwayLabel2.setText(Character.toString(runwayNames[4])+Character.toString(runwayNames[5]));
        runwayLabel2L.setText(Character.toString(runwayNames[6]));
        int rotation = (Character.getNumericValue(runwayNames[0])*10+Character.getNumericValue(runwayNames[1])-9)*10;
        if (rotation<0) rotation += 360;
        rotationSlider.setValue(rotation);
        rotate();



        selectedRunway = runwaySelection.getValue();
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

        recalcLDA1.setText("-");
        recalcTORA1.setText("-");
        recalcTODA1.setText("-");
        recalcASDA1.setText("-");

        recalcLDA2.setText("-");
        recalcTORA2.setText("-");
        recalcTODA2.setText("-");
        recalcASDA2.setText("-");

        obstacleTop.setVisible(false);
    }

    public void rotate() {
        double rotation = rotationSlider.getValue();
        runwayGroup.setRotate(rotation);
        compass.setRotate(rotation);
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

    public void resetView(ActionEvent actionEvent) {
        runwayGroup.setScaleX(1);
        runwayGroup.setScaleY(1);
        runwayGroup.setTranslateX(0);
        runwayGroup.setTranslateY(0);
        runwayGroup.setRotate(0);
        compass.setRotate(0);
        zoomSlider.setValue(1);
        rotationSlider.setValue(0);
    }

    public void showObstacle() {
        int dtt1 = selectedObstacle.getDistToThreshold1();
        int dtt2 = selectedObstacle.getDistToThreshold2();
        int dtc = selectedObstacle.getDistToCentreline();
        obstacleTop.setVisible(true);
        int x = dtt1*740/selectedRunway.getRunway1().getTORA();
        obstacleTop.setTranslateX(x);
        obstacleTop.setWidth(740 - dtt2*740/selectedRunway.getRunway2().getTORA()-x);
        obstacleTop.setTranslateY(dtc/3.0+25);
    }
}
