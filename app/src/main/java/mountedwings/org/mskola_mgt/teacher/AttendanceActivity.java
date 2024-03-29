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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.AttendanceAdapter;
import mountedwings.org.mskola_mgt.data.Number;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AttendanceActivity extends AppCompatActivity {
    private View back_drop;
    private boolean rotate = false;
    private View lyt_hols;
    private View lyt_save;
    public CheckBox all_morning, all_afternoon;
    private ArrayList<Number> numbers;
    private ArrayList names = new ArrayList();
    private ArrayList regNo = new ArrayList();
    private RecyclerView list;
    private FloatingActionButton fab_add;
    public String[] morning = null, afternoon = null;

    private String date, school_id, class_name, arm, TAG = "mSkola";
    private ProgressBar loading;
    private AttendanceAdapter adapter;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;
    private AsyncTask lastThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        fab_add = findViewById(R.id.see_more);
        FloatingActionButton fab_holidays = findViewById(R.id.fab_hols);
        FloatingActionButton fab_save = findViewById(R.id.fab_save);
        TextView pub = findViewById(R.id.pub);
        TextView sav = findViewById(R.id.sav);
        back_drop = findViewById(R.id.back_drop);
        all_morning = findViewById(R.id.all_checkbox1);
        all_afternoon = findViewById(R.id.all_checkbox2);

        loading = findViewById(R.id.loading);
        loading.setVisibility(VISIBLE);

        lyt_hols = findViewById(R.id.lyt_mic);
        lyt_save = findViewById(R.id.lyt_call);
        ViewAnimation.initShowOut(lyt_hols);
        ViewAnimation.initShowOut(lyt_save);
        back_drop.setVisibility(GONE);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(false);

/////////////////////////////////////////////////////////////////////////////////////////////////////  init
        school_id = getIntent().getStringExtra("school_id");
        class_name = getIntent().getStringExtra("class_name");
        arm = getIntent().getStringExtra("arm");
        date = getIntent().getStringExtra("date");

        loading.setVisibility(View.VISIBLE);

        //        new first_loading().execute("cac180826043520", "JSS1", "A", "2018-10-03");
        //  new first_loading().execute("cac181009105222", "JSS1", "A", "2018-10-03");
///////////////////////////////////////////////////////////////////////////////////////////////////////


        back_drop.setOnClickListener(v -> toggleFabMode(fab_add));

        fab_holidays.setOnClickListener(v -> {
            if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                new publicHolidays().execute(school_id, class_name, arm, date);
            } else {
                Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);
            }
        });

        fab_save.setOnClickListener(v -> {
            //    if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            lastThread = new markAttendance().execute(school_id, class_name, arm, date);
        });


        fab_add.setOnClickListener(v -> toggleFabMode(v));

        sav.setOnClickListener(v -> {
            //   if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            lastThread = new markAttendance().execute(school_id, class_name, arm, date);
        });
        pub.setOnClickListener(v -> {
            // if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            lastThread = new publicHolidays().execute(school_id, class_name, arm, date);

        });

        all_morning.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                //tick all
                for (int i = 0; i < numbers.size(); i++) {
                    numbers.get(i).setSelected(true);
                }
                adapter.selectAll();
            } else {
                //uncheck all
                for (int i = 0; i < numbers.size(); i++) {
                    numbers.get(i).setSelected(false);
                }
                adapter.unSelectAll();
            }
        });
        all_afternoon.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                //tick all
                for (int i = 0; i < numbers.size(); i++) {
                    numbers.get(i).setSelected1(true);
                }
                adapter.selectAll1();
            } else {
                //uncheck all
                for (int i = 0; i < numbers.size(); i++) {
                    numbers.get(i).setSelected1(false);
                }
                adapter.unSelectAll1();
            }
        });
    }

    //DONE
    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_hols);
            ViewAnimation.showIn(lyt_save);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_hols);
            ViewAnimation.showOut(lyt_save);
            back_drop.setVisibility(View.GONE);
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
                            Tools.toast("Back Online! Reconnecting...", AttendanceActivity.this, R.color.green_800);
                        else
                            lastThread = new first_loading().execute();

                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        if (w > 1) {
                            try {
                                Tools.toast(getResources().getString(R.string.no_internet_connection), AttendanceActivity.this, R.color.red_900);
                                lastThread.cancel(true);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            Tools.toast(getResources().getString(R.string.no_internet_connection), AttendanceActivity.this, R.color.red_900);
                        }

                        //              finish();
                    }
                }).

                        execute();
            }

        }

        ;

        registerReceiver(
                this.mReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
        super.

                onResume();

    }

    public class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                do {
                    storageFile storageObj = new storageFile();
                    storageObj.setOperation("loadattendance");
                    //school_id, class_name, arm, date
                    storageObj.setStrData(school_id + "<>" + class_name + "<>" + arm + "<>" + date);
                    storageFile sentData = new serverProcess().requestProcess(storageObj);
                    return sentData.getStrData();

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
            if (!text.isEmpty() && !text.equals("0")) {
                loading.setVisibility(View.GONE);

                //get values
                morning = new String[text.split("<>").length];
                afternoon = new String[text.split("<>").length];

                numbers = new ArrayList<>();

                //if there was previous data
                if (text.contains("##")) {
                    morning = text.split("##")[1].split(";");
                    afternoon = text.split("##")[2].split(";");
                    text = text.split("##")[0];
                }

                //worked. Split into reg and names
                String[] rows = text.split("<>");
                if (rows.length > 0) {
                    for (int i = 0; i < rows.length; i++) {
                        regNo.add(rows[i].split(";")[0]);
                        names.add(rows[i].split(";")[1]);

                        Number number = new Number();
                        number.setONEs((i + 1) + "");
                        number.setTextONEs(names.get(i).toString());
                        try {
                            //checked morning
                            if (morning[i].trim().equals("1")) number.setSelected(true);

                            //checked afternoon
                            if (afternoon[i].trim().equals("1")) number.setSelected1(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        numbers.add(number);
                    }
                    //show recyclerView with inflated views
                    adapter = new AttendanceAdapter(numbers);
                    list.setAdapter(adapter);
                } else {
                    // display an EMPTY error dialog and return to previous activity
                    Tools.toast("Oops! Something went wrong", AttendanceActivity.this, R.color.red_600);
                    finish();
                }
            }
            if (text.equalsIgnoreCase("network error")) {
                Tools.toast("Network error. Reconnecting...", AttendanceActivity.this, R.color.red_900);
                new first_loading().execute();
            }

        }
    }

    private class markAttendance extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                do {
                    storageFile storageObj = new storageFile();
                    storageObj.setOperation("takeattendance");
                    //to get the attendance values
                    StringBuilder allReg = new StringBuilder();
                    StringBuilder morning = new StringBuilder();
                    StringBuilder afternoon = new StringBuilder();

                    for (int i = 0; i < regNo.size(); i++) {
                        if (allReg.length() == 0) {
                            allReg = new StringBuilder(regNo.get(i).toString());
                        } else {
                            allReg.append(";").append(regNo.get(i).toString());
                        }

                        //morning
                        if (morning.length() == 0) {
                            //get checkbox at position i
                            //get the string at that index
                            if (numbers.get(i).isSelected()) {
                                morning = new StringBuilder("1");
                            } else {
                                morning = new StringBuilder("0");
                            }
                        } else {
                            if (numbers.get(i).isSelected()) {
                                morning.append(";1");
                            } else {
                                morning.append(";0");
                            }
                        }
//afternoon
                        if (afternoon.length() == 0) {
                            if (numbers.get(i).isSelected1()) {
                                afternoon = new StringBuilder("1");
                            } else {
                                afternoon = new StringBuilder("0");
                            }
                        } else {
                            if (numbers.get(i).isSelected1()) {
                                afternoon.append(";1");
                            } else {
                                afternoon.append(";0");
                            }
                        }


                    }


                    storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + allReg + "<>" + morning + "<>" + afternoon);
                    storageFile sentData = new serverProcess().requestProcess(storageObj);

                    String text = sentData.getStrData();

                    return text;

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
//            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
//            Tools.toast("No achievements found", AttendanceActivity.this, R.color.yellow_600);
            try {
                if (text.equals("success")) {
                    Tools.toast("Successfully marked", AttendanceActivity.this, R.color.green_800);
                    finish();
                }

                if (text.equalsIgnoreCase("network error")) {
                    Tools.toast("Network error. Reconnecting...", AttendanceActivity.this, R.color.red_900);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setMessage("Cancel without saving attendance?")
                .setCancelable(true)
                .setPositiveButton("Yes, cancel ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }

    private class publicHolidays extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                do {
                    storageFile storageObj = new storageFile();
                    storageObj.setOperation("takeattendance");
                    String allReg = "", morning = "", afternoon = "";
                    for (int i = 0; i < regNo.size(); i++) {
                        if (allReg.isEmpty()) {
                            allReg = regNo.get(i).toString();
                        } else {
                            allReg += ";" + regNo.get(i).toString();
                        }

                        if (morning.isEmpty()) {
                            morning = "2";
                        } else {
                            morning += ";2";
                        }

                        if (afternoon.isEmpty()) {
                            afternoon = "2";
                        } else {
                            afternoon += ";2";
                        }
                    }


                    storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + allReg + "<>" + morning + "<>" + afternoon);
                    storageFile sentData = new serverProcess().requestProcess(storageObj);

                    String text = sentData.getStrData();

                    return text;

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
//            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            try {
                if (text.equals("success")) {
//                    Toast.makeText(getApplicationContext(), "Successfully marked", Toast.LENGTH_SHORT).show();
                    Tools.toast("Successfully marked", AttendanceActivity.this, R.color.green_800);
                    finish();
                }
                if (text.equalsIgnoreCase("network error")) {
                    Tools.toast("Network error. Reconnecting...", AttendanceActivity.this, R.color.red_900);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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

