package org.monarchinitiative.pubman.pubmed;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.monarchinitiative.pubman.except.PubmanException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PubmedXmlParserTest {

    private static String payload;
    private static PubMedEntry entry;

    @BeforeAll
    static void init() throws IOException  {
        Path path = Paths.get("src", "test", "resources", "pubmedEutilExample.xml");
        List<String> lines = Files.readAllLines(path);
        payload = String.join(" ", lines);
        PubmedXmlParser xmlParser = new PubmedXmlParser(payload);
        entry = xmlParser.getCitation();
    }

    @Test
    void testGotPayload() {
        assertNotNull(payload);
        assertTrue(payload.length() > 0);
    }

    @Test
    void testGotEntry() {
        assertNotNull(entry);
    }

    @Test
    void testJournal() throws PubmanException {
        String journal = "Proc Natl Acad Sci U S A";
        assertEquals(journal, entry.getJournal());
    }

    @Test
    void testYear() {
        String year = "2005";
        assertEquals(year, entry.getYear());
    }

    @Test
    void testIssue () {
        String issue = "102";
        assertEquals(issue, entry.getVolume());
    }

    @Test
    void testTitle() {
        String title = "Extreme hyperopia is the result of null mutations in MFRP, which encodes a Frizzled-related protein.";
        assertEquals(title, entry.getTitle());
    }

    @Test
    void testAuthors() {
        String authors = "Sundin OH, Leppert GS, Silva ED, ";
        assertTrue(entry.getAuthorList().startsWith(authors));
    }

    @Test
    void testNumberOfAuthors() {
        int expectedNumberOfAuthors = 14;
        String authors = entry.getAuthorList();
        System.out.println(authors);
        int n = authors.split(",").length;
        assertEquals(expectedNumberOfAuthors, n);
    }

    @Test
    void testPmid() {
        String pmid = "15976030";
        assertEquals(pmid, entry.getPmid());
    }
    @Test
    void testPages() {
        String pmid = "9553-8";
        assertEquals(pmid, entry.getPages());
    }

}
