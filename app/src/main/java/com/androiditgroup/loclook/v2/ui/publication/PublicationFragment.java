package com.androiditgroup.loclook.v2.ui.publication;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.androiditgroup.loclook.v2.models.Publication;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;
import com.androiditgroup.loclook.v2.utils.ExpandableHeightGridView;
import com.androiditgroup.loclook.v2.utils.ExpandableHeightListView;
import com.androiditgroup.loclook.v2.utils.ParentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sostrovschi on 1/25/17.
 */

public class PublicationFragment extends    ParentFragment
                                 implements View.OnClickListener,
                                            TextWatcher,
                                            CompoundButton.OnCheckedChangeListener,
                                            QuizAnswersAdapter.QuizAnswerCallback {

    private static LayoutInflater mInflater;

    private RelativeLayout mPublicationContainer;
    private RelativeLayout mBadgeBlock;
    private RelativeLayout mQuizAnswersBlock;

    private ScrollView mScroll;
    private EditText mPublicationText;
    private TextView mLeftCharactersBody;
    private TextView mAnonymousStateTV;
    private TextView mQuizStateTV;
    private TextView mAddAnswerTV;
    private TextView mSelectedBadgeTV;

    private Switch mAnonymousSwitch;
    private Switch mQuizSwitch;

    private ArrayList<String> mAnswersList = new ArrayList<>();

    private ImageView mShowBadgesBlock;
    private ImageView mSelectedBadgeIV;
    private ExpandableHeightListView mQuizAnswersList;
    private ExpandableHeightGridView mChooseBadgeBlockGV;

    private QuizAnswersAdapter mQuizAnswersAdapter;
    private BadgesAdapter mBadgesAdapter;

    private final int anonymousSwitchResId  = R.id.Publication_AnonymousSwitchBTN;
    private final int quizSwitchResId       = R.id.Publication_QuizSwitchBTN;
    private final int badgeBlockResId       = R.id.Publication_BadgeBlock;
    private final int addAnswerResId        = R.id.Publication_QuizAnswersBlockAddAnswer;

    private int mPublicationTextLimit;

    private boolean isAvailableToDelete;
    private boolean isBadgesBlockHidden;

    private Badge selectedBadge;

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

        mPublicationContainer   = (RelativeLayout) view.findViewById(R.id.Publication_Container);
        mQuizAnswersBlock       = (RelativeLayout) view.findViewById(R.id.Publication_QuizAnswersBlock);
        mBadgeBlock             = (RelativeLayout) view.findViewById(badgeBlockResId);

        mScroll = (ScrollView) view.findViewById(R.id.Publication_ScrollView);

        mPublicationTextLimit = getActivity().getResources().getInteger(R.integer.publication_length);
        selectedBadge = LocLookApp.badgesList.get(0);

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

        mAddAnswerTV = (TextView) view.findViewById(addAnswerResId);
        mAddAnswerTV.setOnClickListener(this);

        for(int i=0; i<Constants.QUIZ_MIN_ANSWERS; i++)
            mAnswersList.add(null);

        mQuizAnswersAdapter = new QuizAnswersAdapter(mInflater, mAnswersList, this);
        mQuizAnswersList = (ExpandableHeightListView) view.findViewById(R.id.Publication_QuizAnswersList);
        mQuizAnswersList.setExpanded(true);
        mQuizAnswersList.setAdapter(mQuizAnswersAdapter);

        isAvailableToDelete = true;
        isBadgesBlockHidden = true;

        mSelectedBadgeIV    = (ImageView) view.findViewById(R.id.Publication_BadgeImageIV);
        mShowBadgesBlock    = (ImageView) view.findViewById(R.id.Publication_ShowBadges);
        mSelectedBadgeTV    = (TextView) view.findViewById(R.id.Publication_BadgeNameTV);
        mChooseBadgeBlockGV = (ExpandableHeightGridView) view.findViewById(R.id.Publication_ChooseBadgeBlockGV);

        mBadgesAdapter = new BadgesAdapter(mInflater);
        mChooseBadgeBlockGV.setAdapter(mBadgesAdapter);
        mChooseBadgeBlockGV.setOnItemClickListener(onBadgeClickListener);

        mBadgeBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isBadgesBlockHidden) {
                    mShowBadgesBlock.setImageResource(R.drawable.hide_data);
                    mChooseBadgeBlockGV.setVisibility(View.VISIBLE);
                    isBadgesBlockHidden = false;
                    autoScrollDown();
                }
                else {
                    mShowBadgesBlock.setImageResource(R.drawable.show_data);
                    mChooseBadgeBlockGV.setVisibility(View.GONE);
                    isBadgesBlockHidden = true;
                }
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public String getFragmentTitle() {
        return LocLookApp.getInstance().getString(R.string.publication_text);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.publication_menu, menu);
        menu.getItem(0).setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // добавить фото
            case R.id.action_add_photo:

                return true;
            // отправить публикацию
            case R.id.action_send:
                sendPublication();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    autoScrollDown();
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
                if(mAnswersList.size() < Constants.QUIZ_MAX_ANSWERS) {
                    // добавляем поле с возможностью удаления
                    mAnswersList.add(null);

                    if (mAnswersList.size() == Constants.QUIZ_MAX_ANSWERS) {
                        mAddAnswerTV.setBackgroundResource(R.color.light_grey);
                        mAddAnswerTV.setClickable(false);
                    }
                    mQuizAnswersAdapter.notifyDataSetChanged();
                    autoScrollDown();
                }

                break;
        }
    }

    @Override
    public void onDelete(int quizAnswerId) {
        if(isAvailableToDelete) {
            // удаляем поле с вариантом ответа
            mAnswersList.remove(quizAnswerId);

            if (mAnswersList.size() < Constants.QUIZ_MAX_ANSWERS) {
                mAddAnswerTV.setBackgroundResource(R.color.colorPrimaryDark);
                mAddAnswerTV.setClickable(true);
            }
            mQuizAnswersAdapter.notifyDataSetChanged();

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
            // Badge selectedBadge = LocLookApp.badgesList.get(position);
            selectedBadge = LocLookApp.badgesList.get(position);

            if(selectedBadge != null) {
                mSelectedBadgeIV.setImageResource(selectedBadge.getIconResId());
                mSelectedBadgeTV.setText(selectedBadge.getName());
            }
        }
    };

    private void autoScrollDown() {
        mScroll.post(new Runnable() {
            @Override
            public void run() {
                mScroll.smoothScrollTo(0, mScroll.getHeight());
            }
        });
    }

    private void sendPublication() {
        Log.e("ABC", "PublicationFragment: sendPublication()");

        // если публикация без текста
        if (TextUtils.isEmpty(mPublicationText.getText())) {
            LocLookApp.showSimpleSnakeBar(mPublicationContainer, "empty_publication_text");
            return;
        }

        ArrayList<String> realQuizAnswersList = new ArrayList<>();

        // если надо включить опрос в публикацию
        if(mQuizSwitch.isChecked()) {
            // List<Integer> notEmptyQuizAnswerIdsList = new ArrayList<>();
            for(int i=0; i<mAnswersList.size(); i++) {
//                if((mAnswersList.get(i) == null) || (mAnswersList.get(i).equals("")))
//                    mAnswersList.remove(i);
                if(!TextUtils.isEmpty(mAnswersList.get(i)))
                    // notEmptyQuizAnswerIdsList.add(i);
                    realQuizAnswersList.add(mAnswersList.get(i));
            }

            // if(notEmptyQuizAnswerIdsList.size() >= Constants.QUIZ_MIN_ANSWERS) {
            if(realQuizAnswersList.size() < Constants.QUIZ_MIN_ANSWERS) {
                LocLookApp.showSimpleSnakeBar(mPublicationContainer, "need_more_answers_text");
                return;
            }
        }

        ArrayList<Bitmap> imagesList = new ArrayList<>();

        Publication publication = DBManager.getInstance().createPublication(mPublicationText.getText().toString(), selectedBadge, realQuizAnswersList, imagesList, mAnonymousSwitch.isSelected());

        if(publication != null) {
            Log.e("ABC", "PublicationFragment: sendPublication(): publication is saved");
        }
        else {
            LocLookApp.showSimpleSnakeBar(mPublicationContainer, "publication_send_error_text");
        }
//        // если запись публикации в БД совершена
//        if(sendPublication()) {
//
//            // отключаем кликабельность "кнопки отправки публикации"
//            sendPublicationLL.setClickable(false);
//
//            // переходим на Ленту
//            Intent intent = new Intent(Publication_Activity.this, Tape_Activity.class);
//            startActivity(intent);
//        }
    }
}
