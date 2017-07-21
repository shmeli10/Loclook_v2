package com.androiditgroup.loclook.v2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.models.Quiz;
import com.androiditgroup.loclook.v2.models.QuizAnswer;
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

    private ArrayList<QuizAnswerModel> quizAnswerList;

    public ShowPublicationQuizAnswersAdapter_NEW(LayoutInflater             inflater,
                                                 QuizModel                  quiz,
                                                 ArrayList<QuizAnswerModel> quizAnswerList) {
        this.inflater       = inflater;
        this.quiz           = quiz;
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

        // если пользователь уже выбрал ответ в опросе
        /*if(quiz.userSelectedAnswer()) {
            // больше голосвать нельзя
            FrameLayout answerBlock = UiUtils.findView(view, R.id.ShowPublication_QI_AnswerBlock);
            answerBlock.setClickable(false);

            // отображаем прогресс голосования за данные ответ
            ProgressBar mProgress = UiUtils.findView(view, R.id.ShowPublication_QI_Progress);
            mProgress.setVisibility(View.VISIBLE);
            mProgress.setProgress(answer.getVotesInPercents());

            // отображаем кол-во пользователей выбравших данный ответ
            TextView mQuizAnswersSum = UiUtils.findView(view, R.id.ShowPublication_QI_AnswersSum);
            mQuizAnswersSum.setVisibility(View.VISIBLE);
            mQuizAnswersSum.setText("" +answer.getVotesSum());
        }*/

        return view;
    }
}
