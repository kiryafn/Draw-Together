package data;

import java.awt.*;

public interface ChatViewContract {

    interface View {

        void drawMessage(String message);

        void broadcastMessage(String message);

        void sendMessageTo(String message);

        void dontSendMessageTo(String message);

        void appendMessage(String text, Color color);
    }

    interface Presenter {

        void onEvent(String message);
    }
}
