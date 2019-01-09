package mountedwings.org.mskola_mgt.parent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberChildrenList;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Attendance_menu_activity extends AppCompatActivity {
    private String school_id = "", regNo = "", attendance_date = "", parent_id;
    private Spinner select_child;
    private ProgressBar progressBar1, progressBar;
    private TextView date;
    private TextView load;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;
    ArrayList<NumberChildrenList> numbers = new ArrayList<>();

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;

    private void initComponent() {
        progressBar = findViewById(R.id.progress);
        View bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        load = findViewById(R.id.load);
        select_child = findViewById(R.id.select_child);
        progressBar1 = findViewById(R.id.progress1);
        progressBar1.setVisibility(View.VISIBLE);
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


        //   Toast.makeText(this, staff_id, Toast.LENGTH_SHORT).show();
        initComponent();

        select_child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_child.getSelectedItemPosition() >= 1) {
                    //get child school ID and reg number
                    regNo = numbers.get(select_child.getSelectedItemPosition() - 1).getRegNo();
                    school_id = numbers.get(select_child.getSelectedItemPosition() - 1).getSchoolId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        load.setOnClickListener(v -> {
            attendance_date = date.getText().toString().trim();
            if (!school_id.equals("") && !attendance_date.equals("") && !regNo.equals("")) {
                if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                    //run api and display result
                    new loadAttendance().execute(school_id, regNo, attendance_date);
                } else {
                    Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);
                }
            } else {
                Tools.toast("Fill all necessary fields", Attendance_menu_activity.this, R.color.yellow_900);
            }
        });
    }


    //loads Children
    private class loadChildren extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getchildren");
            storageObj.setStrData(strings[0]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);

            if (!text.equals("0") && !text.equals("") && !text.isEmpty()) {
                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {
                    NumberChildrenList numberChildrenList = new NumberChildrenList();
                    numberChildrenList.setRegNo(rows[i].split(";")[0]);
                    numberChildrenList.setName(rows[i].split(";")[1]);
                    numberChildrenList.setSchoolId(rows[i].split(";")[2]);
                    numberChildrenList.setSchoolName(rows[i].split(";")[3]);
                    numberChildrenList.setClass_name(rows[i].split(";")[4]);
                    numberChildrenList.setArm(rows[i].split(";")[5]);

                    numbers.add(numberChildrenList);

                    String[] data = new String[(numbers.size() + 1)];
                    data[0] = "";
                    for (int j = 1; j <= numbers.size(); j++) {
                        data[j] = numbers.get(j - 1).getName();
                    }

                    ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                    spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    select_child.setAdapter(spinnerAdapter1);


                    progressBar1.setVisibility(View.INVISIBLE);
                }
            } else {
                Tools.toast("No student(s) assigned to you. Contact the school administration", Attendance_menu_activity.this);
                finish();
            }
        }
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
        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));
        //parent_id from sharedPrefs
        parent_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));

        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                w++;
                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        status = 1;
                        if (w > 1)
                            Tools.toast("Back Online! Try again", Attendance_menu_activity.this, R.color.green_800);
                        else //load classes and assessments
                            //    new loadChildren().execute("4raan6@gmail.com");
                            new loadChildren().execute(parent_id);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), Attendance_menu_activity.this, R.color.red_500);
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