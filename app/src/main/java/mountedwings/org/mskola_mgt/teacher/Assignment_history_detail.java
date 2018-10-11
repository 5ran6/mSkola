package mountedwings.org.mskola_mgt.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import mountedwings.org.mskola_mgt.R;

public class Assignment_history_detail extends AppCompatActivity {
    private RelativeLayout parent_layout;
    private MaterialRippleLayout done;
    private LinearLayout progressBar;
    private TextView done_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_history_details);
        Intent intent = getIntent();
        String text = intent.getStringExtra("ass_details");
        String subDueDate = text.split(";")[0];
        String subDueTime = text.split(";")[1];
        String assignment = text.split(";")[2];

        parent_layout = findViewById(R.id.lyt_parent);
        done = findViewById(R.id.done);
        done_button = findViewById(R.id.done_button);
        progressBar = findViewById(R.id.lyt_progress);

        done.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        parent_layout.setVisibility(View.GONE);

        TextView date = findViewById(R.id.date);
        TextView time = findViewById(R.id.time);
        AppCompatTextView assignment_content = findViewById(R.id.assignmentText);

        date.setText(subDueDate);
        time.setText(subDueTime);
        assignment_content.setText(assignment);

        progressBar.setVisibility(View.GONE);
        parent_layout.setVisibility(View.VISIBLE);
        done.setVisibility(View.VISIBLE);

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
