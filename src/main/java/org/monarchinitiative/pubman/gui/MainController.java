package org.monarchinitiative.pubman.gui;

import javafx.application.Platform;
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
import org.monarchinitiative.pubman.item.Item;
import org.monarchinitiative.pubman.item.ItemQC;
import org.monarchinitiative.pubman.item.Topic;
import org.monarchinitiative.pubman.pubmed.*;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    @FXML private CheckBox exomiserCB;
    @FXML private CheckBox ehrCB;
    @FXML private CheckBox coreCB;
    @FXML private CheckBox reviewCB;

    private PubMedEntry currentPubMedEntry=null;

    private List<Item> itemList;

    private Map<String,Item> citedByMap = new HashMap<>();

    private Stack<String> toBeFetchedStack =null;
    /** Set of all PMIDs that are already in our commonDisease. Want to skip repeat entries! */
    private Set<String> currentSeenPmids;

    /** Path to the curated file of HPO citations. */
    private String citationFilePath=null;


    public void initialize(URL url, ResourceBundle rb) {
        this.itemList = new ArrayList<>();
        this.toBeFetchedStack = new Stack<>();
        this.currentSeenPmids = new HashSet<>();
        try {
            this.citationFilePath = PubManPlatform.getPubManFile();
        } catch (IOException e) {
            PopupFactory.displayException("Error","Could not find citation file", e);
            return;
        }
        ingestPubMedEntryList();
    }




    private void ingestPubMedEntryList() {
        if (this.citationFilePath==null) {
            PopupFactory.displayError("Error","Could not find citation file. Please set it using the Edit menu");
            return;
        }
        try {
            logger.trace("Reading existing items from {}", this.citationFilePath);
            BufferedReader br = new BufferedReader(new FileReader(this.citationFilePath));
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

        } catch (PubMedParseException | IOException exc) {
            exc.printStackTrace();
            this.currentPubMedEntry=null;
            return false;
        }
        return true;
    }




    private String getAlreadyExistsWarning() {
        return "<html>" +
                "<style type=\"text/css\">\n" +
                " span.bold-red {\n" +
                "    color: red;\n" +
                "    font-weight: bold;\n" +
                "}\n" +
                " span.blu {\n" +
                "    color: #4e89a4;\n" +
                "    font-weight: normal;\n" +
                "}\n" +
                "</style>" +
                "<body><h2>warning</h2>" +
                "<p>Article already present in our citation list. " +
                "It was not added a second time</p>" +
                "</body></html>";
    }

    /**
     * Revise an existing entry (e.g., change its category)
     */
    @FXML private void revisePMID(ActionEvent e) {
        this.currentSeenPmids.add(currentPubMedEntry.getPmid());

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
                exomiser(this.exomiserCB.isSelected()).
                cancer(this.cancerCB.isSelected()).
                review(this.reviewCB.isSelected()).
                core(this.coreCB.isSelected()).
                EHR(this.ehrCB.isSelected()).
                entry(this.currentPubMedEntry);
        try {
            Item item = buiilder.build();
            Item previousItem = getCurrentItem(currentPubMedEntry);
            logger.trace("Updating item {}", item.toLine());
            // this will replace the current item
            this.itemList.set(this.itemList.indexOf(previousItem), item);
            updateWebview();
        } catch (IllegalArgumentException exc) {
            PopupFactory.displayError("Error with new item", exc.getMessage());
            return;
        }
        e.consume();
    }


    private Item getCurrentItem(PubMedEntry entry) {
        for (Item item: itemList) {
            if (item.getPmid().equals(entry.getPmid())) {
                return item;
            }
        }
        return null;
    }

    /** SHow the categories of the current entry (for existing entries). */
    @FXML private void showCurrentAnnotations(ActionEvent e) {
        Item currentItem =  getCurrentItem(this.currentPubMedEntry);
        if (currentItem==null) {
            PopupFactory.displayMessage("Error","Could not retrieve item");
            return;
        }
        List<Topic> topics = currentItem.getTopics();
        this.cancerCB.setSelected(topics.contains(Topic.CANCER));
        this.crossSpeciesCB.setSelected(topics.contains(Topic.CROSS_SPECIES));
        this.commonDiseaseCB.setSelected(topics.contains(Topic.COMMONDISEASE));
        this.ehrCB.setSelected(topics.contains(Topic.EHR));
        this.resourceCB.setSelected(topics.contains(Topic.RESOURCE));
        this.phenoGenoAlgCB.setSelected(topics.contains(Topic.PHENOGENO));
        this.exomiserCB.setSelected(topics.contains(Topic.EXOMISER_USE));
        this.systemsBioAlgCB.setSelected(topics.contains(Topic.SYSTEMSBIO));
        this.clinicalUseCB.setSelected(topics.contains(Topic.CLINICAL));
        this.environmentCB.setSelected(topics.contains(Topic.ENVIRONMENT));
        this.reviewCB.setSelected(topics.contains(Topic.REVIEW));
        this.coreCB.setSelected(topics.contains(Topic.CORE));
        this.inHouseCB.setSelected(currentItem.isInHouse());
        this.hpoCB.setSelected(currentItem.isHpo());
        this.monarchCB.setSelected(currentItem.isMonarch());
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
                exomiser(this.exomiserCB.isSelected()).
                cancer(this.cancerCB.isSelected()).
                review(this.reviewCB.isSelected()).
                core(this.coreCB.isSelected()).
                EHR(this.ehrCB.isSelected()).
                entry(this.currentPubMedEntry);
        try {
            Item item = buiilder.build();
            logger.trace("Adding item {}", item.toLine());
            this.itemList.add(item);
            updateWebview();
        } catch (IllegalArgumentException exc) {
            PopupFactory.displayError("Error with new item", exc.getMessage());
            return;
        }
       e.consume();
    }


    private void updateWebview() {
        if (currentPubMedEntry==null) {
            logger.error("Current pubmed entry is null");
            return;
        }
        logger.trace("updating webview, current pubmed entry is {}", currentPubMedEntry.toString());
        mywebview.setContextMenuEnabled(false);
        mywebengine = mywebview.getEngine();
        mywebengine.loadContent(getCurrentPubMedHTML());
        mywebengine.setUserDataDirectory(new File(PubManPlatform.getWebEngineUserDataDirectory(), getClass().getCanonicalName()));
    }

    private void updateWebview(String message) {
//        logger.trace("updating webview, current pubmed entry is {}", currentPubMedEntry.toString());
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
        if (citedByMap.containsKey(currentPubMedEntry.getPmid())) {
            Item citedItem = citedByMap.get(currentPubMedEntry.getPmid());
            sb.append(String.format("<p>Cites: %s",citedItem.getEntry().getTitle())).append("</p>");
        }


        sb.append("<h2>Stats</h2>");
        sb.append("<ul>");
        sb.append("<li>Number of articles currently in citation database: ").append(this.itemList.size()).append("</li>");
        if (! toBeFetchedStack.empty()) {
            sb.append("<li style=\"color:red\">Number of articles in stack waiting to be checked: ").append(this.toBeFetchedStack.size()).append("</li>");
        }
        sb.append("</ul>");
        sb.append("<p style=\"color:gray;font-size:10px;\">Citation file: ").append(this.citationFilePath).append("</p>");
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
        int alreadySeen=0;
        for (String pmid : pmids) {
            if (this.currentSeenPmids.contains(pmid)) { alreadySeen++; continue; }
            this.toBeFetchedStack.add(pmid);
        }
        PopupFactory.displayMessage("Retrieving citations",
               String.format("Skipping %d citations already present in our database",alreadySeen));
        updateWebview();
    }

    @FXML private void showNext(ActionEvent e) {
        if (toBeFetchedStack.empty()) {
            PopupFactory.displayMessage("Stack empty!","No more PMIDs to be fetched");
            return;
        }
        String pmid = toBeFetchedStack.pop();
        logger.trace("About to get next pmid: " + pmid);
        fetchPmid(pmid);
        updateWebview();
    }


    @FXML private void chooseFileLocationForCitations() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Choose location of citation file");
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            logger.error("Could not get name of PubMan citations file.");
        } else {
            this.citationFilePath = file.getAbsolutePath();
            logger.info("citation file: "+file.getAbsolutePath());
            saveSettings();
        }
    }


    private final static String settingsFileName = "pubman-settings.txt";


    private void loadSettings() {
        File pubmandir=PubManPlatform.getPubManDir();
        Objects.requireNonNull(pubmandir);
        File defaultSettingsPath = new File(pubmandir.getAbsolutePath()
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
                    this.citationFilePath=line.substring(5).trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.citationFilePath==null) {
            logger.error("Could not read citation file path from settings file");
        }

    }

    @FXML private void showCorePubs() {
        List<Item> coreitems = this.itemList.stream().filter(Item::isCore).collect(Collectors.toList());
        CorePubViewer viewer = new CorePubViewer(coreitems);
        viewer.show();
    }

    @FXML private void showCuratedPublications() {
        PubViewer viewer = new PubViewer(this.itemList);
        viewer.show();
    }

    private String getCurrentNewCitationHTML() {
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
                "</style><body>");


        sb.append("<h2>Stats</h2>");
        sb.append("<ul>");
        sb.append("<li>New citations retrieved: ").append(String.valueOf(toBeFetchedStack.size())).append("</li>");
        sb.append("<li>Citations already in database: ").append(this.itemList.size()).append("</li>");
        sb.append("</ul>");
        sb.append("</body></html>");
        return sb.toString();
    }



    @FXML private void getNewCitations() {
        citedByMap.clear();
        List<Item> coreitems = this.itemList.stream().filter(Item::isCore).collect(Collectors.toList());
        Set<String> newPmids=new HashSet<>();
        int alreadySeen=0;
        for (Item item : coreitems) {
            CitationGrabber grabber = new CitationGrabber(item.getPmid());
            List<String> citingPmids = grabber.citingPMIDs();
            for (String pmid : citingPmids) {
                if (this.currentSeenPmids.contains(pmid)) { alreadySeen++; continue; }
                this.toBeFetchedStack.add(pmid);
                citedByMap.put(pmid,item);
            }
            Platform.runLater( () -> updateWebview(getCurrentNewCitationHTML()) );
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        PopupFactory.displayMessage("New citations",
                String.format("Previously seen: %d; new citations: %d",alreadySeen,toBeFetchedStack.size()));
    }


    /**
     * This method gets called when user chooses to close Gui. Content of
     * in XML format to platform-dependent default location.
     */
    private void saveSettings() {
        File pubmandir = PubManPlatform.getPubManDir();
        Objects.requireNonNull(pubmandir);
        File defaultSettingsPath = new File(pubmandir.getAbsolutePath()
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


    @FXML private void doQC(ActionEvent e) {
        try {
            ItemQC itemQC = new ItemQC();
            for (Item item : this.itemList) {
                if (! itemQC.check(item)) {
                    String html = HtmlFactory.getParseErrorHtml(item);
                    updateWebview(html);
                    return;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String html = HtmlFactory.qcOK();
        updateWebview(html);
        e.consume();
    }

    @FXML private void replace(ActionEvent e) {
        ListIterator<Item> iterator = itemList.listIterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getPmid().equals(this.currentPubMedEntry.getPmid())) {
                //Replace element
                //iterator.set("New");
                System.out.println("We found and replaced the entry");
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
                        exomiser(this.exomiserCB.isSelected()).
                        cancer(this.cancerCB.isSelected()).
                        review(this.reviewCB.isSelected()).
                        core(this.coreCB.isSelected()).
                        EHR(this.ehrCB.isSelected()).
                        entry(this.currentPubMedEntry);
                try {
                    Item newItem = buiilder.build();
                    iterator.set(newItem);
                } catch (IllegalArgumentException iae) {
                    PopupFactory.displayException("Error","Failure to set categories",iae);
                    return;
                }

            }
        }
        e.consume();
    }


}
