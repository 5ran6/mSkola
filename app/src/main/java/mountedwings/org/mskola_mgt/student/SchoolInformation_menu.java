package mountedwings.org.mskola_mgt.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.AdapterListSchools;
import mountedwings.org.mskola_mgt.data.NumberSchool;
import mountedwings.org.mskola_mgt.utils.Tools;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class SchoolInformation_menu extends AppCompatActivity {

    private View parent_view;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private CardView list;
    private String student_email;
    private AdapterListSchools mAdapter;
    private ArrayList<NumberSchool> schools = new ArrayList<>();
    private ArrayList<byte[]> logos = new ArrayList<>();
    private LinearLayout search_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_information_menu);
        parent_view = findViewById(R.id.parent_view);
        search_layout = findViewById(R.id.search_layout);
        search_layout.setVisibility(View.GONE);
        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Schools using mSkola");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        student_email = mPrefs.getString("email_address", getIntent().getStringExtra("email_address"));

        progressBar = findViewById(R.id.progress);
        list = findViewById(R.id.list);

        list.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        //if network
        new initialLoad().execute(student_email);


        //run API

    }

    //loads schools
    private class initialLoad extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getschools");
            storageObj.setStrData(strings[0]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            logos = sentData.getImageFiles();
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
//            System.out.println(text);
            if (!text.equals("0") && !text.isEmpty()) {
                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {
                    NumberSchool numberSchool = new NumberSchool();
                    numberSchool.setSchool_id(rows[i].split(";")[0]);
                    numberSchool.setSchool_name(rows[i].split(";")[1]);
                    numberSchool.setSchool_address(rows[i].split(";")[2]);
                    numberSchool.setLogo(logos.get(i));
                    schools.add(numberSchool);
                }
                //set data and list adapter
                mAdapter = new AdapterListSchools(SchoolInformation_menu.this, schools);
                recyclerView.setAdapter(mAdapter);
                // on item list clicked
                mAdapter.setOnItemClickListener((view, obj, position) -> startActivity(new Intent(getApplicationContext(), SchoolInformation.class).putExtra("school_id", obj.getSchool_id()).putExtra("school_name", obj.getSchool_name())));

                list.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            } else {
                Tools.toast("No schools found for your children. Please contact school administration", SchoolInformation_menu.this, R.color.red_500, Toast.LENGTH_LONG);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            //search function
            search_layout.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}