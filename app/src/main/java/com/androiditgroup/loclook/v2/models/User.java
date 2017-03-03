package com.androiditgroup.loclook.v2.models;

import android.graphics.Bitmap;
import android.location.Location;
import android.text.TextUtils;

/**
 * Created by sostrovschi on 09.01.2017.
 */

public class User {

    private String mUserId;
    private String mName;
    private String mPhoneNumber;
    private String mRate;
    // private String mBgImgUrl;
    // private String mAvatarUrl;
    private Bitmap mBackground;
    private Bitmap mAvatar;
    private String mDescription;
    private String mSiteUrl;
    private String mLatitude;
    private String mLongitude;
    private String mRadius;
    private String mRegionName;
    private String mStreetName;
    private Location mLocation;

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
        private String mRate;
//        private String mBgImgUrl;
//        private String mAvatarUrl;
        private Bitmap mBackground;
        private Bitmap mAvatar;
        private String mDescription;
        private String mSiteUrl;
        private String mLatitude;
        private String mLongitude;
        private String mRadius;
        private String mRegionName;
        private String mStreetName;

        public Builder userId(String userId) {
            mUserId = userId;
            return this;
        }

        public Builder name(String name) {
            mName = name;
            return this;
        }

        public Builder phone(String phone) {
            mPhoneNumber = phone;
            return this;
        }

        public Builder rate(String rate) {
            mRate = rate;
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

        public Builder background(Bitmap background) {
            mBackground = background;
            return this;
        }

        public Builder avatar(Bitmap avatar) {
            mAvatar = avatar;
            return this;
        }

        public Builder description(String description) {
            mDescription = description;
            return this;
        }

        public Builder siteUrl(String siteUrl) {
            mSiteUrl = siteUrl;
            return this;
        }

        public Builder latitude(String latitude) {
            mLatitude = latitude;
            return this;
        }

        public Builder longitude(String longitude) {
            mLongitude = longitude;
            return this;
        }

        public Builder radius(String radius) {
            mRadius = radius;
            return this;
        }

        public Builder regionName(String regionName) {
            mRegionName = regionName;
            return this;
        }

        public Builder streetName(String streetName) {
            mStreetName = streetName;
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
            return user;
        }
    }
}
