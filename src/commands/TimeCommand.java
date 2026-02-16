package commands;

import server.ClientHandler;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeCommand implements Command {

    @Override
    public CommandResult execute(String str, ClientHandler handler) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return new CommandResult(time, false);
    }
}
