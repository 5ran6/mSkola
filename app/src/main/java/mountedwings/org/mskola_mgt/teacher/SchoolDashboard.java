package mountedwings.org.mskola_mgt.teacher;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.Tools;
import mountedwings.org.mskola_mgt.utils.ViewAnimation;


public class SchoolDashboard extends AppCompatActivity {

    private NestedScrollView nested_scroll_view;
    private CardView bt_toggle_info_messages, bt_toggle_info_academics, bt_toggle_info_cbt, bt_toggle_info_bursary, bt_toggle_info_achievements;
    private ImageButton bt_toggle_info_messages1, bt_toggle_info_academics1, bt_toggle_info_cbt1, bt_toggle_info_bursary1, bt_toggle_info_achievements1;
    private Button bt_hide_info_messages, bt_hide_info_academics, bt_hide_info_cbt, bt_hide_info_bursary, bt_hide_info_achievements;
    private View lyt_expand_info_academics, lyt_expand_info_cbt, lyt_expand_info_bursary, lyt_expand_info_messages, lyt_expand_info_achievements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_dashboard);
      //  initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        nested_scroll_view = findViewById(R.id.nested_scroll_view);

        // info item_academics
        bt_toggle_info_academics = findViewById(R.id.bt_toggle_info_academics);
        bt_toggle_info_academics1 = findViewById(R.id.bt_toggle_info_academics1);
        bt_hide_info_academics = findViewById(R.id.bt_hide_info_academics);
        lyt_expand_info_academics = findViewById(R.id.lyt_expand_info_academics);
        bt_toggle_info_academics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoAcademics(bt_toggle_info_academics1);
            }
        });
        bt_toggle_info_academics1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoAcademics(bt_toggle_info_academics1);
            }
        });
        bt_hide_info_academics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoAcademics(bt_toggle_info_academics1);
            }
        });

        // info item_bursary
        bt_toggle_info_bursary = findViewById(R.id.bt_toggle_info_bursary);
        bt_toggle_info_bursary1 = findViewById(R.id.bt_toggle_info_bursary1);
        bt_hide_info_bursary = findViewById(R.id.bt_hide_info_bursary);
        lyt_expand_info_bursary = findViewById(R.id.lyt_expand_info_bursary);
        bt_toggle_info_bursary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoBursary(bt_toggle_info_bursary1);
            }
        });
        bt_toggle_info_bursary1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoBursary(bt_toggle_info_bursary1);
            }
        });

        bt_hide_info_bursary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoBursary(bt_toggle_info_bursary1);
            }
        });

        // info item_cbt
        bt_toggle_info_cbt = findViewById(R.id.bt_toggle_info_cbt);
        bt_toggle_info_cbt1 = findViewById(R.id.bt_toggle_info_cbt1);
        bt_hide_info_cbt = findViewById(R.id.bt_hide_info_cbt);
        lyt_expand_info_cbt = findViewById(R.id.lyt_expand_info_cbt);
        bt_toggle_info_cbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoCBT(bt_toggle_info_cbt1);
            }
        });
        bt_toggle_info_cbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoCBT(bt_toggle_info_cbt1);
            }
        });

        bt_hide_info_cbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoCBT(bt_toggle_info_cbt1);
            }
        });

        // info item_achievements
        bt_toggle_info_achievements = findViewById(R.id.bt_toggle_info_achievements);
        bt_toggle_info_achievements1 = findViewById(R.id.bt_toggle_info_achievements1);
        bt_hide_info_achievements = findViewById(R.id.bt_hide_info_achievements);
        lyt_expand_info_achievements = findViewById(R.id.lyt_expand_info_achievements);
        bt_toggle_info_achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoAchievements(bt_toggle_info_achievements1);
            }
        });
        bt_toggle_info_achievements1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoAchievements(bt_toggle_info_achievements1);
            }
        });

        bt_hide_info_achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoAchievements(bt_toggle_info_achievements1);
            }
        });

        // info item_messages
        bt_toggle_info_messages = findViewById(R.id.bt_toggle_info_messages);
        bt_toggle_info_messages1 = findViewById(R.id.bt_toggle_info_messages1);
        bt_hide_info_messages = findViewById(R.id.bt_hide_info_messages);
        lyt_expand_info_messages = findViewById(R.id.lyt_expand_info_messages);
        bt_toggle_info_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoMessages(bt_toggle_info_messages1);
            }
        });
        bt_toggle_info_messages1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoMessages(bt_toggle_info_messages1);
            }
        });

        bt_hide_info_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInfoMessages(bt_toggle_info_messages1);
            }
        });

    }

    private void toggleSectionInfoAcademics(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info_academics, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info_academics);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_info_academics);
        }
    }

    private void toggleSectionInfoBursary(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info_bursary, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info_bursary);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_info_bursary);
        }
    }

    private void toggleSectionInfoCBT(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info_cbt, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info_cbt);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_info_cbt);
        }
    }

    private void toggleSectionInfoAchievements(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info_achievements, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info_achievements);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_info_achievements);
        }
    }

    private void toggleSectionInfoMessages(View view) {
        boolean show = toggleArrow(view);
        if (show) {
            ViewAnimation.expand(lyt_expand_info_messages, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, lyt_expand_info_messages);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_info_messages);
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
