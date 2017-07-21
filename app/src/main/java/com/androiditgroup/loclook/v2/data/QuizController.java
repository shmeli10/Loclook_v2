package com.androiditgroup.loclook.v2.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.models.QuizAnswerModel;
import com.androiditgroup.loclook.v2.models.QuizModel;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Serghei Ostrovschi on 7/4/17.
 */

public class QuizController {

    private DatabaseHandler     databaseHandler;
    private SQLiteDatabase      sqLiteDatabase;

    private Map<Integer, QuizModel> sortedByPublicationIdQuizMap = new TreeMap<>();

    public QuizController(DatabaseHandler    databaseHandler,
                          SQLiteDatabase     sqLiteDatabase) throws Exception {

        if(databaseHandler == null)
            throw new Exception(ErrorConstants.DATABASE_HANDLER_NULL_ERROR);

        if(sqLiteDatabase == null)
            throw new Exception(ErrorConstants.SQLITE_DATABASE_NULL_ERROR);

        this.databaseHandler    = databaseHandler;
        this.sqLiteDatabase     = sqLiteDatabase;
    }


    /*public static QuizController getQuizControllerInstance()  {

        if(quizController == null) {
            quizController = new QuizController();
        }

        return quizController;
    }*/

    private void addQuizToCollections(Cursor cursor) {
        Log.e("LOG", "-------------------------------------");
        Log.e("LOG", "QuizController: addQuizToCollections()");

        boolean noErrors = true;

        QuizAnswerModel quizAnswer = new QuizAnswerModel();

        try {
            quizAnswer.setQuizAnswerId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.ROW_ID)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "QuizController: addQuizToCollections(): set quiz answer id error: " +exc.getMessage());
        }

        try {
            quizAnswer.setQuizAnswerPublicationId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.QUIZ_ANSWER_PUBLICATION_ID)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "QuizController: addQuizToCollections(): set quiz answer publication id error: " +exc.getMessage());
        }

        try {
            quizAnswer.setQuizAnswerText(cursor.getString(cursor.getColumnIndex(DatabaseConstants.QUIZ_ANSWER_TEXT)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "QuizController: addQuizToCollections(): set quiz answer text error: " +exc.getMessage());
        }


        // ------------------------------------------------------------------------------- //

        if(noErrors){

            Log.e("LOG", "QuizController: addQuizToCollections(): quizAnswerPublicationId= " +quizAnswer.getQuizAnswerPublicationId());

            Log.e("LOG", "QuizController: addQuizToCollections(): sortedByPublicationIdQuizMap.contains quizAnswerPublicationId: " +(sortedByPublicationIdQuizMap.containsKey(quizAnswer.getQuizAnswerPublicationId())));

            if(sortedByPublicationIdQuizMap.containsKey(quizAnswer.getQuizAnswerPublicationId())) {

                QuizModel quiz = sortedByPublicationIdQuizMap.get(quizAnswer.getQuizAnswerPublicationId());
                quiz.getQuizAnswerList().add(quizAnswer);

                Log.e("LOG", "QuizController: addQuizToCollections(): (1) quizAnswer: " +quizAnswer.getQuizAnswerId()+ " added");
            }
            else {

                QuizModel quiz = new QuizModel();
                quiz.getQuizAnswerList().add(quizAnswer);

                sortedByPublicationIdQuizMap.put(quizAnswer.getQuizAnswerPublicationId(), quiz);

                Log.e("LOG", "QuizController: addQuizToCollections(): (2) quizAnswer: " +quizAnswer.getQuizAnswerId()+ " added");
            }
        }
        else {
            Log.e("LOG", "QuizController: addQuizToCollections(): quiz will not be added, error occured");
        }
    }

    public void populateQuizCollections() {
        Log.e("LOG", "-------------------------------------");
        Log.e("LOG", "QuizController: populateQuizCollections()");

        sortedByPublicationIdQuizMap.clear();

        Cursor cursor = databaseHandler.queryColumns(sqLiteDatabase, DatabaseConstants.QUIZ_ANSWER_TABLE, null);

        Log.e("LOG", "QuizController: populateQuizCollections(): cursor is null: " +(cursor == null));

        if((cursor != null) && (cursor.getCount() > 0)) {
            Log.e("LOG", "QuizController: populateQuizCollections(): cursor rows sum= " +cursor.getCount());
            cursor.moveToFirst();

            // Log.e("LOG", "UserController: populateUserMap(): cursor rows: " +cursor.getCount());

            do {
                addQuizToCollections(cursor);
            } while (cursor.moveToNext());
        }
    }

    public QuizModel getPublicationQuiz(int publicationId) throws Exception {

        if(publicationId <= 0)
            throw new Exception(ErrorConstants.PUBLICATION_ID_ERROR);

        if(sortedByPublicationIdQuizMap.containsKey(publicationId))
            return sortedByPublicationIdQuizMap.get(publicationId);
        else
            return null;
    }

    public boolean createQuiz(int               publicationId,
                              ArrayList<String> quizAnswerList) throws Exception {
        Log.e("LOG", "-------------------------------------");
        Log.e("LOG", "QuizController: createQuiz()");

        // Cursor cursor   = null;

        boolean result  = false;

        boolean noErrors = true;

        int addedAnswersSum = 0;

        if(quizAnswerList == null)
            throw new Exception(ErrorConstants.QUIZ_ANSWER_LIST_NULL_ERROR);

        if(publicationId <= 0)
            throw new Exception(ErrorConstants.PUBLICATION_ID_ERROR);

        sqLiteDatabase.beginTransaction();

        try {

            String[] columnsArr = { DatabaseConstants.QUIZ_ANSWER_TEXT,
                                    DatabaseConstants.QUIZ_ANSWER_PUBLICATION_ID};

            for(int i=0; i<quizAnswerList.size(); i++) {

                String[] dataArr = {quizAnswerList.get(i),
                                    String.valueOf(publicationId)};

                Cursor quizAnswerCursor = databaseHandler.insertRow(sqLiteDatabase,
                                                                    DatabaseConstants.QUIZ_ANSWER_TABLE,
                                                                    columnsArr,
                                                                    dataArr);

                if((quizAnswerCursor == null) || (quizAnswerCursor.getCount() <= 0)) {
                    noErrors = false;
                }
                else
                    addedAnswersSum++;

                quizAnswerCursor.close();
            }

            if((noErrors) && (addedAnswersSum == quizAnswerList.size())) {

                Cursor quizCursor = databaseHandler.queryColumns(sqLiteDatabase,
                                                                 DatabaseConstants.QUIZ_ANSWER_TABLE,
                                                                 null,
                                                                 DatabaseConstants.QUIZ_ANSWER_PUBLICATION_ID,
                                                                 String.valueOf(publicationId));

                if((quizCursor != null) || (quizCursor.getCount() > 0)) {
                    quizCursor.moveToFirst();

                    //addQuizToCollections(quizCursor);

                    do {
                        addQuizToCollections(quizCursor);
                    } while (quizCursor.moveToNext());

                    sqLiteDatabase.setTransactionSuccessful();

                    result = true;
                }

                quizCursor.close();
            }

        } catch(SQLiteException sqliteExc) {
            Log.e("LOG", "QuizController: createPublication(): SQLiteException: " +sqliteExc.getMessage());
        } catch(Exception exc) {
            //Error in between database transaction
            Log.e("LOG", "QuizController: createPublication(): Exception: " +exc.getMessage());
        } finally {
            sqLiteDatabase.endTransaction();
        }

        return result;
    }
}
