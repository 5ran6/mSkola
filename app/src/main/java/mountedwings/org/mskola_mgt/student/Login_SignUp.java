package mountedwings.org.mskola_mgt.student;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mountedwings.org.mskola_mgt.MskolaLogin;
import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.Sign_Up;
import mountedwings.org.mskola_mgt.utils.Tools;


public class Login_SignUp extends AppCompatActivity {

    private static final int MAX_STEP = 3;


    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private Button btn_login, btn_sign_up;
    private String title_array[] = {
            "Track your Academic Records",
            "Interconnectivity",
            "Assignments"
    };
    private String description_array[] = {
            "Records continuous assessments, give assignments and compile results.",
            "Instant messaging with parents and teachers.",
            "Access and submit assignments.",
    };
    private int about_images_array[] = {
            R.drawable.records,
            R.drawable.img_wizard_2,
            R.drawable.homework,
    };
    private int color_array[] = {
            R.color.blue_grey_600,
            R.color.red_600,
            R.color.purple_600
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper_wizard_color);
        initComponent();
        Tools.clearSystemBarLight(this);
    }

    private void initComponent() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        btn_login = (Button) findViewById(R.id.login);
        btn_sign_up = (Button) findViewById(R.id.sign_up);

        // adding bottom dots
        bottomProgressDots(0);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btn_login.setVisibility(View.GONE);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MskolaLogin.class));
            }
        });
        btn_sign_up.setVisibility(View.GONE);
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Sign_Up.class));
            }
        });

    }

    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.overlay_dark_30), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.grey_10), PorterDuff.Mode.SRC_IN);
        }
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);
            if (position == title_array.length - 1) {
                btn_login.setVisibility(View.VISIBLE);
                btn_sign_up.setVisibility(View.VISIBLE);
            } else {
                btn_login.setVisibility(View.GONE);
                btn_sign_up.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_stepper_wizard_color, container, false);
            ((TextView) view.findViewById(R.id.title)).setText(title_array[position]);
            ((TextView) view.findViewById(R.id.description)).setText(description_array[position]);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(about_images_array[position]);
            ((RelativeLayout) view.findViewById(R.id.lyt_parent)).setBackgroundColor(getResources().getColor(color_array[position]));
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return title_array.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}