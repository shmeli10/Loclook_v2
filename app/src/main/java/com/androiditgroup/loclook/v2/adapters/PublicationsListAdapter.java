package com.androiditgroup.loclook.v2.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.models.Badge;
import com.androiditgroup.loclook.v2.models.Publication;
import com.androiditgroup.loclook.v2.models.Quiz;
import com.androiditgroup.loclook.v2.models.User;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;
import com.androiditgroup.loclook.v2.utils.ExpandableHeightListView;
import com.androiditgroup.loclook.v2.utils.FavoritesUtility;
import com.androiditgroup.loclook.v2.utils.ImageDelivery;
import com.androiditgroup.loclook.v2.utils.QuizUtility;
import com.androiditgroup.loclook.v2.utils.UiUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by sostrovschi on 3/2/17.
 */

public class PublicationsListAdapter extends RecyclerView.Adapter<PublicationsListAdapter.ViewHolder> {

    private MainActivity mMainActivity;
    private ArrayList<Publication> mPublications;

    private User user = LocLookApp.usersMap.get(LocLookApp.appUserId);

    private ArrayList<String> userFavoritesList = user.getFavoritesList();

    // public PublicationsListAdapter(ArrayList<Publication> publicationsList) {
    public PublicationsListAdapter(MainActivity mainActivity, ArrayList<Publication> publicationsList) {
        this.mMainActivity = mainActivity;
        this.mPublications = publicationsList;
    }

    @Override
    public PublicationsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.publication_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PublicationsListAdapter.ViewHolder holder, int position) {
//        LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder(): publication(" +position+ ")");

        final Publication publication = mPublications.get(position);

        // текст публикации
        holder.mText.setText(publication.getText());

        // если публикация написана публично
        if(!publication.isAnonymous()){
            // если в коллекции уже есть нужный пользователь
            if(LocLookApp.usersMap.containsKey(publication.getAuthorId())) {
                // получить его и показать его имя
                User author = LocLookApp.usersMap.get(publication.getAuthorId());

                if(author != null)
                    holder.mAuthorNameTV.setText(author.getName());
            }
        }
        // если публикация написана анонимно
        else {
            holder.mAuthorNameTV.setText(R.string.publication_anonymous_text);
        }

        // установить изображение бейджика
        Badge badge = LocLookApp.badgesMap.get(publication.getBadgeId());
        if(badge != null)
            holder.mBadgeImageIV.setImageResource(badge.getIconResId());

        // дата и время публикации
        holder.mDateAndTimeTV.setText(publication.getDateAndTime());

        // если есть изображения
        if(publication.hasImages()) {
            // отобразить фото-блок
            holder.mPhotoBlock.setVisibility(View.VISIBLE);

            ArrayList<Bitmap> photosList = new ArrayList<>();
            photosList.addAll(ImageDelivery.getPhotosListById(publication.getPhotosIdsList()));

            // LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder(): photosList size= " +photosList.size()+ ", tumbnailsList size= " +tumbnailsList.size());

            GalleryListAdapter galleryAdapter = new GalleryListAdapter(mMainActivity, photosList);
            holder.mGalleryPhotosRV.setAdapter(galleryAdapter);
        }

        // если есть опрос
        if(publication.hasQuiz()) {
            // отобразить блок с опросом
            holder.mQuizBlock.setVisibility(View.VISIBLE);

            // получаем опрос
            final Quiz quiz = publication.getQuiz();

            if(quiz != null) {
                final ShowPublicationQuizAnswersAdapter quizAnswersAdapter = new ShowPublicationQuizAnswersAdapter(mMainActivity.getLayoutInflater(), quiz, quiz.getAnswersList());

                // настраиваем список С ответами
                holder.mQuizAnswersList.setAdapter(quizAnswersAdapter);

                // если пользователь еще не отвечал в опросе
                if(!quiz.userSelectedAnswer()) {
                    holder.mQuizAnswersList.setOnItemClickListener(new ListView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder(): answer was not selected: click on answer(" +quiz.getAnswersList().get(position).getId()+ "): " +quiz.getAnswersList().get(position).getText());

                            boolean saveResult = QuizUtility.saveUserAnswer(quiz.getAnswersList().get(position).getId());

                            if(saveResult) {
//                                LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder(): user answer saved successfully");

                                // получаем из БД обновленные данные и задаем их опросу
                                QuizUtility.setQuizAnswersVotesSum(quiz);
                                QuizUtility.setQuizAnswersVotesInPercents(quiz);
                                QuizUtility.setUserSelectedQuizAnswer(quiz, false);

                                // обновляем данные в опросе
                                quizAnswersAdapter.notifyDataSetChanged();

                                // задаем общее кол-во ответов в опросе
                                holder.mQuizAnswersSum.setText("" +quiz.getAllVotesSum());

                                // отключаем слушателя клика по вариантам ответов опроса
                                holder.mQuizAnswersList.setOnItemClickListener(null);
                            }
//                            else
//                                LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder(): user answer save error");
                        }
                    });
                }

                // задаем общее кол-во ответов в опросе
                holder.mQuizAnswersSum.setText("" +quiz.getAllVotesSum());
            }
        }

        // если публикация уже добавлена в избранное
        if(userFavoritesList.contains(publication.getId())) {
            // помечаем значок как активный
            holder.mFavoritesIV.setColorFilter(null);
            holder.mFavoritesIV.setColorFilter(R.color.colorPrimary);
        }
        // если публикация еще не добавлена в избранное
        else {
            // помечаем значок как неактивный
            holder.mFavoritesIV.setColorFilter(null);
//            holder.mFavoritesIV.setColorFilter(R.color.medium_dark_grey);
        }

        holder.mFavoritesBlock.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder: favorites click in publication(" +publication.getId()+ ")");

                // если публикация уже добавлена в избранное
                if(userFavoritesList.contains(publication.getId())) {
                    LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder: delete from favorites");

                    // boolean deleteResult = FavoritesUtility.deletePublicationFromUserFavorites(publication.getId());

                    // помечаем значок как активный
                    holder.mFavoritesIV.setColorFilter(null);
                    holder.mFavoritesIV.setColorFilter(R.color.medium_dark_grey);

                    userFavoritesList.remove(publication.getId());
                }
                // если публикация еще не добавлена в избранное
                else {
                    LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder: add to favorites");

                    // boolean addResult = FavoritesUtility.addPublicationToUserFavorites(publication.getId());

//                    if (addResult) {
//                        LocLookApp.showLog("PublicationsListAdapter: onBindViewHolder(): add publication to favorites is successful");

                        // помечаем значок как активный
                        holder.mFavoritesIV.setColorFilter(null);
                        holder.mFavoritesIV.setColorFilter(R.color.colorPrimary);

                        userFavoritesList.add(publication.getId());
//                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPublications.size();
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

        FrameLayout mFavoritesBlock;
        ImageView   mFavoritesIV;

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
        }

//        @Override
//        public void onClick(View v) {
//            LocLookApp.showLog("PublicationsListAdapter: onClick(): publication(" +getPosition()+ ")");
//        }
    }
}