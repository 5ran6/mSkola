package mountedwings.org.mskola_mgt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private String single_choice_selected;

    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;

    private static final int PREFRENCE_MODE_PRIVATE = 0;

    private static final String[] TIME = new String[]{
            "0 mins", "5 mins", "10 mins", "15 mins", "30 mins"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_flat);

        if (getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE).toString() != null) {
            mPrefs = getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE);
            notifyState = mPrefs.getBoolean("notification", true);
            alarmState = mPrefs.getBoolean("minutes_before_state", false);

            if (alarmState) {
                single_choice_selected = mPrefs.getString("minutes_before_text", "10 mins");
            }

        }

        mPrefs = getSharedPreferences(myPref, PREFRENCE_MODE_PRIVATE);

        parent_view = findViewById(android.R.id.content);
        time = findViewById(R.id.time);
        notification = findViewById(R.id.notifications);
        notification.setChecked(notifyState);
        alarm = findViewById(R.id.alarm);
        if (alarmState) {
            alarm.setChecked(alarmState);
            time.setText(single_choice_selected + " before closing time");
        }
    }

    private void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    public void notifications(View view) {
        notifyState = !notifyState;

        editor = mPrefs.edit();
        editor.putBoolean("notification", notifyState);
        editor.apply();
    }

    public void alarm(View view) {
        alarmState = !alarmState;
        //sharedPrefs
        editor = mPrefs.edit();
        editor.putBoolean("minutes_before_state", alarmState);
        editor.apply();

        if (alarmState) {
            //          dialogTimePickerDark((SwitchCompat) view);
            showSingleChoiceDialog();

        } else {
            time.setText(R.string.default_alarm_text);
        }
    }


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
                        editor = mPrefs.edit();
                        editor.putString("minutes_before_text", "0 mins");
                        editor.putInt("minutes_before_time", 0);
                        editor.apply();

                        break;
                    case "5 mins":
                        //TODO
                        //sharedPrefs
                        editor = mPrefs.edit();
                        editor.putString("minutes_before_text", "5 mins");
                        editor.putInt("minutes_before_time", 5);
                        editor.apply();
                        break;
                    case "10 mins":
                        //TODO
                        //sharedPrefs
                        editor = mPrefs.edit();
                        editor.putString("minutes_before_text", "10 mins");
                        editor.putInt("minutes_before_time", 10);
                        editor.apply();
                        break;
                    case "15 mins":
                        //TODO
                        //sharedPrefs
                        editor = mPrefs.edit();
                        editor.putString("minutes_before_text", "15 mins");
                        editor.putInt("minutes_before_time", 15);
                        editor.apply();
                        break;
                    case "30 mins":
                        //TODO
                        //sharedPrefs
                        editor = mPrefs.edit();
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

    @Override
    protected void onResume() {
        super.onResume();

//        notification_state = mPrefs.getBoolean("notification", true);
        //  String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
        //  int idName = prefs.getInt("idName", 0); //0 is the default value.

    }
}