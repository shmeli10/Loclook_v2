package com.androiditgroup.loclook.v2.controllers;


import android.database.Cursor;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.models.Publication;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serghei Ostrovschi on 7/4/17.
 */

public class PublicationController {

    private static PublicationController publicationController = null;

    private List<Publication> allPublicationsList = new ArrayList<>();

    public static PublicationController getInstance()  {

        if(publicationController == null) {
            publicationController = new PublicationController();
        }

        return publicationController;
    }

    public void populateAllPublicationsList() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("PublicationController: populateAllPublicationsList()");

        allPublicationsList.clear();

        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(),
                                                             Constants.DataBase.PUBLICATION_TABLE,
                                                             null);

        if((cursor != null) && (cursor.getCount() > 0)) {
            LocLookApp.showLog("PublicationController: populateAllPublicationsList(): cursor.getCount()= " + cursor.getCount());
            cursor.moveToFirst();

            try {

                do {

                    int publicationId = cursor.getInt(cursor.getColumnIndex("_ID"));

                    Publication.Builder publicationBuilder = new Publication.Builder();
                    publicationBuilder.publicationId(publicationId);
                    publicationBuilder.publicationAuthorId(cursor.getInt(cursor.getColumnIndex("AUTHOR_ID")));
                    publicationBuilder.publicationBadgeId(cursor.getInt(cursor.getColumnIndex("BADGE_ID")));
                    publicationBuilder.publicationCommentsSum(CommentController.getCommentsControllerInstance().getPublicationCommentsSum(publicationId));
                    publicationBuilder.publicationLikesSum(LikeController.getInstance().getPublicationLikesSum(publicationId));
                    publicationBuilder.publicationText(cursor.getString(cursor.getColumnIndex("TEXT")));
                    publicationBuilder.publicationLatitude(cursor.getString(cursor.getColumnIndex("LATITUDE")));
                    publicationBuilder.publicationLongitude(cursor.getString(cursor.getColumnIndex("LONGITUDE")));
                    publicationBuilder.publicationRegionName(cursor.getString(cursor.getColumnIndex("REGION_NAME")));
                    publicationBuilder.publicationStreetName(cursor.getString(cursor.getColumnIndex("STREET_NAME")));
                    publicationBuilder.publicationCreatedAt(cursor.getString(cursor.getColumnIndex("CREATED_AT")));
                    publicationBuilder.publicationHasQuiz(cursor.getInt(cursor.getColumnIndex("HAS_QUIZ")) > 0);
                    publicationBuilder.publicationHasImages(cursor.getInt(cursor.getColumnIndex("HAS_IMAGES")) > 0);
                    publicationBuilder.publicationIsAnonymous(cursor.getInt(cursor.getColumnIndex("IS_ANONYMOUS")) > 0);

                } while (cursor.moveToNext());

                LocLookApp.showLog("PublicationController: populateAllPublicationsList(): allPublicationsList size= " +allPublicationsList.size());

            } catch(Exception exc) {
                LocLookApp.showLog("PublicationController: populateAllPublicationsList(): error: " +exc.toString());
            } finally {
                cursor.close();
            }

        }
    }
}
