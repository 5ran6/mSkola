package mountedwings.org.mskola_mgt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mountedwings.org.mskola_mgt.parent.Parents;
import mountedwings.org.mskola_mgt.student.Students;
import mountedwings.org.mskola_mgt.teacher.Record_scores;
import mountedwings.org.mskola_mgt.teacher.Record_scores_Test;
import mountedwings.org.mskola_mgt.teacher.SchoolDashboard;
import mountedwings.org.mskola_mgt.utils.Tools;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Tools.setSystemBarTransparent(this);
    }

    public void testing(View view) {
        startActivity(new Intent(getApplicationContext(), Record_scores_Test.class));
    }
}
