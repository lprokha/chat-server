package commands;

import server.ClientHandler;

public class NameCommand implements Command {
    @Override
    public CommandResult execute(String newName, ClientHandler handler) {
        if (newName == null || newName.isBlank() || newName.contains(" ")) {
            return new CommandResult("Ошибка: Имя не может быть пустым или содержать пробелы.", false);
        }

        // Проверка на уникальность
        for (ClientHandler client : handler.getAllClients()) {
            if (client.getUserName().equalsIgnoreCase(newName)) {
                return new CommandResult("Ошибка: Имя " + newName + " уже занято.", false);
            }
        }

        String oldName = handler.getUserName();
        handler.setUserName(newName);

        // Уведомляем всех, кроме автора (автор получит персональный ответ через CommandResult)
        handler.broadcastCustom(String.format("Пользователь %s теперь известен как %s", oldName, newName));

        return new CommandResult("Вы теперь известны как " + newName, false);
    }
}
