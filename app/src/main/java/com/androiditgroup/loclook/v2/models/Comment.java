package com.androiditgroup.loclook.v2.models;

/**
 * Created by sostrovschi on 2/20/17.
 */

public class Comment {

    private int commentId;
    private int commentPublicationId;
    private int commentAuthorId;        // USER_ID
    private int commentRecipientId;     // RECIPIENT_ID

    private String commentText;
    private String commentCreatedAt;

    private Comment() { }

    public int getCommentId() {
        return commentId;
    }

    public int getCommentPublicationId() {
        return commentPublicationId;
    }

    public int getCommentAuthorId() {
        return commentAuthorId;
    }

    public int getCommentRecipientId() {
        return commentRecipientId;
    }

    public String getCommentText() {
        return commentText;
    }

    public String getCommentCreatedAt() {
        return commentCreatedAt;
    }

    public static class Builder {
        private int commentId;
        private int commentPublicationId;
        private int commentAuthorId;        // USER_ID
        private int commentRecipientId;     // RECIPIENT_ID

        private String commentText;
        private String commentCreatedAt;

        public Comment.Builder commentId(int value) {
            commentId = value;
            return this;
        }

        public Comment.Builder commentPublicationId(int value) {
            commentPublicationId = value;
            return this;
        }

        public Comment.Builder commentAuthorId(int value) {
            commentAuthorId = value;
            return this;
        }

        public Comment.Builder commentRecipientId(int value) {
            commentRecipientId = value;
            return this;
        }

        public Comment.Builder commentText(String value) {
            commentText = value;
            return this;
        }

        public Comment.Builder commentCreatedAt(String value) {
            commentCreatedAt = value;
            return this;
        }

        public Comment build() {
            Comment comment = new Comment();

            comment.commentId               = this.commentId;
            comment.commentPublicationId    = this.commentPublicationId;
            comment.commentAuthorId         = this.commentAuthorId;
            comment.commentRecipientId      = this.commentRecipientId;
            comment.commentText             = this.commentText;
            comment.commentCreatedAt        = this.commentCreatedAt;

            return comment;
        }
    }
}
