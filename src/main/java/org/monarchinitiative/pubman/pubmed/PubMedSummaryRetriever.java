package org.monarchinitiative.pubman.pubmed;

import org.monarchinitiative.pubman.except.PubmanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class contains methods which can be used to retrieve PubMed summary text of publication which corresponds to
 * provided PMID. Use {@link #getSummary(String)} method to get the summary text.
 */
public class PubMedSummaryRetriever {
    private static final Logger LOGGER = LoggerFactory.getLogger(PubMedSummaryRetriever.class);
    /**
     * This is a template for URL targeted for PubMed's REST API.
     */
    private static final String URL_TEMPLATE = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&id=%s&retmode=xml";

    /**
     * PubMed returns a response containing this String, if there is no record associated with given PMID.
     */
    private static final String NON_EXISTING_PMID = "cannot get document summary";



    private final Function<String, InputStream> connectionFactory;


    public PubMedSummaryRetriever(Function<String, InputStream> connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    /**
     * Use default connection factory that is available through method {@link PubMedSummaryRetriever#getConnectionFactory()}.
     * <p>
     * This constructor should work in most cases.
     */
    public PubMedSummaryRetriever() {
        this(getConnectionFactory());
    }

    /**
     * Enter PMID and retrieve PubMed summary text with description of the corresponding publication.
     *
     * @param pmid String with PMID of the publication.
     * @return String with summary text if the retrieval was successful
     * @throws IOException if retrieval fails
     */
    public static String getSummary(InputStream is, String pmid) throws PubmanException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String htmlResponse = reader.lines().collect(Collectors.joining(" "));

        if (htmlResponse.contains(NON_EXISTING_PMID)) { // the entry for submitted pmid doesn't exist.
            throw new PubmanException("PMID " + pmid + " is not associated with any publication on Pubmed");
        } else {
            return htmlResponse;
        }
    }

    /**
     * PubMed API returns whole HTML page in response to the query. This method extracts the relevant part of the response.
     *
     * @param payload String with HTML page
     * @return String with extracted
     */
    private static String extractContent(String payload) throws PubmanException {
        int start, stop;
        start = payload.indexOf("<pre>") + 5;
        stop = payload.indexOf("</pre>");
        if (stop < 0) {
            throw new PubmanException(String.format("Could not extract content from PMID: %s", payload));
        }
        String content = payload.substring(start, stop);
        return content.replaceAll("\n", "").trim();
    }


    public static Function<String, InputStream> getConnectionFactory() {
        return pmid -> {
            String urlString = String.format(URL_TEMPLATE, pmid);
            try {
                URL url = new URL(urlString);
                return url.openStream();
            } catch (IOException e) {
                LOGGER.warn("Error when trying to open URL '{}' for reading", urlString, e);
                return null;
            }
        };
    }

    public String getSummary(String pmid) throws PubmanException {
        try (InputStream is = connectionFactory.apply(pmid)) {
            return getSummary(is, pmid);
        } catch (IOException e) {
            throw new PubmanException(e.getMessage());
        }
    }
}
