package com.androiditgroup.loclook.v2;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;

import com.androiditgroup.loclook.v2.models.Badge;
import com.androiditgroup.loclook.v2.models.User;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by sostrovschi on 09.01.2017.
 */

public class LocLookApp extends Application {

    public static       User        user;
    public static       Context     context;
    protected static    LocLookApp  instance;

    private static      SharedPreferences preferences;

//    public static String authCode;

    public static Typeface bold, boldItalic, extraBold, extraBoldItalic, italic,
            light, lightItalic, regular, semiBold, semiBoldItalic;

    protected static final String APP_PREFERENCES = "loclook_preferences";

    public static LocLookApp getInstance() {
        return instance;
    }

    public static ArrayList<Badge> badgesList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        bold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");
        boldItalic = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-BoldItalic.ttf");
        extraBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-ExtraBold.ttf");
        extraBoldItalic = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-ExtraBoldItalic.ttf");
        italic = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Italic.ttf");
        light = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Light.ttf");
        lightItalic = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-LightItalic.ttf");
        regular = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        semiBold = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold.ttf");
        semiBoldItalic = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-SemiboldItalic.ttf");

        if (isFirstLaunch()) {
            String language = Locale.getDefault().getDisplayLanguage();
            if (language.equalsIgnoreCase(Constants.LONG_RUSSIAN)) {
                setUserLanguage(Constants.RUSSIAN);
            } else {
                setUserLanguage(Constants.ENGLISH);
            }
            preferences.edit().putBoolean("isFirstLaunch", false).apply();
        }
    }

    private boolean isFirstLaunch() {
        return preferences.getBoolean("isFirstLaunch", true);
    }

    public void setUserLanguage(String language) {
        preferences.edit().putString("language", language).apply();
    }

    public void setLoginStatus(boolean isLoggedIn) {
        preferences.edit().putBoolean("login status", isLoggedIn).apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean("login status", false);
    }

    public void savePhoneNumber(String phoneNumber) {
        preferences.edit().putString("phone", phoneNumber).apply();
    }

    public String getPhoneNumber() {
        return preferences.getString("phone", "");
    }

    public void saveEnteredUserName(String userName) {
        preferences.edit().putString("user_name", userName).apply();
    }

    public String getEnteredUserName() {
        return preferences.getString("user_name", "");
    }

    public void logOut() {
        user = null;
        setLoginStatus(false);
        preferences.edit().clear().commit();
    }

    public static void setBadgesList() {
        // Cursor badgesDataCursor = DBManager.getInstance().getDataBase().queryColumns(Constants.DataBase.BADGE_DATA_TABLE, null, null, null);
        Cursor cursor = DBManager.getInstance().getDataBase().query(Constants.DataBase.BADGE_DATA_TABLE, null, null, null, null, null, null);

        if(cursor != null) {
            // Log.e("ABC", "LocLookApp: setBadgesList: badgesSum = " + cursor.getCount());

            String[] cursorArr = cursor.getColumnNames();

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    Badge badge = new Badge.Builder()
                            .id(cursor.getString(cursor.getColumnIndex(cursorArr[0])))
                            .name(cursor.getString(cursor.getColumnIndex(cursorArr[1])))
                            .isEnabled(cursor.getInt(cursor.getColumnIndex(cursorArr[2])) == 1)
                            .iconResId(getDrawableResId("badge_" +cursor.getString(cursor.getColumnIndex(cursorArr[0]))))
                            .build();
                    badgesList.add(badge);
                } while (cursor.moveToNext());
            }
        }
        else
            Log.e("ABC", "LocLookApp: setBadgesList: badgesDataCursor is null");
    }

    public static int getDrawableResId(String resource) {
        return context.getResources().getIdentifier("@drawable/" +resource, null, context.getPackageName());
    }

    public static int getDpFromPixels(int px) {
        return (int) (context.getResources().getDisplayMetrics().density * px);
    }
}
