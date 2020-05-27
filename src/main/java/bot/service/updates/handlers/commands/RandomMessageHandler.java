package bot.service.updates.handlers.commands;

import library.googleplay.GooglePlayGame;
import library.googleplay.service.LibraryService;

import java.util.Random;

public class RandomMessageHandler implements MessageHandler {

    @Override
    public String reply() {
        return getRandomGame().toString();
    }

    public GooglePlayGame getRandomGame(){
        Object [] values = LibraryService.getLibrary().values().toArray();
        return (GooglePlayGame) values[new Random().nextInt(values.length)];
    }
}
