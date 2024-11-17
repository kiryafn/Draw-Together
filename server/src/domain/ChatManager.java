package domain;

import data.BannedWordException;

public class ChatManager {
    Server server;

    public ChatManager(Server server) {
        this.server = server;
    }

    private void isMessageAllowed(String message) throws BannedWordException {
        for (String phrase : server.getBannedWords()) {
            if (message.contains(phrase)) {
                throw new BannedWordException("Your message contains prohibited phrase");
            }
        }
    }

    public void broadcastMessage(String message, String sender) {
        try {
            isMessageAllowed(message);
            for (ClientHandler client : server.getPlayers().values()) {
                if (!client.getClientName().equals(sender)) {
                    client.sendMessage(message);
                    client.sendSenderNickname(sender);
                    client.sendAcessModifier("All");
                }else {
                    client.sendMessage(message);
                    client.sendSenderNickname("Me");
                    client.sendAcessModifier("All");
                }
            }

        } catch (BannedWordException e) {
            server.getPlayers().get(sender).sendMessage("Your message contains prohibited words and will not be sent.");
            server.getPlayers().get(sender).sendSenderNickname("Server");
            server.getPlayers().get(sender).sendAcessModifier("All");
        }
    }

    public void processPrivateMessage(String sender, String message) {
        // Проверяем, что сообщение имеет правильный формат
        if (!message.matches("^/to \\[.*\\].*")) {
            server.getPlayers().get(sender).sendMessage("Error: Invalid command format.\n");
            server.getPlayers().get(sender).sendSenderNickname("Server");
            server.getPlayers().get(sender).sendAcessModifier("All");
            return;
        }

        // Извлекаем список получателей
        int start = message.indexOf('[') + 1;
        int end = message.indexOf(']');
        String recipients = message.substring(start, end); // Например: "user1,user2"
        String actualMessage = message.substring(end + 1); // Текст сообщения

        try {
            isMessageAllowed(actualMessage);
        } catch (BannedWordException e) {
            server.getPlayers().get(sender).sendMessage("Your message contains prohibited words and will not be sent.");
            server.getPlayers().get(sender).sendSenderNickname("Server");
        }

        if (recipients.isEmpty() || actualMessage.isEmpty()) {
            server.getPlayers().get(sender).sendMessage("Error: Empty recipients or message.\n");
            server.getPlayers().get(sender).sendSenderNickname("Server");

            return;
        }

        // Разделяем список получателей на массив
        recipients.replaceAll(" ", "");

        String[] recipientList = recipients.split(", ");

        // Отправляем сообщение каждому получателю
        for (String recipient : recipientList) {
            ClientHandler recipientHandler = server.getPlayers().get(recipient);
            if (recipientHandler != null) {
                recipientHandler.sendMessage(actualMessage);
                recipientHandler.sendSenderNickname(sender);
            } else {
                server.getPlayers().get(sender).sendMessage("User " + recipient + " not found.\n");
                server.getPlayers().get(sender).sendSenderNickname("Server");
            }
        }
        server.getPlayers().get(sender).sendMessage(actualMessage);
        server.getPlayers().get(sender).sendSenderNickname("Me");
    }

}
