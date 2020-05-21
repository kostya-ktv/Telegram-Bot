package bot;

import bot.service.Bot;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Log4j2
public class BotApplication {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
            log.info("[Бот успешно запущен]");
        } catch (TelegramApiRequestException e) {
            log.error("[Не удалось запустить бота]: {}", e.toString());
        }
    }
}
