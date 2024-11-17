package domain;

import ui.ChatPanel;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class ChatLogic {
    Client client;
    ChatPanel chatPanel;

    public ChatLogic(ChatPanel chatPanel, Client client){
        this.chatPanel = chatPanel;
        this.client = client;
    }

    private void appendMessage(String text, Color color) {
        StyledDocument doc = chatPanel.getChatArea().getStyledDocument();

        Style style = chatPanel.getChatArea().addStyle("ColorStyle", null);
        StyleConstants.setForeground(style, color);

        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    //@Override
    public void drawMessage(String message, String sender, String group) {
        if (message.isEmpty())
            return;

        if(sender.equals("Server")) {
            appendMessage("(Server) " + message + "\n", new Color(200, 0, 255));
            return;
        }

        if (group.equals("Whisper")) {
            drawPrivateMessage(message, sender, group);
        } else if (group.equals("!Whisper")) {
            group = "Whisper";
            drawPrivateExcludedMessage(message, sender, group);
        } else
            drawBroadcastMessage(message, sender, group);
    }

    public void sendMessageToServer(String message) {
        client.sendMessageToServer(message);
    }

    public void cleanInputField(){
        chatPanel.getInputField().setText("");
    }

    //@Override
    public void drawBroadcastMessage(String message, String sender, String group) {
            appendMessage("(" + group + ") ", new Color(93, 185, 216));
            appendMessage(sender + ": ", new Color(124, 39, 184, 255));
            appendMessage(message + "\n", Color.BLACK);
    }

    /// @Override
    public void drawPrivateMessage(String message, String sender, String group) {
        if (!sender.equals("Me")) {
            appendMessage("(" + group + ")", new Color(237, 104, 255));
            appendMessage("from " + sender + ": ", new Color(124, 39, 184));
            appendMessage(message + "\n", Color.BLACK);
        } else {
            int start = message.indexOf('[') + 1;
            int end = message.indexOf(']');
            if (start < end) {
                String recipients = message.substring(start, end); // Извлекаем список получателей
                String actualMessage = message.substring(end + 1).trim(); // Само сообщение после ']'

                appendMessage("(" + group + ")", new Color(237, 104, 255));
                appendMessage("to " + recipients + ": ", new Color(124, 39, 184));
                appendMessage(actualMessage + "\n", Color.BLACK);
            }
        }
    }

    //@Override
    public void drawPrivateExcludedMessage(String message, String sender, String group) {
        if (!sender.equals("Me")) {
            appendMessage("(" + group + ")", new Color(237, 104, 255));
            appendMessage("from " + sender + ": ", new Color(124, 39, 184));
            appendMessage(message + "\n", Color.BLACK);
        } else {
            int start = message.indexOf('[') + 1;
            int end = message.indexOf(']');
            if (start < end) {
                String recipients = message.substring(start, end); // Извлекаем список получателей
                String actualMessage = message.substring(end + 1).trim(); // Само сообщение после ']'

                appendMessage("(" + group + ")", new Color(237, 104, 255));
                appendMessage("to all except " + recipients + ": ", new Color(124, 39, 184));
                appendMessage(actualMessage + "\n", Color.BLACK);
            }
        }
    }
}
