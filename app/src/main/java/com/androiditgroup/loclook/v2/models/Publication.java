package com.androiditgroup.loclook.v2.models;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by OS1 on 26.02.2017.
 */
public class Publication {

    private int publicationId;
    private int publicationAuthorId;
    private int publicationBadgeId;
    private int publicationCommentsSum;
    private int publicationLikesSum;

    private String publicationText;
    private String publicationLatitude;
    private String publicationLongitude;
    private String publicationRegionName;
    private String publicationStreetName;
    private String publicationCreatedAt;

    private boolean publicationHasQuiz;
    private boolean publicationHasImages;
    private boolean publicationIsAnonymous;

    private Location publicationLocation;

    private Quiz publicationQuiz;

    private ArrayList<String> publicationPhotosIdsList = new ArrayList<>();

    // --------------------------- GETTERS ------------------------------------------- //

    public int getPublicationId() {
        return publicationId;
    }

    public int getPublicationAuthorId() {
        return publicationAuthorId;
    }

    public int getPublicationBadgeId() {
        return publicationBadgeId;
    }

    public int getPublicationCommentsSum() {
        return publicationCommentsSum;
    }

    public int getPublicationLikesSum() {
        return publicationLikesSum;
    }

    public String getPublicationText() {
        return publicationText;
    }

    public String getPublicationLatitude() {
        return publicationLatitude;
    }

    public String getPublicationLongitude() {
        return publicationLongitude;
    }

    public String getPublicationRegionName() {
        return publicationRegionName;
    }

    public String getPublicationStreetName() {
        return publicationStreetName;
    }

    public String getPublicationCreatedAt() {
        return publicationCreatedAt;
    }

    public boolean hasPublicationQuiz() {
        return publicationHasQuiz;
    }

    public boolean hasPublicationImages() {
        return publicationHasImages;
    }

    public boolean isPublicationAnonymous() {
        return publicationIsAnonymous;
    }

    public ArrayList<String> getPublicationPhotosIdsList() {
        return publicationPhotosIdsList;
    }

    public Quiz getPublicationQuiz() {
        return publicationQuiz;
    }

    public static class Builder {
        private int publicationId;
        private int publicationAuthorId;
        private int publicationBadgeId;
        private int publicationCommentsSum;
        private int publicationLikesSum;

        private String publicationText;
        private String publicationLatitude;
        private String publicationLongitude;
        private String publicationRegionName;
        private String publicationStreetName;
        private String publicationCreatedAt;

        private boolean publicationHasQuiz;
        private boolean publicationHasImages;
        private boolean publicationIsAnonymous;

        private Location publicationLocation;

        private Quiz publicationQuiz;

        private ArrayList<String> publicationPhotosIdsList = new ArrayList<>();


        public Builder publicationId(int value) {
            publicationId = value;
            return this;
        }

        public Builder publicationAuthorId(int value) {
            publicationAuthorId = value;
            return this;
        }
        public Builder publicationBadgeId(int value) {
            publicationBadgeId = value;
            return this;
        }
        public Builder publicationCommentsSum(int value) {
            publicationCommentsSum = value;
            return this;
        }
        public Builder publicationLikesSum(int value) {
            publicationLikesSum = value;
            return this;
        }
        public Builder publicationText(String value) {
            publicationText = value;
            return this;
        }
        public Builder publicationLatitude(String value) {
            publicationLatitude = value;
            return this;
        }
        public Builder publicationLongitude(String value) {
            publicationLongitude = value;
            return this;
        }
        public Builder publicationRegionName(String value) {
            publicationRegionName = value;
            return this;
        }
        public Builder publicationStreetName(String value) {
            publicationStreetName = value;
            return this;
        }
        public Builder publicationCreatedAt(String value) {
            publicationCreatedAt = value;
            return this;
        }
        public Builder publicationHasQuiz(boolean value) {
            publicationHasQuiz = value;
            return this;
        }
        public Builder publicationHasImages(boolean value) {
            publicationHasImages = value;
            return this;
        }
        public Builder publicationIsAnonymous(boolean value) {
            publicationIsAnonymous = value;
            return this;
        }
        public Builder publicationLocation(Location value) {
            publicationLocation = value;
            return this;
        }
        public Builder publicationQuiz(Quiz value) {
            publicationQuiz = value;
            return this;
        }
        public Builder publicationPhotosIdsList(ArrayList<String> list) {
            publicationPhotosIdsList = list;
            return this;
        }

        public Publication build() {
            Publication publication = new Publication();

            publication.publicationId           = this.publicationId;
            publication.publicationAuthorId     = this.publicationAuthorId;
            publication.publicationBadgeId      = this.publicationBadgeId;
            publication.publicationCommentsSum  = this.publicationCommentsSum;
            publication.publicationLikesSum     = this.publicationLikesSum;

            publication.publicationText         = this.publicationText;
            publication.publicationLatitude     = this.publicationLatitude;
            publication.publicationLongitude    = this.publicationLongitude;
            publication.publicationRegionName   = this.publicationRegionName;
            publication.publicationStreetName   = this.publicationStreetName;
            publication.publicationCreatedAt    = this.publicationCreatedAt;

            publication.publicationHasQuiz      = this.publicationHasQuiz;
            publication.publicationHasImages    = this.publicationHasImages;
            publication.publicationIsAnonymous  = this.publicationIsAnonymous;

            publication.publicationLocation     = this.publicationLocation;

            publication.publicationQuiz         = this.publicationQuiz;

            publication.publicationPhotosIdsList = new ArrayList<>();

            return publication;
        }
    }

    //private String mId;
    //private String mText;
    //private String mAuthorId;
    //    private Badge mBadge;
    //private String mBadgeId;
    // private String mDateAndTime;
    //private Location mLocation;
    //private String mRegionName;
    //private String mStreetName;
    //private boolean mHasQuiz;
    //    private ArrayList<QuizAnswer> mQuizAnswersList = new ArrayList<>();
    // private ArrayList<String> mQuizAnswersIdsList = new ArrayList<>();
    //private Quiz mQuiz;
    //private boolean mHasImages;
    //    private ArrayList<Bitmap> mPhotosList = new ArrayList<>();
    //private ArrayList<String> mPhotosIdsList = new ArrayList<>();
    //private boolean mIsAnonymous;
    //private int mCommentsSum;
    //private int mLikesSum;

    /*public String getId() { return mId; }

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

    public int getCommentsSum() { return mCommentsSum; }

    public int getLikesSum() { return mLikesSum; }*/

    /*public static class Builder {
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
        private int mCommentsSum;
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

        public Builder commentsSum(int value) {
            mCommentsSum = value;
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
            publication.mCommentsSum = this.mCommentsSum;
            publication.mLikesSum = this.mLikesSum;

            return publication;
        }
    }*/
}
