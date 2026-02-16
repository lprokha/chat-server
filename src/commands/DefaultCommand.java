package commands;

import server.ClientHandler;

public class DefaultCommand implements Command {
    @Override
    public CommandResult execute(String str, ClientHandler handler) {
        return new CommandResult("Ошибка: Неизвестная команда.", false);
    }
}
