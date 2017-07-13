package com.androiditgroup.loclook.v2.data;

/**
 * Created by Serghei Ostrovschi on 7/5/16.
 */

public class DatabaseConstants {

    protected static final int DATABASE_VERSION 	                        =   1;

    protected static final String DATABASE_NAME 	                        =   "LOCLOOK_DATABASE";

    protected static final String ROW_ID                                    =   "_ID";

    protected static final String USER_TABLE                                =   "USER_DATA";
    protected static final String USER_NAME                                 =   "NAME";
    protected static final String USER_PHONE_NUMBER                         =   "PHONE_NUMBER";
    protected static final String USER_RATE                                 =   "RATE";
    protected static final String USER_BG_PHOTO_ID                          =   "BG_PHOTO_ID";
    protected static final String USER_AVATAR_PHOTO_ID                      =   "AVATAR_PHOTO_ID";
    protected static final String USER_DESCRIPTION                          =   "DESCRIPTION";
    protected static final String USER_SITE_URL                             =   "SITE_URL";
    protected static final String USER_LATITUDE                             =   "LATITUDE";
    protected static final String USER_LONGITUDE                            =   "LONGITUDE";
    protected static final String USER_MAP_RADIUS                           =   "RADIUS";
    protected static final String USER_REGION_NAME                          =   "REGION_NAME";
    protected static final String USER_STREET_NAME                          =   "STREET_NAME";
    protected static final String USER_TABLE_COLUMNS                        =   USER_NAME                           + " TEXT NOT NULL,      " +
                                                                                USER_PHONE_NUMBER                   + " TEXT NOT NULL,      " +
                                                                                USER_RATE                           + " INTEGER DEFAULT 0,  " +
                                                                                USER_BG_PHOTO_ID                    + " INTEGER DEFAULT 0,  " +
                                                                                USER_AVATAR_PHOTO_ID                + " INTEGER DEFAULT 0,  " +
                                                                                USER_DESCRIPTION                    + " TEXT,               " +
                                                                                USER_SITE_URL                       + " TEXT,               " +
                                                                                USER_LATITUDE                       + " TEXT,               " +
                                                                                USER_LONGITUDE                      + " TEXT,               " +
                                                                                USER_MAP_RADIUS                     + " INTEGER DEFAULT 0,  " +
                                                                                USER_REGION_NAME                    + " TEXT,               " +
                                                                                USER_STREET_NAME                    + " TEXT";

    protected static final String BADGE_TABLE                               =   "BADGE_DATA";
    protected static final String BADGE_NAME                                =   "NAME";
    protected static final String BADGE_TABLE_COLUMNS                       =   BADGE_NAME                          + " TEXT NOT NULL";

    protected static final String PUBLICATION_TABLE                         =   "PUBLICATION_DATA";
    protected static final String PUBLICATION_TEXT                          =   "TEXT";
    protected static final String PUBLICATION_AUTHOR_ID                     =   "AUTHOR_ID";
    protected static final String PUBLICATION_BADGE_ID                      =   "BADGE_ID";
    protected static final String PUBLICATION_CREATED_AT                    =   "CREATED_AT";
    protected static final String PUBLICATION_LATITUDE                      =   "LATITUDE";
    protected static final String PUBLICATION_LONGITUDE                     =   "LONGITUDE";
    protected static final String PUBLICATION_REGION_NAME                   =   "REGION_NAME";
    protected static final String PUBLICATION_STREET_NAME                   =   "STREET_NAME";
    protected static final String PUBLICATION_HAS_QUIZ                      =   "HAS_QUIZ";
    protected static final String PUBLICATION_HAS_IMAGES                    =   "HAS_IMAGES";
    protected static final String PUBLICATION_IS_ANONYMOUS                  =   "IS_ANONYMOUS";
    protected static final String PUBLICATION_TABLE_COLUMNS                 =   PUBLICATION_TEXT                    + " TEXT NOT NULL,      " +
                                                                                PUBLICATION_AUTHOR_ID               + " INTEGER NOT NULL,   " +
                                                                                PUBLICATION_BADGE_ID                + " INTEGER NOT NULL,   " +
                                                                                PUBLICATION_CREATED_AT              + " TEXT NOT NULL,      " +
                                                                                PUBLICATION_LATITUDE                + " TEXT,               " +
                                                                                PUBLICATION_LONGITUDE               + " TEXT,               " +
                                                                                PUBLICATION_REGION_NAME             + " TEXT,               " +
                                                                                PUBLICATION_STREET_NAME             + " TEXT,               " +
                                                                                PUBLICATION_HAS_QUIZ                + " INTEGER DEFAULT 0,  " +
                                                                                PUBLICATION_HAS_IMAGES              + " INTEGER DEFAULT 0,  " +
                                                                                PUBLICATION_IS_ANONYMOUS            + " INTEGER DEFAULT 0";

