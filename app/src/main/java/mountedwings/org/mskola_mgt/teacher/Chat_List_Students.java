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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.Chat_menu;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.PromoteStudentsAdapter;
import mountedwings.org.mskola_mgt.data.NumberPromoteStudents;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.widget.LineItemDecoration;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Chat_List_Students extends AppCompatActivity {

    private ArrayList<NumberPromoteStudents> numbers = new ArrayList<>();
    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> regNo = new ArrayList<>();
    private String classes;
    private String session;
    private String arm;
    private boolean selected = true;

    //    private RecyclerView list;
    private FloatingActionButton fab_done;
    private TextView heading;
    private int PREFERENCE_MODE_PRIVATE = 0;
    String school_id, staff_id, TAG = "mSkola";

    PromoteStudentsAdapter adapter;


    private RecyclerView recyclerView;
    private PromoteStudentsAdapter mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_students);

        initToolbar();
        initComponent();
        Tools.toast("Long press for selection", this);

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select students to promote");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.blue_400);
    }

    private void initComponent() {
        SharedPreferences mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);

        Intent intent = getIntent();
        students = intent.getStringArrayListExtra("students_names");
        regNo = intent.getStringArrayListExtra("reg_nos");
        classes = intent.getStringExtra("classes");
        session = intent.getStringExtra("session");
        arm = intent.getStringExtra("arm");

        fab_done = findViewById(R.id.done);

        adapter = new PromoteStudentsAdapter(this, numbers);

        //school_id/staff id from sharedPrefs
        staff_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
        school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

        for (int i = 0; i < students.size(); i++) {
            NumberPromoteStudents number = new NumberPromoteStudents();
            number.setName(students.get(i));
            number.setsession(session);
            number.setclass_arm(classes + " " + arm);
            number.setregNo(regNo.get(i));
            numbers.add(number);
        }

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(false);

        //set data and list adapter
        mAdapter = new PromoteStudentsAdapter(this, numbers);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(new PromoteStudentsAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, NumberPromoteStudents obj, int pos) {

                if (mAdapter.getSelectedItemCount() > 0) {
                    enableActionMode(pos);
                } else {
                    NumberPromoteStudents numberPromoteStudents = mAdapter.getItem(pos);
                    Tools.toast("Long press to select " + numberPromoteStudents.getName(), Chat_List_Students.this);

                }
            }

            @Override
            public void onItemLongClick(View view, NumberPromoteStudents obj, int pos) {
                enableActionMode(pos);
            }
        });

        actionModeCallback = new ActionModeCallback();


        fab_done.setOnClickListener(v -> {
            //Toast.makeText(getApplicationContext(), mAdapter.getSelectedItems().toString(), Toast.LENGTH_SHORT).show();
            StringBuilder regNos = new StringBuilder(mAdapter.getSelectedItems().get(0));
            // Toast.makeText(getApplicationContext(), "First: " + regNos, Toast.LENGTH_SHORT).show();
            for (int i = 1; i < mAdapter.getSelectedItems().size(); i++) {
                regNos.append(";").append(mAdapter.getSelectedItems().get(i));
            }
//            Toast.makeText(getApplicationContext(), regNos.toString(), Toast.LENGTH_SHORT).show();

            Intent intent1 = new Intent(getApplicationContext(), PromoteStudentsActivity.class);
            intent1.putExtra("reg_nos", regNos.toString());
            startActivity(intent1);

            //always make this line last so as not to run into errors -  I know what am saying ;)
            actionMode.finish();
        });


    }

    @SuppressLint("RestrictedApi")
    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
            fab_done.setVisibility(View.VISIBLE);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Tools.setSystemBarColor(Chat_List_Students.this, R.color.blue_grey_700);
            mode.getMenuInflater().inflate(R.menu.menu_selecet_all, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();

            //            if (id == R.id.action_delete) {
//                deleteInboxes();
//                mode.finish();
//                return true;
//            }
//
            if (id == R.id.select_all) {
                if (selected) {
                    selectAll();
                    selected = !selected;
                } else {
                    actionMode.finish();
                    selected = !selected;
                }
                return true;
            }
            return false;
        }

        @SuppressLint("RestrictedApi")
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
            fab_done.setVisibility(View.INVISIBLE);
            Tools.setSystemBarColor(Chat_List_Students.this, R.color.blue_400);
        }
    }

    //  '
    private void selectAll() {
        List<String> listOfSelected = mAdapter.selectAll();
        Tools.toast(listOfSelected.toString(), Chat_List_Students.this);

        actionMode.setTitle(String.valueOf(mAdapter.getSelectedItemCount()));
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplication(), Chat_menu.class));
    }
}