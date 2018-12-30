package mountedwings.org.mskola_mgt.parent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import mountedwings.org.mskola_mgt.R;

public class SingleChild_menu extends AppCompatActivity {
    private String school_id = "", parent_id = "", student_name = "", class_name = "";
    private Intent intent;
    private TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_child_menu);
        heading = findViewById(R.id.heading);
        Intent intent = getIntent();
        student_name = intent.getStringExtra("student_name");
        school_id = intent.getStringExtra("school_id");
        parent_id = intent.getStringExtra("email_address");
        class_name = intent.getStringExtra("class_name");

        heading.setText(student_name.toUpperCase());
    }

    public void assessment(View view) {
        intent = new Intent(getBaseContext(), AssessmentView.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", parent_id);
        intent.putExtra("class_name", class_name);
        startActivity(intent);

    }

    public void attendance(View view) {
    }

    public void information(View view) {
    }

    public void result(View view) {
        intent = new Intent(getBaseContext(), AssessmentView.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", parent_id);
        intent.putExtra("class_name", class_name);
        startActivity(intent);
    }

    public void timetable(View view) {
    }
}
