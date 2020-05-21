package bot.service.updates.handlers.commands;

public class ContactsMessageHandler implements MessageHandler {

    private static final String GITHUB_PROJECT_REPO = "https://github.com/miroha/Telegram-Bot";

    @Override
    public String reply() {
        return GITHUB_PROJECT_REPO;
    }
}

