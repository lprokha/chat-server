package commands;

public class CommandResult {
    private final String reply;
    private final boolean shouldClose;

    public CommandResult(String reply, boolean shouldClose) {
        this.reply = reply;
        this.shouldClose = shouldClose;
    }

    public String getReply() {
        return reply;
    }

    public boolean shouldClose() {
        return shouldClose;
    }
}
