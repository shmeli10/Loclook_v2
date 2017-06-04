package com.androiditgroup.loclook.v2.utils;

import android.database.Cursor;
import android.text.TextUtils;

import com.androiditgroup.loclook.v2.LocLookApp;

import java.util.ArrayList;

/**
 * Created by OS1 on 15.04.2017.
 */
public class CommentsUtility {

    public static ArrayList<String> getUserCommentedPublicationsList() {

        ArrayList<String> result = new ArrayList<>();

        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.COMMENTS_TABLE, null, "USER_ID", LocLookApp.appUserId, "_ID");

        if((cursor != null) && (cursor.getCount() > 0)) {
            LocLookApp.showLog("CommentsUtility: getUserCommentedPublicationsList(): cursor.getCount()= " +cursor.getCount());
            cursor.moveToFirst();

            try {

                do {
                    // получаем очередное значение из курсора
                    String publicationId = cursor.getString(cursor.getColumnIndex("PUBLICATION_ID"));

                    // если значение получено и его еще нет в списке
                    if((!TextUtils.isEmpty(publicationId)) && (!result.contains(publicationId))) {
                        // добавляем его в список
                        result.add(publicationId);
                    }

                } while (cursor.moveToNext());

            } catch(Exception exc) {
                LocLookApp.showLog("CommentsUtility: getUserCommentedPublicationsList(): error: " +exc.toString());
            } finally {
                cursor.close();
            }
        }

        return result;
    }
}
