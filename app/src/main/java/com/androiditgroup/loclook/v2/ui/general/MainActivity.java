package com.androiditgroup.loclook.v2.ui.general;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.ui.SplashActivity;
import com.androiditgroup.loclook.v2.utils.CustomTypefaceSpan;
import com.androiditgroup.loclook.v2.utils.LockableSlidingPane;
import com.androiditgroup.loclook.v2.utils.ParentActivity;

/**
 * Created by sostrovschi on 10.01.2017
 */

public class MainActivity   extends     ParentActivity
                            implements  NavigationView.OnNavigationItemSelectedListener {

    private TextView mToolbarTitle;
    private ImageButton mNavigationBtn;
    private ProgressBar mProgressBar;

    private static NavigationView navigationView;
    public static LockableSlidingPane mSlidingPaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("ABC", "MainActivity: onCreate()");

        // initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        getFragmentManager().addOnBackStackChangedListener(this);

        // initialize sliding panel layout
        mSlidingPaneLayout = (LockableSlidingPane) findViewById(R.id.MainActivity_SlidingPanel);
        assert mSlidingPaneLayout != null;
        mSlidingPaneLayout.setCoveredFadeColor(getResources().getColor(R.color.colorPrimary));
        mSlidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
        mSlidingPaneLayout.setShadowDrawableLeft(getResources().getDrawable(R.drawable.nav_shadow));
        mSlidingPaneLayout.setParallaxDistance(200);

        mToolbarTitle = (TextView) findViewById(R.id.main_activity_toolbarTitle);
        mToolbarTitle.setTypeface(LocLookApp.semiBold);

        // initialize toolbar button
        mNavigationBtn = (ImageButton) findViewById(R.id.toolbar_navButton);
        assert mNavigationBtn != null;
        mNavigationBtn.setOnClickListener(mMenuClickListener);

        Log.e("ABC", "MainActivity: onCreate(): isLoggedIn: " +(LocLookApp.getInstance().isLoggedIn()));

        // if user is not logged in
        if (!LocLookApp.getInstance().isLoggedIn()) {
            Intent loginIntent = new Intent(this, SplashActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        navigationView = (NavigationView) findViewById(R.id.MainActivity_NavigationView);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        for (int i = 1; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            applyTypeFaceToMenu(item);
        }

        refreshNavItemTitles();

        mProgressBar = (ProgressBar) findViewById(R.id.mainActivityProgressBar);

    }

    private View.OnClickListener mMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSlidingPaneLayout.openPane();
        }
    };

    @Override
    public void setToolbarTitle(String title) {

    }

    @Override
    public void setFragment(Fragment fragment, boolean animate, boolean addToBackStack) {

    }

    @Override
    public void changeFragment(Fragment fragment) {

    }

    @Override
    public void toggleLoadingProgress(boolean show) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /*

        FragmentManager fm = getFragmentManager();
        for (int f = 0; f < fm.getBackStackEntryCount(); f++) {
            fm.popBackStack();
        }
        switch (item.getItemId()) {
            case R.id.nav_main:
                changeFragment(CategoriesFragment.newInstance());
                selectedFragment = SelectedFragment.supercategory;
                break;
            case R.id.nav_login:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                YellowApp.getInstance().setLoginStatus(false);
                YellowApp.getInstance().setIsStarnetUser(false);
                DBManager.getInstance().deleteDB(Constants.DataBase.HISTORY_TABLE);
                finish();
                return true;
            case R.id.nav_bookings:
                addFragment(new BookingsList(), false);
                selectedFragment = SelectedFragment.MY_BOOKINGS;
                break;
            case R.id.nav_favorites:
                addFragment(new FavoritesFragment(), false);
                selectedFragment = SelectedFragment.favorites_list;
                break;
            case R.id.nav_history:
                addFragment(new HistoryFragment(), false);
                selectedFragment = SelectedFragment.histories_list;
                break;
            case R.id.nav_settings:
                Intent settings = new Intent(this, SettingsFragment.class);
                startActivity(settings);
                break;
            case R.id.nav_card:
                addFragment(new CardFragment(), false);
                selectedFragment = SelectedFragment.CARDS;
                break;
        }
        mNavigationBtn.setOnClickListener(mMenuClickListener);
        mNavigationBtn.setImageResource(R.drawable.ic_nav_side_menu);
        mHotOffersBtn.setImageResource(R.drawable.ic_hot_offer);
        mHotOffersBtn.setOnClickListener(mHotOffersListener);
        if (mSlidingPaneLayout.isOpen()) {
            mSlidingPaneLayout.closePane();
        }
        return true;

         */

        return false;
    }

    private void applyTypeFaceToMenu(MenuItem mi) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", LocLookApp.extraBold), 0, mNewTitle.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public void refreshNavItemTitles() {
//        Menu navMenu = navigationView.getMenu();

//        MenuItem main = navMenu.findItem(R.id.nav_main);
//        main.setTitle(LocalizationManager.getInstance().getLocalizedString("main").toUpperCase());
//
//        MenuItem myBookings = navMenu.findItem(R.id.nav_bookings);
//        myBookings.setTitle(LocalizationManager.getInstance().getLocalizedString("my_bookings").toUpperCase());
//
//        MenuItem favorites = navMenu.findItem(R.id.nav_favorites);
//        favorites.setTitle(LocalizationManager.getInstance().getLocalizedString("favorites").toUpperCase());
//
//        MenuItem history = navMenu.findItem(R.id.nav_history);
//        history.setTitle(LocalizationManager.getInstance().getLocalizedString("history").toUpperCase());
//
//        MenuItem settings = navMenu.findItem(R.id.nav_settings);
//        settings.setTitle(LocalizationManager.getInstance().getLocalizedString("settings").toUpperCase());
//
//        MenuItem exit = navMenu.findItem(R.id.nav_login);
//        exit.setTitle(LocalizationManager.getInstance().getLocalizedString("log_out").toUpperCase());
//
//        if (YellowApp.getInstance().isStarnetUser()) {
//            MenuItem card = navMenu.findItem(R.id.nav_card);
//            card.setTitle(LocalizationManager.getInstance().getLocalizedString("my_card").toUpperCase());
//        } else {
//            navMenu.removeItem(R.id.nav_card);
//        }
    }
}
