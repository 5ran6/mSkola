package mountedwings.org.mskola_mgt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcess;
import com.mskola.files.storageFile;

import java.util.ArrayList;
import java.util.Objects;

import mountedwings.org.mskola_mgt.adapter.NumbersViewResultAdapter;
import mountedwings.org.mskola_mgt.data.NumberViewResult;

import static mountedwings.org.mskola_mgt.SettingFlat.myPref;

public class Chat_menu extends AppCompatActivity {
    String school_id, staff_id, TAG = "mSkola";
    String session;
    private String message_category;

    String term;
    ArrayList<NumberViewResult> numbers = new ArrayList<>();


    private RecyclerView list;
    private FloatingActionButton fab_done;
    private TextView heading;
    private storageFile data = new storageFile();
    private ArrayList<byte[]> allPassport_aPerson = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> message = new ArrayList<>();

    ProgressBar loading;
    NumbersViewResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_menu);
        Intent intent = getIntent();

        SharedPreferences mPrefs = Objects.requireNonNull(getSharedPreferences(myPref, 0));

        //school_id/staff id from sharedPrefs

        staff_id = mPrefs.getString("staff_id", intent.getStringExtra("staff_id"));
        school_id = mPrefs.getString("school_id", intent.getStringExtra("school_id"));

        fab_done = findViewById(R.id.done);
        heading = findViewById(R.id.assignment_history_title);
        //   heading.setText(new StringBuilder().append("Result for ").append(Objects.requireNonNull(getArguments()).getString("class_name")).toString());

        loading = findViewById(R.id.loading);

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(false);

        adapter = new NumbersViewResultAdapter(numbers);
        list.setAdapter(adapter);


        //hide parentView
        loading.setVisibility(View.VISIBLE);

        new first_loading().execute(school_id, staff_id);
///////////////////////////////////////////////////////////////////////////////////////////////////////
        fab_done.setOnClickListener(v -> showSingleChoiceDialog());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //DONE
    private class first_loading extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            data = storageObj;

            storageObj.setOperation("getmessages");
            storageObj.setStrData(strings[0] + "<>" + strings[1] + "<>" + strings[2]);
            data = new serverProcess().requestProcess(storageObj);
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
                Log.i(TAG, "Passports = " + String.valueOf(allPassport_aPerson));

                String rows[] = text.split("<>");
                for (int i = 0; i < rows.length; i++) {

                }
            } else {
                Toast.makeText(Objects.requireNonNull(getApplicationContext()), "No student found in the selected class. Compile Result first", Toast.LENGTH_SHORT).show();
                finish();
            }
            //finally

        }
    }

    private void showSingleChoiceDialog() {
        final String items[] = {"Staff", "Parent", "Student"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send Message to");
        builder.setItems(items, (dialogInterface, i) -> {
            message_category = items[i];
            Intent intent;
            switch (message_category) {
                case "Staff":
                    intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    break;
                case "Parent":
                    intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    break;
                case "Student":
                    intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    break;
            }
        });
        builder.show();
    }

}

