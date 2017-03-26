package com.androiditgroup.loclook.v2.models;

import java.util.ArrayList;

/**
 * Created by OS1 on 25.03.2017.
 */
public class Quiz {

    private boolean mUserSelectedAnswer;

    private ArrayList<QuizAnswer> mAnswersList = new ArrayList<>();

    public ArrayList<QuizAnswer> getAnswersList() { return mAnswersList; }

    public int getSelectedAnswersSum() {
        int result = 0;

        for(int i=0; i<mAnswersList.size(); i++)
            result += mAnswersList.get(i).getSelectedSum();

        return result;
    }

    public boolean userSelectedAnswer() { return mUserSelectedAnswer; }

    public void setUserSelectedAnswer(boolean value) {
        mUserSelectedAnswer = value;
    }

    public static class Builder {
        private boolean mUserSelectedAnswer;
        private ArrayList<QuizAnswer> mAnswersList = new ArrayList<>();

        public Builder userSelectedAnswer(boolean value) {
            mUserSelectedAnswer = value;
            return this;
        }

        public Builder answersList(ArrayList<QuizAnswer> list) {
            mAnswersList.addAll(list);
            return this;
        }

        public Quiz build() {
            Quiz quiz = new Quiz();
            quiz.mUserSelectedAnswer = this.mUserSelectedAnswer;
            quiz.mAnswersList = this.mAnswersList;
            return quiz;
        }
    }
}
