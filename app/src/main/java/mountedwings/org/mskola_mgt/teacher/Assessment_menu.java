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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Assessment_menu extends AppCompatActivity {
    private String school_id = "", staff_id = "", class_name = "", arm = "", assessment = "", subject = "";
    private Spinner select_class, select_arm, select_subject, select_assessment;
    private ProgressBar progressBar1, progressBar2, progressBar3, progressBar4;
    private int counter = 0;
    private BroadcastReceiver mReceiver;
    private int w = 0;
    private int status;
    private AsyncTask lastThread;

    @Override
    protected void onResume() {
        super.onResume();

        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                w++;
                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        status = 1;
                        if (w > 1) {
                            lastThread.execute();
                            Tools.toast("Back Online! Resuming request....", Assessment_menu.this, R.color.green_800);
                        } else
                            //load classes and assessments
                            lastThread = new initialLoad().execute();
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Log.d("mSkola", String.valueOf(status));
                        Tools.toast(getResources().getString(R.string.no_internet_connection), Assessment_menu.this, R.color.red_600);
                        lastThread.cancel(true);
                    }
                }).execute();
            }

        };

        registerReceiver(
                this.mReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_menu);

        //get stuff from sharedPrefs
        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        //school_id/staff id from sharedPrefs
        staff_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
        school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));


        TextView load = findViewById(R.id.load);
        select_arm = findViewById(R.id.select_arm);
        select_class = findViewById(R.id.select_class);
        select_subject = findViewById(R.id.subject);
        select_assessment = findViewById(R.id.assessment);
        progressBar1 = findViewById(R.id.progress1);
        progressBar1.setVisibility(View.VISIBLE);

        progressBar2 = findViewById(R.id.progress2);
        progressBar2.setVisibility(View.INVISIBLE);

        progressBar3 = findViewById(R.id.progress3);
        progressBar3.setVisibility(View.INVISIBLE);

        progressBar4 = findViewById(R.id.progress4);
        progressBar4.setVisibility(View.INVISIBLE);
//        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
//            new initialLoad().execute(school_id, staff_id);

        select_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_class.getSelectedItemPosition() >= 0) {
                    class_name = select_class.getSelectedItem().toString();
                    counter++;
                    //run new thread
                    if (counter >= 1)
                        loadArm();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        select_arm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_arm.getSelectedItemPosition() >= 0) {
                    arm = select_arm.getSelectedItem().toString();
                    //run new thread

                    counter++;
                    //run new thread
                    if (counter >= 1)
                        loadSubject();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        select_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = select_subject.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        select_assessment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                assessment = select_assessment.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        load.setOnClickListener(v -> {
            if (!class_name.isEmpty() && !assessment.isEmpty() && !arm.isEmpty() && !subject.isEmpty()) {
                if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                    Intent intent1 = new Intent(getBaseContext(), Assessment.class);
                    intent1.putExtra("school_id", school_id);
                    intent1.putExtra("class_name", class_name);
                    intent1.putExtra("assessment", assessment);
                    intent1.putExtra("arm", arm);
                    intent1.putExtra("subject", subject);
                    startActivity(intent1);
                } else {
                    Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);
                }
            } else {
                Tools.toast("Fill all necessary fields", Assessment_menu.this, R.color.yellow_700);
            }
        });

    }


    private void loadArm() {
        progressBar2.setVisibility(View.VISIBLE);
        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
            lastThread = new loadArms().execute();
    }

    private void loadSubject() {
        progressBar3.setVisibility(View.VISIBLE);
        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
            lastThread = new loadSubject().execute();
    }

    //loads arms and cas
    private class loadArms extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getrsarm");
//            school_id, staff_id, class_name
            storageObj.setStrData(school_id + "<>" + staff_id + "<>" + class_name);
            storageFile sentData = new serverProcess().requestProcess(storageObj);

            return sentData.getStrData();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar2.setVisibility(View.VISIBLE);
            progressBar4.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (!text.equals("0") && !text.isEmpty()) {
                int no_cas = Integer.parseInt(text.split("<>")[1]);

                String[] dataRows = text.split("<>")[0].split(",");
                String[] data = new String[(dataRows.length + 1)];
                ArrayList ca = new ArrayList();
//                String[] cas = new String[(no_cas + 1)];
                //              cas[0] = "";

                data[0] = "";
                for (int i = 1; i <= dataRows.length; i++) {
                    data[i] = dataRows[(i - 1)];
                }
                ca.add("");

                for (int i = 1; i <= no_cas; i++) {
                    ca.add(i, "CA" + String.valueOf(i));
                }

                ca.add("Exams");

                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_arm.setAdapter(spinnerAdapter1);
                arm = select_arm.getSelectedItem().toString();
                progressBar2.setVisibility(View.INVISIBLE);

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, ca);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_assessment.setAdapter(spinnerAdapter);
                progressBar4.setVisibility(View.INVISIBLE);
                counter = -1;

            } else {
                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, Collections.emptyList());
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_arm.setAdapter(spinnerAdapter1);
                progressBar2.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
    }

    private class loadSubject extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getrssubject");
            //school_id, staff_id, class_name, arm
            storageObj.setStrData(school_id + "<>" + staff_id + "<>" + class_name + "<>" + arm);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);

            if (!text.equals("0") && !text.isEmpty()) {
                String[] dataRows = text.split(";");
                String[] data = new String[(dataRows.length + 1)];
                data[0] = "";
                for (int i = 1; i <= dataRows.length; i++) {
                    data[i] = dataRows[(i - 1)];
                }
                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_subject.setAdapter(spinnerAdapter1);
                subject = select_subject.getSelectedItem().toString();
                progressBar3.setVisibility(View.INVISIBLE);
                progressBar3.setVisibility(View.INVISIBLE);
            } else {
                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, Collections.emptyList());
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_subject.setAdapter(spinnerAdapter1);

                progressBar3.setVisibility(View.INVISIBLE);

            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
    }

    //loads Classes
    private class initialLoad extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getrsclass");
            storageObj.setStrData(school_id + "<>" + staff_id);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);

            if (!text.equals("0") && !text.isEmpty()) {
                String dataRows[] = text.split("<>");
                String[] data = new String[(dataRows.length + 1)];
                data[0] = "";
                for (int i = 1; i <= dataRows.length; i++) {
                    data[i] = dataRows[(i - 1)];
                }

                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_class.setAdapter(spinnerAdapter1);
                class_name = select_class.getSelectedItem().toString();


                progressBar1.setVisibility(View.INVISIBLE);
                counter = -1;
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
