package mountedwings.org.mskola_mgt.teacher;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

public class Assessment extends AppCompatActivity {

    // TODO: TO ADD EVENT WHERE WHENEVER A VIEW IS CLICKED, WE CAN EDIT

    private List<View> view_list = new ArrayList<>();
    private List<RelativeLayout> step_view_list = new ArrayList<>();
    private String[] regNumbs;
    private String[] names;
    private ProgressBar loading;
    private int success_step = 0, len;
    private int current_step = 0;
    private View parent_view;
    private AppCompatEditText score;
    private ViewGroup main;
    private int last_index;
    private String TAG = "mSkola", first_persons_score = "", school_id, class_name, arm, assessment, subject;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_scores_test);
        parent_view = findViewById(android.R.id.content);
        TextView heading = findViewById(R.id.text);
        Intent intent = getIntent();

        school_id = intent.getStringExtra("school_id");
        class_name = intent.getStringExtra("class_name");
        assessment = intent.getStringExtra("assessment");
        arm = intent.getStringExtra("arm");
        subject = intent.getStringExtra("subject");


        heading.setText(new StringBuilder().append(assessment.toUpperCase()).append(" for ").append(class_name).append(arm).toString());

        // initToolbar(assessment.toUpperCase() + " for " + class_name + arm);
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

    }


    private void initViews(int index, String name) {
        //to load all names and score of first person
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_assessment_record, null);

        TextView textView = view.findViewById(R.id.index);
        textView.setText(String.valueOf(index + 1));

        //students name from server
        TextView student_name = view.findViewById(R.id.tv_label_title);
        student_name.setText(name);
        //button init
        Button mark_scores = view.findViewById(R.id.bt_continue_title);
        Button skip = view.findViewById(R.id.bt_skip);

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        main = findViewById(R.id.main_content);
        main.addView(view, index);
        //add to the view list array
        view_list.add(view);
        step_view_list.add((view.findViewById(R.id.step_title)));

        for (View v : view_list) {
            v.findViewById(R.id.lyt_title).setVisibility(View.GONE);
        }
