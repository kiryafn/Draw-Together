package domain;

import ui.ChatPanel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;

public class IncomingMessagesHandler implements Runnable {
    private final Client client;
    private final ChatPanel chatPanel;

    public IncomingMessagesHandler(Client client, ChatPanel chatPanel) {
        this.client = client;
        this.chatPanel = chatPanel;
    }

    @Override
    public void run() {
        try (BufferedReader in = client.getInputStream()) {
            String message;
            while ((message = in.readLine()) != null) {
                synchronized (chatPanel) {
                    String sender = in.readLine();
                    String group = in.readLine();
                    chatPanel.getChatLogic().drawMessage(message, sender, group);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading messages: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
