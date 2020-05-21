package bot.service.updates.handlers.commands;

import bot.service.updates.ReplyMessageGenerator;
import library.googleplay.GooglePlayGame;
import library.googleplay.service.GooglePlayGameService;
import library.googleplay.service.LibraryService;
import lombok.extern.log4j.Log4j2;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import parser.googleplay.GooglePlayParser;
import parser.googleplay.connection.GooglePlayConnection;
import parser.googleplay.connection.exceptions.InvalidGooglePlayLinkException;
import parser.googleplay.connection.exceptions.NotGooglePlayLinkException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Log4j2
public class GameMessageHandler implements MessageHandler {

    @Override
    public String reply() {
        return "Чтобы воспользоваться данной командой и получить информацию об игре," +
                " достаточно ввести валидную ссылку в таком формате: " +
                "\nhttps://play.google.com/store/apps/details?id=com.mobge.Oddmar " +
                "\n\nДоступна полная версия команды: " +
                "\n/game https://play.google.com/store/apps/details?id=com.mobge.Oddmar";
    }

    private GooglePlayGame getRequiredGame(String URL) throws NotGooglePlayLinkException, IOException, URISyntaxException, InvalidGooglePlayLinkException {
        return new GooglePlayGameService(getDocument(URL), new GooglePlayParser()).getGooglePlayGame();
    }

    private Document getDocument(String URL) throws NotGooglePlayLinkException, IOException, URISyntaxException, InvalidGooglePlayLinkException {
        return new GooglePlayConnection(URL).connectToGooglePlay();
    }

    public void handleGameMessage(String URL, long chat_id, ReplyMessageGenerator replyGenerator) {
        try {
            if (URL.startsWith(ChatCommands.GAME.getDescription())){
                URL = URL.substring(ChatCommands.GAME.getDescription().length()).trim();
            }
            GooglePlayGame game = getRequiredGame(URL);

            if (!isGenreValid(game)){
                replyGenerator.sendTextMessage(chat_id, "К сожалению, бот работает исключительно с играми. Введите другую ссылку.");
                throw new InvalidGooglePlayLinkException();
            }
            replyGenerator.sendTextMessage(chat_id, game.toString());
            LibraryService.addToLibrary(game.getTitle(), game);
        }
        catch (HttpStatusException e){
            log.error("[ОШИБКА ПОДКЛЮЧЕНИЯ К GOOGLE PLAY]: {}", e.getStatusCode());
            replyGenerator.sendTextMessage(chat_id, "Не удаётся получить доступ к магазину Google Play." +
                    "\nВозможно, что игра изъята из магазина, но по-прежнему доступна тем, кто купил/установил её ранее.");
        }
        catch (InvalidGooglePlayLinkException e){
            log.error("[НЕВЕРНАЯ ССЫЛКА: относится к Google Play, но не имет отношения к разделу Игр]: \n" + URL);
            replyGenerator.sendTextMessage(chat_id, e.getMessage());
        }
        catch (NotGooglePlayLinkException | URISyntaxException | IOException e) {
            log.error("[НЕВЕРНАЯ ССЫЛКА]: " + URL);
            replyGenerator.sendTextMessage(chat_id, "Ссылка не имеет отношения к магазину Google Play.");
        }
    }

    private boolean isGenreValid (GooglePlayGame game) {
        Set<String> availableGenres = new HashSet<>(Arrays.asList("Аркады", "Викторины", "Головоломки", "Гонки", "Казино", "Казуальные", "Карточные", "Музыка", "Настольные игры",
                "Настольные", "Обучающие", "Приключения", "Ролевые", "Симуляторы", "Словесные игры", "Словесные", "Спортивные игры", "Спортивные", "Стратегии", "Экшен")
        );
        return Stream.of(game.getGenre().split(" "))
                .anyMatch(availableGenres::contains);
    }
}

