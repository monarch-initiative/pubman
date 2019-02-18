package org.monarchinitiative.item;

import org.monarchinitiative.pubmed.PubMedEntry;

/**
 * #authorList title   journal year    volume  pages   pmid    inhouse resource    clinical.use    phenogeno.algorithm systems.bio.algorithm
 */
public class Item {



    private final PubMedEntry entry;
    private final boolean inHouse;
    private final boolean resource;
    private final boolean clinical;
    private final boolean phenoAlg;
    private final boolean systemsBio;

    private final static String TRUE = "T";
    private final static String FALSE = "F";

    private final static String [] fields = {"authorList", "title","journal", "year", "volume", "pages", "pmid", "inhouse", "resource", "clinical.use","phenogeno.algorithm", "systems.bio.algorithm"};


    private Item(String authorList, String title, String journal, String year, String volume, String pages, String pmid,
                 boolean inHouse, boolean resource,boolean clinical, boolean phenoAlg, boolean systemsBio) {
        this.entry = new PubMedEntry(authorList,  title,  journal,  year,  volume,  pages,  pmid);
        this.inHouse=inHouse;
        this.resource=resource;
        this.clinical=clinical;
        this.phenoAlg=phenoAlg;
        this.systemsBio=systemsBio;
    }


    public Item(PubMedEntry entry, boolean inHouse, boolean resource,boolean clinical, boolean phenoAlg, boolean systemsBio) {
        this.entry=entry;
        this.inHouse=inHouse;
        this.resource=resource;
        this.clinical=clinical;
        this.phenoAlg=phenoAlg;
        this.systemsBio=systemsBio;
    }


    public static String getHeaderLine() {
        return "#" + String.join("\t",fields);
    }


    public static Item fromLine(String line) {
        String fields[]=line.trim().split("\t");
        if (fields.length != 12) {
            System.err.println("Bad length of line: " + line.length() + ", " + line);
            System.exit(1);
        }
        String authors = fields[0];
        String title = fields[1];
        String journal = fields[2];
        String year = fields[3];
        String volume = fields[4];
        String pages = fields[5];
        String pmid = fields[6];
        boolean inHouse = fields[7].equals("T");
        boolean resource = fields[8].equals("T");
        boolean clinical = fields[9].equals("T");
        boolean pheno = fields[10].equals("T");
        boolean systems = fields[11].equals("T");
        PubMedEntry entry = new PubMedEntry(authors,title,journal,year,volume,pages,pmid);
        return new Item(entry,inHouse,resource,clinical,pheno,systems);
    }

    public String getPmid() {
        return this.entry.getPmid();
    }


    public String toLine() {
        String fields[] = new String[12];
        fields[0] = this.entry.getAuthorList();
        fields[1] = this.entry.getTitle();
        fields[2] = this.entry.getJournal();
        fields[3] = this.entry.getYear();
        fields[4] = this.entry.getVolume();
        fields[5]= this.entry.getPages();
        fields[6] = this.entry.getPmid();
        fields[7] = this.inHouse ? TRUE : FALSE;
        fields[8] = this.resource ? TRUE : FALSE;
        fields[9] = this.clinical ? TRUE : FALSE;
        fields[10] = this.phenoAlg ? TRUE : FALSE;
        fields[11] = this.systemsBio ? TRUE : FALSE;
        return String.join("\t",fields);
    }


}
