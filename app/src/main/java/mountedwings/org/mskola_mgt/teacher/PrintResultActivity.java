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

package mountedwings.org.mskola_mgt.teacher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.AdapterListClassStudents;
import mountedwings.org.mskola_mgt.data.NumberClassStudents;
import mountedwings.org.mskola_mgt.utils.CheckNetworkConnection;
import mountedwings.org.mskola_mgt.utils.Tools;


public class PrintResultActivity extends AppCompatActivity {
    boolean timedOut = false;
    private List<NumberClassStudents> students = new ArrayList<>();
    private AdapterListClassStudents mAdapter;
    private RecyclerView list;
    private ImageView passport;
    private TextView result_summary;
    private TextInputLayout ti_total_score, ti_postion, ti_head_teachers_remark, ti_class_teachers_remark, ti_average, ti_no_subjects;
    private AppCompatEditText total_score, position_, head_teachers_remark, class_teachers_remark, average, no_subjects;
    private String result_summary_text = "", school_id, class_name, arm, term, session, student_reg_no = "", position = "yes", resultClass = "class";
    private BroadcastReceiver mReceiver;
    private int w = 0, timer = 0;
    private int status;
    private AsyncTask lastThread;
    private ProgressBar loading;
    private LinearLayout linearLayout1, linearLayout2;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> regNo = new ArrayList<>();
    private String[] grades, upperValues, lowerValues, gradeTags;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_result_activity);
        school_id = getIntent().getStringExtra("school_id");
        class_name = getIntent().getStringExtra("class_name");
        arm = getIntent().getStringExtra("arm");
        term = getIntent().getStringExtra("term");
        session = getIntent().getStringExtra("session");

        loading = findViewById(R.id.progress);
        passport = findViewById(R.id.passport);
        passport.setImageResource(R.drawable.user3);


        linearLayout1 = findViewById(R.id.header);
        linearLayout2 = findViewById(R.id.body);
        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);
        list.setNestedScrollingEnabled(false);

        ti_total_score = findViewById(R.id.ti_total_score);
        ti_postion = findViewById(R.id.ti_position);
        ti_head_teachers_remark = findViewById(R.id.ti_head_teachers_remark);
        ti_class_teachers_remark = findViewById(R.id.ti_class_teachers_remark);
        ti_average = findViewById(R.id.ti_average);
        ti_no_subjects = findViewById(R.id.ti_no_subjects);

        total_score = findViewById(R.id.total_score);
        position_ = findViewById(R.id.position);
        head_teachers_remark = findViewById(R.id.head_teachers_remark);
        class_teachers_remark = findViewById(R.id.class_teachers_remark);
        average = findViewById(R.id.average);
        no_subjects = findViewById(R.id.no_subjects);

        //call API to load students names/regNo
        new initialLoading().execute();
        new getGrades().execute();
        new getSubjects().execute();

        //no need for textWatcher or textInput sef...mtchew


        //show recyclerView with inflated views

    }

    public void save_remark(View view) {
        //  new saveTeachersRemark().execute();
        new saveTeachersRemark().execute();
    }

    public void view_term_result(View view) {
        BottomSheetBehavior<View> mBehavior;
        BottomSheetDialog[] mBottomSheetDialog = new BottomSheetDialog[1];

        View bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        //show bottom sheet
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View v = getLayoutInflater().inflate(R.layout.view_result_sheet, null);
        result_summary = v.findViewById(R.id.result_summary);

        result_summary.setText(result_summary_text);

        mBottomSheetDialog[0] = new BottomSheetDialog(this);
        mBottomSheetDialog[0].setContentView(v);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog[0].getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog[0].show();
        mBottomSheetDialog[0].setOnDismissListener(dialog -> mBottomSheetDialog[0] = null);

    }

    public void view_annual_result(View view) {
        BottomSheetBehavior<View> mBehavior;
        BottomSheetDialog[] mBottomSheetDialog = new BottomSheetDialog[1];

        View bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        //show bottom sheet
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View v = getLayoutInflater().inflate(R.layout.view_result_sheet, null);
        result_summary = v.findViewById(R.id.result_summary);

        result_summary.setText("No Annual results available!");

        mBottomSheetDialog[0] = new BottomSheetDialog(this);
        mBottomSheetDialog[0].setContentView(v);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog[0].getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog[0].show();
        mBottomSheetDialog[0].setOnDismissListener(dialog -> mBottomSheetDialog[0] = null);
    }

    private void generate_result(String raw_result) {

//        result_summary_text =
    }

    public void by_class(View view) {
        resultClass = "class";
    }

    public void by_arm(View view) {
        resultClass = "arm";
    }

    public void yes_position(View view) {
        position = "yes";
    }

    public void no_position(View view) {
        position = "no";
    }


    @Override
    protected void onResume() {
        super.onResume();

        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                w++;
                new CheckNetworkConnection(context, new CheckNetworkConnection.OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        status = 1;
                        try {
                            if (w > 1) {
                                //new saveTeachersRemark().execute();
                                Tools.toast("Back Online! Resuming request....", PrintResultActivity.this, R.color.green_800);
                            } //else
                            //load classes and assessments
                            //lastThread = new saveTeachersRemark().execute();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onConnectionFail(String errorMsg) {
                        status = 0;
                        Log.d("mSkola", String.valueOf(status));
                        Tools.toast(getResources().getString(R.string.no_internet_connection), PrintResultActivity.this, R.color.red_600);
                        try {
                            lastThread.cancel(true);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).execute();
            }

        };

        registerReceiver(
                this.mReceiver,
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void unregisterReceiver(BroadcastReceiver receiver) {
        super.unregisterReceiver(receiver);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    public class initialLoading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                do {
                    storageFile storageObj = new storageFile();
                    storageObj.setOperation("getpsstudentinfo");
                    //     storageObj.setStrData("cac181022112152" + "<>" + "SSS3" + "<>" + "A");
                    storageObj.setStrData(school_id + "<>" + class_name + "<>" + arm);
                    storageFile sentData = new serverProcess().requestProcess(storageObj);
                    return sentData.getStrData();

                } while (!isCancelled());
            } catch (Exception ex) {
                ex.printStackTrace();
                return "network error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Tools.toast("Fetching Results", ViewResultActivity.this);
            //linLay2 gone
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.d("mskola", text);
            if (text.equalsIgnoreCase("network error") && !timedOut) {
                // Tools.toast("Network error. Reconnecting...", PrintResultActivity.this, R.color.red_900);
                new initialLoading().execute();
                if (timer > 10)
                    timedOut = true;

                timer++;
            }

            if (timedOut) {
                Tools.toast("Network error.", PrintResultActivity.this, R.color.red_900);
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);

            }

            if (!text.isEmpty() && !text.equals("0")) {
                timer = 0;
                //worked. Split into reg and names
                String[] rows = text.split("<>");

                if (rows.length > 0) {
                    for (int i = 0; i < rows.length; i++) {
                        regNo.add(rows[i].split(";")[0]);
                        names.add(i, rows[i].split(";")[1]);
                        NumberClassStudents number = new NumberClassStudents();
                        number.setName(names.get(i));
                        number.setReg_no(regNo.get(i));
                        students.add(number);
                        //show recyclerView with inflated views
                        mAdapter = new AdapterListClassStudents(students);
                        list.setAdapter(mAdapter);

                        linearLayout1.setVisibility(View.VISIBLE);
                        linearLayout2.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);

                    }

                    mAdapter.setOnItemClickListener((view, obj, location) -> {
                        student_reg_no = obj.getReg_no().trim();
                        new getStudentsValue().execute();
                    });


                } else {
                    // display an EMPTY error dialog and return to previous activity
                    Tools.toast("Oops! No student found in the selected class", PrintResultActivity.this, R.color.red_600);
                    finish();
                }


            }

        }
    }

    public class getSubjects extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                do {
                    storageFile storageObj = new storageFile();
                    storageObj.setOperation("getprsubjects");
                    //storageObj.setStrData("cac181022112152" + "<>" + "SSS3" + "<>" + "2019/2020");
                    storageObj.setStrData(school_id + "<>" + class_name + "<>" + session);
                    storageFile sentData = new serverProcess().requestProcess(storageObj);
                    return sentData.getStrData();

                } while (!isCancelled());
            } catch (Exception ex) {
                ex.printStackTrace();
                return "network error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //linLay2 gone
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.d("mSkola", text);
            if (text.equalsIgnoreCase("network error")) {
                //     Tools.toast("Network error. Reconnecting...", PrintResultActivity.this, R.color.red_900);
                new getGrades().execute();
            }
            if (!text.isEmpty() && !text.equals("0")) {
                String[] subjects = text.split("<>")[0].split(";");
                String[] short_codes = text.split("<>")[1].split(";");
                int noCA = Integer.parseInt(text.split("<>")[2]);

            } else {
                // display an EMPTY error dialog and return to previous activity
                Tools.toast("No grading system set for this class!", PrintResultActivity.this, R.color.red_600);
            }


        }
    }

    public class getGrades extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                do {
                    storageFile storageObj = new storageFile();
                    storageObj.setOperation("getgrades");
                    //storageObj.setStrData("cac181022112152" + "<>" + "SSS3");
                    storageObj.setStrData(school_id + "<>" + class_name);
                    storageFile sentData = new serverProcess().requestProcess(storageObj);
                    return sentData.getStrData();

                } while (!isCancelled());
            } catch (Exception ex) {
                ex.printStackTrace();
                return "network error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //linLay2 gone
//            linearLayout1.setVisibility(View.GONE);
//            linearLayout2.setVisibility(View.GONE);
//            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.d("mSkola", text);
            if (text.equalsIgnoreCase("network error")) {
                //    Tools.toast("Network error. Reconnecting...", PrintResultActivity.this, R.color.red_900);
                new getGrades().execute();
            }
            if (!text.isEmpty() && !text.equals("0")) {
                grades = text.split("<>")[0].split(";");
                upperValues = text.split("<>")[1].split(";");
                lowerValues = text.split("<>")[2].split(";");
                gradeTags = text.split("<>")[3].split(";");
            } else {
                // display an EMPTY error dialog and return to previous activity
                Tools.toast("No grading system set for this class!", PrintResultActivity.this, R.color.red_600);
            }
        }
    }

    public class saveTeachersRemark extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                do {
                    storageFile storageObj = new storageFile();
                    storageObj.setOperation("saveteachersremark");
                    storageObj.setStrData(school_id + "<>" + student_reg_no + "<>" + head_teachers_remark.getText() + "<>" + class_teachers_remark.getText() + "<>" + class_name + "<>" + arm);
//                    storageObj.setStrData("cac181022112152" + "<>" + student_reg_no + "<>" + head_teachers_remark.getText() + "<>" + class_teachers_remark.getText() + "<>" + "SSS3" + "<>" + "A");
                    storageFile sentData = new serverProcess().requestProcess(storageObj);
                    return sentData.getStrData();

                } while (!isCancelled());
            } catch (Exception ex) {
                ex.printStackTrace();
                return "network error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //linLay2 gone
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.d("mSkola", text);
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            if (text.equalsIgnoreCase("network error")) {
                //  Tools.toast("Network error. Reconnecting...", PrintResultActivity.this, R.color.red_900);
                new saveTeachersRemark().execute();
            }

            if (!text.isEmpty() && !text.equals("0")) {

                if (text.equals("success")) {
                    Tools.toast("Successful", PrintResultActivity.this, R.color.green_900);

                } else {
                    Tools.toast("Oops...Unsuccessful", PrintResultActivity.this, R.color.red_900);
                }
            } else {
                // display an EMPTY error dialog and return to previous activity
                Tools.toast("Something went wrong. Try again!", PrintResultActivity.this, R.color.red_600);
            }
        }
    }

    public class getStudentsValue extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                do {
                    storageFile storageObj = new storageFile();
                    storageObj.setOperation("getstudentprintresult");
//                    storageObj.setStrData("cac181022112152" + "<>" + student_reg_no + "<>" + resultClass + "<>" + position + "<>" + "2019/2020" + "<>" + "First" + "<>" + "SSS3" + "<>" + "A");
                    storageObj.setStrData(school_id + "<>" + student_reg_no + "<>" + resultClass + "<>" + position + "<>" + session + "<>" + term + "<>" + class_name + "<>" + arm);
                    //             storageObj.setStrData("cac181022112152" + "<>" + "MSK181023181815" + "<>" + "class" + "<>" + "yes" + "<>" + "2019/2020" + "<>" + "First" + "<>" + "SSS3" + "<>" + "A");
                    storageFile sentData = new serverProcess().requestProcess(storageObj);
                    return sentData.getStrData();

                } while (!isCancelled());
            } catch (Exception ex) {
                ex.printStackTrace();
                return "network error";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //linLay2 gone
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            Log.d("mSkola", text);
            if (text.equalsIgnoreCase("network error")) {
                //  Tools.toast("Network error. Reconnecting...", PrintResultActivity.this, R.color.red_900);
                new getStudentsValue().execute();
            }

            if (!text.isEmpty() && !text.equals("0")) {
                if (text.equals("not compiled")) {
                    Tools.toast("Result has not been compiled", PrintResultActivity.this, R.color.red_600);
                    // finish();
                } else {
                    Log.d("mSkola", text);
                    //works
                    position_.setText("");
                    average.setText("");
                    head_teachers_remark.setText("");
                    class_teachers_remark.setText("");
                    total_score.setText("");
                    no_subjects.setText("");

                    String[] rows = text.split(";");

                    total_score.setText(rows[0]);
                    no_subjects.setText(rows[2]);
                    average.setText(String.format("%.2f", Double.parseDouble(rows[1])));

                    if (!rows[3].equals("_"))
                        position_.setText(rows[3]);

                    if (!rows[4].equals("_"))
                        head_teachers_remark.setText(rows[4]);

                    if (!rows[5].equals("_"))
                        class_teachers_remark.setText(rows[5]);
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);

//
//                    byte[] byteArray = myClass.PRObj.getImageFiles().get(myClass.selPRStudent);
//                    ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
//                    BufferedImage read = ImageIO.read(in);
//
//                    myClass.PRPassport.setImage(SwingFXUtils.toFXImage(read, null));

                }

            } else {
                // display an EMPTY error dialog and return to previous activity
                Tools.toast("An error occurred", PrintResultActivity.this, R.color.red_600);
                linearLayout1.setVisibility(View.VISIBLE);
                linearLayout2.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }


        }
    }
}
