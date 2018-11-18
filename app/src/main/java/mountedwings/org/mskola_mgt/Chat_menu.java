package mountedwings.org.mskola_mgt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Objects;

import mountedwings.org.mskola_mgt.adapter.NumbersChatMenuAdapter;
import mountedwings.org.mskola_mgt.data.NumberChat;
import mountedwings.org.mskola_mgt.data.NumberChatParentsList;
import mountedwings.org.mskola_mgt.data.NumberChatStaffList;
import mountedwings.org.mskola_mgt.teacher.Chat_List_Teachers;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Chat_menu extends AppCompatActivity {
    String school_id, staff_id, TAG = "mSkola", category = "";
    private RecyclerView list;
    private String message_category;
    private storageFile sentData;
    private ArrayList<NumberChat> numbers = new ArrayList<>();
    private boolean rotate = false;
    private ArrayList<NumberChatStaffList> staff_list = new ArrayList<>();
    private ArrayList<String> staff = new ArrayList<>();
    private ArrayList<String> staff_email = new ArrayList<>();
    private SwipeRefreshLayout swipe_refresh;
    private View lyt_staff;
    private View lyt_parent;
    private View lyt_student;

    private FloatingActionButton fab_staff;
    private FloatingActionButton fab_parent;
    private FloatingActionButton fab_student;

    private CardView card_staff;
    private CardView card_parent;
    private CardView card_student;

    private View back_drop;

    private ArrayList<NumberChatParentsList> parents_list = new ArrayList<>();

    private ProgressBar loading;
    NumbersChatMenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_menu);
        Intent intent = getIntent();

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));
        //school_id/staff id from sharedPrefs
        staff_id = mPrefs.getString("staff_id", intent.getStringExtra("staff_id"));
        school_id = mPrefs.getString("school_id", intent.getStringExtra("school_id"));

        FloatingActionButton fab_done = findViewById(R.id.done);

        back_drop = findViewById(R.id.back_drop);
        swipe_refresh = findViewById(R.id.swipe_refresh_layout);
        loading = findViewById(R.id.loading);

        lyt_staff = findViewById(R.id.lyt_staff);
        lyt_parent = findViewById(R.id.lyt_parent);
        lyt_student = findViewById(R.id.lyt_student);

        card_staff = findViewById(R.id.card_staff);
        card_parent = findViewById(R.id.card_parent);
        card_student = findViewById(R.id.card_student);

        fab_staff = findViewById(R.id.fab_staff);
        fab_parent = findViewById(R.id.fab_parent);
        fab_student = findViewById(R.id.fab_student);

        ViewAnimation.initShowOut(lyt_staff);
        ViewAnimation.initShowOut(lyt_parent);
        ViewAnimation.initShowOut(lyt_student);
        back_drop.setVisibility(View.GONE);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(false);

        adapter = new NumbersChatMenuAdapter(numbers);
        list.setAdapter(adapter);
        //hide parentView
        loading.setVisibility(View.VISIBLE);

        new first_loading().execute(school_id, staff_id);
        // on swipe list
        swipe_refresh.setOnRefreshListener(() -> pullAndRefresh());

        lyt_student.setOnClickListener(v -> new getStaffMembers().execute(school_id, staff_id));
        lyt_parent.setOnClickListener(v -> new getParents().execute(school_id));
        lyt_staff.setOnClickListener(v -> new getStaffMembers().execute(school_id, staff_id));

        card_student.setOnClickListener(v -> new getStaffMembers().execute(school_id, staff_id));
        card_parent.setOnClickListener(v -> new getParents().execute(school_id));
        card_staff.setOnClickListener(v -> new getStaffMembers().execute(school_id, staff_id));

        fab_student.setOnClickListener(v -> new getStaffMembers().execute(school_id, staff_id));
        fab_parent.setOnClickListener(v -> new getParents().execute(school_id));
        fab_staff.setOnClickListener(v -> new getStaffMembers().execute(school_id, staff_id));

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
        new Handler().postDelayed(() -> new first_loading().execute(school_id, staff_id), 3000);
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
            storageObj.setOperation("getmessages");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
            sentData = new serverProcess().requestProcess(storageObj);
            String text = sentData.getStrData();
            Log.d(TAG, text);
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

                String rows[] = text.split("<>");
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
                adapter = new NumbersChatMenuAdapter(numbers);
                list.setAdapter(adapter);
                adapter.setOnItemClickListener((view, obj, position) -> {
                    String recipient = numbers.get(position).getEmail();
                    //       Toast.makeText(getBaseContext(), "Item " + recipient + " clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ChatActivity.class).putExtra("recipient", recipient));
                    finish();
                });
            } else {
                Toast.makeText(Objects.requireNonNull(getApplicationContext()), "No messages yet", Toast.LENGTH_SHORT).show();
                finish();
            }
            //finally
            loading.setVisibility(View.GONE);
            swipeProgress(false);

        }
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
            Log.d(TAG, text);
            return text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            category = "staff";
            if (!text.equals("0") && !text.isEmpty()) {
                String rows[] = text.split("<>");
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
                    staff.add(number.getRecipient());
                    staff_email.add(number.getEmail());
                }
            } else {
                Toast.makeText(Objects.requireNonNull(getApplicationContext()), "No staff registered", Toast.LENGTH_SHORT).show();
                finish();
            }
            //finally
            Intent intent = new Intent(getApplicationContext(), Chat_List_Teachers.class);
            intent.putExtra("staff_list", staff);
            intent.putExtra("category", category);
            intent.putExtra("staff_email", staff_email);
            startActivity(intent);

        }
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
            Log.d(TAG, text);
            return text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            category = "parent";
            Log.i("mSkola", text);
            if (!text.equals("0") && !text.isEmpty()) {
                String rows[] = text.split("<>");
                ArrayList<byte[]> allPassport_aPerson = sentData.getImageFiles();

                for (int i = 0; i < rows.length; i++) {
                    NumberChatParentsList number = new NumberChatParentsList();
                    number.setRecipient(rows[i].split(";")[0]);
                    number.setEmail(rows[i].split(";")[1]);
                    number.setImage(allPassport_aPerson.get(i));
                    parents_list.add(number);

                }
            } else {
                Toast.makeText(Objects.requireNonNull(getApplicationContext()), "No Parent registered", Toast.LENGTH_SHORT).show();
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
            Log.d(TAG, text);
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
                String rows[] = text.split("<>");
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
                Toast.makeText(Objects.requireNonNull(getApplicationContext()), "No messages yet", Toast.LENGTH_SHORT).show();
                finish();
            }
            //finally
            Intent intent = new Intent(getApplicationContext(), Chat_List_Teachers.class);
            startActivity(intent);

        }
    }

    private void showSingleChoiceDialog() {
        final String items[] = {"Staff", "Parent", "Student"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send Message to");
        builder.setItems(items, (dialogInterface, i) -> {
            message_category = items[i];
            Intent intent;
            switch (message_category) {
                case "Staff":
                    new getStaffMembers().execute(school_id, staff_id);
                    break;
                case "Parent":
                    new getParents().execute(school_id);
                    break;
                case "Student":
//                    new getStudents().execute(staff_id);
                    break;
            }
        });
        builder.show();
    }

}

