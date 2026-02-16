package server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(
                socket;
                Scanner reader = getReader(socket);
                PrintWriter writer = getWriter(socket)
        ) {
            sendResponse("Привет " + socket.getPort(), writer);

            while (true) {
                if (!reader.hasNextLine()) {
                    System.out.printf("Пользователь %s отключился%n", socket.getPort());
                    break;
                }
                String message = reader.nextLine().strip();
                System.out.printf("Got: %s%n", message);
                if (isQuitMsg(message)) {
                    System.out.printf("Пользователь %s отключился%n", socket.getPort());
                    break;
                }
                sendResponse(message.toUpperCase(), writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Клиент отключен!");
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
