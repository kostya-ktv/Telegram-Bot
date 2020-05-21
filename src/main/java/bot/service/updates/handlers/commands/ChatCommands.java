package bot.service.updates.handlers.commands;

public enum ChatCommands {
    START("/start"),
    HELP("/help"),
    GAME("/game"),
    LIBRARY("/library"),
    CONTACTS("/contacts"),
    RANDOM("/random");

    private String description;

    ChatCommands(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
