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

package mountedwings.org.mskola_mgt.student;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import mountedwings.org.mskola_mgt.ChangePassword;
import mountedwings.org.mskola_mgt.ForgotPassword;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class MskolaLogin extends AppCompatActivity {

    int keep_signed_in = 0;
    private boolean singedIn;
    private String error_from_server = "Error";
    private TextInputLayout email, password1;
    private AppCompatEditText emailE, pass1;
    private String emailAddress, password, TAG = "mSkola";
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private String role;
    private static final int PREFRENCE_MODE_PRIVATE = 0;
    private AppCompatCheckBox checkedTextView;
    private String text;
    private LinearLayout parent_layout;
    private LinearLayout lyt_progress;
    private String name, school;
    private byte[] pass;
    private Intent intent;
    private storageFile data;
    private BroadcastReceiver mReceiver;
    private int w = 0, status = 1;

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword1()) {
            return;
        }

        emailAddress = emailE.getText().toString().trim();
        password = pass1.getText().toString();
        doLogin();
    }

    //DONE
    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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

    private void checkKeepState() {
        if (checkedTextView.isChecked()) {
            keep_signed_in = 1;
            singedIn = true;
        } else {
            keep_signed_in = 0;
            singedIn = false;
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
                if (lyt_progress.getVisibility() == View.VISIBLE || parent_layout.getVisibility() == View.GONE) {
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

    public void change(View view) {
        startActivity(new Intent(getApplicationContext(), ChangePassword.class));
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
            storageObj.setStrData(emailAddress + "," + password);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);

            //received from server
            text = sentData.getStrData();
            boolean isSuccess;
            if (text.contains("success")) {
                isSuccess = true;
                Log.d(TAG, "login successful");
            } else if (text.contains("invalid")) {
                isSuccess = false;
                error_from_server = "Email and password mismatch. Try again";
                Log.d(TAG, error_from_server);
            } else if (text.contains("not found")) {
                isSuccess = false;
                error_from_server = "Looks like you don't have an mSkola account. Make sure you register before login";
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

                intent = new Intent(getApplicationContext(), List_of_schools.class);

                //sharedPref
                editor = mPrefs.edit();
                //      editor.putBoolean("signed_in", true);
                editor.putString("account_type", role);
                editor.putString("email_address", emailE.getText().toString());
                editor.putBoolean("signed_in", singedIn);

                editor.apply();

                intent.putExtra("email_address", emailE.getText().toString());

                startActivity(intent);
                finish();
            } else {
                showCustomDialogFailure(error_from_server);
                lyt_progress.setVisibility(View.GONE);
                //clear sharedPref
                clearSharedPreferences(MskolaLogin.this);
            }
        }
    }

    public static void clearSharedPreferences(Context ctx) {
        ctx.getSharedPreferences("mSkola", 0).edit().clear().apply();
    }


    private void doLogin() {
        lyt_progress = findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
        parent_layout.setVisibility(View.GONE);
        hideSoftKeyboard();
        //toolbar.setVisibility(View.GONE);
        new login().execute();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //not necessary when you have implemented logout
//        clearSharedPreferences(MskolaLogin.this);

        Intent intent = getIntent();
        role = intent.getStringExtra("account_type");

        if (getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE).toString() != null) {
            mPrefs = getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE);
            singedIn = mPrefs.getBoolean("signed_in", false);

//            editor = mPrefs.edit();
//            editor.putString("role", role);
//            editor.putString("email_address", emailAddress);
//            editor.apply();
        }

        if (singedIn) {
            //startIntent to next activity
            finish();
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }

        setContentView(R.layout.activity_login_mskola);
        parent_layout = findViewById(R.id.parent_layout);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);
        mPrefs = getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE);

        checkedTextView = findViewById(R.id.keep_signed_in);

        email = findViewById(R.id.email);
        password1 = findViewById(R.id.password1);

        emailE = findViewById(R.id.emailE);
        pass1 = findViewById(R.id.pass1);

        emailE.addTextChangedListener(new MyTextWatcher(emailE));
        pass1.addTextChangedListener(new MyTextWatcher(pass1));

        checkedTextView.setOnCheckedChangeListener((buttonView, isChecked) -> checkKeepState());

        findViewById(R.id.sig_in).setOnClickListener(view -> submitForm());
    }

    @Override
    protected void onResume() {
//        this.mReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                w++;
//                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
//                    @Override
//                    public void onConnectionSuccess() {
//                        status = 1;
//                        if (w > 1)
//                            Tools.toast("Back Online! Try again", MskolaLogin.this, R.color.green_800);
//                    }
//
//                    @Override
//                    public void onConnectionFail(String errorMsg) {
//                        status = 0;
//                        Tools.toast(getResources().getString(R.string.no_internet_connection), MskolaLogin.this, R.color.red_700);
//                    }
//                }).execute();
//            }
//        };
//
//        registerReceiver(
//                this.mReceiver,
//                new IntentFilter(
//                        ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
//        unregisterReceiver(this.mReceiver);
//        w = 0;
        super.onPause();
    }
}
