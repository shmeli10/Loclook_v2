package com.androiditgroup.loclook.v2.ui.general;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.models.User;
import com.androiditgroup.loclook.v2.ui.SplashActivity;
import com.androiditgroup.loclook.v2.ui.auth.PhoneNumberFragment;
import com.androiditgroup.loclook.v2.ui.badges.BadgesFragment;
import com.androiditgroup.loclook.v2.ui.favorites.FavoritesFragment;
import com.androiditgroup.loclook.v2.ui.feed.FeedFragment;
import com.androiditgroup.loclook.v2.ui.geolocation.GeolocationFragment;
import com.androiditgroup.loclook.v2.ui.notifications.NotificationsFragment;
import com.androiditgroup.loclook.v2.ui.publication.PublicationFragment;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.CustomTypefaceSpan;
import com.androiditgroup.loclook.v2.utils.DBManager;
import com.androiditgroup.loclook.v2.utils.DefineUserLocationName;
import com.androiditgroup.loclook.v2.utils.LockableSlidingPane;
import com.androiditgroup.loclook.v2.utils.ParentActivity;
import com.androiditgroup.loclook.v2.utils.ParentFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * Created by sostrovschi on 10.01.2017
 */

public class MainActivity   extends     ParentActivity
                            implements  NavigationView.OnNavigationItemSelectedListener,
                                        FragmentManager.OnBackStackChangedListener {

    private TextView mToolbarTitle;
    private ImageButton mNavigationBtn;
//    private LinearLayout mToolbarBtnContainer;
//    private FrameLayout mPublicationBtn;
//    private FrameLayout mCameraBtn;
//    private FrameLayout mSendBtn;
//    private ProgressBar mProgressBar;

//    private LocationManager locationManager;

    private DefineUserLocationName mLocationDefiner;

    private static NavigationView       mNavigationView;
    public static CircularImageView     mNavigationUserAvatar;
    public static TextView              mNavigationUserName;
    public static TextView              mNavigationUserLocationName;

    public static LockableSlidingPane   mSlidingPaneLayout;
    public static SelectedFragment      selectedFragment;

    private LayoutInflater mInflater;

    public static boolean refreshFeed;

    public enum SelectedFragment {
        show_feed, send_publication
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.MainActivity_Toolbar);
        assert toolbar != null;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getFragmentManager().addOnBackStackChangedListener(this);

        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

        mLocationDefiner = new DefineUserLocationName((LocationManager) getSystemService(LOCATION_SERVICE));
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.LOCATION_REQUEST_INTERVAL, 10, locationListener);

        mNavigationView = (NavigationView) findViewById(R.id.MainActivity_NavigationView);
        mNavigationView.setNavigationItemSelectedListener(this);
        Menu menu = mNavigationView.getMenu();
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

                // Cursor data = DBManager.getInstance().queryColumns(Constants.DataBase.USER_TABLE, null, "PHONE_NUMBER", phoneNumber);
                Cursor data = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.USER_TABLE, null, "PHONE_NUMBER", phoneNumber);

                // если данные получены
                if (data.getCount() > 0) {
                    // формируем пользователя приложения
                    if (setUserData(data))
                        setUserDataOnNavView();
                }
            }
        }

//        mProgressBar = (ProgressBar) findViewById(R.id.mainActivityProgressBar);

        setFragment(FeedFragment.newInstance(), false, false);

        // формируем список бейджей
        LocLookApp.setBadgesList();

        // Log.e("ABC", "MainActivity: onCreate(): badgesSum = " + LocLookApp.badgesList.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // добавить публикацию
            case R.id.action_write_publication:
                selectedFragment = SelectedFragment.send_publication;
                setFragment(PublicationFragment.newInstance(mInflater), false, true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener mMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSlidingPaneLayout.openPane();
        }
    };

    private View.OnClickListener mBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        clearBackStack();
        switch (item.getItemId()) {
            case R.id.nav_feed:
                setFragment(FeedFragment.newInstance(), false, false);
                break;
            case R.id.nav_exit:
                Intent logoutIntent = new Intent(this, SplashActivity.class);
                startActivity(logoutIntent);
                LocLookApp.getInstance().logOut();
                finish();
                return true;
            case R.id.nav_favorites:
                setFragment(FavoritesFragment.newInstance(), false, true);
                break;
            case R.id.nav_notifications:
                setFragment(NotificationsFragment.newInstance(), false, true);
                break;
            case R.id.nav_badges:
                setFragment(BadgesFragment.newInstance(), false, true);
                break;
            case R.id.nav_geolocation:
                setFragment(GeolocationFragment.newInstance(), false, true);
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

    // public void setUserData(final User user) {
    public void setUserDataOnNavView() {
        View navHeaderView = mNavigationView.getHeaderView(0);
        // final TextView userName = (TextView) navHeaderView.findViewById(R.id.navHeaderUserName);

        mNavigationUserAvatar       = (CircularImageView) navHeaderView.findViewById(R.id.navHeaderUserAvatar);
        mNavigationUserName         = (TextView) navHeaderView.findViewById(R.id.navHeaderUserName);
        mNavigationUserName.setTypeface(LocLookApp.light);

        mNavigationUserLocationName = (TextView) navHeaderView.findViewById(R.id.navHeaderUserLocation);
        mNavigationUserLocationName.setTypeface(LocLookApp.light);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                User user = LocLookApp.usersMap.get(LocLookApp.appUserId);
                if(user != null) {
                    mNavigationUserName.setText(user.getName());
                }

                // if(LocLookApp.user != null) {
                //     mNavigationUserName.setText(LocLookApp.user.getName());
//                    if (LocLookApp.user.getImage() != null) {
//                        userImage.setImageBitmap(user.getImage());
//                    } else {
//                        userImage.setImageResource(R.drawable.userpic_holder);
//                    }
                // }
            }
        });
    }

    public void clearBackStack() {
        // Log.e("ABC", "MainActivity: clearBackStack()");
        FragmentManager fragmentManager = getFragmentManager();
        for (int f = 0; f < fragmentManager.getBackStackEntryCount(); f++)
            fragmentManager.popBackStack();
    }

    @Override
    public void onBackPressed() {
        // Log.e("ABC", "MainActivity: onBackPressed()");
        if (mSlidingPaneLayout.isOpen()) {
            mSlidingPaneLayout.closePane();
        }
        else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            if (getFragmentManager().getBackStackEntryCount() == 1) {
                mNavigationBtn.setOnClickListener(mMenuClickListener);
                mNavigationBtn.setImageResource(R.drawable.menu_icon);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackStackChanged() {
        // Log.e("ABC", "MainActivity: onBackStackChanged()");
        FragmentManager fragmentManager = getFragmentManager();
        int backStackSize = fragmentManager.getBackStackEntryCount();
        if (backStackSize == 0) {
            setToolbarTitle(LocLookApp.getInstance().getResources().getString(R.string.feed_text));
            selectedFragment = SelectedFragment.show_feed;
            return;
        }

        ParentFragment fragment = (ParentFragment) fragmentManager.findFragmentByTag(fragmentManager.getBackStackEntryAt(backStackSize - 1).getName());
        setToolbarTitle(fragment.getFragmentTitle());

        if (selectedFragment == SelectedFragment.send_publication) {
            mNavigationBtn.setOnClickListener(mBackClickListener);
            mNavigationBtn.setImageResource(R.drawable.arrow_back_icon);
        }
    }
}
