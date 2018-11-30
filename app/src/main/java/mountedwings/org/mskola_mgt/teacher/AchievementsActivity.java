package mountedwings.org.mskola_mgt.teacher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.NumbersAchievementsAdapter;
import mountedwings.org.mskola_mgt.data.NumberAchievements;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class AchievementsActivity extends AppCompatActivity {
    String school_id, staff_id, TAG = "mSkola";
    ArrayList<NumberAchievements> numbers = new ArrayList<>();


    private RecyclerView list;
    private FloatingActionButton fab_done;
    private TextView heading;
    private ArrayList<byte[]> allPassport_aPerson = new ArrayList<>();
    private storageFile data = new storageFile();
    private ArrayList<String> achievements = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private BroadcastReceiver mReceiver;
    private int w = 0;
    private int status;


    ProgressBar loading;
    NumbersAchievementsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);


        Intent intent = getIntent();

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        //school_id from sharedPrefs
        school_id = mPrefs.getString("school_id", intent.getStringExtra("school_id"));

        fab_done = findViewById(R.id.done);
        heading = findViewById(R.id.achievements_heading);
        heading.setText("School Achievements");

        loading = findViewById(R.id.loading);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(false);

        adapter = new NumbersAchievementsAdapter(numbers);
        list.setAdapter(adapter);

        //hide parentView
        loading.setVisibility(View.VISIBLE);

        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            new first_loading().execute(school_id);
        } else {
            Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);
            finish();
        }
///////////////////////////////////////////////////////////////////////////////////////////////////////
        fab_done.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //DONE
    private class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            data = storageObj;

            storageObj.setOperation("getpsstudentinfo");
            storageObj.setStrData(strings[0]);
            data = new serverProcess().requestProcess(storageObj);
            String text = data.getStrData();
            return text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (!text.equals("0") && !text.equals("") && !text.equals("not found")) {
                //       Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                allPassport_aPerson = data.getImageFiles();

                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {
                    achievements.add(rows[i].split(";")[1]);
                    title.add(rows[i].split(";")[0]);
                    NumberAchievements number = new NumberAchievements();
                    number.setachievement(achievements.get(i));
                    number.settitle(achievements.get(i));
                    number.setImage(allPassport_aPerson.get(i));
                    numbers.add(number);
                }
                //show recyclerView with inflated views
                adapter = new NumbersAchievementsAdapter(numbers);
                list.setAdapter(adapter);
            } else {
                Tools.toast("No achievements found", AchievementsActivity.this, R.color.yellow_800);
                finish();
            }
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
                        if (w > 1)
                            Tools.toast("Back Online! Try again", AchievementsActivity.this, R.color.green_800);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), AchievementsActivity.this, R.color.red_500);
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

