package org.monarchinitiative.gui;

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
import java.util.*;

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

    private Stack<String> toBeFetchedStack =null;
    /** Set of all PMIDs that are already in our database. Want to skip repeat entries! */
    private Set<String> currentSeenPmids;


    public void initialize(URL url, ResourceBundle rb) {
        this.itemList = new ArrayList<>();
        this.toBeFetchedStack = new Stack<>();
        this.currentSeenPmids = new HashSet<>();
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
        // Add the PubMed ids of all entries to our seen list
        itemList.stream().map(Item::getPmid).forEach( p -> this.currentSeenPmids.add(p));
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
        String userinput = this.pmidTextField.getText().trim();
        fetchPmid(userinput);
        updateWebview();
        e.consume();
    }


    private void fetchPmid(String pmid) {
        try {
            PubMedSummaryRetriever pmretriever = new PubMedSummaryRetriever();
            String summary  = pmretriever.getSummary(pmid);
            this.currentPubMedEntry = PubMedParser.parsePubMed(summary);

        } catch (PubMedParseException pmex) {
            pmex.printStackTrace();
            this.currentPubMedEntry=null;
            return;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            this.currentPubMedEntry=null;
            return;
        }
    }




    private String getAlreadyExistsWarning() {
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
            sb.append("<body><h2>warning</h2>");
        sb.append("<p>Article already present in our citation database. " +
                "It was not added a second time</p>");
        sb.append("</body></html>");
        return sb.toString();
    }



    @FXML private void addPMID(ActionEvent e) {
        boolean inhouse = this.inHouseCB.isSelected();
        boolean resource = this.resourceCB.isSelected();
        boolean clinical = this.clinicalUseCB.isSelected();
        boolean phenogeno = this.phenoGenoAlgCB.isSelected();
        boolean systemsBio = this.systemsBioAlgCB.isSelected();
        if (this.currentSeenPmids.contains(currentPubMedEntry.getPmid())) {
            updateWebview(getAlreadyExistsWarning());
        } else {
            this.currentSeenPmids.add(currentPubMedEntry.getPmid());
        }
        Item item = new Item(this.currentPubMedEntry,inhouse,resource,clinical,phenogeno,systemsBio);
        logger.trace("Adding item {}", item.toLine());
        this.itemList.add(item);
        updateWebview();
    }


    private void updateWebview() {
        logger.trace("updating webview, current pubmed entry is {}", currentPubMedEntry.toString());
        mywebview.setContextMenuEnabled(false);
        mywebengine = mywebview.getEngine();
        mywebengine.loadContent(getCurrentPubMedHTML());
        mywebengine.setUserDataDirectory(new File(PubManPlatform.getWebEngineUserDataDirectory(), getClass().getCanonicalName()));
    }

    private void updateWebview(String message) {
        logger.trace("updating webview, current pubmed entry is {}", currentPubMedEntry.toString());
        mywebview.setContextMenuEnabled(false);
        mywebengine = mywebview.getEngine();
        mywebengine.loadContent(message);
        mywebengine.setUserDataDirectory(new File(PubManPlatform.getWebEngineUserDataDirectory(), getClass().getCanonicalName()));
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

        if (this.currentPubMedEntry==null) {
            sb.append("<p>Could not be parsed</p>");
        } else {
            sb.append("<body><h2>").append(this.currentPubMedEntry.getTitle()).append("</h2>");
            sb.append("<p>").append(this.currentPubMedEntry.toString()).append("</p>");
        }
        if (this.currentSeenPmids.contains(this.currentPubMedEntry.getPmid())) {
            sb.append("<p style=\"color:red\">Warning: this article is already in our citation database!</p>");
        }


        sb.append("<h2>Stats</h2>");
        sb.append("<ul>");
        sb.append("<li>Number of articles currently in citation database: ").append(String.valueOf(this.itemList.size())).append("</li>");
        if (! toBeFetchedStack.empty()) {
            sb.append("<li style=\"color:red\">Number of articles in stack waiting to be checked: ").append(String.valueOf(this.toBeFetchedStack.size())).append("</li>");
        }
        sb.append("</ul>");
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
        this.toBeFetchedStack.addAll(pmids);
        updateWebview();
    }

    @FXML private void showNext(ActionEvent e) {
        String pmid = toBeFetchedStack.pop();
        fetchPmid(pmid);
        updateWebview();

    }


}
