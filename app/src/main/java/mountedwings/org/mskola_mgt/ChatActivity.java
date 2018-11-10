package mountedwings.org.mskola_mgt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ChatView chatView = findViewById(R.id.chat_view);
        chatView.addMessage(new ChatMessage("Message received", System.currentTimeMillis(), ChatMessage.Type.RECEIVED));
        chatView.addMessage(new ChatMessage("A message with a sender name",
                System.currentTimeMillis(), ChatMessage.Type.RECEIVED, "Ryan Java"));
        chatView.addMessage(new ChatMessage("Message Sent", System.currentTimeMillis(), ChatMessage.Type.SENT));

        chatView.setOnSentMessageListener(chatMessage -> true);

        chatView.setTypingListener(new ChatView.TypingListener() {
            @Override
            public void userStartedTyping() {

            }

            @Override
            public void userStoppedTyping() {

            }
        });
    }
}