//        view = view_list.get(0);
        view_list.get(0).findViewById(R.id.lyt_title).setVisibility(View.VISIBLE);
        view = view_list.get(0);
        score = view.findViewById(R.id.et_title);
        score.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                areYouSure();
                handled = true;
            }
            return handled;
        });

        score.setText(first_persons_score);
        hideSoftKeyboard();

        mark_scores.setOnClickListener(v -> {
            //is Not a float
            String i = "";
            try {
//                i = Float.valueOf(score.getText().toString());
                i = score.getText().toString();
            } catch (Exception e) {
                Tools.toast(score.getText().toString() + " is an Invalid score", this, R.color.yellow_900);
                e.printStackTrace();
            } finally {
                if (Float.valueOf(i) > 100.00 || Float.valueOf(i) < 0) {
                    Tools.toast(score.getText().toString() + " is an Invalid score", this, R.color.yellow_900);
                    return;
                }
                if (score.getText().toString().trim().equals("")) {
                    Snackbar.make(parent_view, "Score cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }

            //send to server
            if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                new submitScore().execute(school_id, class_name, arm, assessment, subject, String.valueOf(index), String.valueOf(i));
                collapseAndContinue(index);
            }

        });

        skip.setOnClickListener(v -> {

            collapseAndContinue(index);
            //don't record anything

        });
    }

    private void areYouSure() {
        new AlertDialog.Builder(this)
                .setMessage("Are you done?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", (dialog, which) -> {
                })
                .show();
    }

    private void subsequentView(int index, View vv) {
        // button init
        Button mark_scores = vv.findViewById(R.id.bt_continue_title);
        Button skip = vv.findViewById(R.id.bt_skip);

        score = vv.findViewById(R.id.et_title);
        score.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                areYouSure();
                handled = true;
            }
            return handled;
        });

        score.setText("");

        hideSoftKeyboard();

        mark_scores.setOnClickListener(v -> {
//is Not a float
            String i = "";
            try {
//                i = Float.valueOf(score.getText().toString());
                i = score.getText().toString();
            } catch (Exception e) {
                Tools.toast(score.getText().toString() + " is an Invalid score", this, R.color.yellow_900);
                e.printStackTrace();
            } finally {
                if (Float.valueOf(i) > 100.00 || Float.valueOf(i) < 0) {
                    Tools.toast(score.getText().toString() + " is an Invalid score", this, R.color.yellow_900);
                    return;
                }

                if (score.getText().toString().trim().equals("")) {
                    Snackbar.make(parent_view, "Score cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }

            //      Toast.makeText(getApplicationContext(), "Valid " + score.getText().toString(), Toast.LENGTH_SHORT).show();
            //send to server
            if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                new submitScore().execute(school_id, class_name, arm, assessment, subject, String.valueOf(index), String.valueOf(i));
                collapseAndContinue(index);
            }


        });
        skip.setOnClickListener(v -> {

            collapseAndContinue(index);
            //don't record anything
        });
    }

    public void clickAction(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.bt_continue_title:
                // validate input user here
                if (((EditText) findViewById(R.id.et_title)).getText().toString().trim().equals("")) {
                    Snackbar.make(parent_view, "Title cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                collapseAndContinue(0);
                break;
            case R.id.bt_continue_title1:
                // validate input user here
                if (((EditText) findViewById(R.id.et_title1)).getText().toString().trim().equals("")) {
                    Snackbar.make(parent_view, "Title cannot empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                collapseAndContinue(1);
                break;
            case R.id.bt_continue_title2:
                // validate input user here
                if (((EditText) findViewById(R.id.et_title2)).getText().toString().trim().equals("")) {
                    Snackbar.make(parent_view, "Title cannot empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                collapseAndContinue(2);
                break;
            case R.id.bt_continue_title3:
                // validate input user here
                if (((EditText) findViewById(R.id.et_title3)).getText().toString().trim().equals("")) {
                    Snackbar.make(parent_view, "Title cannot empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                collapseAndContinue(3);
                break;
            case R.id.bt_continue_title4:
                // validate input user here
                if (((EditText) findViewById(R.id.et_title4)).getText().toString().trim().equals("")) {
                    Snackbar.make(parent_view, "Title cannot empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

//                collapseAndContinue(4);
                finish();
                break;
        }
    }

    public void clickLabel(View view) {
        //Found out I can't get the index of the view clicked unless i do a full overhaul[that's change the type of view entirely]. So i chose to always go to the first item to start editing from there. It might be stressful but Pele
        int index = 0;
        //index = main.indexOfChild(view);
        // index = step_view_list.indexOf(view);
        // index = view_list.indexOf(view);
        //TODO: just try and get the right ID for the index of the child view and you're good to go
        //  index = view_list.indexOf(view.getRootView().getId());
        //index = view.getId();
//        Toast.makeText(getApplicationContext(), "Review from top", Toast.LENGTH_SHORT).show();
        Tools.toast("Reviewing from top", this, R.color.yellow_900);
//        TODO: scroll to the top of activity

        if (success_step >= index && current_step != index) {
            current_step = index;
            collapseAll();
            ViewAnimation.expand(view_list.get(index).findViewById(R.id.lyt_title));
        }

    }

    //DONE
    private void collapseAndContinue(int index) {
        if (index - 1 < view_list.size()) {
            //     Toast.makeText(getApplicationContext(), "Current index is " + index + ", size of view List is " + view_list.size(), Toast.LENGTH_SHORT).show();
            ViewAnimation.collapse(view_list.get(index).findViewById(R.id.lyt_title));
            setCheckedStep(index);
            last_index = index;
            index++;
            //thread for loading score for next person using index++ for index
            score.setText("");
            current_step = index;
            if (current_step >= view_list.size()) {
                ViewAnimation.collapse(view_list.get(view_list.size() - 1).findViewById(R.id.lyt_title));
            } else {
                if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                    new loading().execute(school_id, class_name, arm, assessment, subject, String.valueOf(index));
                    success_step = index > success_step ? index : success_step;
                    ViewAnimation.expand(view_list.get(index).findViewById(R.id.lyt_title));
                    View vv = view_list.get(index);
                    subsequentView(index, vv);
                }
            }
        } else {
            //just collapse
            ViewAnimation.collapse(view_list.get(view_list.size() - 1).findViewById(R.id.lyt_title));
            hideSoftKeyboard();
        }
    }

    //DONE
    private void collapseAll() {
        for (View v : view_list) {
            ViewAnimation.collapse(v.findViewById(R.id.lyt_title));
        }
    }

    //DONE
    private void setCheckedStep(int index) {
        RelativeLayout relative = step_view_list.get(index);
        relative.removeAllViews();
        ImageButton img = new ImageButton(this);
        img.setImageResource(R.drawable.ic_done);
        img.setBackgroundColor(Color.TRANSPARENT);
        img.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        relative.addView(img);
    }

    //DONE
    private void setCheckedRecorded(int index) {
        RelativeLayout relative = step_view_list.get(index);
        relative.removeAllViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            relative.setBackground(getResources().getDrawable(R.drawable.shape_round_solid_green));
        } else {
            relative.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_round_solid_green));
        }
        ImageButton img = new ImageButton(this);
        img.setImageResource(R.drawable.ic_done);
        img.setBackgroundColor(Color.TRANSPARENT);
        img.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        relative.addView(img);
    }

    //DONE
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        finish();
        return true;
    }

    //DONE
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    //DONE
    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private class loading extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentscore");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + strings[4] + "<>" + regNumbs[Integer.valueOf(strings[5])]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String scores) {
            super.onPostExecute(scores);
            Log.d(TAG, scores);
            //   Toast.makeText(getApplicationContext(), scores, Toast.LENGTH_SHORT).show();
            if (!scores.isEmpty()) {
                score.setText("");
                //has to be here
                score.setText(scores);
            } else {
//                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class submitScore extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("savestudentscore");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + strings[4] + "<>" + regNumbs[Integer.valueOf(strings[5])] + "<>" + strings[6]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String scores) {
            super.onPostExecute(scores);
            Log.d(TAG, scores);
            //   Toast.makeText(getApplicationContext(), scores, Toast.LENGTH_SHORT).show();
            if (!scores.isEmpty()) {
                score.setText("");
                setCheckedRecorded(last_index);
            } else {
                Tools.toast("Check your internet connection and try again", Assessment.this, R.color.red_800);
            }
        }
    }

    //DONE
    private class first_loading extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean success = false;
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getrecordscoreinfo");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + strings[4]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);

            String text = sentData.getStrData();
            Log.d(TAG, text);

            if (!text.equals("0") && !text.equals("")) {

                try {
                    first_persons_score = text.split("##")[1];
                    //set text of EditText

                } catch (Exception ex) {
                }
                if (first_persons_score.equals("_")) {
                    first_persons_score = "";
                }
                text = text.split("##")[0];
                len = text.split("<>").length;
                names = new String[len];
                regNumbs = new String[len];
                if (!text.isEmpty()) {
                    int i = 0;
                    do {
                        //name
                        names[i] = text.split("<>")[i].split(";")[1];
                        //regNumber
                        regNumbs[i] = text.split("<>")[i].split(";")[0];

                        Log.d(TAG, names[i]);
                        Log.d(TAG, regNumbs[i]);
                        i++;
                    } while (i < text.split("<>").length);
                    success = true;
                } else {
                    // display an error dialog and return to previous activity
                    finish();
                }
            } else {
                success = false;

                finish();

            }

            return success;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if (isSuccess) {
                loading.setVisibility(View.GONE);
                for (int i = 0; i < len; i++)
                    initViews(i, names[i]);
            } else {
                Tools.toast("No record found for selected class/subject", Assessment.this, R.color.yellow_900);
                finish();
            }
        }
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
                        if (w > 1)
                            Tools.toast("Back Online! Try again", Assessment.this, R.color.green_800);
                        else
                            new first_loading().execute(school_id, class_name, arm, assessment, subject);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), Assessment.this, R.color.red_500);
                        finish();
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
    }
}
