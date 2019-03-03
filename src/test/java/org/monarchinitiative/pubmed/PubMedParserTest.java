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
}
