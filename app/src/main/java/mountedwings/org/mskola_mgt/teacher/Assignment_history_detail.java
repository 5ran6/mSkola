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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_history_details);
        Intent intent = getIntent();
        String text = intent.getStringExtra("ass_details");
        String subDueDate = text.split(";")[0];
        String subDueTime = text.split(";")[1];
        String assignment = text.split(";")[2];

        RelativeLayout parent_layout = findViewById(R.id.lyt_parent);
        MaterialRippleLayout done = findViewById(R.id.done);
        TextView done_button = findViewById(R.id.done_button);
        LinearLayout progressBar = findViewById(R.id.lyt_progress);

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

        done_button.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //  startActivity(new Intent(this, AssignmentHistoryActivity.class));
    }
}
