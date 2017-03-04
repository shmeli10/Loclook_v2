package com.androiditgroup.loclook.v2.models;

/**
 * Created by OS1 on 26.02.2017.
 */
public class QuizAnswer {

    private String mId;
    private String mText;

    public String getId() { return mId;}

    public String getText() { return mText;}

    public static class Builder {
        private String mId;
        private String mText;

        public Builder id(String value) {
            mId = value;
            return this;
        }

        public Builder text(String value) {
            mText = value;
            return this;
        }

        public QuizAnswer build() {
            QuizAnswer quizAnswer = new QuizAnswer();
            quizAnswer.mId = this.mId;
            quizAnswer.mText = this.mText;
            return quizAnswer;
        }
    }
}
