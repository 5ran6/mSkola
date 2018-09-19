package mountedwings.org.mskola_mgt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.io.ByteArrayOutputStream;
import java.net.NoRouteToHostException;
import java.util.Objects;

import static android.Manifest.permission.CAMERA;

import de.hdodenhof.circleimageview.CircleImageView;
import mountedwings.org.mskola_mgt.model.People;
import mountedwings.org.mskola_mgt.utils.ImagePicker;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;


public class Sign_Up extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    private String TAG = "mSkola", error = "Error";
    boolean isSuccess = false;
    private Toolbar toolbar;
    private CoordinatorLayout parent_layout;
    private People newbie;
    boolean isSent = false;
    Boolean network, newPassport = false;

    private TextInputLayout firstName, lastName, phone, email, town, password1, password2;
    private Spinner country;
    private AppCompatEditText fName, lName, phoneE, emailE, townE, pass1, pass2;
    private ImageView passport;
    private boolean isFilled = false;
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    private String[] separatedValues;
    private View view;
    private String f, l, c, t, p, a, e, pass, account_type;
    BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent intent = getIntent();
        account_type = intent.getStringExtra("account_type");


        //        view = findViewById(android)
        parent_layout = findViewById(R.id.parent_layout);
        initToolbar();
        initUI();


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.clearSystemBarLight(this);
    }

    private void initUI() {
        country = findViewById(R.id.spn_country);

        //start thread
        new performInBackground().execute("getcountries");

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        town = findViewById(R.id.town);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        passport = findViewById(R.id.photo);
        passport.setImageResource(R.drawable.user3);

        fName = findViewById(R.id.fname);
        lName = findViewById(R.id.lname);
        phoneE = findViewById(R.id.phoneE);
        emailE = findViewById(R.id.emailE);
        townE = findViewById(R.id.townE);
        pass1 = findViewById(R.id.pass1);
        pass2 = findViewById(R.id.pass2);

        fName.addTextChangedListener(new MyTextWatcher(fName));
        lName.addTextChangedListener(new MyTextWatcher(lName));
        phoneE.addTextChangedListener(new MyTextWatcher(phoneE));
        emailE.addTextChangedListener(new MyTextWatcher(emailE));
        townE.addTextChangedListener(new MyTextWatcher(townE));
        pass1.addTextChangedListener(new MyTextWatcher(pass1));
        pass2.addTextChangedListener(new MyTextWatcher(pass2));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            submitForm();
            //showCustomDialogSuccess(newbie);
            //Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitForm() {
        if (!validateFirstName()) {
            return;
        }
        if (!validateLastName()) {
            return;
        }

        if (!validatePhone()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validateTown()) {
            return;
        }
        if (!validatePassword1()) {
            return;
        }
        if (!validatePassword2()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }
//        isFilled = true;
        f = fName.getText().toString();
        l = lName.getText().toString();
        p = phoneE.getText().toString();
        e = emailE.getText().toString();
        t = townE.getText().toString();
        c = country.getSelectedItem().toString();
        pass = pass1.getText().toString();
        a = account_type;
        newbie = new People(f + " " + l, false);
        newbie.image = R.drawable.checked;
        loadingAndDisplayContent();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateFirstName() {
        if (fName.getText().toString().trim().isEmpty()) {
            firstName.setError(getString(R.string.err_msg_fname));
            requestFocus(fName);
            return false;
        } else {
            firstName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLastName() {
        if (lName.getText().toString().trim().isEmpty()) {
            lastName.setError(getString(R.string.err_msg_lname));
            requestFocus(lName);
            return false;
        } else {
            lastName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePhone() {
        if (phoneE.getText().toString().trim().isEmpty()) {
            phone.setError(getString(R.string.err_msg_phone));
            requestFocus(phoneE);
            return false;
        } else {
            phone.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        if (emailE.getText().toString().trim().isEmpty()) {
            email.setError(getString(R.string.err_msg_email));
            requestFocus(emailE);
            return false;
        } else {
            email.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateTown() {
        if (townE.getText().toString().trim().isEmpty()) {
            town.setError(getString(R.string.err_msg_town));
            requestFocus(townE);
            return false;
        } else {
            town.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword1() {
        if (pass1.getText().toString().trim().isEmpty()) {
            password1.setError(getString(R.string.err_msg_pass1));
            requestFocus(pass1);
            return false;
        } else {
            password1.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword2() {
        if (pass2.getText().toString().trim().isEmpty()) {
            password2.setError(getString(R.string.err_msg_pass2));
            requestFocus(pass2);
            return false;
        } else {
            password2.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (!pass2.getText().toString().equals(pass1.getText().toString())) {
            password2.setError(getString(R.string.err_msg_pass_mismatch));
            requestFocus(pass2);
            return false;
        }
        return true;
    }

    private void showCustomDialogSuccess(People p) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_light);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.title)).setText(p.name);
        ((CircleImageView) dialog.findViewById(R.id.image)).setImageResource(p.image);

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_follow)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        dialog.setContentView(R.layout.dialog_warning);
        TextView errorTextView = dialog.findViewById(R.id.title);
        errorTextView.setText(error);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                parent_layout.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void loadingAndDisplayContent() {

        if (!newPassport) {
            showDialog(this, "Seems you didn't set a passport", "Are you sure you want to use the default passport?");
        } else {
            doRegister();
        }
    }

    public void getPhoto(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        if (checkPermission()) {
            startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);

        } else {
            requestPermission();
        }
    }

    private void doRegister() {
        final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
        parent_layout.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        new register().execute();

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.fname:
                    validateFirstName();
                    break;
                case R.id.lname:
                    validateLastName();
                    break;
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.phone:
                    validatePhone();
                    break;
                case R.id.town:
                    validateTown();
                    break;
                case R.id.pass1:
                    validatePassword1();
                    break;
                case R.id.pass2:
                    validatePassword2();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_IMAGE_ID:
                if (data != null) {
                    Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                    // TODO use bitmap
                    passport.setImageBitmap(bitmap);
                    newPassport = true;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    private boolean checkPermission() {
        // int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    //   boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (!cameraAccepted) {
                        Toast.makeText(getApplicationContext(), "You need to permit this app to use the camera", Toast.LENGTH_LONG).show();
                    } else {
                        getPhoto(view);
                    }
                }


        }
    }

    private class performInBackground extends AsyncTask<String, Integer, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation(strings[0]);
            try {
                storageFile sentData = new serverProcess().requestProcess(storageObj);
                String text = sentData.getStrData();
                //to separate by comma
                separatedValues = text.split(",");
                Log.d("mSkola", text);
                network = true;
            } catch (Exception e) {
                network = false;
            }
            return separatedValues;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String[] seperatedValue) {
            super.onPostExecute(seperatedValue);
            if (network) {
                Toast.makeText(getApplicationContext(), "Connected to the internet", Toast.LENGTH_LONG).show();
                //inflate the data on the spinner
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, seperatedValue);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                country.setAdapter(spinnerAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "Check your connection to the internet", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
    }

    @Override
    protected void onPause() {
//unregister receiver
        unregisterReceiver(this.mReceiver);
        super.onPause();
    }


    @Override
    protected void onResume() {
        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = NetworkUtil.getConnectivityStatusString(context);
                Log.e(TAG, "receiver checking");
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                    if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                        //    new ResumeForceExitPause(context).execute();
                        new performInBackground().execute("getcountries");
                    }  //      new ForceExitPause(context).execute();

                }
            }

        };

        registerReceiver(
                this.mReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
    }

    private class register extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {

            storageFile storageObj = new storageFile();
            //to get and send the picture
            Bitmap bitmap = ((BitmapDrawable) passport.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageInByte = baos.toByteArray();

            storageObj.setOperation("createaccount");
            storageObj.setStrData(Tools.personInfo(f, l, c, t, p, a, e, pass));
            storageObj.addImageFile(imageInByte);
            storageFile sentData = new serverProcess().requestProcess(storageObj);

            isSent = true;
            if (isSent) Log.d(TAG, "registeration sent to server");

            //received from server
            String text = sentData.getStrData();
            if (text.contains("success")) {
                isSuccess = true;
                Log.d(TAG, "registration successful");
            } else if (text.contains("exists")) {
                isSuccess = false;
                error = "mSkola account already exists.";
                Log.d(TAG, error);
            } else {
                isSuccess = false;
                error = "Oops. an error occurred!";
                Log.d(TAG, error);
            }
            return isSuccess;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if (isSuccess) {
                showCustomDialogSuccess(newbie);
            } else {
                showCustomDialogFailure(error);
            }
        }
    }

    public void showDialog(Activity activity, String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                passport.setImageResource(R.drawable.user3);
                dialog.dismiss();
                doRegister();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}


