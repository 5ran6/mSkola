package mountedwings.org.mskola_mgt.teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.NumbersViewResultAdapter;
import mountedwings.org.mskola_mgt.data.NumberViewResult;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class ViewResultActivity extends AppCompatActivity {
    String school_id, staff_id, TAG = "mSkola";
    String session;

    String class_name;
    String class_;
    String arm;

    String term;
    ArrayList<NumberViewResult> numbers = new ArrayList<>();


    private RecyclerView list;
    private ArrayList<byte[]> allPassport_aPerson = new ArrayList<>();
    private storageFile data = new storageFile();
    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> regNo = new ArrayList<>();

    private ArrayList<String> total = new ArrayList<>();
    private ArrayList<String> no_subjects = new ArrayList<>();
    private ArrayList<String> position = new ArrayList<>();
    private ArrayList<String> average = new ArrayList<>();

    ProgressBar loading;
    NumbersViewResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);
        Intent intent = getIntent();

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        //school_id/staff id from sharedPrefs

        staff_id = mPrefs.getString("staff_id", intent.getStringExtra("staff_id"));
        school_id = mPrefs.getString("school_id", intent.getStringExtra("school_id"));
        arm = intent.getStringExtra("arm");
        term = intent.getStringExtra("term");
        class_ = intent.getStringExtra("class");
        class_name = intent.getStringExtra("class_name");
        session = intent.getStringExtra("session");

        FloatingActionButton fab_done = findViewById(R.id.done);

        loading = findViewById(R.id.loading);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(false);

        adapter = new NumbersViewResultAdapter(numbers);
        list.setAdapter(adapter);


        //hide parentView
        loading.setVisibility(View.VISIBLE);

        new first_loading().execute(school_id, class_, arm);
        fab_done.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //DONE
    private class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            data = storageObj;

            storageObj.setOperation("getpsstudentinfo");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
            data = new serverProcess().requestProcess(storageObj);
            String text = data.getStrData();
            Log.d(TAG, text);
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
            if (!text.equals("0") && !text.equals("")) {
                allPassport_aPerson = data.getImageFiles();
                Log.i(TAG, "Passports = " + String.valueOf(allPassport_aPerson));

                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {
                    students.add(rows[i].split(";")[1]);
                    regNo.add(text.split("<>")[i].split(";")[0]);
                }
            } else {
                Toast.makeText(Objects.requireNonNull(getApplicationContext()), "No student found in the selected class. Compile Result first", Toast.LENGTH_SHORT).show();
                finish();
            }
            //finally
            Toast.makeText(Objects.requireNonNull(getApplicationContext()), "Stage 1/2 done", Toast.LENGTH_SHORT).show();
            new second_loading().execute();
        }
    }


    public class second_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {

            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentprintresult");
            for (int i = 0; i < regNo.size(); i++) {
                storageObj.setStrData(school_id + "<>" + regNo.get(i) + "<>" + "arm" + "<>" + "yes" + "<>" + session + "<>" + term + "<>" + class_name + "<>" + arm);
                storageFile sentData = new serverProcess().requestProcess(storageObj);

                String text = sentData.getStrData();
                String[] rows = text.split(";");

                Log.d(TAG, Arrays.toString(rows));

                total.add(i, rows[0]);
                average.add(i, rows[1]);
                no_subjects.add(i, rows[2]);
                position.add(i, rows[3]);

                Log.d(TAG, total.get(i));
            }

            return "success";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (text.equals("success")) {
                loading.setVisibility(View.GONE);
                //worked. Split and save into file
                for (int i = 0; i < students.size(); i++) {
                    NumberViewResult number = new NumberViewResult();
                    number.setName(students.get(i));
                    number.settotal(total.get(i));
                    number.setaverage(average.get(i));
                    number.setno_subjects(no_subjects.get(i));
                    number.setPosition(position.get(i));
                    number.setImage(allPassport_aPerson.get(i));
                    numbers.add(number);
                }

                //show recyclerView with inflated views
                adapter = new NumbersViewResultAdapter(numbers);
                list.setAdapter(adapter);

                adapter.setOnItemClickListener((view, obj, position) -> {

                });
            } else {
                Toast.makeText(getApplicationContext(), "No records found", Toast.LENGTH_SHORT).show();
                finish();
            }
            Toast.makeText(getApplicationContext(), "Stage 2/2 done", Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.GONE);
        }
    }


}

