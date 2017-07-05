package com.androiditgroup.loclook.v2;

import android.app.Application;
import android.graphics.Typeface;
import android.util.Log;

import com.androiditgroup.loclook.v2.controllers.SettingsManager;

/**
 * Created by Serghei Ostrovschi on on 7/5/16.
 */

public class LocLookApp_NEW extends Application {

    private SettingsManager   settingsManager;

    public static Typeface  fontBold,
                            fontBoldItalic,
                            fontExtraBold,
                            fontExtraBoldItalic,
                            fontItalic,
                            fontLight,
                            fontLightItalic,
                            fontRegular,
                            fontSemiBold,
                            fontSemiBoldItalic;

    @Override
    public void onCreate() {
        super.onCreate();

        setFonts();
    }

    // ------------------------------------- GETTERS ----------------------------------------- //

    public SettingsManager getSettingsManager() {
        LocLookApp_NEW.showLog("-------------------------------------");
        LocLookApp_NEW.showLog("LocLookApp_NEW: getSettingsManager(): settingsManager is null: " +(settingsManager == null));

        if(settingsManager == null) {
            settingsManager = new SettingsManager(this);
        }
        return settingsManager;
    }


    // ------------------------------------- SETTERS ----------------------------------------- //

    private void setFonts() {

        fontBold            = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Bold.ttf");
        fontBoldItalic      = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-BoldItalic.ttf");
        fontExtraBold       = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-ExtraBold.ttf");
        fontExtraBoldItalic = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-ExtraBoldItalic.ttf");
        fontItalic          = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Italic.ttf");
        fontLight           = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Light.ttf");
        fontLightItalic     = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-LightItalic.ttf");
        fontRegular         = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
        fontSemiBold        = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Semibold.ttf");
        fontSemiBoldItalic  = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-SemiboldItalic.ttf");
    }

    public static void showLog(String msg) {
        Log.e("LOG", msg);
    }
}
