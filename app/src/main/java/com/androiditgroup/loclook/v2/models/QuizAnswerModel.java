package com.androiditgroup.loclook.v2.models;

import com.androiditgroup.loclook.v2.constants.ErrorConstants;

/**
 * Created by Serghei Ostrovschi on 7/19/16.
 */

public class QuizAnswerModel {

    private int quizAnswerId;
    private int quizAnswerPublicationId;

    private String quizAnswerText;

    // ------------------------------------- GETTERS ----------------------------------------- //

    public int getQuizAnswerId() {
        return quizAnswerId;
    }

    public int getQuizAnswerPublicationId() {
        return quizAnswerPublicationId;
    }

    public String getQuizAnswerText() {
        return quizAnswerText;
    }

    // ------------------------------------- SETTERS ----------------------------------------- //

    public void setQuizAnswerId(int quizAnswerId) throws Exception {
        if(quizAnswerId > 0)
            this.quizAnswerId = quizAnswerId;
        else
            throw new Exception(ErrorConstants.QUIZ_ANSWER_ID_ERROR);
    }

    public void setQuizAnswerPublicationId(int quizAnswerPublicationId) throws Exception {
        if(quizAnswerPublicationId > 0)
            this.quizAnswerPublicationId = quizAnswerPublicationId;
        else
            throw new Exception(ErrorConstants.QUIZ_ANSWER_PUBLICATION_ID_ERROR);
    }

    public void setQuizAnswerText(String quizAnswerText) throws Exception {
        if(quizAnswerText != null)
            this.quizAnswerText = quizAnswerText;
        else
            throw new Exception(ErrorConstants.QUIZ_ANSWER_TEXT_NULL_ERROR);
    }
}
