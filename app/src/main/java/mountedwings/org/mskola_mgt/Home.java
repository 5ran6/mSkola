package mountedwings.org.mskola_mgt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Objects;

import mountedwings.org.mskola_mgt.parent.Login_SignUp;
import mountedwings.org.mskola_mgt.utils.Tools;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //  initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void parents(View view) {
        startActivity(new Intent(getApplicationContext(), Login_SignUp.class));
    }

    public void students(View view) {
        startActivity(new Intent(getApplicationContext(), mountedwings.org.mskola_mgt.student.Login_SignUp.class));

    }

    public void teachers(View view) {
        startActivity(new Intent(getApplicationContext(), mountedwings.org.mskola_mgt.teacher.Login_SignUp.class));

    }

    @Override
    public void onBackPressed() {
        doExitApp();
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Tools.toast("Press again to exit app", Home.this);
            exitTime = System.currentTimeMillis();
        } else {
            finishAffinity();
        }
    }

}
