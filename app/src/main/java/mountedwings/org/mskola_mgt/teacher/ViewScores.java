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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ViewScores extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(new TableMainLayout(this));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        startActivity(new Intent(getApplicationContext(), View_Scores_menu.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
