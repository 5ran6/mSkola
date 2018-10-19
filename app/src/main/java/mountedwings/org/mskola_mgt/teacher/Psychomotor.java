package mountedwings.org.mskola_mgt.teacher;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;


public class Psychomotor extends AppCompatActivity {

    private storageFile data;
    private int MAX_STEP = 20;
    private int current_step = 1;
    private List<View> view_list = new ArrayList<>();
    private String[] skill_names;
    private String[] skills_values;

    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> skills = new ArrayList<>();
    private ArrayList<String> regNo = new ArrayList<>();
    private ArrayList<String> allSkills_aPerson = new ArrayList<>();
    private ArrayList<byte[]> allPassport_aPerson = new ArrayList<>();
    private List<RelativeLayout> step_view_list = new ArrayList<>();
    private String[] regNumbs;
    private String[] names;
    private ProgressBar progressBar, loading;
    private int success_step = 0, len;
    private TextView heading;
    private LinearLayout parent_view;
    private LayoutInflater inflater;
    private View view;
    private ListView listView;

    private AppCompatEditText score;
    private ViewGroup main;
    private TextView skill;
    private AppCompatEditText value;
    private int no_skills;
    private int last_index;
    private LinearLayout first, second;
    private String TAG = "mSkola", first_persons_name = "", staff_id,
            school_id, class_name, arm, assessment, subject;
    private ImageView passport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychomotor_progress);
        initToolbar();
        initComponent();
    }

    private void initComponent() {
        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));
        //school_id/staff id from sharedPrefs
        staff_id = mPrefs.getString("staff_id", getIntent().getStringExtra("email_address"));
        school_id = mPrefs.getString("school_id", getIntent().getStringExtra("school_id"));

        parent_view = findViewById(R.id.parent_layout);


        first = findViewById(R.id.first);
        //  second = findViewById(R.id.second);
        passport = findViewById(R.id.passport);

        loading = findViewById(R.id.loading);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        class_name = intent.getStringExtra("class_name");
        arm = intent.getStringExtra("arm");
        listView = findViewById(R.id.psycho_skills);

        //        new first_loading().execute(school_id, class_name, arm);
        new first_loading().execute(school_id, "JSS1", "A");


        //students name from server


        ((LinearLayout) findViewById(R.id.lyt_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backStep(current_step);
            }
        });

        ((LinearLayout) findViewById(R.id.lyt_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextStep(current_step);
            }
        });

