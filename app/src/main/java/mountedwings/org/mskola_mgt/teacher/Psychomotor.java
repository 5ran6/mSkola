/*
 * Copyright 2019 Mountedwings Cybersystems LTD. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
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
    private int current_step = 0;

    private ArrayList<NumberPsychomotor> numbers = new ArrayList<>();
    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> skills = new ArrayList<>();
    private ArrayList<String> regNo = new ArrayList<>();
    private ArrayList<String> allSkills_aPerson = new ArrayList<>();
    private ArrayList<byte[]> allPassport_aPerson = new ArrayList<>();
    private ProgressBar progressBar, loading;
    private TextView heading;
    private int no_skills;
    private String school_id;
    private String class_name;
    private String arm;
    private ImageView passport;
    private RecyclerView recyclerView;
    private PsychomotorAdapter mAdapter;
    private String values_to_send = "";
    private String ps_values = "";
    private LinearLayout bottomLayout;

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

        findViewById(R.id.lyt_back).setOnClickListener(view -> backStep(current_step));

        findViewById(R.id.lyt_next).setOnClickListener(view -> nextStep(current_step));

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
                .setPositiveButton("Yes", (dialog, id) -> {
                    new savePsychoSkills().execute(ps_values, values_to_send);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                })
                .show();
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
            return data.getStrData();
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
                Tools.toast("No student found in the selected class", Psychomotor.this);
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
            return data.getStrData();
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
                ps_values = ps_values.substring(1);
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
        Objects.requireNonNull(getSupportActionBar()).setTitle("Psychomotor Skills");
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
            //save the current psycho and close
            new savePsychoSkills().execute(ps_values, values_to_send);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //DONE
    @Override
    public void onBackPressed() {
        areYouSure();
    }

    private void nextStep(int progress) {
        //concat string here
        for (int i = 0; i < no_skills; i++) {
            values_to_send += ";" + mAdapter.getSkill_values_array()[i];
        }
        if (values_to_send.contains("null")) {
            values_to_send = values_to_send.replace("null", "_");
        }
        values_to_send = values_to_send.substring(1);


        if (!mAdapter.isItEmpty())
            new savePsychoSkills().execute(ps_values, values_to_send);
        values_to_send = "";

        if (progress < MAX_STEP) {
            progress++;
            current_step = progress;
            //            ViewAnimation.fadeOutIn(status);
            new loadPsychoValue().execute();
        }

        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        ((TextView) findViewById(R.id.steps)).setText(str_progress);
        progressBar.setProgress(current_step);
        heading.setText(String.format("%s", students.get(current_step)));

        // - Passport
        Bitmap bitmap = BitmapFactory.decodeByteArray(allPassport_aPerson.get(current_step), 0, allPassport_aPerson.get(current_step).length);
        passport.setImageBitmap(bitmap);
    }

    private void backStep(int progress) {
        //concat string here
        for (int i = 0; i < no_skills; i++)
            values_to_send += ";" + mAdapter.getSkill_values_array()[i];
        //     new savePsychoSkills().execute();
        if (values_to_send.contains("null")) {
            values_to_send = values_to_send.replace("null", "_");
        }
        values_to_send = values_to_send.substring(1);
        if (!mAdapter.isItEmpty())
            new savePsychoSkills().execute(ps_values, values_to_send);
        values_to_send = "";

        if (progress > 1) {
            progress--;
            current_step = progress;
            new loadPsychoValue().execute();
        }
        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        ((TextView) findViewById(R.id.steps)).setText(str_progress);
        progressBar.setProgress(current_step);

        heading.setText(String.format("%s", students.get(current_step)));

        // - Passport
        Bitmap bitmap = BitmapFactory.decodeByteArray(allPassport_aPerson.get(current_step), 0, allPassport_aPerson.get(current_step).length);
        passport.setImageBitmap(bitmap);

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
            if (done) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(allPassport_aPerson.get(0), 0, allPassport_aPerson.get(current_step).length);
                passport.setImageBitmap(bitmap);

                String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
                ((TextView) findViewById(R.id.steps)).setText(str_progress);

                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax(MAX_STEP);
                progressBar.setProgress(1);
                progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

                heading = findViewById(R.id.students_name);
                heading.setText(String.format("%s", students.get(0)));


            } else {
                Tools.toast(getResources().getString(R.string.no_internet_connection), Psychomotor.this, R.color.red_500);
            }

            bottomLayout.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            new loadPsychoValue().execute();
        }
    }

    private class savePsychoSkills extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            data = storageObj;
            storageObj.setOperation("savepsychomotorvalues");
            storageObj.setStrData(school_id + "<>" + regNo.get(current_step) + "<>" + strings[0] + "<>" + strings[1]);
            data = new serverProcess().requestProcess(storageObj);
            return data.getStrData();
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
                Tools.toast(text, Psychomotor.this, R.color.green_600);
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
            storageObj.setStrData(school_id + "<>" + regNo.get(current_step));
            data = new serverProcess().requestProcess(storageObj);
            return data.getStrData();
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
                }

            } else {
                for (int i = 0; i < no_skills; i++) {
                    //clearText to edit text on recyclerview
                    NumberPsychomotor numberPsychomotor = new NumberPsychomotor();
                    numberPsychomotor.setskill(skills.get(i));
                    numberPsychomotor.setvalue("");
                    numbers.add(numberPsychomotor);
                }
            }
            mAdapter = new PsychomotorAdapter(numbers);
            recyclerView.setAdapter(mAdapter);

        }
    }
}