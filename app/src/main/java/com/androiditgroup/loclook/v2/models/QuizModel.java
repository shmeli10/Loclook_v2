package com.androiditgroup.loclook.v2.models;

import com.androiditgroup.loclook.v2.constants.ErrorConstants;

import java.util.ArrayList;

/**
 * Created by Serghei Ostrovschi on 7/19/16.
 */

public class QuizModel {

    private ArrayList<QuizModel> quizAnswerList = new ArrayList<>();

    // ------------------------------------- GETTERS ----------------------------------------- //

    public ArrayList<QuizModel> getQuizAnswerList() {
        return quizAnswerList;
    }

    // ------------------------------------- SETTERS ----------------------------------------- //

    public void setQuizAnswerList(ArrayList<QuizModel> quizAnswerList) throws Exception {

        if(quizAnswerList == null)
            throw new Exception(ErrorConstants.QUIZ_ANSWER_LIST_NULL_ERROR);

        this.quizAnswerList = quizAnswerList;
    }
}
