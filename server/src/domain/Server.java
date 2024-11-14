package domain;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Server {
    private String serverIp;
    private int serverPort;
    private String serverName;
    private ServerSocket serverSocket;
    private List<String> bannedWords = new ArrayList<>();
    private Map<String, ClientHandler> players = new ConcurrentHashMap<>();

    public Server(String configFilePath) {
        loadConfig(configFilePath);
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

    public void start() {
        System.out.println(serverName + " launched on " + serverIp + ":" + serverPort);
        try{
            serverSocket = new ServerSocket(serverPort);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                new Thread(clientHandler).start();
            }
        }catch (IOException e) {
            System.err.println();
        }
    }

    public void broadcastMessage(String message, String sender) {
        if (isMessageAllowed(message)) {
            for (ClientHandler client : players.values()) {
                if (!client.getClientName().equals(sender)) {
                    client.sendMessage("[" + sender + "]: " + message);
                }
            }
        } else {
            players.get(sender).sendMessage("Your message contains prohibited words and will not be sent.");
        }
    }

    public synchronized void registerClient(String clientName, ClientHandler clientHandler) {
        players.put(clientName, clientHandler);
        updateClientList();
    }

    public synchronized void unregisterClient(String clientName) {
        players.remove(clientName);
        updateClientList();
    }

    private void updateClientList() {
        String clientList = String.join(", ", players.keySet());
        for (ClientHandler client : players.values()) {
            client.sendMessage("Number of connected clients: " + clientList);
        }
    }

    private boolean isMessageAllowed(String message) {
        for (String banword : bannedWords) {
            if (message.toLowerCase().contains(banword)) {
                return false;
            }
        }
        return true;
    }
}
