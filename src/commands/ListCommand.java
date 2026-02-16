package commands;

import server.ClientHandler;

import java.util.stream.Collectors;

public class ListCommand implements Command {
    @Override
    public CommandResult execute(String str, ClientHandler handler) {
        String userList = handler.getAllClients().stream()
                .map(ClientHandler::getUserName)
                .collect(Collectors.joining(", "));
        return new CommandResult("Пользователи в чате: " + userList, false);
    }
}
