package mountedwings.org.mskola_mgt;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;


public class SchoolID_Login extends AppCompatActivity {
    private TextInputEditText school_id;
    private String role;
    private TextView verifying;
    private ProgressBar checking;
    private SharedPreferences mPrefs;
    private SharedPreferences mPrefsSchoolID;

    private Spinner recentIds;
    private BroadcastReceiver mReceiver;
    private int w = 0, status;
    private AppCompatButton cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        role = intent.getStringExtra("account_type");
        mPrefs = getSharedPreferences(myPref, 0);
        mPrefsSchoolID = getSharedPreferences("schoolIDs", 0);

        setContentView(R.layout.activity_shool_id_login);
        school_id = findViewById(R.id.school_id);
        verifying = findViewById(R.id.verifying);
        checking = findViewById(R.id.checking);
        recentIds = findViewById(R.id.recent_ids);
        cont = findViewById(R.id.continued);
        recentIds.setVisibility(View.GONE);
        if (!mPrefsSchoolID.getString("school_id", "").isEmpty()) {
            String schoolIds = mPrefsSchoolID.getString("school_id", "");

            String[] schools = schoolIds.split(",");
            String[] data = new String[(schools.length + 1)];
            data[0] = "Recent School IDs.....";
            for (int i = 1; i <= schools.length; i++) {
                data[i] = schools[(i - 1)];
            }

            ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<>(getBaseContext(), R.layout.spinner_item, data);
            spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            recentIds.setAdapter(spinnerAdapter1);

            //setVisibility
            recentIds.setVisibility(View.VISIBLE);
            ImageView overlay = findViewById(R.id.overlay);
            overlay.setVisibility(View.VISIBLE);
        }

        recentIds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (recentIds.getSelectedItemPosition() > 0)
                    school_id.setText(recentIds.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        school_id.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard();
                if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
                    verifyID();
                handled = true;
            }
            return handled;
        });
    }

    //DONE
    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {

            Tools.toast(item.getTitle().toString(), SchoolID_Login.this, R.color.green_800);
        }
        return super.onOptionsItemSelected(item);
    }

    public void continued(View view) {
        // check if the ID is valid
        if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED)
            verifyID();
        else
            Tools.toast(getResources().getString(R.string.no_internet_connection), this, R.color.red_700);

    }

    private void verifyID() {
        //check if the field is empty first
        if (!school_id.getText().toString().isEmpty() && status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            //validate from server
            checking.setVisibility(View.VISIBLE);
            //animate textView
            final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
            animation.setDuration(2000); // duration - 2 seconds
            animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
            animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
            verifying.startAnimation(animation);
            cont.setEnabled(false);
            cont.setTextColor(getResources().getColor(R.color.grey_3));
            new verifySchoolID().execute(school_id.getText().toString().trim());
        } else {
            Tools.toast("Fill in School ID", SchoolID_Login.this, R.color.yellow_800);
        }
    }

    public class verifySchoolID extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            //  Boolean success = false;
            storageFile storageObj = new storageFile();
            storageObj.setOperation("verifyid");
            storageObj.setStrData(strings[0]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            String text = sentData.getStrData();
            Log.d("mSkola", text);
            return text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            checking.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            checking.setVisibility(View.INVISIBLE);
            verifying.startAnimation(new Animation() {
                @Override
                public void cancel() {
                    super.cancel();
                    verifying.setVisibility(View.INVISIBLE);
                }
            });

            switch (text) {
                case "found":
                    //save school_id to sharedPrefs
                    //check if there's already a sheared pref, create or edit

                    SharedPreferences.Editor editor = mPrefs.edit();
                    SharedPreferences.Editor editorSchoolID = mPrefsSchoolID.edit();

                    editor.putString("school_id", school_id.getText().toString().trim());
                    //get School ID text
                    //append with a ,
                    String old_schoolID = mPrefsSchoolID.getString("school_id", "");
                    if (old_schoolID.isEmpty()) {
                        editorSchoolID.putString("school_id", school_id.getText().toString().trim());
                    } else {
                        if (!old_schoolID.contains(school_id.getText().toString().trim())) {
                            editorSchoolID.putString("school_id", old_schoolID + "," + school_id.getText().toString().trim());
                        }
                    }
                    editor.apply();
                    editorSchoolID.apply();

                    //intent
                    Intent intent = new Intent(getApplicationContext(), MskolaLogin.class);
                    intent.putExtra("account_type", role);
                    intent.putExtra("school_id", school_id.getText().toString().trim());
                    startActivity(intent);

                    break;
                case "not found":
                    cont.setEnabled(true);
                    showCustomDialogFailure("The school ID you provided does not exist, please check the ID and try again.");
                    break;
                default:
                    cont.setEnabled(true);
                    showCustomDialogFailure("An error occurred, try again later.");
                    break;
            }
        }

    }

    private void showCustomDialogFailure(String error) {

        //progress bar and text will disappear

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

        dialog.findViewById(R.id.bt_close).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setAttributes(lp);
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
                            Tools.toast("Back Online! Try again", SchoolID_Login.this, R.color.green_800);
                        else
                            verifyID();

                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Tools.toast(getResources().getString(R.string.no_internet_connection), SchoolID_Login.this, R.color.red_700);
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
        finish();
    }
}