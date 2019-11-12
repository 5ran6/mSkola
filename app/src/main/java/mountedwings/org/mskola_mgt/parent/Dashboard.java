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

package mountedwings.org.mskola_mgt.parent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import mountedwings.org.mskola_mgt.ChatActivity;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.Settings;
import mountedwings.org.mskola_mgt.utils.Tools;

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
        Toast.makeText(getApplicationContext(), "Under Development!", Toast.LENGTH_SHORT).show();

//        startActivity(new Intent(getApplicationContext(), Chat.class));
    }

    public void information(View view) {
        startActivity(new Intent(getApplicationContext(), SchoolInformation_menu.class));
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        doExitApp();
    }

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Tools.toast("Press again to exit app", Dashboard.this);
            exitTime = System.currentTimeMillis();
        } else {
            finishAffinity();
        }
    }

}
