package parser.googleplay;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parser.Parser;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class GooglePlayParser implements Parser {

    private static final String CURRENT_VERSION = "div:matchesOwn(^Текущая версия$)";

    private static final String LAST_UPDATED = "div:matchesOwn(^Обновлено$)";

    private static final String REQUIREMENTS = "div:matchesOwn(^Требуемая версия Android$)";

    private static final String CONTACTS = "div:matchesOwn(^Разработчик$)";

    private static final String IAP = "div:matchesOwn(^Платный контент$)";

    private static final String APK_SIZE = "div:matchesOwn(^Размер$)";

    private static final String TRADEMARK_SYMBOL = "\u2122";

    private static final String REGISTRED_SIGN = "\u00AE";

    @Override
    public String parseGenre(Document document) {
        return removeGenreDuplicates(document.getElementsByAttributeValue("itemprop", "genre").text());
    }

    @Override
    public String parseInstallationFileSize(Document document) {
        return parseIfPresent(document, APK_SIZE);
    }

    @Override
    public String parseTitle(Document document) {
        String title = document.getElementsByAttributeValue("itemprop", "name").text();
        if (title.contains(TRADEMARK_SYMBOL)){
                title = title.replace(TRADEMARK_SYMBOL, "").trim();
        }
        if (title.contains(REGISTRED_SIGN)){
            title = title.replace(REGISTRED_SIGN, "").trim();
        }
        return title;
    }

    @Override
    public String parseIAP(Document document) {
        String IAP = parseIfPresent(document, GooglePlayParser.IAP);
        return (IAP.equals("Нет информации")) ? "Отсутствуют" : IAP;
    }

    @Override
    public String parseContacts(Document document) {
        String email = "https://support.google.com/googleplay/";
        Matcher m = Pattern.compile(EMAIL_REGEX).matcher(parseIfPresent(document, CONTACTS));
        while (m.find()) {
            email = m.group();
        }
        return email;
    }

    @Override
    public String parsePrice(Document document) {
        String price = safeParsePrice(document);
        return price.equals("0") ? "Бесплатно" : price;
    }

    private String safeParsePrice(Document document) {
        Elements metaElements = document.getElementsByTag("meta");
        for (Element tag : metaElements) {
            String content = tag.attr("content");
            String itemprop = tag.attr("itemprop");

            if ("price".equals(itemprop)) {
                return content;
            }
        }
        return "Информация не доступна";
    }

    @Override
    public String parseVersion(Document document) {
        return parseIfPresent(document, CURRENT_VERSION);
    }

    @Override
    public String parseRequirements(Document document) {
        return parseIfPresent(document, REQUIREMENTS);
    }

    @Override
    public String parseDateOfLastUpdate(Document document) {
        return parseIfPresent(document, LAST_UPDATED);
    }

    public String parseGamePicture(Document document) {
        Elements metaTags = document.getElementsByTag("meta");
        for (Element tag : metaTags) {
            String content = tag.attr("content");
            String itemprop = tag.attr("name");

            if ("twitter:image".equals(itemprop)) {
                return content;
            }
        }
        return null;
    }

    private String parseByAttribute(Document document, String pattern) {
        return document.select(pattern)
                .first()
                .parent()
                .select("span")
                .first()
                .text();
    }

    private boolean isPresent(Document document, String pattern) {
        return !(document.select(pattern)
                .text()
                .isEmpty());
    }

    private String parseIfPresent(Document document, String pattern) {
        return isPresent(document, pattern) ? parseByAttribute(document, pattern) : "Нет информации";
    }

    private String removeGenreDuplicates(String genre) {
        return Arrays.stream(genre.split("\\s+"))
                     .distinct()
                     .collect(Collectors.joining(" "));
    }
}
