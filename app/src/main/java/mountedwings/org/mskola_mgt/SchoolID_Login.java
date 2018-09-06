package mountedwings.org.mskola_mgt;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class SchoolID_Login extends AppCompatActivity {
    private TextInputEditText school_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shool_id_login);
        school_id = findViewById(R.id.school_id);
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
            startActivity(new Intent(getApplicationContext(), MskolaLogin.class));

        } else {
            Toast.makeText(getApplicationContext(), "Fill in School ID", Toast.LENGTH_SHORT).show();
        }

    }
}
