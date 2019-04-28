package Controller;

import Model.*;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class SelectAirportController
{
    @FXML
    private Button selectHeathrowButton;
    @FXML
    private Button importAirportButton;

    private HostServices hostServices;

    public void handleSelectedAirportButtonAction(ActionEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader((getClass().getResource("UserInterface.fxml")));
            Parent root = (Parent) fxmlLoader.load();

            InterfaceController controller = (InterfaceController) fxmlLoader.getController();
            controller.setController(this);
            controller.setAirport(new Airport("Heathrow Airport", "0"));
            controller.setAirportName(new Label("Heathrow Airport"));


            Stage stage = new Stage();
            stage.setTitle("Runway Re-declaration tool");
            stage.setScene(new Scene(root, 1280, 800));
            stage.setResizable(false);

            stage.show();
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }
        catch(Exception e)
        {
            System.out.println("Can't load new window");
        }
    }


    public void handleImportAirportAction(ActionEvent event) {
        try {
            FileChooser fc = new FileChooser();
            File selectedFile = fc.showOpenDialog(null);
            Airport airport = null;
            if (selectedFile != null) {

                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder documentBuilder;
                documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(selectedFile);

                airport = new Airport(document.getDocumentElement().getAttribute("Name"), "1");
                NodeList physicalRunways = document.getElementsByTagName("PhysicalRunway");

                for (int i = 0; i < physicalRunways.getLength(); i++) {
                    Element physicalRunway = (Element) physicalRunways.item(i);
                    NodeList logicalRunways = physicalRunway.getElementsByTagName("LogicalRunway");
                    Element logicalRunway = (Element) logicalRunways.item(0);
                    String ID = logicalRunway.getElementsByTagName("ID").item(0).getTextContent();
                    int LDA = Integer.parseInt(logicalRunway.getElementsByTagName("LDA").item(0).getTextContent());
                    int TORA = Integer.parseInt(logicalRunway.getElementsByTagName("TORA").item(0).getTextContent());
                    int TODA = Integer.parseInt(logicalRunway.getElementsByTagName("TODA").item(0).getTextContent());
                    int ASDA = Integer.parseInt(logicalRunway.getElementsByTagName("ASDA").item(0).getTextContent());
                    int displacedThreshold = Integer.parseInt(logicalRunway.getElementsByTagName("Displaced_Threshold").item(0).getTextContent());
                    Runway runway1 = new Runway(ID, LDA, TORA, TODA, ASDA, displacedThreshold);

                    logicalRunway = (Element) logicalRunways.item(1);

                    ID = logicalRunway.getElementsByTagName("ID").item(0).getTextContent();
                    LDA = Integer.parseInt(logicalRunway.getElementsByTagName("LDA").item(0).getTextContent());
                    TORA = Integer.parseInt(logicalRunway.getElementsByTagName("TORA").item(0).getTextContent());
                    TODA = Integer.parseInt(logicalRunway.getElementsByTagName("TODA").item(0).getTextContent());
                    ASDA = Integer.parseInt(logicalRunway.getElementsByTagName("ASDA").item(0).getTextContent());
                    displacedThreshold = Integer.parseInt(logicalRunway.getElementsByTagName("Displaced_Threshold").item(0).getTextContent());
                    Runway runway2 = new Runway(ID, LDA, TORA, TODA, ASDA, displacedThreshold);

                    PhysicalRunway runway = new PhysicalRunway(runway1, runway2);
                    airport.addRunway(runway);

                }
            }
            FXMLLoader fxmlLoader = new FXMLLoader((getClass().getResource("UserInterface.fxml")));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Runway Re-declaration tool");
            stage.setScene(new Scene(root, 1280, 800));
            stage.setResizable(false);

            InterfaceController controller = (InterfaceController) fxmlLoader.getController();
            controller.setController(this);
            controller.setAirport(airport);
            controller.setAirportName(new Label(airport.getName()));
            stage.show();
            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
            @FXML
    public void initialize()
    {
        selectHeathrowButton.setOnAction(this::handleSelectedAirportButtonAction);
        importAirportButton.setOnAction(this::handleImportAirportAction);
    }

    public HostServices getHostServices()
    {
        return hostServices;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
}
