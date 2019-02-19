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
    private final boolean hpo;
    private final boolean monarch;
    private final boolean commondisease;
    private final boolean crossspecies;
    private final boolean environment;
    private final boolean cancer;
    private final boolean review;

    private final static String TRUE = "T";
    private final static String FALSE = "F";

    private final static String [] HEADER_FIELDS = {"authorList", "title","journal", "year", "volume", "pages", "pmid", "inhouse", "resource", "clinical.use","phenogeno.algorithm",
            "systems.bio.algorithm", "hpo", "monarch", "common.disease", "cross.species","environment","cancer","review"};


     private Item(PubMedEntry entry, boolean inHouse, boolean resource,boolean clinical, boolean phenoAlg, boolean systemsBio,
             boolean hpo, boolean monarch, boolean commondis, boolean crossspecies, boolean environment, boolean cancer, boolean review) {
         this.entry=entry;
         this.inHouse=inHouse;
         this.resource=resource;
         this.clinical=clinical;
         this.phenoAlg=phenoAlg;
         this.systemsBio=systemsBio;
         this.hpo=hpo;
         this.monarch=monarch;
         this.commondisease =commondis;
         this.crossspecies=crossspecies;
         this.environment=environment;
         this.cancer=cancer;
         this.review=review;
     }


    public static String getHeaderLine() {
        return "#" + String.join("\t", HEADER_FIELDS);
    }


    public static Item fromLine(String line) {
        String fields[]=line.trim().split("\t");
        if (fields.length != HEADER_FIELDS.length) {
            System.err.println("Bad length of line: " + line.length() + ", " + line);
            return null;
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
        boolean hpo = fields[12].equals("T");
        boolean monarch = fields[13].equals("T");
        boolean commondisease = fields[14].equals("T");
        boolean crossspecies = fields[15].equals("T");
        boolean environment = fields[16].equals("T");
        boolean cancer = fields[17].equals("T");
        boolean review = fields[18].equals("T");
        PubMedEntry entry = new PubMedEntry(authors,title,journal,year,volume,pages,pmid);
        return new Item(entry,inHouse,resource,clinical,pheno,systems,hpo, monarch, commondisease, crossspecies, environment, cancer,review);
    }

    public String getPmid() {
        return this.entry.getPmid();
    }


    public String toLine() {
        String fields[] = new String[HEADER_FIELDS.length];
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
        fields[12] = this.hpo ? TRUE : FALSE;
        fields[13] = this.monarch ? TRUE : FALSE;
        fields[14] = this.commondisease ? TRUE : FALSE;
        fields[15] = this.crossspecies ? TRUE : FALSE;
        fields[16] = this.environment ? TRUE : FALSE;
        fields[17] = this.cancer ? TRUE : FALSE;
        fields[18] = this.review ? TRUE : FALSE;
        return String.join("\t",fields);
    }


    public static class Builder {

        private  PubMedEntry entry;
        private  boolean inHouse;
        private  boolean resource;
        private  boolean clinical;
        private  boolean phenoAlg;
        private  boolean systemsBio;
        private boolean hpo;
        private boolean monarch;
        private boolean database;
        private boolean crossspecies;
        private boolean environment;
        private boolean cancer;
        private boolean review;

        public Builder inhouse(boolean b) {
            inHouse=b; return this;
        }
        public Builder resource(boolean b) {
            this.resource=b; return this;
        }
        public Builder clinical(boolean b) {
            this.clinical=b; return this;
        }
        public Builder phenoAlg(boolean b) {
            this.phenoAlg=b; return this;
        }
        public Builder systemsBio(boolean b) {
            this.systemsBio=b; return this;
        }
        public Builder hpo(boolean b) {
            this.hpo=b; return this;
        }
        public Builder monarch(boolean b) {
            this.monarch=b; return this;
        }
        public Builder commonDisease(boolean b) {
            this.database=b; return this;
        }
        public Builder crossspecies(boolean b) {
            this.crossspecies=b; return this;
        }
        public Builder environment(boolean b) {
            this.environment=b; return this;
        }
        public Builder cancer(boolean b) {
            this.cancer=b; return this;
        }
        public Builder review(boolean b) {
            this.review=b; return this;
        }
        public Builder entry(PubMedEntry e) {
            this.entry = e;
            return this;
        }

        public Item build() {
            Item item = new Item(this.entry,
                    this.inHouse,
                    this.resource,
            this.clinical,
            this.phenoAlg,
            this.systemsBio,
            this.hpo,
            this.monarch,
            this.database,
            this.crossspecies,
            this.environment,
            this.cancer,
            this.review);

            return item;

        }


    }


}
