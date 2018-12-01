package mountedwings.org.mskola_mgt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.Objects;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class ChatActivity extends AppCompatActivity {
    String school_id, staff_id, TAG = "mSkola", recipients = "", recipient_category = "";
    private ChatView chatView;
    private ImageView dp;
    private TextView textbar;
    private AppBarLayout appBarLayout;
    private boolean multi = false;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        textbar = findViewById(R.id.textbar);
        appBarLayout = findViewById(R.id.app_bar_layout);
        dp = findViewById(R.id.dp);
        Tools.toast("Loading chats", this);

        Intent intent = getIntent();

        recipients = getIntent().getStringExtra("recipient");
        recipient_category = getIntent().getStringExtra("category");

        if (recipients.split(";").length > 1) {
            multi = true;
        }

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));
        //school_id/staff id from sharedPrefs
        staff_id = mPrefs.getString("email_address", intent.getStringExtra("email_address"));
        school_id = mPrefs.getString("school_id", intent.getStringExtra("school_id"));

        //initToolbar();

        chatView = findViewById(R.id.chat_view);

        if (!multi) {
            if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                new first_loading().execute(school_id, staff_id, recipients);
            } else {
                chatView.addMessage(new ChatMessage("Type your Broadcast message", System.currentTimeMillis(), ChatMessage.Type.RECEIVED, "<" + recipients + ">"));
            }

            chatView.setOnSentMessageListener(chatMessage ->
                    {
                        if (multi)
                            new sendBatchMessage().execute(school_id, chatMessage.getMessage(), recipients, staff_id, recipient_category);
                        else
                            new sendSingleMessage().execute(school_id, chatMessage.getMessage(), recipients, staff_id);
                        return true;
                    }
            );
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

    //DONE
    private class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getsinglemessage");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
            return new serverProcess().requestProcess(storageObj).getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);

            if (!text.equals("not found") && !text.equals("0") && !text.isEmpty()) {
                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {
                    if (rows[i].split("##")[0].equals(staff_id)) {
                        chatView.addMessage(new ChatMessage(rows[i].split("##")[1], System.currentTimeMillis(), ChatMessage.Type.RECEIVED, rows[i].split("<>")[0].split("##")[0]));
                    } else {
                        chatView.addMessage(new ChatMessage(rows[i].split("##")[1], System.currentTimeMillis(), ChatMessage.Type.SENT, "Me"));
                    }
                }
            } else {
                Toast.makeText(Objects.requireNonNull(getApplicationContext()), "No messages found yet", Toast.LENGTH_SHORT).show();
                finish();
            }
            //finally
        }
    }

    //DONE
    private class sendSingleMessage extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("sendmessage");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + "_");
            return new serverProcess().requestProcess(storageObj).getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);

            if (text.equals("success")) {
                Tools.toast("Sent", ChatActivity.this, R.color.green_800);
            } else {
                Tools.toast("Could not send message", ChatActivity.this, R.color.red_800);
            }
        }
    }

    //DONE
    private class sendBatchMessage extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("sendmessage");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + strings[4]);
            return new serverProcess().requestProcess(storageObj).getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);

            if (text.equals("success")) {
                Tools.toast("Sent", ChatActivity.this, R.color.green_800);
            } else {
                Tools.toast("Could not send message", ChatActivity.this, R.color.red_800);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Chat_menu.class));
    }


    @Override
    protected void onResume() {
        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                w++;
                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        status = 1;
                        if (w > 1)
                            Tools.toast("Back Online! Try again", ChatActivity.this, R.color.green_800);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast("Offline", ChatActivity.this, R.color.red_500);
                    }
                }).execute();
            }

        };

        registerReceiver(
                this.mReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(this.mReceiver);
        w = 0;
        super.onPause();
    }

}
