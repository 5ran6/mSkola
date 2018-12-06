package mountedwings.org.mskola_mgt.teacher;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.PsychomotorAdapter;
import mountedwings.org.mskola_mgt.data.NumberPsychomotor;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.widget.LineItemDecoration;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;


public class Psychomotor extends AppCompatActivity {

    private storageFile data;
    private int MAX_STEP = 20;
    private int current_step = 1;

    private ArrayList<NumberPsychomotor> numbers = new ArrayList<>();
    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> skills = new ArrayList<>();
    private ArrayList<String> regNo = new ArrayList<>();
    private ArrayList<String> allSkills_aPerson = new ArrayList<>();
    private ArrayList<byte[]> allPassport_aPerson = new ArrayList<>();
    private ProgressBar progressBar, loading;
    private TextView heading;
    private int no_skills;
    private String TAG = "mSkola";
    private String school_id;
    private String class_name;
    private String arm;
    private ImageView passport;
    private RecyclerView recyclerView;
    private PsychomotorAdapter mAdapter;
    private String values_to_send = "";
    private String ps_values = "";
    private LinearLayout bottomLayout;
    private boolean isEmpty = true;

    //DONE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychomotor_progress);
        initToolbar();
        initComponent();
    }

    //DONE
    private void initComponent() {
        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));
        //school_id/staff id from sharedPrefs
        school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

        passport = findViewById(R.id.passport);

        loading = findViewById(R.id.loading);
        bottomLayout = findViewById(R.id.bottom_Layout);
        bottomLayout.setVisibility(View.GONE);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        class_name = intent.getStringExtra("class_name");
        arm = intent.getStringExtra("arm");

        new first_loading().execute(school_id, class_name, arm);
        // new first_loading().execute(school_id, "JSS1", "A");

        //students name from server

        findViewById(R.id.lyt_back).setOnClickListener(view -> backStep(current_step));

        findViewById(R.id.lyt_next).setOnClickListener(view -> nextStep(current_step));

