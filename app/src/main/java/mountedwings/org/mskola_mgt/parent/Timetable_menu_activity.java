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

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.NumberChildrenList;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Timetable_menu_activity extends AppCompatActivity {
    private String school_id = "", regNo = "", parent_id;
    private Spinner select_child;
    private ProgressBar progressBar1;
    private TextView date;
    private TextView load;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;
    ArrayList<NumberChildrenList> numbers = new ArrayList<>();

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;

    private void initComponent() {

        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        load = findViewById(R.id.load);
        select_child = findViewById(R.id.select_child);
        progressBar1 = findViewById(R.id.progress1);
        progressBar1.setVisibility(View.VISIBLE);
        date = findViewById(R.id.date);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_timetable_menu);

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));
        //parent_id from sharedPrefs
        parent_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));

        //   Toast.makeText(this, staff_id, Toast.LENGTH_SHORT).show();
        initComponent();

        select_child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_child.getSelectedItemPosition() >= 0) {
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
            if (!school_id.equals("") && !regNo.equals("")) {
                if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                    //run api and display result
                    new loadTimeTable().execute(school_id, regNo);
                } else {
                    Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);
                }
            } else {
                Tools.toast("Fill all necessary fields", Timetable_menu_activity.this, R.color.yellow_900);
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

            if (!text.equals("0") && !text.equals("") && !text.isEmpty()) {
                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {
                    NumberChildrenList numberChildrenList = new NumberChildrenList();
                    numberChildrenList.setRegNo(rows[i].split(";")[0]);
                    numberChildrenList.setName(rows[i].split(";")[1]);
                    numberChildrenList.setSchoolId(rows[i].split(";")[2]);
                    numberChildrenList.setSchoolName(rows[i].split(";")[3]);
                    numberChildrenList.setClass_name(rows[i].split(";")[4]);  //seems it's not yet in the API
                    numberChildrenList.setArm(rows[i].split(";")[5]);  //seems it's not yet in the API

                    numbers.add(numberChildrenList);

                    String[] data = new String[(numbers.size() + 1)];
                    data[0] = "";
                    for (int j = 1; j <= numbers.size(); j++) {
                        data[j] = numbers.get(j - 1).getName();
                    }

                    ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                    spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    select_child.setAdapter(spinnerAdapter1);

                    regNo = numbers.get(select_child.getSelectedItemPosition() - 1).getRegNo();
                    school_id = numbers.get(select_child.getSelectedItemPosition() - 1).getSchoolId();

                    progressBar1.setVisibility(View.INVISIBLE);
                }
            } else {
                Tools.toast("No student(s) assigned to you. Contact the school administration", Timetable_menu_activity.this);
                finish();
            }
        }
    }

    //loads Attendance per child
    private class loadTimeTable extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("gettimetable");
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
                String rows[] = text.split("<>");


                //show bottom sheet
                if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                final View view = getLayoutInflater().inflate(R.layout.attendance_sheet, null);
                ((TextView) view.findViewById(R.id.name)).setText("Timetable");
                ((TextView) view.findViewById(R.id.address)).setText("Time table upcoming");
                (view.findViewById(R.id.bt_close)).setOnClickListener(view1 -> mBottomSheetDialog.dismiss());

                mBottomSheetDialog = new BottomSheetDialog(Timetable_menu_activity.this);
                mBottomSheetDialog.setContentView(view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Objects.requireNonNull(mBottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }

                mBottomSheetDialog.show();
                mBottomSheetDialog.setOnDismissListener(dialog -> mBottomSheetDialog = null);


            } else {
                Tools.toast("An error occured", Timetable_menu_activity.this, R.color.red_800, Toast.LENGTH_LONG);
//                load.setVisibility(View.INVISIBLE);
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
                            Tools.toast("Back Online! Try again", Timetable_menu_activity.this, R.color.green_800);
                        else //load classes and assessments
                            new loadChildren().execute(parent_id);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), Timetable_menu_activity.this, R.color.red_500);
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