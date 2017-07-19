package com.androiditgroup.loclook.v2.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.LocLookApp_NEW;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.data.BadgeController;
import com.androiditgroup.loclook.v2.data.PhotoController;
import com.androiditgroup.loclook.v2.data.UserController;
import com.androiditgroup.loclook.v2.models.Badge;
import com.androiditgroup.loclook.v2.models.BadgeModel;
import com.androiditgroup.loclook.v2.models.PhotoModel;
import com.androiditgroup.loclook.v2.models.PublicationModel;
import com.androiditgroup.loclook.v2.models.Quiz;
import com.androiditgroup.loclook.v2.models.User;
import com.androiditgroup.loclook.v2.models.UserModel;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;
import com.androiditgroup.loclook.v2.ui.general.MainActivity_NEW;
import com.androiditgroup.loclook.v2.utils.ExpandableHeightListView;
import com.androiditgroup.loclook.v2.utils.FavoritesUtility;
import com.androiditgroup.loclook.v2.utils.ImageDelivery;
import com.androiditgroup.loclook.v2.utils.LikesUtility;
import com.androiditgroup.loclook.v2.utils.QuizUtility;
import com.androiditgroup.loclook.v2.utils.UiUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by sostrovschi on 3/2/17.
 */

public class PublicationsListAdapter_NEW extends RecyclerView.Adapter<PublicationsListAdapter_NEW.ViewHolder> {

    private MainActivity_NEW    mMainActivity;
    private LocLookApp_NEW      locLookApp_NEW;

    private BadgeController     badgeController;
    private PhotoController     photoController;
    private UserController      userController;

    private ArrayList<PublicationModel> mPublicationList;

    //private User user = LocLookApp.usersMap.get(LocLookApp.appUserId);

    private ArrayList<String> userFavoritesList             = new ArrayList<>(); // user.getFavoritesList();
    private ArrayList<String> userCommentedPublicationsList = new ArrayList<>(); // user.getCommentedPublicationsList();
    private ArrayList<String> userLikesList                 = new ArrayList<>(); // user.getLikesList();

    // public PublicationsListAdapter(ArrayList<Publication> publicationsList) {
    public PublicationsListAdapter_NEW(MainActivity_NEW             mainActivity,
                                       ArrayList<PublicationModel>  publicationsList) {
        this.mMainActivity      = mainActivity;
        this.mPublicationList   = publicationsList;

        locLookApp_NEW  = ((LocLookApp_NEW) mMainActivity.getApplication());

        badgeController = locLookApp_NEW.getAppManager().getBadgeController();
        photoController = locLookApp_NEW.getAppManager().getPhotoController();
        userController  = locLookApp_NEW.getAppManager().getUserController();
    }

