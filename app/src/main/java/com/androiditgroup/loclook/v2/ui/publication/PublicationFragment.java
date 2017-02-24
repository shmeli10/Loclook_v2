package com.androiditgroup.loclook.v2.ui.publication;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.adapters.BadgesAdapter;
import com.androiditgroup.loclook.v2.adapters.QuizAnswersAdapter;
import com.androiditgroup.loclook.v2.models.Badge;
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
                                            QuizAnswersAdapter.QuizAnswerCallback {

    private static LayoutInflater mInflater;

    private ScrollView mScroll;
    private EditText mPublicationText;
    private TextView mLeftCharactersBody;
    private TextView mAnonymousStateTV;
    private TextView mQuizStateTV;
    private TextView mAddAnswerTV;
    private TextView mSelectedBadgeTV;

    private Switch mAnonymousSwitch;
    private Switch mQuizSwitch;

    private RelativeLayout mQuizAnswersBlock;

    private ArrayList<String> answersList = new ArrayList<>();
    private QuizAnswersAdapter mAdapter;
    private ExpandableHeightListView mQuizAnswersList;

    private RelativeLayout mBadgeBlock;

    private ImageView mShowBadgesBlock;
    private ImageView mSelectedBadgeIV;

    private RelativeLayout mChooseBadgeBlock;
    private GridView mChooseBadgeBlockGV;
    private BadgesAdapter mBadgesAdapter;

    final int anonymousSwitchResId  = R.id.Publication_AnonymousSwitchBTN;
    final int quizSwitchResId       = R.id.Publication_QuizSwitchBTN;
    final int badgeBlockResId       = R.id.Publication_BadgeBlock;
    final int addAnswerResId        = R.id.Publication_QuizAnswersBlockAddAnswer;

    private int mPublicationTextLimit;

    private boolean isAvailableToDelete;
    private boolean isBadgesBlockHidden;

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

        mScroll = (ScrollView) view.findViewById(R.id.Publication_ScrollView);

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

        for(int i=0; i<Constants.QUIZ_MIN_ANSWERS; i++)
            answersList.add(null);

        mAdapter = new QuizAnswersAdapter(mInflater, answersList, this);
        mQuizAnswersList = (ExpandableHeightListView) view.findViewById(R.id.Publication_QuizAnswersList);
        mQuizAnswersList.setExpanded(true);
        mQuizAnswersList.setAdapter(mAdapter);

        isAvailableToDelete = true;
        isBadgesBlockHidden = true;

        mSelectedBadgeIV = (ImageView) view.findViewById(R.id.Publication_BadgeImageIV);
        mShowBadgesBlock = (ImageView) view.findViewById(R.id.Publication_ShowBadges);

        mSelectedBadgeTV = (TextView) view.findViewById(R.id.Publication_BadgeNameTV);

        mChooseBadgeBlock = (RelativeLayout) view.findViewById(R.id.Publication_ChooseBadgeBlock);
        mChooseBadgeBlockGV = (GridView) view.findViewById(R.id.Publication_ChooseBadgeBlockGV);

        mBadgesAdapter = new BadgesAdapter(mInflater);
        mChooseBadgeBlockGV.setAdapter(mBadgesAdapter);
        mChooseBadgeBlockGV.setOnItemClickListener(onBadgeClickListener);

        mBadgeBlock = (RelativeLayout) view.findViewById(badgeBlockResId);
        // mBadgeBlock.setOnClickListener(this);
        mBadgeBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isBadgesBlockHidden) {
                    mShowBadgesBlock.setImageResource(R.drawable.hide_data);
                    mChooseBadgeBlock.setVisibility(View.VISIBLE);
                    isBadgesBlockHidden = false;
                }
                else {
                    mShowBadgesBlock.setImageResource(R.drawable.show_data);
                    mChooseBadgeBlock.setVisibility(View.GONE);
                    isBadgesBlockHidden = true;
                }
            }
        });

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
            // щелчок по "кнопке Добавить вариант ответа"
            case addAnswerResId:
                if(answersList.size() < Constants.QUIZ_MAX_ANSWERS) {
                    // добавляем поле с возможностью удаления
                    answersList.add(null);

                    if (answersList.size() == Constants.QUIZ_MAX_ANSWERS) {
                        mAddAnswerTV.setBackgroundResource(R.color.light_grey);
                        mAddAnswerTV.setClickable(false);
                    }
                    mAdapter.notifyDataSetChanged();
                    mScroll.post(new Runnable() {
                        @Override
                        public void run() {
                            mScroll.smoothScrollTo(0, mScroll.getHeight());
                        }
                    });
                }

                break;
        }
    }

    @Override
    public void onDelete(int quizAnswerId) {
        if(isAvailableToDelete) {
            // удаляем поле с вариантом ответа
            answersList.remove(quizAnswerId);

            if (answersList.size() < Constants.QUIZ_MAX_ANSWERS) {
                mAddAnswerTV.setBackgroundResource(R.color.colorPrimaryDark);
                mAddAnswerTV.setClickable(true);
            }
            mAdapter.notifyDataSetChanged();

            isAvailableToDelete = false;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isAvailableToDelete = true;
                }
            }, 1000);
        }
    }

    private GridView.OnItemClickListener onBadgeClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Badge selectedBadge = LocLookApp.badgesList.get(position);

            if(selectedBadge != null) {
                mSelectedBadgeIV.setImageResource(selectedBadge.getIconResId());
                mSelectedBadgeTV.setText(selectedBadge.getName());
            }
        }
    };
}
