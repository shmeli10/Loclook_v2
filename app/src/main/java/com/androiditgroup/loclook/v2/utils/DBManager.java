package com.androiditgroup.loclook.v2.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.models.Badge;
import com.androiditgroup.loclook.v2.models.QuizAnswer;
import com.androiditgroup.loclook.v2.models.User;

import java.io.File;
import java.util.ArrayList;

import static com.androiditgroup.loclook.v2.LocLookApp.context;

/**
 * Created by sostrovschi on 1/16/17.
 */

public class DBManager {
    private static DBManager mInstance;
    private SQLiteOpenHelper mOpenHelper;

    private DBManager(Context context) {

        final String DB_CACHE_DIR = context.getCacheDir().getPath() + File.separator + "dbcache" + File.separator;

        mOpenHelper = new SQLiteOpenHelper(context, DB_CACHE_DIR + Constants.DataBase.DATABASE_NAME, null, Constants.DataBase.DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                try {
                    // sqLiteDatabase.execSQL(Constants.DataBase.CREATE_USER_DATA_TABLE);
                    // sqLiteDatabase.execSQL(Constants.DataBase.CREATE_HISTORY_TABLE);

                    LocLookApp.showLog("DBManager: onCreate()");

                    createTable(sqLiteDatabase, Constants.DataBase.USER_TABLE,              Constants.DataBase.USER_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.BADGE_TABLE,             Constants.DataBase.BADGE_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.PUBLICATION_TABLE,       Constants.DataBase.PUBLICATION_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.QUIZ_ANSWER_TABLE,       Constants.DataBase.QUIZ_ANSWER_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.PHOTOS_TABLE,            Constants.DataBase.PHOTOS_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.USER_QUIZ_ANSWER_TABLE,  Constants.DataBase.USER_QUIZ_ANSWER_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.FAVORITES_TABLE,         Constants.DataBase.FAVORITES_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.COMMENTS_TABLE,          Constants.DataBase.COMMENTS_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.LIKES_TABLE,             Constants.DataBase.LIKES_TABLE_COLUMNS);

                    populateTables(sqLiteDatabase);
                } catch (Exception e) {
                    e.printStackTrace();
                    LocLookApp.showLog("DBManager: onCreate(): error: " +e.toString());
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                try {
                    // sqLiteDatabase.execSQL(Constants.DataBase.DROP_USER_DATA_TABLE);
                    // sqLiteDatabase.execSQL(Constants.DataBase.DROP_HISTORY_TABLE);

                    LocLookApp.showLog("DBManager: onUpgrade()");

                    onCreate(sqLiteDatabase);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        getDataBase();
    }

    public static synchronized DBManager getInstance() {
        if (mInstance == null)
            mInstance = new DBManager(LocLookApp.getInstance().getApplicationContext());
        return mInstance;
    }

    private void createTable(SQLiteDatabase db,String tableName, String columns) {
        LocLookApp.showLog("--- create table " + tableName + " ---");

        // String createQuery = "CREATE TABLE IF NOT EXISTS " +tableName+ " (" + "id integer primary key autoincrement, " +columns+ ");";

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("CREATE TABLE IF NOT EXISTS ");
        sqlQuery.append(tableName);
        sqlQuery.append(" (_ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlQuery.append(columns);
        sqlQuery.append(");");

        // создаем таблицу с полями
        db.execSQL(sqlQuery.toString());
    }

    /**
     * Return an instance of writable database
     */
    public SQLiteDatabase getDataBase() {
        return mOpenHelper.getWritableDatabase();
    }

    /**
     * Populate db tables by persistent data
     */
    private void populateTables(SQLiteDatabase db) {
        LocLookApp.showLog("DBManager: populateTables()");

        String[] badgesArr = context.getResources().getStringArray(R.array.badges);
        // String[] columnsArr = {"NAME", "IS_ENABLED"};
        String[] columnsArr = {"NAME"};

        for(int i=0; i<badgesArr.length; i++)
            // insertData(db, Constants.DataBase.BADGE_DATA_TABLE, columnsArr, new String[] {badgesArr[i], "0"});
            insertData(db, Constants.DataBase.BADGE_TABLE, columnsArr, new String[] {badgesArr[i]});

        showAllTableData(db, Constants.DataBase.BADGE_TABLE);
    }

    /**
     * Запрос на возвращение столбцов с нужными строками из таблицы
     *
     * @param table   Имя таблицы
     * @param columns Масив строк с именами нужных столбцов
     * @return Возвращает Cursor
     */

    // public Cursor queryColumns(String table, String... columns) {
    //public Cursor queryColumns(SQLiteDatabase db, String table, String... columns) {
    public Cursor queryColumns(SQLiteDatabase db, String table, String[] columns) {
        return db.query(table, columns, null, null, null, null, null);
    }

    public Cursor queryColumns(SQLiteDatabase db, String table, String[] column, String requestColumn, String where, String orderBy) {
        return db.query(table, column, requestColumn + "='" + where + "'", null, null, null, orderBy);
    }

    // public Cursor queryColumns(String table, String[] column, String requestColumn, String where) {
    public Cursor queryColumns(SQLiteDatabase db, String table, String[] column, String requestColumn, String where) {
        return db.query(table, column, requestColumn + "='" + where + "'", null, null, null, null);
    }

    public Cursor insertData(SQLiteDatabase db, String tableName, String[] columnsArr, String[] dataArr) {
        LocLookApp.showLog("DBManager: insertData(): tableName= " +tableName);

        Cursor result = null;

        ContentValues cv = new ContentValues();

        for(int i=0; i<columnsArr.length; i++)
            cv.put(columnsArr[i], dataArr[i]);

        if(cv.size() > 0) {
            // вставляем запись и получаем ее ID
            long rowID = db.insert(tableName, null, cv);

            // если идентификатор записи получен
            if (rowID > 0) {
                LocLookApp.showLog("row inserted, ID = " + rowID + " in table " + tableName);

                // получаем курсор с данными
                result = queryColumns(db, tableName, null, "_ID", String.valueOf(rowID));
            }
        }

        return result;
    }

    public void showAllTableData(SQLiteDatabase db,String table) {
        Cursor cursor = db.query(table, null, null, null, null, null, null);

        String[] cursorArr = cursor.getColumnNames();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            LocLookApp.showLog("----- Table: " + table + " -----");

            StringBuilder sb1 = new StringBuilder();

            for(int i=0; i<cursorArr.length; i++) {
                sb1.append("" + cursorArr[i] + " \t\t");
            }

            LocLookApp.showLog(sb1.toString());

            do {
                StringBuilder sb2 = new StringBuilder();

                for(int i=0; i<cursorArr.length; i++) {
                    sb2.append("" +cursor.getString(cursor.getColumnIndex(cursorArr[i])) + "\t\t");
                }

                LocLookApp.showLog(sb2.toString());

            } while (cursor.moveToNext());
        }

        cursor.close();

        LocLookApp.showLog("--------------------------------------------------");
    }

    public boolean deleteRows(SQLiteDatabase db, String table, String requestColumn, String where, boolean withUserId) {
        StringBuilder sb = new StringBuilder(requestColumn + "='" + where + "'");

        if(withUserId)
            sb.append(" AND USER_ID = '" +LocLookApp.appUserId+ "'");

        return db.delete(table, sb.toString(), null) > 0;
    }

    /**
     * Записывает данные в таблицу
     *
     * @param table   Имя таблицы
     * @param values  Данные для добавления
     * @param columns В какие столбцы добавить данные (должны быть в том порядке что и values)
     */
    public void insertInTable(String table, String[] values, String[] columns) {
        StringBuilder valueBuilder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (values.length == 1) {
                valueBuilder.append(values[i]);
            } else if (i == 0) {
                valueBuilder.append("");
                valueBuilder.append(values[i]);
                valueBuilder.append("'");
            } else if (i == values.length - 1) {
                valueBuilder.append(", '");
                valueBuilder.append(values[i]);
            } else {
                valueBuilder.append(", '");
                valueBuilder.append(values[i]);
                valueBuilder.append("'");
            }
        }
        String strValues = valueBuilder.toString();

        StringBuilder columnBuilder = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            if (i == 0) {
                columnBuilder.append("");
                columnBuilder.append(columns[i]);
            } else {
                columnBuilder.append(", ");
                columnBuilder.append(columns[i]);
            }
        }
        String strColumns = columnBuilder.toString();

        String insert = "INSERT OR REPLACE INTO " + table + " (" + strColumns + ") VALUES ('" + strValues + "');";
        ////Log.e(TAG, insert);
        try {
            getDataBase().execSQL(insert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete all the data from the table except users login data (password and id)
     * @param table table you want to delete from
     */
    public void deleteDB(String table) {
        deleteAllData(table);
    }

    /**
     * Used to delete all data from one table
     * @param table table you want to delete data from
     */
    public void deleteAllData(String table) {
        String delete = "DELETE FROM " + table;
        getDataBase().execSQL(delete);
    }

    private String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    // ---------------------------------- USER ------------------------------------------------ //

    /**
     * Запрос на создание пользователя
     * @param userName      Введенное пользователем имя
     * @param phoneNumber   Введенный пользователем номер телефона
     * @return Возвращаем курсор с данными созданного в БД пользователя
     */
    public Cursor createUser(String userName, String phoneNumber) {
        LocLookApp.showLog("DBManager: createUser()");

        // String[] columnsArr = {"NAME", "PHONE_NUMBER", "RATE"};
        // String[] dataArr    = {userName, phoneNumber, "0"};

        String[] columnsArr = {"NAME", "PHONE_NUMBER"};
        String[] dataArr    = {userName, phoneNumber};

        return insertData(getDataBase(), Constants.DataBase.USER_TABLE, columnsArr, dataArr);
    }

    // ---------------------------------- PUBLICATION ----------------------------------------- //

    // public Publication createPublication(String text, Badge badge, ArrayList<String> answersList, ArrayList<Bitmap> photosList, boolean isAnonymous) {
    public boolean createPublication(String text, Badge badge, ArrayList<String> answersList, ArrayList<Bitmap> photosList, boolean isAnonymous) {

        boolean result = false;

        Cursor pCursor = null;
        ArrayList<QuizAnswer> pQuizAnswersList = new ArrayList<>();

//        Publication.Builder mPublicationBuilder = null;
        getDataBase().beginTransaction();

        try {
            double latitude = 0.0;
            double longitude = 0.0;

            User user = LocLookApp.usersMap.get(LocLookApp.appUserId);

            if(user.getLocation() != null) {
                latitude  = user.getLocation().getLatitude();
                longitude = user.getLocation().getLongitude();
            }

            String[] pColumnsArr = {"TEXT", "AUTHOR_ID", "BADGE_ID", "CREATED_AT", "LATITUDE", "LONGITUDE", "REGION_NAME", "STREET_NAME",
                    "HAS_QUIZ", "HAS_IMAGES", "IS_ANONYMOUS"};
            String[] pDataArr    = {text, user.getId(), "" +badge.getId(), "" +System.currentTimeMillis(), "" +latitude, "" +longitude,
                    user.getRegionName(), user.getStreetName(),
                    (answersList.size() == 0)  ? "0" : "1", (photosList.size() == 0)  ? "0" : "1", (!isAnonymous) ? "0" : "1"};

            pCursor = insertData(getDataBase(), Constants.DataBase.PUBLICATION_TABLE, pColumnsArr, pDataArr);

            if((pCursor != null) && (pCursor.getCount() > 0)) {
                pCursor.moveToFirst();

                String pId = pCursor.getString(pCursor.getColumnIndex("_ID"));

                ///////////////////////////////////////////////////////////////////////////////////

                String[] qaColumnsArr = {"TEXT", "PUBLICATION_ID"};

                for(int i=0; i<answersList.size(); i++) {

                    String[] qaDataArr = {answersList.get(i), pId};

                    Cursor qaCursor = insertData(getDataBase(), Constants.DataBase.QUIZ_ANSWER_TABLE, qaColumnsArr, qaDataArr);

                    if((qaCursor != null) && (qaCursor.getCount() > 0)) {
                        qaCursor.moveToFirst();

                        String qaId     = qaCursor.getString(qaCursor.getColumnIndex("_ID"));
                        String qaText   = qaCursor.getString(qaCursor.getColumnIndex("TEXT"));

                        qaCursor.close();

                        QuizAnswer.Builder mQuizAnswerBuilder = new QuizAnswer.Builder()
                                .id(qaId)
                                .text(qaText);

                        pQuizAnswersList.add(mQuizAnswerBuilder.build());
                    }
                }

                ///////////////////////////////////////////////////////////////////////////////////

                for(int i=0; i<photosList.size(); i++) {

                    ContentValues cv = new  ContentValues();
                    cv.put("PHOTO",             DbBitmapUtility.getBytes(photosList.get(i)));
                    cv.put("PUBLICATION_ID",    pId);

                    // вставляем запись и получаем ее ID
                    long rowID = getDataBase().insert(Constants.DataBase.PHOTOS_TABLE, null, cv);

                    // если идентификатор записи получен
//                    if (rowID > 0) {
//                        LocLookApp.showLog("image inserted, ID = " + rowID + " in table " + Constants.DataBase.PHOTOS_TABLE);
//                    }
//                    else {

                        // если идентификатор записи получен
                    if (rowID <= 0) {
//                        LocLookApp.showLog("ERROR: image(" +i+ ") not inserted");
                        throw new SQLiteException();
                    }
                }
            }

            getDataBase().setTransactionSuccessful();
            result = true;
        } catch(SQLiteException sqliteExc) {
            LocLookApp.showLog("DBManager: createPublication(): sqliteExc: " +sqliteExc.toString());
        } catch(Exception exc) {
            //Error in between database transaction
            LocLookApp.showLog("DBManager: createPublication(): error: " +exc.toString());
        } finally {
            getDataBase().endTransaction();
            // return PublicationGenerator.getPublicationFromCursor(pCursor, pQuizAnswersList, photosList);
            return result;
        }
    }

    // --------------------------------- QUIZ ---- ---------------------------------------- //

    /**
     * Запрос на возвращение числовой раскладки по кол-ву выбранных ответов в опросе
     * @return Возвращает строки из таблицы
     */
    public Cursor getQuizAnswersSum(SQLiteDatabase db, String[] answersIdsArr) {
        // формируем запрос к БД
        String query = "SELECT QUIZ_ANSWER_ID, COUNT(*) QUIZ_SUM FROM " +Constants.DataBase.USER_QUIZ_ANSWER_TABLE+ " WHERE QUIZ_ANSWER_ID IN (" + makePlaceholders(answersIdsArr.length) + ") GROUP BY QUIZ_ANSWER_ID ";
//        LocLookApp.showLog("DBManager: getQuizAnswersSum(): query= " +query);

        return db.rawQuery(query, answersIdsArr);
    }

    /**
     * Запрос на сохранение выбранного пользователем ответа в опросе
     * @param quizAnswerId   Идентификатор выбранного пользователем ответа в опросе
     * @return Возвращаем курсор с данными добавленной записи
     */
    public Cursor saveSelectedQuizAnswer(String quizAnswerId) {
//        LocLookApp.showLog("DBManager: saveSelectedQuizAnswer()");

        String[] columnsArr = {"QUIZ_ANSWER_ID", "USER_ID"};
        String[] dataArr    = {quizAnswerId, LocLookApp.appUserId};

        return insertData(getDataBase(), Constants.DataBase.USER_QUIZ_ANSWER_TABLE, columnsArr, dataArr);
    }

    // --------------------------------- FAVORITES ----------------------------------------- //

    public Cursor addPublicationToFavorites(String publicationId) {

        String[] columnsArr = {"PUBLICATION_ID", "USER_ID"};
        String[] dataArr    = {publicationId, LocLookApp.appUserId};

        return insertData(getDataBase(), Constants.DataBase.FAVORITES_TABLE, columnsArr, dataArr);
    }

    public boolean deletePublicationFromFavorites(String publicationId) {
        return deleteRows(getDataBase(), Constants.DataBase.FAVORITES_TABLE, "PUBLICATION_ID", publicationId, true);
    }

    // --------------------------------- COMMENTS ------------------------------------------ //

    /**
     * Запрос для получения количества пользователей, который понравилась заданная публикация
     * @return
     */
    public Cursor getPublicationCommentsSum(SQLiteDatabase db, String publicationId) {
        String query = "SELECT COUNT(*) COMMENTS_SUM FROM " +Constants.DataBase.COMMENTS_TABLE+ " WHERE PUBLICATION_ID = " +publicationId;

        return db.rawQuery(query, null);
    }

    // --------------------------------- LIKES --------------------------------------------- //

    public Cursor addPublicationToLikes(String publicationId) {

        String[] columnsArr = {"PUBLICATION_ID", "USER_ID"};
        String[] dataArr    = {publicationId, LocLookApp.appUserId};

        return insertData(getDataBase(), Constants.DataBase.LIKES_TABLE, columnsArr, dataArr);
    }

    public boolean deletePublicationFromLikes(String publicationId) {
        return deleteRows(getDataBase(), Constants.DataBase.LIKES_TABLE, "PUBLICATION_ID", publicationId, true);
    }

    /**
     * Запрос для получения количества пользователей, который понравилась заданная публикация
     * @return
     */
    public Cursor getPublicationLikesSum(SQLiteDatabase db, String publicationId) {
        // String[] publicationIdArr = new String[]{publicationId};

        String query = "SELECT COUNT(*) LIKES_SUM FROM " +Constants.DataBase.LIKES_TABLE+ " WHERE PUBLICATION_ID = " +publicationId;

        return db.rawQuery(query, null);
    }
}
