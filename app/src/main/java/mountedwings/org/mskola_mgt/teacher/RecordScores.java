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

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.AdapterAssessment;
import mountedwings.org.mskola_mgt.data.NumberAssessment;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.widget.LineItemDecoration;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class RecordScores extends AppCompatActivity {

    private ArrayList<NumberAssessment> numbers = new ArrayList<>();

    private RecyclerView recyclerView;

    private BroadcastReceiver mReceiver;
    private int w = 0;
    private int status = 1;
    private String[] regNumbs;
    private String[] names;
    private String TAG = "mSkola", first_persons_score = "", school_id, class_name, arm, assessment, subject;

    private String reg_no;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.blue_400);
    }

    private void initComponent() {
        Intent intent = getIntent();
        int PREFERENCE_MODE_PRIVATE = 0;
        SharedPreferences mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(false);
        progressBar = findViewById(R.id.loading);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        TextView heading = findViewById(R.id.text);

        school_id = mPrefs.getString("school_id", intent.getStringExtra("school_id"));
        class_name = intent.getStringExtra("class_name");
        assessment = intent.getStringExtra("assessment");
        arm = intent.getStringExtra("arm");
        subject = intent.getStringExtra("subject");

        heading.setText(String.format("%s for %s%s", assessment.toUpperCase(), class_name, arm));
        AdapterAssessment mAdapter = new AdapterAssessment(RecordScores.this, numbers);

        mAdapter.setOnItemClickListener((view, obj, position) -> {
            // mAdapter.getItemId();
        });


    }

    public void save(View view) {
        // TODO:  send last score to server
        //  TODO: if successful, finish

    }

    //DONE
    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void areYouSure() {
        new AlertDialog.Builder(this)
                .setMessage("Are you done?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", (dialog, which) -> {
                })
                .show();
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
//                        status = 1;
//                        if (w > 1)
                        //             Tools.toast("Back Online! Try again", ClassTeachers.this, R.color.green_800);
                        //       else
                        //             new getClassTeacher().execute(school_id, reg_no);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), RecordScores.this, R.color.red_500);
                        finish();
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
//        unregisterReceiver(this.mReceiver);
//        w = 0;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        areYouSure();
    }

    private class loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentscore");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + strings[4] + "<>" + regNumbs[Integer.valueOf(strings[5])]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String scores) {
            super.onPostExecute(scores);
            Log.d(TAG, scores);
            //   Toast.makeText(getApplicationContext(), scores, Toast.LENGTH_SHORT).show();
            if (!scores.isEmpty()) {
                //  TODO: set editText score with scores


            } else {
//                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class submitScore extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("savestudentscore");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + strings[4] + "<>" + regNumbs[Integer.valueOf(strings[5])] + "<>" + strings[6]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String scores) {
            super.onPostExecute(scores);
            Log.d(TAG, scores);
            if (!scores.isEmpty()) {
                // TODO: collapse and continue

            } else {
                Tools.toast("Check your internet connection and try again", RecordScores.this, R.color.red_800);
            }
        }
    }

    //DONE
    private class first_loading extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            boolean success = false;
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getrecordscoreinfo");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + strings[4]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);

            String text = sentData.getStrData();
            Log.d(TAG, text);

            if (!text.equals("0") && !text.equals("")) {

                try {
                    first_persons_score = text.split("##")[1];
                    // TODO: set text of EditText

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (first_persons_score.equals("_")) {
                    first_persons_score = "";
                }
                text = text.split("##")[0];
                names = new String[text.split("<>").length];
                regNumbs = new String[text.split("<>").length];
                if (!text.isEmpty()) {
                    int i = 0;
                    do {
                        //  set data to data file object for names, index and scores (score = "")

                        NumberAssessment number = new NumberAssessment();
                        //name
                        names[i] = text.split("<>")[i].split(";")[1];
                        number.setName(text.split("<>")[i].split(";")[1]);
                        //regNumber
                        regNumbs[i] = text.split("<>")[i].split(";")[0];
                        number.setRegNo(text.split("<>")[i].split(";")[0]);
                        number.setIndex(String.valueOf(i));
                        number.setScore("");

                        numbers.add(number);

                        Log.d(TAG, names[i]);
                        Log.d(TAG, regNumbs[i]);
                        i++;
                    } while (i < text.split("<>").length);
                    success = true;
                } else {
                    finish();
                }
            } else {
                success = false;

                finish();

            }
            AdapterAssessment mAdapter = new AdapterAssessment(RecordScores.this, numbers);
            mAdapter.setRegNos(regNumbs);
            mAdapter.setSchool_id(school_id);
            mAdapter.setClass_name(class_name);
            mAdapter.setArm(arm);
            mAdapter.setSubject(subject);
            mAdapter.setAssessment(assessment);

            return success;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if (isSuccess) {
                progressBar.setVisibility(View.GONE);
                //set data to adapter
                AdapterAssessment mAdapter = new AdapterAssessment(RecordScores.this, numbers);
                // set View on recyclerview
                recyclerView.setAdapter(mAdapter);

            } else {
                Tools.toast("No record found for selected class/subject", RecordScores.this, R.color.yellow_900);
                finish();
            }
        }
    }
}