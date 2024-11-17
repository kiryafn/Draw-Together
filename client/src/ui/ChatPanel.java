package ui;

import data.ChatViewContract;
import domain.ChatLogic;
import domain.ChatPresenter;
import domain.Client;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel{
    private JTextPane  chatArea   = new JTextPane();;
    private JTextField inputField = new JTextField();
    private JButton    sendButton = new JButton("Send");
    private ChatLogic  chatLogic;

    //private Presenter presnetr =...
    //presentr.view = this
    //press button presnetr.click

    public ChatPanel(Client client) {
        chatLogic = new ChatLogic(this, client);
        setLayout(new BorderLayout());

        chatArea.setEditable(false);
        chatArea.setPreferredSize(new Dimension(550, 300));

        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        // 2. Панель для ввода сообщений
        JPanel inputPanel = new JPanel(new BorderLayout());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Слушатель для отправки сообщений
        sendButton.addActionListener(e -> {
            chatLogic.sendMessageToServer(inputField.getText().trim());
            chatLogic.cleanInputField();
        });
        inputField.addActionListener(e -> {
            chatLogic.sendMessageToServer(inputField.getText().trim());
            chatLogic.cleanInputField();
        });
    }

    public JTextPane getChatArea() {
        return chatArea;
    }


    public JTextField getInputField() {
        return inputField;
    }

    public ChatLogic getChatLogic() {
        return chatLogic;
    }
}