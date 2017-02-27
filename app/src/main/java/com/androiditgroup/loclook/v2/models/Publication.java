package com.androiditgroup.loclook.v2.models;

import android.graphics.Bitmap;
import android.location.Location;

import java.util.ArrayList;

/**
 * Created by OS1 on 26.02.2017.
 */
public class Publication {

    private String mId;
    private String mText;
    private String mAuthorId;
    private Badge mBadge;
    private String mCreatedAt;
    private Location mLocation;
    private String mRegionName;
    private String mStreetName;
    private boolean mHasQuiz;
    private ArrayList<QuizAnswer> mQuizAnswersList = new ArrayList<>();
    private boolean mHasImages;
    private ArrayList<Bitmap> mImagesList = new ArrayList<>();
    private boolean mIsAnonymous;

    public static class Builder {
        private String mId;
        private String mText;
        private String mAuthorId;
        private Badge mBadge;
        private String mCreatedAt;
        private Location mLocation;
        private String mRegionName;
        private String mStreetName;
        private boolean mHasQuiz;
        private ArrayList<QuizAnswer> mQuizAnswersList = new ArrayList<>();
        private boolean mHasImages;
        private ArrayList<Bitmap> mImagesList = new ArrayList<>();
        private boolean mIsAnonymous;

        public Builder id(String value) {
            mId = value;
            return this;
        }

        public Builder text(String value) {
            mText = value;
            return this;
        }

        public Builder authorId(String value) {
            mAuthorId = value;
            return this;
        }

        public Builder badge(Badge value) {
            mBadge = value;
            return this;
        }

        public Builder createdAt(String value) {
            mCreatedAt = value;
            return this;
        }

        public Builder location(Location value) {
            mLocation = value;
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

        public Builder hasQuiz(boolean value) {
            mHasQuiz = value;
            return this;
        }

        public Builder quizAnswerList(ArrayList<QuizAnswer> list) {
            mQuizAnswersList = list;
            return this;
        }

        public Builder hasImages(boolean value) {
            mHasImages = value;
            return this;
        }

        public Builder imagesList(ArrayList<Bitmap> list) {
            mImagesList = list;
            return this;
        }

        public Builder isAnonymous(boolean value) {
            mIsAnonymous = value;
            return this;
        }

        public Publication build() {
            Publication publication = new Publication();
            publication.mId = this.mId;
            publication.mText = this.mText;
            publication.mAuthorId = this.mAuthorId;
            publication.mBadge = this.mBadge;
            publication.mCreatedAt = this.mCreatedAt;
            publication.mLocation = this.mLocation;
            publication.mRegionName = this.mRegionName;
            publication.mStreetName = this.mStreetName;
            publication.mHasQuiz = this.mHasQuiz;
            publication.mQuizAnswersList = this.mQuizAnswersList;
            publication.mHasImages = this.mHasImages;
            publication.mImagesList = this.mImagesList;
            publication.mIsAnonymous = this.mIsAnonymous;

            return publication;
        }
    }
}
