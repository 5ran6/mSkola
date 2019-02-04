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

package mountedwings.org.mskola_mgt.parent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import java.util.ArrayList;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.StudentsInformationAdapter;
import mountedwings.org.mskola_mgt.data.NumberStudentsInformation;
import mountedwings.org.mskola_mgt.utils.Tools;

public class StudentInformation extends AppCompatActivity {

    private TextView class_arm_session;
    private String school_id, student_full_name, reg_no;
    private ImageView logo;
    private ArrayList<byte[]> student_passport = new ArrayList<>();
    private ArrayList<NumberStudentsInformation> studentsInformation = new ArrayList<>();
    private LinearLayout lyt_form;
    private ProgressBar progressBar;
    private RecyclerView list;
    private StudentsInformationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_information);

        list = findViewById(R.id.list);
        school_id = getIntent().getStringExtra("school_id");
        student_full_name = getIntent().getStringExtra("student_name");
        reg_no = getIntent().getStringExtra("reg_no");

        TextView student_name = findViewById(R.id.students_name);
        student_name.setText(student_full_name);
        progressBar = findViewById(R.id.progress);
        logo = findViewById(R.id.logo);
        class_arm_session = findViewById(R.id.class_arm_session);

        lyt_form = findViewById(R.id.lyt_form);
        lyt_form.setVisibility(View.GONE);

        //run API if network
        new initialLoad().execute(school_id, reg_no);
    }

    //loads students info
    private class initialLoad extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentinfo");
            storageObj.setStrData(strings[0] + "<>" + strings[0]);
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
//            System.out.println(text);

            if (!text.equals("0") && !text.isEmpty()) {
                String rows[] = text.split("##");
                //data1
                String data1[] = rows[0].split("<>");
                class_arm_session.setText(String.format("Current Class: %s %s", data1[0], data1[1]));

                //set Logo
                Bitmap bitmap = BitmapFactory.decodeByteArray(student_passport.get(0), 0, student_passport.get(0).length);
                logo.setImageBitmap(bitmap);

                //data2
                String data2[] = rows[1].split("<>");
                //data3
                String data3[] = rows[2].split("<>");
                for (int i = 0; i < data2.length; i++) {
                    NumberStudentsInformation numberStudentsInformation = new NumberStudentsInformation();
                    //TODO: check whats happening here
                    numberStudentsInformation.setField(data2[i]);
                    numberStudentsInformation.setValue(data3[i]);
                    //     numberStudentsInformation.setField(data3[i]);
                    studentsInformation.add(numberStudentsInformation);
                }

                mAdapter = new StudentsInformationAdapter(getApplicationContext(), studentsInformation);
                list.setAdapter(mAdapter);

                //show
                lyt_form.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            } else {
                Tools.toast("Oops! This information has not yet been published by the school.", StudentInformation.this, R.color.red_500, Toast.LENGTH_LONG);
                finish();
            }
        }
    }
}
