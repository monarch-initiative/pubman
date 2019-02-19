package org.monarchinitiative.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.monarchinitiative.item.Item;
import org.monarchinitiative.pubmed.*;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController implements Initializable {
    private static final Logger logger = LogManager.getLogger();

    @FXML
    TextField pmidTextField;

    @FXML
    WebView mywebview;

    private WebEngine mywebengine;

    @FXML private CheckBox inHouseCB;
    @FXML private CheckBox hpoCB;
    @FXML private CheckBox monarchCB;
    @FXML private CheckBox commonDiseaseCB;
    @FXML private CheckBox resourceCB;
    @FXML private CheckBox clinicalUseCB;
    @FXML private CheckBox phenoGenoAlgCB;
    @FXML private CheckBox systemsBioAlgCB;
    @FXML private CheckBox crossSpeciesCB;
    @FXML private CheckBox cancerCB;
    @FXML private CheckBox environmentCB;
    @FXML private CheckBox reviewCB;

    private PubMedEntry currentPubMedEntry=null;

    private List<Item> itemList;

    private Stack<String> toBeFetchedStack =null;
    /** Set of all PMIDs that are already in our commonDisease. Want to skip repeat entries! */
    private Set<String> currentSeenPmids;

    /** Path to the curated file of HPO citations. */
    private String citationFilePath=null;


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
                if (item == null) continue;
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
        javafx.application.Platform.exit();
    }


    private void saveItems() {
        if (this.citationFilePath==null) {
            logger.error("Warning -- cannot save items before specifying citation file path");
            return;
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.citationFilePath));
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
        if (!fetchPmid(userinput)) {
            return;
        }
        updateWebview();
        e.consume();
    }


    private boolean fetchPmid(String pmid) {
        if (pmid.startsWith("PMID:") && pmid.length()>6){
            pmid=pmid.substring(5);
        }
        Pattern pat = Pattern.compile("\\d+");
        Matcher m = pat.matcher(pmid);
        if (!m.matches()) {
            logger.error("Did not recognize PMID string " + pmid);
            return false;
        }
        try {
            PubMedSummaryRetriever pmretriever = new PubMedSummaryRetriever();
            String summary  = pmretriever.getSummary(pmid);
            this.currentPubMedEntry = PubMedParser.parsePubMed(summary);

        } catch (PubMedParseException pmex) {
            pmex.printStackTrace();
            this.currentPubMedEntry=null;
            return false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            this.currentPubMedEntry=null;
            return false;
        }
        return true;
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
        sb.append("<p>Article already present in our citation commonDisease. " +
                "It was not added a second time</p>");
        sb.append("</body></html>");
        return sb.toString();
    }



    @FXML private void addPMID(ActionEvent e) {




        if (this.currentSeenPmids.contains(currentPubMedEntry.getPmid())) {
            updateWebview(getAlreadyExistsWarning());
            return;
        } else {
            this.currentSeenPmids.add(currentPubMedEntry.getPmid());
        }
        Item.Builder buiilder = new Item.Builder().
                inhouse(this.inHouseCB.isSelected()).
                hpo(this.hpoCB.isSelected()).
                monarch(this.monarchCB.isSelected()).
                crossspecies(this.crossSpeciesCB.isSelected()).
                clinical(this.clinicalUseCB.isSelected()).
                resource(this.resourceCB.isSelected()).
                phenoAlg(this.phenoGenoAlgCB.isSelected()).
                systemsBio(this.systemsBioAlgCB.isSelected()).
                commonDisease(this.commonDiseaseCB.isSelected()).
                environment(this.environmentCB.isSelected()).
                cancer(this.cancerCB.isSelected()).
                review(this.reviewCB.isSelected()).
                entry(this.currentPubMedEntry);
        Item item = buiilder.build();
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
            sb.append("<p style=\"color:red\">Warning: this article is already in our citation commonDisease!</p>");
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


    @FXML private void chooseFileLocationForCitations() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Choose location of citation file");
        File file = fileChooser.showSaveDialog(null);
        if (file == null) {
            logger.error("Could not get name of file with gene symbols");
            return;
        } else {
            this.citationFilePath = file.getAbsolutePath();
            logger.info("citation file: "+file.getAbsolutePath());
            saveSettings();

        }
    }


    private final static String settingsFileName = "pubman-settings.txt";


    private void loadSettings() {
        File defaultSettingsPath = new File(PubManPlatform.getPubManDir().getAbsolutePath()
                + File.separator + settingsFileName);
        if (!defaultSettingsPath.exists()) {
            File fck = new File(defaultSettingsPath.getAbsolutePath());
            if (!fck.mkdir()) { // make sure config directory is created, exit if not
                logger.error("Unable to create PubMan config directory.");
                throw new RuntimeException("Unable to create PubMan config directory.");
            }
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(defaultSettingsPath));
            String line;
            this.citationFilePath=null;
            while ((line=br.readLine())!=null) {
                if (line.startsWith("#")) continue;
                if (line.startsWith("file:")) {
                    String name=line.substring(5).trim();
                    this.citationFilePath=name;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.citationFilePath==null) {
            logger.error("Could not read citation file path from settings file");
        }

    }

    /**
     * This method gets called when user chooses to close Gui. Content of
     * in XML format to platform-dependent default location.
     */
    private void saveSettings() {
        File defaultSettingsPath = new File(PubManPlatform.getPubManDir().getAbsolutePath()
                + File.separator + settingsFileName);
        if (!PubManPlatform.getPubManDir().exists()) {
            if (!PubManPlatform.getPubManDir().mkdir()) {
                return;
            }
        }
        if (this.citationFilePath==null) {
            logger.error("Attempt to set null file path for citations");
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(defaultSettingsPath));
            writer.write("file:" + this.citationFilePath);
            writer.close();

        }catch(IOException e) {
            e.printStackTrace();
        }
    }


}
