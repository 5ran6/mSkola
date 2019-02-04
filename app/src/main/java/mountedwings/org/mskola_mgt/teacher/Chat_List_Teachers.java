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
import android.os.AsyncTask;
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
import android.widget.ProgressBar;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mountedwings.org.mskola_mgt.ChatActivity;
import mountedwings.org.mskola_mgt.Chat_menu;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.NumbersChatStaffListAdapter;
import mountedwings.org.mskola_mgt.data.NumberChatStaffList;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.widget.LineItemDecoration;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Chat_List_Teachers extends AppCompatActivity {

    private ArrayList<NumberChatStaffList> numbers = new ArrayList<>();
    private ArrayList<String> staff_list = new ArrayList<>();
    private ArrayList<String> staff_email = new ArrayList<>();
    private ArrayList<byte[]> passports = new ArrayList<>();
    private ProgressBar loading;
    private boolean selected = true;
    private storageFile sentData;

    //    private RecyclerView list;
    private FloatingActionButton fab_done;
    private int PREFERENCE_MODE_PRIVATE = 0;
    String school_id, staff_id, TAG = "mSkola", category = "";

    NumbersChatStaffListAdapter adapter;


    private RecyclerView recyclerView;
    private NumbersChatStaffListAdapter mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list_teachers);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Select Staff to message");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.blue_400);
    }

    private void initComponent() {
        SharedPreferences mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);
        //school_id/staff id from sharedPrefs
        staff_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
        school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

        fab_done = findViewById(R.id.done);
        loading = findViewById(R.id.loading);

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(false);

        //initialLoad
        new getStaffMembers().execute(school_id, staff_id);


        //adapter = new NumbersChatStaffListAdapter(this, numbers);

//        for (int i = 0; i < staff_list.size(); i++) {
//            NumberChatStaffList number = new NumberChatStaffList();
//            number.setRecipient(staff_list.get(i));
//            number.setEmail(staff_email.get(i));
//            numbers.add(number);
//        }
        //set data and list adapter
        actionModeCallback = new ActionModeCallback();


        fab_done.setOnClickListener(v -> {
            //Toast.makeText(getApplicationContext(), mAdapter.getSelectedItems().toString(), Toast.LENGTH_SHORT).show();
            StringBuilder recipients = new StringBuilder(mAdapter.getSelectedItems().get(0));
            // Toast.makeText(getApplicationContext(), "First: " + recipients, Toast.LENGTH_SHORT).show();
            for (int i = 1; i < mAdapter.getSelectedItems().size(); i++) {
                recipients.append(";").append(mAdapter.getSelectedItems().get(i));
            }
//            Toast.makeText(getApplicationContext(), recipients.toString(), Toast.LENGTH_SHORT).show();

            Intent intent1 = new Intent(getApplicationContext(), ChatActivity.class);
            intent1.putExtra("recipient", recipients.toString());
            intent1.putExtra("category", category);
            startActivity(intent1);

            //always make this line last so as not to run into errors -  I know what am saying ;)
            actionMode.finish();
        });


    }

    //DONE
    private class getStaffMembers extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstaffmembers");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
            sentData = new serverProcess().requestProcess(storageObj);
            String text = sentData.getStrData();
            return text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recyclerView.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            category = "staff";
            if (!text.equals("0") && !text.isEmpty()) {
                String rows[] = text.split("<>");
                ArrayList<byte[]> allPassport_aPerson = sentData.getImageFiles();

                for (int i = 0; i < rows.length; i++) {
                    NumberChatStaffList number = new NumberChatStaffList();
                    number.setRecipient(rows[i].split(";")[0]);
                    number.setEmail(rows[i].split(";")[1]);
                    number.setImage(allPassport_aPerson.get(i));
                    numbers.add(number);
                }
                mAdapter = new NumbersChatStaffListAdapter(getApplicationContext(), numbers);
                recyclerView.setAdapter(mAdapter);
                mAdapter.setOnClickListener(new NumbersChatStaffListAdapter.OnClickListener() {
                    @Override
                    public void onItemClick(View view, NumberChatStaffList obj, int pos) {

                        if (mAdapter.getSelectedItemCount() > 0) {
                            enableActionMode(pos);
                        } else {
                            NumberChatStaffList numberChatStaffList = mAdapter.getItem(pos);
                            Tools.toast("Long press to select " + numberChatStaffList.getRecipient(), Chat_List_Teachers.this);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, NumberChatStaffList obj, int pos) {
                        enableActionMode(pos);
                    }

                });
                recyclerView.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                Tools.toast("Long press for selection", Chat_List_Teachers.this);

            } else {
                Tools.toast("No registered staff", Chat_List_Teachers.this, R.color.yellow_600);
                finish();
            }

        }
    }


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
            Tools.setSystemBarColor(Chat_List_Teachers.this, R.color.blue_grey_700);
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

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelections();
            actionMode = null;
            fab_done.setVisibility(View.INVISIBLE);
            Tools.setSystemBarColor(Chat_List_Teachers.this, R.color.blue_400);
        }
    }

    //  '
    private void selectAll() {
        List<String> listOfSelected = mAdapter.selectAll();
        Tools.toast(listOfSelected.toString(), Chat_List_Teachers.this);

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