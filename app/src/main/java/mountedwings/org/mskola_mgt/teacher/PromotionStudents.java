package mountedwings.org.mskola_mgt.teacher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.NumbersPromoteStudentsAdapter;
import mountedwings.org.mskola_mgt.data.NumberPromoteStudents;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.widget.LineItemDecoration;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class PromotionStudents extends AppCompatActivity {

    private ArrayList<NumberPromoteStudents> numbers = new ArrayList<>();
    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> regNo = new ArrayList<>();
    private String classes;
    private String session;
    private String arm;
    private boolean selected = true;

    //    private RecyclerView list;
    private FloatingActionButton fab_done;
    private int PREFERENCE_MODE_PRIVATE = 0;
    String school_id, staff_id, TAG = "mSkola";


    private RecyclerView recyclerView;
    private NumbersPromoteStudentsAdapter mAdapter;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private Toolbar toolbar;
    private BroadcastReceiver mReceiver;
    private int w = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_students);

        initToolbar();
        initComponent();
        Tools.toast("Long press for selection", PromotionStudents.this, R.color.yellow_900);
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
        mAdapter = new NumbersPromoteStudentsAdapter(this, numbers);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(new NumbersPromoteStudentsAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, NumberPromoteStudents obj, int pos) {

                if (mAdapter.getSelectedItemCount() > 0) {
                    enableActionMode(pos);
                } else {
                    NumberPromoteStudents numberPromoteStudents = mAdapter.getItem(pos);
                    Tools.toast("Long press to select " + numberPromoteStudents.getName(), PromotionStudents.this, R.color.yellow_800);

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
            Tools.setSystemBarColor(PromotionStudents.this, R.color.blue_grey_700);
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
            Tools.setSystemBarColor(PromotionStudents.this, R.color.blue_400);
        }
    }

    //  '
    private void selectAll() {
        List<String> listOfSelected = mAdapter.selectAll();
        //   Tools.toast(listOfSelected.toString(), PromotionStudents.this, R.color.yellow_800);

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
    }

    @Override
    protected void onResume() {
        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = NetworkUtil.getConnectivityStatusString(context);
                if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED && w < 1) {
                    Tools.toast("No Internet connection!", PromotionStudents.this, R.color.red_500);
                }
                w++;
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction()) && w > 1) {
                    if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                        Tools.toast("Back Online!", PromotionStudents.this, R.color.green_800);
                    } else {
                        Tools.toast("No Internet connection!", PromotionStudents.this, R.color.red_500);
                    }
                }
            }

        };

        registerReceiver(
                this.mReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

}