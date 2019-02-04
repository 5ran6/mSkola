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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.ViewResultAdapter;
import mountedwings.org.mskola_mgt.data.NumberViewResult;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class ViewResultActivity extends AppCompatActivity {
    String school_id, staff_id, TAG = "mSkola";
    String session;

    String class_name;
    String class_;
    String arm;

    String term;
    ArrayList<NumberViewResult> numbers = new ArrayList<>();


    private RecyclerView list;
    private ArrayList<byte[]> allPassport_aPerson = new ArrayList<>();
    private storageFile data = new storageFile();
    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> regNo = new ArrayList<>();

    private ArrayList<String> total = new ArrayList<>();
    private ArrayList<String> no_subjects = new ArrayList<>();
    private ArrayList<String> position = new ArrayList<>();
    private ArrayList<String> average = new ArrayList<>();
    private BroadcastReceiver mReceiver;
    private int w = 0;
    private int status;

    ProgressBar loading;
    private ViewResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);
        Tools.toast("Requesting results.....", this, R.color.green_600);
        Intent intent = getIntent();

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        //school_id/staff id from sharedPrefs

        staff_id = mPrefs.getString("email_address", intent.getStringExtra("email_address"));
        school_id = mPrefs.getString("school_id", intent.getStringExtra("school_id"));
        arm = intent.getStringExtra("arm");
        term = intent.getStringExtra("term");
        class_ = intent.getStringExtra("class");
        class_name = intent.getStringExtra("class_name");
        session = intent.getStringExtra("session");
        TextView heading = findViewById(R.id.heading);
        heading.setText(String.format("Result for %s", class_name));
        FloatingActionButton fab_done = findViewById(R.id.done);

        loading = findViewById(R.id.loading);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(false);

        adapter = new ViewResultAdapter(numbers);
        list.setAdapter(adapter);


        //hide parentView
        loading.setVisibility(View.VISIBLE);

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
//            data = storageObj;

            storageObj.setOperation("getpsstudentinfo");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
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
            if (!text.equals("0") && !text.equals("")) {

                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {
                    students.add(rows[i].split(";")[1]);
                    regNo.add(text.split("<>")[i].split(";")[0]);
                }
            } else {
                Tools.toast("No student found in the selected class. Compile Result first", ViewResultActivity.this);
                finish();
            }
            //finally
            if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
                new second_loading().execute();
            else {
                Tools.toast(getResources().getString(R.string.no_internet_connection), ViewResultActivity.this, R.color.red_600);
                finish();
            }

        }
    }


    public class second_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            boolean compiled = false;
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentprintresult");
            for (int i = 0; i < regNo.size(); i++) {
                storageObj.setStrData(school_id + "<>" + regNo.get(i) + "<>" + "arm" + "<>" + "yes" + "<>" + session + "<>" + term + "<>" + class_name + "<>" + arm);
                storageFile sentData = new serverProcess().requestProcess(storageObj);
                allPassport_aPerson.add(data.getImageFiles().get(i));
                String text = sentData.getStrData();
                String[] rows = text.split(";");

                if (!text.equalsIgnoreCase("not compiled"))
                    compiled = true;
                try {
                    total.add(i, rows[0]);
                    average.add(i, rows[1]);
                    no_subjects.add(i, rows[2]);
                    position.add(i, rows[3]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (compiled)
                return "success";
            else
                return "not compile";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Tools.toast("Fetching Results", ViewResultActivity.this);
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (text.equals("success")) {
                loading.setVisibility(View.GONE);
                //worked. Split and save into file
                for (int i = 0; i < students.size(); i++) {
                    NumberViewResult number = new NumberViewResult();
                    number.setName(students.get(i));
                    number.settotal(total.get(i));
                    number.setaverage(average.get(i));
                    number.setno_subjects(no_subjects.get(i));
                    number.setPosition(position.get(i));
                    number.setImage(allPassport_aPerson.get(i));
                    numbers.add(number);
                }

                //show recyclerView with inflated views
                adapter = new ViewResultAdapter(numbers);
                list.setAdapter(adapter);

                adapter.setOnItemClickListener((view, obj, position) -> {

                });
            } else {
                //               Toast.makeText(getApplicationContext(), "No results found. Results may not have been compile", Toast.LENGTH_SHORT).show();
                showCustomDialogFailure("No results found. Results may not have been compile");

            }
            loading.setVisibility(View.GONE);
        }
    }

    private void showCustomDialogFailure(String error) {

        //progress bar and text will disappear

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_error);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TextView error_message = dialog.findViewById(R.id.content);
        error_message.setText(error);

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
                            Tools.toast("Back Online! Try again", ViewResultActivity.this, R.color.green_800);
                        else
                            new first_loading().execute(school_id, class_, arm);

                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), ViewResultActivity.this, R.color.red_600);
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

