package com.androiditgroup.loclook.v2.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.models.Badge;
import com.androiditgroup.loclook.v2.models.Publication;
import com.androiditgroup.loclook.v2.models.QuizAnswer;
import com.androiditgroup.loclook.v2.models.User;

import java.util.ArrayList;
import java.util.Arrays;

import static com.androiditgroup.loclook.v2.LocLookApp.context;

/**
 * Created by sostrovschi on 1/16/17.
 */

public class DBManager {
    private static DBManager mInstance;
    private SQLiteOpenHelper mOpenHelper;

    private DBManager(Context context) {
        mOpenHelper = new SQLiteOpenHelper(context, Constants.DB_CACHE_DIR + Constants.DataBase.DATABASE_NAME, null, Constants.DataBase.DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                try {
                    // sqLiteDatabase.execSQL(Constants.DataBase.CREATE_USER_DATA_TABLE);
                    // sqLiteDatabase.execSQL(Constants.DataBase.CREATE_HISTORY_TABLE);

                    Log.e("ABC", "DBManager: onCreate()");

                    createTable(sqLiteDatabase, Constants.DataBase.USER_TABLE, Constants.DataBase.USER_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.BADGE_TABLE, Constants.DataBase.BADGE_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.PUBLICATION_TABLE, Constants.DataBase.PUBLICATION_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.QUIZ_ANSWER_TABLE, Constants.DataBase.QUIZ_ANSWER_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.PHOTOS_TABLE, Constants.DataBase.PHOTOS_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.USER_QUIZ_ANSWER_TABLE, Constants.DataBase.USER_QUIZ_ANSWER_TABLE_COLUMNS);

                    populateTables(sqLiteDatabase);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("ABC", "DBManager: onCreate(): error: " +e.toString());
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                try {
                    // sqLiteDatabase.execSQL(Constants.DataBase.DROP_USER_DATA_TABLE);
                    // sqLiteDatabase.execSQL(Constants.DataBase.DROP_HISTORY_TABLE);

                    Log.e("ABC", "DBManager: onUpgrade()");

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
        Log.e("ABC", "--- create table " + tableName + " ---");

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
        Log.e("ABC", "DBManager: populateTables()");

        String[] badgesArr = context.getResources().getStringArray(R.array.badges);
        // String[] columnsArr = {"NAME", "IS_ENABLED"};
        String[] columnsArr = {"NAME"};

        // Log.e("ABC", "DBManager: populateTables(): badgesArr length = " +badgesArr.length);

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
    public Cursor queryColumns(SQLiteDatabase db, String table, String... columns) {
        return db.query(table, columns, null, null, null, null, null);
    }

    // public Cursor queryColumns(String table, String[] column, String requestColumn, String where) {
    public Cursor queryColumns(SQLiteDatabase db, String table, String[] column, String requestColumn, String where) {
        return db.query(table, column, requestColumn + "='" + where + "'", null, null, null, null);
    }

    /**
     * Запрос на возвращение строк из таблицы
     *
     * @param sql           SQL запрос. Щн не должен заканчиваться с ;
     * @param selectionArgs Можете включить ?s в WHERE и он будет заменятся на selectionArgs.
     * @return Возвращает строки из таблицы
     */
//    public Cursor queryRows(SQLiteDatabase database, String sql, String[] selectionArgs) {
//        return database.rawQuery(sql, selectionArgs);
//    }

    /**
     * Запрос на создание пользователя
     * @param userName      Введенное пользователем имя
     * @param phoneNumber   Введенный пользователем номер телефона
     * @return Возвращаем идентификатор пользователя
     */
    public Cursor createUser(String userName, String phoneNumber) {
        // Log.e("ABC", "DBManager: createUser()");

        String[] columnsArr = {"NAME", "PHONE_NUMBER", "RATE"};
        String[] dataArr    = {userName, phoneNumber, "0"};

        return insertData(getDataBase(), Constants.DataBase.USER_TABLE, columnsArr, dataArr);
    }

    public Publication createPublication(String text, Badge badge, ArrayList<String> answersList, ArrayList<Bitmap> photosList, boolean isAnonymous) {

        Publication.Builder mPublicationBuilder = null;
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

            Cursor pCursor = insertData(getDataBase(), Constants.DataBase.PUBLICATION_TABLE, pColumnsArr, pDataArr);

//            Log.e("ABC", "DBManager: createPublication(): pCursor is null:" +(pCursor == null));

            if((pCursor != null) && (pCursor.getCount() > 0)) {
//                Log.e("ABC", "DBManager: createPublication(): pCursor.getCount()= " +pCursor.getCount());
                pCursor.moveToFirst();

                String pId = pCursor.getString(pCursor.getColumnIndex("_ID"));
                String pText = pCursor.getString(pCursor.getColumnIndex("TEXT"));
                String pAuthorId = pCursor.getString(pCursor.getColumnIndex("AUTHOR_ID"));
                String pCreatedAt = pCursor.getString(pCursor.getColumnIndex("CREATED_AT"));
                String pLatitude = pCursor.getString(pCursor.getColumnIndex("LATITUDE"));
                String pLongitude = pCursor.getString(pCursor.getColumnIndex("LONGITUDE"));
                String pRegionName = pCursor.getString(pCursor.getColumnIndex("REGION_NAME"));
                String pStreetName = pCursor.getString(pCursor.getColumnIndex("STREET_NAME"));

                int pBadgeId = pCursor.getInt(pCursor.getColumnIndex("BADGE_ID"));

                boolean pHasQuiz = (pCursor.getInt(pCursor.getColumnIndex("HAS_QUIZ")) == 1);
                boolean pHasImages = (pCursor.getInt(pCursor.getColumnIndex("HAS_IMAGES")) == 1);
                boolean pIsAnonymous = (pCursor.getInt(pCursor.getColumnIndex("IS_ANONYMOUS")) == 1);

                pCursor.close();

//                Log.e("ABC", "DBManager: createPublication(): pId= " +pId);
//                Log.e("ABC", "DBManager: createPublication(): pLatitude= " +pLatitude);
//                Log.e("ABC", "DBManager: createPublication(): pLongitude= " +pLongitude);

                Location pLocation = new Location("");

                if(pLatitude != null)
                    pLocation.setLatitude(Double.parseDouble(pLatitude));

                if(pLongitude != null)
                    pLocation.setLongitude(Double.parseDouble(pLongitude));

                ///////////////////////////////////////////////////////////////////////////////////

                ArrayList<QuizAnswer> pQuizAnswerList = new ArrayList<>();

                String[] qaColumnsArr = {"TEXT", "PUBLICATION_ID"};

                for(int i=0; i<answersList.size(); i++) {

                    String[] qaDataArr = {answersList.get(i), pId};

                    Cursor qaCursor = insertData(getDataBase(), Constants.DataBase.QUIZ_ANSWER_TABLE, qaColumnsArr, qaDataArr);

                    // if(qaCursor != null) {
                    if((qaCursor != null) && (qaCursor.getCount() > 0)) {
                        qaCursor.moveToFirst();

                        String qaId     = qaCursor.getString(qaCursor.getColumnIndex("_ID"));
                        String qaText   = qaCursor.getString(qaCursor.getColumnIndex("TEXT"));
//                        Log.e("ABC", "DBManager: createPublication(): new qaId: " +qaId);
//                        Log.e("ABC", "DBManager: createPublication(): new qaText: " +qaText);

                        qaCursor.close();

                        QuizAnswer.Builder mQuizAnswerBuilder = new QuizAnswer.Builder()
                                .id(qaId)
                                .text(qaText);

                        pQuizAnswerList.add(mQuizAnswerBuilder.build());
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
                    if (rowID > 0) {
                        Log.e("ABC", "image inserted, ID = " + rowID + " in table " + Constants.DataBase.PHOTOS_TABLE);
                    }
                    else {
                        Log.e("ABC", "ERROR: image(" +i+ ") not inserted");
                        throw new SQLiteException();
                    }
                }

                ///////////////////////////////////////////////////////////////////////////////////

//                mPublicationBuilder = new Publication.Builder()
//                        .id(pId)
//                        .text(pText)
//                        .authorId(pAuthorId)
//                        .badge(LocLookApp.badgesList.get(pBadgeId))
//                        .createdAt(pCreatedAt)
//                        .location(pLocation)
//                        .regionName(pRegionName)
//                        .streetName(pStreetName)
//                        .hasQuiz(pHasQuiz)
//                        .hasImages(pHasImages)
//                        .isAnonymous(pIsAnonymous)
//                        .quizAnswerList(pQuizAnswerList)
//                        .photosList(photosList);
            }

            getDataBase().setTransactionSuccessful();
        } catch(SQLiteException sqliteExc) {
            Log.e("ABC", "DBManager: createPublication(): sqliteExc: " +sqliteExc.toString());
        } catch(Exception exc) {
            //Error in between database transaction
            Log.e("ABC", "DBManager: createPublication(): error: " +exc.toString());
        } finally {
            getDataBase().endTransaction();

            if(mPublicationBuilder != null) {
                Log.e("ABC", "DBManager: createPublication(): publication is saved");

                // Publication.Builder mPublicationBuilder = new Publication.Builder();
                return mPublicationBuilder.build();
            }
            else {
                Log.e("ABC", "DBManager: createPublication(): publication is not saved");
                return null;
            }
        }
    }

    public Cursor insertData(SQLiteDatabase db, String tableName, String[] columnsArr, String[] dataArr) {
        Log.e("ABC", "DBManager: insertData(): tableName= " +tableName);

        Cursor result = null;

        ContentValues cv = new ContentValues();

        for(int i=0; i<columnsArr.length; i++)
            cv.put(columnsArr[i], dataArr[i]);

        if(cv.size() > 0) {
            // вставляем запись и получаем ее ID
            long rowID = db.insert(tableName, null, cv);

            // если идентификатор записи получен
            if (rowID > 0) {
                Log.e("ABC", "row inserted, ID = " + rowID + " in table " + tableName);

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

            Log.e("ABC", "----- Table: " + table + " -----");

            StringBuilder sb1 = new StringBuilder();

            for(int i=0; i<cursorArr.length; i++) {
                sb1.append("" + cursorArr[i] + " \t\t");
            }

            Log.e("ABC", sb1.toString());

            do {
                StringBuilder sb2 = new StringBuilder();

                for(int i=0; i<cursorArr.length; i++) {
                    sb2.append("" +cursor.getString(cursor.getColumnIndex(cursorArr[i])) + "\t\t");
                }

                Log.e("ABC", sb2.toString());

            } while (cursor.moveToNext());
        }

        cursor.close();

        Log.e("ABC", "--------------------------------------------------");
    }

    /**
     * Записывает данные в таблицу
     *
     * @param table   Имя таблицы
     * @param values  Данные для добавления
     * @param columns В какие столбцы добавить данные (должны быть в том порядке что и values)
     */
//    public void insertInTable(String table, String[] values, String[] columns) {
//        StringBuilder valueBuilder = new StringBuilder();
//        for (int i = 0; i < values.length; i++) {
//            if (values.length == 1) {
//                valueBuilder.append(values[i]);
//            } else if (i == 0) {
//                valueBuilder.append("");
//                valueBuilder.append(values[i]);
//                valueBuilder.append("'");
//            } else if (i == values.length - 1) {
//                valueBuilder.append(", '");
//                valueBuilder.append(values[i]);
//            } else {
//                valueBuilder.append(", '");
//                valueBuilder.append(values[i]);
//                valueBuilder.append("'");
//            }
//        }
//        String strValues = valueBuilder.toString();
//
//        StringBuilder columnBuilder = new StringBuilder();
//        for (int i = 0; i < columns.length; i++) {
//            if (i == 0) {
//                columnBuilder.append("");
//                columnBuilder.append(columns[i]);
//            } else {
//                columnBuilder.append(", ");
//                columnBuilder.append(columns[i]);
//            }
//        }
//        String strColumns = columnBuilder.toString();
//
//        String insert = "INSERT OR REPLACE INTO " + table + " (" + strColumns + ") VALUES ('" + strValues + "');";
//        ////Log.e(TAG, insert);
//        try {
//            getDataBase().execSQL(insert);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Delete all the data from the table except users login data (password and id)
     * @param table table you want to delete from
     */
//    public void deleteDB(String table) {
//        deleteAllData(table);
//    }

    /**
     * Used to delete all data from one table
     * @param table table you want to delete data from
     */
//    public void deleteAllData(String table) {
//        String delete = "DELETE FROM " + table;
//        getDataBase().execSQL(delete);
//    }

    /**
     * Used to delete one row from table
     * @param table table you want to delete from
     * @param column column from where to search the requested row
     * @param where data within the row that you want to delete
     */
//    public void deleteRow(String table, String column, String... where) {
//        String delete = "DELETE FROM " + table + " WHERE " + column + " = \"" + Arrays.toString(where).replace("[", "").replace("]", "") + "\"";
//        getDataBase().execSQL(delete);
//    }

    /**
     * Check if in the database already exist data with the given key
     * @param request URL from your request, it's used like primary key
     * @return true if there are data with the given key (request URL) or false instead
     */
//    public static boolean isDataAlreadyLoaded(URL request) {
//        String[] columns = {Constants.DataBase.REQUEST};
//        String json = null;
//        Cursor cursor = DBManager.getInstance().queryColumns(Constants.DataBase.DATA_TABLE, columns, Constants.DataBase.REQUEST, request.toString());
//        if (cursor.getCount() != -1) {
//            while (cursor.moveToNext()) {
//                if (cursor.isLast()) {
//                    int index = cursor.getColumnIndex(Constants.DataBase.REQUEST);
//                    json = cursor.getString(index);
//                }
//            }
//        }
//        cursor.close();
//        return request.toString().equalsIgnoreCase(json);
//    }


    /**
     * Return the last inserted user token
     * @return String that contains the last inserted user token
     */
//    public static String getToken() {
//        String[] columns = {Constants.DataBase.RESPONSE};
//        String response = null;
//        String token = null;
//        try {
//            Cursor cursor = DBManager.getInstance().queryColumns(Constants.DataBase.DATA_TABLE, columns, Constants.REQUEST, YellowApp.api.login().toString());
//            while (cursor.moveToNext()) {
//                if (cursor.isLast()) {
//                    int index = cursor.getColumnIndex(Constants.RESPONSE);
//                    response = cursor.getString(index);
//                }
//            }
//            cursor.close();
//            if (response != null) {
//                JSONObject data = new JSONObject(response);
//                token = data.getString("ResponseData");
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return token;
//    }

//    public static ArrayList<String> loadHistory() throws Exception {
//        Cursor cursor = DBManager.getInstance().queryColumns(Constants.DataBase.HISTORY_TABLE, Constants.DataBase.RESPONSE, Constants.DataBase.DATE);
//        ArrayList<String> histories = new ArrayList<>();
//        while (cursor.moveToNext()) {
//            int responseIndex = cursor.getColumnIndex(Constants.DataBase.RESPONSE);
//            int dateIndex = cursor.getColumnIndex(Constants.DataBase.DATE);
//            String responseData = cursor.getString(responseIndex);
//            String date = cursor.getString(dateIndex);
//            JSONObject data = new JSONObject(responseData);
//            data.put("visitedDate", date);
//            histories.add(data.toString());
//        }
//        cursor.close();
//        return histories;
//    }

    /**
     * Search the data in database using the given key
     * @param request the request url is used like primary key
     * @return a last inserted JSON with the given key (request URL)
     */
//    public static String loadDataFromDB(URL request) {
//        String[] columns = {Constants.DataBase.RESPONSE, Constants.DataBase.OTHER};
//        String response = null;
//        String other = null;
//        JSONObject data = null;
//        Cursor cursor = DBManager.getInstance().queryColumns(Constants.DataBase.DATA_TABLE, columns, Constants.DataBase.REQUEST, request.toString());
//        if (cursor.getCount() != -1) {
//            while (cursor.moveToNext()) {
//                if (cursor.isLast()) {
//                    int responseIndex = cursor.getColumnIndex(Constants.DataBase.RESPONSE);
//                    int otherIndex = cursor.getColumnIndex(Constants.DataBase.OTHER);
//                    response = cursor.getString(responseIndex);
//                    other = cursor.getString(otherIndex);
//                }
//            }
//        }
//        cursor.close();
//        if (other != null && response != null) {
//            try {
//                JSONObject object = new JSONObject(other);
//                data = new JSONObject(response);
//                data.put("ContractId", object.getString("ContractId"));
//                data.put("Password", object.getString("Password"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (data != null) {
//            return data.toString();
//        } else {
//            return "---";
//        }
//    }
}
