package mountedwings.org.mskola_mgt.parent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import mountedwings.org.mskola_mgt.Chat;
import mountedwings.org.mskola_mgt.ChatActivity;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.Settings;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents);
    }

    public void clear_activities(View view) {
        Toast.makeText(getApplicationContext(), "Cleared", Toast.LENGTH_SHORT).show();
    }

    public void chat(View view) {
        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
    }

    public void academics(View view) {
        startActivity(new Intent(getApplicationContext(), ChildsList.class));
    }

    public void settings(View view) {
        startActivity(new Intent(getApplicationContext(), Settings.class));
    }

    public void fees(View view) {
        startActivity(new Intent(getApplicationContext(), Chat.class));
    }

    public void information(View view) {
        startActivity(new Intent(getApplicationContext(), SchoolInformation_menu.class));
    }
}
