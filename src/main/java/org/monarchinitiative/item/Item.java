package org.monarchinitiative.item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.monarchinitiative.pubmed.PubMedEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * #authorList title   journal year    volume  pages   pmid    inhouse resource    clinical.use    phenogeno.algorithm systems.bio.algorithm
 */
public class Item {
    private static final Logger logger = LogManager.getLogger();


    private final PubMedEntry entry;
    private final boolean inHouse;
    private final boolean hpo;
    private final boolean monarch;
    private final List<Topic> topiclist;

    private final static String TRUE = "T";
    private final static String FALSE = "F";

    private final static String [] HEADER_FIELDS = {"authorList", "title","journal", "year", "volume", "pages", "pmid", "inhouse","hpo", "monarch", "topic.list"};


     private Item(PubMedEntry entry, boolean inHouse,boolean hpo, boolean monarch, List<Topic> topics) {
         this.entry=entry;
         this.inHouse=inHouse;
         this.hpo=hpo;
         this.monarch=monarch;
         this.topiclist=topics;
         if (topiclist.isEmpty()) throw new IllegalArgumentException("Need to pass at least one topic (in additional to in-house,hpo,monarch)");
     }

    public PubMedEntry getEntry() {
        return entry;
    }

    public static String getHeaderLine() {
        return "#" + String.join("\t", HEADER_FIELDS);
    }

    public boolean isInHouse() {
        return inHouse;
    }

    public boolean isHpo() {
        return hpo;
    }

    public boolean isMonarch() {
        return monarch;
    }

    public boolean isCore() {
         return this.topiclist.contains(Topic.CORE);
    }



    public List<Topic> getTopics(){ return this.topiclist; }

    public static Item fromLine(String line) throws IllegalArgumentException {
        String[] fields=line.trim().split("\t");
        if (fields.length != HEADER_FIELDS.length) {
            logger.error("Bad length ({} instead of {}) of line: \"{}\"" , fields.length, HEADER_FIELDS.length, line);
            for (int i=0;i<fields.length;i++) {
                logger.error("\t{}] {}" , i,fields[i]);
            }
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
        boolean hpo = fields[8].equals("T");
        boolean monarch = fields[9].equals("T");
        String topicstring = fields[10];
        String[] topics = topicstring.split(";");
        List<Topic> topiclist = new ArrayList<>();
        for (String t:topics) {
            Topic topic = Topic.fromString(t);
            topiclist.add(topic);
        }
        PubMedEntry entry = new PubMedEntry(authors,title,journal,year,volume,pages,pmid);
        return new Item(entry,inHouse,hpo, monarch, topiclist);
    }

    public String getPmid() {
        return this.entry.getPmid();
    }


    public String toLine() {
        String[] fields = new String[HEADER_FIELDS.length];
        fields[0] = this.entry.getAuthorList();
        fields[1] = this.entry.getTitle();
        fields[2] = this.entry.getJournal();
        fields[3] = this.entry.getYear();
        fields[4] = this.entry.getVolume();
        fields[5]= this.entry.getPages();
        fields[6] = this.entry.getPmid();
        fields[7] = this.inHouse ? TRUE : FALSE;
        fields[8] = this.hpo ? TRUE : FALSE;
        fields[9] = this.monarch ? TRUE : FALSE;
        fields[10] = this.topiclist.stream().map(Topic::toString).collect(Collectors.joining(";"));
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
        private boolean EHR;
        private boolean exomiser;
        private boolean core;
        private boolean review;

        public Builder inhouse(boolean b) {
            inHouse=b; return this;
        }
        public Builder resource(boolean b) {
            this.resource=b; return this;
        }
        public Builder exomiser(boolean b) {
            exomiser=b; return this;
        }
        public Builder core(boolean b) {
            core=b; return this;
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

        public Builder EHR(boolean b) {
            this.EHR=b; return this;
        }
        public Builder entry(PubMedEntry e) {
            this.entry = e;
            return this;
        }

        public Item build() {
            List<Topic> topiclist = new ArrayList<>();
            if (clinical) topiclist.add(Topic.CLINICAL);
            if (resource) topiclist.add(Topic.RESOURCE);
            if (phenoAlg) topiclist.add(Topic.PHENOGENO);
            if (systemsBio) topiclist.add(Topic.SYSTEMSBIO);
            if (database) topiclist.add(Topic.COMMONDISEASE);
            if (crossspecies) topiclist.add(Topic.CROSS_SPECIES);
            if (environment) topiclist.add(Topic.ENVIRONMENT);
            if (cancer) topiclist.add(Topic.CANCER);
            if (review) topiclist.add(Topic.REVIEW);
            if (exomiser) topiclist.add(Topic.EXOMISER_USE);
            if (EHR) topiclist.add(Topic.EHR);

            return new Item(this.entry,
                    this.inHouse,
                    this.hpo,
                    this.monarch,
                    topiclist);
        }

    }


}
