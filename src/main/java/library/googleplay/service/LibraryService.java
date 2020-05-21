package library.googleplay.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import library.googleplay.GooglePlayGame;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Log4j2
public final class LibraryService {

    private static final String LIBRARY_PATH = "./library/gpgames_library.json";

    private static ConcurrentMap<String, GooglePlayGame> games = new ConcurrentSkipListMap<>(String.CASE_INSENSITIVE_ORDER);

    public static ConcurrentMap<String, GooglePlayGame> getLibrary() {
        return games;
    }

    public static ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .enable(SerializationFeature.INDENT_OUTPUT);

    static {
        TypeFactory typeFactory = mapper.getTypeFactory();
        MapType mapType = typeFactory.constructMapType(ConcurrentSkipListMap.class, String.class, GooglePlayGame.class);

        try {
            Path path = Paths.get(LIBRARY_PATH);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                log.info("[Файл библиотеки создан]");
            }
            else {
                ConcurrentMap<String, GooglePlayGame> temporary = mapper.readValue(new File(LIBRARY_PATH), mapType);
                games.putAll(temporary);
                log.info("[Количество игр в загруженной библиотеке] = " + games.size());
            }
        }
        catch (IOException e) {
            log.error("[Ошибка при чтении/записи файла] {}", e.getMessage());
        }
    }

    public static void addToLibrary(String title, GooglePlayGame game) {
        if (game.equals(games.get(title))){
            log.info("[Игра {} уже есть в библиотеке!]", title);
            return;
        }
        games.put(title, game);
        log.info("[Игра {} добавлена в библиотеку]", title);
        saveLibraryToFile();
    }

    private static void saveLibraryToFile() {
        try {
            mapper.writeValue(new File(LIBRARY_PATH), games);
            log.info("[Библиотека записана в файл]");
        } catch (IOException e) {
            log.error("[Не удалось записать библиотеку в файл] {}", e.getMessage());
        }
    }
}
