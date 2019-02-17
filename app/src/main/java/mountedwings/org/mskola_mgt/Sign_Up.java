/*
 * Copyright 2019 Mountedwings Cybersystems LTD. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mountedwings.org.mskola_mgt;

import android.annotation.SuppressLint;
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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import mountedwings.org.mskola_mgt.model.People;
import mountedwings.org.mskola_mgt.utils.ImagePicker;
import mountedwings.org.mskola_mgt.utils.NetworkUtil;
import mountedwings.org.mskola_mgt.utils.Tools;

import static android.Manifest.permission.CAMERA;


public class Sign_Up extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 200;
    public String TAG = "mSkola", error = "Error";
    boolean isSuccess = false;
    private Toolbar toolbar;
    private CoordinatorLayout parent_layout;
    private People newbie;
    boolean isSent = false;
    Boolean network, newPassport = false;
    private static final int PICK_IMAGE_ID_CAMERA = 334; // the number doesn't matter

    private TextInputLayout firstName, lastName, phone, email, town, password1, password2;
    private Spinner country;
    private AppCompatEditText fName, lName, phoneE, emailE, townE, pass1, pass2;
    private ImageView passport;
    private boolean isFilled = false;
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    private int status;
    private String[] separatedValues;
    private View view;
    private String f, l, c = "Nigeria", t, p, a, e, pass, account_type;
    private BroadcastReceiver mReceiver;
    private int w = 0;

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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(null);
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
        if (Objects.requireNonNull(emailE.getText()).toString().trim().isEmpty()) {
            email.setError(getString(R.string.err_msg_email));
            requestFocus(emailE);
            return false;
        }
        if (!EmailValidator.getInstance().isValid(emailE.getText().toString().trim())) {
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
        if (Objects.requireNonNull(pass2.getText()).toString().trim().isEmpty()) {
            password2.setError(getString(R.string.err_msg_pass2));
            requestFocus(pass2);
            return false;
        } else {
            password2.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        if (!Objects.requireNonNull(pass2.getText()).toString().equals(Objects.requireNonNull(pass1.getText()).toString())) {
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
        ((CircularImageView) dialog.findViewById(R.id.image)).setImageResource(p.image);

        dialog.findViewById(R.id.bt_follow).setOnClickListener(new View.OnClickListener() {
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
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
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

    public void getPhoto(View v) {
        //show dialog for selection
        BottomSheetBehavior mBehavior;
        BottomSheetDialog[] mBottomSheetDialog = new BottomSheetDialog[1];

        View bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        //show bottom sheet
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.sign_up_sheet, null);
        (view.findViewById(R.id.bt_camera)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mBottomSheetDialog[0].dismiss();


                if (checkPermission()) {
                    mBottomSheetDialog[0].dismiss();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, PICK_IMAGE_ID_CAMERA);
                } else {
                    requestPermission();
                }


            }
        });

        (view.findViewById(R.id.bt_gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getApplicationContext());
                if (checkPermission()) {
                    mBottomSheetDialog[0].dismiss();
                    startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
                } else {
                    requestPermission();
                }
            }
        });

        mBottomSheetDialog[0] = new BottomSheetDialog(this);
        mBottomSheetDialog[0].setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog[0].getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog[0].show();
        mBottomSheetDialog[0].setOnDismissListener(dialog -> mBottomSheetDialog[0] = null);


    }

    private void doRegister() {
        final LinearLayout lyt_progress = findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
        parent_layout.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        new register().execute();

    }

    private void stopBouncing() {
        final LinearLayout lyt_progress = findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.GONE);
        parent_layout.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);

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
                    passport.setImageBitmap(bitmap);
                    newPassport = true;
                }
                break;
            case PICK_IMAGE_ID_CAMERA:
                if (data != null) {
                    Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
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
                //   Toast.makeText(getApplicationContext(), "Connected to the internet", Toast.LENGTH_LONG).show();
                //inflate the data on the spinner
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, seperatedValue);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                country.setAdapter(spinnerAdapter);
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
                w++;
                status = NetworkUtil.getConnectivityStatusString(context);
                Log.i(TAG, "receiver checking");
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction()) && w > 1) {
                    if (status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                        Tools.toast("Yes Internet connection!", Sign_Up.this, R.color.green_800);
                        //    new ResumeForceExitPause(context).execute();
                        new performInBackground().execute("getcountries");
                    } else {
                        Tools.toast("No Internet connection!", Sign_Up.this, R.color.red_500);
                    }
                    //      new ForceExitPause(context).execute();
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
            try {

                storageFile storageObj = new storageFile();
                //to get and send the picture
                @SuppressLint("WrongThread") Bitmap bitmap = ((BitmapDrawable) passport.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imageInByte = baos.toByteArray();

                storageObj.setOperation("createaccount");
                storageObj.setStrData(Tools.personInfo(f, l, c, t, p, a, e, pass));
                storageObj.addImageFile(imageInByte);
                storageFile sentData = new serverProcess().requestProcess(storageObj);

                //received from server
                String text = sentData.getStrData();
                if (text.contains("success")) {
                    isSuccess = true;
                } else if (text.contains("exists")) {
                    isSuccess = false;
                    error = "mSkola account with this email address already exists.";
                } else {
                    isSuccess = false;
                    error = "Oops. an error occurred!";
                }
            } catch (Exception e) {
                network = false;
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
            if (network) {
                if (isSuccess) {
                    showCustomDialogSuccess(newbie);
                } else {
                    showCustomDialogFailure(error);
                }

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


