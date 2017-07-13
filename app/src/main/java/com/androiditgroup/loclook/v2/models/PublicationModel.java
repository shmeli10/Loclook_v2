package com.androiditgroup.loclook.v2.models;

import com.androiditgroup.loclook.v2.constants.ErrorConstants;

/**
 * Created by Serghei Ostrovschi on 7/11/16.
 */

public class PublicationModel {

    private int publicationId;
    private int publicationAuthorId;
    private int publicationBadgeId;

    private String publicationCreatedAt;
    private String publicationLatitude;
    private String publicationLongitude;
    private String publicationRegionName;
    private String publicationStreetName;
    private String publicationText;

    private boolean publicationHasQuiz;
    private boolean publicationHasImages;
    private boolean publicationIsAnonymous;

    // ------------------------------------- GETTERS ----------------------------------------- //


    public int getPublicationId() {
        return publicationId;
    }

    public int getPublicationAuthorId() {
        return publicationAuthorId;
    }

    public int getPublicationBadgeId() {
        return publicationBadgeId;
    }

    public String getPublicationCreatedAt() {
        return publicationCreatedAt;
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

    public String getPublicationText() {
        return publicationText;
    }

    public boolean isPublicationHasQuiz() {
        return publicationHasQuiz;
    }

    public boolean isPublicationHasImages() {
        return publicationHasImages;
    }

    public boolean isPublicationAnonymous() {
        return publicationIsAnonymous;
    }

    // ------------------------------------- SETTERS ----------------------------------------- //


    public void setPublicationId(int publicationId) throws Exception {
        if(publicationId > 0)
            this.publicationId = publicationId;
        else
            throw new Exception(ErrorConstants.PUBLICATION_ID_ERROR);
    }

    public void setPublicationAuthorId(int publicationAuthorId) throws Exception {
        if(publicationAuthorId > 0)
            this.publicationAuthorId = publicationAuthorId;
        else
            throw new Exception(ErrorConstants.AUTHOR_ID_ERROR);
    }

    public void setPublicationBadgeId(int publicationBadgeId) throws Exception {
        if(publicationBadgeId > 0)
            this.publicationBadgeId = publicationBadgeId;
        else
            throw new Exception(ErrorConstants.BADGE_ID_ERROR);
    }

    public void setPublicationCreatedAt(String publicationCreatedAt) throws Exception {
        if(publicationCreatedAt != null)
            this.publicationCreatedAt = publicationCreatedAt;
        else
            throw new Exception(ErrorConstants.CREATED_AT_NULL_ERROR);
    }

    public void setPublicationLatitude(String publicationLatitude) throws Exception {
        if(publicationLatitude != null)
            this.publicationLatitude = publicationLatitude;
        else
            throw new Exception(ErrorConstants.LATITUDE_NULL_ERROR);
    }

    public void setPublicationLongitude(String publicationLongitude) throws Exception {
        if(publicationLongitude != null)
            this.publicationLongitude = publicationLongitude;
        else
            throw new Exception(ErrorConstants.LONGITUDE_NULL_ERROR);
    }

    public void setPublicationRegionName(String publicationRegionName) throws Exception {
        if(publicationRegionName != null)
            this.publicationRegionName = publicationRegionName;
        else
            throw new Exception(ErrorConstants.REGION_NAME_NULL_ERROR);
    }

    public void setPublicationStreetName(String publicationStreetName) throws Exception {
        if(publicationStreetName != null)
            this.publicationStreetName = publicationStreetName;
        else
            throw new Exception(ErrorConstants.STREET_NAME_NULL_ERROR);
    }

    public void setPublicationText(String publicationText) throws Exception {
        if(publicationText != null)
            this.publicationText = publicationText;
        else
            throw new Exception(ErrorConstants.PUBLICATION_TEXT_NULL_ERROR);
    }

    public void setPublicationHasQuiz(boolean publicationHasQuiz) {
        this.publicationHasQuiz = publicationHasQuiz;
    }

    public void setPublicationHasImages(boolean publicationHasImages) {
        this.publicationHasImages = publicationHasImages;
    }

    public void setPublicationIsAnonymous(boolean publicationIsAnonymous) {
        this.publicationIsAnonymous = publicationIsAnonymous;
    }
}
