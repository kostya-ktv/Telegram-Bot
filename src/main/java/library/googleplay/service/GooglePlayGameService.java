package library.googleplay.service;

import library.googleplay.GooglePlayGame;
import org.jsoup.nodes.Document;
import parser.googleplay.GooglePlayParser;

public final class GooglePlayGameService {
    private GooglePlayParser parser;
    private Document document;

    public GooglePlayGameService(Document document, GooglePlayParser parser) {
        this.document = document;
        this.parser = parser;
    }

    public GooglePlayGame getGooglePlayGame() {
        return GooglePlayGame.builder()
                .title(parser.parseTitle(document))
                .genre(parser.parseGenre(document))
                .price(parser.parsePrice(document))
                .lastUpdated(parser.parseDateOfLastUpdate(document))
                .size(parser.parseInstallationFileSize(document))
                .currentVersion(parser.parseVersion(document))
                .requiresAndroid(parser.parseRequirements(document))
                .iap(parser.parseIAP(document))
                .devEmail(parser.parseContacts(document))
                .pictureURL(parser.parseGamePicture(document))
                .build();
    }
}
