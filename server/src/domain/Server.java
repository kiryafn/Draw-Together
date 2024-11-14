package domain;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private String serverIp;
    private int serverPort;
    private String serverName;
    private ServerSocket serverSocket;
    private List<String> bannedWords = new ArrayList<>();
    private Map<String, ClientHandler> players = new HashMap<>();
    private ChatManager chatManager;
    private DrawingManager drawingManager;

    public Server(String configFilePath) {
        loadConfig(configFilePath);
        chatManager = new ChatManager(this);
        drawingManager = new DrawingManager(this);
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

}
