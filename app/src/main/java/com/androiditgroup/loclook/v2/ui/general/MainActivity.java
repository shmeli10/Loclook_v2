package com.androiditgroup.loclook.v2.ui.general;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
    private LinearLayout mToolbarBtnContainer;
    private FrameLayout mPublicationBtn;
    private FrameLayout mCameraBtn;
    private FrameLayout mSendBtn;
    private ProgressBar mProgressBar;

    private static NavigationView navigationView;

    public static LockableSlidingPane mSlidingPaneLayout;
    public static SelectedFragment selectedFragment;

    private LayoutInflater mInflater;

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

        mPublicationBtn = (FrameLayout) findViewById(R.id.MainActivity_PublicationButton);
        assert mPublicationBtn != null;
        mPublicationBtn.setVisibility(View.VISIBLE);
        mPublicationBtn.setOnClickListener(mPublicationClickListener);

        mCameraBtn = (FrameLayout) findViewById(R.id.MainActivity_CameraButton);
        assert mCameraBtn != null;
        mCameraBtn.setOnClickListener(mCameraBtnClickListener);

        mSendBtn = (FrameLayout) findViewById(R.id.MainActivity_SendButton);
        assert mSendBtn != null;
        mSendBtn.setOnClickListener(mSendBtnClickListener);

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

        // формируем список бейджей
        LocLookApp.setBadgesList();

        // Log.e("ABC", "MainActivity: onCreate(): badgesSum = " + LocLookApp.badgesList.size());
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

    private View.OnClickListener mPublicationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            selectedFragment = SelectedFragment.send_publication;
            // setFragment(PublicationFragment.newInstance(), false, true);
            setFragment(PublicationFragment.newInstance(mInflater), false, true);
        }
    };

    private View.OnClickListener mCameraBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // setFragment(PublicationFragment.newInstance(), false, true);
        }
    };

    private View.OnClickListener mSendBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // setFragment(PublicationFragment.newInstance(), false, true);
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
            if (selectedFragment == SelectedFragment.send_publication) {
                mCameraBtn.setVisibility(View.GONE);
                mSendBtn.setVisibility(View.GONE);
            }

            getFragmentManager().popBackStack();
            if (getFragmentManager().getBackStackEntryCount() == 1) {
                mNavigationBtn.setOnClickListener(mMenuClickListener);
                mNavigationBtn.setImageResource(R.drawable.menu_icon);
                mPublicationBtn.setVisibility(View.VISIBLE);
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
        // if we don't have fragments in back stack just return
        if (backStackSize == 0) {
            setToolbarTitle(LocLookApp.getInstance().getResources().getString(R.string.feed_text));
            selectedFragment = SelectedFragment.show_feed;
            return;
        }

        // get index of the last fragment to be able to get it's tag
        // int currentStackIndex = fragmentManager.getBackStackEntryCount() - 1;
        // otherwise get the framgent from backstack and cast it to ParentFragment so we could get it's title
        ParentFragment fragment = (ParentFragment) fragmentManager.findFragmentByTag(fragmentManager.getBackStackEntryAt(backStackSize - 1).getName());
        // finaly set the title in the toolbar
        setToolbarTitle(fragment.getFragmentTitle());

        if (selectedFragment == SelectedFragment.send_publication) {
            mNavigationBtn.setOnClickListener(mBackClickListener);
            mNavigationBtn.setImageResource(R.drawable.arrow_back_icon);
            mPublicationBtn.setVisibility(View.GONE);
            mCameraBtn.setVisibility(View.VISIBLE);
            mSendBtn.setVisibility(View.VISIBLE);
        }
    }
}
