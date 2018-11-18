package mountedwings.org.mskola_mgt.teacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

import mountedwings.org.mskola_mgt.Chat_menu;
import mountedwings.org.mskola_mgt.Home;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.SchoolID_Login;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;


public class Dashboard extends AppCompatActivity {


    private String role;
    private String school_id;
    private String email;
    private String school;
    private String name;
    private byte[] pass = new byte[1];
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_dashboard);
        if (getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE).toString() != null) {
            SharedPreferences mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);
            email = mPrefs.getString("staff_id", "");
            role = mPrefs.getString("role", "");
            school_id = mPrefs.getString("school_id", "");

            name = mPrefs.getString("name", getIntent().getStringExtra("name"));
            school = mPrefs.getString("school", getIntent().getStringExtra("school"));
            String raw_pass = mPrefs.getString("pass", Arrays.toString(getIntent().getByteArrayExtra("pass")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pass = raw_pass.getBytes(StandardCharsets.UTF_8);
            } else {
                pass = raw_pass.getBytes();
            }
            Log.i("mSkola1", Arrays.toString(pass));
        } else {
            Toast.makeText(getApplicationContext(), "Previous Login invalidated. Login again!", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(getApplicationContext(), SchoolID_Login.class).putExtra("account_type", "Teacher"));
        }
        initComponent();
    }

    private void initComponent() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle("Dashboard");
        }

        TextView staff_name = findViewById(R.id.tv_staff_name);
        TextView school_name = findViewById(R.id.tv_school_name);
        CircularImageView passport = findViewById(R.id.passport);

        staff_name.setText(name);
        school_name.setText(school);
//        BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//        passport.setImageBitmap(bitmap);
        passport.setImageDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(pass, 0, pass.length)));
        CardView assessment = findViewById(R.id.assessment);
        assessment.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Assessment_menu.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("role", role);
            intent.putExtra("staff_id", email);
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

        CardView result = findViewById(R.id.result);
        result.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Results_menu.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("role", role);
            intent.putExtra("email_address", email);
            startActivity(intent);
        });

        CardView achievements = findViewById(R.id.achievements);
        achievements.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AchievementsActivity.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("role", role);
            intent.putExtra("email_address", email);
            startActivity(intent);
        });
        CardView cbt = findViewById(R.id.cbt);
        cbt.setOnClickListener(v -> {
            ///  Snackbar.make(getActionBar().getCustomView(), "Your school doesn't support CBT", Snackbar.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
            clearSharedPreferences(this);
            finish();
            startActivity(new Intent(getApplicationContext(), Home.class));
        });
        FloatingActionButton messages = findViewById(R.id.message);
        messages.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Chat_menu.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("role", role);
            intent.putExtra("email_address", email);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_SHORT).show();
            clearSharedPreferences(this);
            finish();
            startActivity(new Intent(getApplicationContext(), Home.class));
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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
        ctx.getSharedPreferences("mSkola", 0).edit().clear().apply();
    }
}