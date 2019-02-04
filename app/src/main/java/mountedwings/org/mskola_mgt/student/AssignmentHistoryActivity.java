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

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import java.util.ArrayList;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.StudentAssHistAdapter;
import mountedwings.org.mskola_mgt.data.NumberStudentsAssignment;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class AssignmentHistoryActivity extends AppCompatActivity {
    ArrayList<NumberStudentsAssignment> numbers = new ArrayList<>();

    private BroadcastReceiver mReceiver;
    private int w = 0;
    private int status;

    private RecyclerView list;
    private String school_id, student_reg_no, TAG = "mSkola";

    ProgressBar loading;
    StudentAssHistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_history);

        int PREFERENCE_MODE_PRIVATE = 0;
        SharedPreferences mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);

        //school_id/staff id from sharedPrefs

        student_reg_no = mPrefs.getString("student_reg_no", getIntent().getStringExtra("reg_no"));
        school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

        FloatingActionButton fab_done = findViewById(R.id.done);
        TextView heading = findViewById(R.id.assignment_history_title);
        heading.setText(R.string.given_ass);
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(false);

        //hide parentView
        loading.setVisibility(View.VISIBLE);

//        new first_loading().execute("cac180826043520", "admin");
///////////////////////////////////////////////////////////////////////////////////////////////////////
        fab_done.setOnClickListener(v -> finish());
    }

    public class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            //  Boolean success = false;
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getassignments");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);

            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (!text.isEmpty() && !text.equals("0")) {
                loading.setVisibility(View.GONE);
                //worked. Split into reg and names
                String rows[] = text.split("<>");
                for (String row : rows) {
                    NumberStudentsAssignment numberStudentsAssignment = new NumberStudentsAssignment();
                    numberStudentsAssignment.setAssignment_date(row.split(";")[0]);
                    numberStudentsAssignment.setSubject(row.split(";")[1]);
                    numberStudentsAssignment.setCode(row.split(";")[2]);
                    numbers.add(numberStudentsAssignment);
                }
                //show recyclerView with inflated views
                adapter = new StudentAssHistAdapter(numbers);
                list.setAdapter(adapter);

                adapter.setOnItemClickListener((view, obj, position) -> {
                    //run last API
                    if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                        Tools.toast("Fetching assignment...", AssignmentHistoryActivity.this);
                        new loadIndividualAssignment().execute(obj.getCode());
                    } else {
                        Tools.toast(getResources().getString(R.string.no_internet_connection), AssignmentHistoryActivity.this, R.color.red_500);
                        finish();
                    }
                });

            } else {
                Tools.toast("You haven't given an assignment!", AssignmentHistoryActivity.this);
                finish();
            }
        }
    }

    public class loadIndividualAssignment extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getassdetails");
            storageObj.setStrData(school_id + "<>" + strings[0]);

            storageFile sentData = new serverProcessParents().requestProcess(storageObj);

            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            try {
                if (!text.isEmpty() && !text.equals("0")) {
                    startActivity(new Intent(getApplicationContext(), Assignment_history_detail.class).putExtra("ass_details", text));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                            Tools.toast("Back Online! Try again", AssignmentHistoryActivity.this, R.color.green_800);
                        else
                            new first_loading().execute(school_id, student_reg_no);

                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), AssignmentHistoryActivity.this, R.color.red_500);

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
        finish();
    }


}

