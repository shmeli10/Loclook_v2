package com.androiditgroup.loclook.v2.utils;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.models.User;

/**
 * Created by sostrovschi on 10.01.2017
 */

public abstract class ParentActivity extends AppCompatActivity {
    public void expandTextView(TextView tv) {
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", 300);
        animation.setDuration(200).start();
    }

    public void collapseTextView(TextView tv, int numLines) {
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", numLines);
        animation.setDuration(200).start();
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public abstract void setToolbarTitle(final String title);

    public abstract void setFragment(Fragment fragment, boolean animate, boolean addToBackStack);

    public abstract void changeFragment(Fragment fragment);

    public abstract void toggleLoadingProgress(final boolean show);

    public boolean setUserData(Cursor data) {

//        User.Builder mUserBuilder = new User.Builder();
//
//        data.moveToFirst();
//
//        String userId       = data.getString(data.getColumnIndex("_ID"));
//        String name         = data.getString(data.getColumnIndex("NAME"));
//        String phoneNumber  = data.getString(data.getColumnIndex("PHONE_NUMBER"));
//        String rate         = data.getString(data.getColumnIndex("RATE"));
//        // String bgImgUrl     = data.getString(data.getColumnIndex("BG_IMG_URL"));
//        // String avatarUrl    = data.getString(data.getColumnIndex("AVATAR_URL"));
//        String description  = data.getString(data.getColumnIndex("DESCRIPTION"));
//        String siteUrl      = data.getString(data.getColumnIndex("SITE_URL"));
//        String latitude     = data.getString(data.getColumnIndex("LATITUDE"));
//        String longitude    = data.getString(data.getColumnIndex("LONGITUDE"));
//        String radius       = data.getString(data.getColumnIndex("RADIUS"));
//        String regionName   = data.getString(data.getColumnIndex("REGION_NAME"));
//        String streetName   = data.getString(data.getColumnIndex("STREET_NAME"));
//
//        data.close();
//
//        if((userId != null) && (!userId.equals("")))
//            mUserBuilder.userId(userId);
//        else
//            return false;
//
//        if((name != null) && (!name.equals("")))
//            mUserBuilder.name(name);
//
//        if((phoneNumber != null) && (!phoneNumber.equals("")))
//            mUserBuilder.phone(phoneNumber);
//
//        if((rate != null) && (!rate.equals("")))
//            mUserBuilder.rate(rate);
//
//        if((bgImgUrl != null) && (!bgImgUrl.equals("")))
//            mUserBuilder.bgImgUrl(bgImgUrl);
//
//        if((avatarUrl != null) && (!avatarUrl.equals("")))
//            mUserBuilder.avatarUrl(avatarUrl);
//
//        if((description != null) && (!description.equals("")))
//            mUserBuilder.description(description);
//
//        if((siteUrl != null) && (!siteUrl.equals("")))
//            mUserBuilder.siteUrl(siteUrl);
//
//        if((latitude != null) && (!latitude.equals("")))
//            mUserBuilder.latitude(latitude);
//
//        if((longitude != null) && (!longitude.equals("")))
//            mUserBuilder.longitude(longitude);
//
//        if((radius != null) && (!radius.equals("")))
//            mUserBuilder.radius(radius);
//
//        if((regionName != null) && (!regionName.equals("")))
//            mUserBuilder.regionName(regionName);
//
//        if((streetName != null) && (!streetName.equals("")))
//            mUserBuilder.streetName(streetName);
//
//        // сохраняем пользователя
//        LocLookApp.user = mUserBuilder.build();

        User newUser = UserGenerator.getUserFromCursor(data);

        if(newUser != null) {

            LocLookApp.appUserId = newUser.getId();
            LocLookApp.usersMap.put(newUser.getId(), newUser);

            return true;
        }
        else
            return  false;

//        return true;
    }
}
