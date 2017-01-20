package com.androiditgroup.loclook.v2.models;

/**
 * Created by sostrovschi on 09.01.2017.
 */

public class User {

    private String mUserId;
    private String mLogin;
    private String mPhoneNumber;
    private String mRate;
    private String mBgImgUrl;
    private String mAvatarUrl;
    private String mDescription;
    private String mSiteUrl;
    private String mLatitude;
    private String mLongitude;
    private String mRadius;
    private String mRegionName;
    private String mStreetName;

    private User() { }

    public String getUserId() {
        return mUserId;
    }

    public static class Builder {
        private String mUserId;
        private String mLogin;
        private String mPhoneNumber;
        private String mRate;
        private String mBgImgUrl;
        private String mAvatarUrl;
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

        public Builder login(String login) {
            mLogin = login;
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

        public Builder bgImgUrl(String bgImgUrl) {
            mBgImgUrl = bgImgUrl;
            return this;
        }

        public Builder avatarUrl(String avatarUrl) {
            mAvatarUrl = avatarUrl;
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
            user.mLogin = this.mLogin;
            user.mPhoneNumber = this.mPhoneNumber;
            user.mRate = this.mRate;
            user.mBgImgUrl = this.mBgImgUrl;
            user.mAvatarUrl = this.mAvatarUrl;
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
