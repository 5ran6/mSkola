package mountedwings.org.mskola_mgt.parent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.adapter.ParentAssessmentView;
import mountedwings.org.mskola_mgt.data.NumberParentAssessmentView;

public class AssessmentView extends AppCompatActivity {
    private ArrayList<NumberParentAssessmentView> numbers = new ArrayList<>();
    private ParentAssessmentView adapter;
    private String text, session, term, subject, class_name, TAG = "mSkola";
    private RecyclerView list;
    private TextView name;
    private ArrayList<String> cas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_assessment_view);
        initToolbar();
        list = findViewById(R.id.assessments);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(false);


        text = getIntent().getStringExtra("text");
        session = getIntent().getStringExtra("session");
        term = getIntent().getStringExtra("term");
        subject = getIntent().getStringExtra("subject");
        class_name = getIntent().getStringExtra("class_name");
        text = getIntent().getStringExtra("text");
        text = getIntent().getStringExtra("text");

        // substring
        substring(text);
        adapter = new ParentAssessmentView(numbers);
        list.setAdapter(adapter);
    }

    private void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.students_name);
    }

    private void substring(String string) {
        //text = Exams;CA1;C2;CA3........;STUDENT NAME##NO OF CAs
        String[] noCas = string.split("##");

        switch (noCas[0].trim()) {
            case "1":
                cas.add(0, "Exams");
                cas.add(1, "CA1");
                break;
            case "2":
                cas.add(0, "Exams");
                cas.add(1, "CA1");
                cas.add(2, "CA2");
                break;
            case "3":
                cas.add(0, "Exams");
                cas.add(1, "CA1");
                cas.add(2, "CA2");
                cas.add(3, "CA3");
                break;
            case "4":
                cas.add(0, "Exams");
                cas.add(1, "CA1");
                cas.add(2, "CA2");
                cas.add(3, "CA3");
                cas.add(4, "CA4");
                break;
            case "5":
                cas.add(0, "Exams");
                cas.add(1, "CA1");
                cas.add(2, "CA2");
                cas.add(3, "CA3");
                cas.add(4, "CA4");
                cas.add(5, "CA5");
                break;
            case "6":
                cas.add(0, "Exams");
                cas.add(1, "CA1");
                cas.add(2, "CA2");
                cas.add(3, "CA3");
                cas.add(4, "CA4");
                cas.add(5, "CA5");
                cas.add(6, "CA6");
                break;
            case "7":
                cas.add(0, "Exams");
                cas.add(1, "CA1");
                cas.add(2, "CA2");
                cas.add(3, "CA3");
                cas.add(4, "CA4");
                cas.add(5, "CA5");
                cas.add(6, "CA6");
                cas.add(7, "CA7");
                break;
            case "8":
                cas.add(0, "Exams");
                cas.add(1, "CA1");
                cas.add(2, "CA2");
                cas.add(3, "CA3");
                cas.add(4, "CA4");
                cas.add(5, "CA5");
                cas.add(6, "CA6");
                cas.add(7, "CA7");
                cas.add(8, "CA8");
                break;
            case "9":
                cas.add(0, "Exams");
                cas.add(1, "CA1");
                cas.add(2, "CA2");
                cas.add(3, "CA3");
                cas.add(4, "CA4");
                cas.add(5, "CA5");
                cas.add(6, "CA6");
                cas.add(7, "CA7");
                cas.add(8, "CA8");
                cas.add(9, "CA9");
                break;
            case "10":
                cas.add(0, "Exams");
                cas.add(1, "CA1");
                cas.add(2, "CA2");
                cas.add(3, "CA3");
                cas.add(4, "CA4");
                cas.add(5, "CA5");
                cas.add(6, "CA6");
                cas.add(7, "CA7");
                cas.add(8, "CA8");
                cas.add(9, "CA9");
                cas.add(10, "CA10");
                break;
        }

        String[] scores = string.split(";");
        name.setText(scores[cas.size()].toUpperCase());
        for (int i = 1; i <= cas.size(); i++) {
            NumberParentAssessmentView number = new NumberParentAssessmentView();
            number.setCa(cas.get(i));
            number.setScore(scores[i]);
            numbers.add(number);
        }

    }

    public void done(View view) {
        finish();
    }
}
