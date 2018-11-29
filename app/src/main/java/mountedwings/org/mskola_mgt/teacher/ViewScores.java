package mountedwings.org.mskola_mgt.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ViewScores extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(new TableMainLayout(this));
//        setContentView(new TableMainLayoutOriginal(this));
        //   scores.setNAMES(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), View_Scores_menu.class));
    }
}
