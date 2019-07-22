package org.monarchinitiative.pubman.gui;

import org.monarchinitiative.pubman.item.Item;
import org.monarchinitiative.pubman.pubmed.PubMedEntry;

class HtmlFactory {


    private static String getCSS() {
        return "<style type=\"text/css\">\n" +
                " span.bold-red {\n" +
                "    color: red;\n" +
                "    font-weight: bold;\n" +
                "}\n" +
                " span.blu {\n" +
                "    color: #4e89a4;\n" +
                "    font-weight: normal;\n" +
                "}\n" +
                "</style>";
    }


    static String qcOK(){
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(getCSS());
        sb.append("<body><h1>Finished Q/C check</h1><h2>").append("No Q/C problems detected").append("</h2>");
        sb.append("</body></html>");
        return sb.toString();

    }



    static String getParseErrorHtml(Item item) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(getCSS());
        PubMedEntry ent = item.getEntry();
        sb.append("<body><h1>Bad Parse</h1><h2>").append(ent.getTitle()).append("</h2>");
        sb.append("<p>").append(ent.toString()).append("</p>");
        sb.append("</body></html>");
        return sb.toString();
    }
}
