package bot.service.updates.handlers.commands;

public class HelpMessageHandler implements MessageHandler {

    @Override
    public String reply() {
        return  "Получить информацию об игре в магазине Google Play можно двумя способами: " +
                "\n\n1) По ссылке на игру (команда /game) - работает всегда, если введенная Вами ссылка является корректной. " +
                "\n2) По названию игры (команда /library) - срабатывает только в том случае, если игра уже находится в библиотеке бота. " +
                "\n\nНовая игра добавляется в библиотеку каждый раз при запросе с помощью команды /game, либо при прямом запросе по ссылке.";
    }
}
