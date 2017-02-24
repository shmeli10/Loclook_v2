package com.androiditgroup.loclook.v2.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.models.Badge;

import java.util.ArrayList;

/**
 * Created by sostrovschi on 2/24/17.
 */

public class BadgesAdapter  extends BaseAdapter {
    // private Context mContext;

    private ArrayList<Badge> badgesList = new ArrayList<>();
    private LayoutInflater mInflater;
//    private BadgesAdapter.BadgeCallback mBadgeCallback;

//    public interface BadgeCallback {
//        void onSelected(int badgeId);
//    }

//    public BadgesAdapter(LayoutInflater inflater, BadgeCallback callback) {
    public BadgesAdapter(LayoutInflater inflater) {
        mInflater = inflater;
//        mBadgeCallback = callback;
        badgesList.addAll(LocLookApp.badgesList);
    }

    public int getCount() {
        return badgesList.size();
    }

    public Object getItem(int position) {
        return badgesList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        // пункт списка
        View view = mInflater.inflate(R.layout.badge_item, parent, false);

        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = (ImageView) view.findViewById(R.id.Badges_BadgeImageIV);
            // imageView.setLayoutParams(new GridView.LayoutParams(40, 40));
            // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(badgesList.get(position).getIconResId());
        return imageView;
    }
}
