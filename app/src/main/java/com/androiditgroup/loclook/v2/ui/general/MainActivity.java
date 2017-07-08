package com.androiditgroup.loclook.v2.ui.general;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.controllers.AppManager;
import com.androiditgroup.loclook.v2.models.User;
import com.androiditgroup.loclook.v2.ui.SplashActivity;
import com.androiditgroup.loclook.v2.ui.badges.BadgesFragment;
import com.androiditgroup.loclook.v2.ui.favorites.FavoritesFragment;
import com.androiditgroup.loclook.v2.ui.feed.FeedFragment;
import com.androiditgroup.loclook.v2.ui.geolocation.GeolocationFragment;
import com.androiditgroup.loclook.v2.ui.notifications.NotificationsFragment;
import com.androiditgroup.loclook.v2.ui.publication.PublicationFragment;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;
import com.androiditgroup.loclook.v2.utils.DefineUserLocationName;
import com.androiditgroup.loclook.v2.utils.ParentActivity;
import com.androiditgroup.loclook.v2.utils.ParentFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

import static com.androiditgroup.loclook.v2.ui.general.MainActivity.SelectedFragment.badges;
import static com.androiditgroup.loclook.v2.ui.general.MainActivity.SelectedFragment.favorites;
import static com.androiditgroup.loclook.v2.ui.general.MainActivity.SelectedFragment.feed;
import static com.androiditgroup.loclook.v2.ui.general.MainActivity.SelectedFragment.geolocation;
import static com.androiditgroup.loclook.v2.ui.general.MainActivity.SelectedFragment.notifications;

/**
 * Created by sostrovschi on 10.01.2017
 */

