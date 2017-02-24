package com.androiditgroup.loclook.v2.models;

import android.graphics.Bitmap;

/**
 * Created by sostrovschi on 2/20/17.
 */

public class Badge {

    private String mId;
    private String mName;
//    private String mIconPath;
//    private Bitmap mIcon;
    private boolean mIsEnabled;
    private int mIconResId;

    private Badge() { }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getIconResId() {
        return mIconResId;
    }

    public static class Builder {
        private String mId;
        private String mName;
//        private String mIconPath;
//        private Bitmap mIcon;
        private boolean mIsEnabled;
        private int mIconResId;

        public Badge.Builder id(String value) {
            mId = value;
            return this;
        }

        public Badge.Builder name(String value) {
            mName = value;
            return this;
        }

//        public Builder iconPath(String iconPath) {
//            mIconPath = iconPath;
//            return this;
//        }
//
//        public Builder icon(Bitmap icon) {
//            mIcon = icon;
//            return this;
//        }

        public Builder iconResId(int value) {
            mIconResId = value;
            return this;
        }

        public Builder isEnabled(boolean value) {
            mIsEnabled = value;
            return this;
        }

        public Badge build() {
            Badge badge = new Badge();
            badge.mId = this.mId;
            badge.mName = this.mName;
//            badge.mIconPath = this.mIconPath;
//            badge.mIcon = this.mIcon;
            badge.mIsEnabled = this.mIsEnabled;
            badge.mIconResId = this.mIconResId;
            return badge;
        }
    }
}
