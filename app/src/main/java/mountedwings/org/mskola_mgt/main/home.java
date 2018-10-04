package mountedwings.org.mskola_mgt.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ncapdevi.fragnav.FragNavController;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.Splash;
import mountedwings.org.mskola_mgt.utils.Tools;


public class home extends AppCompatActivity {
    private TextView mTextMessage;
    private BottomNavigationView navigation;
    private View search_bar;
    private FragNavController fragNavController;

    //indices to fragments
    private final int TAB_FIRST = FragNavController.TAB1;
    private final int TAB_SECOND = FragNavController.TAB2;
    private final int TAB_THIRD = FragNavController.TAB3;
    private final int TAB_FOURTH = FragNavController.TAB4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_shifting);
        initComponent(savedInstanceState);
    }

    private void initComponent(Bundle instance) {
        Tools.clearSystemBarLight(this);
        search_bar = (View) findViewById(R.id.search_bar);
        mTextMessage = (TextView) findViewById(R.id.search_text);

        //FragNav
        //list of fragments
        List<Fragment> fragments = new ArrayList<>(4);

        //add fragments to list
        fragments.add(homeFragment.newInstance(0));
        fragments.add(academics.newInstance(0));
        fragments.add(bursary.newInstance(0));
        fragments.add(administration.newInstance(0));

        //link fragments to container
        fragNavController = new FragNavController(getSupportFragmentManager(), R.id.container, fragments);
        //start at tab one
        fragNavController.switchTab(TAB_FIRST);
        //End of FragNav

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        fragNavController.switchTab(TAB_FIRST);
//                        Tools.setSystemBarColor(home.this, R.color.green_50);
//                        Tools.setSystemBarLight(home.this);
                        //    mTextMessage.setText(item.getTitle());
                        navigation.setBackgroundColor(getResources().getColor(R.color.green_400));
//                        Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.food_order:
                        fragNavController.switchTab(TAB_SECOND);
//                        Tools.setSystemBarColor(home.this, R.color.pink_50);
//                        Tools.setSystemBarLight(home.this);
                        //              mTextMessage.setText(item.getTitle());
                        navigation.setBackgroundColor(getResources().getColor(R.color.pink_800));
                        //                      Toast.makeText(getApplicationContext(), "Order", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.social_hub:
                        fragNavController.switchTab(TAB_THIRD);
//                        Tools.setSystemBarColor(home.this, R.color.teal_50);
//                        Tools.setSystemBarLight(home.this);

                        //   mTextMessage.setText(item.getTitle());
                        navigation.setBackgroundColor(getResources().getColor(R.color.teal_800));
                        //                    Toast.makeText(getApplicationContext(), "Social Hub", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.favourite:
                        fragNavController.switchTab(TAB_FOURTH);
//                        Tools.setSystemBarColor(home.this, R.color.blue_50);
//                        Tools.setSystemBarLight(home.this);
                        // mTextMessage.setText(item.getTitle());
                        navigation.setBackgroundColor(getResources().getColor(R.color.blue_700));
                        //                  Toast.makeText(getApplicationContext(), "Favourite", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        fragNavController.clearStack();
                }
                return false;
            }
        });

        //Navigation drawer
        new DrawerBuilder().withActivity(this).build();

        //primary items
        PrimaryDrawerItem profile = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName(R.string.drawer_item_1)
                .withIcon(R.drawable.ic_person_outline);
        PrimaryDrawerItem primary_item1 = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName(R.string.drawer_item_2)
                .withIcon(R.drawable.ic_dvr_black_24dp);
        PrimaryDrawerItem primary_item2 = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName(R.string.drawer_item_3)
                .withIcon(R.drawable.ic_whatshot_black_24dp);
        //secondary items
        SecondaryDrawerItem secondary_item1 = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(11)
                .withName(R.string.drawer_item_4)
                .withIcon(R.drawable.ic_settings_black_24dp);
        SecondaryDrawerItem secondary_item2 = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(12)
                .withName(R.string.drawer_item_5)
                .withIcon(R.drawable.info);
        SecondaryDrawerItem secondary_item3 = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(13)
                .withName(R.string.drawer_item_6)
                .withIcon(R.drawable.help);
        //logout
        SecondaryDrawerItem logout = (SecondaryDrawerItem) new SecondaryDrawerItem()
                .withIdentifier(97)
                .withName(R.string.drawer_item_7)
                .withIcon(R.drawable.ic_exit_to_app_black_24dp);

        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        new DrawerBuilder()
                .withActivity(this)
