package com.androiditgroup.loclook.v2.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.constants.SettingsConstants;

/**
 * Created by Serghei Ostrovschi on 7/5/16.
 * Class created for interaction with Shared Preferences
 */

public class SharedPreferencesController {

    private SharedPreferences           sharedPreferences;
    private SharedPreferences.Editor    sharedPreferencesEditor;

    /**
     * SharedPreferencesController constructor.
     *
     * @param context   application context.
     */
    public SharedPreferencesController(Context context) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: constructor.");

        sharedPreferences       = context.getSharedPreferences("user_data", context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }


    /**
     * Method initializes SharedPreferences
     */
    public void init() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: init()");

        // if parameter not exists, create it and set to true
        if (!containsParam("is_undefined_user_mode")) {
            LocLookApp.showLog("SharedPreferencesController: init(): start init");

            // isFirstLaunch
            addBooleanParam("is_undefined_user_mode",       true);

            // login status
            addBooleanParam("is_use_logged",                false);

            // language
            addStringParam("ui_language",                   SettingsConstants.ENGLISH);

            // phone
            addStringParam("user_phone_number",             "");
            addStringParam("user_name",                     "");
        }
        else {
            LocLookApp.showLog("SharedPreferencesController: init(): do not init");
        }
    }

    /**
     * Method answers is it a "undefined user mode" (user is not registered/authenticated) or not
     *
     * @return  answer is it a "undefined user mode" (user is not registered/authenticated) or not
     */
    public boolean isUserUndefinedMode() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: isUserUndefinedMode()");

        return getBooleanValue("is_undefined_user_mode");
    }

    /**
     * Method sets false to parameter which signifies is it a "undefined user mode"
     * (user is not registered/authenticated) or not
     */
    public void setUndefinedUserModeToFalse() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: setUndefinedUserModeToFalse()");

        // if param not exists, create it and set to false
        if(!containsParam("is_undefined_user_mode")) {
            addBooleanParam("is_undefined_user_mode", false);
        }
        // else change param value to false
        else {
            setNewBooleanValue("is_undefined_user_mode", false);
        }
    }

    /**
     * Method adds string param to Shared Preferences
     *
     * @param paramName     parameter name
     * @param paramValue    parameter value
     */
    public void addStringParam(String paramName, String paramValue) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: addStringParam(): paramName= " +paramName+ ",  paramValue= " +paramValue);

        sharedPreferencesEditor.putString(paramName, paramValue);
        sharedPreferencesEditor.apply();
    }

    /**
     * Method adds boolean param to Shared Preferences
     *
     * @param paramName     parameter name
     * @param paramValue    parameter value
     */
    public void addBooleanParam(String paramName, boolean paramValue) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: addBooleanParam(): paramName= " +paramName+ ",  paramValue= " +paramValue);

        sharedPreferencesEditor.putBoolean(paramName, paramValue);
        sharedPreferencesEditor.apply();
    }

    /**
     * Method adds int param to Shared Preferences
     *
     * @param paramName     parameter name
     * @param paramValue    parameter value
     */
    public void addIntParam(String paramName, int paramValue) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: addIntParam(): paramName= " +paramName+ ",  paramValue= " +paramValue);

        sharedPreferencesEditor.putInt(paramName, paramValue);
        sharedPreferencesEditor.apply();
    }

    /**
     * Method returns answer whether SharedPreferences contains specified parameter
     *
     * @param paramName parameter name
     * @return  answer whether SharedPreferences contains specified parameter
     */
    public boolean containsParam(String paramName) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: containsParam(): paramName= " +paramName+ ": " +sharedPreferences.contains(paramName));

        return sharedPreferences.contains(paramName);
    }

    /**
     * Method returns string value of specified parameter
     *
     * @param paramName parameter name
     * @return  value of specified parameter
     */
    public String getStringValue(String paramName) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: getStringValue(): paramName= " +paramName);

        return sharedPreferences.getString(paramName, "");
    }

    /**
     * Method returns boolean value of specified parameter
     *
     * @param paramName parameter name
     * @return  value of specified parameter
     */
    public boolean getBooleanValue(String paramName) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: getBooleanValue(): paramName= " +paramName);

        return sharedPreferences.getBoolean(paramName, true);
    }

    /**
     * Method returns int value of specified parameter
     *
     * @param paramName parameter name
     * @return  value of specified parameter
     */
    public int getIntValue(String paramName) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: getIntValue(): paramName= " +paramName);

        return sharedPreferences.getInt(paramName, -1);
    }

    /**
     * Method sets new string value of specified parameter
     *
     * @param paramName     parameter name
     * @param paramNewValue new parameter value
     */
    public void setNewStringValue(String paramName, String paramNewValue) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: setNewStringValue(): paramName= " +paramName+ ", paramNewValue= " +paramNewValue);

        addStringParam(paramName, paramNewValue);
    }

    /**
     * Method sets new boolean value of specified parameter
     *
     * @param paramName     parameter name
     * @param paramNewValue new parameter value
     */
    public void setNewBooleanValue(String paramName, boolean paramNewValue) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: setNewBooleanValue(): paramName= " +paramName+ ", paramNewValue= " +paramNewValue);

        addBooleanParam(paramName, paramNewValue);
    }

    /**
     * Method sets new int value of specified parameter
     *
     * @param paramName     parameter name
     * @param paramNewValue new parameter value
     */
    public void setNewIntValue(String paramName, int paramNewValue) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: setNewIntValue(): paramName= " +paramName+ ", paramNewValue= " +paramNewValue);

        addIntParam(paramName, paramNewValue);
    }

    /**
     * Methods leaves parameters but clears there values
     */
    public void clear() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SharedPreferencesController: clear()");

        sharedPreferencesEditor.clear();
        sharedPreferencesEditor.apply();
    }
}
