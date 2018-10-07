package mountedwings.org.mskola_mgt;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;

import mountedwings.org.mskola_mgt.adapter.NumbersAdapter;
import mountedwings.org.mskola_mgt.data.Number;


public class SchoolID_Login extends AppCompatActivity {
    private TextInputEditText school_id;
    private String role;
    private TextView verifying;
    private ProgressBar checking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        role = intent.getStringExtra("account_type");

        setContentView(R.layout.activity_shool_id_login);
        school_id = findViewById(R.id.school_id);
        verifying = findViewById(R.id.verifying);
        checking = findViewById(R.id.checking);
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

    public void continued(View view) {
        // check if the ID is valid
        verifyID();
    }

    private void verifyID() {
        //check if the field is empty first
        if (!school_id.getText().toString().isEmpty()) {
            //validate from server
            checking.setVisibility(View.VISIBLE);
            //animate textView
            final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
            animation.setDuration(2000); // duration - 2 seconds
            animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
            animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
            verifying.startAnimation(animation);
            new verifySchoolID().execute(school_id.getText().toString().trim());
        } else {
            Toast.makeText(getApplicationContext(), "Fill in School ID", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public class verifySchoolID extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            //  Boolean success = false;
            storageFile storageObj = new storageFile();
            storageObj.setOperation("verifyid");
            storageObj.setStrData(strings[0]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);

            String text = sentData.getStrData();
            Log.d("mSkola", text);

            return text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            checking.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            checking.setVisibility(View.INVISIBLE);
            verifying.setVisibility(View.INVISIBLE);
            if (text.equals("found")) {
                //intent
                Intent intent = new Intent(getApplicationContext(), MskolaLogin.class);
                intent.putExtra("account_type", role);
                intent.putExtra("school_id", school_id.getText().toString().trim());
                startActivity(intent);

            } else if (text.equals("not found")) {
                showCustomDialogFailure("The school ID you provided does not exist, please check the ID and try again.");
            } else {
                showCustomDialogFailure("An error occurred, try again later.");
            }
        }

    }

    private void showCustomDialogFailure(String error) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_error);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TextView error_message = dialog.findViewById(R.id.content);
        error_message.setText(error);

        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}



