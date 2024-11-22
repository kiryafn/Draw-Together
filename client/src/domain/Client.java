package domain;

import data.ConnectionInfo;
import ui.MyFrame;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    public static String nickname;
    private MyFrame frame;

    public Client() {
            while (true) {
                try {
                    ConnectionInfo connectionInfo = connect();

                    socket = new Socket(connectionInfo.ip, connectionInfo.port);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    // Отправляем никнейм на сервер
                    frame = new MyFrame(this);
                    out.println(nickname);

                    // Поток для чтения сообщений с сервера
                    new Thread(new IncomingMessagesHandler(this, frame.getOptionPanel().getChat())).start();

                    break;
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
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

    public ConnectionInfo connect(){
        while (true){
            try {
                JTextField ipField = new JTextField("127.0.0.1"); // Предустановленное значение
                JTextField portField = new JTextField("1488");   // Предустановленное значение
                JTextField nameField = new JTextField();
                Object[] message = {
                        "Server IP:", ipField,
                        "Server Port:", portField,
                        "Your Name:", nameField
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Connect to Server", JOptionPane.OK_CANCEL_OPTION);
                if (option != JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(null, "Connection cancelled by user.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue; // Возвращаемся в начало цикла
                }

                String serverAddress = ipField.getText().trim();
                String portText = portField.getText().trim();
                nickname = nameField.getText().trim();

                if (serverAddress.isEmpty() || portText.isEmpty() || nickname.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    continue; // Возвращаемся в начало цикла
                }

                int serverPort = Integer.parseInt(portText);
                return new ConnectionInfo(serverAddress, serverPort, nickname);

            }catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Port must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}