//                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(true)
                .withFullscreen(true)
                .withSavedInstance(instance)
                .withHeader(R.layout.drawer_header)
                .withFooter(R.layout.chip_view)
                .withFooterClickable(true)
                .addDrawerItems(
                        new SectionDrawerItem().withName(getString(R.string.personal)),
                        profile,
                        primary_item1,
                        primary_item2,
                        new SectionDrawerItem().withName(getString(R.string.category)),
                        secondary_item1,
                        secondary_item2,
                        secondary_item3,
                        new DividerDrawerItem(),
                        logout

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                //     intent = new Intent(getApplicationContext(), AttendanceActivity.class);
                            } else if (drawerItem.getIdentifier() == 2) {
                                //intent = new Intent(getApplicationContext(), Class.class);
                            } else if (drawerItem.getIdentifier() == 3) {
                                //intent = new Intent(getApplicationContext(), Class.class);
                            } else if (drawerItem.getIdentifier() == 11) {
                                //intent = new Intent(getApplicationContext(), Class.class);
                            } else if (drawerItem.getIdentifier() == 12) {
                                //intent = new Intent(getApplicationContext(), Class.class);
                            } else if (drawerItem.getIdentifier() == 13) {
                                //intent = new Intent(getApplicationContext(), Class.class);
                            } else if (drawerItem.getIdentifier() == 97) {
                                //   intent = new Intent(getApplicationContext(), Settings.class);
                            } else if (drawerItem.getIdentifier() == 98) {
                                // intent = new Intent(getApplicationContext(), Help.class);
                            } else if (drawerItem.getIdentifier() == 99) {
                                //intent = new Intent(getApplicationContext(), Contact.class);
                            }
                            if (intent != null) {
                                getApplicationContext().startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .build();
        //End of Navigation drawer


        // display image
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_1), R.drawable.image_8);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_2), R.drawable.image_9);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_3), R.drawable.image_15);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_4), R.drawable.image_14);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_5), R.drawable.image_12);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_6), R.drawable.image_2);
        Tools.displayImageOriginal(this, (ImageView) findViewById(R.id.image_7), R.drawable.image_5);

//        ((ImageButton) findViewById(R.id.bt_menu)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }


    boolean isNavigationHide = false;

    private void animateNavigation(final boolean hide) {
        if (isNavigationHide && hide || !isNavigationHide && !hide) return;
        isNavigationHide = hide;
        int moveY = hide ? (2 * navigation.getHeight()) : 0;
        navigation.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    boolean isSearchBarHide = false;

    private void animateSearchBar(final boolean hide) {
        if (isSearchBarHide && hide || !isSearchBarHide && !hide) return;
        isSearchBarHide = hide;
        int moveY = hide ? -(2 * search_bar.getHeight()) : 0;
        search_bar.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        fragNavController.getCurrentStack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // When orientation is changed, activity goes through onResume()
        try {
//            Class fragmentClass = homeFragment.class;
//            Fragment myFragment;
//            myFragment = (Fragment) fragmentClass.newInstance();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.container, myFragment).commit();
//            Tools.setSystemBarColor(home.this, R.color.green_50);
//            Tools.setSystemBarLight(home.this);
//            //    mTextMessage.setText(item.getTitle());
//            navigation.setBackgroundColor(getResources().getColor(R.color.green_400));
//            navigation.setSelectedItemId(R.id.home);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register(View view) {
        //intent to registration activity
    }

    public void signIn(View view) {
        //intent to signIn activity
        Intent intent = new Intent(getApplicationContext(), Splash.class);
        startActivity(intent);
    }

    public void profile(View view) {
        // TODO Intent to go to profile activity
    }

    public void coin(View view) {

    }
}
