package mountedwings.org.mskola_mgt.student;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Objects;

import mountedwings.org.mskola_mgt.R;

public class List_of_schools extends AppCompatActivity {
    private RecyclerView list;
    private ProgressBar progressBar;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_schools);
        initToolbar();
        initComponent();
    }

    private void initComponent() {
        progressBar = findViewById(R.id.progress);
        cardView = findViewById(R.id.card_schools);
        list = findViewById(R.id.list);
        cardView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

    }

    private void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Your School(s)");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
