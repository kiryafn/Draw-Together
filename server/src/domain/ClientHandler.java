package domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Server server;
    private PrintWriter out;
    private BufferedReader in;
    private String clientName;
    private int clientPort;
    private ChatManager chatManager;

    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
        this.chatManager = new ChatManager(server);
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Чтение имени клиента
            clientName = in.readLine();
            if (clientName == null) {
                System.err.println("Received null client name.");
                return;
            }

            clientPort = clientSocket.getPort();
            server.registerClient(clientName, this);
            System.out.println("Client connected: " + clientName);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/to")) chatManager.processPrivateMessage(message, clientName);
                else if (message.startsWith("/notto")) chatManager.processPrivateExcludeMessage(message, clientName);
                else chatManager.broadcastMessage(message, clientName);

                System.out.println("Received message from " + clientName + ": " + message);
            }





        } catch (IOException e) {
            System.err.println("Connection error with: " + clientName + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            server.unregisterClient(clientName);
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendSenderNickname(String message) {
        out.println(message);
    }

    public void sendAcessModifier(String message){
        out.println(message);
    }

    public String getClientName() {
        return clientName;
    }
}
