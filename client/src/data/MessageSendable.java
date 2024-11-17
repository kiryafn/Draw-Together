package data;

public interface MessageSendable {
    void drawMessage(String message);

    void broadcastMessage(String message);

    void sendMessageTo(String message);

    void dontSendMessageTo(String message);
}
