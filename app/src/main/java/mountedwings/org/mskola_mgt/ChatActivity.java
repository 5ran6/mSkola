package mountedwings.org.mskola_mgt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.Objects;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class ChatActivity extends AppCompatActivity {
    String school_id, staff_id, TAG = "mSkola", recipients = "", recipient_category = "";
    private ChatView chatView;
    private ImageView dp;
    private TextView textbar;
    private AppBarLayout appBarLayout;
    private boolean multi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        textbar = findViewById(R.id.textbar);
        appBarLayout = findViewById(R.id.app_bar_layout);
        dp = findViewById(R.id.dp);
        Intent intent = getIntent();

        recipients = getIntent().getStringExtra("recipient");
        recipient_category = getIntent().getStringExtra("category");
        Log.d("mSkola", "Recipient(s) = " + recipients);
        Log.d("mSkola", "Category = " + recipient_category);

        if (recipients.split(";").length > 1) {
            multi = true;
        }

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));
        //school_id/staff id from sharedPrefs
        staff_id = mPrefs.getString("staff_id", intent.getStringExtra("staff_id"));
        school_id = mPrefs.getString("school_id", intent.getStringExtra("school_id"));

        //initToolbar();

        chatView = findViewById(R.id.chat_view);

        if (!multi) {
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

    private void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        textbar.setText("John Doe");
        //dp.setImageBitmap(BitmapDrawable b);
        Tools.setSystemBarColor(this, R.color.blue_400);
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
            Log.d("mSkola", "Msg = " + text);

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
            Log.d("mSkola", "Msg = " + text);

            if (text.equals("success")) {
                View layout = getLayoutInflater().inflate(R.layout.toast_custom, findViewById(R.id.custom_toast_layout_id));
                TextView Ttext = layout.findViewById(R.id.text);
                Ttext.setTextColor(getResources().getColor(R.color.green_300));
                Ttext.setText("Sent");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

            } else {
                View layout = getLayoutInflater().inflate(R.layout.toast_custom, findViewById(R.id.custom_toast_layout_id));
                TextView Ttext = layout.findViewById(R.id.text);
                Ttext.setTextColor(getResources().getColor(R.color.red_300));
                Ttext.setText("Could not send message");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                Toast.makeText(getApplicationContext(), "Could not send message"
                        , Toast.LENGTH_SHORT).show();

            }
//finally
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
            Log.d("mSkola", "Msg = " + text);

            if (text.equals("success")) {
                View layout = getLayoutInflater().inflate(R.layout.toast_custom, findViewById(R.id.custom_toast_layout_id));
                TextView Ttext = layout.findViewById(R.id.text);
                Ttext.setTextColor(getResources().getColor(R.color.green_300));
                Ttext.setText("Sent");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

            } else {
                View layout = getLayoutInflater().inflate(R.layout.toast_custom, findViewById(R.id.custom_toast_layout_id));
                TextView Ttext = layout.findViewById(R.id.text);
                Ttext.setTextColor(getResources().getColor(R.color.red_300));
                Ttext.setText("Could not send message");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                Toast.makeText(getApplicationContext(), "Could not send message"
                        , Toast.LENGTH_SHORT).show();

            }
//finally
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Chat_menu.class));
    }
}
