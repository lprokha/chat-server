package commands;

import server.ClientHandler;

public class WhisperCommand implements Command {
    @Override
    public CommandResult execute(String str, ClientHandler handler) {
        String[] parts = str.split(" ", 2);
        if (parts.length < 2) {
            return new CommandResult("Ошибка: напишите \"/whisper имя сообщение\"", false);
        }

        String targetName = parts[0];
        String message = parts[1];

        for (ClientHandler client : handler.getAllClients()) {
            if (client.getUserName().equalsIgnoreCase(targetName)) {
                client.sendRawMessage(String.format("[Шепот от %s]: %s", handler.getUserName(), message));
                return new CommandResult("[Вы шепнули " + targetName + "]: " + message, false);
            }
        }

        return new CommandResult("Ошибка: пользователь " + targetName + " не найден.", false);
    }
}
