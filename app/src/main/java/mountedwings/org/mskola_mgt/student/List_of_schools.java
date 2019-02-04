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

package mountedwings.org.mskola_mgt.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.AdapterListSchoolsStudents;
import mountedwings.org.mskola_mgt.data.NumberSchoolStudents;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class List_of_schools extends AppCompatActivity {
    private RecyclerView list;
    private ProgressBar progressBar;
    private String email;
    private List<NumberSchoolStudents> schools = new ArrayList<>();
    private AdapterListSchoolsStudents mAdapter;
    private ArrayList<byte[]> logos = new ArrayList<>();
    private SharedPreferences.Editor editor;
    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_schools);
        initToolbar();
        initComponent();
    }

    private void initComponent() {
        mPrefs = getSharedPreferences(myPref, 0);

        email = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));

        progressBar = findViewById(R.id.progress);
        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);
        list.setNestedScrollingEnabled(false);

        list.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        new initialLoad().execute(email);
    }

    private void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Your School(s)");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //loads your schools
    private class initialLoad extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentschools");
            storageObj.setStrData(strings[0]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            logos = sentData.getImageFiles();
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.i("mSkola", text);

            if (!text.equals("0") && !text.isEmpty()) {
                list.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);


                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {
                    NumberSchoolStudents numberSchool = new NumberSchoolStudents();
                    numberSchool.setSchool_id(rows[i].split(";")[0]);
                    numberSchool.setSchool_name(rows[i].split(";")[1]);
                    numberSchool.setSchool_address(rows[i].split(";")[2]);
                    numberSchool.setStudent_reg_no(rows[i].split(";")[3]);
                    numberSchool.setLogo(logos.get(i));
                    schools.add(numberSchool);
                }
                //set data and list adapter
                mAdapter = new AdapterListSchoolsStudents(List_of_schools.this, schools);
                list.setAdapter(mAdapter);


                // on item list clicked
                mAdapter.setOnItemClickListener((view, obj, position) -> {
                    //run final API
                    logos.clear();
                    editor = mPrefs.edit();
                    editor.putString("email_address", email);
                    editor.putString("school_id", obj.getSchool_id());
                    editor.putString("school", obj.getSchool_name());
                    editor.putString("student_reg_no", obj.getStudent_reg_no());
                    editor.apply();

                    new finalLoad().execute(obj.getSchool_id().trim(), obj.getStudent_reg_no().trim());
                    //disappear

                });

            } else {
                Tools.toast("Oops. Seems this mSkola account isn't linked with any school. Contact your school Administrator.", List_of_schools.this, getResources().getColor(R.color.red_600), Toast.LENGTH_LONG);
                finish();
                startActivity(new Intent(getApplicationContext(), Login_SignUp.class));

            }
        }
    }

    //loads your schools
    private class finalLoad extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentinfo");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            logos = sentData.getImageFiles();
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
//            Log.i("mSkola", "Student info " + text);

            if (!text.equals("0") && !text.isEmpty()) {
                String rows[] = text.split("##");

                String student_name = rows[2].split("<>")[2] + " " + rows[2].split("<>")[3];
                //      String student_name = "";
                //save to shared prefs first
                editor = mPrefs.edit();
                byte[] pass = logos.get(0);
                editor.putString("student_name", student_name);
                editor.putString("pass", android.util.Base64.encodeToString(pass, android.util.Base64.NO_WRAP));

                editor.apply();

                startActivity(new Intent(getApplicationContext(), Dashboard.class).putExtra("student_name", student_name));
                finish();
//                list.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);

            } else {
                Tools.toast("Oops. Seems this mSkola account isn't linked with any school. Contact your school Administrator.", List_of_schools.this, getResources().getColor(R.color.red_600), Toast.LENGTH_LONG);
                finish();
                startActivity(new Intent(getApplicationContext(), Login_SignUp.class));

            }
        }
    }

}
