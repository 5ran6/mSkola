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

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.Collections;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class PromoteStudentsActivity extends AppCompatActivity {
    private String regNos;
    private String[] classes;
    private String session, class_name;
    private String arm;

    private Spinner select_class, select_arm, select_session;
    private ProgressBar progressBar1, progressBar2, progressBar3;
    private int counter = 0;

    private int PREFERENCE_MODE_PRIVATE = 0;
    String school_id, staff_id, TAG = "mSkola";
    private BroadcastReceiver mReceiver;
    private int w = 0, status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_students_final_menu);

        Intent intent = getIntent();

        regNos = intent.getStringExtra("reg_nos");

        SharedPreferences mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);

        //school_id/staff id from sharedPrefs
        staff_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
        school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));


        TextView load = findViewById(R.id.load);
        select_arm = findViewById(R.id.select_arm);

        select_class = findViewById(R.id.select_class);

        select_session = findViewById(R.id.select_session);

        progressBar1 = findViewById(R.id.progress1);
        progressBar1.setVisibility(View.VISIBLE);

        progressBar2 = findViewById(R.id.progress2);
        progressBar2.setVisibility(View.INVISIBLE);

        progressBar3 = findViewById(R.id.progress3);
        progressBar3.setVisibility(View.INVISIBLE);


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
                        loadSessionList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        select_session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_session.getSelectedItemPosition() >= 0) {
                    session = select_session.getSelectedItem().toString();
                    //run new thread

                    counter++;
                    //run NO new thread
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        load.setOnClickListener(v ->

        {
            if (!class_name.isEmpty() || !arm.isEmpty()) {
                if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
                    new promoteStudents().execute();
                else
                    Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);

            } else {
                Tools.toast("Fill all necessary fields!", PromoteStudentsActivity.this, R.color.yellow_800);
            }
        });
    }


    private void loadSessionList() {
        progressBar3.setVisibility(View.VISIBLE);
        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
            new loadSession().execute();
    }


    private void loadArm() {
        progressBar2.setVisibility(View.VISIBLE);
        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
            new loadArms().execute(school_id, class_name);
    }


    private class loadArms extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getpmsarms");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
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
                String[] dataRows = text.split("<>")[0].split(",");
                String[] data = new String[(dataRows.length + 1)];
                data[0] = "";
                for (int i = 1; i <= dataRows.length; i++) {
                    data[i] = dataRows[(i - 1)];
                }
                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_arm.setAdapter(spinnerAdapter1);
                arm = select_arm.getSelectedItem().toString();
                progressBar2.setVisibility(View.INVISIBLE);
                counter = -1;
            } else {
                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, Collections.emptyList());
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

    //DONE
    private class loadSession extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getprsessions");
            storageObj.setStrData(school_id);
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
            if (!text.equals("0") && !text.equals("")) {
                String[] dataRows = text.split("<>")[0].split(";");
                String[] data = new String[(dataRows.length + 1)];
                data[0] = "";
                for (int i = 1; i <= dataRows.length; i++) {
                    data[i] = dataRows[(i - 1)];
                }

                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_session.setAdapter(spinnerAdapter1);
                session = select_session.getSelectedItem().toString();
                progressBar3.setVisibility(View.INVISIBLE);
                counter = -1;
            } else {
                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, Collections.emptyList());
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_session.setAdapter(spinnerAdapter1);
                progressBar3.setVisibility(View.INVISIBLE);
                showCustomDialogFailure("An error occurred. No Session found. Contact Admin");
            }

        }

        private void showCustomDialogFailure(String error) {
            final Dialog dialog = new Dialog(getApplicationContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.dialog_error);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            TextView error_message = dialog.findViewById(R.id.content);
            error_message.setText(error);

            (dialog.findViewById(R.id.bt_close)).setOnClickListener(v -> {
                dialog.dismiss();
                try {
//                if (lyt_progress.getVisibility() == View.VISIBLE && parent_layout.getVisibility() == View.INVISIBLE) {
//                    lyt_progress.setVisibility(View.INVISIBLE);
//                    parent_layout.setVisibility(View.VISIBLE);
//                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    private class promoteStudents extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("promotestudents");
            storageObj.setStrData(school_id + "<>" + regNos + "<>" + class_name + "<>" + arm + "<>" + session + "<>" + staff_id);
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
            if (text.equals("success")) {
                showCustomDialogSuccess("Student(s) successfully PROMOTED to " + class_name.toUpperCase() + " " + arm.toUpperCase() + "!");
            } else {
                showCustomDialogFailure("An error occurred. Contact Admin");
            }
        }

        private void showCustomDialogFailure(String error) {
            final Dialog dialog = new Dialog(getApplicationContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.dialog_error);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            TextView error_message = dialog.findViewById(R.id.content);
            error_message.setText(error);

            (dialog.findViewById(R.id.bt_close)).setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
            dialog.show();
            dialog.getWindow().setAttributes(lp);
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
            storageObj.setOperation("getpsclass");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
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
            System.out.println(text);

            if (!text.equals("0") && !text.isEmpty()) {
                text = text.split("##")[0];
                String[] dataRows = text.split("<>");

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

    private void showCustomDialogSuccess(String msg) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.title)).setText(msg);

        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
                            Tools.toast("Back Online! Try again", PromoteStudentsActivity.this, R.color.green_800);
                        else
                            //load classes and assessments
                            new initialLoad().execute(school_id, staff_id);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), PromoteStudentsActivity.this, R.color.red_500);
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
