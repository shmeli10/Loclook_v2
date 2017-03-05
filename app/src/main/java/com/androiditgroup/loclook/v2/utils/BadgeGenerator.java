package com.androiditgroup.loclook.v2.utils;

import android.database.Cursor;
import android.util.Log;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.models.Badge;
import java.util.ArrayList;

/**
 * Created by OS1 on 05.03.2017.
 */
public class BadgeGenerator {

    public static ArrayList<Badge> getBadgesList(Cursor cursor) {

        ArrayList<Badge> result = new ArrayList<>();

        if((cursor != null) && (cursor.getCount() > 0)) {
            Log.e("ABC", "BadgeGenerator: getBadgesList(): pCursor.getCount()= " + cursor.getCount());
            cursor.moveToFirst();

            try {

                do {

                    String badgeId   = cursor.getString(cursor.getColumnIndex("_ID"));
                    String name = cursor.getString(cursor.getColumnIndex("NAME"));

                    //////////////////////////////////////////////////////////////////////////////////////

                    Badge.Builder mBadgeBuilder = new Badge.Builder();

                    if((badgeId != null) && (!badgeId.equals("")))
                        mBadgeBuilder.id(badgeId);
                    else
                        return null;

                    if((name != null) && (!name.equals("")))
                        mBadgeBuilder.name(name);
                    else
                        return null;

                    mBadgeBuilder.iconResId(LocLookApp.getDrawableResId("badge_" +badgeId));

                    result.add(mBadgeBuilder.build());

                } while (cursor.moveToNext());

            } catch(Exception exc) {
                Log.e("ABC", "BadgeGenerator: getBadgesList(): error: " +exc.toString());
            } finally {
                cursor.close();
            }
        }
        else {
            Log.e("ABC", "BadgeGenerator: getBadgesList(): pCursor is empty");
        }

        return result;
    }
}
