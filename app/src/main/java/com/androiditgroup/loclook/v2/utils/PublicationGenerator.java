package com.androiditgroup.loclook.v2.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.models.Publication;
import com.androiditgroup.loclook.v2.models.QuizAnswer;
import com.androiditgroup.loclook.v2.models.User;

import java.util.ArrayList;

/**
 * Created by sostrovschi on 3/2/17.
 */

public class PublicationGenerator {

    public static Publication getPublicationFromCursor(Cursor cursor) {

        try {

            cursor.moveToFirst();

            String pId = cursor.getString(cursor.getColumnIndex("_ID"));
            String pText = cursor.getString(cursor.getColumnIndex("TEXT"));
            String pAuthorId = cursor.getString(cursor.getColumnIndex("AUTHOR_ID"));
            String pCreatedAt = cursor.getString(cursor.getColumnIndex("CREATED_AT"));
            String pLatitude = cursor.getString(cursor.getColumnIndex("LATITUDE"));
            String pLongitude = cursor.getString(cursor.getColumnIndex("LONGITUDE"));
            String pRegionName = cursor.getString(cursor.getColumnIndex("REGION_NAME"));
            String pStreetName = cursor.getString(cursor.getColumnIndex("STREET_NAME"));

            int pBadgeId = cursor.getInt(cursor.getColumnIndex("BADGE_ID"));

            boolean pHasQuiz = (cursor.getInt(cursor.getColumnIndex("HAS_QUIZ")) == 1);
            boolean pHasImages = (cursor.getInt(cursor.getColumnIndex("HAS_IMAGES")) == 1);
            boolean pIsAnonymous = (cursor.getInt(cursor.getColumnIndex("IS_ANONYMOUS")) == 1);

            Location pLocation = new Location("");

            if (pLatitude != null)
                pLocation.setLatitude(Double.parseDouble(pLatitude));

            if (pLongitude != null)
                pLocation.setLongitude(Double.parseDouble(pLongitude));

            //////////////////////////////////////////////////////////////////////////////////////

            Publication.Builder mPublicationBuilder = new Publication.Builder();

            if((pId != null) && (!pId.equals("")))
                mPublicationBuilder.id(pId);
            else
                return null;

            if((pAuthorId != null) && (!pAuthorId.equals("")))
                mPublicationBuilder.authorId(pAuthorId);
            else
                return null;

            //////////////////////////////////////////////////////////////////////////////////////////

            ArrayList<String> pQuizAnswersIdsList = new ArrayList<>();

            if(pHasQuiz){

            }

            //////////////////////////////////////////////////////////////////////////////////////////

            ArrayList<String> photosIdsList = new ArrayList<>();

            if(pHasImages) {

            }

            //////////////////////////////////////////////////////////////////////////////////////////


            mPublicationBuilder.text(pText);
            mPublicationBuilder.createdAt(pCreatedAt);
            mPublicationBuilder.location(pLocation);
            mPublicationBuilder.regionName(pRegionName);
            mPublicationBuilder.streetName(pStreetName);
            mPublicationBuilder.badge(String.valueOf(pBadgeId));
            mPublicationBuilder.hasQuiz(pHasQuiz);
            mPublicationBuilder.hasImages(pHasImages);
            mPublicationBuilder.isAnonymous(pIsAnonymous);
            mPublicationBuilder.quizAnswerList(pQuizAnswersIdsList);
            mPublicationBuilder.photosList(photosIdsList);

            // возвращаем публикацию
            return mPublicationBuilder.build();

        } catch(Exception exc) {
            Log.e("ABC", "PublicationGenerator: getPublicationFromCursor(): error: " +exc.toString());
        } finally {
            cursor.close();
        }

        return null;
    }

    /*
    public static ArrayList<Publication> getPublicationsList(Cursor pCursor) {

        ArrayList<Publication> result = new ArrayList<>();

        if((pCursor != null) && (pCursor.getCount() > 0)) {
            Log.e("ABC", "PublicationGenerator: getPublicationsList(): pCursor.getCount()= " + pCursor.getCount());
            pCursor.moveToFirst();

            try {

                do {

                    String pId = pCursor.getString(pCursor.getColumnIndex("_ID"));
                    String pText = pCursor.getString(pCursor.getColumnIndex("TEXT"));
                    String pAuthorId = pCursor.getString(pCursor.getColumnIndex("AUTHOR_ID"));
                    String pCreatedAt = pCursor.getString(pCursor.getColumnIndex("CREATED_AT"));
                    String pLatitude = pCursor.getString(pCursor.getColumnIndex("LATITUDE"));
                    String pLongitude = pCursor.getString(pCursor.getColumnIndex("LONGITUDE"));
                    String pRegionName = pCursor.getString(pCursor.getColumnIndex("REGION_NAME"));
                    String pStreetName = pCursor.getString(pCursor.getColumnIndex("STREET_NAME"));

                    int pBadgeId = pCursor.getInt(pCursor.getColumnIndex("BADGE_ID"));

                    boolean pHasQuiz = (pCursor.getInt(pCursor.getColumnIndex("HAS_QUIZ")) == 1);
                    boolean pHasImages = (pCursor.getInt(pCursor.getColumnIndex("HAS_IMAGES")) == 1);
                    boolean pIsAnonymous = (pCursor.getInt(pCursor.getColumnIndex("IS_ANONYMOUS")) == 1);

                    Location pLocation = new Location("");

                    if (pLatitude != null)
                        pLocation.setLatitude(Double.parseDouble(pLatitude));

                    if (pLongitude != null)
                        pLocation.setLongitude(Double.parseDouble(pLongitude));

                    //////////////////////////////////////////////////////////////////////////////////////////

                    if(!pIsAnonymous) {

                        Cursor authorNameCursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(),
                                                                                        Constants.DataBase.USER_TABLE,
                                                                                        null,
                                                                                        "_ID",
                                                                                        pAuthorId);

                    }

                    //////////////////////////////////////////////////////////////////////////////////////////

                    ArrayList<QuizAnswer> pQuizAnswerList = new ArrayList<>();

//                if(pHasQuiz){
//
//                }

                    //////////////////////////////////////////////////////////////////////////////////////////

                    ArrayList<Bitmap> photosList = new ArrayList<>();

//                if(pHasImages) {
//
//                }

                    //////////////////////////////////////////////////////////////////////////////////////////


                    Publication.Builder mPublicationBuilder = new Publication.Builder()
                            .id(pId)
                            .text(pText)
                            .authorId(pAuthorId)
                            .badge(LocLookApp.badgesList.get(pBadgeId))
                            .createdAt(pCreatedAt)
                            .location(pLocation)
                            .regionName(pRegionName)
                            .streetName(pStreetName)
                            .hasQuiz(pHasQuiz)
                            .hasImages(pHasImages)
                            .isAnonymous(pIsAnonymous)
                            .quizAnswerList(pQuizAnswerList)
                            .photosList(photosList);

                    result.add(mPublicationBuilder.build());

                } while (pCursor.moveToNext());

            } catch(Exception exc) {
                Log.e("ABC", "PublicationGenerator: getPublicationsList(): error: " +exc.toString());
            } finally {
                pCursor.close();
            }
        }
        else {
            Log.e("ABC", "PublicationGenerator: getPublicationsList(): pCursor is empty");
        }

        return result;
    }
    */
}
