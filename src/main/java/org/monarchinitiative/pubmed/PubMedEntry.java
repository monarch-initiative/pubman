package org.monarchinitiative.pubmed;

public class PubMedEntry {
    private final String authorList;

    private final String title;

    private final String journal;

    private final String year;

    private final String volume;

    private final String pages;

    private final String pmid;


    public PubMedEntry(String authorList, String title, String journal, String year, String volume, String pages, String pmid) {
        this.authorList = authorList;
        this.title = title;
        this.journal = journal;
        this.year = year;
        this.volume = volume;
        this.pages = pages;
        this.pmid = pmid;
    }


    public String getAuthorList() {
        return authorList;
    }


    public String getTitle() {
        return title;
    }


    public String getJournal() {
        return journal;
    }


    public String getYear() {
        return year;
    }


    public String getVolume() {
        return volume;
    }


    public String getPages() {
        return pages;
    }


    public String getPmid() {
        return pmid;
    }


    @Override
    public String toString() {
        return authorList +" (" + year +") " + title;
    }
}
