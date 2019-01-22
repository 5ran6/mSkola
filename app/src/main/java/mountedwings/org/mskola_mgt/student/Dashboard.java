package mountedwings.org.mskola_mgt.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import mountedwings.org.mskola_mgt.Chat;
import mountedwings.org.mskola_mgt.ChatActivity;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.parent.SchoolInformation_menu;

public class Dashboard extends AppCompatActivity {

    private String reg_no, school_id, name, class_name, arm, email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        reg_no = getIntent().getStringExtra("student_reg_no");
        school_id = getIntent().getStringExtra("school_id");
        name = getIntent().getStringExtra("name");
        class_name = getIntent().getStringExtra("class_name");
        arm = getIntent().getStringExtra("arm");
        email = getIntent().getStringExtra("email");

    }

    public void clear_activities(View view) {
        Toast.makeText(getApplicationContext(), "Clear clicked", Toast.LENGTH_SHORT).show();
    }

    public void chat(View view) {
        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
    }

    public void academics(View view) {
        startActivity(new Intent(getApplicationContext(), Academics_menu.class).putExtra("student_reg_no", reg_no).putExtra("student_name", name).putExtra("class_name", class_name + " " + arm).putExtra("school_id", school_id).putExtra("email_address", email));
    }

    public void assignment(View view) {
        startActivity(new Intent(getApplicationContext(), Chat.class));
    }

    public void information(View view) {
        startActivity(new Intent(getApplicationContext(), SchoolInformation_menu.class));
    }

}
