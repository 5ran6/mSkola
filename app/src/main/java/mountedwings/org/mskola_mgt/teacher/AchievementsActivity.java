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
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.NumbersAchievementsAdapter;
import mountedwings.org.mskola_mgt.data.NumberAchievements;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class AchievementsActivity extends AppCompatActivity {
    String school_id, staff_id, TAG = "mSkola";
    String session;

    String class_name;
    String class_;
    String arm;

    String term;
    ArrayList<NumberAchievements> numbers = new ArrayList<>();


    private RecyclerView list;
    private FloatingActionButton fab_done;
    private TextView heading;
    private ArrayList<byte[]> allPassport_aPerson = new ArrayList<>();
    private storageFile data = new storageFile();
    private ArrayList<String> achievements = new ArrayList<>();
    private ArrayList<String> subtitle = new ArrayList<>();


    ProgressBar loading;
    NumbersAchievementsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        Intent intent = getIntent();

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        //school_id/staff id from sharedPrefs

        school_id = mPrefs.getString("school_id", intent.getStringExtra("school_id"));

        fab_done = findViewById(R.id.done);
        heading = findViewById(R.id.achievements_heading);
        heading.setText("School Achievements");

        loading = findViewById(R.id.loading);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(false);

        adapter = new NumbersAchievementsAdapter(numbers);
        list.setAdapter(adapter);

        //hide parentView
        loading.setVisibility(View.VISIBLE);

        new first_loading().execute(school_id);
///////////////////////////////////////////////////////////////////////////////////////////////////////
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

            storageObj.setOperation("getachievements");
            storageObj.setStrData(strings[0]);

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
            if (!text.equals("0") && !text.equals("") && !text.equals("not found")) {
                allPassport_aPerson = data.getImageFiles();
                Log.i(TAG, "Passports = " + String.valueOf(allPassport_aPerson));

                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {
                    achievements.add(rows[i].split(";")[1]);
                    subtitle.add(text.split("<>")[i].split(";")[0]);
                }
            } else {
                Toast.makeText(Objects.requireNonNull(getApplicationContext()), "No student found in the selected class. Compile Result first", Toast.LENGTH_SHORT).show();
                finish();
            }
            //finally
            Toast.makeText(Objects.requireNonNull(getApplicationContext()), "Done", Toast.LENGTH_SHORT).show();
        }
    }
}

