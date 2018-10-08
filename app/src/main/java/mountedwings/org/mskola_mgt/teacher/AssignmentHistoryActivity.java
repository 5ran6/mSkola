package mountedwings.org.mskola_mgt.teacher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mountedwings.org.mskola_mgt.R;

public class AssignmentHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_history);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

