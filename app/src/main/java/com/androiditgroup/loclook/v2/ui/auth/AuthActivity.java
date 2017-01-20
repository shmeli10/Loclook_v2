package com.androiditgroup.loclook.v2.ui.auth;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.models.User;
import com.androiditgroup.loclook.v2.utils.ParentActivity;

/**
 * Created by sostrovschi on 10.01.2017.
 */

public class AuthActivity   extends     ParentActivity
                            implements  FragmentManager.OnBackStackChangedListener{

    private ProgressBar mProgressBar;
    private User.Builder mUserBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mUserBuilder = new User.Builder();

        getFragmentManager().addOnBackStackChangedListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.AuthActivity_ProgressBar);

//        PhoneNumberFragment fragment = PhoneNumberFragment.newInstance();
//
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.add(R.id.AuthActivity_FragmentContainer, fragment, fragment.getClass().getName());
//          transaction.replace(R.id.AuthActivity_FragmentContainer, fragment_logo, fragment_logo.TAG);
//        transaction.commit();

        setFragment(PhoneNumberFragment.newInstance(), false, false);
    }

    @Override
    public void setToolbarTitle(String title) { }

    @Override
    public void setFragment(Fragment fragment, boolean animate, boolean addToBackStack) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (animate) {
            transaction.setCustomAnimations(R.animator.slide_in, R.animator.slide_out, R.animator.slide_in, R.animator.slide_out);
        } else {
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }

        // transaction.add(R.id.AuthActivity_FragmentContainer, fragment, fragment.getClass().getName());
        transaction.replace(R.id.AuthActivity_FragmentContainer, fragment, fragment.getClass().getName());

        if(addToBackStack)
            transaction.addToBackStack(fragment.getClass().getName());

        transaction.commit();
    }

//    private void clearStack(){
//        FragmentManager fm = getFragmentManager();
//        for (int f = 0; f < fm.getBackStackEntryCount(); f++) {
//            fm.popBackStack();
//        }
//    }

    @Override
    public void changeFragment(Fragment fragment) {

    }

    @Override
    public void toggleLoadingProgress(boolean show) {

    }

    @Override
    public void onBackStackChanged() {

//            FragmentManager manager = getFragmentManager();
//            // get index of the last fragment to be able to get it's tag
//            int currentStackIndex = manager.getBackStackEntryCount() - 1;
//            // if we don't have fragments in back stack just return
//            if (manager.getBackStackEntryCount() == 0) {
//                setToolbarTitle(LocalizationManager.getInstance().getLocalizedString("categories"));
//                selectedFragment = SelectedFragment.supercategory;
//                toggleSearchBtnVisibility(false);
//                return;
//            }
//            // otherwise get the framgent from backstack and cast it to ParentFragment so we could get it's title
//            ParentFragment fragment = (ParentFragment) manager.findFragmentByTag(
//                    manager.getBackStackEntryAt(currentStackIndex).getName());
//            // finaly set the title in the toolbar
//            setToolbarTitle(formatToolbarTitle(fragment.getFragmentTitle()));
//            toggleSearchBtnVisibility(true);
//            // now we need to update the current selected fragment
//            selectedFragment = fragment.getSelectedFragment();
    }

    public boolean setUserData(Cursor data) {

        data.moveToFirst();

        String userId       = data.getString(data.getColumnIndex("_ID"));
        String login        = data.getString(data.getColumnIndex("LOGIN"));
        String phoneNumber  = data.getString(data.getColumnIndex("PHONE_NUMBER"));
        String rate         = data.getString(data.getColumnIndex("RATE"));
        String bgImgUrl     = data.getString(data.getColumnIndex("BG_IMG_URL"));
        String avatarUrl    = data.getString(data.getColumnIndex("AVATAR_URL"));
        String description  = data.getString(data.getColumnIndex("DESCRIPTION"));
        String siteUrl      = data.getString(data.getColumnIndex("SITE_URL"));
        String latitude     = data.getString(data.getColumnIndex("LATITUDE"));
        String longitude    = data.getString(data.getColumnIndex("LONGITUDE"));
        String radius       = data.getString(data.getColumnIndex("RADIUS"));
        String regionName   = data.getString(data.getColumnIndex("REGION_NAME"));
        String streetName   = data.getString(data.getColumnIndex("STREET_NAME"));

        if((userId != null) && (!userId.equals("")))
            mUserBuilder.userId(userId);
        else
            return false;

        if((login != null) && (!login.equals("")))
            mUserBuilder.login(login);

        if((phoneNumber != null) && (!phoneNumber.equals("")))
            mUserBuilder.phone(phoneNumber);

        if((rate != null) && (!rate.equals("")))
            mUserBuilder.rate(rate);

        if((bgImgUrl != null) && (!bgImgUrl.equals("")))
            mUserBuilder.bgImgUrl(bgImgUrl);

        if((avatarUrl != null) && (!avatarUrl.equals("")))
            mUserBuilder.avatarUrl(avatarUrl);

        if((description != null) && (!description.equals("")))
            mUserBuilder.description(description);

        if((siteUrl != null) && (!siteUrl.equals("")))
            mUserBuilder.siteUrl(siteUrl);

        if((latitude != null) && (!latitude.equals("")))
            mUserBuilder.latitude(latitude);

        if((longitude != null) && (!longitude.equals("")))
            mUserBuilder.longitude(longitude);

        if((radius != null) && (!radius.equals("")))
            mUserBuilder.radius(radius);

        if((regionName != null) && (!regionName.equals("")))
            mUserBuilder.regionName(regionName);

        if((streetName != null) && (!streetName.equals("")))
            mUserBuilder.streetName(streetName);

        // сохраняем пользователя
        LocLookApp.user = mUserBuilder.build();

        return true;
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
    }
}
