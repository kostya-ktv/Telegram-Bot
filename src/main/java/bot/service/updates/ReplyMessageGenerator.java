package bot.service.updates;

import bot.service.keyboards.builders.InlineKeyboardMarkupBuilder;
import bot.service.keyboards.builders.ReplyKeyboardMarkupBuilder;
import bot.service.Bot;
import library.googleplay.GooglePlayGame;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static java.lang.Math.toIntExact;

@Log4j2
public class ReplyMessageGenerator extends Bot {

    public synchronized void sendTextMessage(long chat_id, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false)
                .setChatId(chat_id)
                .setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("[Не удалось отправить текстовое сообщение]: {}", e.getMessage());
        }
    }

    public synchronized void sendMessageWithPicture(long chat_id, GooglePlayGame game){
        String pictureURL = game.getPictureURL();
        if (pictureURL == null){
            pictureURL = "https://www.cyclonis.com/images/2020/03/googleplay.jpg";
        }
        SendPhoto photo_message = new SendPhoto().setChatId(chat_id)
                .setPhoto(pictureURL)
                .setCaption(game.toString());
        try {
            execute(photo_message);
        }
        catch (TelegramApiException e) {
            log.error("[Не удалось отправить сообщение с картинкой]: {}", e.getMessage());
        }

    }

    public synchronized void sendEditedTextMessage(long chat_id, long message_id) {
        EditMessageText new_message = new EditMessageText()
                .setChatId(chat_id)
                .setMessageId(toIntExact(message_id))
                .setText("Клавиатура скрыта");
        try {
            execute(new_message);
        } catch (TelegramApiException e) {
            log.error("[Не удалось отправить исправленное сообщение]: {}", e.getMessage());
        }
    }

    public synchronized void sendInlineKeyboardMessage(long chat_id, String gameTitle) {
        SendMessage keyboard = InlineKeyboardMarkupBuilder.create(chat_id)
                .setText("Вы может узнать следующую информацию об игре " + gameTitle)
                .row()
                .button("Стоимость " + "\uD83D\uDCB0", "/price " + gameTitle)
                .button("Обновлено " + "\uD83D\uDDD3", "/updated " + gameTitle)
                .button("Версия " + "\uD83D\uDEE0", "/version " + gameTitle)
                .endRow()
                .row()
                .button("Требования " + "\uD83D\uDCF5", "/requirements " + gameTitle)
                .button("Покупки " + "\uD83D\uDED2", "/iap " + gameTitle)
                .button("Размер " + "\uD83D\uDD0E", "/size " + gameTitle)
                .endRow()
                .row()
                .button("Получить всю информацию об игре" + "\uD83D\uDD79", "/all " + gameTitle)
                .endRow()
                .row()
                .button("Скрыть клавиатуру", "close")
                .endRow()
                .build();
        try {
            execute(keyboard);
        } catch (TelegramApiException e) {
            log.error("[Не удалось отправить сообщение с -inline- клавиатурой]: {}", e.getMessage());
        }
    }

    public synchronized void sendReplyKeyboardMessage(long chat_id){
        SendMessage keyboard = ReplyKeyboardMarkupBuilder.create(chat_id)
                .setText("Открываю клавиатуру...")
                .row()
                .button("Помощь")
                .button("Библиотека")
                .endRow()
                .row()
                .button("Исходный код проекта")
                .endRow()
                .build();
        try {
            execute(keyboard);
        } catch (TelegramApiException e) {
            log.error("[Не удалось отправить сообщение с -reply- клавиатурой]: {}", e.getMessage());
        }
    }
}

