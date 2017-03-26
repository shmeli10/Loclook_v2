package com.androiditgroup.loclook.v2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.models.Quiz;
import com.androiditgroup.loclook.v2.models.QuizAnswer;
import com.androiditgroup.loclook.v2.utils.QuizUtility;
import com.androiditgroup.loclook.v2.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by OS1 on 18.02.2017.
 */
public class ShowPublicationQuizAnswersAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Quiz mQuiz;
    private ArrayList<QuizAnswer> quizAnswersList;
    // private QuizAnswerCallback mQuizAnswerCallback;

//    public interface QuizAnswerCallback {
//        void onDelete(int quizAnswerId);
//    }

    public ShowPublicationQuizAnswersAdapter(LayoutInflater inflater, Quiz quiz, ArrayList<QuizAnswer> list) {
    // public ShowPublicationQuizAnswersAdapter(LayoutInflater inflater, ArrayList<Boolean> list, QuizAnswerCallback callback) {
        mInflater = inflater;
        mQuiz = quiz;
        quizAnswersList = list;
//        mQuizAnswerCallback = callback;
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

        // если пользователь уже выбрал ответ в опросе
        if(mQuiz.userSelectedAnswer()) {
            ProgressBar mProgress = UiUtils.findView(view, R.id.ShowPublication_QI_Progress);
            mProgress.setVisibility(View.VISIBLE);
            mProgress.setProgress(50);
        }
        // если пользователь еще не отвечал в опросе
        else {
            UiUtils.findView(view, R.id.ShowPublication_QI_AnswerBlock).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocLookApp.showLog("ShowPublicationQuizAnswersAdapter: getView(): click on answer(" +answer.getId()+ "): " +answer.getText());

                    boolean answerSaved = QuizUtility.saveUserAnswer(answer.getId());

                    if(answerSaved) {
                        LocLookApp.showLog("ShowPublicationQuizAnswersAdapter: user answer saved successfully");

                        QuizUtility.setUserSelectedQuizAnswer(mQuiz, false);
                    }
                    else
                        LocLookApp.showLog("ShowPublicationQuizAnswersAdapter: user answer save error");
                }
            });
        }

        TextView mQuizAnswerText = UiUtils.findView(view, R.id.ShowPublication_QI_AnswerText);
        mQuizAnswerText.setText(quizAnswersList.get(position).getText());

        TextView mQuizAnswersSum = UiUtils.findView(view, R.id.ShowPublication_QI_AnswersSum);
        mQuizAnswersSum.setText("" +quizAnswersList.get(position).getSelectedSum());

        return view;
    }
}
