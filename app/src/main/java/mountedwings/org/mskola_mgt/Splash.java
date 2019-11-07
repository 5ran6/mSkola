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

package mountedwings.org.mskola_mgt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import mountedwings.org.mskola_mgt.teacher.Dashboard;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Splash extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private static final int PREFRENCE_MODE_PRIVATE = 0;
    private Boolean singedIn;
    private String who;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE).toString() != null) {
            SharedPreferences mPrefs = getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE);
            singedIn = mPrefs.getBoolean("signed_in", false);
            who = mPrefs.getString("who", "");
        }
        ImageView img = findViewById(R.id.img);
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        final Transition transition = new Transition() {
            @Override
            public void captureEndValues(@NonNull TransitionValues transitionValues) {

            }

            @Override
            public void captureStartValues(@NonNull TransitionValues transitionValues) {

            }
        };
        animation.setDuration(2000); // duration - 2 seconds
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        img.startAnimation(animation);

        Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            // intent

            //   Toast.makeText(this, String.valueOf(singedIn), Toast.LENGTH_SHORT).show();
            if (singedIn) {
                //     Toast.makeText(Splash.this, "Reached    1 " + who, Toast.LENGTH_LONG).show();

                if (who.equalsIgnoreCase("parent")) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), mountedwings.org.mskola_mgt.parent.Dashboard.class));
                } else if (who.equalsIgnoreCase("teacher")) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                } else if (who.equalsIgnoreCase("student")) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), mountedwings.org.mskola_mgt.student.Dashboard.class));

                } else {
                    finish();
                    startActivity(new Intent(getApplicationContext(), Home.class));

                }
            } else {
                //initial Launch
                Toast.makeText(Splash.this, "Reached    wrong", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        }, 4000); // 4000 milliseconds delay
    }
}