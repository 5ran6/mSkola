package mountedwings.org.mskola_mgt.teacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import mountedwings.org.mskola_mgt.R;

public class PromoteStudentsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_students);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
