package mountedwings.org.mskola_mgt.teacher;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.Collections;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.SchoolID_Login;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Compile_Result_menu extends AppCompatActivity {
    private String school_id, staff_id, class_name = "", arm = "";
    private Spinner select_class, select_arm;
    private ProgressBar progressBar1, progressBar2, progressBar;
    private int counter = 0;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor editor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private TextView load;
    private MaterialRippleLayout loadB;
    private ViewGroup transitionsContainer;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;
    private boolean compiling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE).toString() != null) {
            mPrefs = getSharedPreferences(myPref, PREFERENCE_MODE_PRIVATE);
            staff_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
            school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

        } else {
            Tools.toast("Previous Login invalidated. Login again!", this, R.color.red_500);
            finish();
            startActivity(new Intent(getBaseContext(), SchoolID_Login.class).putExtra("account_type", "Teacher"));
        }
        setContentView(R.layout.activity_compile_result_menu);
        load = findViewById(R.id.load);
        select_arm = findViewById(R.id.select_arm);
        select_class = findViewById(R.id.select_class);
        progressBar = findViewById(R.id.progress);
        progressBar1 = findViewById(R.id.progress1);
        progressBar1.setVisibility(View.VISIBLE);

        progressBar2 = findViewById(R.id.progress2);
        progressBar2.setVisibility(View.INVISIBLE);
        loadB = findViewById(R.id.loading);

        transitionsContainer = findViewById(R.id.parent);

        select_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        select_arm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (select_arm.getSelectedItemPosition() >= 0) {
                    arm = select_arm.getSelectedItem().toString();
                    //run new thread

                    counter++;
                    //run NO new thread

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        load.setOnClickListener(v -> {
            if (!class_name.isEmpty() && !arm.isEmpty()) {
                if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
                    new compileResult().execute(school_id, class_name, arm);
                else
                    Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);
            } else {
                Tools.toast("Fill all necessary fields!", this, R.color.yellow_600);

            }
        });
    }


    private void loadArm() {
        progressBar2.setVisibility(View.VISIBLE);
        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
            new loadArms().execute(school_id, staff_id, class_name);
    }


    private class loadArms extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getpsarm");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
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
                String[] dataRows = text.split(",");
                String[] data = new String[(dataRows.length + 1)];
                data[0] = "";
                for (int i = 1; i <= dataRows.length; i++) {
                    data[i] = dataRows[(i - 1)];
                }

                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_arm.setAdapter(spinnerAdapter1);
                arm = select_arm.getSelectedItem().toString();
                progressBar2.setVisibility(View.INVISIBLE);
                counter = -1;
            } else {
                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, Collections.emptyList());
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

    private class compileResult extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("compileresult");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            compiling = true;
            return sentData.getStrData();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            TransitionManager.beginDelayedTransition(transitionsContainer);
            progressBar.setVisibility(View.VISIBLE);
            Tools.toast("Compiling......", Compile_Result_menu.this, R.color.green_600);
            loadB.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            progressBar.setVisibility(View.GONE);
            compiling = false;
            if (text.equals("success")) {
                //    Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT).show();
                showCustomDialogSuccess("Results successfully compiled");
            } else if (text.equals("not found")) {
                showCustomDialogFailure("Dashboard not found in the selected class");
            } else {
                showCustomDialogFailure("An error occurred");
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    private void showCustomDialogSuccess(String msg) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_success);
        TextView textView = dialog.findViewById(R.id.title);
        textView.setText(msg);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getBaseContext(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showCustomDialogFailure(String error) {
        final Dialog dialog = new Dialog(this);
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
            loadB.setVisibility(View.VISIBLE);

        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
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

                ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, data);
                spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_class.setAdapter(spinnerAdapter1);
                class_name = select_class.getSelectedItem().toString();

                progressBar1.setVisibility(View.INVISIBLE);
                counter = -1;
            } else {
                Tools.toast("Either you're not a CLASS TEACHER or you have to " + getResources().getString(R.string.no_internet_connection), Compile_Result_menu.this, R.color.red_800, Toast.LENGTH_LONG);
                load.setVisibility(View.INVISIBLE);

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
                            Tools.toast("Back Online! Try again", Compile_Result_menu.this, R.color.green_800);
                        else
                            new initialLoad().execute(school_id, staff_id);
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), Compile_Result_menu.this, R.color.red_500);
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

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        //stop the thread or don't go anywhere until the reply is given
        if (compiling) {
            if ((System.currentTimeMillis() - exitTime) > 100) {
                Tools.toast("Result still compiling", Compile_Result_menu.this);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }

        }
    }
}
