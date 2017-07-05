package com.androiditgroup.loclook.v2.ui.comments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.controllers.CommentController;
import com.androiditgroup.loclook.v2.models.Badge;
import com.androiditgroup.loclook.v2.models.Publication;
import com.androiditgroup.loclook.v2.models.User;
import com.androiditgroup.loclook.v2.utils.FavoritesUtility;
import com.androiditgroup.loclook.v2.utils.LikesUtility;
import com.androiditgroup.loclook.v2.utils.ParentFragment;
import com.androiditgroup.loclook.v2.utils.UiUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by sostrovschi on 1/25/17.
 */

public class CommentsFragment   extends ParentFragment
                                implements View.OnClickListener {

    private static Publication mPublication;

    private User user;
    private ArrayList<String> userFavoritesList;

    private FrameLayout sendCommentLayout;

    private TextView    commentsSumValueTextView;

    public CommentsFragment() {
        // Required empty public constructor

        CommentController.getCommentsControllerInstance().populateAllCommentsList();
    }

    // public static CommentsFragment newInstance(LayoutInflater inflater) {
    //        mInflater = inflater;
    public static CommentsFragment newInstance(Publication publication) {
        mPublication = publication;
        Bundle args = new Bundle();
        CommentsFragment fragment = new CommentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mMainActivity = (MainActivity) getActivity();

        user = LocLookApp.usersMap.get(LocLookApp.appUserId);
        userFavoritesList = user.getFavoritesList();

        View view = inflater.inflate(R.layout.fragment_comments, container, false);

        commentsSumValueTextView = UiUtils.findView(view, R.id.Comments_SumValue_TextView);
        commentsSumValueTextView.setText("" + CommentController.getCommentsControllerInstance().getPublicationCommentsSum(mPublication.getPublicationId()));

        LinearLayout footerBlock = UiUtils.findView(view, R.id.Publication_LI_FooterBlock);
        footerBlock.setWeightSum(3f);

        View footerView = inflater.inflate(R.layout.publication_footer, footerBlock, true);

        setHasOptionsMenu(true);

        setPublicationData(view);

        // find "send comment layout"
        sendCommentLayout = (FrameLayout) view.findViewById(R.id.Comments_Send_Wrap);
        sendCommentLayout.setOnClickListener(this);

        return view;
    }

    @Override
    public String getFragmentTitle() {
        return LocLookApp.getInstance().getString(R.string.сomments_text);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.getItem(0).setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void setPublicationData(View rootView) {

        if(mPublication != null) {
            final int textColorActive = LocLookApp.getColorResId("colorPrimary");
            final int textColorSimple = LocLookApp.getColorResId("dark_grey");

            // TextView mUserNameTV = UiUtils.findView(mRootView, R.id.Publication_LI_UserNameTV);
            TextView mUserNameTV = UiUtils.findView(rootView, R.id.Publication_LI_UserNameTV);

            // если публикация написана публично
            if(!mPublication.isPublicationAnonymous()){
                // если в коллекции уже есть нужный пользователь
                if(LocLookApp.usersMap.containsKey(mPublication.getPublicationAuthorId())) {
                    // получить его и показать его имя
                    User author = LocLookApp.usersMap.get(mPublication.getPublicationAuthorId());

                    if(author != null)
                        mUserNameTV.setText(author.getName());
                }
            }
            // если публикация написана анонимно
            else {
                mUserNameTV.setText(R.string.publication_anonymous_text);
            }

            // ------------------------------------------------------------------------------- //

            CircularImageView mBadgeImageIV = UiUtils.findView(rootView, R.id.Publication_LI_BadgeImageIV);

            // установить изображение бейджика
            Badge badge = LocLookApp.badgesMap.get(mPublication.getPublicationBadgeId());
            if(badge != null)
                mBadgeImageIV.setImageResource(badge.getIconResId());

            // ------------------------------------------------------------------------------- //

            TextView mDateAndTimeTV = UiUtils.findView(rootView, R.id.Publication_LI_DateAndTimeTV);

            // дата и время публикации
            mDateAndTimeTV.setText(mPublication.getPublicationCreatedAt());

            // ------------------------------------------------------------------------------- //

            TextView mText = UiUtils.findView(rootView, R.id.Publication_LI_TextTV);

            // текст публикации
            mText.setText(mPublication.getPublicationText());

            // --------------------------------- FAVORITES ----------------------------------- //

            FrameLayout mFavoritesBlock = UiUtils.findView(rootView, R.id.Publication_LI_FavoritesBlock);
            final ImageView mFavoritesIV = UiUtils.findView(rootView, R.id.Publication_LI_Favorites_IV);

            // если публикация уже добавлена в избранное
            if(userFavoritesList.contains(mPublication.getPublicationId())) {
                // помечаем значок как активный
                mFavoritesIV.setImageResource(R.drawable.star_active);
            }
            // если публикация еще не добавлена в избранное
            else {
                // помечаем значок как неактивный
                mFavoritesIV.setImageResource(R.drawable.star_simple);
            }

            mFavoritesBlock.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                LocLookApp.showLog("CommentsFragment: setPublicationData(): favorites click in publication(" +mPublication.getPublicationId()+ ")");

                // если публикация уже добавлена в избранное
                if(userFavoritesList.contains(mPublication.getPublicationId())) {
                    LocLookApp.showLog("CommentsFragment: setPublicationData(): delete from favorites");

                    // помечаем значок как не активный
                    mFavoritesIV.setImageResource(R.drawable.star_simple);

                    //FavoritesUtility.deletePublicationFromUserFavorites(userFavoritesList, mPublication.getId());
                    FavoritesUtility.deletePublicationFromUserFavorites(userFavoritesList, "" +mPublication.getPublicationId());
                }
                // если публикация еще не добавлена в избранное
                else {
                    LocLookApp.showLog("CommentsFragment: setPublicationData(): add to favorites");

                    // помечаем значок как активный
                    mFavoritesIV.setImageResource(R.drawable.star_active);

                    //FavoritesUtility.addPublicationToUserFavorites(userFavoritesList, mPublication.getId());
                    FavoritesUtility.addPublicationToUserFavorites(userFavoritesList, "" +mPublication.getPublicationId());
                }
                }
            });

            // --------------------------------- LIKES --------------------------------------- //

            final RelativeLayout  mLikesBlock = UiUtils.findView(rootView, R.id.Publication_LI_LikesBlock);
            final ImageView       mLikesIV    = UiUtils.findView(rootView, R.id.Publication_LI_LikesSumIV);
            final TextView        mLikesTV    = UiUtils.findView(rootView, R.id.Publication_LI_LikesSumTV);

            final ArrayList<String> userLikesList = user.getLikesList();

            // если публикация уже добавлена в понравившиеся
            if(userLikesList.contains(mPublication.getPublicationId())) {
                // помечаем значок как активный
                mLikesIV.setImageResource(R.drawable.likes_active);
                mLikesTV.setTextColor(textColorActive);
            }
            // если публикация еще не добавлена в понравившиеся
            else {
                // помечаем значок как неактивный
                mLikesIV.setImageResource(R.drawable.likes_simple);
                mLikesTV.setTextColor(textColorSimple);
            }

            // задаем кол-во пользователей, которым понравилась данная публикация
            mLikesTV.setText(String.valueOf(mPublication.getPublicationLikesSum()));

            // задаем обработчик клика по блоку
            mLikesBlock.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    LocLookApp.showLog("CommentsFragment: setPublicationData(): likes click in publication(" +mPublication.getPublicationId()+ ")");

                    // если публикация уже добавлена в понравившиеся
                    if(userLikesList.contains(mPublication.getPublicationId())) {
                        LocLookApp.showLog("CommentsFragment: setPublicationData(): delete from likes");

                        // помечаем значок как не активный
                        mLikesIV.setImageResource(R.drawable.likes_simple);

                        // задаем прежнее значение
                        mLikesTV.setText(String.valueOf(mPublication.getPublicationLikesSum()));

                        // меняем цвет текста с кол-вом пользователей, которым понравилась публикация
                        mLikesTV.setTextColor(textColorSimple);

                        //LikesUtility.deletePublicationFromUserLikes(userLikesList, mPublication.getId());
                        LikesUtility.deletePublicationFromUserLikes(userLikesList, "" +mPublication.getPublicationId());
                    }
                    // если публикация еще не добавлена в понравившиеся
                    else {
                        LocLookApp.showLog("CommentsFragment: setPublicationData(): add to likes");
                        // помечаем значок как активный
                        mLikesIV.setImageResource(R.drawable.likes_active);

                        // задаем новое значение
                        mLikesTV.setText(String.valueOf(mPublication.getPublicationLikesSum() + 1));

                        // меняем цвет текста с кол-вом пользователей, которым понравилась публикация
                        mLikesTV.setTextColor(textColorActive);

                        //LikesUtility.addPublicationToUserLikes(userLikesList, mPublication.getId());
                        LikesUtility.addPublicationToUserLikes(userLikesList, "" +mPublication.getPublicationId());
                    }
                }
            });
        }

        // ------------------------------------------------------------------------------- //

        View mBottomLine = UiUtils.findView(rootView, R.id.Publication_LI_BottomLine);
        mBottomLine.setVisibility(View.VISIBLE);


    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.Comments_Send_Wrap:
                LocLookApp.showLog("CommentsFragment: onClick(): on send comment click");
                break;
        }
    }
}
