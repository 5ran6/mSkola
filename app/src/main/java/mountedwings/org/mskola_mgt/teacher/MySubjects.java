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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.NumbersMySubjectsAdapter;
import mountedwings.org.mskola_mgt.data.NumberMySubjects;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.widget.LineItemDecoration;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class MySubjects extends AppCompatActivity {

    private ArrayList<NumberMySubjects> numbers = new ArrayList<>();

    private RecyclerView recyclerView;
    private BroadcastReceiver mReceiver;
    private int w = 0;
    private int status;

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
        getSupportActionBar().setTitle("List of your subjects");
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


        //school_id/staff id from sharedPrefs
        String staff_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
        String school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
            new getMySubjects().execute(school_id, staff_id);
        else {
            finish();
            Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);
        }

    }

    private class getMySubjects extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getmysubjects");
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

                for (String row : rows) {
                    NumberMySubjects number = new NumberMySubjects();
                    number.setSubjects(row.split(";")[0]);
                    number.setClasses(row.split(";")[1]);
                    numbers.add(number);
                }

                //set data to adapter
                NumbersMySubjectsAdapter mAdapter = new NumbersMySubjectsAdapter(MySubjects.this, numbers);
                recyclerView.setAdapter(mAdapter);

            } else {
                Tools.toast("No subjects allocated to you.", MySubjects.this, R.color.red_500);
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
        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                w++;
                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        status = 1;
                        if (w > 1)
                            Tools.toast("Back Online! Try again", MySubjects.this, R.color.green_800);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), MySubjects.this, R.color.red_500);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}