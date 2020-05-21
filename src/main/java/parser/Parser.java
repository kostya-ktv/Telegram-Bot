package parser;

import org.jsoup.nodes.Document;

public interface Parser {
    String EMAIL_REGEX = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";

    String parseTitle(Document document);

    String parseGenre(Document document);

    String parsePrice(Document document);

    String parseContacts(Document document);

    String parseVersion(Document document);

    String parseRequirements(Document document);

    String parseDateOfLastUpdate(Document document);

    String parseIAP(Document document);

    String parseInstallationFileSize(Document document);
}