//        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        ((TextView) findViewById(R.id.steps)).setText("---/---");
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new LineItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(false);

    }

    //DONE
    private void areYouSure() {
        new AlertDialog.Builder(this)
                .setMessage("Are you done?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, id) ->
                        //TODO : save
                        finish())
                .setNegativeButton("No", (dialog, which) -> {
                })
                .show();
    }

    //DONE
    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    //DONE
    private class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            data = storageObj;

            storageObj.setOperation("getpsstudentinfo");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            data = sentData;
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
                //       Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                allPassport_aPerson = data.getImageFiles();

                String rows[] = text.split("<>");
                MAX_STEP = rows.length;
                for (int i = 0; i < rows.length; i++) {
                    students.add(rows[i].split(";")[1]);
                    regNo.add(text.split("<>")[i].split(";")[0]);
                }
            } else {
                Toast.makeText(getApplicationContext(), "No student found in the selected class", Toast.LENGTH_SHORT).show();
                finish();
            }
            //finally

            new loadPsychoSkills().execute();


        }

    }

    //DONE
    private class loadPsychoSkills extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            data = storageObj;

            storageObj.setOperation("getpsychometry");
            storageObj.setStrData(school_id);
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
                String rows[] = text.split(";");
                no_skills = rows.length;

                for (int i = 0; i < rows.length; i++) {
                    String row = rows[i];
                    NumberPsychomotor number = new NumberPsychomotor();
                    skills.add(row.split(",")[1]);
                    number.setskill(skills.get(i));
                    numbers.add(number);
                    ps_values += ";" + skills.get(i);
                }
                ps_values = ps_values.substring(1, ps_values.length());
                Toast.makeText(getApplicationContext(), ps_values, Toast.LENGTH_SHORT).show();
                //set data and list adapter
                mAdapter = new PsychomotorAdapter(numbers);
                recyclerView.setAdapter(mAdapter);
            } else {
                Tools.toast("No skill-set found", Psychomotor.this);
                finish();
            }
            //finally

            new loadAllPsychoValue().execute(regNo);


        }

    }

    //DONE
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Psychomotor Skills");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //DONE
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    //DONE
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.action_done) {
            //save the current psyco and close
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //DONE
    @Override
    public void onBackPressed() {
        areYouSure();
    }

    private void load() {
        String[] skill_names = new String[no_skills];
        String[] skills_values = new String[no_skills];


        for (int x = 0; x < no_skills; x++) {
            skill_names[x] = skills.get(x);
            if (allSkills_aPerson.get(current_step - 1).split(";")[x].equals("0")) {
                skills_values[x] = " ";
            } else {
                skills_values[x] = allSkills_aPerson.get(current_step - 1).split(";")[x];
            }
            //     Toast.makeText(getApplicationContext(), skills_values[x], Toast.LENGTH_SHORT).show();
        }

        //  skillList customAdapter = new skillList();
        //  listView.setAdapter(customAdapter);
    }

    private void nextStep(int progress) {
        Toast.makeText(getApplicationContext(), "The value = " + Arrays.toString(mAdapter.getSkill_values_array()), Toast.LENGTH_SHORT).show();
        //concat string here
        for (int i = 0; i < no_skills; i++) {
            values_to_send += ";" + mAdapter.getSkill_values_array()[i];
        }
        Toast.makeText(getApplicationContext(), "The string = " + values_to_send, Toast.LENGTH_SHORT).show();
        //     new savePsychoSkills().execute();
        if (values_to_send.contains("null")) {
            values_to_send = values_to_send.replace("null", "_");
        }
        values_to_send = values_to_send.substring(1, values_to_send.length());
        Toast.makeText(getApplicationContext(), "The new string = " + values_to_send, Toast.LENGTH_SHORT).show();
//        int step = current_step;
        Log.d(TAG, String.valueOf(mAdapter.isItEmpty()));
        if (!mAdapter.isItEmpty())
            new savePsychoSkills().execute(ps_values, values_to_send);
        values_to_send = "";
        Toast.makeText(getApplicationContext(), String.valueOf(current_step), Toast.LENGTH_SHORT).show();

        if (progress < MAX_STEP) {
            progress++;
            current_step = progress;
            //            ViewAnimation.fadeOutIn(status);
            new loadPsychoValue().execute();
        }

        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        ((TextView) findViewById(R.id.steps)).setText(str_progress);
        progressBar.setProgress(current_step);

        //LOAD EVERYTHING
        // - Name
        //      ViewAnimation.fadeOutIn(heading);
        heading.setText(String.format("%s", students.get(current_step - 1)));

        // - Passport
        Bitmap bitmap = BitmapFactory.decodeByteArray(allPassport_aPerson.get(current_step - 1), 0, allPassport_aPerson.get(current_step - 1).length);
        passport.setImageBitmap(bitmap);


        //load();
        Toast.makeText(getApplicationContext(), String.valueOf(current_step), Toast.LENGTH_SHORT).show();
    }

    private void backStep(int progress) {
        Toast.makeText(getApplicationContext(), "The value = " + Arrays.toString(mAdapter.getSkill_values_array()), Toast.LENGTH_SHORT).show();
        //concat string here
        for (int i = 0; i < no_skills; i++) {
            values_to_send += ";" + mAdapter.getSkill_values_array()[i];
        }
        Toast.makeText(getApplicationContext(), "The string = " + values_to_send, Toast.LENGTH_SHORT).show();
        //     new savePsychoSkills().execute();
        if (values_to_send.contains("null")) {
            values_to_send = values_to_send.replace("null", "_");
        }
        values_to_send = values_to_send.substring(1, values_to_send.length());
        Toast.makeText(getApplicationContext(), new StringBuilder().append("The new string = ").append(values_to_send).toString(), Toast.LENGTH_SHORT).show();
//        int step = current_step;
        Log.d(TAG, String.valueOf(mAdapter.isItEmpty()));
        if (!mAdapter.isItEmpty())
            new savePsychoSkills().execute(ps_values, values_to_send);
        values_to_send = "";
        Toast.makeText(getApplicationContext(), String.valueOf(current_step), Toast.LENGTH_SHORT).show();

        if (progress > 1) {
            progress--;
            current_step = progress;
            new loadPsychoValue().execute();
        }
        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        ((TextView) findViewById(R.id.steps)).setText(str_progress);
        progressBar.setProgress(current_step);

        //LOAD EVERYTHING
        // - Name
        //      ViewAnimation.fadeOutIn(heading);
        heading.setText(String.format("%s", students.get(current_step - 1)));

        // - Passport
        Bitmap bitmap = BitmapFactory.decodeByteArray(allPassport_aPerson.get(current_step - 1), 0, allPassport_aPerson.get(current_step - 1).length);
        passport.setImageBitmap(bitmap);


        //load();
        Toast.makeText(getApplicationContext(), String.valueOf(current_step), Toast.LENGTH_SHORT).show();

    }

    private class loadAllPsychoValue extends AsyncTask<ArrayList<String>, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(ArrayList... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentpsychomotor");
            for (int i = 0; i < strings[0].size(); i++) {
                storageObj.setStrData(school_id + "<>" + strings[0].get(i));
                storageFile sentData = new serverProcess().requestProcess(storageObj);
                if (sentData.getStrData().isEmpty() || sentData.getStrData().equals("")) {
                    StringBuilder zero = new StringBuilder("0");
                    for (int y = 1; y < no_skills; y++) zero.append(";0");
                    allSkills_aPerson.add(zero.toString());
                } else {
                    allSkills_aPerson.add(sentData.getStrData());
                }
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean done) {
            super.onPostExecute(done);
            //            Log.d(TAG, scores[8]);
            if (done) {
                //ALL DATA COLLECTED..YUPPIE
                Toast.makeText(getApplicationContext(), allSkills_aPerson.toString(), Toast.LENGTH_SHORT).show();
                //TODO: From here

                load();
                Bitmap bitmap = BitmapFactory.decodeByteArray(allPassport_aPerson.get(0), 0, allPassport_aPerson.get(current_step - 1).length);
                passport.setImageBitmap(bitmap);

                String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
                ((TextView) findViewById(R.id.steps)).setText(str_progress);

                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax(MAX_STEP);
                progressBar.setProgress(1);
                progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

                heading = findViewById(R.id.students_name);
                heading.setText(String.format("%s", students.get(0)));


                //Load LIN_LAYS

                //    listView.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }

            bottomLayout.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            //set passport, get name, step, psycho skills names and regNo

            // Names                    - Done
            // List of psycho skill     - Done
            // passport                 - Done
            // regNo                    - Done
            new loadPsychoValue().execute();
        }
    }

    private class savePsychoSkills extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            data = storageObj;
            storageObj.setOperation("savepsychomotorvalues");
            storageObj.setStrData(school_id + "<>" + regNo.get(current_step - 1) + "<>" + strings[0] + "<>" + strings[1]);
            //     storageObj.setStrData(school_id + "<>" + regNo.get(current_step - 1) + "<>" + ps_values + "<>" + values_to_send);
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
            loading.setVisibility(View.GONE);
            if (text.equals("success")) {
                Tools.toast(text, Psychomotor.this);
            } else {
                Tools.toast("Couldn't save previous record. Try again", Psychomotor.this, R.color.red_500);
            }

        }

    }

    private class loadPsychoValue extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            data = storageObj;
            storageObj.setOperation("getstudentpsychomotor");
            storageObj.setStrData(school_id + "<>" + regNo.get(current_step - 1));
            //     storageObj.setStrData(school_id + "<>" + regNo.get(current_step - 1) + "<>" + ps_values + "<>" + values_to_send);
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
            loading.setVisibility(View.GONE);
            numbers.clear();
            if (!text.isEmpty() && !text.equals("0")) {
                String[] rows = text.split(";");
                for (int i = 0; i < no_skills; i++) {
//setText to edit text on recyclerview
                    NumberPsychomotor numberPsychomotor = new NumberPsychomotor();
                    numberPsychomotor.setskill(skills.get(i));
                    numberPsychomotor.setvalue(rows[i]);
                    numbers.add(numberPsychomotor);
                    Log.d(TAG, rows[i]);
                }

            } else {
                for (int i = 0; i < no_skills; i++) {
//clearText to edit text on recyclerview
                    NumberPsychomotor numberPsychomotor = new NumberPsychomotor();
                    numberPsychomotor.setskill(skills.get(i));
                    numberPsychomotor.setvalue("");
                    numbers.add(numberPsychomotor);
                    Log.d(TAG, "empty");
                }
            }
            mAdapter = new PsychomotorAdapter(numbers);
            recyclerView.setAdapter(mAdapter);
        }
    }
}