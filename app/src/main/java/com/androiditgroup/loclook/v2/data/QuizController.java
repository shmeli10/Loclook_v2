package com.androiditgroup.loclook.v2.data;

import android.database.Cursor;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.models.Quiz;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Serghei Ostrovschi on 7/4/17.
 */

public class QuizController {

    private static QuizController quizController = null;

    private Map<Integer, Quiz> sortedByPublicationIdQuizeMap = new HashMap<>();

    public static QuizController getQuizControllerInstance()  {

        if(quizController == null) {
            quizController = new QuizController();
        }

        return quizController;
    }

    public void populateSortedByPublicationIdLikesMap() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("LikeController: populateSortedByPublicationIdLikesMap()");

        sortedByPublicationIdQuizeMap.clear();

        // select distinct publicationId
        Cursor cursor = DBManager.getInstance().getDataBase().query(true,
                                                                    Constants.DataBase.QUIZ_ANSWER_TABLE,
                                                                    new String[]{"PUBLICATION_ID"},
                                                                    null, null, null, null, null, null);

//        Cursor cursor = DBManager.getInstance().queryColumns(DBManager.getInstance().getDataBase(),
//                Constants.DataBase.LIKES_TABLE,
//                null, null, null, null);

    }
}