    @Override
    public PublicationsListAdapter_NEW.ViewHolder onCreateViewHolder(ViewGroup  parent,
                                                                     int        viewType) {
        // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.publication_list_item_not_used, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.publication_item, parent, false);

        LinearLayout footerBlock = UiUtils.findView(view, R.id.Publication_LI_FooterBlock);
        footerBlock.setWeightSum(4f);

        View footerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.publication_footer_ext, footerBlock, true);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final  PublicationsListAdapter_NEW.ViewHolder holder,
                                 int    position) {
//        LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder(): publication(" +position+ ")");

        try {
            final int textColorActive = locLookApp_NEW.getColorResId("colorPrimary");
            final int textColorSimple = locLookApp_NEW.getColorResId("dark_grey");
        } catch (Exception exc) {
            Log.e("LOG", "PublicationsListAdapter_NEW: onBindViewHolder(): getColorResId error: " +exc.getMessage());
        }

        final PublicationModel publication = mPublicationList.get(position);

        if(publication != null) {

            // текст публикации
            holder.mText.setText(publication.getPublicationText());

            // если публикация написана публично
            if (!publication.isPublicationAnonymous()) {

                if(userController != null) {
                    UserModel publicationAuthor = userController.getUserById(publication.getPublicationAuthorId());

                    if(publicationAuthor != null)
                        holder.mAuthorNameTV.setText(publicationAuthor.getUserName());
                    else
                        locLookApp_NEW.showLog("PublicationsListAdapter: onBindViewHolder: publicationAuthor is null");
                }
                else
                    locLookApp_NEW.showLog("PublicationsListAdapter: onBindViewHolder: userController is null");
            }
            // если публикация написана анонимно
            else {
                holder.mAuthorNameTV.setText(R.string.publication_anonymous_text);
            }

            if(badgeController != null) {
                BadgeModel publicationBadge = badgeController.getBadgeById(publication.getPublicationBadgeId());

                if (publicationBadge != null) {

                    int publicationBadgeImageResId = badgeController.getBadgeImageById(publicationBadge.getBadgeId());

                    if(publicationBadgeImageResId > 0)
                        holder.mBadgeImageIV.setImageResource(publicationBadgeImageResId);
                    else
                        locLookApp_NEW.showLog("PublicationsListAdapter: onBindViewHolder: publicationBadgeImageResId incorrect");
                }
                else
                    locLookApp_NEW.showLog("PublicationsListAdapter: onBindViewHolder: publicationBadge is null");
            }
            else
                locLookApp_NEW.showLog("PublicationsListAdapter: onBindViewHolder: badgeController is null");

            // дата и время публикации
            holder.mDateAndTimeTV.setText(publication.getPublicationCreatedAt());

            // если есть изображения
            if (publication.isPublicationHasImages()) {
                // отобразить фото-блок
                holder.mPhotoBlock.setVisibility(View.VISIBLE);

                try {
                    ArrayList<Bitmap> publicationPhotoList = photoController.getPublicationPhotoList(publication.getPublicationId());

                    if((publicationPhotoList != null) && (!publicationPhotoList.isEmpty())) {
                        GalleryListAdapter_NEW galleryAdapter = new GalleryListAdapter_NEW(mMainActivity, publicationPhotoList);
                        holder.mGalleryPhotosRV.setAdapter(galleryAdapter);
                    }
                } catch (Exception exc) {
                    locLookApp_NEW.showLog("PublicationsListAdapter: onBindViewHolder: getPublicationPhotoList error: " +exc.getMessage());
                }
            }

            // если есть опрос
            if (publication.isPublicationHasQuiz()) {
                // отобразить блок с опросом
                holder.mQuizBlock.setVisibility(View.VISIBLE);

                // получаем опрос
                final Quiz quiz = null; // publication.getPublicationQuiz();

                if (quiz != null) {
                    final ShowPublicationQuizAnswersAdapter quizAnswersAdapter = new ShowPublicationQuizAnswersAdapter(mMainActivity.getLayoutInflater(), quiz, quiz.getAnswersList());

                    // настраиваем список С ответами
                    holder.mQuizAnswersList.setAdapter(quizAnswersAdapter);

                    // если пользователь еще не отвечал в опросе
                    if (!quiz.userSelectedAnswer()) {
                        holder.mQuizAnswersList.setOnItemClickListener(new ListView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder(): answer was not selected: click on answer(" +quiz.getAnswersList().get(position).getId()+ "): " +quiz.getAnswersList().get(position).getText());

                                boolean saveResult = QuizUtility.saveUserAnswer(quiz.getAnswersList().get(position).getId());

                                if (saveResult) {
//                                LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder(): user answer saved successfully");

                                    // получаем из БД обновленные данные и задаем их опросу
                                    QuizUtility.setQuizAnswersVotesSum(quiz);
                                    QuizUtility.setQuizAnswersVotesInPercents(quiz);
                                    QuizUtility.setUserSelectedQuizAnswer(quiz, false);

                                    // обновляем данные в опросе
                                    quizAnswersAdapter.notifyDataSetChanged();

                                    // задаем общее кол-во ответов в опросе
                                    holder.mQuizAnswersSum.setText("" + quiz.getAllVotesSum());

                                    // отключаем слушателя клика по вариантам ответов опроса
                                    holder.mQuizAnswersList.setOnItemClickListener(null);
                                }
//                            else
//                                LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder(): user answer save error");
                            }
                        });
                    }

                    // задаем общее кол-во ответов в опросе
                    holder.mQuizAnswersSum.setText("" + quiz.getAllVotesSum());
                }
            }

            // --------------------------------- FAVORITES ---------------------------------------- //

           /* // если публикация уже добавлена в избранное
            if (userFavoritesList.contains(publication.getPublicationId())) {
                // помечаем значок как активный
                holder.mFavoritesIV.setImageResource(R.drawable.star_active);
            }
            // если публикация еще не добавлена в избранное
            else {
                // помечаем значок как неактивный
                holder.mFavoritesIV.setImageResource(R.drawable.star_simple);
            }

            holder.mFavoritesBlock.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder: favorites click in publication(" + publication.getPublicationId() + ")");

                    // помечаем значок как не активный
                    holder.mFavoritesIV.setImageResource(R.drawable.star_simple);

                    // если публикация уже добавлена в избранное
                    if (userFavoritesList.contains(publication.getPublicationId())) {
                        LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder: delete from favorites");

                        // помечаем значок как не активный
                        holder.mFavoritesIV.setImageResource(R.drawable.star_simple);

                        //FavoritesUtility.deletePublicationFromUserFavorites(userFavoritesList, publication.getId());
                        FavoritesUtility.deletePublicationFromUserFavorites(userFavoritesList, "" + publication.getPublicationId());
                    }
                    // если публикация еще не добавлена в избранное
                    else {
                        LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder: add to favorites");

                        // помечаем значок как активный
                        holder.mFavoritesIV.setImageResource(R.drawable.star_active);

                        //FavoritesUtility.addPublicationToUserFavorites(userFavoritesList, publication.getId());
                        FavoritesUtility.addPublicationToUserFavorites(userFavoritesList, "" + publication.getPublicationId());
                    }
                }
            });*/

            // --------------------------------- COMMENTS ----------------------------------------- //

            // если пользователь оставил хоть один комментарий в данной публикации
            /*if (userCommentedPublicationsList.contains(publication.getPublicationId())) {
                // помечаем значок как активный
                holder.mCommentsIV.setImageResource(R.drawable.comments_active);
                holder.mCommentsTV.setTextColor(textColorActive);
            }
            // если пользователь еще не оставил ни одного комментария в данной публикации
            else {
                // помечаем значок как неактивный
                holder.mCommentsIV.setImageResource(R.drawable.comments_simple);
                holder.mCommentsTV.setTextColor(textColorSimple);
            }

            // задаем кол-во комментариев оставленных пользователями к данной публикации
            //holder.mCommentsTV.setText(String.valueOf(publication.getPublicationLikesSum()));

            // задаем обработчик клика по блоку
            holder.mCommentsBlock.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder: comments click in publication(" + publication.getPublicationId() + ")");
                    MainActivity.selectedFragment = MainActivity.SelectedFragment.comments;
                    // mMainActivity.setFragment(CommentsFragment.newInstance(MainActivity.mInflater), false, true);
//                mMainActivity.setFragment(CommentsFragment.newInstance(publication), false, true);
                }
            });*/

            // --------------------------------- LIKES --------------------------------------------- //

            // если публикация уже добавлена в понравившиеся
            /*if (userLikesList.contains(publication.getPublicationId())) {
                // помечаем значок как активный
                holder.mLikesIV.setImageResource(R.drawable.likes_active);
                holder.mLikesTV.setTextColor(textColorActive);
            }
            // если публикация еще не добавлена в понравившиеся
            else {
                // помечаем значок как неактивный
                holder.mLikesIV.setImageResource(R.drawable.likes_simple);
                holder.mLikesTV.setTextColor(textColorSimple);
            }

            // задаем кол-во пользователей, которым понравилась данная публикация
//        holder.mLikesTV.setText(String.valueOf(publication.getPublicationLikesSum()));

            // задаем обработчик клика по блоку
            holder.mLikesBlock.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder: likes click in publication(" + publication.getPublicationId() + ")");

                    // если публикация уже добавлена в понравившиеся
                    if (userLikesList.contains(publication.getPublicationId())) {
                        LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder: delete from likes");

                        // помечаем значок как не активный
                        holder.mLikesIV.setImageResource(R.drawable.likes_simple);

                        // задаем прежнее значение
//                    holder.mLikesTV.setText(String.valueOf(publication.getPublicationLikesSum()));

                        // меняем цвет текста с кол-вом пользователей, которым понравилась публикация
                        holder.mLikesTV.setTextColor(textColorSimple);

                        //LikesUtility.deletePublicationFromUserLikes(userLikesList, publication.getId());
                        LikesUtility.deletePublicationFromUserLikes(userLikesList, "" + publication.getPublicationId());
                    }
                    // если публикация еще не добавлена в понравившиеся
                    else {
                        LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder: add to likes");
                        // помечаем значок как активный
                        holder.mLikesIV.setImageResource(R.drawable.likes_active);

                        // задаем новое значение
//                    holder.mLikesTV.setText(String.valueOf(publication.getPublicationLikesSum() + 1));

                        // меняем цвет текста с кол-вом пользователей, которым понравилась публикация
                        holder.mLikesTV.setTextColor(textColorActive);

                        //LikesUtility.addPublicationToUserLikes(userLikesList, publication.getId());
                        LikesUtility.addPublicationToUserLikes(userLikesList, "" + publication.getPublicationId());
                    }
                }
            });*/

            // ------------------------------------------------------------------------------------- //

            int listSize = mPublicationList.size();

            if ((listSize > 1) && (position < (listSize - 1))) {
                holder.mBottomLine.setVisibility(View.VISIBLE);
            }
        }
        else
            locLookApp_NEW.showLog("PublicationsListAdapter: onBindViewHolder: publication[" +position+ "] is null");
    }

    @Override
    public int getItemCount() {
        return mPublicationList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder { // implements View.OnClickListener {
        TextView mText;
        TextView mAuthorNameTV;
        TextView mDateAndTimeTV;
        CircularImageView mBadgeImageIV;

        LinearLayout mPhotoBlock;
        RecyclerView mGalleryPhotosRV;

        LinearLayout mQuizBlock;
        // ListView mQuizAnswersList;
        ExpandableHeightListView mQuizAnswersList;

        TextView mQuizAnswersSum;

        FrameLayout     mFavoritesBlock;
        ImageView       mFavoritesIV;

        RelativeLayout  mCommentsBlock;
        ImageView       mCommentsIV;
        TextView        mCommentsTV;

        RelativeLayout  mLikesBlock;
        ImageView       mLikesIV;
        TextView        mLikesTV;

        View            mBottomLine;

        public ViewHolder(View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);

            mText               = UiUtils.findView(itemView, R.id.Publication_LI_TextTV);
            mAuthorNameTV       = UiUtils.findView(itemView, R.id.Publication_LI_UserNameTV);
            mDateAndTimeTV      = UiUtils.findView(itemView, R.id.Publication_LI_DateAndTimeTV);
            mBadgeImageIV       = UiUtils.findView(itemView, R.id.Publication_LI_BadgeImageIV);

            mPhotoBlock         = UiUtils.findView(itemView, R.id.Publication_LI_PhotoBlock);
            mGalleryPhotosRV    = UiUtils.findView(itemView, R.id.Publication_LI_GalleryRecyclerView);

            mQuizBlock          = UiUtils.findView(itemView, R.id.Publication_LI_QuizBlock);
            mQuizAnswersList    = UiUtils.findView(itemView,R.id.Publication_LI_AnswersList);
            mQuizAnswersList.setExpanded(true);

            mQuizAnswersSum     = UiUtils.findView(itemView, R.id.Publication_LI_AnswersSum);

            mFavoritesBlock     = UiUtils.findView(itemView, R.id.Publication_LI_FavoritesBlock);
            mFavoritesIV        = UiUtils.findView(itemView, R.id.Publication_LI_Favorites_IV);

            mCommentsBlock       = UiUtils.findView(itemView, R.id.Publication_LI_CommentsBlock);
            mCommentsIV          = UiUtils.findView(itemView, R.id.Publication_LI_CommentsSumIV);
            mCommentsTV          = UiUtils.findView(itemView, R.id.Publication_LI_CommentsSumTV);

            mLikesBlock         = UiUtils.findView(itemView, R.id.Publication_LI_LikesBlock);
            mLikesIV            = UiUtils.findView(itemView, R.id.Publication_LI_LikesSumIV);
            mLikesTV            = UiUtils.findView(itemView, R.id.Publication_LI_LikesSumTV);

            mBottomLine         = UiUtils.findView(itemView, R.id.Publication_LI_BottomLine);
        }

//        @Override
//        public void onClick(View v) {
//            LocLookApp.showLog("PublicationsListAdapter: onClick(): publication(" +getPosition()+ ")");
//        }
    }
}