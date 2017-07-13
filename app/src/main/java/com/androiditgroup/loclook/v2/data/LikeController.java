package com.androiditgroup.loclook.v2.data;

import android.database.Cursor;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Serghei Ostrovschi on 7/4/17.
 */

public class LikeController {

    private static LikeController likeController = null;

    private Map<Integer, List<Integer>> sortedByPublicationIdLikesMap   = new HashMap<>();

    public static LikeController getInstance()  {

        if(likeController == null) {
            likeController = new LikeController();
        }

        return likeController;
    }

    public void populateSortedByPublicationIdLikesMap() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("LikeController: populateSortedByPublicationIdLikesMap()");

        sortedByPublicationIdLikesMap.clear();

        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(),
                                                             Constants.DataBase.LIKES_TABLE,
                                                             null, null, null, null);

        if((cursor != null) && (cursor.getCount() > 0)) {
            LocLookApp.showLog("LikeController: populateSortedByPublicationIdLikesMap(): cursor.getCount()= " + cursor.getCount());
            cursor.moveToFirst();

            try {

                do {

                    int likePublicationId   = cursor.getInt(cursor.getColumnIndex("PUBLICATION_ID"));
                    int likeAuthorId        = cursor.getInt(cursor.getColumnIndex("USER_ID"));

                    if(sortedByPublicationIdLikesMap.containsKey(likePublicationId)) {

                        List<Integer> usersIdsList = sortedByPublicationIdLikesMap.get(likePublicationId);
                        usersIdsList.add(likeAuthorId);
                    }
                    else {
                        List<Integer> usersIdsList = new ArrayList<>();
                        usersIdsList.add(likeAuthorId);

                        sortedByPublicationIdLikesMap.put(likePublicationId, usersIdsList);
                    }

                } while (cursor.moveToNext());

                LocLookApp.showLog("LikeController: populateSortedByPublicationIdLikesMap(): sortedByPublicationIdLikesMap size= " +sortedByPublicationIdLikesMap.size());

            } catch(Exception exc) {
                LocLookApp.showLog("LikeController: populateSortedByPublicationIdLikesMap(): error: " +exc.toString());
            } finally {
                cursor.close();
            }
        }
    }

    public int getPublicationLikesSum(int publicationId) {

        if(sortedByPublicationIdLikesMap.containsKey(publicationId)) {
            return sortedByPublicationIdLikesMap.get(publicationId).size();
        }
        else
            return 0;
    }
}
