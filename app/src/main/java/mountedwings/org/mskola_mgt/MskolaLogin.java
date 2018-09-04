package mountedwings.org.mskola_mgt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import mountedwings.org.mskola_mgt.utils.Tools;


public class MskolaLogin extends AppCompatActivity {

    private View parent_view;
    int keep_signed_in = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mskola);
        parent_view = findViewById(android.R.id.content);

        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);

        ((Button) findViewById(R.id.sig_in)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progress bar

            }
        });
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
}
