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

    // public static Publication getPublicationFromCursor(Cursor cursor) {
    public static Publication getPublicationFromCursor(Cursor pCursor, ArrayList<QuizAnswer> quizAnswerList, ArrayList<Bitmap> photosList) {

        ArrayList<QuizAnswer> mQuizAnswerList = new ArrayList<>();

        try {
            pCursor.moveToFirst();

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

            //////////////////////////////////////////////////////////////////////////////////////

            Publication.Builder mPublicationBuilder = new Publication.Builder();

            if((pId != null) && (!pId.equals("")))
                mPublicationBuilder.id(pId);
            else
                return null;

            if((pAuthorId != null) && (!pAuthorId.equals(""))) {
                mPublicationBuilder.authorId(pAuthorId);

                User author = UserGenerator.getUserById(pAuthorId);

                if((author != null) && (!LocLookApp.usersMap.containsKey(author.getId()))) {
                    LocLookApp.usersMap.put(author.getId(), author);
                }
            }
            else
                return null;

            //////////////////////////////////////////////////////////////////////////////////////////

            ArrayList<String> pQuizAnswersIdsList = new ArrayList<>();

            // если публикация содержит опрос
            if(pHasQuiz){

                // если список с вариантами ответа задан
                if(quizAnswerList != null) {
                    // получаем список из заданных значений
                    mQuizAnswerList.addAll(quizAnswerList);
                }
                // если список с вариантами ответа не задан
                else {
                    // получаем курсор с вариантами ответа опраса из БД
                    Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.QUIZ_ANSWER_TABLE, null, "PUBLICATION_ID", pId);

                    // если данные получены
                    if(cursor != null)
                        // получаем список с вариантами ответа опроса из БД
                        mQuizAnswerList.addAll(QuizAnswerGenerator.getQuizAnswersList(cursor));
                }

                // проходим циклом по списку
                for(int i=0; i<mQuizAnswerList.size(); i++) {
                    // получаем очередной вариант ответа
                    QuizAnswer quizAnswer = mQuizAnswerList.get(i);

                    // если вариант получен
                    if(quizAnswer != null) {
                        // добавляем его идентификатор в "список с идентификаторами вариантов ответов опроса в публикации"
                        pQuizAnswersIdsList.add(quizAnswer.getId());

                        // кладем вариант ответа в "коллецию всех вариантов ответов всех опросов в публикациях"
                        LocLookApp.quizAnswersMap.put(quizAnswer.getId(), quizAnswer);
                    }
                }
            }

            //////////////////////////////////////////////////////////////////////////////////////////

            ArrayList<String> photosIdsList = new ArrayList<>();

            if(pHasImages) {

                if((photosList != null) && (photosList.size() > 0)) {

                }
                else {

                }
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
            pCursor.close();
        }

        return null;
    }

    public static ArrayList<Publication> getPublicationsList(Cursor pCursor) {

        ArrayList<Publication> result = new ArrayList<>();

        if((pCursor != null) && (pCursor.getCount() > 0)) {
            Log.e("ABC", "PublicationGenerator: getPublicationsList(): pCursor.getCount()= " + pCursor.getCount());
            pCursor.moveToFirst();

            ArrayList<QuizAnswer> mQuizAnswerList = new ArrayList<>();

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

                    //////////////////////////////////////////////////////////////////////////////////////

                    Publication.Builder mPublicationBuilder = new Publication.Builder();

                    if((pId != null) && (!pId.equals("")))
                        mPublicationBuilder.id(pId);
                    else
                        return null;

                    if((pAuthorId != null) && (!pAuthorId.equals(""))) {
                        mPublicationBuilder.authorId(pAuthorId);

                        User author = UserGenerator.getUserById(pAuthorId);

                        if((author != null) && (!LocLookApp.usersMap.containsKey(author.getId()))) {
                            LocLookApp.usersMap.put(author.getId(), author);
                        }
                    }
                    else
                        return null;

                    //////////////////////////////////////////////////////////////////////////////////////////

                    ArrayList<String> pQuizAnswersIdsList = new ArrayList<>();

                    // если публикация содержит опрос
                    if(pHasQuiz){

                        // получаем курсор с вариантами ответа опраса из БД
                        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.QUIZ_ANSWER_TABLE, null, "PUBLICATION_ID", pId);

                        // если данные получены
                        if(cursor != null)
                            // получаем список с вариантами ответа опроса из БД
                            mQuizAnswerList.addAll(QuizAnswerGenerator.getQuizAnswersList(cursor));

                        // проходим циклом по списку
                        for(int i=0; i<mQuizAnswerList.size(); i++) {
                            // получаем очередной вариант ответа
                            QuizAnswer quizAnswer = mQuizAnswerList.get(i);

                            // если вариант получен
                            if(quizAnswer != null) {
                                // добавляем его идентификатор в "список с идентификаторами вариантов ответов опроса в публикации"
                                pQuizAnswersIdsList.add(quizAnswer.getId());

                                // кладем вариант ответа в "коллецию всех вариантов ответов всех опросов в публикациях"
                                LocLookApp.quizAnswersMap.put(quizAnswer.getId(), quizAnswer);
                            }
                        }
                    }

                    //////////////////////////////////////////////////////////////////////////////////////////

                    ArrayList<String> photosIdsList = new ArrayList<>();

//                    if(pHasImages) {
//
//                        if((photosList != null) && (photosList.size() > 0)) {
//
//                        }
//                        else {
//
//                        }
//                    }

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

//                    String pId = pCursor.getString(pCursor.getColumnIndex("_ID"));
//                    String pText = pCursor.getString(pCursor.getColumnIndex("TEXT"));
//                    String pAuthorId = pCursor.getString(pCursor.getColumnIndex("AUTHOR_ID"));
//                    String pCreatedAt = pCursor.getString(pCursor.getColumnIndex("CREATED_AT"));
//                    String pLatitude = pCursor.getString(pCursor.getColumnIndex("LATITUDE"));
//                    String pLongitude = pCursor.getString(pCursor.getColumnIndex("LONGITUDE"));
//                    String pRegionName = pCursor.getString(pCursor.getColumnIndex("REGION_NAME"));
//                    String pStreetName = pCursor.getString(pCursor.getColumnIndex("STREET_NAME"));
//
//                    int pBadgeId = pCursor.getInt(pCursor.getColumnIndex("BADGE_ID"));
//
//                    boolean pHasQuiz = (pCursor.getInt(pCursor.getColumnIndex("HAS_QUIZ")) == 1);
//                    boolean pHasImages = (pCursor.getInt(pCursor.getColumnIndex("HAS_IMAGES")) == 1);
//                    boolean pIsAnonymous = (pCursor.getInt(pCursor.getColumnIndex("IS_ANONYMOUS")) == 1);
//
//                    Location pLocation = new Location("");
//
//                    if (pLatitude != null)
//                        pLocation.setLatitude(Double.parseDouble(pLatitude));
//
//                    if (pLongitude != null)
//                        pLocation.setLongitude(Double.parseDouble(pLongitude));
//
//                    //////////////////////////////////////////////////////////////////////////////////////////
//
//                    if(!pIsAnonymous) {
//
//                        Cursor authorNameCursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(),
//                                                                                        Constants.DataBase.USER_TABLE,
//                                                                                        null,
//                                                                                        "_ID",
//                                                                                        pAuthorId);
//
//                    }
//
//                    //////////////////////////////////////////////////////////////////////////////////////////
//
//                    ArrayList<QuizAnswer> pQuizAnswerList = new ArrayList<>();
//
////                if(pHasQuiz){
////
////                }
//
//                    //////////////////////////////////////////////////////////////////////////////////////////
//
//                    ArrayList<Bitmap> photosList = new ArrayList<>();
//
////                if(pHasImages) {
////
////                }
//
//                    //////////////////////////////////////////////////////////////////////////////////////////
//
//
//                    Publication.Builder mPublicationBuilder = new Publication.Builder()
//                            .id(pId)
//                            .text(pText)
//                            .authorId(pAuthorId)
//                            .badge(LocLookApp.badgesList.get(pBadgeId))
//                            .createdAt(pCreatedAt)
//                            .location(pLocation)
//                            .regionName(pRegionName)
//                            .streetName(pStreetName)
//                            .hasQuiz(pHasQuiz)
//                            .hasImages(pHasImages)
//                            .isAnonymous(pIsAnonymous)
//                            .quizAnswerList(pQuizAnswerList)
//                            .photosList(photosList);

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
}
