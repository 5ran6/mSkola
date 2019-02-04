package mountedwings.org.mskola_mgt;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import email.GMailSender;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class DialogAddReview extends AppCompatActivity {
    private String email;
    private String rating = "";
    private String response = "";
    private ProgressBar progressBar;
    private int color = R.color.blue_400;

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
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText et_post = dialog.findViewById(R.id.et_post);
        final TextView tv_email = dialog.findViewById(R.id.tv_email);
        progressBar = dialog.findViewById(R.id.progress);
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
                if (review.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill a review text", Toast.LENGTH_SHORT).show();
                } else {

                    rating = "From: " + email + "\n" + review + "\n" + rating_bar.getRating() + " star(s) rating";
                    //email to mountedwings customer care
                    try {
                        new DialogAddReview.LongOperation().execute(rating, email);  //sends the email in background
                    } catch (Exception e) {
                        Log.e("mSkola", e.getMessage(), e);
                    }

                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {

//            GMailSender sender = new GMailSender("sender.sendermail.com", "senders password");
//            sender.sendMail("subject",
//                    "body",
//                    "sender.sendermail.com",
//                    "reciepients.recepientmail.com");
//
                GMailSender sender = new GMailSender("5raan6@gmail.com", "franship101");
                sender.sendMail(" mSkola Customer Feedback",
                        params[0], params[1],
                        "5raan6@gmail.com,mountedwingscsltd@gmail.com");

            } catch (Exception e) {
                Log.e("error", e.getMessage(), e);
                return "Email Not Sent";
            }
            return "Email Sent";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("LongOperation", result + "");
            if (result.equalsIgnoreCase("Email Sent")) {
                response = "Submitted. Thanks for your review.";
                color = R.color.green_600;
                Tools.toast(response, DialogAddReview.this, color);
                progressBar.setVisibility(View.GONE);
                finish();
            } else {
                response = "Something went wrong. Try again.";
                color = R.color.red_600;
                progressBar.setVisibility(View.GONE);
            }

        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
