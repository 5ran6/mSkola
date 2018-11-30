package mountedwings.org.mskola_mgt;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;


public class ForgotPassword extends AppCompatActivity {


    private View parent_view;
    private String email_text, password1_text;
    private TextView done;
    private boolean isSuccess = false;
    private RelativeLayout parent_layout;
    private boolean isFilled = false;
    private TextInputLayout email, password1;
    private AppCompatEditText emailE, pass1;
    private BroadcastReceiver mReceiver;
    private int w = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        parent_view = findViewById(android.R.id.content);
        parent_layout = findViewById(R.id.parent_layout);

        email = findViewById(R.id.email);
        password1 = findViewById(R.id.password1);

        emailE = findViewById(R.id.emailE);
        pass1 = findViewById(R.id.pass1);

        emailE.addTextChangedListener(new MyTextWatcher(emailE));
        pass1.addTextChangedListener(new MyTextWatcher(pass1));

        done = findViewById(R.id.done);
        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
        done.setOnClickListener(v -> {
            email_text = emailE.getText().toString().toLowerCase();
            password1_text = pass1.getText().toString();
            submitForm();
            if (isFilled) {
                isFilled = false;
                //ready for sockets
                loadingAndDisplayContent();
            }
        });
    }

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword1()) {
            return;
        }
        isFilled = true;

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


    private void showCustomDialogSuccess() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> {
            //              Toast.makeText(getApplicationContext(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            finish();
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showCustomDialogFailure() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> {
//                Toast.makeText(getApplicationContext(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            parent_layout.setVisibility(View.VISIBLE);
            done.setVisibility(View.VISIBLE);
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void loadingAndDisplayContent() {
        //init
        final int LOADING_DURATION = 3500;

        final LinearLayout lyt_progress = findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
        parent_layout.setVisibility(View.GONE);
        done.setVisibility(View.GONE);
        new Handler().postDelayed(() -> ViewAnimation.fadeOut(lyt_progress), LOADING_DURATION);

        new Handler().postDelayed(() -> {
            if (isSuccess) {
                showCustomDialogSuccess();
            } else {
                showCustomDialogFailure();
            }
        }, LOADING_DURATION + 400);
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
    protected void onResume() {
        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = NetworkUtil.getConnectivityStatusString(context);
                if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED && w < 1) {
                    Tools.toast("No Internet connection!", ForgotPassword.this, R.color.red_500);
                }
                w++;
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction()) && w > 1) {
                    if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                        Tools.toast("Back Online!", ForgotPassword.this, R.color.green_800);
                    } else {
                        Tools.toast("Offline!", ForgotPassword.this, R.color.red_500);
                    }
                }
            }

        };

        registerReceiver(
                this.mReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

}
