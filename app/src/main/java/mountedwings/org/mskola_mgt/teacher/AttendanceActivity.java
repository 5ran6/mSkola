package mountedwings.org.mskola_mgt.teacher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.NumbersAdapter;
import mountedwings.org.mskola_mgt.data.Number;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AttendanceActivity extends AppCompatActivity {
    private View back_drop;
    private boolean rotate = false;
    private View lyt_hols;
    private View lyt_save;
    public CheckBox all_morning, all_afternoon;
    ArrayList<Number> numbers;
    private ArrayList names = new ArrayList();
    private ArrayList regNo = new ArrayList();
    private RecyclerView list;
    private FloatingActionButton fab_add;
    public String[] morning = null, afternoon = null;

    String date, school_id, class_name, arm, TAG = "mSkola";
    ProgressBar loading;
    NumbersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        fab_add = findViewById(R.id.see_more);
        FloatingActionButton fab_holidays = findViewById(R.id.fab_hols);
        FloatingActionButton fab_save = findViewById(R.id.fab_save);
        TextView pub = findViewById(R.id.pub);
        TextView sav = findViewById(R.id.sav);
        back_drop = findViewById(R.id.back_drop);
        all_morning = findViewById(R.id.all_checkbox1);
        all_afternoon = findViewById(R.id.all_checkbox2);

        loading = findViewById(R.id.loading);
        loading.setVisibility(VISIBLE);

        lyt_hols = findViewById(R.id.lyt_mic);
        lyt_save = findViewById(R.id.lyt_call);
        ViewAnimation.initShowOut(lyt_hols);
        ViewAnimation.initShowOut(lyt_save);
        back_drop.setVisibility(GONE);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(false);

/////////////////////////////////////////////////////////////////////////////////////////////////////  init
        school_id = getIntent().getStringExtra("school_id");
        class_name = getIntent().getStringExtra("class_name");
        arm = getIntent().getStringExtra("arm");
        date = getIntent().getStringExtra("date");

        loading.setVisibility(View.VISIBLE);

        new first_loading().execute(school_id, class_name, arm, date);
//        new first_loading().execute("cac180826043520", "JSS1", "A", "2018-10-03");
        //  new first_loading().execute("cac181009105222", "JSS1", "A", "2018-10-03");
///////////////////////////////////////////////////////////////////////////////////////////////////////


        back_drop.setOnClickListener(v -> toggleFabMode(fab_add));

        fab_holidays.setOnClickListener(v -> new publicHolidays().execute(school_id, class_name, arm, date));

        fab_save.setOnClickListener(v -> {
            new markAttendance().execute(school_id, class_name, arm, date);
//            new markAttendance().execute("cac180826043520", "JSS1", "A", "2018-10-03");

        });


        fab_add.setOnClickListener(v -> toggleFabMode(v));

        sav.setOnClickListener(v -> new markAttendance().execute(school_id, class_name, arm, date));
        pub.setOnClickListener(v -> new publicHolidays().execute(school_id, class_name, arm, date));

        all_morning.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                //tick all
                for (int i = 0; i < numbers.size(); i++) {
                    numbers.get(i).setSelected(true);
                }
                adapter.selectAll();
            } else {
                //uncheck all
                for (int i = 0; i < numbers.size(); i++) {
                    numbers.get(i).setSelected(false);
                }
                adapter.unSelectAll();
            }
        });
        all_afternoon.setOnClickListener(v -> {
            if (((CheckBox) v).isChecked()) {
                //tick all
                for (int i = 0; i < numbers.size(); i++) {
                    numbers.get(i).setSelected1(true);
                }
                adapter.selectAll1();
            } else {
                //uncheck all
                for (int i = 0; i < numbers.size(); i++) {
                    numbers.get(i).setSelected1(false);
                }
                adapter.unSelectAll1();
            }
        });
    }

    //DONE
    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_hols);
            ViewAnimation.showIn(lyt_save);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_hols);
            ViewAnimation.showOut(lyt_save);
            back_drop.setVisibility(View.GONE);
        }
    }


    public class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            //  Boolean success = false;
            storageFile storageObj = new storageFile();
            storageObj.setOperation("loadattendance");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);

            String text = sentData.getStrData();

            return text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (!text.isEmpty() && !text.equals("0")) {
                loading.setVisibility(View.GONE);

                //get values
                morning = new String[text.split("<>").length];
                afternoon = new String[text.split("<>").length];

                numbers = new ArrayList<>();

                //if there was previous data
                if (text.contains("##")) {
                    morning = text.split("##")[1].split(";");
                    afternoon = text.split("##")[2].split(";");
                    text = text.split("##")[0];
                }

                //worked. Split into reg and names
                String rows[] = text.split("<>");
                if (rows.length > 0) {
                    for (int i = 0; i < rows.length; i++) {
                        regNo.add(rows[i].split(";")[0]);
                        names.add(rows[i].split(";")[1]);

                        Number number = new Number();
                        number.setONEs((i + 1) + "");
                        number.setTextONEs(names.get(i).toString());
                        try {
                            //checked morning
                            if (morning[i].trim().equals("1")) number.setSelected(true);

                            //checked afternoon
                            if (afternoon[i].trim().equals("1")) number.setSelected1(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        numbers.add(number);
                    }
                    //show recyclerView with inflated views
                    adapter = new NumbersAdapter(numbers);
                    list.setAdapter(adapter);
                } else {
                    // display an EMPTY error dialog and return to previous activity
                    Tools.toast("Oops! Something went wrong", AttendanceActivity.this, R.color.red_600);
                    finish();
                }
            } else {
                Tools.toast("No record found for selected class/arm", AttendanceActivity.this, R.color.yellow_600);
                finish();
            }
        }
    }

    private class markAttendance extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("takeattendance");
            //to get the attendance values
            StringBuilder allReg = new StringBuilder();
            StringBuilder morning = new StringBuilder();
            StringBuilder afternoon = new StringBuilder();

            for (int i = 0; i < regNo.size(); i++) {
                if (allReg.length() == 0) {
                    allReg = new StringBuilder(regNo.get(i).toString());
                } else {
                    allReg.append(";").append(regNo.get(i).toString());
                }

                //morning
                if (morning.length() == 0) {
                    //get checkbox at position i
                    //get the string at that index
                    if (numbers.get(i).isSelected()) {
                        morning = new StringBuilder("1");
                    } else {
                        morning = new StringBuilder("0");
                    }
                } else {
                    if (numbers.get(i).isSelected()) {
                        morning.append(";1");
                    } else {
                        morning.append(";0");
                    }
                }
//afternoon
                if (afternoon.length() == 0) {
                    if (numbers.get(i).isSelected1()) {
                        afternoon = new StringBuilder("1");
                    } else {
                        afternoon = new StringBuilder("0");
                    }
                } else {
                    if (numbers.get(i).isSelected1()) {
                        afternoon.append(";1");
                    } else {
                        afternoon.append(";0");
                    }
                }


            }


            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + allReg + "<>" + morning + "<>" + afternoon);
            storageFile sentData = new serverProcess().requestProcess(storageObj);

            String text = sentData.getStrData();

            return text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
