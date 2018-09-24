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

public class Record_scores_Test extends AppCompatActivity {

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
    private int position, counter =0;
    private String TAG = "mSkola", first_persons_score = "", school_id, class_name, arm, assessment, subject, next_persons_score = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_scores_test);
        parent_view = findViewById(android.R.id.content);
//        initToolbar();
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        new first_loading().execute("cac180826043520", "JSS1", "A", "CA1", "English Language");
        //        initComponent();
    }

    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        //parent linLay
        parent = findViewById(R.id.main_content);
        parent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.HORIZONTAL);


        serial_number = new LinearLayout(this);
        whole_details = new LinearLayout(this);
        serial_number_with_textView = new RelativeLayout(this);


        //Layout 1
        s_no = new TextView(this);
        s_no.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        s_no.setTextAppearance(this, R.style.TextAppearance_AppCompat_Subhead);
        s_no.setTextColor(R.color.white);
        s_no.setText("19");

        serial_number_with_textView.setLayoutParams(new RelativeLayout.LayoutParams(35, 35));
        serial_number_with_textView.setGravity(Gravity.CENTER);

        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            serial_number_with_textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_round_solid));
        } else {
            serial_number_with_textView.setBackground(getResources().getDrawable(R.drawable.shape_round_solid));

        }
        serial_number_with_textView.addView(s_no);

        view_divider = new View(this);
        view_divider.setLayoutParams(new ViewGroup.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT));
        view_divider.setBackgroundColor(R.color.grey_10);
        view_divider.setMinimumHeight(R.attr.actionBarSize);

        serial_number = new LinearLayout(this);
        serial_number.setOrientation(LinearLayout.VERTICAL);
        serial_number.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        serial_number.setGravity(Gravity.CENTER_HORIZONTAL);


        serial_number.addView(serial_number_with_textView);
        serial_number.addView(view_divider);
        // View small
        view_small = new View(this);
        view_small.setLayoutParams(new ViewGroup.LayoutParams(2, ViewGroup.LayoutParams.MATCH_PARENT));


        // Layout 2
        whole_details.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        whole_details.setOrientation(LinearLayout.VERTICAL);
        whole_details.setMinimumHeight(R.attr.actionBarSize);


        //textView of students name
        students_name = new TextView(this);
        students_name.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 35));
        students_name.setText(R.string.demo_name);
        students_name.setGravity(Gravity.CENTER_VERTICAL);
        students_name.setPadding(3, 0, 3, 0);
        students_name.setTextAppearance(this, R.style.TextAppearance_AppCompat_Medium);
//        students_name.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        students_name.setTextColor(R.color.grey_90);

        //LinearLayout recording section
        input_section = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 15);
        input_section.setOrientation(LinearLayout.VERTICAL);
        input_section.setLayoutParams(params);


        score = new AppCompatEditText(this);
        score.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        score.setHint(R.string.enter_score);
        score.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        score.setMaxLines(1);
        //setMaxLength

        //record button
        record = new Button(this);
        record.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        record.setGravity(Gravity.CENTER);
        record.setMinWidth(0);
        record.setText("Register Score");
//        record.setBackground(getResources().getResourceName(R.style.Button_Primary));
        //    record.setBackgroundResource(R.style.Button_Primary);

        input_section.addView(score);
        input_section.addView(record);

        parent.addView(serial_number);
        parent.addView(view_small);
        parent.addView(whole_details);

    }

    private void initViews(int index, String name) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.assessment_record, null);
        TextView textView = view.findViewById(R.id.index);
        textView.setText(String.valueOf(index));

        TextView student_name = view.findViewById(R.id.tv_label_title);
        student_name.setText(name);
        Button mark_scores = view.findViewById(R.id.bt_continue_title);
        Button skip = view.findViewById(R.id.bt_skip);

        score = view.findViewById(R.id.et_title);


        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        main = (ViewGroup) findViewById(R.id.main_content);
        main.addView(view, index);
        //add to the view list array
        view_list.add(view);
        step_view_list.add(((RelativeLayout) view.findViewById(R.id.step_title)));

        for (View v : view_list) {
            v.findViewById(R.id.lyt_title).setVisibility(View.GONE);
//            v.setVisibility(View.VISIBLE);
        }
        view_list.get(0).findViewById(R.id.lyt_title).setVisibility(View.VISIBLE);
        if (counter <= 1) {
            score.setText(first_persons_score);
            counter++;
        } else {
            score.setText("");
        }
        hideSoftKeyboard();

        mark_scores.setOnClickListener(v -> {
            if (Integer.valueOf(score.getText().toString()) > 100) {
                Toast.makeText(getApplicationContext(), score.getText().toString() + " is an Invalid score", Toast.LENGTH_SHORT).show();
                return;
            }
            if (score.getText().toString().trim().equals("")) {
                Snackbar.make(parent_view, "Score cannot be empty", Snackbar.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getApplicationContext(), "Valid " + score.getText().toString(), Toast.LENGTH_SHORT).show();
            collapseAndContinue(index);

        });
        skip.setOnClickListener(v -> {

            collapseAndContinue(index);
            //don't record anything

        });
//        Toast.makeText(getApplicationContext(), "Here", Toast.LENGTH_SHORT).show();

    }

    private void initComponent() {
        // populate layout field
        view_list.add(findViewById(R.id.lyt_title));

        // populate view step (circle in left)
        step_view_list.add(((RelativeLayout) findViewById(R.id.step_title)));

        for (View v : view_list) {
            v.setVisibility(View.GONE);
        }

        view_list.get(0).setVisibility(View.VISIBLE);
        hideSoftKeyboard();


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
        Objects.requireNonNull(getSupportActionBar()).setTitle("CA1 for SSS 3B");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //DONE
    private void collapseAndContinue(int index) {
        ViewAnimation.collapse(view_list.get(index).findViewById(R.id.lyt_title));
        setCheckedStep(index);
        index++;
        //thread for loading score for next person using index++ for index
        new loading().execute(school_id, class_name, arm, assessment, subject, String.valueOf(index));
        //       loadNextScore(school_id, class_name, arm, assessment, subject, index);
        current_step = index;
        success_step = index > success_step ? index : success_step;
        ViewAnimation.expand(view_list.get(index).findViewById(R.id.lyt_title));
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
            return loadNextScore(strings[0], strings[1], strings[2], strings[3], strings[4], Integer.valueOf(strings[5]));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String scores) {
            super.onPostExecute(scores);
            if (!scores.isEmpty()) {
                score.setText(scores);
            } else {
                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class first_loading extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

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

                } else {
                    // display an error dialog and return to previous activity
                    Toast.makeText(getApplicationContext(), "No student found in the selected class", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                // display an EMPTY error dialog and return to previous activity
                Toast.makeText(getApplicationContext(), "No student found in the selected class", Toast.LENGTH_SHORT).show();
                finish();

            }


            return true;
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
                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void loadRecordScorePage(String schID, String className, String arm, String assessment, String subject) {

    }

    private String loadNextScore(String schID, String className, String arm, String assessment, String subject, int index) {
        storageFile storageObj = new storageFile();
        storageObj.setOperation("getstudentscore");
        storageObj.setStrData(schID + "<>" + className + "<>" + arm + "<>" + assessment + "<>" + subject + "<>" + regNumbs[index]);
        storageFile sentData = new serverProcess().requestProcess(storageObj);
        next_persons_score = sentData.getStrData();
//try
        view_list.get(index).findViewById(R.id.lyt_title).setVisibility(View.VISIBLE);
        return next_persons_score;
    }

}
