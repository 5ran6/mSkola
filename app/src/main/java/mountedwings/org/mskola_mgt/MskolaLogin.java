package mountedwings.org.mskola_mgt;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;


public class MskolaLogin extends AppCompatActivity {

    private View parent_view;
    int keep_signed_in = 0;
    private String error_from_server = "Error";
    private final static int LOADING_DURATION = 3500;
    //    private LinearLayout parent_layout;
    private TextInputLayout email, password1;
    private AppCompatEditText emailE, pass1, pass2;
    private boolean isFilled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mskola);
        parent_view = findViewById(android.R.id.content);
        //      parent_layout = findViewById(R.id.parent_layout);
        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);

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
                if (isFilled) {
                    loadingAndDisplayContent();
                }
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
            //       Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
        } else {
            keep_signed_in = 0;
            //       Toast.makeText(getApplicationContext(), "Not Checked", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCustomDialogFailure() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_error);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TextView error_message = findViewById(R.id.content);
        error_message.setText(error_from_server);

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void loadingAndDisplayContent() {
        //init
        LinearLayout parent_layout = findViewById(R.id.parent_layout);
        final int LOADING_DURATION = 3500;

        final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
        parent_layout.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewAnimation.fadeOut(lyt_progress);
            }
        }, LOADING_DURATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), SchoolDashboard.class));
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

}