//        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        ((TextView) findViewById(R.id.steps)).setText("---/---");

    }

    private void nextStep(int progress) {
        new savePsychoSkills().execute();
        if (progress < MAX_STEP) {
            progress++;
            current_step = progress;
            //            ViewAnimation.fadeOutIn(status);
        }
        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        ((TextView) findViewById(R.id.steps)).setText(str_progress);
        progressBar.setProgress(current_step);

        //LOAD EVERYTHING
        // - Name
        ViewAnimation.fadeOutIn(heading);
        heading.setText(String.format("Name: %s", students.get(current_step - 1)));

        // - Passport
        Bitmap bitmap = BitmapFactory.decodeByteArray(allPassport_aPerson.get(current_step - 1), 0, allPassport_aPerson.get(current_step - 1).length);
        passport.setImageBitmap(bitmap);
        parent_view = findViewById(R.id.parent_layout);
        //  parent_view.removeView(main);


        loading.setVisibility(View.VISIBLE);
        load(current_step - 1);
        loading.setVisibility(View.GONE);

    }

    private void backStep(int progress) {
        new savePsychoSkills().execute();
        if (progress > 1) {
            progress--;
            current_step = progress;
        }
        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);

        ((TextView) findViewById(R.id.steps)).setText(str_progress);
        progressBar.setProgress(current_step);

        //LOAD EVERYTHING
        // - Name
        ViewAnimation.fadeOutIn(heading);
        heading.setText(String.format("Name: %s", students.get(current_step - 1)));

        // - Passport
        Bitmap bitmap = BitmapFactory.decodeByteArray(allPassport_aPerson.get(current_step - 1), 0, allPassport_aPerson.get(current_step - 1).length);
        passport.setImageBitmap(bitmap);

        // - Skills and Values
        loading.setVisibility(View.VISIBLE);
        load(current_step - 1);
        loading.setVisibility(View.GONE);
    }


    private void areYouSure() {
        new AlertDialog.Builder(this)
                .setMessage("Are you done?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, id) ->
                        //TODO : save
                        finish())
                .setNegativeButton("No", (dialog, which) -> {
                })
                .show();
    }

    //DONE
    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private class laodPsychoValue extends AsyncTask<ArrayList<String>, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(ArrayList... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getstudentpsychomotor");
            for (int i = 0; i < strings[0].size(); i++) {
                storageObj.setStrData(school_id + "<>" + strings[0].get(i));
                storageFile sentData = new serverProcess().requestProcess(storageObj);
                if (sentData.getStrData().isEmpty() || sentData.getStrData().equals("")) {
                    StringBuilder zero = new StringBuilder("0");
                    for (int y = 1; y < no_skills; y++) zero.append(";0");
                    allSkills_aPerson.add(zero.toString());
                } else {
                    allSkills_aPerson.add(sentData.getStrData());
                }
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean done) {
            super.onPostExecute(done);
            //            Log.d(TAG, scores[8]);
            if (done) {
                //main = findViewById(R.id.second);
                Toast.makeText(getApplicationContext(), allSkills_aPerson.toString(), Toast.LENGTH_SHORT).show();
                load(current_step);

                Bitmap bitmap = BitmapFactory.decodeByteArray(allPassport_aPerson.get(0), 0, allPassport_aPerson.get(current_step - 1).length);
                passport.setImageBitmap(bitmap);

                String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
                ((TextView) findViewById(R.id.steps)).setText(str_progress);

                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax(MAX_STEP);
                progressBar.setProgress(1);
                progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

                heading = findViewById(R.id.students_name);
                heading.setText(String.format("Name: %s", students.get(0)));


                //Load LIN_LAYS


                first.setVisibility(View.VISIBLE);
                listView.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(getApplicationContext(), "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }

            loading.setVisibility(View.GONE);
            //set passport, get name, step, psyco skills names and regNo

            // Names                    - Done
            // List of psyco skill      - Done
            // passport                 - Done
            // regNo                    - Done
        }
    }

    private void load(int i) {
        skill_names = new String[no_skills];
        skills_values = new String[no_skills];


        for (int x = 0; x < no_skills; x++) {
            skill_names[x] = skills.get(x);
            if (allSkills_aPerson.get(current_step - 1).split(";")[x].equals("0")) {
                skills_values[x] = " ";
            } else {
                skills_values[x] = allSkills_aPerson.get(current_step - 1).split(";")[x];
            }
            Toast.makeText(getApplicationContext(), skills_values[x], Toast.LENGTH_SHORT).show();
        }

        skillList customAdapter = new skillList();
        listView.setAdapter(customAdapter);
    }

    //DONE
    private class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            data = storageObj;

            storageObj.setOperation("getpsstudentinfo");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            data = sentData;
            String text = data.getStrData();
            Log.d(TAG, text);
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
            if (!text.equals("0") && !text.equals("")) {
                //       Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                allPassport_aPerson = data.getImageFiles();

                String rows[] = text.split("<>");
                MAX_STEP = rows.length;
                for (int i = 0; i < rows.length; i++) {
                    students.add(rows[i].split(";")[1]);
                    regNo.add(text.split("<>")[i].split(";")[0]);
                    //                    regNo.add() = myClass.PSObj.getStrData().split("<>")[myClass.selPSStudent].split(";")[0];
                }
            } else {
                Toast.makeText(getApplicationContext(), "No student found in the selected class", Toast.LENGTH_SHORT).show();
                finish();
            }
            //finally
            first_persons_name = students.get(0);

            new loadPsychoSkills().execute();


        }

    }

    private class loadPsychoSkills extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            data = storageObj;

            storageObj.setOperation("getpsychometry");
            storageObj.setStrData(school_id);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            data = sentData;
            String text = data.getStrData();
            Log.d(TAG, text);
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
            if (!text.equals("0") && !text.equals("")) {
                String rows[] = text.split(";");
                no_skills = rows.length;

                for (int i = 0; i < rows.length; i++) {
                    skills.add(rows[i].split(",")[1]);
                }
                //                Toast.makeText(getApplicationContext(), String.valueOf(no_skills) + ": " + skills.get(1), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "No skill set found", Toast.LENGTH_SHORT).show();
                finish();
            }
            //finally

            new laodPsychoValue().execute(regNo);


        }

    }

    private class savePsychoSkills extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            //pre
            String values_to_send = "";
            String ps_values = "";
            // TODO
            /*
            for (int i = 0; i < no_skills; i++) {
                if (values_to_send.isEmpty()) {
                    if (new skillList().getItemId(i) == R.id.psycho_skills) {
                        values_to_send = "_";
                    } else {
                        values_to_send = myClass.psValues[i].getText();
                    }
                } else {
                    if (myClass.psValues[i].getText().isEmpty()) {
                        values_to_send += ";_";
                    } else {
                        values_to_send += ";" + myClass.psValues[i].getText();
                    }
                }

                //to get all the psychomotor skills
                if (ps_values.isEmpty()) {
                    ps_values = myClass.allPSSkills.get(i).toString();
                } else {
                    ps_values += ";" + myClass.allPSSkills.get(i).toString();
                }
            }
*/

            storageFile storageObj = new storageFile();
            data = storageObj;


            storageObj.setOperation("savepsychomotorvalues");

            storageObj.setStrData(school_id + "<>" + regNo.get(current_step - 1) + "<>" + ps_values + "<>" + values_to_send);
            storageFile sentData = new serverProcess().requestProcess(storageObj);
            data = sentData;
            String text = data.getStrData();
            Log.d(TAG, text);
            return text;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
            if (text.equals("success")) {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Couldn't save. Try again", Toast.LENGTH_SHORT).show();
            }

        }

    }

    //DONE
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Psychomotor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        }
        if (item.getItemId() == R.id.action_done) {
            //save the current psyco and close
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        areYouSure();
    }


    class skillList extends BaseAdapter {
        // TODO: Still displaying one skill
        // TODO: Get text from editText from List View

        int i;

        private skillList() {
        }

        @Override
        public int getCount() {
            return no_skills;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_psychomotor_skills, null);

            TextView skill = convertView.findViewById(R.id.tv_psycho_skil);
            AppCompatEditText value = convertView.findViewById(R.id.et_value);

            skill.setText(skill_names[position]);
            value.setText(skills_values[position]);

            return convertView;
        }
    }
}