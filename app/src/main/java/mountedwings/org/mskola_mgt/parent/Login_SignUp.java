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

package mountedwings.org.mskola_mgt.parent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.Sign_Up;
import mountedwings.org.mskola_mgt.utils.Tools;


public class Login_SignUp extends AppCompatActivity {

    private static final int MAX_STEP = 3;

    private ViewPager viewPager;

    private String title_array[] = {
            "Be part of the classroom",
            "Interconnectivity",
            "Ease of School fees Payment"
    };
    private String description_array[] = {
            "Stay in touch with your child's continuous assessments, assignments and term's results.",
            "Instant messaging with teachers and your kids. Also get notifications from the school about special activities",
            "Access schools' online fees portal, pay at your comfort and keep track of payments.",
    };
    private int about_images_array[] = {
            R.drawable.part_of_class,
            R.drawable.img_wizard_2,
            R.drawable.payment
    };
    private int color_array[] = {
            R.drawable.image_10,
            R.drawable.image_3,
            R.drawable.image_23
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepper_wizard_color);
        initComponent();
        Tools.clearSystemBarLight(this);
    }

    private void initComponent() {
        viewPager = findViewById(R.id.view_pager);

        // adding bottom dots
        bottomProgressDots(0);
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);


    }

    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = findViewById(R.id.layoutDots);
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

        private MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_card_wizard_bg, container, false);
            ((TextView) view.findViewById(R.id.title)).setText(title_array[position]);
            ((TextView) view.findViewById(R.id.description)).setText(description_array[position]);
            ((ImageView) view.findViewById(R.id.image)).setImageResource(about_images_array[position]);
            ((ImageView) view.findViewById(R.id.image_bg)).setImageResource(color_array[position]);

            Button btnNext = view.findViewById(R.id.btn_next);

            if (position == title_array.length - 1) {
                btnNext.setText("Get Started");
            } else {
                btnNext.setText("Next");
            }

            btnNext.setOnClickListener(v -> {
                int current = viewPager.getCurrentItem() + 1;
                if (current < MAX_STEP) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    showCustomDialog();
                }
            });

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

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info_login);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        // Animate the close button
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        final Transition transition = new Transition() {
            @Override
            public void captureEndValues(@NonNull TransitionValues transitionValues) {

            }

            @Override
            public void captureStartValues(@NonNull TransitionValues transitionValues) {

            }
        };

        animation.setDuration(800); // duration - 2 seconds
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        ImageView img = dialog.findViewById(R.id.close);
        img.startAnimation(animation);

        dialog.findViewById(R.id.close).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.bt_sign_up).setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), Sign_Up.class);
            intent.putExtra("account_type", "Parent");
            startActivity(intent);
            dialog.dismiss();
            finish();
        });
        dialog.findViewById(R.id.bt_login).setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), mountedwings.org.mskola_mgt.parent.MskolaLogin.class));
            dialog.dismiss();
            finish();
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}