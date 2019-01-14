package mountedwings.org.mskola_mgt.parent;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.ResultFooter;
import mountedwings.org.mskola_mgt.data.ResultHeader;
import mountedwings.org.mskola_mgt.data.result;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class ResultActivity extends AppCompatActivity {
    private NestedScrollView nested_scroll_view;
    private ImageButton bt_toggle_student_info, bt_toggle_result, bt_toggle_result_summary, bt_toggle_result_keys, bt_toggle_psychomotor;
    private View lyt_expand_student_info, lyt_expand_result_summary, lyt_expand_keys, lyt_expand_psychomotor;
    private TextView school_name, school_address, students_info, keys_tv, pschomotor_tv, result_summary_tv, students_details, term_result;
    private CircularImageView school_logo;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;

    private LinearLayout result_layout;
    private LinearLayout parent_layout;
    private ProgressDialog progressDialog;
    private String TAG = "mSkola", location = "Mbayande Gboko", email = "cac@gmail.com", website = "www.livingseed.org", full_address = "You know my fulll address dude", calendar = "Resumption = 5th January, 2019; Mid term = NIL; Vacation = We shall post that soon", term, school_id, session, student_reg_no, parent_id;
    private String class_arm, student_info_titles = "", student_info_values = "", no_cas;
    private ArrayList<byte[]> schoolLogo = new ArrayList<>();
    private ArrayList<String> subjects = new ArrayList<>();
    private ArrayList<String> short_codes = new ArrayList<>();
    private ArrayList<String> subjects_with_scores = new ArrayList<>();
    private ArrayList<String> class_average__highest__lowest = new ArrayList<>();
    private ArrayList<String> psychomotor_skills = new ArrayList<>();
    private ArrayList<String> psychomotor_values = new ArrayList<>();
    private ArrayList<String> grades = new ArrayList<>();
    private ArrayList<String> upper_values = new ArrayList<>();
    private ArrayList<String> lower_values = new ArrayList<>();
    private ArrayList<String> tags = new ArrayList<>();

    private ArrayList<String> CA1 = new ArrayList<>();
    private ArrayList<String> CA2 = new ArrayList<>();
    private ArrayList<String> CA3 = new ArrayList<>();
    private ArrayList<String> CA4 = new ArrayList<>();
    private ArrayList<String> CA5 = new ArrayList<>();
    private ArrayList<String> CA6 = new ArrayList<>();
    private ArrayList<String> CA7 = new ArrayList<>();
    private ArrayList<String> CA8 = new ArrayList<>();
    private ArrayList<String> CA9 = new ArrayList<>();
    private ArrayList<String> CA10 = new ArrayList<>();
    private ArrayList<String> EXAM = new ArrayList<>();
    private ArrayList<String> TOTAL = new ArrayList<>();
    private ArrayList<String> AVERAGE = new ArrayList<>();
    private ArrayList<String> HIGHEST = new ArrayList<>();
    private ArrayList<String> LOWEST = new ArrayList<>();
    private ArrayList<String> GRADE = new ArrayList<>();
    private ArrayList<String> SUBJECTS = new ArrayList<>();

    private ArrayList<String> HEADERS = new ArrayList<>();
    private ProgressBar loading;
    //    private String location, email, website, full_address, calendar, school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_result);
        initComponents();
        new getschoolinfo().execute(school_id);
    }

    //  1. loads header results (w/o term sha)
    private class getschoolinfo extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getschoolinfo");
            storageObj.setStrData(strings[0]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            schoolLogo = sentData.getImageFiles();
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.d(TAG, "Loaded round 1: " + text);

            if (!text.equals("0") && !text.isEmpty()) {
                String school_info[] = text.split("<>");
                String raw_info, raw_next_term;
                raw_info = school_info[0];
                raw_next_term = school_info[1];

                String[] info = raw_info.split(";");
                String[] next_term = raw_next_term.split(";");

                ResultHeader resultHeader = new ResultHeader();
                resultHeader.setTerm(term);
                resultHeader.setSchool_name(info[0]);
                resultHeader.setLocation(info[1]);
                resultHeader.setWebsite(info[2]);
                resultHeader.setEmail(info[3]);
                resultHeader.setContact(info[3]);
                resultHeader.setFull_address("Address: " + resultHeader.getLocation() + "; Contact: " + resultHeader.getContact());

                String resumption, mid_term, vacation;
                resumption = next_term[0];
                mid_term = next_term[1];
                vacation = next_term[2];
                resultHeader.setCalendar("Resumption Date: " + resumption + "; Mid Term Date: " + mid_term + "; Vacation Date: " + vacation);

                //school Logo
                Bitmap bitmap = BitmapFactory.decodeByteArray(schoolLogo.get(0), 0, schoolLogo.size());
                school_logo.setImageBitmap(bitmap);

                //inflate on UI
                school_name.setText(resultHeader.getSchool_name().toUpperCase());
                school_address.setText(resultHeader.getLocation());
                term_result.setText(term.toUpperCase() + " TERM RESULT");
                location = resultHeader.getLocation();
                email = resultHeader.getEmail().toLowerCase();
                website = resultHeader.getWebsite().toLowerCase();
                calendar = resultHeader.getCalendar();
                full_address = resultHeader.getFull_address();


            } else {
                Tools.toast("An error occurred", ResultActivity.this, R.color.red_800);
                finish();
            }

            //run next thread
            new getstudentinfo().execute(school_id, student_reg_no);

        }
    }


    //  2. loads student basic info
    private class getstudentinfo extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentinfo");
            storageObj.setStrData(strings[0] + "<>" + strings[1]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.d(TAG, "Loaded round 2: " + text);

            if (!text.equals("0") && !text.isEmpty() && !text.equals("0")) {
                String data[] = text.split("##");

                String raw_data1, raw_data2, raw_data3;
                raw_data1 = data[0];
                raw_data2 = data[1];
                raw_data3 = data[2];

                String[] data1 = raw_data1.split("<>");
                String[] data2 = raw_data2.split("<>");
                String[] data3 = raw_data3.split("<>");

                class_arm = data1[0].toUpperCase() + " " + data1[1].toUpperCase();
                session = data1[2];
                term = data1[3];

                for (int i = 0; i <= data2.length; i++) {
                    student_info_titles = student_info_titles + data2[i] + ": \n";
                    student_info_values = student_info_values + data3[i] + " \n";
                }
                //inflate on UI
                students_info.setText(student_info_titles);
                students_details.setText(student_info_values);

            } else {
                Tools.toast("An error occurred", ResultActivity.this, R.color.red_800);
                finish();
            }

            //run next thread
            new getsubjectsca().execute(school_id, student_reg_no, session, term); //have to be sure which format the term came in
        }
    }


    //  3. loads subjects info
    private class getsubjectsca extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getsubjectsca");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.d(TAG, "Loaded round 3: " + text);

            if (!text.equals("0") && !text.isEmpty() && !text.equals("not compiled")) {
                String data[] = text.split("<>");

                String raw_subjects = data[0], raw_short_codes = data[1];
                no_cas = data[2];
                result.setNoCas(Integer.valueOf(no_cas) + 6); // for exam, total, average....grade
                int no_subjects = raw_subjects.split(";").length;
                for (int i = 0; i <= no_subjects; i++) {
                    subjects.add(raw_subjects.split(";")[i]);
                    short_codes.add(raw_short_codes.split(";")[i]);
                }

                switch (Integer.valueOf(no_cas)) {
                    case 1:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        HEADERS.add("AVERAGE");
                        HEADERS.add("HIGHEST");
                        HEADERS.add("LOWEST");
                        HEADERS.add("GRADE");
                        break;
                    case 2:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        HEADERS.add("AVERAGE");
                        HEADERS.add("HIGHEST");
                        HEADERS.add("LOWEST");
                        HEADERS.add("GRADE");
                        break;
                    case 3:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        HEADERS.add("AVERAGE");
                        HEADERS.add("HIGHEST");
                        HEADERS.add("LOWEST");
                        HEADERS.add("GRADE");
                        break;
                    case 4:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        HEADERS.add("AVERAGE");
                        HEADERS.add("HIGHEST");
                        HEADERS.add("LOWEST");
                        HEADERS.add("GRADE");
                        break;
                    case 5:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("CA5");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        HEADERS.add("AVERAGE");
                        HEADERS.add("HIGHEST");
                        HEADERS.add("LOWEST");
                        HEADERS.add("GRADE");
                        break;
                    case 6:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("CA5");
                        HEADERS.add("CA6");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        HEADERS.add("AVERAGE");
                        HEADERS.add("HIGHEST");
                        HEADERS.add("LOWEST");
                        HEADERS.add("GRADE");
                        break;
                    case 7:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("CA5");
                        HEADERS.add("CA6");
                        HEADERS.add("CA7");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        HEADERS.add("AVERAGE");
                        HEADERS.add("HIGHEST");
                        HEADERS.add("LOWEST");
                        HEADERS.add("GRADE");
                        break;
                    case 8:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("CA5");
                        HEADERS.add("CA6");
                        HEADERS.add("CA7");
                        HEADERS.add("CA8");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        HEADERS.add("AVERAGE");
                        HEADERS.add("HIGHEST");
                        HEADERS.add("LOWEST");
                        HEADERS.add("GRADE");
                        break;
                    case 9:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("CA5");
                        HEADERS.add("CA6");
                        HEADERS.add("CA7");
                        HEADERS.add("CA8");
                        HEADERS.add("CA9");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        HEADERS.add("AVERAGE");
                        HEADERS.add("HIGHEST");
                        HEADERS.add("LOWEST");
                        HEADERS.add("GRADE");
                        break;
                    case 10:
                        HEADERS.add(" NAMES\t\t\t\t\t\t\t\t\t\t\t\t");
                        HEADERS.add("CA1");
                        HEADERS.add("CA2");
                        HEADERS.add("CA3");
                        HEADERS.add("CA4");
                        HEADERS.add("CA5");
                        HEADERS.add("CA6");
                        HEADERS.add("CA7");
                        HEADERS.add("CA8");
                        HEADERS.add("CA9");
                        HEADERS.add("CA10");
                        HEADERS.add("EXAM");
                        HEADERS.add("TOTAL");
                        HEADERS.add("AVERAGE");
                        HEADERS.add("HIGHEST");
                        HEADERS.add("LOWEST");
                        HEADERS.add("GRADE");
                        break;

                }

                for (int j = 0; j <= no_subjects; j++) {
                    for (int i = 0; i <= result.getNoCas(); i++) {
                        switch (i) {
                            case 1:
                                for (int k = 0; k <= no_subjects; k++) {
                                    SUBJECTS.add(subjects_with_scores.get(k).split(";")[0]);
                                    CA1.add(subjects_with_scores.get(k).split(";")[1]);
                                    EXAM.add(subjects_with_scores.get(k).split(";")[2]);
                                    TOTAL.add(subjects_with_scores.get(k).split(";")[4]);
                                    AVERAGE.add(subjects_with_scores.get(k).split(";")[4]);
                                    HIGHEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    LOWEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    GRADE.add(subjects_with_scores.get(k).split(";")[4]);
                                }
                                break;
                            case 2:
                                for (int k = 0; k <= no_subjects; k++) {
                                    SUBJECTS.add(subjects_with_scores.get(k).split(";")[0]);
                                    CA1.add(subjects_with_scores.get(k).split(";")[1]);
                                    CA2.add(subjects_with_scores.get(k).split(";")[2]);
                                    EXAM.add(subjects_with_scores.get(k).split(";")[4]);
                                    TOTAL.add(subjects_with_scores.get(k).split(";")[4]);
                                    AVERAGE.add(subjects_with_scores.get(k).split(";")[4]);
                                    HIGHEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    LOWEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    GRADE.add(subjects_with_scores.get(k).split(";")[4]);
                                }
                                break;
                            case 3:
                                for (int k = 0; k <= no_subjects; k++) {
                                    SUBJECTS.add(subjects_with_scores.get(k).split(";")[0]);
                                    CA1.add(subjects_with_scores.get(k).split(";")[1]);
                                    CA2.add(subjects_with_scores.get(k).split(";")[2]);
                                    CA3.add(subjects_with_scores.get(k).split(";")[3]);
                                    EXAM.add(subjects_with_scores.get(k).split(";")[4]);
                                    TOTAL.add(subjects_with_scores.get(k).split(";")[4]);
                                    AVERAGE.add(subjects_with_scores.get(k).split(";")[4]);
                                    HIGHEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    LOWEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    GRADE.add(subjects_with_scores.get(k).split(";")[4]);
                                }
                                break;
                            case 4:
                                for (int k = 0; k <= no_subjects; k++) {
                                    SUBJECTS.add(subjects_with_scores.get(k).split(";")[0]);
                                    CA1.add(subjects_with_scores.get(k).split(";")[1]);
                                    CA2.add(subjects_with_scores.get(k).split(";")[2]);
                                    CA3.add(subjects_with_scores.get(k).split(";")[3]);
                                    CA4.add(subjects_with_scores.get(k).split(";")[4]);
                                    EXAM.add(subjects_with_scores.get(k).split(";")[4]);
                                    TOTAL.add(subjects_with_scores.get(k).split(";")[4]);
                                    AVERAGE.add(subjects_with_scores.get(k).split(";")[4]);
                                    HIGHEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    LOWEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    GRADE.add(subjects_with_scores.get(k).split(";")[4]);
                                }
                                break;
                            case 5:
                                for (int k = 0; k <= no_subjects; k++) {
                                    SUBJECTS.add(subjects_with_scores.get(k).split(";")[0]);
                                    CA1.add(subjects_with_scores.get(k).split(";")[1]);
                                    CA2.add(subjects_with_scores.get(k).split(";")[2]);
                                    CA3.add(subjects_with_scores.get(k).split(";")[3]);
                                    CA4.add(subjects_with_scores.get(k).split(";")[4]);
                                    CA5.add(subjects_with_scores.get(k).split(";")[5]);
                                    EXAM.add(subjects_with_scores.get(k).split(";")[4]);
                                    TOTAL.add(subjects_with_scores.get(k).split(";")[4]);
                                    AVERAGE.add(subjects_with_scores.get(k).split(";")[4]);
                                    HIGHEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    LOWEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    GRADE.add(subjects_with_scores.get(k).split(";")[4]);
                                }
                                break;
                            case 6:
                                for (int k = 0; k <= no_subjects; k++) {
                                    SUBJECTS.add(subjects_with_scores.get(k).split(";")[0]);
                                    CA1.add(subjects_with_scores.get(k).split(";")[1]);
                                    CA2.add(subjects_with_scores.get(k).split(";")[2]);
                                    CA3.add(subjects_with_scores.get(k).split(";")[3]);
                                    CA4.add(subjects_with_scores.get(k).split(";")[4]);
                                    CA5.add(subjects_with_scores.get(k).split(";")[5]);
                                    CA6.add(subjects_with_scores.get(k).split(";")[6]);
                                    EXAM.add(subjects_with_scores.get(k).split(";")[4]);
                                    TOTAL.add(subjects_with_scores.get(k).split(";")[4]);
                                    AVERAGE.add(subjects_with_scores.get(k).split(";")[4]);
                                    HIGHEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    LOWEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    GRADE.add(subjects_with_scores.get(k).split(";")[4]);
                                }
                                break;
                            case 7:
                                for (int k = 0; k <= no_subjects; k++) {
                                    SUBJECTS.add(subjects_with_scores.get(k).split(";")[0]);
                                    CA1.add(subjects_with_scores.get(k).split(";")[1]);
                                    CA2.add(subjects_with_scores.get(k).split(";")[2]);
                                    CA3.add(subjects_with_scores.get(k).split(";")[3]);
                                    CA4.add(subjects_with_scores.get(k).split(";")[4]);
                                    CA5.add(subjects_with_scores.get(k).split(";")[5]);
                                    CA6.add(subjects_with_scores.get(k).split(";")[6]);
                                    CA7.add(subjects_with_scores.get(k).split(";")[7]);
                                    EXAM.add(subjects_with_scores.get(k).split(";")[4]);
                                    TOTAL.add(subjects_with_scores.get(k).split(";")[4]);
                                    AVERAGE.add(subjects_with_scores.get(k).split(";")[4]);
                                    HIGHEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    LOWEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    GRADE.add(subjects_with_scores.get(k).split(";")[4]);
                                }
                                break;
                            case 8:
                                for (int k = 0; k <= no_subjects; k++) {
                                    SUBJECTS.add(subjects_with_scores.get(k).split(";")[0]);
                                    CA1.add(subjects_with_scores.get(k).split(";")[1]);
                                    CA2.add(subjects_with_scores.get(k).split(";")[2]);
                                    CA3.add(subjects_with_scores.get(k).split(";")[3]);
                                    CA4.add(subjects_with_scores.get(k).split(";")[4]);
                                    CA5.add(subjects_with_scores.get(k).split(";")[5]);
                                    CA6.add(subjects_with_scores.get(k).split(";")[6]);
                                    CA7.add(subjects_with_scores.get(k).split(";")[7]);
                                    CA8.add(subjects_with_scores.get(k).split(";")[8]);
                                    EXAM.add(subjects_with_scores.get(k).split(";")[4]);
                                    TOTAL.add(subjects_with_scores.get(k).split(";")[4]);
                                    AVERAGE.add(subjects_with_scores.get(k).split(";")[4]);
                                    HIGHEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    LOWEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    GRADE.add(subjects_with_scores.get(k).split(";")[4]);
                                }
                                break;
                            case 9:
                                for (int k = 0; k <= no_subjects; k++) {
                                    SUBJECTS.add(subjects_with_scores.get(k).split(";")[0]);
                                    CA1.add(subjects_with_scores.get(k).split(";")[1]);
                                    CA2.add(subjects_with_scores.get(k).split(";")[2]);
                                    CA3.add(subjects_with_scores.get(k).split(";")[3]);
                                    CA4.add(subjects_with_scores.get(k).split(";")[4]);
                                    CA5.add(subjects_with_scores.get(k).split(";")[5]);
                                    CA6.add(subjects_with_scores.get(k).split(";")[6]);
                                    CA7.add(subjects_with_scores.get(k).split(";")[7]);
                                    CA8.add(subjects_with_scores.get(k).split(";")[8]);
                                    CA9.add(subjects_with_scores.get(k).split(";")[9]);
                                    EXAM.add(subjects_with_scores.get(k).split(";")[4]);
                                    TOTAL.add(subjects_with_scores.get(k).split(";")[4]);
                                    AVERAGE.add(subjects_with_scores.get(k).split(";")[4]);
                                    HIGHEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    LOWEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    GRADE.add(subjects_with_scores.get(k).split(";")[4]);
                                }
                                break;
                            case 10:
                                for (int k = 0; k <= no_subjects; k++) {
                                    SUBJECTS.add(subjects_with_scores.get(k).split(";")[0]);
                                    CA1.add(subjects_with_scores.get(k).split(";")[1]);
                                    CA2.add(subjects_with_scores.get(k).split(";")[2]);
                                    CA3.add(subjects_with_scores.get(k).split(";")[3]);
                                    CA4.add(subjects_with_scores.get(k).split(";")[4]);
                                    CA5.add(subjects_with_scores.get(k).split(";")[5]);
                                    CA6.add(subjects_with_scores.get(k).split(";")[6]);
                                    CA7.add(subjects_with_scores.get(k).split(";")[7]);
                                    CA8.add(subjects_with_scores.get(k).split(";")[8]);
                                    CA9.add(subjects_with_scores.get(k).split(";")[9]);
                                    CA10.add(subjects_with_scores.get(k).split(";")[10]);
                                    EXAM.add(subjects_with_scores.get(k).split(";")[4]);
                                    TOTAL.add(subjects_with_scores.get(k).split(";")[4]);
                                    AVERAGE.add(subjects_with_scores.get(k).split(";")[4]);
                                    HIGHEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    LOWEST.add(subjects_with_scores.get(k).split(";")[4]);
                                    GRADE.add(subjects_with_scores.get(k).split(";")[4]);
                                }
                                break;
                        }

                        //CA1.add(subjects_with_scores.get(j).split(";")[1]);
                    }
                }

                result.setCA1(CA1);
                result.setCA2(CA2);
                result.setCA3(CA3);
                result.setCA4(CA4);
                result.setCA5(CA5);
                result.setCA6(CA6);
                result.setCA7(CA7);
                result.setCA8(CA8);
                result.setCA9(CA9);
                result.setCA10(CA10);
                result.setEXAM(EXAM);
                result.setTOTAL(TOTAL);
                result.setTOTAL(AVERAGE);
                result.setTOTAL(HIGHEST);
                result.setTOTAL(LOWEST);
                result.setTOTAL(GRADE);
                result.setHEADERS(HEADERS);
                result.setSUBJECTS(subjects);
                result.setNoSubjects(no_subjects);

            } else {
                Tools.toast("An error occurred", ResultActivity.this, R.color.red_800);
                finish();
            }

            //run next thread
            new getstdscores().execute(school_id, session, term, student_reg_no);

        }
    }


    //  4. loads students scores
    private class getstdscores extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstdscores");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2] + "<>" + strings[3]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.d(TAG, "Loaded round 4: " + text);

            if (!text.equals("0") && !text.isEmpty()) {
                String data[] = text.split("##");

                String raw_scores = data[0], raw_others = data[1], raw_psychomotor = data[2];
                int no_scores = raw_scores.split("<>").length;

                for (int i = 0; i <= no_scores; i++) {
                    subjects_with_scores.add(raw_scores.split("<>")[i]);
                    class_average__highest__lowest.add(raw_others.split("<>")[i]);
                    class_average__highest__lowest.add(raw_others.split("<>")[i]);
                }
                for (int i = 0; i <= raw_psychomotor.split("<>").length; i++) {
                    psychomotor_skills.add(raw_psychomotor.split("<>")[i].split(";")[0]);
                    psychomotor_values.add(raw_psychomotor.split("<>")[i].split(";")[1]);
                }
                //inflate on UI

            } else {
                Tools.toast("An error occurred", ResultActivity.this, R.color.red_800);
                finish();
            }

            //run next thread
            new getgrades().execute(school_id, student_reg_no, session);

        }
    }

    //  5. loads grades
    private class getgrades extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getgrades");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
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
            Log.d(TAG, "Loaded round 5: " + text);

            if (!text.equals("0") && !text.isEmpty()) {
                String data[] = text.split("<>");

                String raw_grades = data[0], raw_upper_values = data[1], raw_lower_values = data[2], raw_tags = data[3];
                int no_grades = raw_grades.split(";").length;

                for (int i = 0; i <= no_grades; i++) {
                    grades.add(raw_grades.split(";")[i]);
                    upper_values.add(raw_upper_values.split(";")[i]);
                    lower_values.add(raw_lower_values.split(";")[i]);
                    tags.add(raw_tags.split(";")[i]);
                }
                //inflate on UI


            } else {
                Tools.toast("An error occurred", ResultActivity.this, R.color.red_800);
                finish();
            }

            //run next thread
            new getstudentresult().execute(school_id, student_reg_no, session, term);

        }
    }

    //  6. loads grades
    private class getstudentresult extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentresult");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.d(TAG, "Loaded round 6: " + text);

            if (!text.equals("0") && !text.isEmpty()) {
                String data[] = text.split(";");

                ResultFooter resultFooter = new ResultFooter();
                resultFooter.setTotal(data[0]);
                resultFooter.setAverage(data[1]);
                resultFooter.setNo_subjects(data[2]);
                resultFooter.setPosition(data[3]);
                resultFooter.setHead_teacher_remark(data[4]);
                resultFooter.setClass_teacher_remark(data[5]);
                resultFooter.setHead_teacher_name(data[6]);
                resultFooter.setClass_teacher_name(data[7]);
                resultFooter.setAttendance(data[8]);
                resultFooter.setTotal_attendance(data[9]);
                resultFooter.setTotal_number_students_in_class(data[10]);

                //concat and inflate on UI
                result_summary_tv.setText(String.format("Total: %s;\tAverage: %s;\tNumber of Subjects: %s;\tPosition: %s;\tHead Teachers' Remark: %s;\tClass Teachers' Remark: %s;\tHead Teachers' Name: %s;\tClass Teachers' Name: %s;\tAttendance: %s;\tTotal Attendance: %s;\tTotal Number of Students in Class: %s", resultFooter.getTotal(), resultFooter.getAverage(), resultFooter.getNo_subjects(), resultFooter.getPosition(), resultFooter.getHead_teacher_remark(), resultFooter.getClass_teacher_remark(), resultFooter.getHead_teacher_name(), resultFooter.getClass_teacher_name(), resultFooter.getAttendance(), resultFooter.getTotal_attendance(), resultFooter.getTotal_number_students_in_class())
                );
            } else {
                Tools.toast("An error occurred", ResultActivity.this, R.color.red_800);
                finish();
            }

            //inflate everything in UI, then unveil

            //TODO: unveil gradually from top to bottom. How about that?
            loading.setVisibility(View.GONE);
            nested_scroll_view.setVisibility(View.VISIBLE);
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //DONE
    private void initComponents() {
        //get stuff from sharedPrefs
        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        //school_id/staff id from sharedPrefs
        parent_id = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));
        school_id = getIntent().getStringExtra("school_id");
        student_reg_no = getIntent().getStringExtra("student_reg_no");
        term = getIntent().getStringExtra("term");
        session = getIntent().getStringExtra("session");
        school_logo = findViewById(R.id.logo);

        // nested scrollview
        nested_scroll_view = findViewById(R.id.nested_content);
        loading = findViewById(R.id.loading);
        // loading.setVisibility(View.VISIBLE);
        // nested_scroll_view.setVisibility(View.GONE);
        result_layout = findViewById(R.id.result);

        result_layout.addView(new TableMainLayoutOriginal(this));
        school_name = findViewById(R.id.school_name);
        school_name.setText("Calvary Arrows College");
        school_address = findViewById(R.id.address);
        school_address.setText(full_address);
        term_result = findViewById(R.id.term_result);
        //TODO: HINT >>> we display the uncertain text first before the certain text

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
        bt_toggle_result.setOnClickListener(view -> {
            toggleSection1(view);
            if (result_layout.getVisibility() == View.VISIBLE)
                result_layout.setVisibility(View.GONE);
            else
                result_layout.setVisibility(View.VISIBLE);
        });
    }

    //DONE
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

    //DONE
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

    //DONE
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

    //DONE
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

    //DONE
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

    //DONE
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

    //DONE
    private void toggleSection1(View bt) {
        toggleArrow(bt);
    }

    //DONE
    public boolean toggleArrow(View view) {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

}
