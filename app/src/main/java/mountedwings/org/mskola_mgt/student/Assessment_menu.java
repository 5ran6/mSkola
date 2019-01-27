package mountedwings.org.mskola_mgt.student;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import java.util.Collections;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Assessment_menu extends AppCompatActivity {
    private String school_id = "", parent_id = "", session = "", term = "", subject = "", student_reg_no = "";
    private RelativeLayout parent;
    private LinearLayout progress;
    private Spinner select_session, select_term, select_subject;
    private ProgressBar progressBar1;
    private ProgressBar progressBar3;
    private int counter = 0;
    private BroadcastReceiver mReceiver;
    private int w = 0;
    private int status;
    private MaterialRippleLayout loading;

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
//                        status = 1;
//                        if (w > 1)
//                            Tools.toast("Back Online! Try again", Assessment_menu.this, R.color.green_800);
//                        else
                        //                    //load classes and assessments
                        //                     new initialLoad().execute(school_id);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Log.d("mSkola", String.valueOf(status));
                        Tools.toast(getResources().getString(R.string.no_internet_connection), Assessment_menu.this, R.color.red_600);
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
        setContentView(R.layout.activity_parent_assessment_menu);

        //get stuff from sharedPrefs
        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        //school_id/staff id from sharedPrefs
        parent_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
        //school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));
        school_id = getIntent().getStringExtra("school_id");
        student_reg_no = getIntent().getStringExtra("student_reg_no");


        TextView load = findViewById(R.id.load);
        select_term = findViewById(R.id.select_term);
        select_session = findViewById(R.id.select_session);
        select_subject = findViewById(R.id.select_subject);
        progressBar1 = findViewById(R.id.progress1);
        progressBar1.setVisibility(View.VISIBLE);

        ProgressBar progressBar2 = findViewById(R.id.progress2);
        progressBar2.setVisibility(View.INVISIBLE);

        progressBar3 = findViewById(R.id.progress3);
        progressBar3.setVisibility(View.INVISIBLE);

        progress = findViewById(R.id.lyt_progress);
        parent = findViewById(R.id.parent_layout);
        loading = findViewById(R.id.loadButton);
        //        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
//            new initialLoad().execute(school_id, parent_id);

        new initialLoad().execute(school_id);


        select_session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_session.getSelectedItemPosition() >= 0) {
                    session = select_session.getSelectedItem().toString();
                    counter++;
                    //run new thread
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        select_term.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_term.getSelectedItemPosition() >= 1) {
                    term = select_term.getSelectedItem().toString();
                    counter++;
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
                if (select_session.getSelectedItemPosition() >= 0)
                    subject = select_subject.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        load.setOnClickListener(v -> {
            if (!session.isEmpty() && !term.isEmpty() && !subject.isEmpty()) {
                new loadAssessment().execute(school_id, session, term, subject, student_reg_no);
                if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                    //    new loadAssessment().execute(school_id, session, term, subject, student_reg_no);
                } else {
                    Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);
                }
            } else {
                Tools.toast("Fill all necessary fields", Assessment_menu.this, R.color.yellow_700);
            }
        });
    }

    private void loadSubject() {
        progressBar3.setVisibility(View.VISIBLE);
        //     if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
        new loadSubject().execute(school_id, session, term, student_reg_no);
    }

    private class loadAssessment extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentassessment");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + strings[4]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //let all the other views fade away #5ran6Sings
            parent.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            //till there's only you #5ran6Sings
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            //text = Exams;CA1;C2;CA3........;STUDENT NAME##NO OF CAs
            if (!text.equals("0") && !text.isEmpty()) {
                Intent intent1 = new Intent(getBaseContext(), AssessmentView.class);
                intent1.putExtra("session", session);
                intent1.putExtra("term", term);
                intent1.putExtra("subject", subject);
                intent1.putExtra("text", text);
                startActivity(intent1);
            } else {
                //let all the other views fade away #5ran6Sings
                progress.setVisibility(View.GONE);
                //till there's only you #5ran6Sings
                parent.setVisibility(View.VISIBLE);
                loading.setVisibility(View.VISIBLE);
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
            storageObj.setOperation("getstudentsubjects");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3]);
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

            if (!text.equals("0") && !text.isEmpty()) {
                String[] dataRows = text.split("<>");
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
                String[] data1 = {"", "First", "Second", "Third"};
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
                progressBar3.setVisibility(View.INVISIBLE);
                counter = -1;
            } else {
                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, Collections.emptyList());
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_session.setAdapter(spinnerAdapter1);

                progressBar1.setVisibility(View.INVISIBLE);
                //display error message

            }
        }
    }

    @Override
    protected void onPause() {
        finish();
        unregisterReceiver(this.mReceiver);
        w = 0;
        super.onPause();
    }


}
