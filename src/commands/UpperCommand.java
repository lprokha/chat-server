package commands;

import server.ClientHandler;

public class UpperCommand implements Command {
    @Override
    public CommandResult execute(String str, ClientHandler handler) {
        return new CommandResult(str.toUpperCase(), false);
    }
}
