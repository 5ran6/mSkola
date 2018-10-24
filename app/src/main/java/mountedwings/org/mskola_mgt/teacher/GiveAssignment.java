package mountedwings.org.mskola_mgt.teacher;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.Tools;

public class GiveAssignment extends AppCompatActivity {
    private String school_id, class_name, arm, subject, dueDate, dueTime, questions, staff_id;
    private TextView date, time;
    private LinearLayout lyt_progress;
    private RelativeLayout parent_layout;
    private AppCompatEditText assignment;
    private boolean dateSelected = false, timeSelected = false;

    private void initComponents() {
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        assignment = findViewById(R.id.assignmentText);
        (findViewById(R.id.pick_date)).setOnClickListener(this::dialogDatePickerLight);
        (findViewById(R.id.pick_time)).setOnClickListener(this::dialogTimePickerDark);
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

    private void dialogTimePickerDark(final View bt) {
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
        staff_id = intent1.getStringExtra("staff_id");
        class_name = intent1.getStringExtra("class_name");
        arm = intent1.getStringExtra("arm");
        subject = intent1.getStringExtra("subject");

        initComponents();
    }

    public void upload(View view) {
        if (dateSelected && timeSelected) {
            if (!assignment.getText().toString().isEmpty()) {
                dueDate = date.getText().toString().trim();
                questions = assignment.getText().toString();
                //begin wait dialog
                lyt_progress = findViewById(R.id.lyt_progress);
                parent_layout = findViewById(R.id.lyt_parent);

                parent_layout.setVisibility(View.GONE);
                lyt_progress.setVisibility(View.VISIBLE);
                lyt_progress.setAlpha(1.0f);
/////////////////////////////////////////////
                //pass into background thread
                new uploadAssignment().execute(school_id, questions, subject, class_name + " " + arm, dueDate, dueTime, staff_id);
                //        new uploadAssignment().execute("cac180826043520", questions, "English Language", "JSS1 A", dueDate, dueTime, "admin");
            } else {
                Toast.makeText(getApplicationContext(), "Assignment Field is empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please select due Date and Time", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(getApplicationContext(), "An Error Occurred. Try Again", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
    }

}