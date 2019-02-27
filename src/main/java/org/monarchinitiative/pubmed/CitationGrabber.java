package org.monarchinitiative.pubmed;


import com.google.common.collect.ImmutableList;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This call to NCBI eUtils grabs the PMIDs that cite the given article in PubMed
 * https://eutils.ncbi.nlm.nih.gov/entrez/eutils/elink.fcgi?dbfrom=pubmed&linkname=pubmed_pubmed_citedin&id=21876726&id=21876761
 */
public class CitationGrabber {
    private static final Logger logger = LogManager.getLogger();
    private final String pmid;

    public CitationGrabber(String pmid) {
        this.pmid=pmid;
    }


    public List<String> citingPMIDs() {
        ImmutableList.Builder<String> builder = new ImmutableList.Builder<>();
        final String USER_AGENT = "Mozilla/5.0";
        String url = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/elink.fcgi?dbfrom=pubmed&linkname=pubmed_pubmed_citedin&id=" +this.pmid;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String resp = response.toString().replaceAll("\\s+","");
            logger.trace("Got back payload {}", resp);
            // This is XML but all we need are the <Id>23434893</Id> elements
            Pattern pat = Pattern.compile("<Id>(\\d+)</Id>");
            Matcher mat = pat.matcher(resp);
            while (mat.find()) {
                String pmid = mat.group(1);
                builder.add(pmid);
                System.out.println(pmid);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return builder.build();
    }



}
