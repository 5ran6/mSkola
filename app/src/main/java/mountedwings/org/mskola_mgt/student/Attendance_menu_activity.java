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
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberChildrenList;
import mountedwings.org.mskola_mgt.utils.Tools;

public class Attendance_menu_activity extends AppCompatActivity {
    private String school_id = "", regNo = "", attendance_date = "";
    private ProgressBar progressBar;
    private TextView date;
    private TextView load;
    private BroadcastReceiver mReceiver;
    private int w = 0, status = 1;
    ArrayList<NumberChildrenList> numbers = new ArrayList<>();

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;

    private void initComponent() {
        progressBar = findViewById(R.id.progress);
        View bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        load = findViewById(R.id.load);
        date = findViewById(R.id.date);

        (findViewById(R.id.pick_date)).setOnClickListener(this::dialogDatePickerLight);
    }

    private void dialogDatePickerLight(final View bt) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    long date_ship_millis = calendar.getTimeInMillis();
                    date.setText(Tools.getFormattedDate(date_ship_millis));
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_attendance_menu);
        school_id = getIntent().getStringExtra("school_id");
        regNo = getIntent().getStringExtra("reg_no");

        //   Toast.makeText(this, staff_id, Toast.LENGTH_SHORT).show();
        initComponent();


        load.setOnClickListener(v -> {
            attendance_date = date.getText().toString().trim();
            if (!school_id.equals("") && !attendance_date.equals("") && !regNo.equals("")) {
                //       if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                //run api and display result
                new loadAttendance().execute(school_id, regNo, attendance_date);
                //     } else {
                //       Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);
                // }
            } else {
                Tools.toast("Select a date to view its attendance", Attendance_menu_activity.this, R.color.yellow_900);
            }
        });
    }

    //loads Attendance per child
    private class loadAttendance extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getattendance");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressbar
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            progressBar.setVisibility(View.GONE);
            if (!text.equals("0") && !text.isEmpty() && !text.equals("no attendance") && !text.equals("error")) {
                String morning = "", afternoon = "";

                String rows[] = text.split("<>");
                switch (Integer.valueOf(rows[0])) {
                    case 0:
                        morning = "Not in school";
                        break;
                    case 1:
                        morning = "Yes, in school";

                        break;
                    case 2:
                        morning = "Was a public holiday";
                        break;
                }
                switch (Integer.valueOf(rows[1])) {
                    case 0:
                        afternoon = "Not in school";
                        break;
                    case 1:
                        afternoon = "Yes, in school";

                        break;
                    case 2:
                        afternoon = "Was a public holiday";
                        break;
                }

                //show bottom sheet
                if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                final View view = getLayoutInflater().inflate(R.layout.attendance_sheet, null);
                ((TextView) view.findViewById(R.id.name)).setText("Attendance on " + attendance_date);
                ((TextView) view.findViewById(R.id.address)).setText("Morning: " + morning + "\n Afternoon: " + afternoon);
                (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetDialog.dismiss();
                    }
                });

                mBottomSheetDialog = new BottomSheetDialog(Attendance_menu_activity.this);
                mBottomSheetDialog.setContentView(view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }

                mBottomSheetDialog.show();
                mBottomSheetDialog.setOnDismissListener(dialog -> mBottomSheetDialog = null);
            } else if (text.equals("no attendance") || text.equals("error")) {
                Tools.toast("No attendance recorded for this day", Attendance_menu_activity.this);
                progressBar.setVisibility(View.GONE);
            } else {
                Tools.toast("An error occured", Attendance_menu_activity.this, R.color.red_800, Toast.LENGTH_LONG);
//                load.setVisibility(View.INVISIBLE);
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
//                            Tools.toast("Back Online! Try again", Attendance_menu_activity.this, R.color.green_800);
//
//                    }
//
//                    @Override
//                    public void onConnectionFail(String errorMsg) {
//                        status = 0;
//                        Tools.toast(getResources().getString(R.string.no_internet_connection), Attendance_menu_activity.this, R.color.red_500);
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
}