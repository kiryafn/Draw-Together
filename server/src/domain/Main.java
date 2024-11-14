package domain;

public class Main {
    public static void main(String[] args) {
        Server server = new Server("/Users/alieksieiev/IdeaProjects/UTP/Project/SocketBased/src/data/configuration.config");
        server.start();
    }
}