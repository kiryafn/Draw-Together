package domain;

import ui.MyFrame;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    public static String nickname;
    MyFrame frame = new MyFrame(this);

    public Client(String serverAddress, int serverPort) {
        try {
            nickname = "valera";
            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Инициализация имени клиента;
            out.println(nickname);

            // Поток для чтения сообщений с сервера
            new Thread(new IncomingMessagesHandler(this, frame.getOptionPanel().getChat())).start();

            System.out.println("Connected");

        } catch (IOException e) {
            System.err.println("Connection to server error: " + e.getMessage());
            closeConnections();
        }
    }

    private void closeConnections() {
        try {
            if (socket != null){
                out.close();
                in.close();
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Connection close error:" + e.getMessage());
        }
    }

    public void sendMessageToServer(String message) {
        out.println(message);
    }

    public BufferedReader getInputStream(){
        return in;
    }
}