package com.androiditgroup.loclook.v2.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import com.androiditgroup.loclook.v2.R;

import java.util.ArrayList;

/**
 * Created by OS1 on 18.02.2017.
 */
public class QuizAnswersListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Boolean> answers;
    private QuizAnswerCallback mQuizAnswerCallback;

    public interface QuizAnswerCallback {
        void onDelete(int quizAnswerId);
    }

    // public QuizAnswersListAdapter(LayoutInflater inflater, ArrayList<Boolean> list) {
    public QuizAnswersListAdapter(LayoutInflater inflater, ArrayList<Boolean> list, QuizAnswerCallback callback) {
        mInflater = inflater;
        answers = list;
        mQuizAnswerCallback = callback;
    }

    @Override
    public int getCount() {
        // кол-во элементов
        return answers.size();
    }

    @Override
    public Boolean getItem(int position) {
        // элемент по позиции
        return answers.get(position);
    }

    @Override
    public long getItemId(int position) {
        // id по позиции
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // пункт списка
        View view = mInflater.inflate(R.layout.quiz_item, parent, false);

//        EditText answerField = (EditText) view.findViewById(R.id.Publication_QuizAnswer);
//        answerField.setText("" +position);

        // скрыть кнопку удаления и сделать не кликабельной
        ImageView deleteAnswer = (ImageView) view.findViewById(R.id.Publication_QuizAnswerDelete);
        deleteAnswer.setTag("" +position);
        deleteAnswer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log.d("ABC", "QuizAnswersListAdapter: getView(): delete quiz answer= " +position);
                mQuizAnswerCallback.onDelete(position);
            }
        });

        if(!answers.get(position)) {
            deleteAnswer.setClickable(false);
            deleteAnswer.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}
