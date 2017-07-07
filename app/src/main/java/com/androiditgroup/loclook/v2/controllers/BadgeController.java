package com.androiditgroup.loclook.v2.controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.data.DatabaseConstants;
import com.androiditgroup.loclook.v2.data.DatabaseHandler;

/**
 * Created by Serghei Ostrovschi on 7/7/17.
 */

public class BadgeController {

    private Context         context;
    private DatabaseHandler databaseHandler;
    private SQLiteDatabase  sqLiteDatabase;

    public BadgeController(Context          context,
                           DatabaseHandler  databaseHandler,
                           SQLiteDatabase   sqLiteDatabase) throws Exception {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("BadgeController: constructor.");

        if(databaseHandler == null)
            throw new Exception(ErrorConstants.DATABASE_HANDLER_NULL_ERROR);

        if(sqLiteDatabase == null)
            throw new Exception(ErrorConstants.SQLITE_DATABASE_NULL_ERROR);

        this.databaseHandler    = databaseHandler;
        this.sqLiteDatabase     = sqLiteDatabase;
        this.context            = context;
    }

    protected void populateBadgesTable() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("BadgeController: populateBadgesTable()");

        String[] badgesArray  = context.getResources().getStringArray(R.array.badges);
        String[] columnsArray = {"NAME"};

        int insertedBadgesSum = 0;

        for(int i=0; i<badgesArray.length; i++) {

            Cursor cursor = databaseHandler.insertRow(sqLiteDatabase,
                                                      DatabaseConstants.BADGE_TABLE,
                                                      columnsArray,
                                                      new String[]{badgesArray[i]});

            if((cursor != null) && (cursor.getCount() > 0))
                insertedBadgesSum++;
        }

        if(insertedBadgesSum == badgesArray.length)
            LocLookApp.showLog("BadgeController: populateBadgesTable(): all badges inserted successfully");
        else
            LocLookApp.showLog("BadgeController: populateBadgesTable(): was inserted " +insertedBadgesSum+ " badges");
    }
}
