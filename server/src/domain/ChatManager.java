package domain;

import data.BannedWordException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChatManager {
    Server server;

    public ChatManager(Server server) {
        this.server = server;
    }

    private void isMessageAllowed(String message) throws BannedWordException {
        for (String phrase : server.getBannedWords()) {
            if (message.contains(phrase)) {
                throw new BannedWordException("Your message contains prohibited words and will not be sent.");
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
                    client.sendAccessModifier("All");
                } else {
                    client.sendMessage(message);
                    client.sendSenderNickname("Me");
                    client.sendAccessModifier("All");
                }
            }

        } catch (BannedWordException e) {
            server.getPlayers().get(sender).sendMessage("Your message contains prohibited words and will not be sent.");
            server.getPlayers().get(sender).sendSenderNickname("Server");
            server.getPlayers().get(sender).sendAccessModifier("All");
        }
    }

    public void processBannedWords(String message, String sender) {
        if (!message.matches("^/bannedwords")) {
            server.getPlayers().get(sender).sendMessage("Invalid command format. Type '/bannedwords'");
            server.getPlayers().get(sender).sendSenderNickname("Server");
            server.getPlayers().get(sender).sendAccessModifier("All");
            return;
        }

        server.getPlayers().get(sender).sendMessage(server.getBannedWords().toString());
        server.getPlayers().get(sender).sendSenderNickname("Server");
        server.getPlayers().get(sender).sendAccessModifier("All");
    }

    public void processPrivateMessage(String message, String sender) {
        // Проверяем, что сообщение имеет правильный формат
        if (!message.matches("^/to \\[.*\\].*")) {
            server.getPlayers().get(sender).sendMessage("Invalid command format.");
            server.getPlayers().get(sender).sendSenderNickname("Server");
            server.getPlayers().get(sender).sendAccessModifier("All");
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
            server.getPlayers().get(sender).sendAccessModifier("All");
        }

        if (recipients.isEmpty() || actualMessage.isEmpty()) {
            server.getPlayers().get(sender).sendMessage("Empty recipients or message.");
            server.getPlayers().get(sender).sendSenderNickname("Server");
            server.getPlayers().get(sender).sendAccessModifier("All");


            return;
        }

        // Разделяем список получателей на массив
        recipients.replaceAll(" ", "");

        String[] recipientList = recipients.split(",");

        // Отправляем сообщение каждому получателю
        for (String recipient : recipientList) {
            ClientHandler recipientHandler = server.getPlayers().get(recipient);
            if (recipientHandler != null) {
                recipientHandler.sendMessage(actualMessage);
                recipientHandler.sendSenderNickname(sender);
                recipientHandler.sendAccessModifier("Whisper");

            } else {
                server.getPlayers().get(sender).sendMessage("User " + recipient + " not found.");
                server.getPlayers().get(sender).sendSenderNickname("Server");
                server.getPlayers().get(sender).sendAccessModifier("All");
            }
        }
        server.getPlayers().get(sender).sendMessage("[" + recipients + "]" + actualMessage);
        server.getPlayers().get(sender).sendSenderNickname("Me");
        server.getPlayers().get(sender).sendAccessModifier("Whisper");
    }


    public void processPrivateExcludeMessage(String message, String sender) {
        // Проверяем, что сообщение имеет правильный формат
        if (!message.matches("^/notto \\[.*\\].*")) {
            server.getPlayers().get(sender).sendMessage("Error: Invalid command format.");
            server.getPlayers().get(sender).sendSenderNickname("Server");
            server.getPlayers().get(sender).sendAccessModifier("");
            return;
        }

        // Извлекаем список получателей
        int start = message.indexOf('[') + 1;
        int end = message.indexOf(']');
        String excludedRecipients = message.substring(start, end); // Например: "user1,user2"
        String actualMessage = message.substring(end + 1); // Текст сообщения

        try {
            isMessageAllowed(actualMessage);
        } catch (BannedWordException e) {
            server.getPlayers().get(sender).sendMessage("Your message contains prohibited words and will not be sent.");
            server.getPlayers().get(sender).sendSenderNickname("Server");
            server.getPlayers().get(sender).sendAccessModifier("All");

        }

        if (excludedRecipients.isEmpty() || actualMessage.isEmpty()) {
            server.getPlayers().get(sender).sendMessage("Error: Empty excluded recipients or message.");
            server.getPlayers().get(sender).sendSenderNickname("Server");
            server.getPlayers().get(sender).sendAccessModifier("All");


            return;
        }

        // Разделяем список получателей на массив
        excludedRecipients.replaceAll(" ", "");

        String[] excludedList = excludedRecipients.split(", ");

        // Добавляем исключённых пользователей в набор для быстрого поиска
        Set<String> excludedSet = new HashSet<>(Arrays.asList(excludedList));

        // Отправляем сообщение всем, кроме исключённых
        for (Map.Entry<String, ClientHandler> entry : server.getPlayers().entrySet()) {
            String recipient = entry.getKey();
            ClientHandler recipientHandler = entry.getValue();

            // Пропускаем исключённых пользователей
            if (excludedSet.contains(recipient) || recipient.equals(sender)) continue;

            // Отправляем сообщение остальным
            recipientHandler.sendMessage(actualMessage);
            recipientHandler.sendSenderNickname(sender);
            recipientHandler.sendAccessModifier("!Whisper");
        }

        server.getPlayers().get(sender).sendMessage("[" + excludedRecipients + "]" + actualMessage);
        server.getPlayers().get(sender).sendSenderNickname("Me");
        server.getPlayers().get(sender).sendAccessModifier("!Whisper");

    }

    public void processGetPlayers(String message, String sender) {
        if (!message.matches("^/getplayers$")) {
            server.getPlayers().get(sender).sendMessage("Invalid command format. Type '/getplayers'");
            server.getPlayers().get(sender).sendSenderNickname("Server");
            server.getPlayers().get(sender).sendAccessModifier("");
            return;
        }

        server.getPlayers().get(sender).sendMessage(server.getPlayers().size() + " connected players: " + server.getPlayers().keySet());
        server.getPlayers().get(sender).sendSenderNickname("Server");
        server.getPlayers().get(sender).sendAccessModifier("");
    }

    public void processHelpCommand(String message, String sender) {
        if (!message.matches("^/help$")) {
            server.getPlayers().get(sender).sendMessage("Invalid command format. Type '/help'");
            server.getPlayers().get(sender).sendSenderNickname("Server");
            server.getPlayers().get(sender).sendAccessModifier("");
            return;
        }

        server.getPlayers().get(sender).sendMessage("Available commands: /to [recipients] message, /notto [excluded recipients] message, /bannedwords, /getplayers, /help");
        server.getPlayers().get(sender).sendSenderNickname("Server");
        server.getPlayers().get(sender).sendAccessModifier("");
    }

    public void processUnknownCommand(String message, String sender) {
        server.getPlayers().get(sender).sendMessage("Unknown command. To see list of all commands type '/help'");
        server.getPlayers().get(sender).sendSenderNickname("Server");
        server.getPlayers().get(sender).sendAccessModifier("");
    }
}
