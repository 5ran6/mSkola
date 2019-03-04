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
import mountedwings.org.mskola_mgt.adapter.AchievementsAdapter;
import mountedwings.org.mskola_mgt.data.NumberAchievements;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class AchievementsActivity extends AppCompatActivity {
    String school_id, staff_id, TAG = "mSkola";
    ArrayList<NumberAchievements> numbers = new ArrayList<>();


    private RecyclerView list;
    private storageFile data = new storageFile();
    private ArrayList<String> achievements = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private BroadcastReceiver mReceiver;
    private int w = 0;
    private int status;
    private AsyncTask lastThread;
    private boolean done = false;

    ProgressBar loading;
    AchievementsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        Intent intent = getIntent();

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        //school_id from sharedPrefs
        school_id = mPrefs.getString("school_id", intent.getStringExtra("school_id"));

        FloatingActionButton fab_done = findViewById(R.id.done);
        TextView heading = findViewById(R.id.achievements_heading);
        heading.setText("School Achievements");

        loading = findViewById(R.id.loading);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);

        adapter = new AchievementsAdapter(numbers);
        list.setAdapter(adapter);

        //hide parentView
//        loading.setVisibility(View.VISIBLE);

///////////////////////////////////////////////////////////////////////////////////////////////////////
        fab_done.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));
        Intent intent = getIntent();
        //school_id from sharedPrefs
        school_id = mPrefs.getString("school_id", intent.getStringExtra("school_id"));

        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                w++;
                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        status = 1;
                        if (w > 1) {
                            if (!done) {
                                try {
                                    Tools.toast("Back Online! Reconnecting...", AchievementsActivity.this, R.color.green_800);
                                    lastThread = new first_loading().execute();
                                    lastThread.execute();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } else
                            lastThread = new first_loading().execute();

                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        if (w > 1) {
                            try {
                                Tools.toast(getResources().getString(R.string.no_internet_connection), AchievementsActivity.this, R.color.red_900);
                                lastThread.cancel(true);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            Tools.toast(getResources().getString(R.string.no_internet_connection), AchievementsActivity.this, R.color.red_900);
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

                do {
                    storageFile storageObj = new storageFile();
                    data = storageObj;
                    storageObj.setOperation("getpsstudentinfo");
                    //school_id
                    storageObj.setStrData(school_id);
                    data = new serverProcess().requestProcess(storageObj);
                    return data.getStrData();
                } while (!isCancelled());
            } catch (Exception ex) {
                ex.printStackTrace();
                return "network error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (!text.equals("0") && !text.equals("") && !text.equals("not found") && !text.equalsIgnoreCase("network error")) {
                done = true;
                //       Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                ArrayList<byte[]> allPassport_aPerson = data.getImageFiles();

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
                adapter = new AchievementsAdapter(numbers);
                list.setAdapter(adapter);
            }
            if (text.equalsIgnoreCase("network error")) {
                loading.setVisibility(View.GONE);
                Tools.toast("Network error. Reconnecting...", AchievementsActivity.this, R.color.red_900);
            } else {
                Tools.toast("No achievements found", AchievementsActivity.this, R.color.yellow_800);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            loading.setVisibility(View.GONE);
            Tools.toast("Network error. Waiting for network", AchievementsActivity.this, R.color.red_900);
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(this.mReceiver);
        w = 0;
        super.onPause();
    }


}

