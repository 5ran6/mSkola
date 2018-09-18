package mountedwings.org.mskola_mgt;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Objects;

import mountedwings.org.mskola_mgt.utils.Tools;

public class SettingFlat extends AppCompatActivity {
    private SwitchCompat notification, alarm;
    private boolean notifyState = true, alarmState = false;
    private TextView time;
    private View parent_view;
    public static final String myPref = "mSkola";
    SharedPreferences.Editor editor = getSharedPreferences(myPref, MODE_PRIVATE).edit();


    private static final String[] TIME = new String[]{
            "0 mins", "5 mins", "10 mins", "15 mins", "30 mins"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_flat);

        parent_view = findViewById(android.R.id.content);
        time = findViewById(R.id.time);
        notification = findViewById(R.id.notifications);
        alarm = findViewById(R.id.alarm);
    }

    private void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    public void notifications(View view) {
        notifyState = !notifyState;
        editor.putBoolean("notification", notifyState);
        editor.apply();
    }

    public void alarm(View view) {
        alarmState = !alarmState;
        //sharedPrefs
        editor.putBoolean("minutes_before_state", alarmState);
        editor.apply();

        if (alarmState) {
            //          dialogTimePickerDark((SwitchCompat) view);
            showSingleChoiceDialog();

        } else {
            time.setText(R.string.default_alarm_text);
        }
    }

    private String single_choice_selected;


    private void showSingleChoiceDialog() {
        single_choice_selected = TIME[0];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Minutes before closing");
        builder.setSingleChoiceItems(TIME, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                single_choice_selected = TIME[i];
//                alarm.setChecked(false);

            }
        });
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alarm.setChecked(true);
                time.setText(single_choice_selected + " before closing time");

                switch (single_choice_selected) {
                    case "0 mins":
                        //TODO
                        //sharedPrefs
                        editor.putString("minutes_before_text", "0 mins");
                        editor.putInt("minutes_before_time", 0);
                        editor.apply();

                        break;
                    case "5 mins":
                        //TODO
                        //sharedPrefs
                        editor.putString("minutes_before_text", "5 mins");
                        editor.putInt("minutes_before_time", 5);
                        editor.apply();
                        break;
                    case "10 mins":
                        //TODO
                        //sharedPrefs
                        editor.putString("minutes_before_text", "10 mins");
                        editor.putInt("minutes_before_time", 10);
                        editor.apply();
                        break;
                    case "15 mins":
                        //TODO
                        //sharedPrefs
                        editor.putString("minutes_before_text", "15 mins");
                        editor.putInt("minutes_before_time", 15);
                        editor.apply();
                        break;
                    case "30 mins":
                        //TODO
                        //sharedPrefs
                        editor.putString("minutes_before_text", "30 mins");
                        editor.putInt("minutes_before_time", 30);
                        editor.apply();
                        break;
                }
                Snackbar.make(parent_view, "alarm set for : " + single_choice_selected + " before closing", Snackbar.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alarm.setChecked(false);
                alarmState = !alarmState;

            }

        });
        builder.show();
    }


}