package bot.service.updates.handlers.commands;


import bot.service.updates.ReplyMessageGenerator;
import library.googleplay.service.LibraryService;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
public class LibraryMessageHandler implements MessageHandler {

    @Override
    public String reply() {
        return "Чтобы получить информацию об игре, которая уже находится в библиотеке бота, " +
                "введите её полное название после команды через пробел: \n\n/library Oddmar" +
                "\n\nНазвание должно соответствовать названию игры в магазине Google Play (поиск не чувствителен к регистру)." +
                "\n\nКоличество игр в библиотеке: " + LibraryService.getLibrary().size() +
                "\n\nСлучайные игры из библиотеки:\n\n" + getRandomTitles();
    }

    private String getRandomTitles(){
        if (LibraryService.getLibrary().size() < 10){
            return String.join("\n", LibraryService.getLibrary().keySet());
        }
        List<String> keys = new ArrayList<>(LibraryService.getLibrary().keySet());
        Collections.shuffle(keys);
        List<String> randomKeys = keys.subList(0, 10);
        return String.join("\n", randomKeys);
    }

    public void handleLibraryMessage(String message, long chat_id, ReplyMessageGenerator replyGenerator){
        if (!(message.contains("Библиотека"))
                && message.length() > ChatCommands.LIBRARY.getDescription().length()) {
            String title = message.replace(ChatCommands.LIBRARY.getDescription(), "").trim();
            if (LibraryService.getLibrary().containsKey(title)){
                title = LibraryService.getLibrary().get(title).getTitle();
                replyGenerator.sendInlineKeyboardMessage(chat_id, title);
            }
            else {
                replyGenerator.sendTextMessage(chat_id, "Такой игры в библиотеке нет!");
                log.info("[Игра {} не найдена в библиотеке]", title);
            }
        }
        else {
            replyGenerator.sendTextMessage(chat_id, reply());
        }
    }

}
