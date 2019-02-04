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

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.ChildrenListAdapter;
import mountedwings.org.mskola_mgt.data.NumberChildrenList;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class ChildsList extends AppCompatActivity {
    String email, TAG = "mSkola";
    ArrayList<NumberChildrenList> numbers = new ArrayList<>();

    private RecyclerView list;
    private ArrayList<byte[]> allPassport_aPerson = new ArrayList<>();
    private storageFile data = new storageFile();

    private BroadcastReceiver mReceiver;
    private int w = 0;
    private int status;

    private ProgressBar loading;
    private ChildrenListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_child);
        //     Tools.toast("Requesting results.....", this);
        Intent intent = getIntent();

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        //school_id/staff id from sharedPrefs

        email = mPrefs.getString("email_address", intent.getStringExtra("email_address"));
        TextView heading = findViewById(R.id.heading);
        heading.setText(R.string.list_children);


        loading = findViewById(R.id.loading);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);
        new first_loading().execute(email);

//        adapter = new ChildrenListAdapter(numbers);
//        list.setAdapter(adapter);

        //hide parentView
        loading.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //DONE
    private class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
//            data = storageObj;

            storageObj.setOperation("getchildren");
            storageObj.setStrData(strings[0]);
            data = new serverProcessParents().requestProcess(storageObj);

            return data.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (!text.equals("0") && !text.equals("") && !text.isEmpty()) {
                allPassport_aPerson = data.getImageFiles();
                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {

                    NumberChildrenList numberChildrenList = new NumberChildrenList();
                    numberChildrenList.setRegNo(rows[i].split(";")[0]);
                    numberChildrenList.setName(rows[i].split(";")[1]);
                    numberChildrenList.setSchoolId(rows[i].split(";")[2]);
                    numberChildrenList.setSchoolName(rows[i].split(";")[3]);
                    numberChildrenList.setClass_name(rows[i].split(";")[4]);
                    numberChildrenList.setArm(rows[i].split(";")[5]);

                    numberChildrenList.setImage(allPassport_aPerson.get(i));
                    numbers.add(numberChildrenList);
                    loading.setVisibility(View.GONE);
                }
                //setRecyclerView
                adapter = new ChildrenListAdapter(numbers);
                list.setAdapter(adapter);
                loading.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);

                adapter.setOnItemClickListener((view, obj, position) -> {
                    adapter.getItemId(position);
                    //sends the students' regNo and name(for menu header)
                    Tools.toast(email + " reg no = " + obj.getRegNo() + " " + obj.getSchoolId(), ChildsList.this);
                    startActivity(new Intent(getApplicationContext(), SingleChild_menu.class).putExtra("student_reg_no", obj.getRegNo()).putExtra("student_name", obj.getName()).putExtra("class_name", obj.getClass_name() + " " + obj.getArm()).putExtra("school_id", obj.getSchoolId()).putExtra("email_address", email));
                    finish();
                });
            } else {
                Tools.toast("No student(s) assigned to you. Contact the school administration", ChildsList.this);
                finish();
            }
        }
    }


    @Override
    protected void onResume() {
//        list.invalidate();
//        list.setAdapter(adapter);

//        this.mReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                w++;
//                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
//                    @Override
//                    public void onConnectionSuccess() {
//                        status = 1;
//                        if (w > 1)
//                            Tools.toast("Back Online! Try again", ChildsList.this, R.color.green_800);
//                        else
//                            new first_loading().execute(email);
//
//                    }
//
//                    @Override
//                    public void onConnectionFail(String errorMsg) {
//                        status = 0;
//                        Tools.toast(getResources().getString(R.string.no_internet_connection), ChildsList.this, R.color.red_600);
//                    }
//                }).execute();
//            }
//
//        };

//        registerReceiver(
//                this.mReceiver,
//                new IntentFilter(
//                        ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
//        unregisterReceiver(this.mReceiver);
//        w = 0;
        super.onPause();
    }


}

