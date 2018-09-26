package mountedwings.org.mskola_mgt.teacher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

public class Record_scores extends AppCompatActivity {

    // TODO
    // TO ADD EVENT WHERE WHENEVER A VIEW IS CLICKED, WE CAN EDIT

    private List<View> view_list = new ArrayList<>();
    private List<RelativeLayout> step_view_list = new ArrayList<>();
    private String[] regNumbs;
    private String[] names;
    private ProgressBar loading;
    private int success_step = 0, len;
    private int current_step = 0;
    private View parent_view;
    private LinearLayout parent, serial_number, whole_details, input_section;
    private RelativeLayout serial_number_with_textView;
    private View view_small, view_divider;
    private TextView s_no, students_name;
    private AppCompatEditText score;
    private Button record;
    private ViewGroup main;
    private int position, counter = 0, last_index;
    private String TAG = "mSkola", first_persons_score = "", school_id, class_name, arm, assessment, subject, next_persons_score = "" , title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_scores_test);
        parent_view = findViewById(android.R.id.content);
        Intent intent = getIntent();

        school_id = intent.getStringExtra("school_id");
        class_name = intent.getStringExtra("class_name");
        assessment = intent.getStringExtra("assessment");
        arm = intent.getStringExtra("arm");
        subject = intent.getStringExtra("subject");

        title = assessment.toUpperCase() + " for " + class_name + arm;
    //    initToolbar();
        loading = (ProgressBar) findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        new first_loading().execute(school_id, class_name, arm, assessment, subject);
        //        initComponent();
    }


    private void initViews(int index, String name) {
        //to load all names and score of first person
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.assessment_record, null);

        TextView textView = view.findViewById(R.id.index);
        textView.setText(String.valueOf(index + 1));

        //students name from server
        TextView student_name = view.findViewById(R.id.tv_label_title);
        student_name.setText(name);
        //button init
        Button mark_scores = view.findViewById(R.id.bt_continue_title);
        Button skip = view.findViewById(R.id.bt_skip);


        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        main = (ViewGroup) findViewById(R.id.main_content);
        main.addView(view, index);
        //add to the view list array
        view_list.add(view);
        step_view_list.add(((RelativeLayout) view.findViewById(R.id.step_title)));

        for (View v : view_list) {
            v.findViewById(R.id.lyt_title).setVisibility(View.GONE);
//            student_name.setText("");
//            v.setVisibility(View.VISIBLE);
        }
        view = view_list.get(0);
        view_list.get(0).findViewById(R.id.lyt_title).setVisibility(View.VISIBLE);
        view = view_list.get(0);
        score = view.findViewById(R.id.et_title);

        score.setText(first_persons_score);
        hideSoftKeyboard();

        mark_scores.setOnClickListener(v -> {
//is Not a float
            boolean check = false;
            int i = 0;
            try {
                i = Integer.valueOf(score.getText().toString());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), score.getText().toString() + " is an Invalid score", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                if (i > 100) {
                    Toast.makeText(getApplicationContext(), score.getText().toString() + " is an Invalid score", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (score.getText().toString().trim().equals("")) {
                    Snackbar.make(parent_view, "Score cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }

            Toast.makeText(getApplicationContext(), "Valid " + score.getText().toString(), Toast.LENGTH_SHORT).show();
            //send to server
            new submitScore().execute(school_id, class_name, arm, assessment, subject, String.valueOf(index), String.valueOf(i));

            collapseAndContinue(index);

        });

        skip.setOnClickListener(v -> {

            collapseAndContinue(index);
            //don't record anything

        });
//        Toast.makeText(getApplicationContext(), "Here", Toast.LENGTH_SHORT).show();

    }

    private void subsequentView(int index, View vv) {
        // button init
        Button mark_scores = vv.findViewById(R.id.bt_continue_title);
        Button skip = vv.findViewById(R.id.bt_skip);

        score = vv.findViewById(R.id.et_title);
        score.setText("");

        hideSoftKeyboard();

        mark_scores.setOnClickListener(v -> {
//is Not a float
            boolean check = false;
            int i = 0;
            try {
                i = Integer.valueOf(score.getText().toString());

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), score.getText().toString() + " is an Invalid score", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                if (i > 100) {
                    Toast.makeText(getApplicationContext(), score.getText().toString() + " is an Invalid score", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (score.getText().toString().trim().equals("")) {
                    Snackbar.make(parent_view, "Score cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }

            Toast.makeText(getApplicationContext(), "Valid " + score.getText().toString(), Toast.LENGTH_SHORT).show();
            //send to server
            new submitScore().execute(school_id, class_name, arm, assessment, subject, String.valueOf(index), String.valueOf(i));

            collapseAndContinue(index);

        });
        skip.setOnClickListener(v -> {

            collapseAndContinue(index);
            //don't record anything

        });
//        Toast.makeText(getApplicationContext(), "Here", Toast.LENGTH_SHORT).show();

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
        int index;
        index = main.indexOfChild(view);
        // index = step_view_list.indexOf(view);
        // index = view_list.indexOf(view);
        index = 2;

//        index = view.getId();
        if (success_step >= index && current_step != index) {
            current_step = index;
            collapseAll();
            ViewAnimation.expand(view_list.get(index).findViewById(R.id.lyt_title));
        }

    }

    //DONE
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //DONE
    private void collapseAndContinue(int index) {
        if (index - 1 < view_list.size()) {
            Toast.makeText(getApplicationContext(), "Current index is " + index + ", size of view List is " + view_list.size(), Toast.LENGTH_SHORT).show();
            ViewAnimation.collapse(view_list.get(index).findViewById(R.id.lyt_title));
            setCheckedStep(index);
            last_index = index;
            index++;
            //thread for loading score for next person using index++ for index
            score.setText("");
//            Log.d(TAG, "Reg number = " + regNumbs[index]);
            current_step = index;
            if (current_step >= view_list.size()) {
                ViewAnimation.collapse(view_list.get(view_list.size() - 1).findViewById(R.id.lyt_title));
            } else {
                new loading().execute(school_id, class_name, arm, assessment, subject, String.valueOf(index));
                success_step = index > success_step ? index : success_step;
                ViewAnimation.expand(view_list.get(index).findViewById(R.id.lyt_title));
                View vv = view_list.get(index);
                subsequentView(index, vv);
            }
        } else {
            //just collapse
            ViewAnimation.collapse(view_list.get(view_list.size() - 1).findViewById(R.id.lyt_title));
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
            return next_persons_score = sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String scores) {
            super.onPostExecute(scores);
            Log.d(TAG, scores);
            Toast.makeText(getApplicationContext(), scores, Toast.LENGTH_SHORT).show();
            if (!scores.isEmpty()) {
                score.setText("");
                score.setText(scores);
            } else {
                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
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
            return next_persons_score = sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String scores) {
            super.onPostExecute(scores);
            Log.d(TAG, scores);
            Toast.makeText(getApplicationContext(), scores, Toast.LENGTH_SHORT).show();
            if (!scores.isEmpty()) {
                score.setText("");
                score.setText(scores);
                setCheckedRecorded(Integer.valueOf(last_index));
            } else {
                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
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
//            myClass.recordScoresObj = sentData;

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
                        position = i;
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
                    //      Toast.makeText(getApplicationContext(), "No student found in the selected class", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                // display an EMPTY error dialog and return to previous activity
                //  Toast.makeText(getApplicationContext(), "No student found in the selected class", Toast.LENGTH_SHORT).show();
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
                //initViews(0, names[0]);
                for (int i = 0; i < len; i++)
                    initViews(i, names[i]);
            } else {
                Toast.makeText(getApplicationContext(), "No record found for selected class/subject", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
