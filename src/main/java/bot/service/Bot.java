package bot.service;

import bot.service.updates.UpdatesReceiver;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Log4j2
public class Bot extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = "Google Play Parser Bot";

    private static final String BOT_TOKEN = System.getenv("TOKEN");

    @Override
    public void onUpdateReceived(Update update) {
        UpdatesReceiver.handleUpdates(update);
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
