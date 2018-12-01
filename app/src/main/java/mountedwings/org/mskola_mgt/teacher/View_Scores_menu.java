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

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.scores;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class View_Scores_menu extends AppCompatActivity {
    private String school_id = "", staff_id = "", class_name = "", arm = "", subject = "";
    private Spinner select_class, select_arm, select_subject;
    private ProgressBar progressBar1, progressBar2, progressBar3, progressBar;
    private int counter = 0;

    private ArrayList<String> NAMES = new ArrayList<>();
    private ArrayList<String> CA1 = new ArrayList<>();
    private ArrayList<String> CA2 = new ArrayList<>();
    private ArrayList<String> CA3 = new ArrayList<>();
    private ArrayList<String> CA4 = new ArrayList<>();
    private ArrayList<String> CA5 = new ArrayList<>();
    private ArrayList<String> CA6 = new ArrayList<>();
    private ArrayList<String> CA7 = new ArrayList<>();
    private ArrayList<String> CA8 = new ArrayList<>();
    private ArrayList<String> CA9 = new ArrayList<>();
    private ArrayList<String> CA10 = new ArrayList<>();
    private ArrayList<String> EXAM = new ArrayList<>();
    private ArrayList<String> TOTAL = new ArrayList<>();
    private ArrayList<String> HEADERS = new ArrayList<>();
    Bundle savedInstance;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scores_menu);

        //get stuff from sharedPrefs
        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        //school_id/staff id from sharedPrefs

        staff_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
        school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));


        TextView load = findViewById(R.id.load);
        select_arm = findViewById(R.id.select_arm);
        select_class = findViewById(R.id.select_class);
        select_subject = findViewById(R.id.subject);

        progressBar = findViewById(R.id.progress);

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
                subject = select_subject.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        load.setOnClickListener(v -> {
            if (!class_name.isEmpty() && !arm.isEmpty() && !subject.isEmpty()) {
                //load the scores
                if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
                    new loadScores().execute(school_id, class_name, arm, subject);
                else
                    Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);

            } else {
                Tools.toast("Fill all necessary fields", this, R.color.yellow_700);
            }
        });
        savedInstance = savedInstanceState;

    }


    private void loadArm() {
        progressBar2.setVisibility(View.VISIBLE);
        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
            new loadArms().execute(school_id, staff_id, class_name);
    }

    private void loadSubject() {
        progressBar3.setVisibility(View.VISIBLE);
        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
            new loadSubject().execute(school_id, staff_id, class_name, arm);
    }

    //loads arms
    private class loadArms extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getrsarm");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);

            return sentData.getStrData();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar2.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (!text.equals("0") && !text.isEmpty()) {

                String[] dataRows = text.split("<>")[0].split(",");
                String[] data = new String[(dataRows.length + 1)];
                data[0] = "";
                System.arraycopy(dataRows, 0, data, 1, dataRows.length);

                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_arm.setAdapter(spinnerAdapter1);
                arm = select_arm.getSelectedItem().toString();
                progressBar2.setVisibility(View.INVISIBLE);
                counter = -1;

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

    private class loadSubject extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getrssubject");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3]);
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
                String[] dataRows = text.split(";");
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

    //loads Classes
    private class initialLoad extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getrsclass");
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
                String dataRows[] = text.split("<>");
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

    /*TODO:WORK
     *
     * This collects the values of all the CA's and switch for different numbers of CA's
     * Then in passes everything to the scores object
     *
     * */
    private class loadScores extends AsyncTask<String, Integer, String> {
        String text;
        String col0 = "", col1 = "", col2 = "", col3 = "", col4 = "", col5 = "", col6 = "", col7 = "", col8 = "", col9 = "";

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getallstudentscore");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.progress);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            progressBar.setVisibility(View.GONE);

            if (!text.equals("0") && !text.isEmpty() && !text.equals("##3") && !text.equals("##9")) {
                scores.setNoCas(Integer.parseInt(text.split("##")[1]) + 2); // + 2 for EXAM aand TOTAL
                int no_cas = Integer.parseInt(text.split("##")[1]);
                text = text.split("##")[0];

                String[] rows = text.split("<>");
                float total = 0;
                String exams = "";

                for (int i = 0; i < rows.length; i++) {
                    exams = rows[i].split(";")[0];
                    total = 0;

                    for (int j = 0; j < no_cas; j++) {
                        switch (j) {
                            case 0:
                                col0 = rows[i].split(";")[1];
                                if (!rows[i].split(";")[1].equals("_")) {
                                    total += Float.parseFloat(rows[i].split(";")[1]);
                                }

                                break;
                            case 1:
                                col1 = rows[i].split(";")[2];
                                if (!rows[i].split(";")[2].equals("_")) {
                                    total += Float.parseFloat(rows[i].split(";")[2]);
                                }
                                break;
                            case 2:
                                col2 = rows[i].split(";")[3];
                                if (!rows[i].split(";")[3].equals("_")) {
                                    total += Float.parseFloat(rows[i].split(";")[3]);
                                }
                                break;
                            case 3:
                                col3 = rows[i].split(";")[4];
                                if (!rows[i].split(";")[4].equals("_")) {
                                    total += Float.parseFloat(rows[i].split(";")[4]);
                                }
                                break;
                            case 4:
                                col4 = rows[i].split(";")[5];
                                if (!rows[i].split(";")[5].equals("_")) {
                                    total += Float.parseFloat(rows[i].split(";")[5]);
                                }
                                break;
                            case 5:
                                col5 = rows[i].split(";")[6];
                                if (!rows[i].split(";")[6].equals("_")) {
                                    total += Float.parseFloat(rows[i].split(";")[6]);
                                }
                                break;
                            case 6:
                                col6 = rows[i].split(";")[7];
                                if (!rows[i].split(";")[7].equals("_")) {
                                    total += Float.parseFloat(rows[i].split(";")[7]);
                                }
                                break;
                            case 7:
                                col7 = rows[i].split(";")[8];
                                if (!rows[i].split(";")[8].equals("_")) {
                                    total += Float.parseFloat(rows[i].split(";")[8]);
                                }
                                break;
                            case 8:
                                col8 = rows[i].split(";")[9];
                                if (!rows[i].split(";")[9].equals("_")) {
                                    total += Float.parseFloat(rows[i].split(";")[9]);
                                }
                                break;
                            case 9:
                                col9 = rows[i].split(";")[10];
                                if (!rows[i].split(";")[10].equals("_")) {
                                    total += Float.parseFloat(rows[i].split(";")[10]);
                                }
                                break;
                        }
                    }


                    if (!exams.equals("_")) {
                        total += Float.parseFloat(exams);
                    }

                    String nameVar = rows[i].split(";")[(rows[i].split(";").length - 1)];
                    NAMES.add(rows[i].split(";")[(rows[i].split(";").length - 1)]);

                    CA1.add(col0);
                    CA2.add(col1);
                    CA3.add(col2);
                    CA4.add(col3);
                    CA5.add(col4);
                    CA6.add(col5);
                    CA7.add(col6);
                    CA8.add(col7);
                    CA9.add(col8);
                    CA10.add(col9);
                    EXAM.add(exams);
                    TOTAL.add(String.valueOf(total));

                }

                switch (no_cas) {
                    case 1:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        break;
                    case 2:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        break;
                    case 3:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        break;
                    case 4:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        break;
                    case 5:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("CA5");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        break;
                    case 6:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("CA5");
                        HEADERS.add("CA6");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        break;
                    case 7:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("CA5");
                        HEADERS.add("CA6");
                        HEADERS.add("CA7");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        break;
                    case 8:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("CA5");
                        HEADERS.add("CA6");
                        HEADERS.add("CA7");
                        HEADERS.add("CA8");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        break;
                    case 9:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("CA5");
                        HEADERS.add("CA6");
                        HEADERS.add("CA7");
                        HEADERS.add("CA8");
                        HEADERS.add("CA9");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        break;
                    case 10:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("CA5");
                        HEADERS.add("CA6");
                        HEADERS.add("CA7");
                        HEADERS.add("CA8");
                        HEADERS.add("CA9");
                        HEADERS.add("CA10");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        break;
                }

                scores.setCA1(CA1);
                scores.setCA2(CA2);
                scores.setCA3(CA3);
                scores.setCA4(CA4);
                scores.setCA5(CA5);
                scores.setCA6(CA6);
                scores.setCA7(CA7);
                scores.setCA8(CA8);
                scores.setCA9(CA9);
                scores.setCA10(CA10);
                scores.setEXAM(EXAM);
                scores.setTOTAL(TOTAL);
                scores.setHEADERS(HEADERS);
                scores.setNAMES(NAMES);
                scores.setNoStudents(NAMES.size());
                startActivity(new Intent(getApplicationContext(), ViewScores.class));
                finish();
            } else {
                Tools.toast("No scores yet for selected class", View_Scores_menu.this);
            }


        }

    }

    @Override
    protected void onPause() {
        w = 0;
        unregisterReceiver(this.mReceiver);
        super.onPause();
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
                            Tools.toast("Back Online! Try again", View_Scores_menu.this, R.color.green_800);
                        else
                            //load classes
                            new initialLoad().execute(school_id, staff_id);


                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), View_Scores_menu.this, R.color.red_500);
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

}
