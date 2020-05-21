package bot.service.updates;

import bot.service.updates.handlers.CallbackQueryHandler;
import bot.service.updates.handlers.TextMessageHandler;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j2
public class UpdatesReceiver {

    public static void handleUpdates(Update update) {
        ReplyMessageGenerator replyGenerator = new ReplyMessageGenerator();
        if (update.hasMessage() && update.getMessage().hasText()){
            log.info("[Update (id {}) типа \"Текстовое сообщение\"]", update.getUpdateId());
            new TextMessageHandler(update, replyGenerator).handleTextMessage();
        }
        else if (update.hasCallbackQuery()) {
            log.info("[Update (id {}) типа \"CallBack от клавиатуры\" с id {}]", update.getUpdateId(), update.getCallbackQuery().getMessage().getMessageId());
            new CallbackQueryHandler(update, replyGenerator).handleCallBackQuery();
        }
        else {
            log.error("[Неверный тип Update]: {}", update.getUpdateId());
            replyGenerator.sendTextMessage(update.getMessage().getChatId(), "Я могу принимать только текстовые сообщения!");
            throw new IllegalArgumentException();
        }
    }
}

