package com.androiditgroup.loclook.v2;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.data.AppManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Serghei Ostrovschi on on 7/5/16.
 */

public class LocLookApp_NEW extends Application {

    protected static LocLookApp_NEW appInstance;

    private AppManager  appManager;

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
        appInstance  = this;

        setFonts();
    }

    // ------------------------------------- GETTERS ----------------------------------------- //

    public static LocLookApp_NEW getInstance() {
        return appInstance;
    }

    public AppManager getAppManager() {
        //LocLookApp_NEW.showLog("-------------------------------------");
        //LocLookApp_NEW.showLog("LocLookApp_NEW: getAppManager(): appManager is null: " +(appManager == null));

        if(appManager == null) {
            appManager = new AppManager(this);
        }
        return appManager;
    }

    public static int getDrawableResId(String resource) throws Exception {
        if(TextUtils.isEmpty(resource))
            throw new Exception(ErrorConstants.RESOURCE_NULL_ERROR);

        return appInstance.getResources().getIdentifier("@drawable/" +resource,
                                                        null,
                                                        appInstance.getPackageName());
    }

    public static int getColorResId(String resource) throws Exception {
        if(TextUtils.isEmpty(resource))
            throw new Exception(ErrorConstants.RESOURCE_NULL_ERROR);

        return appInstance.getResources().getColor( appInstance.getResources().getIdentifier("@color/" +resource,
                                                    null,
                                                    appInstance.getPackageName()));
    }

    public static Display getScreenResolution() {
        WindowManager wm = (WindowManager) appInstance.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay();
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

    public static void showSimpleSnakeBar(View view, String resName) {
        int resId   = appInstance.getResources().getIdentifier( "@string/" +resName,
                                                                null,
                                                                appInstance.getPackageName());

        String text = appInstance.getResources().getString(resId);

        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }
}
