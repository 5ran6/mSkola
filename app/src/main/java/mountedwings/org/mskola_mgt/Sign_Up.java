package mountedwings.org.mskola_mgt;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import mountedwings.org.mskola_mgt.model.People;
import mountedwings.org.mskola_mgt.utils.ImagePicker;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

import static android.support.v4.graphics.TypefaceCompatUtil.getTempFile;


public class Sign_Up extends AppCompatActivity {
    private boolean isSuccess = true;
    private Toolbar toolbar;
    private CoordinatorLayout parent_layout;
    private People newbie = new People("Agom", false);
    private TextInputLayout firstName, lastName, phone, email, town, password1, password2;
    private AppCompatEditText fName, lName, phoneE, emailE, townE, pass1, pass2;
    private ImageView passport;
    private boolean isFilled = false;
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        town = findViewById(R.id.town);
        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
        passport = findViewById(R.id.photo);

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
            if (isFilled) {
                loadingAndDisplayContent();
            }
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
        isFilled = true;
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

    private void showCustomDialogFailure() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
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
        //init
        final int LOADING_DURATION = 3500;

        final LinearLayout lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
        lyt_progress.setVisibility(View.VISIBLE);
        lyt_progress.setAlpha(1.0f);
        parent_layout.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewAnimation.fadeOut(lyt_progress);
            }
        }, LOADING_DURATION);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isSuccess) {
                    newbie.image = R.drawable.avatar;
                    showCustomDialogSuccess(newbie);
                } else {
                    showCustomDialogFailure();
                }
            }
        }, LOADING_DURATION + 400);
    }

    public void getPhoto(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
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
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // TODO use bitmap
                passport.setImageBitmap(bitmap);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
