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
import com.androiditgroup.loclook.v2.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by OS1 on 18.02.2017.
 */
public class ShowPublicationQuizAnswersAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Quiz mQuiz;
    private ArrayList<QuizAnswer> quizAnswersList;

    public ShowPublicationQuizAnswersAdapter(LayoutInflater inflater, Quiz quiz, ArrayList<QuizAnswer> list) {
        mInflater = inflater;
        mQuiz = quiz;
        quizAnswersList = list;
    }

    @Override
    public int getCount() {
        // кол-во элементов
        return quizAnswersList.size();
    }

    @Override
    public QuizAnswer getItem(int position) {
        // элемент по позиции
        return quizAnswersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // id по позиции
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // пункт списка
        View view = mInflater.inflate(R.layout.show_publication_quiz_item, parent, false);

        final QuizAnswer answer = quizAnswersList.get(position);

        TextView mQuizAnswerText = UiUtils.findView(view, R.id.ShowPublication_QI_AnswerText);
        mQuizAnswerText.setText(answer.getText());

        // если пользователь уже выбрал ответ в опросе
        if(mQuiz.userSelectedAnswer()) {
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
        }

        return view;
    }
}
