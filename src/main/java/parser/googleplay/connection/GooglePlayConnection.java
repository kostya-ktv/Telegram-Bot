package parser.googleplay.connection;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import parser.googleplay.connection.exceptions.InvalidGooglePlayLinkException;
import parser.googleplay.connection.exceptions.NotGooglePlayLinkException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Log4j2
public final class GooglePlayConnection {

    private String URL;

    public GooglePlayConnection(String URL){
        this.URL = URL;
    }

    public Document connectToGooglePlay() throws IOException, NotGooglePlayLinkException, URISyntaxException, InvalidGooglePlayLinkException {
        Document document;
        final URI link = new URI(URL);
        if (GooglePlayCorrectURL.isLinkValid(link)){
            if (!link.getPath().contains("apps")){
                throw new InvalidGooglePlayLinkException("К сожалению, бот работает исключительно с играми. Введите другую ссылку.");
            }
            URL = forceToRusLocalization(URL);
            document = Jsoup.connect(URL).get();
        }
        else {
            throw new NotGooglePlayLinkException();
        }
        return document;
    }

    private String forceToRusLocalization(String URL) {
        if (URL.endsWith("&hl=ru")){
            return URL;
        }
        else {
            if (URL.contains("&hl=")){
                URL = URL.replace(
                        URL.substring(URL.length()-"&hl=ru".length()), "&hl=ru");
            }
            else {
                URL += "&hl=ru";
            }
        }
        return URL;
    }

    private static class GooglePlayCorrectURL {

        private static final String VALID_HOST = "play.google.com";

        private static final String VALID_PROTOCOL = "https";

        private static final int VALID_PORT = -1;

        private static boolean isLinkValid(URI link) {
            return (isHostExist(link) && isProtocolExist(link) && link.getPort() == VALID_PORT);
        }

        private static boolean isProtocolExist(URI link) {
            if (link.getScheme() != null) {
                return link.getScheme().equals(VALID_PROTOCOL);
            }
            else {
                return false;
            }
        }

        private static boolean isHostExist(URI link) {
            if (link.getHost() != null) {
                return link.getHost().equals(VALID_HOST);
            }
            else {
                return false;
            }
        }

    }
}
