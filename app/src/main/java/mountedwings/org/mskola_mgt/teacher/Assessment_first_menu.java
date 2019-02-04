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
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mountedwings.org.mskola_mgt.R;

public class Assessment_first_menu extends AppCompatActivity {
    private String school_id = "", email_address = "";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_first_menu);

        Intent intent = getIntent();
        school_id = intent.getStringExtra("school_id");
        email_address = intent.getStringExtra("email_address");

    }

    public void record_scores(View view) {
        intent = new Intent(getBaseContext(), Assessment_menu.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", email_address);
        startActivity(intent);

    }

    public void view_scores(View view) {
        intent = new Intent(getBaseContext(), View_Scores_menu.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", email_address);
        startActivity(intent);
    }

    public void my_subject(View view) {
        intent = new Intent(getBaseContext(), MySubjects.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", email_address);
        startActivity(intent);
    }
}
