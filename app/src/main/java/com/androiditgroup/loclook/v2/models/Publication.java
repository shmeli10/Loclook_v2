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
    //    private Badge mBadge;
    private String mBadgeId;
    private String mDateAndTime;
    private Location mLocation;
    private String mRegionName;
    private String mStreetName;
    private boolean mHasQuiz;
    //    private ArrayList<QuizAnswer> mQuizAnswersList = new ArrayList<>();
    // private ArrayList<String> mQuizAnswersIdsList = new ArrayList<>();
    private Quiz mQuiz;
    private boolean mHasImages;
    //    private ArrayList<Bitmap> mPhotosList = new ArrayList<>();
    private ArrayList<String> mPhotosIdsList = new ArrayList<>();
    private boolean mIsAnonymous;
    private int mLikesSum;

    public String getId() { return mId; }

    public String getText() {
        return (mText != null) ? mText : "";
    }

    public String getAuthorId() { return mAuthorId; }

    public String getBadgeId() { return mBadgeId; }

    public String getDateAndTime() { return mDateAndTime; }

    public boolean hasImages() { return mHasImages; }

    public ArrayList<String> getPhotosIdsList() { return mPhotosIdsList; }

    public boolean hasQuiz() { return mHasQuiz; }

    public Quiz getQuiz() { return mQuiz; }

    public boolean isAnonymous() { return mIsAnonymous; }

    public int getLikesSum() { return mLikesSum; }

    public static class Builder {
        private String mId;
        private String mText;
        private String mAuthorId;
        //        private Badge mBadge;
        private String mBadgeId;
        private String mDateAndTime;
        private Location mLocation;
        private String mRegionName;
        private String mStreetName;
        private boolean mHasQuiz;
        private Quiz mQuiz;

        //        private ArrayList<QuizAnswer> mQuizAnswersList = new ArrayList<>();
        // private ArrayList<String> mQuizAnswersIdsList = new ArrayList<>();
        private boolean mHasImages;
        //        private ArrayList<Bitmap> mPhotosList = new ArrayList<>();
        private ArrayList<String> mPhotosIdsList = new ArrayList<>();
        private boolean mIsAnonymous;
        private int mLikesSum;

        public Builder id(String value) {
            mId = value;
            return this;
        }

        public Builder text(String value) {
            // mText = value;
            mText = (value != null) ? value : "";
            return this;
        }

        public Builder authorId(String value) {
            mAuthorId = value;
            return this;
        }

//        public Builder badge(Badge value) {
//            mBadge = value;
//            return this;
//        }

        public Builder badge(String value) {
            mBadgeId = value;
            return this;
        }

        public Builder dateAndTime(String value) {
            mDateAndTime = (value != null) ? value : "";
            return this;
        }

        public Builder location(Location value) {
            mLocation = value;
            return this;
        }

        public Builder regionName(String value) {
            mRegionName = (value != null) ? value : "";
            return this;
        }

        public Builder streetName(String value) {
            mStreetName = (value != null) ? value : "";
            return this;
        }

        public Builder hasQuiz(boolean value) {
            mHasQuiz = value;
            return this;
        }

        public Builder quiz(Quiz value) {
            mQuiz = value;
            return this;
        }

//        public Builder quizAnswerList(ArrayList<QuizAnswer> list) {
//            mQuizAnswersList = list;
//            return this;
//        }

//        public Builder quizAnswerList(ArrayList<String> list) {
//            mQuizAnswersIdsList = list;
//            return this;
//        }

        public Builder hasImages(boolean value) {
            mHasImages = value;
            return this;
        }

//        public Builder photosList(ArrayList<Bitmap> list) {
//            mPhotosList = list;
//            return this;
//        }

        public Builder photosList(ArrayList<String> list) {
            mPhotosIdsList = list;
            return this;
        }

        public Builder isAnonymous(boolean value) {
            mIsAnonymous = value;
            return this;
        }

        public Builder likesSum(int value) {
            mLikesSum = value;
            return this;
        }

        public Publication build() {
            Publication publication = new Publication();
            publication.mId = this.mId;
            publication.mText = this.mText;
            publication.mAuthorId = this.mAuthorId;
            publication.mBadgeId = this.mBadgeId;
            publication.mDateAndTime = this.mDateAndTime;
            publication.mLocation = this.mLocation;
            publication.mRegionName = this.mRegionName;
            publication.mStreetName = this.mStreetName;
            publication.mHasQuiz = this.mHasQuiz;
            publication.mQuiz = this.mQuiz;
//            publication.mQuizAnswersIdsList = this.mQuizAnswersIdsList;
            publication.mHasImages = this.mHasImages;
            publication.mPhotosIdsList = this.mPhotosIdsList;
            publication.mIsAnonymous = this.mIsAnonymous;
            publication.mLikesSum = this.mLikesSum;

            return publication;
        }
    }
}
