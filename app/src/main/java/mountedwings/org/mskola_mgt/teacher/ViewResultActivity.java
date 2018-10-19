package mountedwings.org.mskola_mgt.teacher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mountedwings.org.mskola_mgt.R;

public class ViewResultActivity extends AppCompatActivity {

    public String arm;

    public String term;


    public String class_;

    public String class_name;

    public String session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);
        arm = getIntent().getStringExtra("arm");
        term = getIntent().getStringExtra("term");
        class_ = getIntent().getStringExtra("class");
        class_name = getIntent().getStringExtra("class_name");
        session = getIntent().getStringExtra("session");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

