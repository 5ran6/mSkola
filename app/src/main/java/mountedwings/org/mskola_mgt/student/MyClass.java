package mountedwings.org.mskola_mgt.student;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

public class MyClass extends AppCompatActivity {
    private TextView name, class_session, others, term, class_members;
    private CircleImageView passport;
    private RecyclerView list;
    private View backdrop;
    private LinearLayout header, bottom;
    private ProgressBar progressBar;
    private boolean rotate = false;
    private View lyt_hols;
    private View lyt_save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_class);
        initComponents();
    }

    private void initComponents() {
        name = findViewById(R.id.tv_student_name);
        others = findViewById(R.id.tv_other_details);
        term = findViewById(R.id.tv_term);
        class_session = findViewById(R.id.class_arm_session);
        passport = findViewById(R.id.passport);
        backdrop = findViewById(R.id.back_drop);
        list = findViewById(R.id.list_of_schools);
        class_members = findViewById(R.id.class_members);
        header = findViewById(R.id.header);
        bottom = findViewById(R.id.bottom);
        progressBar = findViewById(R.id.progress);

        lyt_hols = findViewById(R.id.lyt_subject_teachers);
        lyt_save = findViewById(R.id.lyt_class_teachers);
        ViewAnimation.initShowOut(lyt_hols);
        ViewAnimation.initShowOut(lyt_save);

        //   disappear();
        appear();
    }

    private void disappear() {
        // Disappear
        class_members.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
        header.setVisibility(View.GONE);
        class_members.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);
    }

    private void appear() {
        // Appear
        progressBar.setVisibility(View.GONE);
        class_members.setVisibility(View.VISIBLE);
        list.setVisibility(View.VISIBLE);
        header.setVisibility(View.VISIBLE);
        class_members.setVisibility(View.VISIBLE);
        bottom.setVisibility(View.VISIBLE);
    }

    public void subject_teachers(View view) {

    }

    public void class_teachers(View view) {
    }

    public void show_options(View view) {
        toggleFabMode(view);
    }

    //DONE
    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_hols);
            ViewAnimation.showIn(lyt_save);
            backdrop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_hols);
            ViewAnimation.showOut(lyt_save);
            backdrop.setVisibility(View.GONE);
        }
    }

}