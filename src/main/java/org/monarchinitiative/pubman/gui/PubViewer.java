package org.monarchinitiative.pubman.gui;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.monarchinitiative.pubman.item.Item;
import org.monarchinitiative.pubman.pubmed.PubMedEntry;

import java.util.List;

/**
 * Show all curated publications in a Webview
 */
class PubViewer {
    private final List<Item> citationlist;

    PubViewer(List<Item> items ){
        this.citationlist =items;
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
                "<p>There are ").append(String.valueOf(citationlist.size())).append(" citations in the PubMan dabase.</p>");
        sb.append("<p><table class=\"redTable\">");
        sb.append("<tr><th>Author</th><th>Year</th><th>Journal</th><th>Title</th></tr>");
        for (Item item : this.citationlist) {
            sb.append(tableRow(item));
        }
        sb.append("</table></p>");
        sb.append("</body></html>");
        return sb.toString();
    }



    void show() {
        Stage stage = new Stage();
        stage.setTitle("Web View");
        Scene scene = new Scene(new MyBrowser(getHTML()),750,500, Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("webviewsample/BrowserToolbar.css");
        stage.show();
    }



}

