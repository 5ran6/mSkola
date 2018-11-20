package mountedwings.org.mskola_mgt.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mountedwings.org.mskola_mgt.R;

public class Assessment_first_menu extends AppCompatActivity {
    private String school_id = "", email_address = "";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_first_menu);

        Intent intent = getIntent();
        school_id = intent.getStringExtra("school_id");
        email_address = intent.getStringExtra("email_address");

    }

    public void record_scores(View view) {
        intent = new Intent(getBaseContext(), Assessment_menu.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", email_address);
        startActivity(intent);

    }

    public void view_scores(View view) {
        intent = new Intent(getBaseContext(), ViewScores.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", email_address);
        startActivity(intent);
    }

    public void my_subject(View view) {
        intent = new Intent(getBaseContext(), MySubjects.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", email_address);
        startActivity(intent);
    }
}
