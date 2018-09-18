package mountedwings.org.mskola_mgt.student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import mountedwings.org.mskola_mgt.R;

public class Students extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
    }

    public void clear_activities(View view) {
        Toast.makeText(getApplicationContext(), "Clear clicked", Toast.LENGTH_SHORT).show();
    }
}
