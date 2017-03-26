package com.androiditgroup.loclook.v2.utils;

import android.database.Cursor;
import android.text.TextUtils;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.models.Quiz;
import com.androiditgroup.loclook.v2.models.QuizAnswer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by OS1 on 26.03.2017.
 */
public class QuizUtility {

    public static boolean saveUserAnswer(String answerId) {
        Cursor cursor = DBManager.getInstance().saveSelectedQuizAnswer(answerId);

        if((cursor != null) && (cursor.getCount() > 0)) {

            try {
                cursor.moveToFirst();

                String userAnswerRowId = cursor.getString(cursor.getColumnIndex("_ID"));

                if(!TextUtils.isEmpty(userAnswerRowId)) {
                    // LocLookApp.showLog("QuizUtility: saveUserAnswer(): Saved user answer id= " + userAnswerRowId);
                    return true;
                }
                else {
                    // LocLookApp.showLog("QuizUtility: saveUserAnswer(): Saved user answer error(0)");
                }
            } catch(Exception exc) {
                LocLookApp.showLog("QuizUtility: saveUserAnswer(): Saved user answer error: " +exc.toString());
            } finally {
                cursor.close();
            }
        }
//        else {
//            LocLookApp.showLog("QuizUtility: saveUserAnswer(): Saved user answer error(1)");
//        }

        return false;
    }

    public static void setQuizAnswersSum(Quiz quiz) {

        ArrayList<QuizAnswer> quizAnswersList = quiz.getAnswersList();

        Map<String, QuizAnswer> quizAnswersMap = new HashMap<>();

        String[] quizAnswersIdsArr = new String[quizAnswersList.size()];

        for(int i=0; i<quizAnswersList.size(); i++) {
            QuizAnswer quizAnswer = quizAnswersList.get(i);

            quizAnswersIdsArr[i] = quizAnswer.getId();
            quizAnswersMap.put(quizAnswer.getId(), quizAnswer);
            LocLookApp.showLog("QuizUtility: setQuizAnswersSum(): id= " +quizAnswersIdsArr[i]);
        }

        Cursor cursor = DBManager.getInstance().getQuizAnswersSum(DBManager.getInstance().getDataBase(), quizAnswersIdsArr);
        // Cursor cursor = DBManager.getInstance().rawQuery(DBManager.getInstance().getDataBase(), query, new String[]{});

        try {

            if(cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // получаем очередной идентификатор ответа
                    String quizAnswerId             = cursor.getString(cursor.getColumnIndex("QUIZ_ANSWER_ID"));
                    String quizAnswerSelectedSum    = cursor.getString(cursor.getColumnIndex("QUIZ_SUM"));

                    // задаем кол-во выбора пользователями заданного ответа
                    quizAnswersMap.get(quizAnswerId).setSelectedSum(Integer.valueOf(quizAnswerSelectedSum));

                    // LocLookApp.showLog("QUIZ_ANSWER_ID= " +quizAnswerId+ ", sum= " +quizAnswerSelectedSum);
                }
            }
        } catch(Exception exc) {
            LocLookApp.showLog("QuizUtility: setQuizAnswersSum(): error: " +exc.toString());
        } finally {
            cursor.close();
        }
    }

    public static void setUserSelectedQuizAnswer(Quiz quiz, boolean needRequestDB) {

        String[] columns = new String[] {"QUIZ_ANSWER_ID"};

        // если нужно получить результат из БД
        if(needRequestDB) {

            Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(), Constants.DataBase.USER_QUIZ_ANSWER_TABLE, columns, "USER_ID", LocLookApp.appUserId);

            try {

                if(cursor.getCount() > 0) {
                    // будем накапливать идентификаторы ответов выбранных пользователем в опросах
                    ArrayList<String> userSelectedAnswersIdsList = new ArrayList<>();

                    while (cursor.moveToNext()) {

                        // получаем очередной идентификатор ответа
                        String quizAnswerId  = cursor.getString(cursor.getColumnIndex("QUIZ_ANSWER_ID"));

                        // если значение получено
                        if(!TextUtils.isEmpty(quizAnswerId))
                            // добавляем его в список
                            userSelectedAnswersIdsList.add(quizAnswerId);
                    }

                    // если в списке есть элементы
                    if(!userSelectedAnswersIdsList.isEmpty()) {
                        ArrayList<QuizAnswer> quizAnswersList = quiz.getAnswersList();

                        boolean result = false;

                        for(int i=0; i<quizAnswersList.size(); i++) {
                            if(userSelectedAnswersIdsList.contains(quizAnswersList.get(i).getId())) {
                                result = true;
                                break;
                            }
                        }

                        quiz.setUserSelectedAnswer(result);
                    }
                    // если список пустой
                    else {
                        quiz.setUserSelectedAnswer(false);
                    }
                }
            } catch(Exception exc) {
                LocLookApp.showLog("QuizUtility: setUserSelectedQuizAnswer(): error: " +exc.toString());
            } finally {
                cursor.close();
            }

        }
        // если результат ясен
        else {
            quiz.setUserSelectedAnswer(true);
        }

    }
}
