package mountedwings.org.mskola_mgt.parent;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

public class ResultActivity extends AppCompatActivity {
    private NestedScrollView nested_scroll_view;
    private ImageButton bt_toggle_student_info, bt_toggle_result, bt_toggle_result_summary, bt_toggle_result_keys, bt_toggle_psychomotor;
    private View lyt_expand_student_info, lyt_expand_result_summary, lyt_expand_keys, lyt_expand_psychomotor;
    private TextView school_name, school_address, students_info, keys_tv, pschomotor_tv, result_summary_tv, students_details;
    private CircularImageView school_logo;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    private ViewGroup result;
    private LinearLayout result_layout;
    private String location = "Mbayande Gboko", email = "cac@gmail.com", website = "www.livingseed.org", full_address = "You know my fulll address dude", calendar = "Resumption = 5th January, 2019; Mid term = NIL; Vacation = We shall post that soon";

//    private String location, email, website, full_address, calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_result);
        initComponents();
    }

    private void initComponents() {
        // nested scrollview
        nested_scroll_view = findViewById(R.id.nested_content);
        result_layout = findViewById(R.id.result);

//        result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        result.addView(new TableMainLayoutOriginal(this));
        result_layout.addView(new TableMainLayoutOriginal(this));
        school_name = findViewById(R.id.school_name);
        school_name.setText("Calvary Arrows College");
        school_address = findViewById(R.id.address);
        school_address.setText(full_address);

        //TODO: we display the uncertain text first before the certain text

        // students textView header
        students_info = findViewById(R.id.students_info);
        // students textView details
        students_details = findViewById(R.id.students_details);

        // section description
        bt_toggle_student_info = findViewById(R.id.bt_toggle_student_info);
        lyt_expand_student_info = findViewById(R.id.lyt_expand_student_info);
        bt_toggle_student_info.setOnClickListener(view -> toggleSection(view, lyt_expand_student_info));

        // expand first description
        toggleArrow(bt_toggle_student_info);
        lyt_expand_student_info.setVisibility(View.VISIBLE);

        // section result summary
        bt_toggle_result_summary = findViewById(R.id.bt_toggle_result_summary);
        lyt_expand_result_summary = findViewById(R.id.lyt_expand_result_summary);
        bt_toggle_result_summary.setOnClickListener(view -> toggleSection(view, lyt_expand_result_summary));

        // section keys
        bt_toggle_result_keys = findViewById(R.id.bt_toggle_keys);
        lyt_expand_keys = findViewById(R.id.lyt_expand_keys);
        bt_toggle_result_keys.setOnClickListener(view -> toggleSection(view, lyt_expand_keys));

        // section psychomotor
        bt_toggle_psychomotor = findViewById(R.id.bt_toggle_psychomotor);
        lyt_expand_psychomotor = findViewById(R.id.lyt_expand_psychomotor);
        bt_toggle_psychomotor.setOnClickListener(view -> toggleSection(view, lyt_expand_psychomotor));


        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        // result section description
        bt_toggle_result = findViewById(R.id.bt_toggle_result);
//        bt_toggle_result.setOnClickListener(view -> toggleSection1(view, new TableMainLayoutOriginal(this)));
        bt_toggle_result.setOnClickListener(view -> {
            toggleSection1(view);
            if (result_layout.getVisibility() == View.VISIBLE)
                result_layout.setVisibility(View.GONE);
            else
                result_layout.setVisibility(View.VISIBLE);
        });

    }

    private void toggleSection(View bt, final View lyt) {
        boolean show = toggleArrow(bt);
        if (show) {
            ViewAnimation.expand(lyt, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt);
                }
            });
        } else {
            ViewAnimation.collapse(lyt);
        }
    }

    private void toggleSection1(View bt) {
        toggleArrow(bt);
    }

    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    public void location(View v) {
        if (location.isEmpty()) {
//            mBottomSheetDialog.dismiss();
            Tools.toast("No location provided", ResultActivity.this);
        } else {
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.sheet_basic, null);
            ((TextView) view.findViewById(R.id.name)).setText("Location");
            ((TextView) view.findViewById(R.id.address)).setText(location);
            (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.dismiss();
                }
            });

            (view.findViewById(R.id.bt_details)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //copy code
                    Tools.copyToClipboard(getApplicationContext(), ((TextView) view.findViewById(R.id.address)).getText().toString().trim());
                }
            });

            mBottomSheetDialog = new BottomSheetDialog(this);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            mBottomSheetDialog.show();
            mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mBottomSheetDialog = null;
                }
            });
        }
    }

    public void website(View v) {

        if (website.isEmpty()) {
//            mBottomSheetDialog.dismiss();
            Tools.toast("No Website provided", ResultActivity.this);
        } else {


            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            final View view = getLayoutInflater().inflate(R.layout.sheet_basic, null);
            ((TextView) view.findViewById(R.id.name)).setText("Website");
            ((TextView) view.findViewById(R.id.address)).setText(website);
            (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.dismiss();
                }
            });

            (view.findViewById(R.id.bt_details)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //copy code
                    Tools.copyToClipboard(getApplicationContext(), ((TextView) view.findViewById(R.id.address)).getText().toString().trim());
                }
            });

            mBottomSheetDialog = new BottomSheetDialog(this);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            mBottomSheetDialog.show();
            mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mBottomSheetDialog = null;
                }
            });
        }
    }

    public void email(View v) {
        if (location.isEmpty()) {
//            mBottomSheetDialog.dismiss();
            Tools.toast("No email provided", ResultActivity.this);
        } else {
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.sheet_basic, null);
            ((TextView) view.findViewById(R.id.name)).setText("Email");
            ((TextView) view.findViewById(R.id.address)).setText(email);
            (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.dismiss();
                }
            });

            (view.findViewById(R.id.bt_details)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //copy code
                    Tools.copyToClipboard(getApplicationContext(), ((TextView) view.findViewById(R.id.address)).getText().toString().trim());
                }
            });

            mBottomSheetDialog = new BottomSheetDialog(this);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            mBottomSheetDialog.show();
            mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mBottomSheetDialog = null;
                }
            });
        }

    }

    public void contact(View v) {

        if (full_address.isEmpty()) {
//            mBottomSheetDialog.dismiss();
            Tools.toast("No contact/address provided", ResultActivity.this);
        } else {


            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            final View view = getLayoutInflater().inflate(R.layout.sheet_basic, null);
            ((TextView) view.findViewById(R.id.name)).setText("Full Contact");
            ((TextView) view.findViewById(R.id.address)).setText(full_address);
            (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.dismiss();
                }
            });

            (view.findViewById(R.id.bt_details)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //copy code
                    Tools.copyToClipboard(getApplicationContext(), ((TextView) view.findViewById(R.id.address)).getText().toString().trim());
                }
            });

            mBottomSheetDialog = new BottomSheetDialog(this);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            mBottomSheetDialog.show();
            mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mBottomSheetDialog = null;
                }
            });
        }

    }

    public void calendar(View v) {

        if (calendar.isEmpty()) {
//            mBottomSheetDialog.dismiss();
            Tools.toast("School Calendar not published yet!", ResultActivity.this);
        } else {


            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }

            final View view = getLayoutInflater().inflate(R.layout.sheet_basic, null);
            ((TextView) view.findViewById(R.id.name)).setText("School Calendar");
            ((TextView) view.findViewById(R.id.address)).setText(calendar);
            (view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.dismiss();
                }
            });

            (view.findViewById(R.id.bt_details)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //copy code
                    Tools.copyToClipboard(getApplicationContext(), ((TextView) view.findViewById(R.id.address)).getText().toString().trim());
                }
            });

            mBottomSheetDialog = new BottomSheetDialog(this);
            mBottomSheetDialog.setContentView(view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            mBottomSheetDialog.show();
            mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mBottomSheetDialog = null;
                }
            });
        }
    }
}