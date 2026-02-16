package commands;

import server.ClientHandler;

public class ByeCommand implements Command {
    @Override
    public CommandResult execute(String str, ClientHandler handler) {
        return new CommandResult("Bye bye!", true);
    }
}
