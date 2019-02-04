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

package mountedwings.org.mskola_mgt.student;

import android.content.BroadcastReceiver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.AdapterTimeTableListSectioned;
import mountedwings.org.mskola_mgt.model.TimeTable;
import mountedwings.org.mskola_mgt.utils.Tools;

public class Timetable_menu_activity extends AppCompatActivity {
    private ProgressBar progressBar1;
    private BroadcastReceiver mReceiver;
    private RecyclerView recyclerView;
    private int w = 0, status;
    private List<TimeTable> items = new ArrayList<>();

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Timetable");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {

        String school_id = getIntent().getStringExtra("school_id");
        String regNo = getIntent().getStringExtra("reg_no");
        progressBar1 = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        new loadTimeTable().execute(school_id, regNo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_timetable_menu);
        initToolbar();
        initComponent();
    }

    //loads timetable
    private class loadTimeTable extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("gettimetable");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar1.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.i("mSkola", text);
            if (!text.equals("0") && !text.isEmpty()) {
                String days[] = text.split("##");

                ArrayList<String> day = new ArrayList<>();
                ArrayList<String> activities = new ArrayList<>();
                ArrayList<String> time = new ArrayList<>();

                //get days
                for (int i = 0; i < days.length; i++) {
                    day.add(days[i].split("<>")[0]);
                    Log.i("mSkola", day.get(i));
                    for (int j = 0; j < days[i].split("<>").length - 1; j++) {
                        try {
                            activities.add(days[i].split("<>")[j + 1].split(";")[0]);
                            time.add(days[i].split("<>")[j + 1].split(";")[1]);
                            Log.i("mSkola", activities.get(j));
                            Log.i("mSkola", time.get(j));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                int no_activities_per_day = days[0].split("<>").length - 1;
                int header_count = 1;
                int j = 0;
                int q = 0;
                //first_instance
                items.add(new TimeTable(day.get(0), "", true));

                try {
                    //master array list
                    for (int i = 0; i < (day.size() + activities.size()) - 1; i++) {
                        Log.i("mSkola", "master loop round = " + i);

                        if (j < no_activities_per_day) {
                            Log.i("mSkola", "act per day = " + j);
                            Log.i("mSkola", "inner q = " + q);

                            //put activities
                            items.add(new TimeTable(activities.get(q), time.get(q), false));
                            j++;
                        } else {
                            //put new header
                            Log.i("mSkola", "header = " + header_count);

                            items.add(new TimeTable(day.get(header_count), "", true));
                            header_count++;
                            j = 0;
                            q--;
                        }
                        q++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //set data and list adapter
                AdapterTimeTableListSectioned mAdapter = new AdapterTimeTableListSectioned(getApplicationContext(), items);
                recyclerView.setAdapter(mAdapter);


                //view shows back
                progressBar1.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                Tools.toast("Timetable not available", Timetable_menu_activity.this, R.color.red_800, Toast.LENGTH_LONG);
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
//        this.mReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                w++;
//                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
//                    @Override
//                    public void onConnectionSuccess() {
//                        status = 1;
//                        if (w > 1)
//                            Tools.toast("Back Online! Try again", Timetable_menu_activity.this, R.color.green_800);
//                        else //load classes and assessments
//                            new loadTimeTable().execute(school_id, regNo);
//                    }
//
//                    @Override
//                    public void onConnectionFail(String errorMsg) {
//                        status = 0;
//                        Tools.toast(getResources().getString(R.string.no_internet_connection), Timetable_menu_activity.this, R.color.red_500);
//                    }
//                }).execute();
//            }
//
//        };
//
//        registerReceiver(
//                this.mReceiver,
//                new IntentFilter(
//                        ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
//        unregisterReceiver(this.mReceiver);
//        w = 0;
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.homeAsUp) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}