package com.androiditgroup.loclook.v2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.interfaces.DatabaseCreateInterface;

/**
 * Created by Serghei Ostrovschi on 7/5/16.
 */

public class AppManager implements DatabaseCreateInterface {

    private Context                     context;
    private DatabaseHandler databaseHandler;
    private SQLiteDatabase              sqLiteDatabase;

    private BadgeController             badgeController;
    //private InitAppController           initAppController;
    private PhotoController             photoController;
    private PublicationController       publicationController;
    private SharedPreferencesController sharedPreferencesController;
    private UserController              userController;

    private boolean databaseIsCreated   = false;

    /**
     * AppManager constructor
     *
     * @param context   application context.
     */
    public AppManager(Context context) {
        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp.showLog("AppManager: constructor.");

        this.context = context;

        sharedPreferencesController = new SharedPreferencesController(context);

        try {
            databaseHandler = new DatabaseHandler(context, this);
        } catch (Exception exc) {
            LocLookApp.showLog("AppManager: constructor: get DatabaseHandler instance error: " +exc.getMessage());
        }

        if(databaseHandler != null) {
            //LocLookApp.showLog("AppManager: constructor: databaseHandler is not null");

            sqLiteDatabase = databaseHandler.getWritableDatabase();
            //LocLookApp.showLog("AppManager: constructor: sqLiteDatabase is null: " +(sqLiteDatabase == null));

            try {
                badgeController = new BadgeController(context, databaseHandler, sqLiteDatabase);
            } catch (Exception exc) {
                LocLookApp.showLog("AppManager: get BadgeController instance error: " +exc.getMessage());
            }

            try {
                userController = new UserController(databaseHandler,
                                                    sqLiteDatabase,
                                                    sharedPreferencesController);
            } catch (Exception exc) {
                LocLookApp.showLog("AppManager: get UserController instance error: " +exc.getMessage());
            }

            // ---------------------------------------------------------------------------- //

            if(databaseIsCreated) {
                populateDatabase();
            }
        }
        else {
            LocLookApp.showLog("AppManager: constructor: databaseHandler is null");
        }

        //sharedPreferencesController = new SharedPreferencesController(context);
        sharedPreferencesController.init();

        /*try {
            initAppController = new InitAppController(sharedPreferencesController);

//            settingsInitAppController = new SettingsInitAppController(sharedPreferencesController,
//                    settingsLanguageController,
//                    settingsAccountModelController,
//                    settingsAppDataController);
        } catch (Exception exc) {
            LocLookApp.showLog("AppManager: Create InitAppController instance error: " +exc.getMessage());
            return;
        }*/
    }

    public BadgeController getBadgeController() {
        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp.showLog("AppManager: getBadgeController() badgeController is null: " +(badgeController == null));

//        if(badgeController == null) {
//            try {
//                badgeController = new BadgeController(context, databaseHandler, sqLiteDatabase);
//            } catch (Exception exc) {
//                LocLookApp.showLog("AppManager: getBadgeController(): error: " +exc.getMessage());
//            }
//        }
        return badgeController;
    }

    public UserController getUserController() {
        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp.showLog("AppManager: getUserController()");

        return userController;
    }

    /**
     * Method gets link on existing instance of {@link SharedPreferencesController} class
     *
     * @return  link on existing instance of {@link SharedPreferencesController} class
     */
    public SharedPreferencesController getSharedPreferencesController() {
        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp.showLog("AppManager: getSharedPreferencesController()");

        return sharedPreferencesController;
    }

    /**
     * Method gets link on instance of {@link PhotoController} class
     *
     * @return  link on instance of {@link PhotoController} class
     */
    public PhotoController getPhotoController() {

        if(photoController == null) {
            try {
                photoController = new PhotoController(  databaseHandler,
                                                        sqLiteDatabase);
            } catch (Exception exc) {
                LocLookApp.showLog("AppManager: get PhotoController instance error: " +exc.getMessage());
            }
        }

        return photoController;
    }

    /**
     * Method gets link on instance of {@link PublicationController} class
     *
     * @return  link on instance of {@link PublicationController} class
     */
    public PublicationController getPublicationController() {

        if(publicationController == null) {
            try {
                publicationController = new PublicationController(databaseHandler,
                                                                  sqLiteDatabase,
                                                                  userController);
            } catch (Exception exc) {
                LocLookApp.showLog("AppManager: get PublicationController instance error: " +exc.getMessage());
            }
        }

        return publicationController;
    }

    /**
     * Method gets link on existing instance of {@link InitAppController} class
     *
     * @return  link on existing instance of {@link InitAppController} class
     */
    /*public InitAppController getInitAppController() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("AppManager: getInitAppController()");

        return initAppController;
    }*/

    private void populateDatabase() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("AppManager: populateDatabase()");

        badgeController.populateBadgesTable();
    }

    @Override
    public void onDatabaseCreateSuccess() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("AppManager: onDatabaseCreateSuccess()");

        databaseIsCreated = true;
    }

    @Override
    public void onDatabaseCreateError(String error) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("AppManager: onDatabaseCreateError(): error: " +error);
    }
}
