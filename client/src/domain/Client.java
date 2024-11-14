package domain;

import ui.MyFrame;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private String nickname;
    private MyFrame frame = new MyFrame();

    public Client(String serverAddress, int serverPort) {
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Инициализация имени клиента
            Scanner scanner = new Scanner(System.in);
            System.out.print("Введите ваше имя: ");
            nickname = scanner.nextLine();
            out.println(nickname);

            // Поток для чтения сообщений с сервера
            new Thread(new IncomingMessagesHandler()).start();

            // Цикл для отправки сообщений
            while (true) {
                String message = scanner.nextLine();
                if (message.equalsIgnoreCase("exit")) {
                    break; // Завершение работы клиента
                }
                out.println(message);
            }

        } catch (IOException e) {
            System.err.println("Ошибка подключения к серверу: " + e.getMessage());
        } finally {
            closeConnections();
        }
    }

    private void closeConnections() {
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Connection close error" + e.getMessage());
        }
    }

    // Класс для обработки входящих сообщений от сервера
    private class IncomingMessagesHandler implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message); // Печать сообщения от сервера
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении сообщения от сервера: " + e.getMessage());
            }
        }
    }
}
