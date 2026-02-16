package server;

import commands.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final List<ClientHandler> allClients; // Ссылка на общий список
    private String userName;
    private PrintWriter out;
    private final Map<String, Command> commands = new HashMap<>();

    public ClientHandler(Socket socket, List<ClientHandler> allClients) {
        this.socket = socket;
        this.allClients = allClients;
        this.userName = "User-" + (100 + new Random().nextInt(900));
        initCommands();
    }

    private void initCommands() {
        commands.put("/name", new NameCommand());
        commands.put("/list", new ListCommand());
        commands.put("/whisper", new WhisperCommand());
        commands.put("/bye", new ByeCommand());
        commands.put("/time", new TimeCommand());
        commands.put("/date", new DateCommand());
        commands.put("/reverse", new ReverseCommand());
        commands.put("/upper", new UpperCommand());
    }

    @Override
    public void run() {
        try(
                socket;
                Scanner reader = getReader(socket);
                PrintWriter writer = getWriter(socket)
        ) {
            this.out = writer;
            allClients.add(this);
            System.out.printf("Клиент %s (порт %s) вошел в чат%n", userName, socket.getPort());
            broadcast("присоединился к чату!");
            sendResponse("Привет, " + userName + "! Ты в чате.\nДоступные команды: " + commands.keySet(), writer);

            while (reader.hasNextLine()) {
                String message = reader.nextLine().strip();
                if (message.isEmpty()){
                    continue;
                }
                if (message.startsWith("/")) {
                    String[] parts = message.split(" ", 2);
                    String cmdLabel = parts[0].toLowerCase();
                    String str = parts.length > 1 ? parts[1] : "";

                    Command command = commands.getOrDefault(cmdLabel, new DefaultCommand());
                    CommandResult result = command.execute(str, this);

                    sendResponse(result.getReply(), writer);
                    if (result.shouldClose()) break;
                } else {
                    broadcast(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.printf("Пользователь %s отключился%n", userName);
            allClients.remove(this);
            broadcast("покинул чат.");
        }

        System.out.println("Клиент отключен!");
    }

    public void broadcast(String message) {
        broadcastCustom(userName + ": " + message);
    }

    public void broadcastCustom(String fullMessage) {
        for (ClientHandler client : allClients) {
            if (client != this) {
                client.sendRawMessage(fullMessage);
            }
        }
    }

    public void sendRawMessage(String rawMessage) {
        if (out != null) {
            out.write(rawMessage);
            out.write(System.lineSeparator());
            out.flush();
        }
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public List<ClientHandler> getAllClients() {
        return allClients;
    }

    private void sendResponse(String response, Writer writer) throws IOException {
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream os = socket.getOutputStream();
        return new PrintWriter(os);
    }

    private Scanner getReader(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        return new Scanner(isr);
    }
}
