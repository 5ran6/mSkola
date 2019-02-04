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

package mountedwings.org.mskola_mgt.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import mountedwings.org.mskola_mgt.DialogAddReview;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.AdapterListExpand;
import mountedwings.org.mskola_mgt.data.NumberFAQ;
import mountedwings.org.mskola_mgt.widget.LineItemDecoration;

public class FAQ extends AppCompatActivity {

    private ArrayList<NumberFAQ> numbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_expand);
        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("FAQ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        ArrayAdapter<String> faqUests;
        ArrayAdapter<String> faqAns;
        String[] faqAnswers = getResources().getStringArray(R.array.faq_answers_students);
        String[] faqUestions = getResources().getStringArray(R.array.faq_questions_students);

        faqAns = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, faqAnswers);
        faqUests = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, faqUestions);

        for (int i = 0; i < faqAns.getCount(); i++) {
            NumberFAQ numberFAQ = new NumberFAQ();
            numberFAQ.question = faqUests.getItem(i);
            numberFAQ.answer = faqAns.getItem(i);
            numbers.add(numberFAQ);
        }

        AdapterListExpand mAdapter = new AdapterListExpand(this, numbers);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_get_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.get_help) {
            startActivity(new Intent(getApplicationContext(), DialogAddReview.class));
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
