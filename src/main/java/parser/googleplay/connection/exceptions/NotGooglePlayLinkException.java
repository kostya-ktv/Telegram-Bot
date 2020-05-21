package parser.googleplay.connection.exceptions;

public class NotGooglePlayLinkException extends Exception {

    public NotGooglePlayLinkException() {
        super();
    }

    public NotGooglePlayLinkException(String message) {
        super(message);
    }

    public NotGooglePlayLinkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotGooglePlayLinkException(Throwable cause) {
        super(cause);
    }
}
