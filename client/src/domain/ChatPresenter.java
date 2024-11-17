package domain;

import data.ChatViewContract;

public class ChatPresenter implements ChatViewContract.Presenter {
    ChatViewContract.View view;
    Client client;

     public ChatPresenter(ChatViewContract.View view, Client client) {
         this.view = view;
         this.client = client;
     }

    @Override
    public void onEvent(String message) {
        client.sendMessageToServer(message);
    }
}
