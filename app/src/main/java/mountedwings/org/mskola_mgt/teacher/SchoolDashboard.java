package mountedwings.org.mskola_mgt.teacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

import mountedwings.org.mskola_mgt.Home;
import mountedwings.org.mskola_mgt.MskolaLogin;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.SchoolID_Login;
import mountedwings.org.mskola_mgt.Splash;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;


public class SchoolDashboard extends AppCompatActivity {

    private NestedScrollView nested_scroll_view;
    private ImageButton bt_toggle_info_messages1, bt_toggle_info_academics1, bt_toggle_info_cbt1, bt_toggle_info_bursary1, bt_toggle_info_achievements1;
    private View lyt_expand_info_academics, lyt_expand_info_cbt, lyt_expand_info_bursary, lyt_expand_info_messages, lyt_expand_info_achievements;
    private String role, school_id, email;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_dashboard);
        if (getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE).toString() != null) {
            mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);
            email = mPrefs.getString("staff_id", "");
            role = mPrefs.getString("role", "");
            school_id = mPrefs.getString("school_id", "");
        } else {
            Toast.makeText(getApplicationContext(), "Previous Login invalidated. Login again!", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(getApplicationContext(), SchoolID_Login.class).putExtra("account_type", "Teacher"));
        }
        initComponent();
    }

    private void initComponent() {
        nested_scroll_view = findViewById(R.id.nested_scroll_view);

        // info item_academics
        CardView bt_toggle_info_academics = findViewById(R.id.bt_toggle_info_academics);
        bt_toggle_info_academics1 = findViewById(R.id.bt_toggle_info_academics1);
        Button bt_hide_info_academics = findViewById(R.id.bt_hide_info_academics);
        lyt_expand_info_academics = findViewById(R.id.lyt_expand_info_academics);
        bt_toggle_info_academics.setOnClickListener(view -> toggleSectionInfoAcademics(bt_toggle_info_academics1));
        bt_toggle_info_academics1.setOnClickListener(view -> toggleSectionInfoAcademics(bt_toggle_info_academics1));
        bt_hide_info_academics.setOnClickListener(view -> toggleSectionInfoAcademics(bt_toggle_info_academics1));

        // info item_bursary
        CardView bt_toggle_info_bursary = findViewById(R.id.bt_toggle_info_bursary);
        bt_toggle_info_bursary1 = findViewById(R.id.bt_toggle_info_bursary1);
        Button bt_hide_info_bursary = findViewById(R.id.bt_hide_info_bursary);
        lyt_expand_info_bursary = findViewById(R.id.lyt_expand_info_bursary);
        bt_toggle_info_bursary.setOnClickListener(view -> toggleSectionInfoBursary(bt_toggle_info_bursary1));
        bt_toggle_info_bursary1.setOnClickListener(view -> toggleSectionInfoBursary(bt_toggle_info_bursary1));

        bt_hide_info_bursary.setOnClickListener(view -> toggleSectionInfoBursary(bt_toggle_info_bursary1));

        // info item_cbt
        CardView bt_toggle_info_cbt = findViewById(R.id.bt_toggle_info_cbt);
        bt_toggle_info_cbt1 = findViewById(R.id.bt_toggle_info_cbt1);
        Button bt_hide_info_cbt = findViewById(R.id.bt_hide_info_cbt);
        lyt_expand_info_cbt = findViewById(R.id.lyt_expand_info_cbt);
        bt_toggle_info_cbt.setOnClickListener(view -> toggleSectionInfoCBT(bt_toggle_info_cbt1));
        bt_toggle_info_cbt1.setOnClickListener(view -> toggleSectionInfoCBT(bt_toggle_info_cbt1));

        bt_hide_info_cbt.setOnClickListener(view -> toggleSectionInfoCBT(bt_toggle_info_cbt1));

        // info item_achievements
        CardView bt_toggle_info_achievements = findViewById(R.id.bt_toggle_info_achievements);
        bt_toggle_info_achievements1 = findViewById(R.id.bt_toggle_info_achievements1);
        Button bt_hide_info_achievements = findViewById(R.id.bt_hide_info_achievements);
        lyt_expand_info_achievements = findViewById(R.id.lyt_expand_info_achievements);
        bt_toggle_info_achievements.setOnClickListener(view -> toggleSectionInfoAchievements(bt_toggle_info_achievements1));
        bt_toggle_info_achievements1.setOnClickListener(view -> toggleSectionInfoAchievements(bt_toggle_info_achievements1));

        bt_hide_info_achievements.setOnClickListener(view -> toggleSectionInfoAchievements(bt_toggle_info_achievements1));

        // info item_messages
        CardView bt_toggle_info_messages = findViewById(R.id.bt_toggle_info_messages);
        bt_toggle_info_messages1 = findViewById(R.id.bt_toggle_info_messages1);
        Button bt_hide_info_messages = findViewById(R.id.bt_hide_info_messages);
        lyt_expand_info_messages = findViewById(R.id.lyt_expand_info_messages);
        bt_toggle_info_messages.setOnClickListener(view -> toggleSectionInfoMessages(bt_toggle_info_messages1));
        bt_toggle_info_messages1.setOnClickListener(view -> toggleSectionInfoMessages(bt_toggle_info_messages1));

        bt_hide_info_messages.setOnClickListener(view -> toggleSectionInfoMessages(bt_toggle_info_messages1));

    }

    private void toggleSectionInfoAcademics(View view) {

        CardView assessment = findViewById(R.id.assessment);
        assessment.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Assessment_menu.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("role", role);
            intent.putExtra("email_address", email);
            startActivity(intent);
        });
        CardView attendance = findViewById(R.id.attendance);
        attendance.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Attendance_menu.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("role", role);
            intent.putExtra("email_address", email);
            startActivity(intent);
        });
        CardView assignment = findViewById(R.id.assignment);
        assignment.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Assignment_menu.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("role", role);
            intent.putExtra("email_address", email);
            startActivity(intent);
        });

        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info_academics, () -> Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info_academics));
        } else {
            ViewAnimation.collapse(lyt_expand_info_academics);
        }
    }

    private void toggleSectionInfoBursary(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info_bursary, () -> Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info_bursary));
        } else {
            ViewAnimation.collapse(lyt_expand_info_bursary);
        }
    }

    private void toggleSectionInfoCBT(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info_cbt, () -> Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info_cbt));
        } else {
            ViewAnimation.collapse(lyt_expand_info_cbt);
        }
    }

    private void toggleSectionInfoAchievements(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info_achievements, () -> Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info_achievements));
        } else {
            ViewAnimation.collapse(lyt_expand_info_achievements);
        }
    }

    private void toggleSectionInfoMessages(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info_messages, () -> Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info_messages));
        } else {
            ViewAnimation.collapse(lyt_expand_info_messages);
        }
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout(View view) {
        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
        finish();
        clearSharedPreferences(this);
        startActivity(new Intent(getApplicationContext(), Home.class));
    }

    @Override
    public void onBackPressed() {
        doExitApp();
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "Press again to exit app", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                finish(); // call this to finish the current activity
            }
        }
    }

    public static void clearSharedPreferences(Context ctx) {
        File dir = new File(ctx.getFilesDir().getParent() + "/shared_prefs/");
        String[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            // clear each of the prefs
            ctx.getSharedPreferences(children[i].replace(".xml", ""), Context.MODE_PRIVATE).edit().clear().commit();
        }
        // Make sure it has enough time to save all the commited changes
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        for (int i = 0; i < children.length; i++) {
            // delete the files
            new File(dir, children[i]).delete();
        }
    }
}