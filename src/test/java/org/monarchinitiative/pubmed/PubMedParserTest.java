package org.monarchinitiative.pubmed;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PubMedParserTest {



    @Test
    void testNoVolumePages() throws PubMedParseException{
        String pubmedstring="Gainotti S, Mascalzoni D, Bros-Facer V, Petrini C, Floridia G, Roos M, Salvatore M, Taruscio D. Meeting Patients' Right to the Correct Diagnosis: Ongoing International Initiatives on Undiagnosed Rare Diseases and Ethical and Social Issues. Int J Environ Res Public Health. 2018 Sep 21;15(10). pii: E2072. doi: 10.3390/ijerph15102072. Review. PubMed PMID: 30248891; PubMed Central PMCID: PMC6210164.\n" +
                "\tat org.monarchinitiative.pubmed.PubMedParser.parsePubMed(PubMedParser.java:105)";
        PubMedEntry entry =  PubMedParser.parsePubMed(pubmedstring);
        assertNotNull(entry);
        assertEquals(entry.getPages(),"pii: E2072");
    }
}
