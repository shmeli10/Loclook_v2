package com.androiditgroup.loclook.v2.utils;

import android.database.Cursor;
import android.util.Log;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.models.QuizAnswer;

import java.util.ArrayList;

/**
 * Created by OS1 on 04.03.2017.
 */
public class QuizAnswerGenerator {

    public static QuizAnswer getQuizAnswerFromCursor(Cursor cursor) {

        try {

            cursor.moveToFirst();

            String qaId     = cursor.getString(cursor.getColumnIndex("_ID"));
            String qaText   = cursor.getString(cursor.getColumnIndex("TEXT"));

            //////////////////////////////////////////////////////////////////////////////////////

            QuizAnswer.Builder mQuizAnswerBuilder = new QuizAnswer.Builder();

            if((qaId != null) && (!qaId.equals("")))
                mQuizAnswerBuilder.id(qaId);
            else
                return null;

            if((qaText != null) && (!qaText.equals("")))
                mQuizAnswerBuilder.text(qaText);
            else
                return null;

            // возвращаем вариант ответа опроса
            return mQuizAnswerBuilder.build();

        } catch(Exception exc) {
            LocLookApp.showLog("QuizAnswerGenerator: getQuizAnswerFromCursor(): error: " +exc.toString());
        } finally {
            cursor.close();
        }

        return null;
    }

    public static ArrayList<QuizAnswer> getQuizAnswersList(Cursor cursor) {

        ArrayList<QuizAnswer> quizAnswersList = new ArrayList<>();

        try {

            if(cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    String qaId     = cursor.getString(cursor.getColumnIndex("_ID"));
                    String qaText   = cursor.getString(cursor.getColumnIndex("TEXT"));

                    //////////////////////////////////////////////////////////////////////////////////////

                    QuizAnswer.Builder mQuizAnswerBuilder = new QuizAnswer.Builder();

                    if((qaId != null) && (!qaId.equals("")))
                        mQuizAnswerBuilder.id(qaId);
                    else
                        return null;

                    if((qaText != null) && (!qaText.equals("")))
                        mQuizAnswerBuilder.text(qaText);
                    else
                        return null;

                    // добавляем в список вариант ответа опроса
                    quizAnswersList.add(mQuizAnswerBuilder.build());
                }
                cursor.close();
            }
        } catch(Exception exc) {
            LocLookApp.showLog("QuizAnswerGenerator: getQuizAnswersList(): error: " +exc.toString());
        } finally {
            cursor.close();
        }

        return quizAnswersList;
    }
}