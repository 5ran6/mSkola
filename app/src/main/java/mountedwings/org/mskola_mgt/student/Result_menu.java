package mountedwings.org.mskola_mgt.student;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import java.util.Collections;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Result_menu extends AppCompatActivity {
    private String school_id = "";
    private String session = "";
    private String term = "";
    private String subject = "";
    private String student_reg_no = "";
    private String class_name = "";
    private Spinner select_session;
    private Spinner select_term;
    private ProgressBar progressBar1;
    private int counter = 0;
    private BroadcastReceiver mReceiver;
    private int w = 0;
    private int status;

    @Override
    protected void onResume() {
        super.onResume();
        //        this.mReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                w++;
//                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
//                    @Override
//                    public void onConnectionSuccess() {
//                        status = 1;
//                        if (w > 1)
//                            Tools.toast("Back Online! Try again", Result_menu.this, R.color.green_800);
//                        else
//                            //load classes and assessments
//                            new initialLoad().execute(school_id, parent_id);
//                    }
//
//                    @Override
//                    public void onConnectionFail(String errorMsg) {
//                        status = 0;
//                        Log.d("mSkola", String.valueOf(status));
//                        Tools.toast(getResources().getString(R.string.no_internet_connection), Result_menu.this, R.color.red_600);
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_result_menu);

        //get stuff from sharedPrefs
        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        //school_id/staff id from sharedPrefs
        String parent_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
        school_id = getIntent().getStringExtra("school_id");
        student_reg_no = getIntent().getStringExtra("student_reg_no");
        new initialLoad().execute(school_id, student_reg_no);

        TextView load = findViewById(R.id.load);
        select_term = findViewById(R.id.select_term);
        select_session = findViewById(R.id.select_session);
        Spinner select_subject = findViewById(R.id.subject);
        progressBar1 = findViewById(R.id.progress1);
        progressBar1.setVisibility(View.VISIBLE);

        ProgressBar progressBar2 = findViewById(R.id.progress2);
        progressBar2.setVisibility(View.INVISIBLE);

        LinearLayout progress = findViewById(R.id.lyt_progress);
        //        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
//            new initialLoad().execute(school_id, parent_id);

        select_session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_session.getSelectedItemPosition() >= 0) {
                    session = select_session.getSelectedItem().toString();
                    counter++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        select_term.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_term.getSelectedItemPosition() >= 0) {
                    term = select_term.getSelectedItem().toString();
                    counter++;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        load.setOnClickListener(v -> {
            if (!session.isEmpty() && !term.isEmpty()) {
                // new loadAssessment().execute(school_id, session, term, subject, student_reg_no);
                // if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                //    new loadAssessment().execute(school_id, session, term, subject, student_reg_no);
                Tools.toast(student_reg_no, this);
                startActivity(new Intent(this, ResultActivity.class).putExtra("term", term).putExtra("session", session).putExtra("student_reg_no", student_reg_no).putExtra("school_id", school_id));
                // } else {
                Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);
                //}
            } else {
                Tools.toast("Fill all necessary fields", Result_menu.this, R.color.yellow_700);
            }
        });
    }

    //loads sessions
    private class initialLoad extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getallsessions");
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
            System.out.println(text);

            if (!text.equals("0") && !text.isEmpty()) {
                String dataRows[] = text.split("<>");
                String[] data = new String[(dataRows.length + 1)];
                String[] data1 = {"", "First", "Second", "Third", "Annual"};
                data[0] = "";
                System.arraycopy(dataRows, 0, data, 1, dataRows.length);

                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_session.setAdapter(spinnerAdapter1);
                session = select_session.getSelectedItem().toString();

                ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, data1);
                spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_term.setAdapter(spinnerAdapter2);
                term = select_term.getSelectedItem().toString();

                progressBar1.setVisibility(View.INVISIBLE);
                counter = -1;
            } else {
                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, Collections.emptyList());
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_session.setAdapter(spinnerAdapter1);

                progressBar1.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onPause() {
//        unregisterReceiver(this.mReceiver);
//        w = 0;
        super.onPause();
    }


}
