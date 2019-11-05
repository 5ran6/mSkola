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

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

import mountedwings.org.mskola_mgt.teacher.Dashboard;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
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
    private String role = "Teacher", school_id;
    private static final int PREFRENCE_MODE_PRIVATE = 0;
    private AppCompatCheckBox checkedTextView;
    private String text;
    private LinearLayout parent_layout;
    private LinearLayout lyt_progress;
    private Intent intent;
    private storageFile data;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;
    private AsyncTask lastThread;

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword1()) {
            return;
        }

        emailAddress = emailE.getText().toString().trim();
        password = pass1.getText().toString().trim();
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
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        role = intent.getStringExtra("account_type");
        school_id = intent.getStringExtra("school_id").trim();

        if (getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE).toString() != null) {
            mPrefs = getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE);
            singedIn = mPrefs.getBoolean("signed_in", false);
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

    private void doLogin() {
        if ((EmailValidator.getInstance().isValid(emailE.getText().toString().trim()) || emailE.getText().toString().trim().equalsIgnoreCase("admin")) && status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            lyt_progress = findViewById(R.id.lyt_progress);
            lyt_progress.setVisibility(View.VISIBLE);
            lyt_progress.setAlpha(1.0f);
            parent_layout.setVisibility(View.GONE);
            hideSoftKeyboard();
            lastThread = new login().execute();
        } else
            Tools.toast("Input a valid email address", MskolaLogin.this, R.color.red_500);
    }

    @Override
    protected void onResume() {
        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                w++;
                Log.d("mSkola", "W = " + w);
                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        status = 1;
                        if (w > 1) {
                            try {
                                Tools.toast("Back Online!", MskolaLogin.this, R.color.green_900);
                                lastThread.execute();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), MskolaLogin.this, R.color.red_900);
                        try {
                            lastThread.cancel(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).execute();
            }
        };

        registerReceiver(
                this.mReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(this.mReceiver);
        w = 0;
        Log.d("mSkola", "W = " + w);
        super.onPause();
    }

    private class login extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                do {

                    storageFile storageObj = new storageFile();
                    storageObj.setOperation("requestlogin");
                    // Log.d(TAG, school_id.toLowerCase() + " - " + emailAddress + " - " + password);
                    storageObj.setStrData(emailAddress + "," + password + "," + school_id.toLowerCase());

                    storageFile sentData = new serverProcess().requestProcess(storageObj);

                    //received from server
                    text = sentData.getStrData();
                    boolean isSuccess;

                    Log.d(TAG, text);
                    if (text.contains("success")) {
                        isSuccess = true;
                        role = text.split("<>")[1];
                        //          Log.d(TAG, "registration successful");
                    } else if (text.contains("invalid")) {
                        isSuccess = false;
                        error_from_server = "Email and password mismatch. Try again";
                        //        Log.d(TAG, error_from_server);
                    } else if (text.contains("not found")) {
                        isSuccess = false;
                        error_from_server = "Looks like you don't have an mSkola account. Make sure you register before login";
                        //      Log.d(TAG, error_from_server);
                    } else if (text.contains("no access")) {
                        isSuccess = false;
                        error_from_server = "You do not have access to this school. Contact the schools' admin to verify your account";
                        //    Log.d(TAG, error_from_server);
                    } else {
                        isSuccess = false;
                        error_from_server = "An error occurred!";
                        //  Log.d(TAG, error_from_server);
                    }
                    return isSuccess;

                } while (!isCancelled());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            error_from_server = "Something went wrong. Try again";
            return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if (isSuccess) {
                intent = new Intent(getApplicationContext(), Dashboard.class);
                //sharedPref
                editor = mPrefs.edit();
//                editor.putBoolean("signed_in", true);
                editor.putString("account_type", role);
                editor.putString("staff_id", text.split("<>")[1]);
                editor.putString("email_address", Objects.requireNonNull(emailE.getText()).toString());
                editor.putString("role", role.toLowerCase());
                editor.putBoolean("signed_in", singedIn);
                editor.apply();

                intent.putExtra("email_address", emailE.getText().toString());
                intent.putExtra("role", text.split("<>")[1]);
                emailAddress = emailE.getText().toString().toLowerCase().trim();
                lastThread = new dashboardInfo().execute();

            } else {
                showCustomDialogFailure(error_from_server);
                lyt_progress.setVisibility(View.GONE);

            }
        }
    }

    //DONE
    private class dashboardInfo extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                do {
                    storageFile storageObj = new storageFile();
                    data = storageObj;

                    storageObj.setOperation("getinfoonlogin");
                    storageObj.setStrData(school_id + "<>" + emailAddress);
                    storageFile sentData = new serverProcess().requestProcess(storageObj);
                    data = sentData;
                    return sentData.getStrData();
                } while (!isCancelled());
            } catch (Exception ex) {
                ex.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (!text.equals("0") && !text.equals("")) {
                String[] rows = text.split("<>");
                String school = rows[0];
                String name = rows[2];
//              school = rows[2];
                byte[] pass = {};
                try {
                    data.getImageFiles().get(0);
                } catch (Exception p) {
                    p.printStackTrace();
                }
                //finally and intent
                lyt_progress.setVisibility(View.GONE);

                //sharedPref
                editor = mPrefs.edit();
                editor.putString("name", name);
                editor.putString("school", school);
                editor.putString("email_address", Objects.requireNonNull(emailE.getText()).toString());
                editor.putString("pass", android.util.Base64.encodeToString(pass, android.util.Base64.NO_WRAP));
                editor.apply();

                intent.putExtra("name", name);
                intent.putExtra("school", school);
                intent.putExtra("pass", pass);
                startActivity(intent);

            } else {
                finish();
                Tools.toast("An error occurred", MskolaLogin.this, R.color.red_800);
            }
            finish();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }
}
