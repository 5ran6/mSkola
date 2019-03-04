/*
 * Copyright 2019 Mountedwings Cybersystems LTD. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    String school_id, staff_id, TAG = "mSkola", recipients = "", recipient_category = "", message = "";
    private ChatView chatView;
    private ImageView dp;
    private TextView textbar;
    private AppBarLayout appBarLayout;
    private boolean multi = false;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;
    private AsyncTask lastThread;

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
                lastThread = new first_loading().execute();
            } else {
                chatView.addMessage(new ChatMessage("Type your Broadcast message", System.currentTimeMillis(), ChatMessage.Type.RECEIVED, "<" + recipients + ">"));
            }

            chatView.setOnSentMessageListener(chatMessage ->
                    {
                        message = chatMessage.getMessage();
                        if (multi)
                            lastThread = new sendBatchMessage().execute();
                        else
                            lastThread = new sendSingleMessage().execute();
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
                        if (w > 1) {
                            try {
//                                Tools.toast("Back Online!", MskolaLogin.this, R.color.green_900);
                                lastThread.execute();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
//                        Tools.toast("Offline", ChatActivity.this, R.color.red_500);
                        try {
                            lastThread.cancel(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
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

    //DONE
    private class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                storageFile storageObj = new storageFile();
                storageObj.setOperation("getsinglemessage");
                //school_id, staff_id, recipients
                storageObj.setStrData(school_id + "<>" + staff_id + "<>" + recipients);
                return new serverProcess().requestProcess(storageObj).getStrData();

            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
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

        @Override
        protected void onCancelled() {
            super.onCancelled();
            finish();
        }
    }

    //DONE
    private class sendSingleMessage extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                do {
                    storageFile storageObj = new storageFile();
                    storageObj.setOperation("sendmessage");
                    //school_id, chatMessage.getMessage(), recipients, staff_id
                    storageObj.setStrData(school_id + "<>" + message + "<>" + recipients + "<>" + staff_id + "<>" + "_");
                    return new serverProcess().requestProcess(storageObj).getStrData();
                } while (!isCancelled());
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
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

    //DONE
    private class sendBatchMessage extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                do {
                    storageFile storageObj = new storageFile();
                    storageObj.setOperation("sendmessage");
                    //school_id, chatMessage.getMessage(), recipients, staff_id, recipient_category
                    storageObj.setStrData(school_id + "<>" + message + "<>" + recipients + "<>" + staff_id + "<>" + recipient_category);
                    return new serverProcess().requestProcess(storageObj).getStrData();

                } while (!isCancelled());

            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }


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
    protected void onPause() {
        unregisterReceiver(this.mReceiver);
        w = 0;
        super.onPause();
    }

}
