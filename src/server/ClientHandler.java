package server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final List<ClientHandler> allClients; // Ссылка на общий список
    private final String userName;
    private PrintWriter out;

    public ClientHandler(Socket socket, List<ClientHandler> allClients) {
        this.socket = socket;
        this.allClients = allClients;
        this.userName = "User-" + (100 + new Random().nextInt(900));
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
            sendResponse("Привет, " + userName + "! Ты в чате.", writer);

            while (true) {
                if (!reader.hasNextLine()) {
                    break;
                }
                String message = reader.nextLine().strip();
                if (isQuitMsg(message)) {
                    break;
                }
                broadcast(message);
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

    private void broadcast(String message) {
        String formattedMessage = userName + ": " + message;
        System.out.println("Рассылка: " + formattedMessage);

        for (ClientHandler client : allClients) {
            if (client != this) {
                client.sendRawMessage(formattedMessage);
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

    private void sendResponse(String response, Writer writer) throws IOException {
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }

    private boolean isQuitMsg(String message) {
        return "bye".equalsIgnoreCase(message);
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
