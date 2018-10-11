package mountedwings.org.mskola_mgt;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.io.ByteArrayOutputStream;

import mountedwings.org.mskola_mgt.teacher.SchoolDashboard;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkBroadcastReceiver;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;


public class MskolaLogin extends AppCompatActivity {

    private View parent_view;
    int keep_signed_in = 0;
    private boolean singedIn, isSuccess;
    private String error_from_server = "Error";
    //    private LinearLayout parent_layout;
    private TextInputLayout email, password1;
    private AppCompatEditText emailE, pass1;
    private String emailAddress, password, TAG = "mSkola";
    private boolean isFilled = false;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private String role, school_id;
    private static final int PREFRENCE_MODE_PRIVATE = 0;
    private String text;
    private LinearLayout parent_layout;
    private LinearLayout lyt_progress;

//
//    NetworkBroadcastReceiver networkBroadcastReceiver = new NetworkBroadcastReceiver();
//
//    public NetworkBroadcastReceiver getNetworkBroadcastReceiver() {
//        return networkBroadcastReceiver;
//    }
//

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword1()) {
            return;
        }
        isFilled = true;

        emailAddress = emailE.getText().toString().trim();
        password = pass1.getText().toString();
        doLogin();
    }

    private boolean validateEmail() {
        if (emailE.getText().toString().trim().isEmpty()) {
            email.setError(getString(R.string.err_msg_email));
            requestFocus(emailE);
            return false;
        } else {
            email.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword1() {
        if (pass1.getText().toString().trim().isEmpty()) {
            password1.setError(getString(R.string.err_msg_pass1));
            requestFocus(pass1);
            return false;
        } else {
            password1.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void forgot(View view) {
        startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
    }

    public void keep_signed_in(View view) {
        checkKeepState();
    }

    private void checkKeepState() {
        CheckBox checkedTextView = findViewById(R.id.keep_signed_in);
        if (checkedTextView.isChecked()) {
            keep_signed_in = 1;
            singedIn = true;
            editor = mPrefs.edit();
            editor.putBoolean("signed_in", singedIn);
            editor.apply();
        } else {
            keep_signed_in = 0;
            singedIn = false;
            editor = mPrefs.edit();
            editor.putBoolean("signed_in", singedIn);
            editor.apply();
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

        (dialog.findViewById(R.id.bt_close)).setOnClickListener(v -> {
            dialog.dismiss();
            try {
                if (lyt_progress.getVisibility() == View.VISIBLE && parent_layout.getVisibility() == View.INVISIBLE) {
                    lyt_progress.setVisibility(View.INVISIBLE);
                    parent_layout.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.pass1:
                    validatePassword1();
                    break;
            }
        }
    }

    private class login extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("requestlogin");
            storageObj.setStrData(emailAddress + "," + password + "," + school_id);
            storageFile sentData = new serverProcess().requestProcess(storageObj);

            //received from server
            text = sentData.getStrData();
            if (text.contains("success")) {
                isSuccess = true;
                Log.d(TAG, "registration successful");
            } else if (text.contains("invalid")) {
                isSuccess = false;
                error_from_server = "Email and password mismatch. Try again";
                Log.d(TAG, error_from_server);
            } else if (text.contains("not found")) {
                isSuccess = false;
                error_from_server = "Looks like you don't have an mSkola account. Make sure you register before login";
                Log.d(TAG, error_from_server);
            } else if (text.contains("no access")) {
                isSuccess = false;
                error_from_server = "You do not have access to this school. Contact the schools' admin to verify your account";
                Log.d(TAG, error_from_server);
            } else {
                isSuccess = false;
                error_from_server = "An error occurred!";
                Log.d(TAG, error_from_server);
            }
            return isSuccess;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if (isSuccess) {
                Intent intent = new Intent(getApplicationContext(), SchoolDashboard.class);

                //sharedPref
                editor = mPrefs.edit();
                editor.putBoolean("signed_in", true);
                editor.putString("account_type", role);
                editor.putString("staff_id", text.split("<>")[1]);
                editor.apply();

                intent.putExtra("email_address", emailAddress);
                intent.putExtra("school_role", text.split("<>")[1]);
                startActivity(intent);
                finish();
            } else {
                showCustomDialogFailure(error_from_server);
            }
        }
    }

    private void doLogin() {
        lyt_progress = findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
        parent_layout.setVisibility(View.GONE);
        //toolbar.setVisibility(View.GONE);
        new login().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        role = intent.getStringExtra("account_type");
        school_id = intent.getStringExtra("school_id");

        if (getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE).toString() != null) {
            mPrefs = getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE);
            singedIn = mPrefs.getBoolean("signed_in", false);

            editor = mPrefs.edit();
            editor.putString("role", role);
            editor.putString("school_id", school_id);
            editor.apply();

        }

        if (singedIn) {
            //startIntent to next activity
            finish();
            startActivity(new Intent(getApplicationContext(), SchoolDashboard.class));
        }

        setContentView(R.layout.activity_login_mskola);
        parent_layout = findViewById(R.id.parent_layout);
        parent_view = findViewById(android.R.id.content);
        //      parent_layout = findViewById(R.id.parent_layout);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
        mPrefs = getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE);

        email = findViewById(R.id.email);
        password1 = findViewById(R.id.password1);

        emailE = findViewById(R.id.emailE);
        pass1 = findViewById(R.id.pass1);

        emailE.addTextChangedListener(new MyTextWatcher(emailE));
        pass1.addTextChangedListener(new MyTextWatcher(pass1));


        ((Button) findViewById(R.id.sig_in)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
//                if (isFilled) {
//                    loadingAndDisplayContent();
//                }
            }
        });
    }
}
