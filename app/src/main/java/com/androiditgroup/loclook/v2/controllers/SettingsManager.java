package com.androiditgroup.loclook.v2.controllers;

import android.content.Context;
import android.util.Log;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.data.DatabaseHandler;

/**
 * Created by Serghei Ostrovschi on 7/5/16.
 */

public class SettingsManager {

    private DatabaseHandler databaseHandler;

    private SharedPreferencesController sharedPreferencesController;
    private InitAppController           initAppController;

    /**
     * SettingsManager constructor
     *
     * @param context   application context.
     */
    public SettingsManager(Context context) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SettingsManager: constructor.");

        this.databaseHandler        = new DatabaseHandler(context);

        sharedPreferencesController = new SharedPreferencesController(context);

        try {
            initAppController = new InitAppController(sharedPreferencesController);

//            settingsInitAppController = new SettingsInitAppController(sharedPreferencesController,
//                    settingsLanguageController,
//                    settingsAccountModelController,
//                    settingsAppDataController);
        } catch (Exception exc) {
            LocLookApp.showLog("SettingsManager: Create InitAppController instance error: " +exc.getMessage());
            return;
        }
    }

    /**
     * Method gets link on existing instance of {@link SharedPreferencesController} class
     *
     * @return  link on existing instance of {@link SharedPreferencesController} class
     */
    public SharedPreferencesController getSharedPreferencesController() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SettingsManager: getSharedPreferencesController()");

        return sharedPreferencesController;
    }

    /**
     * Method gets link on existing instance of {@link InitAppController} class
     *
     * @return  link on existing instance of {@link InitAppController} class
     */
    public InitAppController getInitAppController() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SettingsManager: getInitAppController()");

        return initAppController;
    }
}
