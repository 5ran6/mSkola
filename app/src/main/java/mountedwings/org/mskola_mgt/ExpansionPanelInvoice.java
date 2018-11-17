package mountedwings.org.mskola_mgt;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;


public class ExpansionPanelInvoice extends AppCompatActivity {

    private ImageButton bt_toggle_description;
    private View lyt_expand_items, lyt_expand_address, lyt_expand_description;
    private NestedScrollView nested_scroll_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expansion_panel_invoice);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private void initComponent() {

        // nested scrollview
        nested_scroll_view = findViewById(R.id.nested_scroll_view);

        // section items
        ImageButton bt_toggle_items = findViewById(R.id.bt_toggle_items);
        lyt_expand_items = findViewById(R.id.lyt_expand_items);
        bt_toggle_items.setOnClickListener(view -> toggleSection(view, lyt_expand_items));

        // section address
        ImageButton bt_toggle_address = findViewById(R.id.bt_toggle_address);
        lyt_expand_address = findViewById(R.id.lyt_expand_address);
        bt_toggle_address.setOnClickListener(view -> toggleSection(view, lyt_expand_address));

        // section description
        bt_toggle_description = findViewById(R.id.bt_toggle_description);
        lyt_expand_description = findViewById(R.id.lyt_expand_description);
        bt_toggle_description.setOnClickListener(view -> toggleSection(view, lyt_expand_description));

        // copy to clipboard
        final TextView tv_invoice_code = findViewById(R.id.tv_invoice_code);
        ImageButton bt_copy_code = findViewById(R.id.bt_copy_code);
        bt_copy_code.setOnClickListener(view -> Tools.copyToClipboard(getApplicationContext(), tv_invoice_code.getText().toString()));

    }


    private void toggleSection(View bt, final View lyt) {
        boolean show = toggleArrow(bt);
        if (show) {
            ViewAnimation.expand(lyt, () -> Tools.nestedScrollTo(nested_scroll_view, lyt));
        } else {
            ViewAnimation.collapse(lyt);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
