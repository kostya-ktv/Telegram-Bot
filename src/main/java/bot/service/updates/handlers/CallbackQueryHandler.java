package bot.service.updates.handlers;

import bot.service.updates.ReplyMessageGenerator;
import library.googleplay.GooglePlayGame;
import library.googleplay.service.LibraryService;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CallbackQueryHandler {

    private Update update;
    private ReplyMessageGenerator replyGenerator;

    public CallbackQueryHandler(Update update, ReplyMessageGenerator replyGenerator){
        this.update = update;
        this.replyGenerator = replyGenerator;
    }

    public void handleCallBackQuery() {
        String call_data = update.getCallbackQuery().getData();
        long message_id = update.getCallbackQuery().getMessage().getMessageId();
        long chat_id = update.getCallbackQuery().getMessage().getChatId();
        String gameTitle = call_data.substring(call_data.indexOf(" ") + 1);
        GooglePlayGame game = LibraryService.getLibrary().get(gameTitle);
        switch (call_data.split("\\s+")[0]){
            case "/price" :
                replyGenerator.sendTextMessage(chat_id, game.getPrice());
                break;
            case "/updated":
                replyGenerator.sendTextMessage(chat_id, game.getLastUpdated());
                break;
            case "/version":
                replyGenerator.sendTextMessage(chat_id, game.getCurrentVersion());
                break;
            case "/requirements":
                replyGenerator.sendTextMessage(chat_id, game.getRequiresAndroid());
                break;
            case "/iap":
                replyGenerator.sendTextMessage(chat_id, game.getIap());
                break;
            case "/size":
                replyGenerator.sendTextMessage(chat_id, game.getSize());
                break;
            case "/all":
                replyGenerator.sendMessageWithPicture(chat_id, LibraryService.getLibrary().get(gameTitle));
                break;
            case "close":
                replyGenerator.sendEditedTextMessage(chat_id, message_id);
                break;
        }
    }
}

