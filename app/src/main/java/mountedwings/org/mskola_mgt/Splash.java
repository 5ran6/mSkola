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

import mountedwings.org.mskola_mgt.teacher.Dashboard;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Splash extends AppCompatActivity {
    private ImageView img;
    private SharedPreferences.Editor editor;
    private static final int PREFRENCE_MODE_PRIVATE = 0;
    private Boolean singedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE).toString() != null) {
            SharedPreferences mPrefs = getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE);
            singedIn = mPrefs.getBoolean("signed_in", false);
        }
        img = findViewById(R.id.img);
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
            if (singedIn) {
                finish();
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            } else {
                //initial Launch
                finish();
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        }, 4000); // 4000 milliseconds delay
    }
}