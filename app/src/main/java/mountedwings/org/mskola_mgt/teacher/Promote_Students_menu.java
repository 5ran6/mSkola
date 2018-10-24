package mountedwings.org.mskola_mgt.teacher;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Collections;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.SchoolID_Login;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Promote_Students_menu extends AppCompatActivity {
    private String school_id = "", staff_id = "", class_name = "", arm = "", session;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private Spinner select_class, select_arm, select_session;
    private ProgressBar progressBar1, progressBar2, progressBar3;
    private int counter = 0;
    private ArrayList studentsName = new ArrayList();
    private ArrayList regNos = new ArrayList();
    private String[] classes;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE).toString() != null) {
            mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);
            staff_id = mPrefs.getString("staff_id", getIntent().getStringExtra("email_address"));
            school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));
        } else {
            Toast.makeText(getApplicationContext(), "Previous Login invalidated. Login again!", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(getApplicationContext(), SchoolID_Login.class).putExtra("account_type", "Teacher"));
        }
        setContentView(R.layout.activity_promote_students_menu);

        TextView load = findViewById(R.id.load);
        select_arm = findViewById(R.id.select_arm);

        select_class = findViewById(R.id.select_class);

        select_session = findViewById(R.id.select_session);

        progressBar1 = findViewById(R.id.progress1);
        progressBar1.setVisibility(View.VISIBLE);

        progressBar2 = findViewById(R.id.progress2);
        progressBar2.setVisibility(View.INVISIBLE);

        progressBar3 = findViewById(R.id.progress3);
        progressBar3.setVisibility(View.INVISIBLE);

        //load classes and assessments
        new initialLoad().execute(school_id, staff_id);

        select_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_class.getSelectedItemPosition() >= 0) {
                    class_name = select_class.getSelectedItem().toString();
                    counter++;
                    //run new thread
                    if (counter >= 1)
                        loadArm();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        select_arm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_arm.getSelectedItemPosition() >= 0) {
                    arm = select_arm.getSelectedItem().toString();
                    //run new thread

                    counter++;
                    //run new thread
                    if (counter >= 1)
                        loadSessionList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        select_session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_session.getSelectedItemPosition() >= 0) {
                    session = select_session.getSelectedItem().toString();
                    //run new thread

                    counter++;
                    //run NO new thread
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        load.setOnClickListener(v ->

        {
            if (!class_name.isEmpty() || !arm.isEmpty()) {
                //TODO: Loading progressBar
                new getStudentsToPromote().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Fill all necessary fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSessionList() {
        progressBar3.setVisibility(View.VISIBLE);
        new loadSession().execute();
    }


    private void loadArm() {
        progressBar2.setVisibility(View.VISIBLE);
        new loadArms().execute(school_id, class_name);
    }


    private class loadArms extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getpmsarms");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
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
            if (!text.equals("0") && !text.isEmpty()) {
                String[] dataRows = text.split("<>")[0].split(",");
                String[] data = new String[(dataRows.length + 1)];
                data[0] = "";
                for (int i = 1; i <= dataRows.length; i++) {
                    data[i] = dataRows[(i - 1)];
                }
                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_arm.setAdapter(spinnerAdapter1);
                arm = select_arm.getSelectedItem().toString();
                progressBar2.setVisibility(View.INVISIBLE);
                counter = -1;
            } else {
                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, Collections.emptyList());
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_arm.setAdapter(spinnerAdapter1);
                progressBar2.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    //DONE
    private class loadSession extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getprsessions");
            storageObj.setStrData(school_id);
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
            if (!text.equals("0") && !text.equals("")) {
                String dataRows[] = text.split("<>")[0].split(";");
                String[] data = new String[(dataRows.length + 1)];
                data[0] = "";
                for (int i = 1; i <= dataRows.length; i++) {
                    data[i] = dataRows[(i - 1)];
                }

                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_session.setAdapter(spinnerAdapter1);
                session = select_session.getSelectedItem().toString();
                progressBar3.setVisibility(View.INVISIBLE);
                counter = -1;
            } else {
                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Collections.emptyList());
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_session.setAdapter(spinnerAdapter1);
                progressBar3.setVisibility(View.INVISIBLE);
                showCustomDialogFailure("An error occurred. No Session found. Contact Admin");
            }

        }

        private void showCustomDialogFailure(String error) {
            final Dialog dialog = new Dialog(getApplicationContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.dialog_error);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            TextView error_message = dialog.findViewById(R.id.content);
            error_message.setText(error);

            (dialog.findViewById(R.id.bt_close)).setOnClickListener(v -> {
                dialog.dismiss();
                try {
//                if (lyt_progress.getVisibility() == View.VISIBLE && parent_layout.getVisibility() == View.INVISIBLE) {
//                    lyt_progress.setVisibility(View.INVISIBLE);
//                    parent_layout.setVisibility(View.VISIBLE);
//                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    private class getStudentsToPromote extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getpmstudents");
            storageObj.setStrData(school_id + "<>" + class_name + "<>" + arm + "<>" + session);
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
            if (!text.equals("0") && !text.equals("")) {

                classes = text.split("##")[1].split(";");
                text = text.split("##")[0];


                if (!text.equals("_")) {
                    String rows[] = text.split("<>");
                    for (int i = 0; i < rows.length; i++) {

                        studentsName.add(rows[i].split(";")[1]);
                        regNos.add(rows[i].split(";")[0]);

//                        myClass.pmStudentsDisplay.getItems().add(rows[i].split(";")[1]);
//                        myClass.pmRegs.add(rows[i].split(";")[0]);

                    }
                }

            } else {
                showCustomDialogFailure("An error occurred. No Session found. Contact Admin");
            }
            //finally

            intent = new Intent(getApplicationContext(), PromotionStudents.class);
            intent.putExtra("students_names", studentsName);
            intent.putExtra("reg_nos", regNos);

            intent.putExtra("arm", arm);
            intent.putExtra("session", session);

            intent.putExtra("classes", class_name);

            startActivity(intent);
            //finish();
        }

        private void showCustomDialogFailure(String error) {
            final Dialog dialog = new Dialog(getApplicationContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog.setContentView(R.layout.dialog_error);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            TextView error_message = dialog.findViewById(R.id.content);
            error_message.setText(error);

            (dialog.findViewById(R.id.bt_close)).setOnClickListener(v -> {
                dialog.dismiss();
                try {
//                if (lyt_progress.getVisibility() == View.VISIBLE && parent_layout.getVisibility() == View.INVISIBLE) {
//                    lyt_progress.setVisibility(View.INVISIBLE);
//                    parent_layout.setVisibility(View.VISIBLE);
//                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    //loads Classes
    private class initialLoad extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getpsclass");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
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
            System.out.println(text);

            if (!text.equals("0") && !text.isEmpty()) {
                text = text.split("##")[0];
                String dataRows[] = text.split("<>");

                String[] data = new String[(dataRows.length + 1)];
                data[0] = "";
                for (int i = 1; i <= dataRows.length; i++) {
                    data[i] = dataRows[(i - 1)];
                }

                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_class.setAdapter(spinnerAdapter1);
                class_name = select_class.getSelectedItem().toString();

                progressBar1.setVisibility(View.INVISIBLE);
                counter = -1;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}