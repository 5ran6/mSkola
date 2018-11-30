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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Attendance_menu extends AppCompatActivity {
    private String school_id = "", staff_id = "", class_name = "", arm = "";
    private Spinner select_class, select_arm;
    private ProgressBar progressBar1, progressBar2;
    private TextView date;
    private TextView load;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;

    private void initComponent() {
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
        setContentView(R.layout.activity_attendance_menu);

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));
        //school_id/staff id from sharedPrefs

        staff_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
        school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

        //   Toast.makeText(this, staff_id, Toast.LENGTH_SHORT).show();
        initComponent();
        load = findViewById(R.id.load);
        select_arm = findViewById(R.id.select_arm);
        select_class = findViewById(R.id.select_class);
        progressBar1 = findViewById(R.id.progress1);
        progressBar1.setVisibility(View.VISIBLE);
        date = findViewById(R.id.date);

        progressBar2 = findViewById(R.id.progress2);
        progressBar2.setVisibility(View.INVISIBLE);

        //load classes and assessments
        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
            new loadClass().execute(school_id, staff_id);

        select_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_class.getSelectedItemPosition() >= 0) {
                    class_name = select_class.getSelectedItem().toString();
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        load.setOnClickListener(v -> {
            if (!class_name.equals("") && !date.equals("") && !arm.equals("")) {
                if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {

                    Intent intent1 = new Intent(getBaseContext(), AttendanceActivity.class);
                    intent1.putExtra("school_id", school_id);
                    intent1.putExtra("class_name", class_name);
                    intent1.putExtra("arm", arm);
                    intent1.putExtra("date", date.getText().toString().trim());
                    startActivity(intent1);

                } else {
                    Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);
                }
            } else {
                Tools.toast("Fill all necessary fields", Attendance_menu.this, R.color.yellow_900);
            }
        });
    }

    //finished
    private void loadArm() {
        progressBar2.setVisibility(View.VISIBLE);
        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
            new loadArms().execute(school_id, staff_id, class_name);
    }

    private class loadArms extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getattendancearm");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
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
                String[] dataRows = text.split(",");
                String[] data = new String[(dataRows.length + 1)];
                data[0] = "";
                for (int i = 1; i <= dataRows.length; i++) {
                    data[i] = dataRows[(i - 1)];
                }

                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_arm.setAdapter(spinnerAdapter1);
                arm = select_arm.getSelectedItem().toString();
                progressBar2.setVisibility(View.INVISIBLE);

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

    //loads Classes and arms
    private class loadClass extends AsyncTask<String, Integer, String> {
        String x;

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getattendanceclass");
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
                String rows[] = text.split(";");

                String[] data = new String[(rows.length + 1)];
                data[0] = "";

                //I did one jazz here. About to test
                System.arraycopy(rows, 0, data, 1, rows.length);
                //Boom! the JAzz worked ;)

                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_class.setAdapter(spinnerAdapter1);
                class_name = select_class.getSelectedItem().toString();
                progressBar1.setVisibility(View.INVISIBLE);
            } else {
                Tools.toast("Either you're not a CLASS TEACHER or you have to " + getResources().getString(R.string.no_internet_connection), Attendance_menu.this, R.color.red_800, Toast.LENGTH_LONG);
                load.setVisibility(View.INVISIBLE);
            }
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
                            Tools.toast("Back Online! Try again", Attendance_menu.this, R.color.green_800);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), Attendance_menu.this, R.color.red_500);
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