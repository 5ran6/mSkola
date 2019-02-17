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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mountedwings.org.mskola_mgt.R;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Results_menu extends AppCompatActivity {
    private String school_id = "";
    private String staff_id = "";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mPrefs = getSharedPreferences(myPref, 0);
        String role = mPrefs.getString("role", getIntent().getStringExtra("role"));

        Intent intent = getIntent();
        school_id = intent.getStringExtra("school_id");
        staff_id = intent.getStringExtra("email_address");
        if (role.equalsIgnoreCase("admin"))
            setContentView(R.layout.activity_result_menu);
        else
            setContentView(R.layout.activity_result_menu_not_admin);

    }

    public void psychomotor(View view) {
        intent = new Intent(getBaseContext(), Psychomotor_menu.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);
    }

    public void compileResults(View view) {
        intent = new Intent(getBaseContext(), Compile_Result_menu.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);

    }

    public void promoteStudents(View view) {
        intent = new Intent(getBaseContext(), Promote_Students_menu.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);
    }

    public void viewResults(View view) {
        intent = new Intent(getBaseContext(), ViewResult_menu.class);
        intent.putExtra("school_id", school_id);
        intent.putExtra("email_address", staff_id);
        startActivity(intent);

    }
}
