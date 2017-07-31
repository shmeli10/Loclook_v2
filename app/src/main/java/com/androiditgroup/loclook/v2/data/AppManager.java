package com.androiditgroup.loclook.v2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.interfaces.DatabaseCreateInterface;

/**
 * Created by Serghei Ostrovschi on 7/5/16.
 */

public class AppManager implements DatabaseCreateInterface {

    private Context                     context;
    private DatabaseHandler             databaseHandler;
    private SQLiteDatabase              sqLiteDatabase;

    private BadgeController             badgeController;
    //private InitAppController           initAppController;
    private PhotoController             photoController;
    private PublicationController       publicationController;
    private QuizController              quizController;
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
            Log.e("LOG", "AppManager: constructor: get DatabaseHandler instance error: " +exc.getMessage());
        }

        if(databaseHandler != null) {
            //Log.e("LOG", "AppManager: constructor: databaseHandler is not null");

            sqLiteDatabase = databaseHandler.getWritableDatabase();
            //Log.e("LOG", "AppManager: constructor: sqLiteDatabase is null: " +(sqLiteDatabase == null));

            try {
                badgeController = new BadgeController(context, databaseHandler, sqLiteDatabase);
            } catch (Exception exc) {
                Log.e("LOG", "AppManager: get BadgeController instance error: " +exc.getMessage());
            }

            try {
                photoController = new PhotoController(databaseHandler,
                                                      sqLiteDatabase);
            } catch (Exception exc) {
                Log.e("LOG", "AppManager: get PhotoController instance error: " +exc.getMessage());
            }

            try {
                userController = new UserController(databaseHandler,
                                                    sqLiteDatabase,
                                                    sharedPreferencesController);
            } catch (Exception exc) {
                Log.e("LOG", "AppManager: get UserController instance error: " +exc.getMessage());
            }

            try {
                quizController = new QuizController(databaseHandler,
                                                    sqLiteDatabase);
            } catch (Exception exc) {
                Log.e("LOG", "AppManager: get QuizController instance error: " +exc.getMessage());
            }

            // ---------------------------------------------------------------------------- //

            if(databaseIsCreated) {
                populateDatabase();
            }
        }
        else {
            Log.e("LOG", "AppManager: constructor: databaseHandler is null");
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
            Log.e("LOG", "AppManager: Create InitAppController instance error: " +exc.getMessage());
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
//                Log.e("LOG", "AppManager: getBadgeController(): error: " +exc.getMessage());
//            }
//        }
        return badgeController;
    }

    public UserController getUserController() {
        //Log.e("LOG", "-------------------------------------");
        //Log.e("LOG", "AppManager: getUserController()");

        return userController;
    }

    /**
     * Method gets link on existing instance of {@link SharedPreferencesController} class
     *
     * @return  link on existing instance of {@link SharedPreferencesController} class
     */
    public SharedPreferencesController getSharedPreferencesController() {
        //Log.e("LOG", "-------------------------------------");
        //Log.e("LOG", "AppManager: getSharedPreferencesController()");

        return sharedPreferencesController;
    }

    /**
     * Method gets link on instance of {@link PhotoController} class
     *
     * @return  link on instance of {@link PhotoController} class
     */
    public PhotoController getPhotoController() {

//        if(photoController == null) {
//            try {
//                photoController = new PhotoController(  databaseHandler,
//                                                        sqLiteDatabase);
//            } catch (Exception exc) {
//                Log.e("LOG", "AppManager: get PhotoController instance error: " +exc.getMessage());
//            }
//        }

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
                publicationController = new PublicationController(  databaseHandler,
                                                                    sqLiteDatabase,
                                                                    userController,
                                                                    quizController,
                                                                    photoController);
            } catch (Exception exc) {
                Log.e("LOG", "AppManager: get PublicationController instance error: " +exc.getMessage());
            }
        }

        return publicationController;
    }

    /**
     * Method gets link on instance of {@link QuizController} class
     *
     * @return  link on instance of {@link QuizController} class
     */
    public QuizController getQuizController() {

//        if(quizController == null) {
//            try {
//                quizController = new QuizController(databaseHandler,
//                                                    sqLiteDatabase);
//            } catch (Exception exc) {
//                Log.e("LOG", "AppManager: get QuizController instance error: " +exc.getMessage());
//            }
//        }

        return quizController;
    }

    /**
     * Method gets link on existing instance of {@link InitAppController} class
     *
     * @return  link on existing instance of {@link InitAppController} class
     */
    /*public InitAppController getInitAppController() {
        Log.e("LOG", "-------------------------------------");
        Log.e("LOG", "AppManager: getInitAppController()");

        return initAppController;
    }*/

    private void populateDatabase() {
        Log.e("LOG", "-------------------------------------");
        Log.e("LOG", "AppManager: populateDatabase()");

        badgeController.populateBadgesTable();
    }

    @Override
    public void onDatabaseCreateSuccess() {
        Log.e("LOG", "-------------------------------------");
        Log.e("LOG", "AppManager: onDatabaseCreateSuccess()");

        databaseIsCreated = true;
    }

    @Override
    public void onDatabaseCreateError(String error) {
        Log.e("LOG", "-------------------------------------");
        Log.e("LOG", "AppManager: onDatabaseCreateError(): error: " +error);
    }
}
