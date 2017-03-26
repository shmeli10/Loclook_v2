package com.androiditgroup.loclook.v2.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.models.Badge;
import com.androiditgroup.loclook.v2.models.Publication;
import com.androiditgroup.loclook.v2.models.Quiz;
import com.androiditgroup.loclook.v2.models.User;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;
import com.androiditgroup.loclook.v2.utils.ExpandableHeightListView;
import com.androiditgroup.loclook.v2.utils.ImageDelivery;
import com.androiditgroup.loclook.v2.utils.UiUtils;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by sostrovschi on 3/2/17.
 */

public class PublicationsListAdapter extends RecyclerView.Adapter<PublicationsListAdapter.ViewHolder> {

    private MainActivity mMainActivity;
    private ArrayList<Publication> mPublications;

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
    public void onBindViewHolder(PublicationsListAdapter.ViewHolder holder, int position) {
        // Log.e("ABC", "PublicationsListAdapter: onBindViewHolder(): publication(" +position+ ")");
        Publication publication = mPublications.get(position);

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

            // Log.e("ABC", "PublicationsListAdapter: onBindViewHolder(): photosList size= " +photosList.size()+ ", tumbnailsList size= " +tumbnailsList.size());

            GalleryListAdapter galleryAdapter = new GalleryListAdapter(mMainActivity, photosList);
            holder.mGalleryPhotosRV.setAdapter(galleryAdapter);
        }

        // если есть опрос
        if(publication.hasQuiz()) {
            // отобразить блок с опросом
            holder.mQuizBlock.setVisibility(View.VISIBLE);

            // получаем опрос
            Quiz quiz = publication.getQuiz();

            if(quiz != null) {
                ShowPublicationQuizAnswersAdapter quizAnswersAdapter = new ShowPublicationQuizAnswersAdapter(mMainActivity.getLayoutInflater(), quiz, quiz.getAnswersList());

                // настраиваем список
                holder.mQuizAnswersList.setAdapter(quizAnswersAdapter);

                holder.mQuizAnswersSum.setText("" +quiz.getSelectedAnswersSum());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mPublications.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

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
        }

        @Override
        public void onClick(View v) {
            Log.e("ABC", "PublicationsListAdapter: onClick(): publication(" +getPosition()+ ")");
        }
    }
}