//            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
//            Tools.toast("No achievements found", AttendanceActivity.this, R.color.yellow_600);
            try {
                if (text.equals("success")) {
                    Tools.toast("Successfully marked", AttendanceActivity.this, R.color.green_800);
                    finish();
                } else {
                    Tools.toast("An error occurred. Check your connection and try again", AttendanceActivity.this, R.color.red_600);
                }
            } catch (Exception ex) {
            }
        }
    }

    private class publicHolidays extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("takeattendance");
            String allReg = "", morning = "", afternoon = "";
            for (int i = 0; i < regNo.size(); i++) {
                if (allReg.isEmpty()) {
                    allReg = regNo.get(i).toString();
                } else {
                    allReg += ";" + regNo.get(i).toString();
                }

                if (morning.isEmpty()) {
                    morning = "2";
                } else {
                    morning += ";2";
                }

                if (afternoon.isEmpty()) {
                    afternoon = "2";
                } else {
                    afternoon += ";2";
                }
            }


            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3] + "<>" + allReg + "<>" + morning + "<>" + afternoon);
            storageFile sentData = new serverProcess().requestProcess(storageObj);

            String text = sentData.getStrData();

            return text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
//            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            try {
                if (text.equals("success")) {
//                    Toast.makeText(getApplicationContext(), "Successfully marked", Toast.LENGTH_SHORT).show();
                    Tools.toast("Successfully marked", AttendanceActivity.this, R.color.green_800);
                    finish();
                } else {
                    Tools.toast("An error occurred. Check your connection and try again", AttendanceActivity.this, R.color.red_600);
                }
            } catch (Exception ex) {

            }

        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setMessage("Cancel without saving attendance?")
                .setCancelable(true)
                .setPositiveButton("Yes, cancel ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }
}

