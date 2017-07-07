package com.androiditgroup.loclook.v2.controllers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.data.DatabaseHandler;
import com.androiditgroup.loclook.v2.interfaces.DatabaseCreateInterface;

/**
 * Created by Serghei Ostrovschi on 7/5/16.
 */

public class SettingsManager implements DatabaseCreateInterface {

    private Context                     context;
    private DatabaseHandler             databaseHandler;
    private SQLiteDatabase              sqLiteDatabase;

    private BadgeController             badgeController;
    private InitAppController           initAppController;
    private SharedPreferencesController sharedPreferencesController;

    /**
     * SettingsManager constructor
     *
     * @param context   application context.
     */
    public SettingsManager(Context context) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SettingsManager: constructor.");

        this.context = context;

        try {
            this.databaseHandler = new DatabaseHandler(context, this);

            if(databaseHandler != null) {
                sqLiteDatabase = databaseHandler.getWritableDatabase();
                LocLookApp.showLog("SettingsManager: constructor: sqLiteDatabase is null: " +(sqLiteDatabase == null));
            }
            else {
                LocLookApp.showLog("SettingsManager: constructor: databaseHandler is null");
                //throw new Exception(ErrorConstants.DATABASE_HANDLER_NULL_ERROR);
            }
        } catch (Exception exc) {
            LocLookApp.showLog("SettingsManager: constructor: get DatabaseHandler instance error: " +exc.getMessage());
        }

        /*try {
            getSQLiteDatabase();
        } catch (Exception exc) {
            LocLookApp.showLog("SettingsManager: constructor: getSQLiteDatabase error: " +exc.getMessage());
        }*/

//        if(databaseHandler != null)
//            sqLiteDatabase = databaseHandler.getWritableDatabase();

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

    public SQLiteDatabase getSQLiteDatabase() throws Exception {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("0_SettingsManager: getSQLiteDatabase() sqLiteDatabase is null: " +(sqLiteDatabase == null));

        if(sqLiteDatabase == null) {
            if(databaseHandler != null) {
                sqLiteDatabase = databaseHandler.getWritableDatabase();
                LocLookApp.showLog("1_SettingsManager: getSQLiteDatabase(): sqLiteDatabase is null: " +(sqLiteDatabase == null));
            }
            else {
                LocLookApp.showLog("SettingsManager: getSQLiteDatabase(): error");
                throw new Exception(ErrorConstants.DATABASE_HANDLER_NULL_ERROR);
            }
        }

        return sqLiteDatabase;
    }

    public BadgeController getBadgeController() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SettingsManager: getBadgeController() badgeController is null: " +(badgeController == null));

        if(badgeController == null) {
            try {
                badgeController = new BadgeController(context, databaseHandler, sqLiteDatabase);
            } catch (Exception exc) {
                LocLookApp.showLog("SettingsManager: getBadgeController(): error: " +exc.getMessage());
            }
        }
        return badgeController;
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

    @Override
    public void onDatabaseCreateSuccess() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SettingsManager: onDatabaseCreateSuccess()");

        /*try {
            getSQLiteDatabase();
        } catch (Exception exc) {
            LocLookApp.showLog("SettingsManager: onDatabaseCreateSuccess: getSQLiteDatabase error: " +exc.getMessage());
        }*/

        getBadgeController().populateBadgesTable();
    }

    @Override
    public void onDatabaseCreateError(String error) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("SettingsManager: onDatabaseCreateError(): error: " +error);
    }
}
