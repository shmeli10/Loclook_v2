package com.androiditgroup.loclook.v2.data;

/**
 * Created by Serghei Ostrovschi on 7/5/16.
 */

public class DatabaseConstants {

    protected static final int DATABASE_VERSION 	                =   1;

    protected static final String DATABASE_NAME 	                =   "LOCLOOK_DATABASE";

    public static final String ROW_ID                               =   "_ID";

    public static final String USER_TABLE                           =   "USER_DATA";
    public static final String USER_NAME                            =   "NAME";
    public static final String USER_PHONE_NUMBER                    =   "PHONE_NUMBER";
    protected static final String USER_TABLE_COLUMNS                =   USER_NAME         + " TEXT NOT NULL,     " +
                                                                        USER_PHONE_NUMBER + " TEXT NOT NULL,     " +
                                                                        "RATE               INTEGER DEFAULT 0, " +
                                                                        "BG_PHOTO_ID        INTEGER DEFAULT 0, " +
                                                                        "AVATAR_PHOTO_ID    INTEGER DEFAULT 0, " +
//                                                                        "BACKGROUND BLOB,                 " +
//                                                                        "AVATAR BLOB,                     " +
                                                                        "DESCRIPTION        TEXT,              " +
                                                                        "SITE_URL           TEXT,              " +
                                                                        "LATITUDE           TEXT,              " +
                                                                        "LONGITUDE          TEXT,              " +
                                                                        "RADIUS             INTEGER DEFAULT 0, " +
                                                                        "REGION_NAME        TEXT,              " +
                                                                        "STREET_NAME        TEXT";

    public static final String BADGE_TABLE                          =   "BADGE_DATA";
    protected static final String BADGE_TABLE_COLUMNS               =   "NAME               TEXT NOT NULL";

    protected static final String PUBLICATION_TABLE                 =   "PUBLICATION_DATA";
    protected static final String PUBLICATION_TABLE_COLUMNS         =   "TEXT               TEXT NOT NULL,     " +
                                                                        "AUTHOR_ID          INTEGER NOT NULL,  " +
                                                                        "BADGE_ID           INTEGER NOT NULL,  " +
                                                                        "CREATED_AT         TEXT NOT NULL,     " +
                                                                        "LATITUDE           TEXT,              " +
                                                                        "LONGITUDE          TEXT,              " +
                                                                        "REGION_NAME        TEXT,              " +
                                                                        "STREET_NAME        TEXT,              " +
                                                                        "HAS_QUIZ           INTEGER DEFAULT 0, " +
                                                                        "HAS_IMAGES         INTEGER DEFAULT 0, " +
                                                                        "IS_ANONYMOUS       INTEGER DEFAULT 0";

    protected static final String QUIZ_ANSWER_TABLE                 =   "QUIZ_ANSWER_DATA";
    protected static final String QUIZ_ANSWER_TABLE_COLUMNS         =   "TEXT T             EXT NOT NULL,      " +
                                                                        "PUBLICATION_ID     INTEGER NOT NULL";

    protected static final String PHOTOS_TABLE                      =   "PHOTOS_DATA";
    protected static final String PHOTOS_TABLE_COLUMNS              =   "PHOTO              BLOB NOT NULL,     " +
                                                                        "PUBLICATION_ID     INTEGER NOT NULL";

    protected static final String USER_QUIZ_ANSWER_TABLE            =   "USER_QUIZ_ANSWER_DATA";
    protected static final String USER_QUIZ_ANSWER_TABLE_COLUMNS    =   "QUIZ_ANSWER_ID     INTEGER NOT NULL,  " +
                                                                        "USER_ID            INTEGER NOT NULL";

    protected static final String FAVORITES_TABLE                   =   "FAVORITES_DATA";
    protected static final String FAVORITES_TABLE_COLUMNS           =   "PUBLICATION_ID     INTEGER NOT NULL,  " +
                                                                        "USER_ID            INTEGER NOT NULL";

    protected static final String COMMENTS_TABLE                    =   "COMMENTS_DATA";
    protected static final String COMMENTS_TABLE_COLUMNS            =   "TEXT               TEXT NOT NULL,     " +
                                                                        "PUBLICATION_ID     INTEGER NOT NULL,  " +
                                                                        "USER_ID            INTEGER NOT NULL,  " +
                                                                        "RECIPIENT_ID       INTEGER NOT NULL,  " +
                                                                        "CREATED_AT         TEXT NOT NULL";

    protected static final String LIKES_TABLE                       =   "LIKES_DATA";
    protected static final String LIKES_TABLE_COLUMNS               =   "PUBLICATION_ID     INTEGER NOT NULL,  " +
                                                                        "USER_ID            INTEGER NOT NULL";
}
