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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.AdapterListClassmates;
import mountedwings.org.mskola_mgt.data.NumberClassMates;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;
import static mountedwings.org.mskola_mgt.student.Dashboard.clearSharedPreferences;

public class MyClass extends AppCompatActivity {
    private TextView name, class_session, others, term, class_members;
    private CircleImageView passport;
    private RecyclerView list;
    private View backdrop;
    private String school_id = "", reg_no = "";
    private List<NumberClassMates> schools = new ArrayList<>();
    private LinearLayout header, bottom;
    private ProgressBar progressBar;
    private boolean rotate = false;
    private View lyt_hols;
    private AdapterListClassmates mAdapter;
    private View lyt_save;
    private ArrayList<byte[]> student_passport = new ArrayList<>();
    private ArrayList<byte[]> classmates_passport = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);
        if (getSharedPreferences(myPref, 0).toString() != null) {
            SharedPreferences mPrefs = getSharedPreferences(myPref, 0);
            school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));
            reg_no = mPrefs.getString("student_reg_no", getIntent().getStringExtra("reg_no"));
        } else {
            Tools.toast("Previous Login invalidated. Login again!", this, R.color.teal_300);

            //clear mPrefs
            clearSharedPreferences(this);
            finish();
            startActivity(new Intent(getApplicationContext(), MskolaLogin.class).putExtra("account_type", "Student"));
        }

        initComponents();
    }

    private void initComponents() {
        name = findViewById(R.id.tv_student_name);
        others = findViewById(R.id.tv_other_details);
        term = findViewById(R.id.tv_term);
        class_session = findViewById(R.id.class_arm_session);
        passport = findViewById(R.id.passport);
        backdrop = findViewById(R.id.back_drop);

        list = findViewById(R.id.list_of_schools);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);
        list.setNestedScrollingEnabled(false);

        class_members = findViewById(R.id.class_members);
        header = findViewById(R.id.header);
        bottom = findViewById(R.id.bottom);
        progressBar = findViewById(R.id.progress);

        lyt_hols = findViewById(R.id.lyt_subject_teachers);
        lyt_save = findViewById(R.id.lyt_class_teachers);
        ViewAnimation.initShowOut(lyt_hols);
        ViewAnimation.initShowOut(lyt_save);
        Tools.setSystemBarColor(MyClass.this, R.color.teal_300);
        disappear();
        new initialLoad().execute(school_id, reg_no);
    }

    private void disappear() {
        // Disappear
        class_members.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
        header.setVisibility(View.GONE);
        class_members.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);
    }

    private void appear() {
        // Appear
        progressBar.setVisibility(View.GONE);
        class_members.setVisibility(View.VISIBLE);
        list.setVisibility(View.VISIBLE);
        header.setVisibility(View.VISIBLE);
        class_members.setVisibility(View.VISIBLE);
        bottom.setVisibility(View.VISIBLE);
    }

    public void subject_teachers(View view) {
        startActivity(new Intent(getApplicationContext(), SubjectTeachers.class));
    }

    public void class_teachers(View view) {
        startActivity(new Intent(getApplicationContext(), ClassTeachers.class));
    }

    //DONE
    public void show_options(View view) {
        toggleFabMode(view);
    }

    //DONE
    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_hols);
            ViewAnimation.showIn(lyt_save);
            backdrop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_hols);
            ViewAnimation.showOut(lyt_save);
            backdrop.setVisibility(View.GONE);
        }
    }


    //loads students info
    private class initialLoad extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentinfo");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            student_passport = sentData.getImageFiles();
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            System.out.println(text);

            //TODO: come and check which is term and for others
            if (!text.equals("0") && !text.isEmpty()) {
                String[] rows = text.split("##");
                String student_name = rows[2].split("<>")[2] + " " + rows[2].split("<>")[3];
                name.setText(student_name);

                //data1
                String[] data1 = rows[0].split("<>");
                class_session.setText(String.format("%s\t %s", data1[0], data1[1]));
                String current_term = data1[2];
                term.setText(String.format("%s TERM", current_term.toUpperCase()));

                //set Logo
                Bitmap bitmap = BitmapFactory.decodeByteArray(student_passport.get(0), 0, student_passport.get(0).length);
                passport.setImageBitmap(bitmap);

                //data3
                String[] data3 = rows[2].split("<>");
//                others.setText(String.format("REG NO: %s %s", data3[1].toUpperCase(), data3[5].toUpperCase()));
                others.setText(String.format("REG NO: %s", data3[1].toUpperCase()));


                //runNext API - classmates
                new finalLoad().execute(school_id, reg_no);

            } else {
                Tools.toast("Oops! This information has not yet been published by the school.", MyClass.this, R.color.red_500, Toast.LENGTH_LONG);
                finish();
            }
        }
    }

    //loads your class mates
    private class finalLoad extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getclassmembers");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            classmates_passport = sentData.getImageFiles();
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.i("mSkola", "Class mates info " + text);
            if (!text.equals("0") && !text.isEmpty()) {
                String[] rows = text.split("<>");
                try {
                    for (int i = 0; i < rows.length; i++) {
                        NumberClassMates numberClassMates = new NumberClassMates();
                        numberClassMates.setName(rows[i].split(";")[0]);
                        numberClassMates.setGender(rows[i].split(";")[1]);
                        numberClassMates.setLogo(classmates_passport.get(i));
                        schools.add(numberClassMates);
                    }

                    //set data and list adapter
                    mAdapter = new AdapterListClassmates(MyClass.this, schools);
                    list.setAdapter(mAdapter);

                } catch (Exception e) {
                    //   Tools.toast("Seems something went wrong", MyClass.this, getResources().getColor(R.color.red_600), Toast.LENGTH_LONG);
                    Toast.makeText(getApplicationContext(), "Seems something went wrong", Toast.LENGTH_SHORT).show();
                    finish();
                    e.printStackTrace();
                }

                //appear
                appear();
            } else {
                Tools.toast("Seems something went wrong", MyClass.this, getResources().getColor(R.color.red_600), Toast.LENGTH_LONG);
                finish();
            }
        }
    }
}