    protected static final String QUIZ_ANSWER_TABLE                         =   "QUIZ_ANSWER_DATA";
    protected static final String QUIZ_ANSWER_TEXT                          =   "TEXT";
    protected static final String QUIZ_ANSWER_PUBLICATION_ID                =   "PUBLICATION_ID";
    protected static final String QUIZ_ANSWER_TABLE_COLUMNS                 =   QUIZ_ANSWER_TEXT                    + " TEXT NOT NULL,      " +
                                                                                QUIZ_ANSWER_PUBLICATION_ID          + " INTEGER NOT NULL";

    protected static final String PHOTO_TABLE                               =   "PHOTO_DATA";
    protected static final String PHOTO_DATA                                =   "DATA";
    protected static final String PHOTO_PUBLICATION_ID                      =   "PUBLICATION_ID";
    protected static final String PHOTOS_TABLE_COLUMNS                      =   PHOTO_DATA                          + " BLOB NOT NULL,      " +
                                                                                PHOTO_PUBLICATION_ID                + " INTEGER NOT NULL";

    protected static final String USER_QUIZ_ANSWER_TABLE                    =   "USER_QUIZ_ANSWER_DATA";
    protected static final String USER_QUIZ_ANSWER_QUIZ_ANSWER_ID           =   "QUIZ_ANSWER_ID";
    protected static final String USER_QUIZ_ANSWER_USER_ID                  =   "USER_ID";
    protected static final String USER_QUIZ_ANSWER_TABLE_COLUMNS            =   USER_QUIZ_ANSWER_QUIZ_ANSWER_ID     + " INTEGER NOT NULL,  " +
                                                                                USER_QUIZ_ANSWER_USER_ID            + " INTEGER NOT NULL";

    protected static final String FAVORITE_PUBLICATION_TABLE                =   "FAVORITE_PUBLICATION_DATA";
    protected static final String FAVORITE_PUBLICATION_PUBLICATION_ID       =   "PUBLICATION_ID";
    protected static final String FAVORITE_PUBLICATION_USER_ID              =   "USER_ID";
    protected static final String FAVORITE_PUBLICATION_TABLE_COLUMNS        =   FAVORITE_PUBLICATION_PUBLICATION_ID + " INTEGER NOT NULL,  " +
                                                                                FAVORITE_PUBLICATION_USER_ID        + " INTEGER NOT NULL";

    protected static final String COMMENT_TABLE                             =   "COMMENT_DATA";
    protected static final String COMMENT_TEXT                              =   "TEXT";
    protected static final String COMMENT_PUBLICATION_ID                    =   "PUBLICATION_ID";
    protected static final String COMMENT_USER_ID                           =   "USER_ID";
    protected static final String COMMENT_RECIPIENT_ID                      =   "RECIPIENT_ID";
    protected static final String COMMENT_CREATED_AT                        =   "CREATED_AT";
    protected static final String COMMENT_TABLE_COLUMNS                     =   COMMENT_TEXT                        + " TEXT NOT NULL,     " +
                                                                                COMMENT_PUBLICATION_ID              + " INTEGER NOT NULL,  " +
                                                                                COMMENT_USER_ID                     + " INTEGER NOT NULL,  " +
                                                                                COMMENT_RECIPIENT_ID                + " INTEGER NOT NULL,  " +
                                                                                COMMENT_CREATED_AT                  + " TEXT NOT NULL";

    protected static final String LIKE_PUBLICATION_TABLE                    =   "LIKE_PUBLICATION_DATA";
    protected static final String LIKE_PUBLICATION_PUBLICATION_ID           =   "PUBLICATION_ID";
    protected static final String LIKE_PUBLICATION_USER_ID                  =   "USER_ID";
    protected static final String LIKE_PUBLICATION_TABLE_COLUMNS            =   LIKE_PUBLICATION_PUBLICATION_ID     + " INTEGER NOT NULL,  " +
                                                                                LIKE_PUBLICATION_USER_ID            + " INTEGER NOT NULL";
}
