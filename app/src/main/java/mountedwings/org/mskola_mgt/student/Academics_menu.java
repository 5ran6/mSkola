package mountedwings.org.mskola_mgt.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import mountedwings.org.mskola_mgt.R;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Academics_menu extends AppCompatActivity {
    private String school_id = "", parent_id = "", student_name = "", class_name = "";
    private Intent intent;
    private String student_reg_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_child_menu);
        //get stuff from sharedPrefs
        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        TextView heading = findViewById(R.id.heading);
        Intent intent = getIntent();
        student_name = intent.getStringExtra("student_name");

        //school_id
        // parent_id from sharedPrefs
        parent_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
        school_id = getIntent().getStringExtra("school_id");
        // arm = getIntent().getStringExtra("arm");

        class_name = intent.getStringExtra("class_name");
        student_reg_no = getIntent().getStringExtra("student_reg_no");

        heading.setText(student_name.toUpperCase());
    }

    //DONE: YET TO TEST
    public void assessment(View view) {
        intent = new Intent(getBaseContext(), mountedwings.org.mskola_mgt.student.Assessment_menu.class);
        intent.putExtra("class_name", class_name);
        intent.putExtra("student_reg_no", student_reg_no);
        intent.putExtra("email_address", parent_id);
        intent.putExtra("school_id", school_id);
        startActivity(intent);
    }

    //DONE: YET TO TEST
    //THE ATTENDANCE MENU WILL HAVE A BOTTOM SHEET. THAT'S WHERE WE SHALL DISPLAY THE ATTENDANCE
    public void attendance(View view) {
        intent = new Intent(getBaseContext(), mountedwings.org.mskola_mgt.student.Attendance_menu_activity.class);
        intent.putExtra("reg_no", student_reg_no);
        intent.putExtra("school_id", school_id);
        startActivity(intent);
    }

    //DONE: YET TO TEST
    public void information(View view) {
        intent = new Intent(getBaseContext(), mountedwings.org.mskola_mgt.student.StudentInformation.class);
        intent.putExtra("email_address", parent_id);
        intent.putExtra("class_name", class_name);
        intent.putExtra("reg_no", student_reg_no);
        intent.putExtra("school_id", school_id);
        intent.putExtra("student_name", student_name);

        startActivity(intent);

    }

    //DONE: YET TO TEST
    public void result(View view) {
        intent = new Intent(getBaseContext(), mountedwings.org.mskola_mgt.student.Result_menu.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", parent_id);
        intent.putExtra("class_name", class_name);
        intent.putExtra("student_reg_no", student_reg_no);
        startActivity(intent);
    }

    //DONE: YET TO TEST
    public void timetable(View view) {
        intent = new Intent(getBaseContext(), mountedwings.org.mskola_mgt.student.Timetable_menu_activity.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", parent_id);
        intent.putExtra("class_name", class_name);
        intent.putExtra("reg_no", student_reg_no);
        startActivity(intent);
    }
}