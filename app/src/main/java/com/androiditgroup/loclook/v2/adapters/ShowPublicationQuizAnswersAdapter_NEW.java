package com.androiditgroup.loclook.v2.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.data.QuizController;
import com.androiditgroup.loclook.v2.models.QuizAnswerModel;
import com.androiditgroup.loclook.v2.models.QuizModel;
import com.androiditgroup.loclook.v2.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by OS1 on 18.02.2016.
 */
public class ShowPublicationQuizAnswersAdapter_NEW extends BaseAdapter {

    private QuizModel quiz;

    private LayoutInflater inflater;

    private QuizController quizController;

    private ArrayList<QuizAnswerModel> quizAnswerList;

    int currentUserId = 0;

    public ShowPublicationQuizAnswersAdapter_NEW(LayoutInflater             inflater,
                                                 QuizModel                  quiz,
                                                 QuizController             quizController,
                                                 int                        currentUserId,
                                                 ArrayList<QuizAnswerModel> quizAnswerList) throws Exception {

        if(inflater == null)
            throw new Exception(ErrorConstants.LAYOUT_INFLATER_NULL_ERROR);

        if(quiz == null)
            throw new Exception(ErrorConstants.QUIZ_NULL_ERROR);

        if(quizController == null)
            throw new Exception(ErrorConstants.QUIZ_CONTROLLER_NULL_ERROR);

        if(currentUserId <= 0)
            throw new Exception(ErrorConstants.USER_ID_ERROR);

        if(quizAnswerList == null)
            throw new Exception(ErrorConstants.QUIZ_ANSWER_LIST_NULL_ERROR);

        this.inflater       = inflater;
        this.quiz           = quiz;
        this.quizController = quizController;
        this.currentUserId  = currentUserId;
        this.quizAnswerList = quizAnswerList;
    }

    @Override
    public int getCount() {
        return quizAnswerList.size();
    }

    @Override
    public QuizAnswerModel getItem(int position) {
        return quizAnswerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int   position,
                        View        convertView,
                        ViewGroup   parent) {
        // list item
        View view = inflater.inflate(R.layout.show_publication_quiz_item,
                                     parent,
                                     false);

        final QuizAnswerModel quizAnswer = quizAnswerList.get(position);

        TextView mQuizAnswerText = UiUtils.findView(view,
                                                    R.id.ShowPublication_QI_AnswerText);

        mQuizAnswerText.setText(quizAnswer.getQuizAnswerText());

        try {
            // если пользователь уже выбрал ответ в опросе
            if(quizController.isQuizAnsweredByUser( quiz,
                                                    currentUserId)) {
                // больше голосовать нельзя
                FrameLayout answerBlock = UiUtils.findView( view,
                                                            R.id.ShowPublication_QI_AnswerBlock);
                answerBlock.setClickable(false);

                // отображаем прогресс голосования за данные ответ
                ProgressBar mProgress = UiUtils.findView(   view,
                                                            R.id.ShowPublication_QI_Progress);
                mProgress.setVisibility(View.VISIBLE);
                mProgress.setProgress(quizController.getQuizAnswerVotesInPercent(quiz,
                                                                                 quizAnswer));

                // отображаем кол-во пользователей выбравших данный ответ
                TextView mQuizAnswersSum = UiUtils.findView(view,
                                                            R.id.ShowPublication_QI_AnswersSum);
                mQuizAnswersSum.setVisibility(View.VISIBLE);
                mQuizAnswersSum.setText("" +quizController.getQuizAnswerVotesSum(quizAnswer));
            }
        } catch (Exception exc) {
            Log.e("LOG", "ShowPublicationQuizAnswersAdapter_NEW: getView(): isQuizAnsweredByUser error: " +exc.getMessage());
        }

        return view;
    }
}
