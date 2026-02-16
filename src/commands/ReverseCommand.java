package commands;

import server.ClientHandler;

public class ReverseCommand implements Command {
    @Override
    public CommandResult execute(String str, ClientHandler handler) {
        String reversed = new StringBuilder(str).reverse().toString();
        return new CommandResult(reversed, false);
    }
}
