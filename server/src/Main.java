import domain.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server("/Users/alieksieiev/IdeaProjects/UTP/Project/DrawTogether/server/src/resources/configuration.config");
        server.start();
    }
}