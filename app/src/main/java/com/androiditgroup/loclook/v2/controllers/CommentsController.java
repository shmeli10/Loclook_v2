package com.androiditgroup.loclook.v2.controllers;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.models.Comment;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by OS1 on 15.04.2017.
 */
public class CommentsController {

    private static CommentsController commentsController = null;

    private List<Comment> allCommentsList = new ArrayList<>();

    private Map<Integer, List<Comment>> sortedByPublicationIdCommentsMap   = new HashMap<>();
    private Map<Integer, List<Comment>> sortedByAuthorIdCommentsMap        = new HashMap<>();

    public static CommentsController getCommentsControllerInstance()  {

        if(commentsController == null) {
            commentsController = new CommentsController();
        }

        return commentsController;
    }

    public void populateAllCommentsList() {
        Log.e("LOG", "-------------------------------------");
        Log.e("LOG", "CommentsController: populateAllCommentsList()");

        allCommentsList.clear();

        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(),
                                                             Constants.DataBase.COMMENTS_TABLE,
                                                             null, null, null, null);

        if((cursor != null) && (cursor.getCount() > 0)) {
            LocLookApp.showLog("CommentsController: populateAllCommentsList(): cursor.getCount()= " + cursor.getCount());
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

                Log.e("LOG", "CommentsController: populateAllCommentsList(): allCommentsList size= " +allCommentsList);

            } catch(Exception exc) {
                LocLookApp.showLog("CommentsController: getUserCommentedPublicationsList(): error: " +exc.toString());
            } finally {
                cursor.close();
            }
        }
    }

    public int getCommentsSum(String publicationId) {

        if(sortedByPublicationIdCommentsMap.containsKey(Integer.parseInt(publicationId))) {
            return sortedByPublicationIdCommentsMap.get(Integer.parseInt(publicationId)).size();
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
            LocLookApp.showLog("CommentsController: getUserCommentedPublicationsList(): cursor.getCount()= " +cursor.getCount());
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
                LocLookApp.showLog("CommentsController: getUserCommentedPublicationsList(): error: " +exc.toString());
            } finally {
                cursor.close();
            }
        }

        return result;
    }
}
