package org.monarchinitiative.pubman.except;


/**
 * @author <a href="mailto:daniel.danis@jax.org">Daniel Danis</a>
 */
public class PubMedParseException extends PubmanException {

    public PubMedParseException() {
    }


    public PubMedParseException(String message) {
        super(message);
    }


    public PubMedParseException(Throwable cause) {
        super(cause.getMessage());
    }
}
