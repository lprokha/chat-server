package commands;

import server.ClientHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateCommand implements Command {

    @Override
    public CommandResult execute(String str, ClientHandler handler) {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        return new CommandResult(date, false);
    }
}
