package mountedwings.org.mskola_mgt.teacher;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.Tools;

public class GiveAssignment extends AppCompatActivity {
    private String school_id;
    private String class_name;
    private String arm;
    private String subject;
    private String dueTime;
    private String staff_id;
    private TextView date, time;
    private AppCompatEditText assignment;
    private boolean dateSelected = false, timeSelected = false;
    private MaterialRippleLayout materialRippleLayout;

    private void initComponents() {
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        assignment = findViewById(R.id.assignmentText);
        materialRippleLayout = findViewById(R.id.uploadLayout);
        (findViewById(R.id.pick_date)).setOnClickListener(this::dialogDatePickerLight);
        (findViewById(R.id.pick_time)).setOnClickListener(bt -> dialogTimePickerDark());
    }


    private void dialogDatePickerLight(final View bt) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    long date_ship_millis = calendar.getTimeInMillis();
                    date.setText(Tools.getFormattedDate1(date_ship_millis));
                    dateSelected = true;
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }

    private void dialogTimePickerDark() {
        Calendar cur_calender = Calendar.getInstance();
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog datePicker = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance((view, hourOfDay, minute, second) -> {
            String am_pm = "";
            Calendar datetime = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime.set(Calendar.MINUTE, minute);

            if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                am_pm = "AM";
            else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";
            String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";

            time.setText(strHrsToShow + ":" + minute + " " + am_pm);
            dueTime = strHrsToShow + ":" + minute + ":" + second;
            timeSelected = true;
        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), false);
        //set dark light
        datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_assignment);
        Intent intent1 = getIntent();
        school_id = intent1.getStringExtra("school_id");
        staff_id = intent1.getStringExtra("email_address");
        class_name = intent1.getStringExtra("class_name");
        arm = intent1.getStringExtra("arm");
        subject = intent1.getStringExtra("subject");

        initComponents();
    }

    public void upload(View view) {
        if (dateSelected && timeSelected) {
            if (!assignment.getText().toString().isEmpty()) {
                String dueDate = date.getText().toString().trim();
                String questions = assignment.getText().toString();
                //begin wait dialog
                LinearLayout lyt_progress = findViewById(R.id.lyt_progress);
                RelativeLayout parent_layout = findViewById(R.id.lyt_parent);

                parent_layout.setVisibility(View.GONE);
                lyt_progress.setVisibility(View.VISIBLE);
                lyt_progress.setAlpha(1.0f);

                //invisible button
                materialRippleLayout.setVisibility(View.GONE);

/////////////////////////////////////////////

                //pass into background thread
                new uploadAssignment().execute(school_id, questions, subject, class_name + " " + arm, dueDate, dueTime, staff_id);
                //        new uploadAssignment().execute("cac180826043520", questions, "English Language", "JSS1 A", dueDate, dueTime, "admin");
            } else {
                Tools.toast("Assignment Field is empty", GiveAssignment.this, R.color.yellow_800);

            }
        } else {
            Tools.toast("Please select due Date and Time", GiveAssignment.this, R.color.yellow_800);

        }
    }

    private class uploadAssignment extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("uploadassignment");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + strings[4] + "<>" + strings[5] + "<>" + strings[6]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);

            if (text.equals("success")) {
                Tools.toast("Done", GiveAssignment.this, R.color.green_300);
                finish();

            } else {
                Tools.toast("An Error Occurred. Try Again", GiveAssignment.this, R.color.red_500);

            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
    }

}