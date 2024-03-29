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

package mountedwings.org.mskola_mgt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Objects;

import mountedwings.org.mskola_mgt.adapter.ChatMenuAdapter;
import mountedwings.org.mskola_mgt.data.NumberChat;
import mountedwings.org.mskola_mgt.data.NumberChatParentsList;
import mountedwings.org.mskola_mgt.data.NumberChatStaffList;
import mountedwings.org.mskola_mgt.teacher.Chat_List_Teachers;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Chat_menu extends AppCompatActivity {
    String school_id, staff_id, TAG = "mSkola", category = "";
    private RecyclerView list;
    private storageFile sentData;
    private ArrayList<NumberChat> numbers = new ArrayList<>();
    private boolean rotate = false;
    private ArrayList<NumberChatStaffList> staff_list = new ArrayList<>();
    private SwipeRefreshLayout swipe_refresh;
    private View lyt_staff;
    private View lyt_parent;
    private View lyt_student;
    private AsyncTask lastThread;
    private View back_drop;
    private ArrayList<NumberChatParentsList> parents_list = new ArrayList<>();

    private ProgressBar loading;
    ChatMenuAdapter adapter;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_menu);
        Intent intent = getIntent();

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));
        //school_id/staff id from sharedPrefs
        staff_id = mPrefs.getString("email_address", intent.getStringExtra("email_address"));
        school_id = mPrefs.getString("school_id", intent.getStringExtra("school_id"));

        FloatingActionButton fab_done = findViewById(R.id.done);

        back_drop = findViewById(R.id.back_drop);
        swipe_refresh = findViewById(R.id.swipe_refresh_layout);
        loading = findViewById(R.id.loading);

        lyt_staff = findViewById(R.id.lyt_staff);
        lyt_parent = findViewById(R.id.lyt_parent);
        lyt_student = findViewById(R.id.lyt_student);

        CardView card_staff = findViewById(R.id.card_staff);
        CardView card_parent = findViewById(R.id.card_parent);
        CardView card_student = findViewById(R.id.card_student);

        FloatingActionButton fab_staff = findViewById(R.id.fab_staff);
        FloatingActionButton fab_parent = findViewById(R.id.fab_parent);
        FloatingActionButton fab_student = findViewById(R.id.fab_student);

        ViewAnimation.initShowOut(lyt_staff);
        ViewAnimation.initShowOut(lyt_parent);
        ViewAnimation.initShowOut(lyt_student);
        back_drop.setVisibility(View.GONE);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);

        adapter = new ChatMenuAdapter(numbers);
        list.setAdapter(adapter);
        //hide parentView
        loading.setVisibility(View.VISIBLE);

        // on swipe list
        swipe_refresh.setOnRefreshListener(() -> pullAndRefresh());

        lyt_student.setOnClickListener(v -> startActivity(new Intent(this, Chat_List_Teachers.class)));
        lyt_parent.setOnClickListener(v -> new getParents().execute(school_id));
        lyt_staff.setOnClickListener(v -> startActivity(new Intent(this, Chat_List_Teachers.class)));

        card_student.setOnClickListener(v -> startActivity(new Intent(this, Chat_List_Teachers.class)));
        card_parent.setOnClickListener(v -> new getParents().execute(school_id));
        card_staff.setOnClickListener(v -> startActivity(new Intent(this, Chat_List_Teachers.class)));

        fab_student.setOnClickListener(v -> startActivity(new Intent(this, Chat_List_Teachers.class)));
        fab_parent.setOnClickListener(v -> new getParents().execute(school_id));
        fab_staff.setOnClickListener(v -> startActivity(new Intent(this, Chat_List_Teachers.class)));

        fab_done.setOnClickListener(v -> toggleFabMode(v));