public class MainActivity   extends     ParentActivity
                            implements  FragmentManager.OnBackStackChangedListener {

    private TextView mToolbarTitle;
    private ImageButton mNavigationBtn;

    private DefineUserLocationName mLocationDefiner;

    public static CircularImageView     mLeftMenuUserAvatar;
    public static TextView              mLeftMenuUserName;
    public static TextView              mLeftMenuUserLocationName;

    public static DrawerLayout          mLeftMenu;
    public static SelectedFragment      selectedFragment;
    public static SelectedFragment      newSelectedFragment;

    public static LayoutInflater        mInflater;

    private ProgressDialog              mProgressDialog;

    public enum SelectedFragment {
        feed, send_publication, favorites, notifications, badges, geolocation, photo_gallery, comments
    }

    private static final int leftMenuFeedBlockResId             = R.id.Drawer_FeedBlock;
    private static final int leftMenuFavoritesBlockResId        = R.id.Drawer_FavoritesBlock;
    private static final int leftMenuNotificationsBlockResId    = R.id.Drawer_NotificationsBlock;
    private static final int leftMenuBadgesBlockResId           = R.id.Drawer_BadgesBlock;
    private static final int leftMenuGeolocationBlockResId      = R.id.Drawer_GeolocationBlock;
    private static final int leftMenuExitBlockResId             = R.id.Drawer_ExitBlock;

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

        mToolbarTitle = (TextView) findViewById(R.id.MainActivity_ToolbarTitle);
        mToolbarTitle.setTypeface(LocLookApp.semiBold);

        // initialize toolbar button
        mNavigationBtn = (ImageButton) findViewById(R.id.MainActivity_Toolbar_NavButton);
        assert mNavigationBtn != null;
        mNavigationBtn.setOnClickListener(mMenuClickListener);

        mLeftMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftMenu.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) { }

            @Override
            public void onDrawerOpened(View drawerView) { }

            @Override
            public void onDrawerClosed(View drawerView) {
                if(selectedFragment != newSelectedFragment) {
                    switch(newSelectedFragment) {

                        case feed:
                            setFragment(FeedFragment.newInstance(), false, false);
                            break;
                        case favorites:
                            setFragment(FavoritesFragment.newInstance(), false, true);
                            break;
                        case notifications:
                            setFragment(NotificationsFragment.newInstance(), false, true);
                            break;
                        case badges:
                            setFragment(BadgesFragment.newInstance(), false, true);
                            break;
                        case geolocation:
                            setFragment(GeolocationFragment.newInstance(), false, true);
                            break;
                        default:
                            break;
                    }
                    selectedFragment = newSelectedFragment;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) { }
        });

        mLeftMenuUserAvatar       = (CircularImageView) findViewById(R.id.Drawer_UserAvatar);
        mLeftMenuUserName         = (TextView) findViewById(R.id.Drawer_UserName);
        mLeftMenuUserName.setTypeface(LocLookApp.light);

        mLeftMenuUserLocationName = (TextView) findViewById(R.id.Drawer_UserLocation);
        mLeftMenuUserLocationName.setTypeface(LocLookApp.light);

        mLocationDefiner = new DefineUserLocationName((LocationManager) getSystemService(LOCATION_SERVICE));

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

                Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.USER_TABLE, null, "PHONE_NUMBER", phoneNumber);

                // если данные получены
                if (cursor.getCount() > 0) {
                    // формируем пользователя приложения
                    if (setUserData(cursor))
                        setLeftMenuUserData();
                }
            }
        }

        setFragment(FeedFragment.newInstance(), false, false);

        // формируем список бейджей
        LocLookApp.setBadgesList();

        selectedFragment = newSelectedFragment = feed;
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

    public void onClick(View view) {
        clearBackStack();
        switch(view.getId()) {
            case leftMenuFeedBlockResId:
                newSelectedFragment = feed;
                break;
            case leftMenuFavoritesBlockResId:
                newSelectedFragment = favorites;
                break;
            case leftMenuNotificationsBlockResId:
                newSelectedFragment = notifications;
                break;
            case leftMenuBadgesBlockResId:
                newSelectedFragment = badges;
                break;
            case leftMenuGeolocationBlockResId:
                newSelectedFragment = geolocation;
                break;
            case leftMenuExitBlockResId:
                Intent logoutIntent = new Intent(this, SplashActivity.class);
                startActivity(logoutIntent);
                LocLookApp.getInstance().logOut();
                finish();
                break;
            default:
                break;
        }
       mLeftMenu.closeDrawer(Gravity.LEFT);
    }

    private View.OnClickListener mMenuClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                // mSlidingPaneLayout.openPane();
                mLeftMenu.openDrawer(Gravity.LEFT);
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

    public void setLeftMenuUserData() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                User user = LocLookApp.usersMap.get(LocLookApp.appUserId);
                if(user != null) {
                    mLeftMenuUserName.setText(user.getName());
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
        // LocLookApp.showLog("MainActivity: clearBackStack()");

        FragmentManager fragmentManager = getFragmentManager();
        for (int f = 0; f < fragmentManager.getBackStackEntryCount(); f++)
            fragmentManager.popBackStack();
    }

    @Override
    public void onBackPressed() {
        // LocLookApp.showLog("MainActivity: onBackPressed()");

        if(mLeftMenu.isDrawerOpen(Gravity.LEFT)){
            mLeftMenu.closeDrawer(Gravity.LEFT);
        }
        else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            if (getFragmentManager().getBackStackEntryCount() == 1) {
                mNavigationBtn.setOnClickListener(mMenuClickListener);
                mNavigationBtn.setImageResource(R.drawable.menu_icon);
            }
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackStackChanged() {
        // LocLookApp.showLog("MainActivity: onBackStackChanged()");

        FragmentManager fragmentManager = getFragmentManager();
        int backStackSize = fragmentManager.getBackStackEntryCount();
        if (backStackSize == 0) {
            setToolbarTitle(LocLookApp.getInstance().getResources().getString(R.string.feed_text));
            selectedFragment = SelectedFragment.feed;
            return;
        }

        ParentFragment fragment = (ParentFragment) fragmentManager.findFragmentByTag(fragmentManager.getBackStackEntryAt(backStackSize - 1).getName());
        setToolbarTitle(fragment.getFragmentTitle());

        switch(selectedFragment) {

            case send_publication:
            case comments:
            case photo_gallery:
                mNavigationBtn.setOnClickListener(mBackClickListener);
                mNavigationBtn.setImageResource(R.drawable.arrow_back_icon);
                break;
            default:
                break;
        }

//        if (selectedFragment == SelectedFragment.send_publication) {
//            mNavigationBtn.setOnClickListener(mBackClickListener);
//            mNavigationBtn.setImageResource(R.drawable.arrow_back_icon);
//        }
    }

    public void showPD() {
        if(mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        }
    }

    public void hidePD() {
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
