package bot.service.updates.handlers;

import bot.service.updates.ReplyMessageGenerator;
import bot.service.updates.handlers.commands.*;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j2
public class TextMessageHandler {

    private Update update;
    private ReplyMessageGenerator replyGenerator;

    public TextMessageHandler(Update update, ReplyMessageGenerator replyGenerator){
        this.update = update;
        this.replyGenerator = replyGenerator;
    }

    public void handleTextMessage(){
        long chat_id = update.getMessage().getChatId();
        String message = update.getMessage().getText();

        if (message.equals(ChatCommands.START.getDescription())) {
            replyGenerator.sendTextMessage(chat_id, new StartMessageHandler().reply());
            replyGenerator.sendReplyKeyboardMessage(chat_id);
        }
        else if (message.equals(ChatCommands.HELP.getDescription())
                || message.equalsIgnoreCase("Помощь")) {
            replyGenerator.sendTextMessage(chat_id, new HelpMessageHandler().reply());
        }
        else if (message.equals(ChatCommands.GAME.getDescription())) {
            replyGenerator.sendTextMessage(chat_id, new GameMessageHandler().reply());
        }
        else if (message.equals(ChatCommands.RANDOM.getDescription())) {
            replyGenerator.sendMessageWithPicture(chat_id, new RandomMessageHandler().getRandomGame());
        }
        else if (message.equals(ChatCommands.CONTACTS.getDescription())
                || message.equalsIgnoreCase("Исходный код проекта")) {
            replyGenerator.sendTextMessage(chat_id, new ContactsMessageHandler().reply());
        }
        else if ((message.startsWith(ChatCommands.GAME.getDescription()) && message.contains("http"))
                || message.startsWith("http")) {
            new GameMessageHandler().handleGameMessage(message, chat_id, replyGenerator);
        }
        else if (message.startsWith(ChatCommands.LIBRARY.getDescription())
                || message.equalsIgnoreCase("Библиотека")){
            new LibraryMessageHandler().handleLibraryMessage(message, chat_id, replyGenerator);
        }
        else {
            replyGenerator.sendTextMessage(chat_id, "Такой команды я не знаю!\n" +
                    "Список доступных команд можно посмотреть, набрав /start");
            log.info("[Неизвестная команда]: {}", message);
        }
    }
}