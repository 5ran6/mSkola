package mountedwings.org.mskola_mgt.teacher;

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

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.ChatActivity;
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
    private boolean selected = true;

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
        Tools.toast("Long press for selection", this);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Staff to message");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.blue_400);
    }

    private void initComponent() {
        SharedPreferences mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);

        Intent intent = getIntent();
        staff_list = intent.getStringArrayListExtra("staff_list");
        staff_email = intent.getStringArrayListExtra("staff_email");
        category = intent.getStringExtra("category");

        fab_done = findViewById(R.id.done);

        adapter = new NumbersChatStaffListAdapter(this, numbers);

        //school_id/staff id from sharedPrefs
        staff_id = mPrefs.getString("staff_id", getIntent().getStringExtra("staff_id"));
        school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

        for (int i = 0; i < staff_list.size(); i++) {
            NumberChatStaffList number = new NumberChatStaffList();
            number.setRecipient(staff_list.get(i));
            number.setEmail(staff_email.get(i));
            numbers.add(number);
        }

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(false);

        //set data and list adapter
        mAdapter = new NumbersChatStaffListAdapter(this, numbers);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Tools.toast(item.getTitle().toString(), Chat_List_Teachers.this);
        }
        return super.onOptionsItemSelected(item);
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
    }
}