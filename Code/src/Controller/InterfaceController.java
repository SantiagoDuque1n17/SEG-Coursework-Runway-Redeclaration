package Controller;
import Model.PhysicalRunway;
import Model.Obstacle;
import Model.Runway;
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
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
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
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterfaceController {
    private SelectAirportController controller;

    private ObservableList<PhysicalRunway> runways = FXCollections.observableArrayList();
    private ObservableList<Obstacle> obstacles = FXCollections.observableArrayList();

    private PhysicalRunway selectedRunway =
            new PhysicalRunway(new Runway("null",0,0,0,0,0),
                    (new Runway("null",0,0,0,0,0)));
    private Obstacle selectedObstacle;

    private String breakdown = "No calculations";

    @FXML
    private Label airportName;

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

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
            setSystemLogText(sdf.format(cal.getTime()) + " Calculations executed.");

        } catch (DontNeedRedeclarationException e) {
            warningBox.setText("Calculation invalid: Redeclaration not needed");
            System.err.println("Calculation invalid: Redeclaration not needed");
        } catch (NegativeParameterException e) {
            warningBox.setText("Calculation invalid: Negative parameters, can't redeclare");
            System.err.println("Calculation invalid: Negative parameters, can't redeclare");

            //TODO: Make these pop-ups as well?
        }

        /**
         * Setting the status labels
         */
        if (selectedRunway.getRunway1().getStatus().equals("CLOSED")) {
            statusLabel1.setText("CLOSED");
        } else if (selectedRunway.getRunway1().getStatus().equals("RESTRICTED OPERATIONS")) {
            statusLabel1.setText("RESTRICTED OPERATIONS");
        } else {
            statusLabel1.setText("FREE");
        }

        if (selectedRunway.getRunway2().getStatus().equals("CLOSED")) {
            statusLabel2.setText("CLOSED");
        } else if (selectedRunway.getRunway2().getStatus().equals("RESTRICTED OPERATIONS")) {
            statusLabel2.setText("RESTRICTED OPERATIONS");
        } else {
            statusLabel2.setText("FREE");
        }


        /**
         * Writing the values
         */
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


        /**
         * Breakdown of calculations in the pop-up
         */
        if (whichRunway == 1) {
            breakdown = "RUNWAY 1 (" + selectedRunway.getRunway1().getID() + "): \n " +
                    "Take Off Towards: \n" +
                    "• TORA = Distance from threshold " + selectedObstacle.getDistToThreshold1() +
                    " - Slope calculation (" + selectedRunway.getRunway1().getSlopeCalc() + ") " +
                    "- Strip end (" + Runway.getStripEnd() + ") = " + selectedRunway.getRunway1().getTORA() +"\n"+

                    "• ASDA: Recalculated TORA (" + selectedRunway.getRunway1().getTORA() + ") " +
                    " + Clearway (" + selectedRunway.getRunway1().getClearway() + ") = " + selectedRunway.getRunway1().getASDA() + "\n" +

                    "• TODA : Recalculated TORA (" + selectedRunway.getRunway1().getTORA() + ") " +
                    " + Clearway (" + selectedRunway.getRunway1().getClearway() + ") = " + selectedRunway.getRunway1().getTODA() + "\n" +

                    "Landing Towards: \n" +
                    "• LDA = Distance from threshold " + selectedObstacle.getDistToThreshold1() +
                    " - RESA (" + Runway.getRESA() + ") - Strip End (" + Runway.getStripEnd() + ") = " + selectedRunway.getRunway1().getLDA() + "\n" +


                    "\nRUNWAY 2 (" + selectedRunway.getRunway2().getID() + "): \n " +
                    "Take Off Away: \n" +
                    "• TORA = Original TORA (" + selectedRunway.getRunway2().getOriginalTORA() + ") " +
                    "- Blast protection (" + Runway.getBlastProtection() + ") - Distance from threshold (" +
                    selectedObstacle.getDistToThreshold2() + ") - Displaced threshold (" + selectedRunway.getRunway2().getDisplacedThreshold() + ") = " + selectedRunway.getRunway2().getTORA() + "\n" +
                    "• ASDA = TORA (" + selectedRunway.getRunway2().getTORA() + ") + Clearway (" + selectedRunway.getRunway2().getClearway() + ") = " + selectedRunway.getRunway2().getASDA() + "\n" +
                    "• TODA = TORA (" + selectedRunway.getRunway2().getTORA() + ") + Clearway (" + selectedRunway.getRunway2().getClearway() + ") = " + selectedRunway.getRunway2().getTODA() + "\n" +

                    "Landing Over: \n" +
                    "• LDA = Original LDA (" + selectedRunway.getRunway2().getOriginalLDA() + ") - Distance from threshold (" +
                    selectedObstacle.getDistToThreshold2() + ") - Strip end (" + Runway.getStripEnd() + ") " +
                    "- Slope calculation (" + selectedRunway.getRunway2().getSlopeCalc() + ") = " + selectedRunway.getRunway2().getLDA();
        } else if (whichRunway == 2) {
            breakdown =  "RUNWAY 1 (" + selectedRunway.getRunway1().getID() + "): \n" +
                    "Take Off Away: \n" +
                    "• TORA = Original TORA (" + selectedRunway.getRunway1().getOriginalTORA() + ") " +
                    "- Blast protection (" + Runway.getBlastProtection() + ") - Distance from threshold (" +
                    selectedObstacle.getDistToThreshold1() + ") \n - Displaced threshold (" + selectedRunway.getRunway1().getDisplacedThreshold() + ") " +
                    "= " +  selectedRunway.getRunway1().getTORA() + "\n " +
                    "• ASDA = Recalculated TORA (" + selectedRunway.getRunway1().getTORA() + ") + Clearway (" + selectedRunway.getRunway1().getClearway() + ") = " +  selectedRunway.getRunway1().getASDA() + "\n" +
                    "• TODA = Recalculated TORA (" + selectedRunway.getRunway1().getTORA() + ") + Clearway (" + selectedRunway.getRunway1().getClearway() + ") = " +  selectedRunway.getRunway1().getASDA() + "\n" +

                    "Landing Over: \n " +
                    "• LDA = Original LDA (" + selectedRunway.getRunway1().getOriginalLDA() + ") - Distance from threshold (" +
                    selectedObstacle.getDistToThreshold1() + ") - Strip end (" + Runway.getStripEnd() + ") " +
                    "- Slope calculation (" + selectedRunway.getRunway1().getSlopeCalc() + ") = " + selectedRunway.getRunway1().getLDA() + "\n   " +


                    "\nRUNWAY 2 (" + selectedRunway.getRunway2().getID() + "): \n" +
                    "Take Off Towards: \n" +
                    "• TORA = Distance from threshold " + selectedObstacle.getDistToThreshold1() +
                    "- Slope calculation (" + selectedRunway.getRunway2().getSlopeCalc() + ") " +
                    "- Strip end (" + Runway.getStripEnd() + ") = " + selectedRunway.getRunway2().getTORA() +"\n"+

                    "• ASDA = Recalculated TORA (" + selectedRunway.getRunway2().getTORA() + ") " +
                    " + Clearway (" + selectedRunway.getRunway2().getClearway() + ") = " + selectedRunway.getRunway2().getASDA() + "\n" +

                    "• TODA = Recalculated TORA (" + selectedRunway.getRunway2().getTORA() + ") " +
                    " + Clearway (" + selectedRunway.getRunway2().getClearway() + ") = " + selectedRunway.getRunway2().getTODA() + "\n" +

                    "Landing Towards: \n" +
                    "• LDA = Distance from threshold " + selectedObstacle.getDistToThreshold2() +
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

    public void handleRemoveObsButtonAction(ActionEvent event)
    {
        obstacleSelection.getValue().setDistToCentreline(0);
        obstacleSelection.getValue().setDistToThreshold1(0);
        obstacleSelection.getValue().setDistToThreshold2(0);

        obstacleTop.setVisible(false);
        obstacleSide.setVisible(false);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
        setSystemLogText(sdf.format(cal.getTime()) + " Obstacle " + "\"" + selectedObstacle.getName() + "\" removed from runway.");

        statusLabel1.setText("FREE");
        statusLabel2.setText("FREE");

        RESA.setVisible(false);
        LDALine1.setEndX(737);
        LDAArr11.setLayoutX(737);
        LDAArr21.setLayoutX(737);
        thresholdLineLDA21.setLayoutX(737);
        TORALine1.setEndX(737);
        TORAArr11.setLayoutX(737);
        TORAArr21.setLayoutX(737);
        thresholdLineTORA21.setLayoutX(737);
        TORALine2.setEndX(737);
        TORAArr12.setLayoutX(737);
        TORAArr22.setLayoutX(737);
        thresholdLineTORA22.setLayoutX(737);
        thresholdLineASDA12.setLayoutX(0);
        ASDAText2.setLayoutX(0);
        ASDALine2.setEndX(737);
        thresholdLineTODA12.setLayoutX(0);
        TODAText2.setLayoutX(0);
        TODALine2.setEndX(735);

        thresholdLineTORA11.setLayoutX(0);
        TORAText1.setLayoutX(0);
        TORALine1.setStartX(40);
        thresholdLineASDA11.setLayoutX(0);
        ASDAText1.setLayoutX(0);
        ASDALine1.setStartX(40);
        thresholdLineTODA11.setLayoutX(0);
        TODAText1.setLayoutX(0);
        TODALine1.setStartX(40);
        thresholdLineTORA12.setLayoutX(0);
        TORAText2.setLayoutX(0);
        TORALine2.setStartX(40);

        sideLineLDA1.setEndX(720);
        sideArrLDA11.setLayoutX(720);
        sideArrLDA21.setLayoutX(720);
        sideThresholdLDA21.setLayoutX(720);
        sideLineTORA1.setEndX(720);
        sideArrTORA11.setLayoutX(720);
        sideArrTORA21.setLayoutX(720);
        sideThresholdTORA21.setLayoutX(720);

        sideThresholdLDA12.setLayoutX(720);
        sideTextLDA2.setLayoutX(694);
        sideLineLDA2.setEndX(690);
        sideThresholdTORA12.setLayoutX(720);
        sideTextTORA2.setLayoutX(686);
        sideLineTORA2.setEndX(682);
        sideThresholdASDA12.setLayoutX(740);
        sideTextASDA2.setLayoutX(706);
        sideLineASDA2.setEndX(702);
        sideThresholdTODA12.setLayoutX(750);
        sideTextTODA2.setLayoutX(716);
        sideLineTODA2.setEndX(712);

        sideThresholdLDA11.setLayoutX(0);
        sideTextLDA1.setLayoutX(0);
        sideLineLDA1.setStartX(28);
        sideThresholdTORA11.setLayoutX(0);
        sideTextTORA1.setLayoutX(0);
        sideLineTORA1.setStartX(36);
        sideThresholdASDA11.setLayoutX(0);
        sideTextASDA1.setLayoutX(0);
        sideLineASDA1.setStartX(36);
        sideThresholdTODA11.setLayoutX(0);
        sideTextTODA1.setLayoutX(0);
        sideLineTODA1.setStartX(36);
        
        sideLineLDA2.setStartX(0);
        sideArrLDA12.setLayoutX(0);
        sideArrLDA22.setLayoutX(0);
        sideThresholdLDA22.setLayoutX(0);
        sideLineTORA2.setStartX(0);
        sideArrTORA12.setLayoutX(0);
        sideArrTORA22.setLayoutX(0);
        sideThresholdTORA22.setLayoutX(0);
        LDALine2.setStartX(0);
        LDAArr12.setLayoutX(0);
        LDAArr22.setLayoutX(0);
        thresholdLineLDA22.setLayoutX(0);
        thresholdLineLDA12.setLayoutX(0);
        LDAText2.setLayoutX(0);
        LDALine2.setEndX(707);

        runwaySelected();
    }

    @FXML
    public void initialize(){
        logPane.vvalueProperty().bind(logVBox.heightProperty());
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
        removeObs.setOnAction(this::handleRemoveObsButtonAction);

        runwaySelected();
        setSelectedObstacle();
    }

    private void createRunwaysList(){
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

    private void loadObstaclesList(){
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
    
    public void runwaySelected() {
        PhysicalRunway runway = runwaySelection.getValue();
        RESA.setVisible(false);
        Runway runway1 = runway.getRunway1();
        Runway runway2 = runway.getRunway2();

        runway1Label.setText(runway.getRunway1().getID() + " status: ");
        runway2Label.setText(runway.getRunway2().getID() + " status: ");

        if (runway1.getOriginalLDA()>runway2.getOriginalLDA()) {
            runway.switchRunways();
            Runway temp = runway1;
            runway1 = runway2;
            runway2 = temp;
        }

        if (runway1.getClearway()>0) {
            clearway2.setVisible(true);
            sideClearway2.setVisible(true);
            TODALine1.setEndX(767);
            TODAArr11.setLayoutX(767);
            TODAArr21.setLayoutX(767);
            thresholdLineTODA21.setLayoutX(767);
            sideLineTODA1.setEndX(750);
            sideArrTODA11.setLayoutX(750);
            sideArrTODA21.setLayoutX(750);
            sideThresholdTODA21.setLayoutX(750);
        } else {
            clearway2.setVisible(false);
            sideClearway2.setVisible(false);
            TODALine1.setEndX(737);
            TODAArr11.setLayoutX(737);
            TODAArr21.setLayoutX(737);
            thresholdLineTODA21.setLayoutX(737);
            sideLineTODA1.setEndX(720);
            sideArrTODA11.setLayoutX(720);
            sideArrTODA21.setLayoutX(720);
            sideThresholdTODA21.setLayoutX(720);
        }

        if (runway1.getStopway()>0) {
            stopway2.setVisible(true);
            sideStopway2.setVisible(true);
            ASDALine1.setEndX(757);
            ASDAArr11.setLayoutX(757);
            ASDAArr21.setLayoutX(757);
            thresholdLineASDA21.setLayoutX(757);
            sideLineASDA1.setEndX(740);
            sideArrASDA11.setLayoutX(740);
            sideArrASDA21.setLayoutX(740);
            sideThresholdASDA21.setLayoutX(740);
        } else {
            stopway2.setVisible(false);
            sideStopway2.setVisible(false);
            ASDALine1.setEndX(737);
            ASDAArr11.setLayoutX(737);
            ASDAArr21.setLayoutX(737);
            thresholdLineASDA21.setLayoutX(737);
            sideLineASDA1.setEndX(720);
            sideArrASDA11.setLayoutX(720);
            sideArrASDA21.setLayoutX(720);
            sideThresholdASDA21.setLayoutX(720);
        }

        if (runway2.getClearway()>0) {
            clearway1.setVisible(true);
            sideClearway1.setVisible(true);
            TODALine2.setStartX(12);
            TODAArr12.setLayoutX(0);
            TODAArr22.setLayoutX(0);
            thresholdLineTODA22.setLayoutX(0);
            sideLineTODA2.setStartX(0);
            sideArrTODA12.setLayoutX(0);
            sideArrTODA22.setLayoutX(0);
            sideThresholdTODA22.setLayoutX(0);
        } else {
            clearway1.setVisible(false);
            sideClearway1.setVisible(false);
            TODALine2.setStartX(42);
            TODAArr12.setLayoutX(30);
            TODAArr22.setLayoutX(30);
            thresholdLineTODA22.setLayoutX(30);
            sideLineTODA2.setStartX(30);
            sideArrTODA12.setLayoutX(30);
            sideArrTODA22.setLayoutX(30);
            sideThresholdTODA22.setLayoutX(30);
        }

        if (runway2.getStopway()>0) {
            stopway1.setVisible(true);
            sideStopway1.setVisible(true);
            ASDALine2.setStartX(22);
            ASDAArr12.setLayoutX(0);
            ASDAArr22.setLayoutX(0);
            thresholdLineASDA22.setLayoutX(0);
            sideLineASDA2.setStartX(0);
            sideArrASDA12.setLayoutX(0);
            sideArrASDA22.setLayoutX(0);
            sideThresholdASDA22.setLayoutX(0);
        } else {
            stopway1.setVisible(false);
            sideStopway1.setVisible(false);
            ASDALine2.setStartX(42);
            ASDAArr12.setLayoutX(20);
            ASDAArr22.setLayoutX(20);
            thresholdLineASDA22.setLayoutX(20);
            sideLineASDA2.setStartX(20);
            sideArrASDA12.setLayoutX(20);
            sideArrASDA22.setLayoutX(20);
            sideThresholdASDA22.setLayoutX(20);
        }

        int dt = runway1.getDisplacedThreshold();
        if (dt > 0) {
            disThr1.setVisible(true);
            sideDT1.setVisible(true);
            int x = 740 * dt / runway1.getOriginalTORA();
            disThr1.setLayoutX(x);
            sideDT1.setLayoutX(80+x);
            thresholdLineLDA11.setLayoutX(x);
            sideThresholdLDA11.setLayoutX(x);
            LDAText1.setLayoutX(x);
            sideTextLDA1.setLayoutX(x);
            LDALine1.setStartX(32 + x);
            sideLineLDA1.setStartX(32 + x);
        } else {
            disThr1.setVisible(false);
            sideDT1.setVisible(false);
        }
        dt = runway2.getDisplacedThreshold();
        if (dt > 0) {
            disThr2.setVisible(true);
            sideDT2.setVisible(true);
            int x = 740 * dt / runway2.getOriginalTORA();
            disThr2.setLayoutX(-x);
            sideDT2.setLayoutX(-x);
            thresholdLineLDA12.setLayoutX(x);
            sideThresholdLDA12.setLayoutX(x);
            LDAText2.setLayoutX(x);
            sideTextLDA2.setLayoutX(x);
            LDALine2.setStartX(32 + x);
            sideLineLDA2.setStartX(-x);
        } else {
            disThr2.setVisible(false);
            sideDT2.setVisible(false);
        }


        char[] runwayNames = runway.getName().toCharArray();
        runwayLabel1.setText(Character.toString(runwayNames[0])+Character.toString(runwayNames[1]));
        textNum1.setText(Character.toString(runwayNames[0])+Character.toString(runwayNames[1]));
        runwayLabel1L.setText(Character.toString(runwayNames[2]));
        textLet1.setText(Character.toString(runwayNames[2]));
        runwayLabel2.setText(Character.toString(runwayNames[4])+Character.toString(runwayNames[5]));
        textNum2.setText(Character.toString(runwayNames[4])+Character.toString(runwayNames[5]));
        runwayLabel2L.setText(Character.toString(runwayNames[6]));
        textLet2.setText(Character.toString(runwayNames[6]));
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

    public void zoom() {
        runwayGroup.setScaleX(zoomSlider.getValue()/50);
        runwayGroup.setScaleY(zoomSlider.getValue()/50);
    }

    private double x,y;

    public void move(MouseEvent mouseEvent) {
        runwayGroup.setLayoutX(mouseEvent.getX()-x);
        runwayGroup.setLayoutY(mouseEvent.getY()-y);
    }

    public void getPos(MouseEvent mouseEvent) {
        x = mouseEvent.getX()-runwayGroup.getLayoutX();
        y = mouseEvent.getY()-runwayGroup.getLayoutY();
    }

    public void scroll(ScrollEvent scrollEvent) {
        zoomSlider.setValue(zoomSlider.getValue()+(scrollEvent.getDeltaY()/10));
        zoom();
    }

    void addObs(Obstacle obs){
        obstacles.add(obs);
        obstacleSelection.getItems().add(obs);
    }

    Obstacle getObstacleFromComboBox()
    {
        return obstacleSelection.getValue();
    }

    public void resetView(ActionEvent actionEvent) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
        setSystemLogText(sdf.format(cal.getTime()) + " View reset.");

        runwayGroup.setScaleX(1);
        runwayGroup.setScaleY(1);
        runwayGroup.setLayoutX(0);
        runwayGroup.setLayoutY(0);
        runwayGroup.setRotate(0);
        compass.setRotate(0);
        zoomSlider.setValue(1);
        rotationSlider.setValue(0);
    }

    void showObstacle() {
        int dtt1 = selectedObstacle.getDistToThreshold1();
        int dtt2 = selectedObstacle.getDistToThreshold2();
        int dtc = selectedObstacle.getDistToCentreline();
        if (dtt1>-100 && dtt2>-100 && -150<dtc && dtc<150) {
            Runway runway1 = selectedRunway.getRunway1();
            Runway runway2 = selectedRunway.getRunway2();
            obstacleTop.setVisible(true);
            obstacleSide.setVisible(true);
            int x1 = dtt1 * 740 / runway1.getOriginalTORA();
            int x2 = dtt2 * 740 / runway2.getOriginalTORA();
            obstacleTop.setLayoutX(x1);
            obstacleTop.setWidth(740 - x2 - x1);
            obstacleTop.setLayoutY(dtc / 3.0 + 15);
            obstacleSide.setLayoutX(x1);
            obstacleSide.setWidth(740 - x2 - x1);
            double h = selectedObstacle.getHeight() / 1.5;
            obstacleSide.setHeight(h);
            obstacleSide.setLayoutY(-h);
            if (x1 > x2) {
                int x = x1 - 75;
                RESA.setVisible(true);
                RESA.setLayoutX(x + 15);
                LDALine1.setEndX(x);
                LDAArr11.setLayoutX(x);
                LDAArr21.setLayoutX(x);
                thresholdLineLDA21.setLayoutX(x);
                TORALine1.setEndX(x);
                TORAArr11.setLayoutX(x);
                TORAArr21.setLayoutX(x);
                thresholdLineTORA21.setLayoutX(x);
                ASDALine1.setEndX(x);
                ASDAArr11.setLayoutX(x);
                ASDAArr21.setLayoutX(x);
                thresholdLineASDA21.setLayoutX(x);
                TODALine1.setEndX(x);
                TODAArr11.setLayoutX(x);
                TODAArr21.setLayoutX(x);
                thresholdLineTODA21.setLayoutX(x);

                sideLineLDA1.setEndX(x);
                sideArrLDA11.setLayoutX(x);
                sideArrLDA21.setLayoutX(x);
                sideThresholdLDA21.setLayoutX(x);
                sideLineTORA1.setEndX(x);
                sideArrTORA11.setLayoutX(x);
                sideArrTORA21.setLayoutX(x);
                sideThresholdTORA21.setLayoutX(x);
                sideLineASDA1.setEndX(x);
                sideArrASDA11.setLayoutX(x);
                sideArrASDA21.setLayoutX(x);
                sideThresholdASDA21.setLayoutX(x);
                sideLineTODA1.setEndX(x);
                sideArrTODA11.setLayoutX(x);
                sideArrTODA21.setLayoutX(x);
                sideThresholdTODA21.setLayoutX(x);

                thresholdLineLDA12.setLayoutX(x - 737);
                LDAText2.setLayoutX(x - 737);
                LDALine2.setEndX(x - 32);

                TORALine2.setEndX(x);
                TORAArr12.setLayoutX(x);
                TORAArr22.setLayoutX(x);
                thresholdLineTORA22.setLayoutX(x);

                thresholdLineASDA12.setLayoutX(x - 737);
                ASDAText2.setLayoutX(x - 737);
                ASDALine2.setEndX(x);
                thresholdLineTODA12.setLayoutX(x - 737);
                TODAText2.setLayoutX(x - 737);
                TODALine2.setEndX(x);

                sideThresholdLDA12.setLayoutX(x);
                sideTextLDA2.setLayoutX(x - 26);
                sideLineLDA2.setEndX(x - 30);
                sideThresholdTORA12.setLayoutX(x);
                sideTextTORA2.setLayoutX(x - 34);
                sideLineTORA2.setEndX(x - 38);
                sideThresholdASDA12.setLayoutX(x + 20);
                sideTextASDA2.setLayoutX(x - 14);
                sideLineASDA2.setEndX(x - 18);
                sideThresholdTODA12.setLayoutX(x + 30);
                sideTextTODA2.setLayoutX(x - 4);
                sideLineTODA2.setEndX(x - 8);
            } else {
                x = 740 - x2 + 75;
                RESA.setVisible(true);
                RESA.setLayoutX(x - 75);
                thresholdLineLDA11.setLayoutX(x);
                LDAText1.setLayoutX(x);
                LDALine1.setStartX(32 + x);
                thresholdLineTORA11.setLayoutX(x);
                TORAText1.setLayoutX(x);
                TORALine1.setStartX(32 + x);
                thresholdLineASDA11.setLayoutX(x);
                ASDAText1.setLayoutX(x);
                ASDALine1.setStartX(32 + x);
                thresholdLineTODA11.setLayoutX(x);
                TODAText1.setLayoutX(x);
                TODALine1.setStartX(32 + x);

                thresholdLineTORA12.setLayoutX(x);
                TORAText2.setLayoutX(x);
                TORALine2.setStartX(32 + x);
                ASDALine2.setStartX(x + 42);
                ASDAArr12.setLayoutX(x + 20);
                ASDAArr22.setLayoutX(x + 20);
                thresholdLineASDA22.setLayoutX(x + 20);
                TODALine2.setStartX(x + 42);
                TODAArr12.setLayoutX(x + 30);
                TODAArr22.setLayoutX(x + 30);
                thresholdLineTODA22.setLayoutX(x + 30);

                sideThresholdLDA11.setLayoutX(x);
                sideTextLDA1.setLayoutX(x);
                sideLineLDA1.setStartX(32 + x);
                sideThresholdTORA11.setLayoutX(x);
                sideTextTORA1.setLayoutX(x);
                sideLineTORA1.setStartX(32 + x);
                sideThresholdASDA11.setLayoutX(x);
                sideTextASDA1.setLayoutX(x);
                sideLineASDA1.setStartX(32 + x);
                sideThresholdTODA11.setLayoutX(x);
                sideTextTODA1.setLayoutX(x);
                sideLineTODA1.setStartX(32 + x);
                sideLineLDA2.setStartX(x);
                sideArrLDA12.setLayoutX(x);
                sideArrLDA22.setLayoutX(x);
                sideThresholdLDA22.setLayoutX(x);
                sideLineTORA2.setStartX(x);
                sideArrTORA12.setLayoutX(x);
                sideArrTORA22.setLayoutX(x);
                sideThresholdTORA22.setLayoutX(x);
                sideLineASDA2.setStartX(x + 20);
                sideArrASDA12.setLayoutX(x + 20);
                sideArrASDA22.setLayoutX(x + 20);
                sideThresholdASDA22.setLayoutX(x + 20);
                sideLineTODA2.setStartX(x + 30);
                sideArrTODA12.setLayoutX(x + 30);
                sideArrTODA22.setLayoutX(x + 30);
                sideThresholdTODA22.setLayoutX(x + 30);

                LDALine2.setStartX(x);
                LDAArr12.setLayoutX(x);
                LDAArr22.setLayoutX(x);
                thresholdLineLDA22.setLayoutX(x);
            }
        }
    }

    @FXML
    public void saveLog(ActionEvent ae)
        {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                saveTextToFile(systemLog.getText(), file);
            }
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setSystemLogText(String message) {
        systemLog.setText(systemLog.getText() + "\n" + message);
    }


    public PhysicalRunway getSelectedRunway() {
        return selectedRunway;
    }

    public Obstacle getSelectedObstacle() {
        return selectedObstacle;
    }

    @FXML
    public void resetLog(ActionEvent ae) {
        systemLog.setText("SYSTEM LOG");
    }

    public void setController(SelectAirportController controller)
    {
        this.controller = controller;
    }

    public void setAirportName(Label name)
    {
        this.airportName.setText(name.getText());
    }

    public Rectangle obstacleSide;
    public Group sideDT1;
    public Group sideDT2;
    public Line sideThresholdLDA11;
    public Text sideTextLDA1;
    public Line sideLineLDA1;
    public Line sideArrLDA11;
    public Line sideArrLDA21;
    public Line sideThresholdLDA21;
    public Line sideThresholdLDA12;
    public Text sideTextLDA2;
    public Line sideLineLDA2;
    public Line sideArrLDA12;
    public Line sideArrLDA22;
    public Line sideThresholdLDA22;
    public Line sideThresholdTORA11;
    public Text sideTextTORA1;
    public Line sideLineTORA1;
    public Line sideArrTORA11;
    public Line sideArrTORA21;
    public Line sideThresholdTORA21;
    public Line sideThresholdTORA12;
    public Text sideTextTORA2;
    public Line sideLineTORA2;
    public Line sideArrTORA12;
    public Line sideArrTORA22;
    public Line sideThresholdTORA22;
    public Line sideThresholdASDA11;
    public Text sideTextASDA1;
    public Line sideLineASDA1;
    public Line sideArrASDA11;
    public Line sideArrASDA21;
    public Line sideThresholdASDA21;
    public Line sideThresholdASDA12;
    public Text sideTextASDA2;
    public Line sideLineASDA2;
    public Line sideArrASDA12;
    public Line sideArrASDA22;
    public Line sideThresholdASDA22;
    public Line sideThresholdTODA11;
    public Text sideTextTODA1;
    public Line sideLineTODA1;
    public Line sideArrTODA11;
    public Line sideArrTODA21;
    public Line sideThresholdTODA21;
    public Line sideThresholdTODA12;
    public Text sideTextTODA2;
    public Line sideLineTODA2;
    public Line sideArrTODA12;
    public Line sideArrTODA22;
    public Line sideThresholdTODA22;
    public Text textNum1;
    public Text textLet1;
    public Text textNum2;
    public Text textLet2;
    public Label originalLDA1;
    public Label originalLDA2;
    public Label originalTORA1;
    public Label originalTORA2;
    public Label originalTODA1;
    public Label originalTODA2;
    public Label originalASDA1;
    public Label originalASDA2;
    public Label originalDT1;
    public Label originalDT2;
    public Label originalStopway1;
    public Label originalClearway1;
    public Label originalStopway2;
    public Label originalClearway2;
    public Label recalcLDA1;
    public Label recalcLDA2;
    public Label recalcTORA1;
    public Label recalcTORA2;
    public Label recalcTODA1;
    public Label recalcTODA2;
    public Label recalcASDA1;
    public Label recalcASDA2;
    public Line sideClearway1;
    public Line sideClearway2;
    public Line sideStopway1;
    public Line sideStopway2;
    public Text runwayLabel1;
    public Text runwayLabel2;
    public Text runwayLabel1L;
    public Text runwayLabel2L;
    public Group runwayG;
    public Slider rotationSlider;
    public Group runwayGroup;
    public Slider zoomSlider;
    public Group RESA;
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
    public Rectangle obstacleTop;
    public Button plusButton;
    public Button addObsButton;
    public Button resetViewButton;
    public Button brekdownButton;
    public ComboBox<PhysicalRunway> runwaySelection;
    public ComboBox<Obstacle> obstacleSelection;
    public TextArea warningBox = new TextArea();
    public Label calculationsLabel;
    public Button removeObs;
    public Label statusLabel1;
    public Label statusLabel2;
    public Label runway1Label;
    public Label runway2Label;
    public Label systemLog;
    public Button resetLogButton;
    public ScrollPane logPane;
    public VBox logVBox;
    public Button saveLogButton;
}
