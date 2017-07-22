package com.androiditgroup.loclook.v2.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.data.DatabaseConstants;
import com.androiditgroup.loclook.v2.data.DatabaseHandler;
import com.androiditgroup.loclook.v2.models.Badge;
import com.androiditgroup.loclook.v2.models.BadgeModel;
import com.androiditgroup.loclook.v2.models.UserModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Serghei Ostrovschi on 7/7/17.
 */

public class BadgeController {

    private Context         context;
    private DatabaseHandler databaseHandler;
    private SQLiteDatabase  sqLiteDatabase;

    private Map<Integer, BadgeModel>    badgeMap            = new LinkedHashMap<>();
    private Map<Integer, Integer>       badgeImageResIdMap  = new LinkedHashMap<>();

    public BadgeController(Context          context,
                           DatabaseHandler  databaseHandler,
                           SQLiteDatabase   sqLiteDatabase) throws Exception {
        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp.showLog("BadgeController: constructor.");

        if(databaseHandler == null)
            throw new Exception(ErrorConstants.DATABASE_HANDLER_NULL_ERROR);

        if(sqLiteDatabase == null)
            throw new Exception(ErrorConstants.SQLITE_DATABASE_NULL_ERROR);

        this.databaseHandler    = databaseHandler;
        this.sqLiteDatabase     = sqLiteDatabase;
        this.context            = context;

        populateBadgeMap();
    }

    public Map<Integer, BadgeModel> getBadgeMap() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("BadgeController: getBadgeMap()");

        if(badgeMap == null)
            return new LinkedHashMap<>();

        return badgeMap;
    }

    public Map<Integer, Integer> getBadgeImageResIdMap() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("BadgeController: getBadgeImageResIdMap()");

        if(badgeImageResIdMap == null)
            return new LinkedHashMap<>();

        return badgeImageResIdMap;
    }

    public Integer getBadgeImageById(int badgeId) {

        if((badgeId > 0) && (badgeImageResIdMap.containsKey(badgeId)))
            return badgeImageResIdMap.get(badgeId);
        else
            return null;
    }

    public BadgeModel getBadgeById(int badgeId) {

        if((badgeId > 0) && (badgeMap.containsKey(badgeId)))
            return badgeMap.get(badgeId);
        else
            return null;
    }

    protected void populateBadgesTable() {
//        LocLookApp.showLog("-------------------------------------");
//        LocLookApp.showLog("BadgeController: populateBadgesTable()");

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

        if(insertedBadgesSum == badgesArray.length) {
            populateBadgeMap();
            LocLookApp.showLog("BadgeController: populateBadgesTable(): all badges inserted successfully");
        }
        else
            LocLookApp.showLog("BadgeController: populateBadgesTable(): was inserted " +insertedBadgesSum+ " badges");
    }

    private void addBadge(Cursor cursor) {
//        LocLookApp.showLog("-------------------------------------");
//        LocLookApp.showLog("BadgeController: addBadge()");

        boolean noErrors = true;

        BadgeModel badge = new BadgeModel();

        try {
            badge.setBadgeId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.ROW_ID)));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("BadgeController: addBadge(): set badge id error: " +exc.getMessage());
        }

        try {
            badge.setBadgeName(cursor.getString(cursor.getColumnIndex(DatabaseConstants.BADGE_NAME)));
        } catch (Exception exc) {
            noErrors = false;
            LocLookApp.showLog("BadgeController: addBadge(): set badge name error: " +exc.getMessage());
        }

        // ------------------------------------------------------------------------------- //

        if(noErrors){
            badgeMap.put(badge.getBadgeId(), badge);

            int badgeImageResId = context.getResources().getIdentifier("@drawable/badge_" +badge.getBadgeId(),
                                                                       null,
                                                                       context.getPackageName());

            badgeImageResIdMap.put(badge.getBadgeId(), badgeImageResId);
            //LocLookApp.showLog("BadgeController: addBadge(): badge: " +badge.getBadgeId()+ " added");
        }
        else {
            LocLookApp.showLog("BadgeController: addBadge(): badge will not be added, error occured");
        }
    }

    public void populateBadgeMap() {
        //LocLookApp.showLog("-------------------------------------");
        //LocLookApp.showLog("BadgeController: populateBadgeMap()");

        Cursor cursor = databaseHandler.queryColumns(sqLiteDatabase, DatabaseConstants.BADGE_TABLE, null);

        // ------------------------------------------------------------------------------------ //

        //LocLookApp.showLog("BadgeController: populateBadgeMap(): cursor is null: " +(cursor == null));

//        if(cursor != null)
//            LocLookApp.showLog("BadgeController: populateBadgeMap(): cursor rows: " +cursor.getCount());

        // ------------------------------------------------------------------------------------ //

        if((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();

            do{
                addBadge(cursor);
            } while(cursor.moveToNext());
        }
    }
}
