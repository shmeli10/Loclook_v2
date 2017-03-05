package com.androiditgroup.loclook.v2.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.models.Badge;
import com.androiditgroup.loclook.v2.models.Publication;
import com.androiditgroup.loclook.v2.models.User;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by sostrovschi on 3/2/17.
 */

public class PublicationsListAdapter extends RecyclerView.Adapter<PublicationsListAdapter.ViewHolder> {

    private ArrayList<Publication> mPublications;

    public PublicationsListAdapter(ArrayList<Publication> publicationsList) {
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
        holder.mText.setText(publication.getText());

        if(!publication.isAnonymous()){
            if(LocLookApp.usersMap.containsKey(publication.getAuthorId())) {
                User author = LocLookApp.usersMap.get(publication.getAuthorId());

                if(author != null)
                    holder.mAuthorNameTV.setText(author.getName());
            }
        }
        else {
            holder.mAuthorNameTV.setText(R.string.publication_anonymous_text);
        }

        Badge badge = LocLookApp.badgesMap.get(publication.getBadgeId());

        if(badge != null)
            holder.mBadgeImageIV.setImageResource(badge.getIconResId());

        holder.mDateAndTimeTV.setText(publication.getDateAndTime());
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


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mText           = (TextView) itemView.findViewById(R.id.Publication_TextTV);
            mAuthorNameTV   = (TextView) itemView.findViewById(R.id.Publication_UserNameTV);
            mDateAndTimeTV  = (TextView) itemView.findViewById(R.id.Publication_DateAndTimeTV);
            mBadgeImageIV   = (CircularImageView) itemView.findViewById(R.id.Publication_BadgeImageIV);
        }

        @Override
        public void onClick(View v) {
            Log.e("ABC", "PublicationsListAdapter: onClick(): publication(" +getPosition()+ ")");
        }
    }
}