package com.androiditgroup.loclook.v2.models;

import com.androiditgroup.loclook.v2.constants.ErrorConstants;

import java.util.ArrayList;

/**
 * Created by Serghei Ostrovschi on 7/19/16.
 */

public class QuizModel {

    private int allVotesSum = 0;

    private ArrayList<QuizAnswerModel> quizAnswerList = new ArrayList<>();

    // ------------------------------------- GETTERS ----------------------------------------- //

    public ArrayList<QuizAnswerModel> getQuizAnswerList() {
        return quizAnswerList;
    }

    public int getAllVotesSum() {
        return allVotesSum;
    }

    // ------------------------------------- SETTERS ----------------------------------------- //

    public void setQuizAnswerList(ArrayList<QuizAnswerModel> quizAnswerList) throws Exception {

        if(quizAnswerList == null)
            throw new Exception(ErrorConstants.QUIZ_ANSWER_LIST_NULL_ERROR);

        this.quizAnswerList = quizAnswerList;
    }

    public void setAllVotesSum(int allVotesSum) {

        if(allVotesSum >= 0)
            this.allVotesSum = allVotesSum;
    }
}
