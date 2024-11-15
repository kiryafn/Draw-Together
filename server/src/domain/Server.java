package domain;

import exceptions.BannedWordException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private String serverIp;
    private int serverPort;
    private String serverName;
    private List<String> bannedWords = new ArrayList<>();
    private final Map<String, ClientHandler> players = new HashMap<>();
    private final ChatManager chatManager = new ChatManager(this);
    private final DrawingManager drawingManager = new DrawingManager(this);

    public Server(String configFilePath) {
        loadConfig(configFilePath);
    }

    public void start() {
        System.out.println(serverName + " launched on " + serverIp + ":" + serverPort);
        try{
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                new Thread(clientHandler).start();
            }
        }catch (IOException e) {
            System.err.println();
        }
    }

    private void loadConfig(String configFilePath) {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream(configFilePath);
            properties.load(input);
            serverIp = properties.getProperty("server_ip");
            serverPort = Integer.parseInt(properties.getProperty("server_port"));
            serverName = properties.getProperty("server_name");
            bannedWords = Arrays.asList(properties.getProperty("banned_words").split(","));
        } catch (IOException e) {
            System.err.println("Failed to load config file " + configFilePath);
        }
    }

    public void registerClient(String clientName, ClientHandler clientHandler) {
        synchronized (players) {
            players.put(clientName, clientHandler);
            updateClientList();
        }
    }

    public void unregisterClient(String clientName) {
        synchronized (players) {
            players.remove(clientName);
            updateClientList();

        }
    }

    private void updateClientList() {
        String clientList = String.join(", ", players.keySet());
        for (ClientHandler client : players.values()) {
            client.sendMessage(players.size() + " connected clients: " + clientList);
        }
    }

    private void isMessageAllowed(String message) throws BannedWordException {
        for (String phrase : bannedWords) {
            if (message.contains(phrase)) {
                throw new BannedWordException("Your message contains prohibited phrase");
            }
        }
    }

    public void broadcastMessage(String message, String sender) {
        try {
            isMessageAllowed(message);
            for (ClientHandler client : players.values()) {
                if (!client.getClientName().equals(sender)) {
                    client.sendMessage("[" + sender + "]: " + message);
                }
            }

        } catch (BannedWordException e) {
            players.get(sender).sendMessage("Your message contains prohibited words and will not be sent.");
        }
    }
}
