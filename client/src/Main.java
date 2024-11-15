import domain.Client;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int serverPort = 1488;
        SwingUtilities.invokeLater(() -> new Client(serverAddress, serverPort));
    }
}