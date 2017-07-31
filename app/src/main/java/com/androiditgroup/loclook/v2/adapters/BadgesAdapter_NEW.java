package com.androiditgroup.loclook.v2.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.LocLookApp_NEW;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.data.BadgeController;
import com.androiditgroup.loclook.v2.models.BadgeModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sostrovschi on 2/24/17.
 */

public class BadgesAdapter_NEW extends BaseAdapter {

    private LayoutInflater  mInflater;

    private BadgeController badgeController;

    private Map<Integer, BadgeModel> badgeMap            = new LinkedHashMap<>();
    private Map<Integer, Integer>    badgeImageResIdMap  = new LinkedHashMap<>();

    public BadgesAdapter_NEW(LayoutInflater     inflater,
                             LocLookApp_NEW     locLookApp_NEW) {

        mInflater = inflater;

        if(locLookApp_NEW != null) {
            badgeController = locLookApp_NEW.getAppManager().getBadgeController();

            if(badgeController != null) {
                badgeMap = badgeController.getBadgeMap();
                badgeImageResIdMap = badgeController.getBadgeImageResIdMap();
            }
            else
                Log.e("LOG", "BadgesAdapter_NEW: constructor: badgeController is null");
        }
        else
            Log.e("LOG", "BadgesAdapter_NEW: constructor: context is null");
    }

    public int getCount() {
        return badgeMap.size();
    }

    public Object getItem(int position) {
        return badgeMap.get(position+1);
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
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(badgeImageResIdMap.get(position+1));

        return imageView;
    }
}
