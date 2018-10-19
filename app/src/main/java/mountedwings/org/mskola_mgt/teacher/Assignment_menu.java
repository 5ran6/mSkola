package mountedwings.org.mskola_mgt.teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.SchoolID_Login;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Assignment_menu extends AppCompatActivity {
    private String school_id = "", staff_id = "", class_name = "", arm = "";
    private Intent intent;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE).toString() != null) {
            mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);
            staff_id = mPrefs.getString("staff_id", getIntent().getStringExtra("email_address"));
            school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

        } else {
            Toast.makeText(getApplicationContext(), "Previous Login invalidated. Login again!", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(getApplicationContext(), SchoolID_Login.class).putExtra("account_type", "Teacher"));
        }
        setContentView(R.layout.activity_assignment_menu);
        //        Intent intent = getIntent();
//        school_id = intent.getStringExtra("school_id");
//        staff_id = intent.getStringExtra("email_address");

    }

    public void giveAssignments(View view) {
        intent = new Intent(getApplicationContext(), Give_assignment_menu.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);
    }

    public void collateAssignments(View view) {
        intent = new Intent(getApplicationContext(), CollateAssignments.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);

    }

    public void assignmentHistory(View view) {
        intent = new Intent(getApplicationContext(), AssignmentHistoryActivity.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);
    }
}
