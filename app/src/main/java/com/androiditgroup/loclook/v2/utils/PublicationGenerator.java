package com.androiditgroup.loclook.v2.utils;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.models.Publication;
import com.androiditgroup.loclook.v2.models.Quiz;
import com.androiditgroup.loclook.v2.models.QuizAnswer;
import com.androiditgroup.loclook.v2.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
                        // LocLookApp.quizAnswersMap.put(quizAnswer.getId(), quizAnswer);
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
            mPublicationBuilder.dateAndTime(getFormattedDate(pCreatedAt));
            mPublicationBuilder.location(pLocation);
            mPublicationBuilder.regionName(pRegionName);
            mPublicationBuilder.streetName(pStreetName);
            mPublicationBuilder.badge(String.valueOf(pBadgeId));
            mPublicationBuilder.hasQuiz(pHasQuiz);
            mPublicationBuilder.hasImages(pHasImages);
            mPublicationBuilder.isAnonymous(pIsAnonymous);
            // mPublicationBuilder.quizAnswerList(pQuizAnswersIdsList);
            mPublicationBuilder.photosList(photosIdsList);

            // возвращаем публикацию
            return mPublicationBuilder.build();

        } catch(Exception exc) {
            LocLookApp.showLog("PublicationGenerator: getPublicationFromCursor(): error: " +exc.toString());
        } finally {
            pCursor.close();
        }

        return null;
    }

    public static ArrayList<Publication> getPublicationsList(Cursor pCursor) {

        ArrayList<Publication> result = new ArrayList<>();

        if((pCursor != null) && (pCursor.getCount() > 0)) {
            // LocLookApp.showLog("PublicationGenerator: getPublicationsList(): pCursor.getCount()= " + pCursor.getCount());
            pCursor.moveToFirst();

            // ArrayList<QuizAnswer> mQuizAnswerList = new ArrayList<>();

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

                    // -------------------------------- QUIZ ---------------------------------- //

                    Quiz pQuiz = null;

//                    LocLookApp.showLog("PublicationGenerator: getPublicationsList(): publication: " +pId+ ", has quiz: " +pHasQuiz);

                    // если публикация содержит опрос
                    if(pHasQuiz){

                        // получаем курсор с вариантами ответа опраса из БД
                        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.QUIZ_ANSWER_TABLE, null, "PUBLICATION_ID", pId);

//                        LocLookApp.showLog("PublicationGenerator: getPublicationsList(): publication: " +pId+ ", cursor is null: " +(cursor == null));

                        // если данные получены
                        if(cursor != null) {
                            Quiz.Builder mQuizBuilder = new Quiz.Builder();

                            ArrayList<QuizAnswer> list = QuizAnswerGenerator.getQuizAnswersList(cursor);

//                            LocLookApp.showLog("PublicationGenerator: getPublicationsList(): publication: " +pId+ ", list is null: " +(list == null));

                            if(list != null) {
                                pQuiz = mQuizBuilder.answersList(list).build();
                                QuizUtility.setQuizAnswersVotesSum(pQuiz);
                                QuizUtility.setQuizAnswersVotesInPercents(pQuiz);
                                QuizUtility.setUserSelectedQuizAnswer(pQuiz, true);
                            }
                        }
                    }

                    // ------------------------------ PHOTOS ---------------------------------- //

                    ArrayList<String> photosIdsList = new ArrayList<>();

                    if(pHasImages) {

                        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.PHOTOS_TABLE, null, "PUBLICATION_ID", pId);

                        try {

                            if(cursor.getCount() > 0) {
                                while (cursor.moveToNext()) {

                                    String photoId  = cursor.getString(cursor.getColumnIndex("_ID"));
                                    byte[] photoArr = cursor.getBlob(cursor.getColumnIndex("PHOTO"));

                                    //////////////////////////////////////////////////////////////////////////////////////

                                    if(photoArr != null) {
                                        // LocLookApp.showLog("PublicationGenerator: getPublicationsList(): publication(" +pId+ ") photoArr size=: " +photoArr.length);

                                        Bitmap bitmap = DbBitmapUtility.getImage(photoArr);

                                        if((photoId != null) && (bitmap != null)) {
                                            // добавляем изображение в коллекцию
                                            LocLookApp.imagesMap.put(photoId, bitmap);
                                            // LocLookApp.tumbnailsMap.put(photoId, ImageDelivery.getResizedBitmap(bitmap, Constants.TUMBNAIL_WIDTH, Constants.TUMBNAIL_HEIGHT));

                                            // добавляем идентификатор изображения в список
                                            photosIdsList.add(photoId);
                                        }
                                    }
                                }
                            }
                        } catch(Exception exc) {
                            LocLookApp.showLog("PublicationGenerator: getPublicationsList(): error: " +exc.toString());
                        } finally {
                            cursor.close();
                        }
                    }

                    // ------------------------------ COMMENTS -------------------------------- //

                    int pCommentsSum = 0;

                    Cursor commentsCursor = DBManager.getInstance().getPublicationCommentsSum(DBManager.getInstance().getDataBase(), pId);

                    try {

                        if(commentsCursor.getCount() > 0) {
                            while (commentsCursor.moveToNext()) {
                                pCommentsSum = commentsCursor.getInt(commentsCursor.getColumnIndex("COMMENTS_SUM"));
                            }
                        }
                    } catch(Exception exc) {
                        LocLookApp.showLog("PublicationGenerator: getPublicationsList(): error: " +exc.toString());
                    } finally {
                        commentsCursor.close();
                    }

                    // -------------------------------- LIKES --------------------------------- //

                    int pLikesSum = 0;

                    Cursor likesCursor = DBManager.getInstance().getPublicationLikesSum(DBManager.getInstance().getDataBase(), pId);

                    try {

                        if(likesCursor.getCount() > 0) {
                            while (likesCursor.moveToNext()) {
                                pLikesSum = likesCursor.getInt(likesCursor.getColumnIndex("LIKES_SUM"));
                            }
                        }
                    } catch(Exception exc) {
                        LocLookApp.showLog("PublicationGenerator: getPublicationsList(): error: " +exc.toString());
                    } finally {
                        likesCursor.close();
                    }

                    // ------------------------------------------------------------------------ //

                    mPublicationBuilder.text(pText);
                    mPublicationBuilder.dateAndTime(getFormattedDate(pCreatedAt));
                    mPublicationBuilder.location(pLocation);
                    mPublicationBuilder.regionName(pRegionName);
                    mPublicationBuilder.streetName(pStreetName);
                    mPublicationBuilder.badge(String.valueOf(pBadgeId));
                    mPublicationBuilder.hasQuiz(pHasQuiz);
                    mPublicationBuilder.hasImages(pHasImages);
                    mPublicationBuilder.isAnonymous(pIsAnonymous);
                    mPublicationBuilder.quiz(pQuiz);
                    mPublicationBuilder.photosList(photosIdsList);
                    mPublicationBuilder.commentsSum(pCommentsSum);
                    mPublicationBuilder.likesSum(pLikesSum);

                    result.add(mPublicationBuilder.build());

                } while (pCursor.moveToNext());

            } catch(Exception exc) {
                LocLookApp.showLog("PublicationGenerator: getPublicationsList(): error: " +exc.toString());
            } finally {
                pCursor.close();
            }
        }

        return result;
    }

    private static String getFormattedDate(String dateInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
        long dateLong = Long.parseLong(dateInMillis);
        Date now = new Date(dateLong);
        return sdf.format(now);
    }
}
