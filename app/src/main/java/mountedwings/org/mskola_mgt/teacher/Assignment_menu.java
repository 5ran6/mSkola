package mountedwings.org.mskola_mgt.teacher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.SchoolID_Login;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Assignment_menu extends AppCompatActivity {
    private String school_id = "", staff_id = "";
    private Intent intent;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE).toString() != null) {
            SharedPreferences mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);
            staff_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
            school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

        } else {
            Tools.toast("Previous Login invalidated. Login again!", this, R.color.yellow_700);

            finish();
            startActivity(new Intent(getApplicationContext(), SchoolID_Login.class).putExtra("account_type", "Teacher"));
        }
        setContentView(R.layout.activity_assignment_menu);
        //        Intent intent = getIntent();
//        school_id = intent.getStringExtra("school_id");
//        staff_id = intent.getStringExtra("email_address");

    }

    public void giveAssignments(View view) {
        intent = new Intent(getApplicationContext(), Give_assignment_menu.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);
    }

    public void collateAssignments(View view) {
        intent = new Intent(getApplicationContext(), CollateAssignments.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);

    }

    public void assignmentHistory(View view) {
        intent = new Intent(getApplicationContext(), AssignmentHistoryActivity.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                w++;
                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        status = 1;
                        if (w > 1)
                            Tools.toast("Back Online! Try again", Assignment_menu.this, R.color.green_800);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), Assignment_menu.this, R.color.red_500);
                    }
                }).execute();
            }

        };

        registerReceiver(
                this.mReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(this.mReceiver);
        w = 0;
    }
}
