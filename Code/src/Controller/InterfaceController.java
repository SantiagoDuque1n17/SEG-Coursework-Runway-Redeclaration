package Controller;
import Model.PhysicalRunway;
import Model.Obstacle;
import Model.Runway;
import Model.Airport;
import Exceptions.DontNeedRedeclarationException;
import Exceptions.NegativeParameterException;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterfaceController
{
    private Airport airport = null;
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
    private Button helpButton;

    @FXML
    public Obstacle setSelectedObstacle() {
        Obstacle obstacle = obstacleSelection.getValue();
        selectedObstacle = obstacle;
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
        setSystemLogText(sdf.format(cal.getTime()) + " Selected obstacle: " + selectedObstacle.getName());
        
        return obstacle;
    }

    public void setAirport (Airport airport){
        this.airport = airport;
        if(!airport.getId().equals("0")){
            runwaySelection.getItems().removeAll(runwaySelection.getItems());
            runwaySelection.getItems().addAll(airport.getRunways());
            runwaySelection.getSelectionModel().select(airport.getRunways().get(0));
            runways = runwaySelection.getItems();
            runwaySelected();
        }
    }

    public void handleHelpButton(ActionEvent event)
    {
        File file = new File("./UserManual.pdf");
        HostServices hostServices = controller.getHostServices();
        hostServices.showDocument(file.getAbsolutePath());
    }

    public void importButtonAction (ActionEvent event){
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        if(selectedFile != null){
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder documentBuilder;
            try {
                documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(selectedFile);
                NodeList obstaclesList = document.getElementsByTagName("Obstacle");

                for(int i = 0; i<obstaclesList.getLength(); i ++){
                    Element obstacle = (Element)obstaclesList.item(i);
                    String name = obstacle.getElementsByTagName("Name").item(0).getTextContent();
                    int height = Integer.parseInt(obstacle.getElementsByTagName("Height").item(0).getTextContent());
                    Obstacle obstacle1 = new Obstacle(name,height,0,0,0);
                    obstacles.add(obstacle1);
                    obstacleSelection.getItems().add(obstacle1);

                }
                obstacleSelection.getSelectionModel().select(obstacleSelection.getItems().get(0));
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        }else{

        }
    }

    public Integer counter = 1;
    public void exportButtonAction(ActionEvent event){
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            Document document = documentBuilder.newDocument();

            Element root = document.createElement("Airport");
            document.appendChild(root);

            Attr airportName = document.createAttribute("Name");
            airportName.setValue(airport.getName());
            root.setAttributeNode(airportName);

            Element runwayList = document.createElement("RunwayList");

            root.appendChild(runwayList);


            for(PhysicalRunway runwayIt : runwaySelection.getItems()) {

                Element physicalRunway = document.createElement("PhysicalRunway");
                runwayList.appendChild(physicalRunway);

                Element logicalRunway = document.createElement("LogicalRunway");
                physicalRunway.appendChild(logicalRunway);

                Element id = document.createElement("ID");
                id.appendChild(document.createTextNode(runwayIt.getRunway1().getID()));
                logicalRunway.appendChild(id);

                Element lda = document.createElement("LDA");
                lda.appendChild(document.createTextNode(Integer.toString(runwayIt.getRunway1().getLDA())));
                logicalRunway.appendChild(lda);

                Element tora = document.createElement("TORA");
                tora.appendChild(document.createTextNode(Integer.toString(runwayIt.getRunway1().getTORA())));
                logicalRunway.appendChild(tora);

                Element toda = document.createElement("TODA");
                toda.appendChild(document.createTextNode(Integer.toString(runwayIt.getRunway1().getTODA())));
                logicalRunway.appendChild(toda);

                Element asda = document.createElement("ASDA");
                asda.appendChild(document.createTextNode(Integer.toString(runwayIt.getRunway1().getASDA())));
                logicalRunway.appendChild(asda);

                Element displacedThreshold = document.createElement("Displaced_Threshold");
                displacedThreshold.appendChild(document.createTextNode(Integer.toString(runwayIt.getRunway1().getDisplacedThreshold())));
                logicalRunway.appendChild(displacedThreshold);

                logicalRunway = document.createElement("LogicalRunway");
                physicalRunway.appendChild(logicalRunway);

                id = document.createElement("ID");
                id.appendChild(document.createTextNode(runwayIt.getRunway2().getID()));
                logicalRunway.appendChild(id);

                lda = document.createElement("LDA");
                lda.appendChild(document.createTextNode(Integer.toString(runwayIt.getRunway2().getLDA())));
                logicalRunway.appendChild(lda);

                tora = document.createElement("TORA");
                tora.appendChild(document.createTextNode(Integer.toString(runwayIt.getRunway2().getTORA())));
                logicalRunway.appendChild(tora);

                toda = document.createElement("TODA");
                toda.appendChild(document.createTextNode(Integer.toString(runwayIt.getRunway2().getTODA())));
                logicalRunway.appendChild(toda);

                asda = document.createElement("ASDA");
                asda.appendChild(document.createTextNode(Integer.toString(runwayIt.getRunway2().getASDA())));
                logicalRunway.appendChild(asda);

                displacedThreshold = document.createElement("Displaced_Threshold");
                displacedThreshold.appendChild(document.createTextNode(Integer.toString(runwayIt.getRunway2().getDisplacedThreshold())));
                logicalRunway.appendChild(displacedThreshold);

                if(runwayIt.getObstacle() != null){
                    Element obstacleOnRunway = document.createElement("Obstacle_on_Runway");
                    physicalRunway.appendChild(obstacleOnRunway);

                    Element obsName = document.createElement("Name");
                    obsName.appendChild(document.createTextNode(runwayIt.getObstacle().getName()));
                    obstacleOnRunway.appendChild(obsName);

                    Element obsHeight = document.createElement("Height");
                    obsHeight.appendChild(document.createTextNode(Integer.toString(runwayIt.getObstacle().getHeight())));
                    obstacleOnRunway.appendChild(obsHeight);

                    Element obsDistToCentreline = document.createElement("Distance_to_centreline");
                    obsDistToCentreline.appendChild(document.createTextNode(Integer.toString(runwayIt.getObstacle().getDistToCentreline())));
                    obstacleOnRunway.appendChild(obsDistToCentreline);

                    Element obsDistToLeftThreshold = document.createElement("Distance_to_left_threshold");
                    obsDistToLeftThreshold.appendChild(document.createTextNode(Integer.toString(runwayIt.getObstacle().getDistToThreshold1())));
                    obstacleOnRunway.appendChild(obsDistToLeftThreshold);

                    Element obsDistToRightThreshold = document.createElement("Distance_to_right_threshold");
                    obsDistToRightThreshold.appendChild(document.createTextNode(Integer.toString(runwayIt.getObstacle().getDistToThreshold2())));
                    obstacleOnRunway.appendChild(obsDistToRightThreshold);
                }
            }


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer tr = transformerFactory.newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("System_" + counter + ".xml"));
            counter++;

            tr.transform(domSource, streamResult);

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }


    public void executeCalculation() {
        System.out.println("Selected runway: " + selectedRunway.getName());
        System.out.println("Selected obstacle: " + selectedObstacle.getName());

        int whichRunway = 0;

        try {
            if (selectedObstacle.getName().equals("default")) { selectedObstacle = obstacleSelection.getValue(); }
            whichRunway = selectedRunway.setObstacle(selectedObstacle);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
            setSystemLogText(sdf.format(cal.getTime()) + " Calculations executed");
            setSystemLogText(sdf.format(cal.getTime()) + " Runway 1 status: " + selectedRunway.getRunway1().getStatus());
            setSystemLogText(sdf.format(cal.getTime()) + " Runway 2 status: " + selectedRunway.getRunway2().getStatus());
            //setSystemLogText(sdf.format(cal.getTime()) + " Obstacle: " + selectedObstacle);
            showObstacle();
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
                    " - Displaced threshold (" + selectedRunway.getRunway1().getDisplacedThreshold() + ") " +
                    "- Slope calculation (" + selectedRunway.getRunway1().getSlopeCalc() + ") " +
                    "- Strip end (" + Runway.getStripEnd() + ") = " + selectedRunway.getRunway1().getTORA() +"\n"+

                    "• ASDA = Recalculated TORA (" + selectedRunway.getRunway1().getTORA() + ") = " + selectedRunway.getRunway1().getASDA() + "\n" +

                    "• TODA = Recalculated TORA (" + selectedRunway.getRunway1().getTORA() + ") = " + selectedRunway.getRunway1().getTODA() + "\n" +

                    "Landing Towards: \n" +
                    "• LDA = Distance from threshold " + selectedObstacle.getDistToThreshold1() +
                    " - RESA (" + Runway.getRESA() + ") - Strip End (" + Runway.getStripEnd() + ") = " + selectedRunway.getRunway1().getLDA() + "\n" +


                    "\nRUNWAY 2 (" + selectedRunway.getRunway2().getID() + "): \n " +
                    "Take Off Away: \n" +
                    "• TORA = Original TORA (" + selectedRunway.getRunway2().getOriginalTORA() + ") " +
                    "- Blast protection (" + Runway.getBlastProtection() + ") - Distance from threshold (" +
                    selectedObstacle.getDistToThreshold2() + ") - Displaced threshold (" + selectedRunway.getRunway2().getDisplacedThreshold() + ") = " + selectedRunway.getRunway2().getTORA() + "\n" +
                    "• ASDA = TORA (" + selectedRunway.getRunway2().getTORA() + ") + Stopway (" + selectedRunway.getRunway2().getStopway() + ") = " + selectedRunway.getRunway2().getASDA() + "\n" +
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
                    "• ASDA = Recalculated TORA (" + selectedRunway.getRunway1().getTORA() + ") + Stopway (" + selectedRunway.getRunway1().getStopway() + ") = " +  selectedRunway.getRunway1().getASDA() + "\n" +
                    "• TODA = Recalculated TORA (" + selectedRunway.getRunway1().getTORA() + ") + Clearway (" + selectedRunway.getRunway1().getClearway() + ") = " +  selectedRunway.getRunway1().getASDA() + "\n" +

                    "Landing Over: \n " +
                    "• LDA = Original LDA (" + selectedRunway.getRunway1().getOriginalLDA() + ") - Distance from threshold (" +
                    selectedObstacle.getDistToThreshold1() + ") - Strip end (" + Runway.getStripEnd() + ") " +
                    "- Slope calculation (" + selectedRunway.getRunway1().getSlopeCalc() + ") = " + selectedRunway.getRunway1().getLDA() + "\n   " +


                    "\nRUNWAY 2 (" + selectedRunway.getRunway2().getID() + "): \n" +
                    "Take Off Towards: \n" +
                    "• TORA = Distance from threshold " + selectedObstacle.getDistToThreshold2() +
                    " - Displaced threshold (" + selectedRunway.getRunway2().getDisplacedThreshold() + ") " +
                    "- Slope calculation (" + selectedRunway.getRunway2().getSlopeCalc() + ") " +
                    "- Strip end (" + Runway.getStripEnd() + ") = " + selectedRunway.getRunway2().getTORA() +"\n"+

                    "• ASDA = Recalculated TORA (" + selectedRunway.getRunway2().getTORA() + ") = " + selectedRunway.getRunway2().getASDA() + "\n" +

                    "• TODA = Recalculated TORA (" + selectedRunway.getRunway2().getTORA() + ") = " + selectedRunway.getRunway2().getTODA() + "\n" +

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

        try {
            runwaySelection.getValue().setObstacle(null);
        } catch (DontNeedRedeclarationException | NegativeParameterException e) {
            e.printStackTrace();
        }

        obstacleTop.setVisible(false);
        obstacleSide.setVisible(false);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
        setSystemLogText(sdf.format(cal.getTime()) + " Obstacle " + "\"" + selectedObstacle.getName() + "\" removed from runway.");

        statusLabel1.setText("FREE");
        statusLabel2.setText("FREE");

        RESATop.setVisible(false);
        RESASide.setVisible(false);
        slopeTop.setVisible(false);
        slopeSide.setVisible(false);
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

        sideLineLDA1.setEndX(740);
        sideArrLDA11.setLayoutX(740);
        sideArrLDA21.setLayoutX(740);
        sideThresholdLDA21.setLayoutX(740);
        sideLineTORA1.setEndX(740);
        sideArrTORA11.setLayoutX(740);
        sideArrTORA21.setLayoutX(740);
        sideThresholdTORA21.setLayoutX(740);

        sideThresholdLDA12.setLayoutX(740);
        sideTextLDA2.setLayoutX(714);
        sideLineLDA2.setEndX(710);
        sideThresholdTORA12.setLayoutX(740);
        sideTextTORA2.setLayoutX(706);
        sideLineTORA2.setEndX(702);
        sideThresholdASDA12.setLayoutX(760);
        sideTextASDA2.setLayoutX(726);
        sideLineASDA2.setEndX(722);
        sideThresholdTODA12.setLayoutX(770);
        sideTextTODA2.setLayoutX(736);
        sideLineTODA2.setEndX(732);

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

        helpButton.setOnAction(this::handleHelpButton);

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
        RESATop.setVisible(false);
        RESASide.setVisible(false);
        slopeTop.setVisible(false);
        slopeSide.setVisible(false);
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
            sideLineTODA1.setEndX(770);
            sideArrTODA11.setLayoutX(770);
            sideArrTODA21.setLayoutX(770);
            sideThresholdTODA21.setLayoutX(770);
        } else {
            clearway2.setVisible(false);
            sideClearway2.setVisible(false);
            TODALine1.setEndX(737);
            TODAArr11.setLayoutX(737);
            TODAArr21.setLayoutX(737);
            thresholdLineTODA21.setLayoutX(737);
            sideLineTODA1.setEndX(740);
            sideArrTODA11.setLayoutX(740);
            sideArrTODA21.setLayoutX(740);
            sideThresholdTODA21.setLayoutX(740);
        }

        if (runway1.getStopway()>0) {
            stopway2.setVisible(true);
            sideStopway2.setVisible(true);
            ASDALine1.setEndX(757);
            ASDAArr11.setLayoutX(757);
            ASDAArr21.setLayoutX(757);
            thresholdLineASDA21.setLayoutX(757);
            sideLineASDA1.setEndX(760);
            sideArrASDA11.setLayoutX(760);
            sideArrASDA21.setLayoutX(760);
            sideThresholdASDA21.setLayoutX(760);
        } else {
            stopway2.setVisible(false);
            sideStopway2.setVisible(false);
            ASDALine1.setEndX(737);
            ASDAArr11.setLayoutX(737);
            ASDAArr21.setLayoutX(737);
            thresholdLineASDA21.setLayoutX(737);
            sideLineASDA1.setEndX(740);
            sideArrASDA11.setLayoutX(740);
            sideArrASDA21.setLayoutX(740);
            sideThresholdASDA21.setLayoutX(740);
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
            sideDT1.setLayoutX(70+x);
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
        this.rotationDiff = (Character.getNumericValue(runwayNames[0])*10+Character.getNumericValue(runwayNames[1])-9)*10;
        rotate();

        selectedRunway = runwaySelection.getValue();
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss]");
        setSystemLogText(sdf.format(cal.getTime()) + " Selected runway: " + selectedRunway.getName());
        
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
        obstacleSide.setVisible(false);
        slope.setVisible(false);
    }

    private int rotationDiff;

    public void rotate() {
        double rotation = rotationSlider.getValue();
        runwayGroup.setRotate(rotation);
        compass.setRotate(rotation-rotationDiff);
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
        runwayGroup.setLayoutX(30);
        runwayGroup.setLayoutY(80);
        runwayGroup.setRotate(0);
        compass.setRotate(-rotationDiff);
        zoomSlider.setValue(1);
        rotationSlider.setValue(0);
    }

    void showObstacle() {
        PhysicalRunway runway = runwaySelection.getValue();
        obstacleTop.setVisible(true);
        obstacleSide.setVisible(true);
        slope.setVisible(true);
        slopeTop.setVisible(true);
        slopeSide.setVisible(true);
        RESATop.setVisible(true);
        RESASide.setVisible(true);
        double x1 = 740.0/runway.getRunway1().getOriginalTORA()*(runway.getObstacle().getDistToThreshold1()+runway.getRunway1().getDisplacedThreshold());
        double x2 = 740.0/runway.getRunway2().getOriginalTORA()*(runway.getObstacle().getDistToThreshold2()+runway.getRunway2().getDisplacedThreshold());
        obstacleTop.setLayoutY(-runway.getObstacle().getDistToCentreline() / 3.0 - Integer.signum(runway.getObstacle().getDistToCentreline())*10);
        double h = selectedObstacle.getHeight() / 1.5;
        obstacleSide.setHeight(h);
        obstacleSide.setLayoutY(-h);
        slope.setStartY(-h);
        double diff = h*15;
        if (x1 > x2) {
            obstacleSide.setLayoutX(x1);
            obstacleTop.setLayoutX(x1);
            slope.setStartX(x1);
            slope.setEndX(x1-diff);
            slopeTop.setLayoutX(x1 - diff);
            slopeSide.setLayoutX(x1 - diff);
            slopeTopLine.setEndX(diff);
            slopeSideLine.setEndX(diff);
            slopeTopArr1.setLayoutX(diff);
            slopeSideArr1.setLayoutX(diff);
            slopeTopArr2.setLayoutX(diff);
            slopeSideArr2.setLayoutX(diff);
            slopeTopText.setLayoutX(diff/2-20);
            slopeSideText.setLayoutX(diff/2-20);
            RESATop.setLayoutX(x1 - 48);
            RESASide.setLayoutX(x1 - 48);
            double x = x1 - 60;
            LDALine1.setEndX(x);
            LDAArr11.setLayoutX(x);
            LDAArr21.setLayoutX(x);
            thresholdLineLDA21.setLayoutX(x);
            sideLineLDA1.setEndX(x);
            sideArrLDA11.setLayoutX(x);
            sideArrLDA21.setLayoutX(x);
            sideThresholdLDA21.setLayoutX(x);

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
            sideThresholdTORA12.setLayoutX(x);
            sideTextTORA2.setLayoutX(x - 34);
            sideLineTORA2.setEndX(x - 38);
            sideThresholdASDA12.setLayoutX(x + 20);
            sideTextASDA2.setLayoutX(x - 14);
            sideLineASDA2.setEndX(x - 18);
            sideThresholdTODA12.setLayoutX(x + 30);
            sideTextTODA2.setLayoutX(x - 4);
            sideLineTODA2.setEndX(x - 8);
            if(diff>48)
                x = x1 - diff - 12;
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
            sideThresholdLDA12.setLayoutX(x);
            sideTextLDA2.setLayoutX(x - 26);
            sideLineLDA2.setEndX(x - 30);

        } else {
            obstacleSide.setLayoutX(x1-40);
            obstacleTop.setLayoutX(x1-40);
            slope.setStartX(x1);
            slope.setEndX(x1+diff);
            slopeTop.setLayoutX(x1);
            slopeSide.setLayoutX(x1);
            slopeTopLine.setEndX(diff);
            slopeSideLine.setEndX(diff);
            slopeTopArr1.setLayoutX(diff);
            slopeSideArr1.setLayoutX(diff);
            slopeTopArr2.setLayoutX(diff);
            slopeSideArr2.setLayoutX(diff);
            slopeTopText.setLayoutX(diff/2-20);
            slopeSideText.setLayoutX(diff/2-20);
            RESATop.setLayoutX(x1);
            RESASide.setLayoutX(x1);
            double x = x1 + diff + 12;
            if(diff<48)
                x = x1 +60;
            thresholdLineLDA11.setLayoutX(x);
            LDAText1.setLayoutX(x);
            LDALine1.setStartX(32 + x);
            sideThresholdLDA11.setLayoutX(x);
            sideTextLDA1.setLayoutX(x);
            sideLineLDA1.setStartX(32 + x);

            thresholdLineTORA12.setLayoutX(x);
            TORAText2.setLayoutX(x);
            TORALine2.setStartX(38 + x);
            ASDALine2.setStartX(x + 46);
            ASDAArr12.setLayoutX(x + 20);
            ASDAArr22.setLayoutX(x + 20);
            thresholdLineASDA22.setLayoutX(x + 20);
            TODALine2.setStartX(x + 46);
            TODAArr12.setLayoutX(x + 30);
            TODAArr22.setLayoutX(x + 30);
            thresholdLineTODA22.setLayoutX(x + 30);
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
            x = x1 + 60;
            thresholdLineTORA11.setLayoutX(x);
            TORAText1.setLayoutX(x);
            TORALine1.setStartX(38 + x);
            thresholdLineASDA11.setLayoutX(x);
            ASDAText1.setLayoutX(x);
            ASDALine1.setStartX(38 + x);
            thresholdLineTODA11.setLayoutX(x);
            TODAText1.setLayoutX(x);
            TODALine1.setStartX(40 + x);
            sideThresholdTORA11.setLayoutX(x);
            sideTextTORA1.setLayoutX(x);
            sideLineTORA1.setStartX(36 + x);
            sideThresholdASDA11.setLayoutX(x);
            sideTextASDA1.setLayoutX(x);
            sideLineASDA1.setStartX(36 + x);
            sideThresholdTODA11.setLayoutX(x);
            sideTextTODA1.setLayoutX(x);
            sideLineTODA1.setStartX(36 + x);

            LDALine2.setStartX(x);
            LDAArr12.setLayoutX(x);
            LDAArr22.setLayoutX(x);
            thresholdLineLDA22.setLayoutX(x);
            sideLineLDA2.setStartX(x);
            sideArrLDA12.setLayoutX(x);
            sideArrLDA22.setLayoutX(x);
            sideThresholdLDA22.setLayoutX(x);
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

    public void compassClicked(MouseEvent mouseEvent) {
        rotationSlider.setValue(rotationDiff);
        rotate();
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
    public Group RESATop;
    public Group RESASide;
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
    public Button importObsButton;
    public Button exportButton;
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
    public Line slope;
    public Group slopeTop;
    public Group slopeSide;
    public Line slopeTopLine;
    public Line slopeSideLine;
    public Line slopeTopArr1;
    public Line slopeSideArr1;
    public Line slopeTopArr2;
    public Line slopeSideArr2;
    public Text slopeTopText;
    public Text slopeSideText;
    public Pane viewPane, topView;

    public void saveViews(ActionEvent actionEvent) {
        final Rectangle rectangle = new Rectangle(880, 470);
        topView.setClip(rectangle);
        WritableImage img = new WritableImage(887, 812);
        viewPane.snapshot(null, img);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
            } catch (Exception ignored) {
            }
        }
    }
}
