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
        new initialLoad().execute(school_id);
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
                class_arm_session.setText(String.format("Current Class: %s %s \n %s %s", data1[0], data1[1], data1[3], data1[2]));

                //set Logo
                Bitmap bitmap = BitmapFactory.decodeByteArray(student_passport.get(0), 0, student_passport.get(0).length);
                logo.setImageBitmap(bitmap);

                //data2
                String data2[] = rows[1].split("<>");
                //data3
                String data3[] = rows[2].split("<>");
                for (int i = 0; i < data2.length; i++) {
                    NumberStudentsInformation numberStudentsInformation = new NumberStudentsInformation();
                    numberStudentsInformation.setField(data2[i]);
                    numberStudentsInformation.setField(data3[i]);
                    studentsInformation.add(numberStudentsInformation);
                }

                mAdapter = new StudentsInformationAdapter(getApplicationContext(), studentsInformation);
                list.setAdapter(mAdapter);

                //show
                lyt_form.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            } else {
                Tools.toast("Oops! This school has not yrt published her school information.", StudentInformation.this, R.color.red_500, Toast.LENGTH_LONG);
                finish();
            }
        }
    }
}