//        fab_done.setOnClickListener(v -> showSingleChoiceDialog());
    }

    //DONE
    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_staff);
            ViewAnimation.showIn(lyt_student);
            ViewAnimation.showIn(lyt_parent);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_staff);
            ViewAnimation.showOut(lyt_student);
            ViewAnimation.showOut(lyt_parent);
            back_drop.setVisibility(View.GONE);
        }
    }


    private void swipeProgress(final boolean show) {
        if (!show) {
            swipe_refresh.setRefreshing(show);
            return;
        }
        swipe_refresh.post(() -> swipe_refresh.setRefreshing(show));
    }

    private void pullAndRefresh() {
        swipeProgress(true);
        new Handler().postDelayed(() -> {
            if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                new first_loading().execute(school_id, staff_id);
            } else {
                Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);
            }

        }, 3000);
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
                w++;
                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        status = 1;
                        if (w > 1) {
                            try {
                                Tools.toast("Back Online!", Chat_menu.this, R.color.green_800);
                                lastThread.execute();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else
                            lastThread = new first_loading().execute();
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), Chat_menu.this, R.color.red_500);
                    }
                }).execute();
            }

        };

        registerReceiver(
                this.mReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }


    @Override
    protected void onPause() {
        unregisterReceiver(this.mReceiver);
        w = 0;
        super.onPause();
        finish();
    }

    //DONE
    private class getParents extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getparents");
            storageObj.setStrData(strings[0]);
            sentData = new serverProcess().requestProcess(storageObj);
            String text = sentData.getStrData();
            return text;
        }

        @Override
        protected void onPreExecute() {
            list.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            category = "parent";
            if (!text.equals("0") && !text.isEmpty()) {
                String[] rows = text.split("<>");
                ArrayList<byte[]> allPassport_aPerson = sentData.getImageFiles();

                for (int i = 0; i < rows.length; i++) {
                    NumberChatParentsList number = new NumberChatParentsList();
                    number.setRecipient(rows[i].split(";")[0]);
                    number.setEmail(rows[i].split(";")[1]);
                    number.setImage(allPassport_aPerson.get(i));
                    parents_list.add(number);

                }
            } else {
                Tools.toast("No Parent registered", Chat_menu.this, R.color.yellow_600);

                finish();
            }
            //finally
            Intent intent = new Intent(getApplicationContext(), Chat_List_Teachers.class);
            intent.putExtra("category", category);
            startActivity(intent);
        }
    }

    private class getStudents extends AsyncTask<String, Integer, String> {
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
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            category = "student";
            if (!text.equals("0") && !text.isEmpty()) {
                String[] rows = text.split("<>");
                ArrayList<byte[]> allPassport_aPerson = sentData.getImageFiles();

                for (int i = 0; i < rows.length; i++) {
//                    String row = rows[i];
//                    names.add(row.split("##")[0]);
//                    messages.add(row.split("##")[1]);
//                    dates.add(row.split("##")[2]);
                    NumberChatStaffList number = new NumberChatStaffList();
                    number.setRecipient(rows[i].split(";")[0]);
                    number.setEmail(rows[i].split(";")[1]);
                    number.setImage(allPassport_aPerson.get(i));
                    staff_list.add(number);

                }
            } else {
                Tools.toast("No messages yet", Chat_menu.this, R.color.yellow_600);
                finish();
            }
            //finally
            Intent intent = new Intent(getApplicationContext(), Chat_List_Teachers.class);
            startActivity(intent);

        }
    }

    //DONE
    private class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getmessages");
            //school_id, staff_id
            storageObj.setStrData(school_id + "<>" + staff_id);
            sentData = new serverProcess().requestProcess(storageObj);
            String text = sentData.getStrData();
            return text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (!text.equals("not found") && !text.equals("0") && !text.isEmpty()) {

                numbers.clear();
                swipeProgress(false);

                String[] rows = text.split("<>");
                ArrayList<byte[]> allPassport_aPerson = sentData.getImageFiles();

                for (int i = 0; i < rows.length; i++) {
                    String row = rows[i];
                    NumberChat number = new NumberChat();
                    number.setRecipient(row.split("##")[0]);
                    number.setmsg(row.split("##")[1]);
                    number.setdate(row.split("##")[2]);
                    number.setEmail(row.split("##")[3]);
                    number.setImage(allPassport_aPerson.get(i));
                    numbers.add(number);
                }

                //show recyclerView with inflated views
                adapter = new ChatMenuAdapter(numbers);
                list.setAdapter(adapter);
                adapter.setOnItemClickListener((view, obj, position) -> {
                    String recipient = numbers.get(position).getEmail();
                    //       Toast.makeText(getBaseContext(), "Item " + recipient + " clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ChatActivity.class).putExtra("recipient", recipient));
                    finish();
                });
            } else {
                Tools.toast("No messages yet", Chat_menu.this, R.color.yellow_600);

                finish();
            }
            //finally
            loading.setVisibility(View.GONE);
            swipeProgress(false);

        }
    }


}

