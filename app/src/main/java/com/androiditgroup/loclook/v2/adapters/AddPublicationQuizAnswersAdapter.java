package com.androiditgroup.loclook.v2.adapters;

import android.text.Editable;
import android.text.TextWatcher;
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
public class AddPublicationQuizAnswersAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<String> answers;
    private QuizAnswerCallback mQuizAnswerCallback;

    public interface QuizAnswerCallback {
        void onDelete(int quizAnswerId);
    }

    public AddPublicationQuizAnswersAdapter(LayoutInflater inflater, ArrayList<String> list, QuizAnswerCallback callback) {
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
    // public Boolean getItem(int position) {
    public String getItem(int position) {
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
        View view = mInflater.inflate(R.layout.add_publication_quiz_item, parent, false);

        EditText answerField = (EditText) view.findViewById(R.id.Publication_QuizAnswer);
        answerField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                answers.set(position, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        String answerText = answers.get(position);

        if(answerText != null)
            answerField.setText("" +answerText);

        // скрыть кнопку удаления и сделать не кликабельной
        ImageView deleteAnswer = (ImageView) view.findViewById(R.id.Publication_QuizAnswerDelete);
        deleteAnswer.setTag("" +position);
        deleteAnswer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQuizAnswerCallback.onDelete(position);
            }
        });

        if(position < 2) {
            deleteAnswer.setClickable(false);
            deleteAnswer.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}
