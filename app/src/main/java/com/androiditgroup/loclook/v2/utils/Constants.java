package com.androiditgroup.loclook.v2.utils;

import com.androiditgroup.loclook.v2.LocLookApp;

import java.io.File;

/**
 * Created by sostrovschi on 09.01.2017.
 */

public class Constants {

    public static final String RUSSIAN = "ru";
    public static final String ENGLISH = "en";

    public static final String LONG_RUSSIAN = "Russian";
    public static final String LONG_ENGLISH = "English";

    public static final String DB_CACHE_DIR = LocLookApp.getInstance().getBaseContext().getCacheDir().getPath() + File.separator + "dbcache" + File.separator;

    public static final int QUIZ_MAX_ANSWERS = 10;

    public static class DataBase {
        public static final int DB_VERSION = 1;
        public static final String DATABASE_NAME = "cache_database";

//        public static final String ID = "_id";
//        public static final String FNAME = "f_name";
//        public static final String LNAME = "l_name";
//        public static final String MNAME = "m_name";
//        public static final String PHONE = "phone";
//        public static final String IMG_URL = "img_url";

        public static final String USER_DATA_TABLE = "USER_DATA_TABLE";
        public static final String USER_DATA_TABLE_COLUMNS = "NAME TEXT  NOT NULL, PHONE_NUMBER TEXT  NOT NULL, RATE INTEGER, BG_IMG_URL TEXT, AVATAR_URL TEXT, " +
                                                             "DESCRIPTION TEXT, SITE_URL TEXT, LATITUDE TEXT, LONGITUDE TEXT, RADIUS TEXT, REGION_NAME TEXT, STREET_NAME TEXT";

//        public static final String USER_AUTH_DATA_TABLE = "USER_AUTH_DATA_TABLE";
//        public static final String USER_AUTH_DATA_TABLE_COLUMNS = "USER_ID INTEGER NOT NULL, ENTER_DATE TEXT NOT NULL, ENTER_CODE INTEGER NOT NULL";



//        public static final String CREATE_USER_DATA_TABLE = "CREATE TABLE IF NOT EXISTS " +  USER_DATA_TABLE +
//                                                            " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                                                            "" + FNAME + " TEXT NOT NULL, " +
//                                                            "" + LNAME + " TEXT NOT NULL, " +
//                                                            "" + MNAME + " TEXT, " +
//                                                            "" + PHONE + " TEXT NOT NULL, " +
//                                                            "" + IMG_URL + " TEXT);";

//        public static final String DROP_USER_DATA_TABLE = "DROP TABLE IF EXISTS " + USER_DATA_TABLE;
    }
}
