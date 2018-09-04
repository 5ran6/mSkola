package mountedwings.org.mskola_mgt;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mountedwings.org.mskola_mgt.utils.Tools;


public class ForgotPassword extends AppCompatActivity {

    private View parent_view;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_card_light);
        parent_view = findViewById(android.R.id.content);

        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }
}
