package mountedwings.org.mskola_mgt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mountedwings.org.mskola_mgt.teacher.AttendanceActivity;
import mountedwings.org.mskola_mgt.teacher.Compile_Result_menu;
import mountedwings.org.mskola_mgt.teacher.Psychomotor;
import mountedwings.org.mskola_mgt.teacher.Psychomotor_menu;
import mountedwings.org.mskola_mgt.utils.Tools;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Tools.setSystemBarTransparent(this);
    }

    public void testing(View view) {
        startActivity(new Intent(getApplicationContext(), Psychomotor.class));
    }
}
