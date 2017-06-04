package com.androiditgroup.loclook.v2.utils;

import android.database.Cursor;
import android.text.TextUtils;

import com.androiditgroup.loclook.v2.LocLookApp;

import java.util.ArrayList;

/**
 * Created by starlab on 3/28/17.
 */

public class LikesUtility {

    public static void addPublicationToUserLikes(final ArrayList<String> userLikesList, final String publicationId) {

        Thread addLikesThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Cursor cursor = DBManager.getInstance().addPublicationToLikes(publicationId);

                if((cursor != null) && (cursor.getCount() > 0)) {

                    try {
                        cursor.moveToFirst();

                        String likesRowId = cursor.getString(cursor.getColumnIndex("_ID"));

                        if(!TextUtils.isEmpty(likesRowId)) {
                            LocLookApp.showLog("LikesUtility: addPublicationToUsersLikes(): Saved successfully. Id= " +likesRowId);
                            userLikesList.add(publicationId);
                        }
                        else {
                            LocLookApp.showLog("LikesUtility: addPublicationToUsersLikes(): Save error(0)");
                        }
                    } catch(Exception exc) {
                        LocLookApp.showLog("LikesUtility: addPublicationToUsersLikes(): Save error: " +exc.toString());
                    } finally {
                        cursor.close();
                    }
                }
                else {
                    LocLookApp.showLog("LikesUtility: addPublicationToUsersLikes(): Save error(1)");
                }
            }
        });

        addLikesThread.start();
    }

    public static void deletePublicationFromUserLikes(final ArrayList<String> userLikesList, final String publicationId) {
        Thread deleteFromLikesThread = new Thread(new Runnable() {
            @Override
            public void run() {

                boolean result = DBManager.getInstance().deletePublicationFromLikes(publicationId);

                if(result) {
                    LocLookApp.showLog("LikesUtility: deletePublicationFromUserLikes(): Deleted successfully.");
                    userLikesList.remove(publicationId);
                }
            }
        });

        deleteFromLikesThread.start();
    }

    public static ArrayList<String> getUserLikes() {

        ArrayList<String> result = new ArrayList<>();

        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.FAVORITES_TABLE, null, "USER_ID", LocLookApp.appUserId, "_ID");

        if((cursor != null) && (cursor.getCount() > 0)) {
            LocLookApp.showLog("LikesUtility: getUserLikes(): cursor.getCount()= " +cursor.getCount());
            cursor.moveToFirst();

            try {

                do {
                    // получаем очередное значение из курсора
                    String publicationId = cursor.getString(cursor.getColumnIndex("PUBLICATION_ID"));

                    // если значение получено
                    if(!TextUtils.isEmpty(publicationId))
                        // добавляем его в список
                        result.add(publicationId);

                } while (cursor.moveToNext());

            } catch(Exception exc) {
                LocLookApp.showLog("LikesUtility: getUserLikes(): error: " +exc.toString());
            } finally {
                cursor.close();
            }
        }

        return result;
    }
}
