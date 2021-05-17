package scribejava.core.exceptions;

import com.github.scribejava.core.exceptions.OAuthException;

/**
 * @author Pablo Fernandez
 */
public class OAuthConnectionException extends OAuthException {

    private static final String MSG = "There was a problem while creating a connection to the remote service: ";

    public OAuthConnectionException(final String url, final Exception e) {
        super(MSG + url, e);
    }
}
