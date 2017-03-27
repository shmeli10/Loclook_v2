package com.androiditgroup.loclook.v2;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.androiditgroup.loclook.v2.models.Badge;
import com.androiditgroup.loclook.v2.models.User;
import com.androiditgroup.loclook.v2.utils.BadgeGenerator;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by sostrovschi on 09.01.2017.
 */

public class LocLookApp extends Application {

    public static       String      appUserId;
    public static       Context     context;
    protected static    LocLookApp  instance;

    private static      SharedPreferences preferences;

    public static Map<String, User> usersMap = new HashMap<>();
    public static Map<String, Badge> badgesMap = new LinkedHashMap<>();
    public static Map<String, Bitmap> imagesMap = new LinkedHashMap<>();

    public static Typeface bold, boldItalic, extraBold, extraBoldItalic, italic,
            light, lightItalic, regular, semiBold, semiBoldItalic;

    protected static final String APP_PREFERENCES = "loclook_preferences";

    public static LocLookApp getInstance() {
        return instance;
    }

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
        // user = null;
        appUserId = null;
        usersMap.clear();
//        publicationsMap.clear();
        setLoginStatus(false);
        preferences.edit().clear().commit();
    }

    public static void setBadgesList() {
        // Cursor badgesDataCursor = DBManager.getInstance().getDataBase().queryColumns(Constants.DataBase.BADGE_DATA_TABLE, null, null, null);
        Cursor cursor = DBManager.getInstance().getDataBase().query(Constants.DataBase.BADGE_TABLE, null, null, null, null, null, null);

        ArrayList<Badge> badgesList = BadgeGenerator.getBadgesList(cursor);

//        LocLookApp.showLog("LocLookApp: setBadgesList(): badgesList size = " +badgesList.size());

        for(int i=0; i<badgesList.size(); i++) {
            Badge badge = badgesList.get(i);
            badgesMap.put(badge.getId(), badge);
        }
    }

    public static int getDrawableResId(String resource) {
        return context.getResources().getIdentifier("@drawable/" +resource, null, context.getPackageName());
    }

//    public static int getStringResId(String resource) {
//        return context.getResources().getIdentifier("@string/" +resource, null, context.getPackageName());
//    }

//    public static String getStringByResId(int resId) {
//        return context.getResources().getString(resId);
//    }

//    public static String getStringByResName(String resource) {
//        return context.getResources().getString(getStringResId(resource));
//    }

//    public static int getDpFromPixels(int px) {
//        return (int) (context.getResources().getDisplayMetrics().density * px);
//    }

    /**
     * Convert dp to pixels
     * @param dp to convert
     * @return value of passed dp in pixels
     */
    public static int getPixelsFromDp(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static Display getScreenResolution() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay();
    }

    // public static void showSimpleSnakeBar(View view, String text) {
    public static void showSimpleSnakeBar(View view, String resName) {
        int resId = context.getResources().getIdentifier("@string/" +resName, null, context.getPackageName());
        String text = context.getResources().getString(resId);
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

//    public static void showActionSnakeBar(View view, String resName, String actionResName, final String actionResultResName) {
//        Snackbar.make(view, getStringByResName(resName), Snackbar.LENGTH_LONG)
//                .setAction(getStringByResName(actionResName), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(actionResultResName != null) {
//                            Snackbar snackbar1 = Snackbar.make(view, getStringByResName(actionResultResName), Snackbar.LENGTH_SHORT);
//                            snackbar1.show();
//                        }
//                    }
//                }).show();
//    }

    public static void showLog(String msg) {
        Log.e("ABC", msg);
    }
}
