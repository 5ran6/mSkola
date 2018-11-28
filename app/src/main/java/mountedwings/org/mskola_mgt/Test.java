package mountedwings.org.mskola_mgt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import mountedwings.org.mskola_mgt.teacher.Dashboard;
import mountedwings.org.mskola_mgt.utils.Tools;

public class Test extends AppCompatActivity {
    private ArrayList<String> NAMES = new ArrayList<>();
    private ArrayList<String> CA1 = new ArrayList<>();
    private ArrayList<String> CA2 = new ArrayList<>();
    private ArrayList<String> CA3 = new ArrayList<>();
    private ArrayList<String> CA4 = new ArrayList<>();
    private ArrayList<String> CA5 = new ArrayList<>();
    private ArrayList<String> CA6 = new ArrayList<>();
    private ArrayList<String> CA7 = new ArrayList<>();
    private ArrayList<String> CA8 = new ArrayList<>();
    private ArrayList<String> CA9 = new ArrayList<>();
    private ArrayList<String> CA10 = new ArrayList<>();
    private ArrayList<String> EXAM = new ArrayList<>();
    private ArrayList<String> TOTAL = new ArrayList<>();
    private ArrayList<String> HEADERS = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Tools.setSystemBarTransparent(this);

    }

    public void testing(View view) {
        startActivity(new Intent(this, Dashboard.class));
    }
}
