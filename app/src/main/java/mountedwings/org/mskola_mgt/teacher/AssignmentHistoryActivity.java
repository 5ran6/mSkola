package mountedwings.org.mskola_mgt.teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.NumbersAssHistAdapter;
import mountedwings.org.mskola_mgt.data.NumberAssHist;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class AssignmentHistoryActivity extends AppCompatActivity {
    ArrayList<NumberAssHist> numbers = new ArrayList<>();


    private RecyclerView list;
    private FloatingActionButton fab_done;
    private TextView heading;
    String school_id, staff_id, TAG = "mSkola";

    ProgressBar loading;
    NumbersAssHistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_history);

        int PREFERENCE_MODE_PRIVATE = 0;
        SharedPreferences mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);

        //school_id/staff id from sharedPrefs

        staff_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
        school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

        fab_done = findViewById(R.id.done);
        heading = findViewById(R.id.assignment_history_title);
        heading.setText(R.string.given_ass);
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(false);

        adapter = new NumbersAssHistAdapter(numbers);
        list.setAdapter(adapter);

        //hide parentView
        loading.setVisibility(View.VISIBLE);

        new first_loading().execute(school_id, staff_id);
//        new first_loading().execute("cac180826043520", "admin");
///////////////////////////////////////////////////////////////////////////////////////////////////////
        fab_done.setOnClickListener(v -> finish());


    }

    public class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            //  Boolean success = false;
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getasshistory");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);

            String text = sentData.getStrData();

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
            if (!text.isEmpty() && !text.equals("0")) {
                loading.setVisibility(View.GONE);
                //worked. Split into reg and names
                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {
                    NumberAssHist number = new NumberAssHist();
                    number.setDate(rows[i].split(";")[0]);
                    number.setSubject(rows[i].split(";")[1]);
                    number.setClassArm(rows[i].split(";")[2]);
                    number.setStaff(rows[i].split(";")[3]);
                    number.setAssignment(rows[i].split(";")[4]);
                    numbers.add(number);
                }
                //show recyclerView with inflated views
                adapter = new NumbersAssHistAdapter(numbers);
                list.setAdapter(adapter);

                adapter.setOnItemClickListener(new NumbersAssHistAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, NumberAssHist obj, int position) {
                        String assignmentId = numbers.get(position).getAssignment();
//                        Toast.makeText(getContext(), "Item " + assignmentId + " clicked", Toast.LENGTH_SHORT).show();
                        new loadIndividualAssignment().execute(assignmentId);
                    }
                });

            } else {
                Tools.toast("You haven't given an assignment!", AssignmentHistoryActivity.this);
                finish();
            }
        }
    }

    public class loadIndividualAssignment extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getassdetails");
            storageObj.setStrData(school_id + "<>" + strings[0]);

            storageFile sentData = new serverProcess().requestProcess(storageObj);

            String text = sentData.getStrData();

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
            try {
                if (!text.isEmpty() && !text.equals("0")) {
                    startActivity(new Intent(getApplicationContext(), Assignment_history_detail.class).putExtra("ass_details", text));
                }

            } catch (Exception e) {

            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

