package com.androiditgroup.loclook.v2.ui.general;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
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
import com.androiditgroup.loclook.v2.models.User;
import com.androiditgroup.loclook.v2.ui.SplashActivity;
import com.androiditgroup.loclook.v2.ui.auth.PhoneNumberFragment;
import com.androiditgroup.loclook.v2.ui.favorites.FavoritesFragment;
import com.androiditgroup.loclook.v2.ui.feed.FeedFragment;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.CustomTypefaceSpan;
import com.androiditgroup.loclook.v2.utils.DBManager;
import com.androiditgroup.loclook.v2.utils.LockableSlidingPane;
import com.androiditgroup.loclook.v2.utils.ParentActivity;
import com.androiditgroup.loclook.v2.utils.ParentFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

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

        // initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.MainActivity_Toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // getFragmentManager().addOnBackStackChangedListener(this);

        // initialize sliding panel layout
        mSlidingPaneLayout = (LockableSlidingPane) findViewById(R.id.MainActivity_SlidingPanel);
        assert mSlidingPaneLayout != null;
        mSlidingPaneLayout.setCoveredFadeColor(getResources().getColor(R.color.colorPrimary));
        mSlidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
        mSlidingPaneLayout.setShadowDrawableLeft(getResources().getDrawable(R.drawable.nav_shadow));
        mSlidingPaneLayout.setParallaxDistance(200);

        mToolbarTitle = (TextView) findViewById(R.id.MainActivity_ToolbarTitle);
        mToolbarTitle.setTypeface(LocLookApp.semiBold);

        // initialize toolbar button
        // mNavigationBtn = (ImageButton) findViewById(R.id.toolbar_navButton);
        mNavigationBtn = (ImageButton) findViewById(R.id.MainActivity_Toolbar_NavButton);
        assert mNavigationBtn != null;
        mNavigationBtn.setOnClickListener(mMenuClickListener);

        navigationView = (NavigationView) findViewById(R.id.MainActivity_NavigationView);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            applyTypeFaceToMenu(item);
        }

        // if user is not logged in
        if (!LocLookApp.getInstance().isLoggedIn()) {
            Intent loginIntent = new Intent(this, SplashActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }
        // if user enter without logation
        else {

            String phoneNumber = LocLookApp.getInstance().getPhoneNumber();

            if (phoneNumber != null) {

                Cursor data = DBManager.getInstance().queryColumns(Constants.DataBase.USER_DATA_TABLE, null, "PHONE_NUMBER", phoneNumber);

                // если данные получены
                if (data.getCount() > 0) {
                    // формируем пользователя приложения
                    if (setUserData(data))
                        setUserDataOnNavView();
                }
            }
        }

        mProgressBar = (ProgressBar) findViewById(R.id.mainActivityProgressBar);

        setFragment(FeedFragment.newInstance(), false, false);
    }

    private View.OnClickListener mMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSlidingPaneLayout.openPane();
        }
    };

    @Override
    public void setFragment(Fragment fragment, boolean animate, boolean addToBackStack) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (animate) {
            transaction.setCustomAnimations(R.animator.slide_in, R.animator.slide_out, R.animator.slide_in, R.animator.slide_out);
        } else {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }

        transaction.replace(R.id.MainActivity_FragmentContainer, fragment, fragment.getClass().getName());

        if(addToBackStack)
            transaction.addToBackStack(fragment.getClass().getName());

        transaction.commit();

        if(fragment instanceof ParentFragment)
            setToolbarTitle(((ParentFragment) fragment).getFragmentTitle());
    }

    @Override
    public void setToolbarTitle(final String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToolbarTitle.setText(title);
            }
        });
    }

    @Override
    public void changeFragment(Fragment fragment) {

    }

    @Override
    public void toggleLoadingProgress(boolean show) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fm = getFragmentManager();
        for (int f = 0; f < fm.getBackStackEntryCount(); f++) {
            fm.popBackStack();
        }
        switch (item.getItemId()) {
            case R.id.nav_feed:
                setFragment(FeedFragment.newInstance(), false, false);
//                selectedFragment = SelectedFragment.supercategory;
                break;
            case R.id.nav_exit:
                Intent logoutIntent = new Intent(this, SplashActivity.class);
                startActivity(logoutIntent);
                LocLookApp.getInstance().logOut();
                finish();
                return true;
            case R.id.nav_favorites:
                setFragment(FavoritesFragment.newInstance(), false, true);
//                selectedFragment = SelectedFragment.MY_BOOKINGS;
                break;
            case R.id.nav_notifications:
//                addFragment(new FavoritesFragment(), false);
//                selectedFragment = SelectedFragment.favorites_list;
                break;
            case R.id.nav_badges:
//                addFragment(new HistoryFragment(), false);
//                selectedFragment = SelectedFragment.histories_list;
                break;
            case R.id.nav_geolocation:
//                Intent settings = new Intent(this, SettingsFragment.class);
//                startActivity(settings);
                break;
        }
        mNavigationBtn.setOnClickListener(mMenuClickListener);
        mNavigationBtn.setImageResource(R.drawable.menu_icon);
        if (mSlidingPaneLayout.isOpen()) {
            mSlidingPaneLayout.closePane();
        }
        return true;
    }

    private void applyTypeFaceToMenu(MenuItem mi) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", LocLookApp.extraBold), 0, mNewTitle.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

//    public void refreshNavItemTitles() {
//        Menu navMenu = navigationView.getMenu();
//
//        MenuItem feed = navMenu.findItem(R.id.nav_feed);
//        feed.setTitle(R.string.feed_text);
//
//        MenuItem favorites = navMenu.findItem(R.id.nav_favorites);
//        favorites.setTitle(R.string.favorites_text);
//
//        MenuItem notifications = navMenu.findItem(R.id.nav_notifications);
//        notifications.setTitle(R.string.notifications_text);
//
//        MenuItem badges = navMenu.findItem(R.id.nav_badges);
//        badges.setTitle(R.string.badges_text);
//
//        MenuItem geolocation = navMenu.findItem(R.id.nav_geolocation);
//        geolocation.setTitle(R.string.geolocation_text);
//
//        MenuItem exit = navMenu.findItem(R.id.nav_exit);
//        exit.setTitle(R.string.exit_text);
//    }

    // public void setUserData(final User user) {
    public void setUserDataOnNavView() {
        View navHeaderView = navigationView.getHeaderView(0);
        final TextView userName = (TextView) navHeaderView.findViewById(R.id.navHeaderUserName);
        userName.setTypeface(LocLookApp.light);

        // final CircularImageView userImage = (CircularImageView) navHeaderView.findViewById(R.id.navHeaderUserImage);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(LocLookApp.user != null) {
                    userName.setText(LocLookApp.user.getName());
//                    if (LocLookApp.user.getImage() != null) {
//                        userImage.setImageBitmap(user.getImage());
//                    } else {
//                        userImage.setImageResource(R.drawable.userpic_holder);
//                    }
                }
            }
        });
    }
}
