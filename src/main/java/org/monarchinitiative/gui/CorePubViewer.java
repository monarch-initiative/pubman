package org.monarchinitiative.gui;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.monarchinitiative.item.Item;
import org.monarchinitiative.pubmed.PubMedEntry;

import java.util.List;

class CorePubViewer {

    private final List<Item> coreitems;

    CorePubViewer(List<Item> items ){
        this.coreitems=items;

    }



    private String getCSS() {
        return "\n"+
                "table {\n"+
                "\tborder-collapse: collapse;\n"+
                "\twidth:100%;\n"+
                "\tmargin:0.5rem 0;\n"+
                "}\n"+
                "\n"+
                "th, td {\n"+
                "\ttext-align:left;\n"+
                "\tpadding:0.4rem 0.5rem 0.25rem;\n"+
                "}\n"+
                "\n"+
                "th {\n"+
                "\tbackground-color: #e0e3ea;\n"+
                "\tborder-bottom:1px solid white;\n"+
                "}\n"+
                "\n"+
                "table.redTable {\n"+
                "\twidth:auto;\n"+
                "\tmin-width:50%;\n"+
                "}\n"+
                "\n"+
                "table.redTable td {\n"+
                "\tbackground-color:#f0f3fa;\n"+
                "}\n";
    }

    private String tableRow(Item item) {
        String pmid = item.getPmid();
        String link= String.format("https://www.ncbi.nlm.nih.gov/pubmed/%s",pmid);
        PubMedEntry entry = item.getEntry();
        String authorlist = entry.getAuthorList();
        String []authors = authorlist.split(",");
        String journalstring = entry.getJournal() +"." + entry.getVolume() +":" + entry.getPages();
        String year = entry.getYear();
        return String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>",
                authors[0],year,journalstring,entry.getTitle());
    }



    private String getHTML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><style>").append(getCSS()).append("</style><body><h1>Core Publications</h1>" +
                    "<p>There are ").append(String.valueOf(coreitems.size())).append(" core publications in the PubMan dabase.</p>");
        sb.append("<p><table class=\"redTable\">");
        sb.append("<tr><th>Author</th><th>Year</th><th>Journal</th><th>Title</th></tr>");
        for (Item item : this.coreitems) {
            sb.append(tableRow(item));
        }
        sb.append("</table></p>");
        sb.append("</body></html>");
        return sb.toString();
    }



    void show() {
        Stage stage = new Stage();
        stage.setTitle("Web View");
        Scene scene = new Scene(new Browser(getHTML()),750,500, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("webviewsample/BrowserToolbar.css");
        stage.show();
    }


   static  class Browser extends Region {

        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        Browser(String contents) {
            //apply the styles
            getStyleClass().add("browser");
            // load the web page
            webEngine.loadContent(contents);
            //add the web view to the scene
            getChildren().add(browser);

        }
        private Node createSpacer() {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            return spacer;
        }

        @Override protected void layoutChildren() {
            double w = getWidth();
            double h = getHeight();
            layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
        }

        @Override protected double computePrefWidth(double height) {
            return 750;
        }

        @Override protected double computePrefHeight(double width) {
            return 500;
        }
    }

}
