package mountedwings.org.mskola_mgt.parent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import mountedwings.org.mskola_mgt.R;

public class Parents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents);
    }

    public void clear_activities(View view) {
        Toast.makeText(getApplicationContext(), "Clear Clicked", Toast.LENGTH_SHORT).show();
    }
}
