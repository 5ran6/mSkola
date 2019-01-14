package mountedwings.org.mskola_mgt.parent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mskola.controls.serverProcessParents;
import com.mskola.files.storageFile;

import java.util.ArrayList;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.Tools;

public class SchoolInformation extends AppCompatActivity {

    private TextView about;
    private TextView contact;
    private String phone;
    private String school_full_name;
    private FloatingActionButton call;
    private ImageView logo;
    private ArrayList<byte[]> school_logo = new ArrayList<>();
    private LinearLayout lyt_form;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_information);

        String school_id = getIntent().getStringExtra("school_id");
        school_full_name = getIntent().getStringExtra("school_name");
        TextView school_name = findViewById(R.id.school_name);
        school_name.setText(school_full_name);
        progressBar = findViewById(R.id.progress);
        logo = findViewById(R.id.logo);
        about = findViewById(R.id.about);
        contact = findViewById(R.id.contact);
        call = findViewById(R.id.fab);
        call.setVisibility(View.INVISIBLE);

        lyt_form = findViewById(R.id.lyt_form);
        lyt_form.setVisibility(View.GONE);


        //run API if network
        new initialLoad().execute(school_id);

        call.setOnClickListener(v -> {
            // code to open dialer with the number
            String uri = "tel:" + phone.trim();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        });
    }

    //loads schools
    private class initialLoad extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            storageFile storageObj = new storageFile();
            storageObj.setOperation("getschooldetails");
            storageObj.setStrData(strings[0]);
            storageFile sentData = new serverProcessParents().requestProcess(storageObj);
            school_logo = sentData.getImageFiles();
            return sentData.getStrData();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String text) {
            super.onPostExecute(text);
//            System.out.println(text);

            if (!text.equals("0") && !text.isEmpty()) {
                String rows[] = text.split(";");
                phone = rows[5];
                about.setText(String.format("%s is a %s which is located at %s, %s, %s, %s. ", school_full_name, rows[3], rows[4], rows[2], rows[1], rows[0]));
                contact.setText(String.format("%s\n%s, %s, %s", rows[4], rows[5], rows[6], rows[7]));

                //set Logo
                Bitmap bitmap = BitmapFactory.decodeByteArray(school_logo.get(0), 0, school_logo.get(0).length);
                logo.setImageBitmap(bitmap);

                //show
                call.setVisibility(View.VISIBLE);
                lyt_form.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            } else {
                Tools.toast("Oops! This school has not yrt published her school information.", SchoolInformation.this, R.color.red_500, Toast.LENGTH_LONG);
                finish();
            }
        }
    }
}
