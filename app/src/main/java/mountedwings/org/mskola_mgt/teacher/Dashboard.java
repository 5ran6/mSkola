/*
 * Copyright 2019 Mountedwings Cybersystems LTD. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mountedwings.org.mskola_mgt.teacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.Arrays;

import mountedwings.org.mskola_mgt.Chat_menu;
import mountedwings.org.mskola_mgt.Home;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.SchoolID_Login;
import mountedwings.org.mskola_mgt.Settings;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;


public class Dashboard extends AppCompatActivity {
    private String role = "";
    private String school_id;
    private String email;
    private String school = "";
    private String name;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_dashboard);
        if (getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE).toString() != null) {

            SharedPreferences mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);
            email = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
            role = mPrefs.getString("role", getIntent().getStringExtra("role"));
            school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

            name = mPrefs.getString("name", getIntent().getStringExtra("name"));
            school = mPrefs.getString("school", getIntent().getStringExtra("school"));

            String raw_pass = mPrefs.getString("pass", Arrays.toString(getIntent().getByteArrayExtra("pass")));
            try {
                byte[] pass = Base64.decode(raw_pass, Base64.NO_WRAP);

                CircularImageView passport = findViewById(R.id.passport);

                Bitmap bitmap = BitmapFactory.decodeByteArray(pass, 0, pass.length);
                passport.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Tools.toast("Previous Login invalidated. Login again!", Dashboard.this, R.color.red_600);

            //clear mPrefs
            clearSharedPreferences(this);
            finish();
            startActivity(new Intent(getApplicationContext(), SchoolID_Login.class).putExtra("account_type", "Teacher"));
        }
        initComponent();
    }

    private void initComponent() {
        if (school == null) {
            Tools.toast("Previous Login invalidated. Login again!", Dashboard.this, R.color.red_600);
            //clear mPrefs
            clearSharedPreferences(this);
            finish();
            startActivity(new Intent(getApplicationContext(), SchoolID_Login.class).putExtra("account_type", "Teacher"));
        }

        TextView staff_name = findViewById(R.id.tv_staff_name);
        TextView school_name = findViewById(R.id.tv_school_name);
        staff_name.setText(name);
        school_name.setText(school);

        CardView assessment = findViewById(R.id.assessment);
        assessment.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Assessment_first_menu.class);
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
//        cbt.setOnClickListener(v -> Snackbar.make(parent_view, "Your school doesn't support CBT", Snackbar.LENGTH_SHORT).show());
        cbt.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            intent.putExtra("school_id", school_id);
            intent.putExtra("role", role);
            intent.putExtra("email_address", email);
            startActivity(intent);
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
    public void onBackPressed() {
        doExitApp();
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Tools.toast("Press again to exit app", Dashboard.this);
            exitTime = System.currentTimeMillis();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            } else {
                finish(); // call this to finish the current activity and hopefully close the app for older devices
            }
        }
    }

    public static void clearSharedPreferences(Context ctx) {
        ctx.getSharedPreferences("mSkola", 0).edit().clear().apply();
    }

    public void doLogout(View view) {
        clearSharedPreferences(this);
        Tools.toast("Logged out", Dashboard.this, R.color.green_600);
        finish();
        startActivity(new Intent(getApplicationContext(), Home.class));

    }
}