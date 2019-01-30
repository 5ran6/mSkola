package mountedwings.org.mskola_mgt.parent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import mountedwings.org.mskola_mgt.About;
import mountedwings.org.mskola_mgt.DialogAddReview;
import mountedwings.org.mskola_mgt.Home;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Settings extends AppCompatActivity {
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);

        TextView email = findViewById(R.id.email);

        if (getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE).toString() != null) {
            SharedPreferences mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);
            email.setText(mPrefs.getString("email_address", getIntent().getStringExtra("email_address")));

            String raw_pass = mPrefs.getString("pass", Arrays.toString(getIntent().getByteArrayExtra("pass")));
            try {
                byte[] pass = Base64.decode(raw_pass, Base64.NO_WRAP);

                CircleImageView passport = findViewById(R.id.passport);

                Bitmap bitmap = BitmapFactory.decodeByteArray(pass, 0, pass.length);
                passport.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Tools.toast("Previous Login invalidated. Login again!", Settings.this, R.color.red_600);
            //clear mPrefs
            clearSharedPreferences(this);
            finish();
            startActivity(new Intent(getApplicationContext(), Home.class));
        }
    }

    //DONE
    public void app_info(View view) {
        startActivity(new Intent(getApplicationContext(), About.class));
    }

    //DONE
    public void privacy_policy(View view) {
        //go to website from browser
        String uri = "http://www.mountedwings.org/mskola_privacy_policy.php";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(uri));
        startActivity(i);
    }

    public void get_help(View view) {
    }

    public void invite(View view) {
        shareText(getResources().getString(R.string.invite_a_friend));
    }

    public void feedback(View view) {
        startActivity(new Intent(getApplicationContext(), DialogAddReview.class));

    }

    public void logout(View view) {
        clearSharedPreferences(Settings.this);
        Tools.toast("Logged out", Settings.this, R.color.green_600);
        finish();
        startActivity(new Intent(getApplicationContext(), Home.class));

    }

    public static void clearSharedPreferences(Context ctx) {
        ctx.getSharedPreferences("mSkola", 0).edit().clear().apply();
    }

    public void shareText(String text) {
        String mimeType = "text/plain";
        String title = "mSkola";

        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(text)
                .getIntent();
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }
}