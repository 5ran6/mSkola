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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import java.util.ArrayList;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.AdapterStudentsSubjectTeachers;
import mountedwings.org.mskola_mgt.data.NumberSubjectTeachers;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.widget.LineItemDecoration;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class SubjectTeachers extends AppCompatActivity {

    private ArrayList<NumberSubjectTeachers> numbers = new ArrayList<>();
    private ArrayList<byte[]> teachers_passport = new ArrayList<>();

    private RecyclerView recyclerView;
    private BroadcastReceiver mReceiver;
    private int w = 0;
    private int status = 1;
    private String reg_no;
    private String school_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_students);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Subject Teachers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.blue_400);
    }

    private void initComponent() {
        int PREFERENCE_MODE_PRIVATE = 0;
        SharedPreferences mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(false);

        //school_id/students id from sharedPrefs
        reg_no = mPrefs.getString("student_reg_no", getIntent().getStringExtra("reg_no"));
        school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));
        new getSubjectTeachers().execute(school_id, reg_no);
    }

    private class getSubjectTeachers extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getsubjectteachers");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            teachers_passport = sentData.getImageFiles();
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);

            if (!text.equals("not found") && !text.isEmpty() && text.equalsIgnoreCase("found")) {
                String rows[] = text.split("##");
                String subjects[] = rows[0].split(";");
                String teachers[] = rows[1].split("<>");
                try {


                    for (int i = 0; i < rows.length; i++) {
                        String teacher = teachers[i];
                        NumberSubjectTeachers number = new NumberSubjectTeachers();
                        number.setName(teacher.split(";")[0]);
                        number.setPhone(teacher.split(";")[1]);
                        number.setEmail(teacher.split(";")[2]);
                        number.setSubject(subjects[i]);
                        number.setPassport(teachers_passport.get(i));
                        numbers.add(number);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Seems something went wrong", Toast.LENGTH_SHORT).show();
                    finish();
                    e.printStackTrace();
                }
                //set data to adapter
                AdapterStudentsSubjectTeachers mAdapter = new AdapterStudentsSubjectTeachers(SubjectTeachers.this, numbers);
                recyclerView.setAdapter(mAdapter);

            } else {
                Tools.toast("No subject teachers recorded yet for your class.", SubjectTeachers.this, R.color.red_500);
                finish();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

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
//                            Tools.toast("Back Online! Try again", SubjectTeachers.this, R.color.green_800);
//                        else
//                            new getSubjectTeachers().execute(school_id, reg_no);
//                    }
//
//                    @Override
//                    public void onConnectionFail(String errorMsg) {
//                        status = 0;
//                        Tools.toast(getResources().getString(R.string.no_internet_connection), SubjectTeachers.this, R.color.red_500);
//                        finish();
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}