package com.androiditgroup.loclook.v2.models;

import android.graphics.Bitmap;
import android.location.Location;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by sostrovschi on 09.01.2017.
 */

public class User {

    private String mUserId;
    private String mName;
    private String mPhoneNumber;
    // private String mRate;
    private int mRate;
    // private String mBgImgUrl;
    // private String mAvatarUrl;
    private Bitmap mBackground;
    private Bitmap mAvatar;
    private String mDescription;
    private String mSiteUrl;
    private String mLatitude;
    private String mLongitude;
    // private String mRadius;
    private int mRadius;
    private String mRegionName;
    private String mStreetName;
    private Location mLocation;

    private ArrayList<String> mFavoritesList = new ArrayList<>();
    private ArrayList<String> mCommentedPublicationsList = new ArrayList<>();
    private ArrayList<String> mLikesList = new ArrayList<>();

    private User() { }

    public String getId() {
        return mUserId;
    }

    public String getName() {
        return mName;
    }

    public Location getLocation(){ return mLocation; }

    public String getRegionName(){ return mRegionName; }

    public String getStreetName(){ return mStreetName; }

    public ArrayList<String> getFavoritesList() { return mFavoritesList; }

    public ArrayList<String> getCommentedPublicationsList() { return mCommentedPublicationsList; }

    public ArrayList<String> getLikesList() { return mLikesList; }

    public void setLocation(Location value) {
        if(value != null)
            mLocation = value;
    }

    public void setRegionName(String value) {
        if(!TextUtils.isEmpty(value))
            mRegionName = value;
    }

    public void setStreetName(String value) {
        if(!TextUtils.isEmpty(value))
            mStreetName = value;
    }

    public static class Builder {
        private String mUserId;
        private String mName;
        private String mPhoneNumber;
        // private String mRate;
        private int mRate;
        //        private String mBgImgUrl;
//        private String mAvatarUrl;
        private Bitmap mBackground;
        private Bitmap mAvatar;
        private String mDescription;
        private String mSiteUrl;
        private String mLatitude;
        private String mLongitude;
        // private String mRadius;
        private int mRadius;
        private String mRegionName;
        private String mStreetName;
        private ArrayList<String> mFavoritesList = new ArrayList<>();
        private ArrayList<String> mCommentedPublicationsList = new ArrayList<>();
        private ArrayList<String> mLikesList = new ArrayList<>();

        public Builder userId(String value) {
            mUserId = value;
            return this;
        }

        public Builder name(String value) {
            mName = value;
            return this;
        }

        public Builder phone(String value) {
            mPhoneNumber = value;
            return this;
        }

        public Builder rate(int value) {
            mRate = value;
            return this;
        }

//        public Builder bgImgUrl(String bgImgUrl) {
//            mBgImgUrl = bgImgUrl;
//            return this;
//        }
//
//        public Builder avatarUrl(String avatarUrl) {
//            mAvatarUrl = avatarUrl;
//            return this;
//        }

        public Builder background(Bitmap value) {
            mBackground = value;
            return this;
        }

        public Builder avatar(Bitmap value) {
            mAvatar = value;
            return this;
        }

        public Builder description(String value) {
            mDescription = value;
            return this;
        }

        public Builder siteUrl(String value) {
            mSiteUrl = value;
            return this;
        }

        public Builder latitude(String value) {
            mLatitude = value;
            return this;
        }

        public Builder longitude(String value) {
            mLongitude = value;
            return this;
        }

        public Builder radius(int value) {
            mRadius = value;
            return this;
        }

        public Builder regionName(String value) {
            mRegionName = value;
            return this;
        }

        public Builder streetName(String value) {
            mStreetName = value;
            return this;
        }

        public Builder favoriteslist(ArrayList<String> list) {
            mFavoritesList.addAll(list);
            return this;
        }

        public Builder commentedPublicationsList(ArrayList<String> list) {
            mCommentedPublicationsList.addAll(list);
            return this;
        }

        public Builder likeslist(ArrayList<String> list) {
            mLikesList.addAll(list);
            return this;
        }

        public User build() {
            User user = new User();
            user.mUserId = this.mUserId;
            user.mName = this.mName;
            user.mPhoneNumber = this.mPhoneNumber;
            user.mRate = this.mRate;
//            user.mBgImgUrl = this.mBgImgUrl;
//            user.mAvatarUrl = this.mAvatarUrl;
            user.mBackground = this.mBackground;
            user.mAvatar = this.mAvatar;
            user.mDescription = this.mDescription;
            user.mSiteUrl = this.mSiteUrl;
            user.mLatitude = this.mLatitude;
            user.mLongitude = this.mLongitude;
            user.mRadius = this.mRadius;
            user.mRegionName = this.mRegionName;
            user.mStreetName = this.mStreetName;
            user.mFavoritesList = this.mFavoritesList;
            user.mCommentedPublicationsList = this.mCommentedPublicationsList;
            user.mLikesList = this.mLikesList;
            return user;
        }
    }
}
