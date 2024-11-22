package domain;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private final Object object = new Object();
    private String serverIp;
    private int serverPort;
    private List<String> bannedWords = new ArrayList<>();
    private final Map<String, ClientHandler> players = new HashMap<>();

    public Server(String configFilePath) {
        loadConfig(configFilePath);
    }

    public void start() {
        System.out.println("Server launched on " + serverIp + ":" + serverPort);
        try{
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig(String configFilePath) {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream(configFilePath);
            properties.load(input);
            serverIp = properties.getProperty("server_ip");
            serverPort = Integer.parseInt(properties.getProperty("server_port"));
            bannedWords = Arrays.asList(properties.getProperty("banned_words").split(","));
        } catch (IOException e) {
            System.err.println("Failed to load config file " + configFilePath);
        }
    }

    public void registerClient(String clientName, ClientHandler clientHandler) {
        synchronized (object) {
            players.put(clientName, clientHandler);
            for (ClientHandler client : players.values()) {
                client.sendMessage(clientName + " connected");
                client.sendMessage("Server");
                client.sendAccessModifier("");
            }
            updateClientList();
        }
    }

    public void unregisterClient(String clientName) {
        synchronized (object) {
            players.remove(clientName);
            for (ClientHandler client : players.values()) {
                client.sendMessage(clientName + " disconnected");
                client.sendMessage("Server");
                client.sendAccessModifier("");
            }
            updateClientList();
        }
    }

    private void updateClientList() {
        String clientList = String.join(", ", players.keySet());
        for (ClientHandler client : players.values()) {
            client.sendMessage(players.size() + " connected clients: " + clientList);
            client.sendMessage("Server");
            client.sendAccessModifier("All");
        }
    }

    public List<String> getBannedWords() {
        return bannedWords;
    }

    public Map<String, ClientHandler> getPlayers() {
        return players;
    }
}
