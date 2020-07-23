package org.monarchinitiative.pubman.pubmed;

import org.apache.commons.io.IOUtils;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.Charset.defaultCharset;

public class PubmedXmlParser {

    private String journal;
    private List<String> authorlist;
    /** Note that the lastAuthor element is also included in the authors so we do not need to use it.
     * Keep it now just in case.
     */
    private String lastAuthor = null;
    private String title;
    private String volume;
    private String year;
    private String issue;
    private String pages;
    private String pmid;


    public PubmedXmlParser(String payload) {
        authorlist = new ArrayList<>();
       try {
           parse(payload);
       } catch (Exception e) {
           e.printStackTrace();
       }
    }


    private void parse(String payload) throws XMLStreamException {
        InputStream targetStream = IOUtils.toInputStream(payload, 	defaultCharset());
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(targetStream);
        while (xmlEventReader.hasNext()) {
            XMLEvent xmlEvent = xmlEventReader.nextEvent();
            if (xmlEvent.isStartElement()) {
                StartElement startElement = xmlEvent.asStartElement();
                String localName = startElement.getName().getLocalPart();
                if (localName.equals("Id")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    String id = xmlEvent.asCharacters().getData();
                    this.pmid = id;
                } else if (localName.equals("Item")) {
                    Attribute idAttr = startElement.getAttributeByName(new QName("Name"));
                    Attribute typeAttr = startElement.getAttributeByName(new QName("Type"));
                    if (typeAttr != null && typeAttr.getValue().equals("List")) {
                        continue; // References are a list, which we can just skip
                    }
                    if (idAttr != null) {
                        String name = idAttr.getValue();
                        xmlEvent = xmlEventReader.nextEvent();
                        if (xmlEvent.isEndElement()) {
                            // the XML node was empty, we can just skip
                            continue;
                        }
                        String item = xmlEvent.asCharacters().getData();
                        switch (name) {
                            case "Source":
                                this.journal = item;
                                break;
                            case "Author":
                                this.authorlist.add(item);
                                break;
                            case "PubDate":
                                this.year = getYearFromPubData(item);
                                break; // skip
                            case "EPubDate":
                                break;//skip
                            case "LastAuthor":
                                lastAuthor = item;
                                break;
                            case "Title":
                                title = item;
                                break;
                            case "Volume":
                                volume = item;
                                break;
                            case "Issue":
                                issue = item;
                                break;
                            case "Pages":
                                pages = item;
                                break;
                            case "Lang":
                            case "LangList":
                            case "NlmUniqueID":
                            case "ISSN":
                            case "ESSN":
                            case "PubTypeList":
                            case "PubType":
                            case "RecordStatus":
                            case "References":
                            case "AuthorList":
                            case "PubStatus":
                            case "ArticleIds":
                            case "pubmed":
                            case "doi":
                            case "DOI":
                            case "pmc":
                            case "rid":
                            case "eid":
                            case "pmcid":
                            case "History":
                            case "medline":
                            case "entrez":
                            case "pii":
                            case "PmcRefCount":
                            case "FullJournalName":
                            case "HasAbstract":
                            case "ELocationID":
                            case "SO":
                                break; // skip
                            default:
                                System.out.println("Coud not find " + name);
                                System.exit(1);



                        }
                    }


                }
            }
        }
    }

    private String getYearFromPubData(String item) {
        final String pattern19 = "(19\\d\\d)";
        final Pattern r19 = Pattern.compile(pattern19);
        Matcher m = r19.matcher(item);
        if (m.find( )) {
            return m.group(1);
        }
        final String pattern20 = "(20\\d\\d)";
        final Pattern r20 = Pattern.compile(pattern20);
        m = r20.matcher(item);
        if (m.find( )) {
            return m.group(1);
        }
        return "";
    }


    public PubMedEntry getCitation() {
        if (this.authorlist == null || this.title == null || this.journal == null || this.year == null ||
        this.volume == null || this.pages == null || this.pmid == null) {
            return null;
        }
        String authors = String.join(", ", this.authorlist);
        return new PubMedEntry(authors, this.title, this.journal, year, this.volume, this.pages, this.pmid);
    }

}
