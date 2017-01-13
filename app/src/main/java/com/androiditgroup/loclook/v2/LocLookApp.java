package com.androiditgroup.loclook.v2;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.androiditgroup.loclook.v2.models.User;
import com.androiditgroup.loclook.v2.utils.Constants;

import java.util.Locale;

/**
 * Created by sostrovschi on 09.01.2017.
 */

public class LocLookApp extends Application {

    protected static LocLookApp instance;
    public static Context context;
    private static SharedPreferences preferences;
    public static User user;
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

    public void savePhoneNumber(String phoneNumber) {
        preferences.edit().putString("phone", phoneNumber).apply();
    }
}
