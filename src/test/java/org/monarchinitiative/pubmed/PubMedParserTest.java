package org.monarchinitiative.pubmed;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PubMedParserTest {



    @Test
    void testNoVolumePages() throws PubMedParseException{
        String pubmedstring="Gainotti S, Mascalzoni D, Bros-Facer V, Petrini C, Floridia G, Roos M, Salvatore M, Taruscio D. Meeting Patients' Right to the Correct Diagnosis: Ongoing International Initiatives on Undiagnosed Rare Diseases and Ethical and Social Issues. Int J Environ Res Public Health. 2018 Sep 21;15(10). pii: E2072. doi: 10.3390/ijerph15102072. Review. PubMed PMID: 30248891; PubMed Central PMCID: PMC6210164.\n" +
                "\tat org.monarchinitiative.pubmed.PubMedParser.parsePubMed(PubMedParser.java:105)";
        PubMedEntry entry =  PubMedParser.parsePubMed(pubmedstring);
        assertNotNull(entry);
        assertEquals(entry.getPages(),"pii: E2072");
    }


    @Test
    void testNewPub() throws PubMedParseException {
        String pubmedstring="Köhler S. Improved ontology-based similarity calculations using a study-wise annotation model. Database (Oxford). 2018 Jan 1;2018. doi: 10.1093/database/bay026. PubMed PMID: 29688377; PubMed Central PMCID: PMC5868182.";
        PubMedEntry entry =  PubMedParser.parsePubMed(pubmedstring);
        assertNotNull(entry);
    }

    @Test
    void testTitleEndsInQuestionMark() throws PubMedParseException {
        String pubmedstring = "1: Dozmorov MG. Reforming disease classification system-are we there yet? Ann\n" +
                "Transl Med. 2018 Nov;6(Suppl 1):S30. doi: 10.21037/atm.2018.09.36. PubMed PMID:\n" +
                "30613605; PubMed Central PMCID: PMC6291543.\n";
        PubMedEntry entry =  PubMedParser.parsePubMed(pubmedstring);
        assertNotNull(entry);
        assertEquals("2018",entry.getYear());
        assertEquals("Dozmorov MG",entry.getAuthorList());
        assertEquals("Reforming disease classification system-are we there yet?",entry.getTitle());
        assertEquals("Ann Transl Med",entry.getJournal());
        assertEquals("6(Suppl 1)",entry.getVolume());
        assertEquals("S30",entry.getPages());
        assertEquals("30613605",entry.getPmid());
    }

    @Test
    void testYearWithRevision() throws PubMedParseException {
        String pubmedstring = "1: Köhler S, Doelken SC, Ruef BJ, Bauer S, Washington N, Westerfield M, Gkoutos\n" +
                "G, Schofield P, Smedley D, Lewis SE, Robinson PN, Mungall CJ. Construction and\n" +
                "accessibility of a cross-species phenotype ontology along with gene annotations\n" +
                "for biomedical research. Version 2. F1000Res. 2013 Feb 1 [revised 2014 Jan\n" +
                "1];2:30. doi: 10.12688/f1000research.2-30.v2. eCollection 2013. PubMed PMID:\n" +
                "24358873; PubMed Central PMCID: PMC3799545.";
        PubMedEntry entry =  PubMedParser.parsePubMed(pubmedstring);
        assertNotNull(entry);
        assertEquals("2013",entry.getYear());
        String expectedAuthors="Köhler S, Doelken SC, Ruef BJ, Bauer S, Washington N, Westerfield M, Gkoutos" +
                " G, Schofield P, Smedley D, Lewis SE, Robinson PN, Mungall CJ";
        assertEquals(expectedAuthors,entry.getAuthorList());
        String expectedTitle = "Construction and accessibility of a cross-species phenotype ontology along with gene annotations " +
                "for biomedical research. Version 2";
        assertEquals(expectedTitle,entry.getTitle());
        assertEquals("F1000Res",entry.getJournal());
        assertEquals("2",entry.getVolume());
        assertEquals("30",entry.getPages());
        assertEquals("24358873",entry.getPmid());

    }


    @Test
    void testVersion() throws PubMedParseException {
        String pubmedstring = "1: Rajput NK, Singh V, Bhardwaj A. Resources, challenges and way forward in rare \n" +
                "mitochondrial diseases research. Version 2. F1000Res. 2015 Mar 16 [revised 2015\n" +
                "Jan 1];4:70. doi: 10.12688/f1000research.6208.2. eCollection 2015. Review. PubMed\n" +
                "PMID: 26180633; PubMed Central PMCID: PMC4490798.";
        PubMedEntry entry =  PubMedParser.parsePubMed(pubmedstring);
        assertNotNull(entry);
        assertEquals("2015",entry.getYear());
        assertEquals("Rajput NK, Singh V, Bhardwaj A",entry.getAuthorList());
        assertEquals("Resources, challenges and way forward in rare mitochondrial diseases research. Version 2",entry.getTitle());
        assertEquals("F1000Res",entry.getJournal());
        assertEquals("4",entry.getVolume());
        assertEquals("70",entry.getPages());
        assertEquals("26180633",entry.getPmid());
    }
}
