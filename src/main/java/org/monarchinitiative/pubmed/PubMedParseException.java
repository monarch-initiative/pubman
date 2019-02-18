package org.monarchinitiative.pubmed;


/**
 * @author <a href="mailto:daniel.danis@jax.org">Daniel Danis</a>
 */
public class PubMedParseException extends Exception {

    public PubMedParseException() {
    }


    public PubMedParseException(String message) {
        super(message);
    }


    public PubMedParseException(Throwable cause) {
        super(cause);
    }
}
