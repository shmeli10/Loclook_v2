package com.androiditgroup.loclook.v2.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.text.TextUtils;

import com.androiditgroup.loclook.v2.LocLookApp;

import java.util.ArrayList;

/**
 * Created by starlab on 3/28/17.
 */

public class FavoritesUtility {

    // public static boolean addPublicationToUserFavorites(final String publicationId) {
    public static void addPublicationToUserFavorites(final ArrayList<String> userFavoritesList, final String publicationId) {

        Thread addFavoritesThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Cursor cursor = DBManager.getInstance().addPublicationToFavorites(publicationId);

                if((cursor != null) && (cursor.getCount() > 0)) {

                    try {
                        cursor.moveToFirst();

                        String favoritesRowId = cursor.getString(cursor.getColumnIndex("_ID"));

                        if(!TextUtils.isEmpty(favoritesRowId)) {
                            LocLookApp.showLog("FavoritesUtility: addPublicationToUsersFavorites(): Saved successfully. Id= " +favoritesRowId);
                            // result[0] = true;
                            // handler.sendEmptyMessage(1);
                            userFavoritesList.add(publicationId);
                        }
                        else {
                            LocLookApp.showLog("FavoritesUtility: addPublicationToUsersFavorites(): Save error(0)");
                        }
                    } catch(Exception exc) {
                        LocLookApp.showLog("FavoritesUtility: addPublicationToUsersFavorites(): Save error: " +exc.toString());
                    } finally {
                        cursor.close();
                    }
                }
                else {
                    LocLookApp.showLog("FavoritesUtility: addPublicationToUsersFavorites(): Save error(1)");
                }
            }
        });

        addFavoritesThread.start();
    }

    // public static boolean deletePublicationFromUserFavorites(String publicationId) {
    public static void deletePublicationFromUserFavorites(final ArrayList<String> userFavoritesList, final String publicationId) {
        Thread deleteFromFavoritesThread = new Thread(new Runnable() {
            @Override
            public void run() {

                boolean result = DBManager.getInstance().deletePublicationFromFavorites(publicationId);

                if(result) {
                    LocLookApp.showLog("FavoritesUtility: deletePublicationFromUserFavorites(): Deleted successfully.");
                    userFavoritesList.remove(publicationId);
                }
            }
        });

        deleteFromFavoritesThread.start();
    }

    public static ArrayList<String> getUserFavorites() {

        ArrayList<String> result = new ArrayList<>();

        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.FAVORITES_TABLE, null, "USER_ID", LocLookApp.appUserId, "_ID");

        if((cursor != null) && (cursor.getCount() > 0)) {
            LocLookApp.showLog("FavoritesUtility: getUserFavorites(): cursor.getCount()= " +cursor.getCount());
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
                LocLookApp.showLog("FavoritesUtility: getUserFavorites(): error: " +exc.toString());
            } finally {
                cursor.close();
            }

        }

        return result;
    }
}
