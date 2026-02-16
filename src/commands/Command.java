package commands;

import server.ClientHandler;

public interface Command {
    CommandResult execute(String str, ClientHandler handler);
}
