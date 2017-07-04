package com.androiditgroup.loclook.v2.controllers;

import android.database.Cursor;
import android.text.TextUtils;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.models.Comment;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Serghei Ostrovschi on 15.04.2017.
 */
public class CommentController {

    private static CommentController commentController = null;

    private List<Comment> allCommentsList = new ArrayList<>();

    private Map<Integer, List<Comment>> sortedByPublicationIdCommentsMap   = new HashMap<>();
    private Map<Integer, List<Comment>> sortedByAuthorIdCommentsMap        = new HashMap<>();

    public static CommentController getCommentsControllerInstance()  {

        if(commentController == null) {
            commentController = new CommentController();
        }

        return commentController;
    }

    public void populateAllCommentsList() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("CommentController: populateAllCommentsList()");

        allCommentsList.clear();
        sortedByPublicationIdCommentsMap.clear();
        sortedByAuthorIdCommentsMap.clear();

        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(),
                                                             Constants.DataBase.COMMENTS_TABLE,
                                                             null, null, null, null);

        if((cursor != null) && (cursor.getCount() > 0)) {
            LocLookApp.showLog("CommentController: populateAllCommentsList(): cursor.getCount()= " + cursor.getCount());
            cursor.moveToFirst();

            try {

                do {

                    int commentPublicationId    = cursor.getInt(cursor.getColumnIndex("PUBLICATION_ID"));
                    int commentUserId           = cursor.getInt(cursor.getColumnIndex("USER_ID"));

                    Comment.Builder commentBuilder = new Comment.Builder();
                    commentBuilder.commentId(cursor.getInt(cursor.getColumnIndex("_ID")));
                    commentBuilder.commentPublicationId(commentPublicationId);
                    commentBuilder.commentAuthorId(commentUserId);
                    commentBuilder.commentRecipientId(cursor.getInt(cursor.getColumnIndex("RECIPIENT_ID")));
                    commentBuilder.commentText(cursor.getString(cursor.getColumnIndex("TEXT")));
                    commentBuilder.commentCreatedAt(cursor.getString(cursor.getColumnIndex("CREATED_AT")));

                    Comment comment = commentBuilder.build();

                    allCommentsList.add(comment);

                    if(sortedByPublicationIdCommentsMap.containsKey(commentPublicationId)) {

                        List<Comment> commentsList = sortedByPublicationIdCommentsMap.get(commentPublicationId);
                        commentsList.add(comment);
                    }
                    else {
                        List<Comment> commentsList = new ArrayList<>();
                        commentsList.add(comment);

                        sortedByPublicationIdCommentsMap.put(commentPublicationId, commentsList);
                    }


                    if(sortedByAuthorIdCommentsMap.containsKey(commentUserId)) {

                        List<Comment> commentsList = sortedByAuthorIdCommentsMap.get(commentUserId);
                        commentsList.add(comment);
                    }
                    else {
                        List<Comment> commentsList = new ArrayList<>();
                        commentsList.add(comment);

                        sortedByAuthorIdCommentsMap.put(commentUserId, commentsList);
                    }

                } while (cursor.moveToNext());

                LocLookApp.showLog("CommentController: populateAllCommentsList(): allCommentsList size= " +allCommentsList.size());

            } catch(Exception exc) {
                LocLookApp.showLog("CommentController: populateAllCommentsList(): error: " +exc.toString());
            } finally {
                cursor.close();
            }
        }
    }

    public int getPublicationCommentsSum(int publicationId) {

        if(sortedByPublicationIdCommentsMap.containsKey(publicationId)) {
            return sortedByPublicationIdCommentsMap.get(publicationId).size();
        }
        else
            return 0;
    }

    // public static ArrayList<String> getUserCommentedPublicationsList() {
    public ArrayList<String> getUserCommentedPublicationsList() {

        ArrayList<String> result = new ArrayList<>();

        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(),
                                                             Constants.DataBase.COMMENTS_TABLE,
                                                             null,
                                                             "USER_ID",
                                                             LocLookApp.appUserId,
                                                             "_ID");

        if((cursor != null) && (cursor.getCount() > 0)) {
            LocLookApp.showLog("CommentController: getUserCommentedPublicationsList(): cursor.getCount()= " +cursor.getCount());
            cursor.moveToFirst();

            try {

                do {
                    // получаем очередное значение из курсора
                    String publicationId = cursor.getString(cursor.getColumnIndex("PUBLICATION_ID"));

                    // если значение получено и его еще нет в списке
                    if((!TextUtils.isEmpty(publicationId)) && (!result.contains(publicationId))) {
                        // добавляем его в список
                        result.add(publicationId);
                    }

                } while (cursor.moveToNext());

            } catch(Exception exc) {
                LocLookApp.showLog("CommentController: getUserCommentedPublicationsList(): error: " +exc.toString());
            } finally {
                cursor.close();
            }
        }

        return result;
    }
}
