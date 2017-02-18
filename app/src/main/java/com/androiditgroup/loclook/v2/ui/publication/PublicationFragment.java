package com.androiditgroup.loclook.v2.ui.publication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.adapters.QuizAnswersListAdapter;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.ExpandableHeightListView;
import com.androiditgroup.loclook.v2.utils.ParentFragment;

import java.util.ArrayList;

/**
 * Created by sostrovschi on 1/25/17.
 */

public class PublicationFragment extends    ParentFragment
                                 implements View.OnClickListener,
                                            TextWatcher,
                                            CompoundButton.OnCheckedChangeListener,
                                            QuizAnswersListAdapter.QuizAnswerCallback {

    private static LayoutInflater mInflater;

    private EditText mPublicationText;
    private TextView mLeftCharactersBody;
    private TextView mAnonymousStateTV;
    private TextView mQuizStateTV;
    private TextView mAddAnswerTV;

    private Switch mAnonymousSwitch;
    private Switch mQuizSwitch;

    private RelativeLayout mQuizAnswersBlock;

    private ArrayList<Boolean> answersList = new ArrayList<>();
    private QuizAnswersListAdapter mAdapter;
    private ExpandableHeightListView mQuizAnswersList;

    private RelativeLayout mBadgeBlock;

    final int anonymousSwitchResId  = R.id.Publication_AnonymousSwitchBTN;
    final int quizSwitchResId       = R.id.Publication_QuizSwitchBTN;
    final int badgeBlockResId       = R.id.Publication_BadgeBlock;
    final int addAnswerResId        = R.id.Publication_QuizAnswersBlockAddAnswer;

    private int mPublicationTextLimit;

    public PublicationFragment() {
        // Required empty public constructor
    }

    public static PublicationFragment newInstance(LayoutInflater inflater) {
        mInflater = inflater;
        Bundle args = new Bundle();
        PublicationFragment fragment = new PublicationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publication, container, false);

        mPublicationTextLimit = getActivity().getResources().getInteger(R.integer.publication_length);

        mPublicationText = (EditText) view.findViewById(R.id.Publication_PublicationTextET);
        mPublicationText.addTextChangedListener(this);

        mAnonymousStateTV = (TextView) view.findViewById(R.id.Publication_AnonymousStateTV);
        mQuizStateTV = (TextView) view.findViewById(R.id.Publication_QuizStateTV);

        mLeftCharactersBody = (TextView) view.findViewById(R.id.Publication_LeftCharactersBodyTV);
        mLeftCharactersBody.setText("" +mPublicationTextLimit);

        mAnonymousSwitch = (Switch) view.findViewById(anonymousSwitchResId);
        mAnonymousSwitch.setOnCheckedChangeListener(this);

        mQuizSwitch = (Switch) view.findViewById(R.id.Publication_QuizSwitchBTN);
        mQuizSwitch.setOnCheckedChangeListener(this);

        mQuizAnswersBlock = (RelativeLayout) view.findViewById(R.id.Publication_QuizAnswersBlock);

        mAddAnswerTV = (TextView) view.findViewById(addAnswerResId);
        mAddAnswerTV.setOnClickListener(this);

        answersList.add(Boolean.FALSE);
        answersList.add(Boolean.FALSE);
//        answersList.add(Boolean.TRUE);
//        answersList.add(Boolean.TRUE);
//        answersList.add(Boolean.TRUE);
//        answersList.add(Boolean.TRUE);
//        answersList.add(Boolean.TRUE);
//        answersList.add(Boolean.TRUE);
//        answersList.add(Boolean.TRUE);
//        answersList.add(Boolean.TRUE);
        // mAdapter = new QuizAnswersListAdapter(mInflater, answersList);
        mAdapter = new QuizAnswersListAdapter(mInflater, answersList, this);
        mQuizAnswersList = (ExpandableHeightListView) view.findViewById(R.id.Publication_QuizAnswersList);
        mQuizAnswersList.setExpanded(true);
        mQuizAnswersList.setAdapter(mAdapter);

        mBadgeBlock = (RelativeLayout) view.findViewById(badgeBlockResId);
        mBadgeBlock.setOnClickListener(this);

        return view;
    }

    @Override
    public String getFragmentTitle() {
        return LocLookApp.getInstance().getString(R.string.publication_text);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // вычисляем и отображаем кол-во символов, которые еще можно внести в поле
        mLeftCharactersBody.setText("" + (mPublicationTextLimit - mPublicationText.length()));
    }

    @Override
    public void afterTextChanged(Editable editable) { }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        switch (compoundButton.getId()) {

            // щелчок по "переключателю вкл./выкл. анонимность"
            case anonymousSwitchResId:
                mAnonymousStateTV.setText(checked ? R.string.state_on_text: R.string.state_off_text);
                break;
            // щелчок по "переключателю вкл./выкл. опрос"
            case quizSwitchResId:

                if(checked){
                    mQuizStateTV.setText(R.string.state_on_text);
                    mQuizAnswersBlock.setVisibility(View.VISIBLE);
                }
                else {
                    mQuizStateTV.setText(R.string.state_off_text);
                    mQuizAnswersBlock.setVisibility(View.GONE);
                }

                break;
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            // щелчок по "контейнеру выбора бейджика"
            case badgeBlockResId:
                // выводим на жкран "диалоговое окно для выбора бейджика"
                // showBadgesDialog();
                break;
            // щелчок по "кнопке Добавить вариант ответа"
            case addAnswerResId:

                if(answersList.size() < Constants.QUIZ_MAX_ANSWERS) {
                    // добавляем поле с возможностью удаления
                    answersList.add(Boolean.TRUE);

                    if (answersList.size() == Constants.QUIZ_MAX_ANSWERS) {
                        mAddAnswerTV.setBackgroundResource(R.color.light_grey);
                        mAddAnswerTV.setClickable(false);
                    }

                    mAdapter.notifyDataSetChanged();
                }

                break;
        }
    }

    @Override
    public void onDelete(int quizAnswerId) {
        // Log.d("ABC", "PublicationFragment: onDelete(): delete quiz answer= " +quizAnswerId);
        // удаляем поле с вариантом ответа
        answersList.remove(quizAnswerId);
        mAdapter.notifyDataSetChanged();
    }
}
