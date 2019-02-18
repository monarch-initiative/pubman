package org.monarchinitiative.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.monarchinitiative.item.Item;
import org.monarchinitiative.pubmed.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private static final Logger logger = LogManager.getLogger();

    @FXML
    TextField pmidTextField;

    @FXML
    WebView mywebview;

    private WebEngine mywebengine;

    @FXML private CheckBox inHouseCB;
    @FXML private CheckBox resourceCB;
    @FXML private CheckBox clinicalUseCB;
    @FXML private CheckBox phenoGenoAlgCB;
    @FXML private CheckBox systemsBioAlgCB;


    private PubMedEntry currentPubMedEntry=null;

    private List<Item> itemList;

    private List<String> toBeFetchedList=null;


    public void initialize(URL url, ResourceBundle rb) {
        this.itemList = new ArrayList<>();
        this.toBeFetchedList = new ArrayList<>();
        ingestPubMedEntryList();
    }


    private void ingestPubMedEntryList() {

        try {
            URL url =  getClass().getClassLoader().getResource("data/hpo.citations");
            if (url==null) {
                System.err.println("Could not find path to citations file");
                System.exit(1);
            }


            String path = url.getPath();
            logger.trace("Reading existing items from {}", path);
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line=br.readLine())!=null) {
                if (line.startsWith("#")) continue; // comment
                Item item = Item.fromLine(line);
                itemList.add(item);
            }
        } catch (IOException e) {
            System.err.println("Could not read ingest file");
            e.printStackTrace();
            System.exit(1);
        }
        logger.trace("Got " + itemList.size() + " items");
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
        logger.trace("Save Menu Item Action");
        saveItems();
        e.consume();
    }


    @FXML private void exitMenuItemAction(ActionEvent e) {

        saveItems();
        e.consume();
    }


    private void saveItems() {
        try {
            URL url =  getClass().getClassLoader().getResource("data/hpo.citations");
            if (url==null) {
                System.err.println("Could not find path to citations file");
                System.exit(1);
            }
            String path = url.getPath();
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(Item.getHeaderLine() + "\n");
            for (Item item : this.itemList) {
                logger.trace("writing item {}", item.toLine());
                writer.write(item.toLine() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



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

    @FXML private void getPMID(ActionEvent e) {

        PubMedSummaryRetriever pmretriever = new PubMedSummaryRetriever();
        String userinput = this.pmidTextField.getText().trim();
        try {
            String summary = pmretriever.getSummary(userinput);

            this.currentPubMedEntry = PubMedParser.parsePubMed(summary);
        }catch (IOException ex) {
            ex.printStackTrace();
            this.currentPubMedEntry=null;
            return;
        } catch (PubMedParseException pmex) {
            pmex.printStackTrace();
            this.currentPubMedEntry=null;
            return;
        }
        updateWebview();
        e.consume();
    }


    @FXML private void addPMID(ActionEvent e) {
        boolean inhouse = this.inHouseCB.isSelected();
        boolean resource = this.resourceCB.isSelected();
        boolean clinical = this.clinicalUseCB.isSelected();
        boolean phenogeno = this.phenoGenoAlgCB.isSelected();
        boolean systemsBio = this.systemsBioAlgCB.isSelected();
        Item item = new Item(this.currentPubMedEntry,inhouse,resource,clinical,phenogeno,systemsBio);
        logger.trace("Adding item {}", item.toLine());
        this.itemList.add(item);
    }


    private void updateWebview() {
        logger.trace("updating webview, current pubmed entry is {}", currentPubMedEntry.toString());
        mywebview.setContextMenuEnabled(false);
        mywebengine = mywebview.getEngine();
        mywebengine.setUserDataDirectory(new File(PubManPlatform.getWebEngineUserDataDirectory(), getClass().getCanonicalName()));
        mywebengine.getLoadWorker().stateProperty().addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        System.out.println("oldValue: " + oldValue);
                        System.out.println("newValue: " + newValue);

                        if (newValue == Worker.State.SUCCEEDED) {
                            //document finished loading
                        }
                    }
                }
        );
        mywebengine.load(getCurrentPubMedHTML());

    }


    private String getCurrentPubMedHTML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<style type=\"text/css\">\n" +
                " span.bold-red {\n" +
                "    color: red;\n" +
                "    font-weight: bold;\n" +
                "}\n" +
                " span.blu {\n" +
                "    color: #4e89a4;\n" +
                "    font-weight: normal;\n" +
                "}\n" +
                "</style>");
        sb.append("<body><h1>Current Article</h1>");
        sb.append("<p>Current PMID citation.</p>");
        if (this.currentPubMedEntry==null) {
            sb.append("<p>Could not be parsed</p>");
        } else {
            sb.append("<p>").append(this.currentPubMedEntry.toString()).append("</p>");
        }
        sb.append("</body></html>");
        return sb.toString();
    }



    @FXML private void retrieveCiting(ActionEvent e) {
        if (this.currentPubMedEntry==null) {
            logger.error("Attempt to retrieve citations for null current entry");
            return;
        } else {
            logger.trace("Searching for citations for {}", this.currentPubMedEntry.getPmid());
        }
        CitationGrabber grabber = new CitationGrabber(this.currentPubMedEntry.getPmid());
        List<String> pmids = grabber.citingPMIDs();
        for (String s : pmids) {
            logger.trace("Got PMID of citing article \'" + s +"\'");
        }
        this.toBeFetchedList.addAll(pmids);
    }




}
