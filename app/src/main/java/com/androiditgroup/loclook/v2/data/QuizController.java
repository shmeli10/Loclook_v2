package com.androiditgroup.loclook.v2.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.models.QuizAnswerModel;
import com.androiditgroup.loclook.v2.models.QuizModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Serghei Ostrovschi on 7/4/17.
 */

public class QuizController {

    private DatabaseHandler     databaseHandler;
    private SQLiteDatabase      sqLiteDatabase;

    // key - publicationId, value - QuizModel
    private Map<Integer, QuizModel> quizMap = new TreeMap<>();

    // key - quizAnswerId, value - List<userId>
    private Map<Integer, List<Integer>> quizAnswerMap = new TreeMap<>();


    public QuizController(DatabaseHandler    databaseHandler,
                          SQLiteDatabase     sqLiteDatabase) throws Exception {

        if(databaseHandler == null)
            throw new Exception(ErrorConstants.DATABASE_HANDLER_NULL_ERROR);

        if(sqLiteDatabase == null)
            throw new Exception(ErrorConstants.SQLITE_DATABASE_NULL_ERROR);

        this.databaseHandler    = databaseHandler;
        this.sqLiteDatabase     = sqLiteDatabase;
    }

    private void addQuizAnswerToCollections(Cursor cursor) {
        //Log.e("LOG", "-------------------------------------");
        //Log.e("LOG", "QuizController: addQuizAnswerToCollections()");

        boolean noErrors = true;

        QuizAnswerModel quizAnswer = new QuizAnswerModel();

        try {
            quizAnswer.setQuizAnswerId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.ROW_ID)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "QuizController: addQuizAnswerToCollections(): set quiz answer id error: " +exc.getMessage());
        }

        try {
            quizAnswer.setQuizAnswerPublicationId(cursor.getInt(cursor.getColumnIndex(DatabaseConstants.QUIZ_ANSWER_PUBLICATION_ID)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "QuizController: addQuizAnswerToCollections(): set quiz answer publication id error: " +exc.getMessage());
        }

        try {
            quizAnswer.setQuizAnswerText(cursor.getString(cursor.getColumnIndex(DatabaseConstants.QUIZ_ANSWER_TEXT)));
        } catch (Exception exc) {
            noErrors = false;
            Log.e("LOG", "QuizController: addQuizAnswerToCollections(): set quiz answer text error: " +exc.getMessage());
        }

        // ------------------------------------------------------------------------------- //

        if(noErrors){

            int quizAnswerId = quizAnswer.getQuizAnswerId();

            Cursor quizAnswerCursor = databaseHandler.queryColumns( sqLiteDatabase,
                                                                    DatabaseConstants.USER_QUIZ_ANSWER_TABLE,
                                                                    null,
                                                                    DatabaseConstants.USER_QUIZ_ANSWER_QUIZ_ANSWER_ID,
                                                                    String.valueOf(quizAnswerId));

            // ---------------------------------------------------------------------------------------------------------------------- //
            // TEST
            //Log.e("LOG", "QuizController: addQuizAnswerToCollections(): quizAnswerCursor is null: " +(quizAnswerCursor == null));

            /*if(quizAnswerCursor != null) {
                Log.e("LOG", "QuizController: addQuizAnswerToCollections(): quizAnswerCursor rows sum= " + quizAnswerCursor.getCount());
            }*/

            // ---------------------------------------------------------------------------------------------------------------------- //

            if((quizAnswerCursor != null) && (quizAnswerCursor.getCount() > 0)) {
                quizAnswerCursor.moveToFirst();

                do {

                    int userId = quizAnswerCursor.getInt(quizAnswerCursor.getColumnIndex(DatabaseConstants.USER_QUIZ_ANSWER_USER_ID));

                    if(quizAnswerMap.containsKey(quizAnswerId)) {

                        List<Integer> userIdList = quizAnswerMap.get(quizAnswerId);
                        userIdList.add(userId);

                        //Log.e("LOG", "QuizController: addQuizAnswerToCollections(): (1) userId: " +userId+ " added to quizAnswerId: " +quizAnswerId);
                    }
                    else {

                        List<Integer> userIdList = new ArrayList<>();
                        userIdList.add(userId);

                        quizAnswerMap.put(quizAnswerId, userIdList);

                        //Log.e("LOG", "QuizController: addQuizAnswerToCollections(): (2) userId: " +userId+ " added to quizAnswerId: " +quizAnswerId);
                    }

                } while (quizAnswerCursor.moveToNext());
            }

            quizAnswerCursor.close();

            // ------------------------------------------------------------------------------- //

            //Log.e("LOG", "QuizController: addQuizAnswerToCollections(): quizAnswerPublicationId= " +quizAnswer.getQuizAnswerPublicationId());

            //Log.e("LOG", "QuizController: addQuizAnswerToCollections(): quizMap.contains quizAnswerPublicationId: " +(quizMap.containsKey(quizAnswer.getQuizAnswerPublicationId())));

            QuizModel quiz = null;

            if(quizMap.containsKey(quizAnswer.getQuizAnswerPublicationId())) {

                quiz = quizMap.get(quizAnswer.getQuizAnswerPublicationId());
                quiz.getQuizAnswerList().add(quizAnswer);

                //Log.e("LOG", "QuizController: addQuizAnswerToCollections(): (1) quizAnswer: " +quizAnswerId+ " added");
            }
            else {

                quiz = new QuizModel();
                quiz.getQuizAnswerList().add(quizAnswer);

                quizMap.put(quizAnswer.getQuizAnswerPublicationId(), quiz);

                //Log.e("LOG", "QuizController: addQuizAnswerToCollections(): (2) quizAnswer: " +quizAnswerId+ " added");
            }

            if(quiz != null) {

                List<Integer> userIdList = quizAnswerMap.get(quizAnswerId);

                if(userIdList != null) {

                    int allVotesSum         = quiz.getAllVotesSum();
                    int quizAnswerVotesSum  = userIdList.size();

                    quiz.setAllVotesSum(allVotesSum + quizAnswerVotesSum);
                }
            }
        }
        else {
            Log.e("LOG", "QuizController: addQuizAnswerToCollections(): quiz will not be added, error occured");
        }
    }

    public void populateQuizCollections() {
        //Log.e("LOG", "-------------------------------------");
        //Log.e("LOG", "QuizController: populateQuizCollections()");

        quizMap.clear();
        quizAnswerMap.clear();

        Cursor cursor = databaseHandler.queryColumns(sqLiteDatabase,
                                                     DatabaseConstants.QUIZ_ANSWER_TABLE,
                                                     null);

        //Log.e("LOG", "QuizController: populateQuizCollections(): cursor is null: " +(cursor == null));

        if((cursor != null) && (cursor.getCount() > 0)) {
            //Log.e("LOG", "QuizController: populateQuizCollections(): cursor rows sum= " +cursor.getCount());
            cursor.moveToFirst();

            do {
                addQuizAnswerToCollections(cursor);
            } while (cursor.moveToNext());
        }
    }

    public QuizModel getPublicationQuiz(int publicationId) throws Exception {

        if(publicationId <= 0)
            throw new Exception(ErrorConstants.PUBLICATION_ID_ERROR);

        if(quizMap.containsKey(publicationId))
            return quizMap.get(publicationId);
        else
            return null;
    }

    public boolean createQuiz(int               publicationId,
                              ArrayList<String> quizAnswerList) throws Exception {
        //Log.e("LOG", "-------------------------------------");
        //Log.e("LOG", "QuizController: createQuiz()");

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

                    do {
                        addQuizAnswerToCollections(quizCursor);
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

    // ------------------------------------- GETTERS ----------------------------------------- //

    public int getQuizAnswerVotesSum(QuizAnswerModel quizAnswer) throws Exception {

        if(quizAnswer == null)
            throw new Exception(ErrorConstants.QUIZ_ANSWER_NULL_ERROR);

        int result = 0;

        List<Integer> quizAnswerUserIdList = quizAnswerMap.get(quizAnswer.getQuizAnswerId());

        if(quizAnswerUserIdList != null)
            result = quizAnswerUserIdList.size();

        return result;
    }

    public int getQuizAnswerVotesInPercent(QuizModel quiz, QuizAnswerModel quizAnswer) throws Exception {

        if(quiz == null)
            throw new Exception(ErrorConstants.QUIZ_NULL_ERROR);

        if(quizAnswer == null)
            throw new Exception(ErrorConstants.QUIZ_ANSWER_NULL_ERROR);

        int result = 0;

        List<Integer> quizAnswerUserIdList = quizAnswerMap.get(quizAnswer.getQuizAnswerId());

        if(quizAnswerUserIdList != null) {

            int allVotesSum        = quiz.getAllVotesSum();
            int quizAnswerVotesSum = quizAnswerUserIdList.size();

            // если хоть один человек выбрал этот вариант ответа
            if((allVotesSum > 0) && (quizAnswerVotesSum > 0)) {

                // получаем результат
                double value = (100 / allVotesSum) * quizAnswerVotesSum;

                // приводим результат к целому числу
                result = (int) value;
            }
        }

        return result;
    }

    // ------------------------------------- UTILS ----------------------------------------- //

    public boolean isQuizAnsweredByUser(QuizModel quiz, int userId) throws Exception {

        if(quiz == null)
            throw new Exception(ErrorConstants.QUIZ_NULL_ERROR);

        if(userId <= 0)
            throw new Exception(ErrorConstants.USER_ID_ERROR);

        boolean result = false;

        List<QuizAnswerModel> quizAnswerList = quiz.getQuizAnswerList();

        if(quizAnswerList != null) {

            for(int i=0; i<quizAnswerList.size(); i++) {

                int quizAnswerId = quizAnswerList.get(i).getQuizAnswerId();

                if(quizAnswerMap.containsKey(quizAnswerId)) {
                    List<Integer> userList = quizAnswerMap.get(quizAnswerId);

                    if(userList.contains(userId)) {
                        result = true;

                        break;
                    }
                }
            }
        }

        return result;
    }

    public boolean saveQuizAnswerVote(QuizModel quiz, int quizAnswerId, int userId) throws Exception {

        if(quiz == null)
            throw new Exception(ErrorConstants.QUIZ_NULL_ERROR);

        if(quizAnswerId <= 0)
            throw new Exception(ErrorConstants.QUIZ_ANSWER_ID_ERROR);

        if(userId <= 0)
            throw new Exception(ErrorConstants.USER_ID_ERROR);

        Cursor cursor    = null;

        boolean result   = false;

        boolean noErrors = true;

        sqLiteDatabase.beginTransaction();

        try {

            String[] columnsArr = { DatabaseConstants.USER_QUIZ_ANSWER_QUIZ_ANSWER_ID,
                                    DatabaseConstants.USER_QUIZ_ANSWER_USER_ID};

            String[] dataArr  = {   String.valueOf(quizAnswerId),
                                    String.valueOf(userId)};

            cursor = databaseHandler.insertRow( sqLiteDatabase,
                                                DatabaseConstants.USER_QUIZ_ANSWER_TABLE,
                                                columnsArr,
                                                dataArr);

            // ---------------------------------------------------------------------------------------------------------------------- //
            // TEST
            //Log.e("LOG", "QuizController: saveQuizAnswerVote(): cursor is null: " +(cursor == null));

            /*if(cursor != null)
                Log.e("LOG", "QuizController: saveQuizAnswerVote(): cursor rows sum= " +cursor.getCount());*/

            // ---------------------------------------------------------------------------------------------------------------------- //

            if((cursor == null) || (cursor.getCount() <= 0)) {
                noErrors = false;
            }
            else {
                cursor.moveToFirst();

                if(quizAnswerMap.containsKey(quizAnswerId)) {

                    List<Integer> quizAnswerUserIdList = quizAnswerMap.get(quizAnswerId);
                    quizAnswerUserIdList.add(userId);

                    //Log.e("LOG", "QuizController: saveQuizAnswerVote(): (1) userId: " +userId+ " added to quizAnswerId: " +quizAnswerId);
                }
                else {

                    List<Integer> quizAnswerUserIdList = new ArrayList<>();
                    quizAnswerUserIdList.add(userId);

                    quizAnswerMap.put(quizAnswerId, quizAnswerUserIdList);

                    //Log.e("LOG", "QuizController: saveQuizAnswerVote(): (2) userId: " +userId+ " added to quizAnswerId: " +quizAnswerId);
                }

                int quizAllVotesSum = quiz.getAllVotesSum();
                quiz.setAllVotesSum(quizAllVotesSum + 1);
            }

            if(noErrors) {
                sqLiteDatabase.setTransactionSuccessful();

                result = true;
            }

        } catch(SQLiteException sqliteExc) {
            Log.e("LOG", "QuizController: saveQuizAnswerVote(): SQLiteException: " +sqliteExc.getMessage());
        } catch(Exception exc) {
            //Error in between database transaction
            Log.e("LOG", "QuizController: saveQuizAnswerVote(): Exception: " +exc.getMessage());
        } finally {
            sqLiteDatabase.endTransaction();
            cursor.close();
        }

        return result;
    }
}
