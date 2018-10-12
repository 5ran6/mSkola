package mountedwings.org.mskola_mgt.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mountedwings.org.mskola_mgt.R;

public class Results_menu extends AppCompatActivity {
    private String school_id = "", staff_id = "", class_name = "", arm = "";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_menu);

        Intent intent = getIntent();
        school_id = intent.getStringExtra("school_id");
        staff_id = intent.getStringExtra("email_address");

    }

    public void psychomotor(View view) {
        intent = new Intent(getApplicationContext(), Give_assignment_menu.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);
    }

    public void compileResults(View view) {
        intent = new Intent(getApplicationContext(), CollateAssignments.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);

    }

    public void promoteStudents(View view) {
        intent = new Intent(getApplicationContext(), AssignmentHistoryActivity.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);
    }
}
