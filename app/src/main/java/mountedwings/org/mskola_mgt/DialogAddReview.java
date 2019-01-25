package mountedwings.org.mskola_mgt;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class DialogAddReview extends AppCompatActivity {
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_layout);
        SharedPreferences mPrefs = getSharedPreferences(myPref, 0);
        email = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));

        showCustomDialog();
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_review);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText et_post = dialog.findViewById(R.id.et_post);
        final TextView tv_email = dialog.findViewById(R.id.tv_email);
        tv_email.setText(email);
        final AppCompatRatingBar rating_bar = dialog.findViewById(R.id.rating_bar);
        dialog.findViewById(R.id.bt_cancel).setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = et_post.getText().toString().trim();
                String rating = "";
                if (review.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill review text", Toast.LENGTH_SHORT).show();
                } else {

                    rating = review + ".\n" + rating_bar.getRating() + " star(s) rating";
                    //TODO: email to mountedwings customer care

                    dialog.dismiss();
                    Tools.toast("Submitted. Thanks for your review.", DialogAddReview.this, R.color.green_600);
                    finish();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
