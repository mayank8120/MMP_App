package com.example.application;

import static com.example.application.R.layout.activity_main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    static final float END_SCALE = 0.7f;

    ImageView menuIcon;
    LinearLayout contentView;

    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(activity_main);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        naviagtionDrawer();


        getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer, new FragmentHome()).commit();
        navigationView.setCheckedItem(R.id.nav_home);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            Fragment temp;

            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        temp = new FragmentHome();
                        break;
                    case R.id.nav_leaderboard:
                        temp = new FragmentLeaderBoard();
                        break;
//                    case R.id.nav_basicplan:
//                        temp = new FragmentBasicPlans();
//                        break;
//                    case R.id.nav_proplans:
//                        temp = new FragmentProPlans();
//                        break;

                    case R.id.nav_referral:
                        temp = new FragmentReferrals();
                        break;
                    case R.id.nav_setting:
                        temp = new FragmentSettings();
                        break;
                    case R.id.nav_contactus:
                        temp = new FragmentContactUs();
                        break;
                    case R.id.nav_plansinfo:
                        temp = new PLansDetailsFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.framecontainer, temp).commit();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }


//            @Override
//            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
//                return false;
//            }
        });


    }

    //Navigation Drawer Functions
    private void naviagtionDrawer() {

        //Naviagtion Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {

        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }


}