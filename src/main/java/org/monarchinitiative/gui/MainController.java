package org.monarchinitiative.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final Logger logger = LogManager.getLogger();


    public void initialize(URL url, ResourceBundle rb) {

    }



    @FXML private void newMenuItemAction(ActionEvent e) {
        System.out.println("New Menu Item Action");

        e.consume();
    }

    @FXML private void openMenuItemAction(ActionEvent e) {
        System.out.println("Open Menu Item Action");

        e.consume();
    }

    @FXML private void saveMenuItemAction(ActionEvent e) {
        System.out.println("Save Menu Item Action");

        e.consume();
    }

    @FXML private void saveAsMenuItemAction(ActionEvent e) {
        System.out.println("SaveAs Menu Item Action");

        e.consume();
    }

    @FXML private void exitMenuItemAction(ActionEvent e) {
        System.out.println("Exit Menu Item Action");

        e.consume();
    }

    @FXML private void showEditCurrentPublicationMenuItemAction(ActionEvent e) {
        System.out.println("showEditCurrentPublicationMenuItemAction Menu Item Action");

        e.consume();
    }

    @FXML private void setResourcesMenuItemAction(ActionEvent e) {
        System.out.println("setResourcesMenuItemAction Menu Item Action");

        e.consume();
    }

    @FXML private void showCuratedPublicationsMenuItemAction(ActionEvent e) {
        System.out.println("showCuratedPublicationsMenuItemAction Menu Item Action");

        e.consume();
    }

    @FXML private void helpMenuItemAction(ActionEvent e) {
        System.out.println("help Menu Item Action");

        e.consume();
    }
}
