package com.androiditgroup.loclook.v2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.interfaces.DatabaseCreateInterface;

/**
 * Created by Serghei Ostrovschi on 7/5/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private DatabaseCreateInterface databaseCreateListener;

    private boolean createDataBaseSuccess = false;

    /**
     * DatabaseHandler constructor.
     *
     * @param context	application context.
     */
    public DatabaseHandler(Context context, DatabaseCreateInterface databaseCreateListener) throws Exception {
        super(context,
              DatabaseConstants.DATABASE_NAME,
              null,
              DatabaseConstants.DATABASE_VERSION);

        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("DatabaseHandler: constructor.");

        if(databaseCreateListener == null)
            throw new Exception(ErrorConstants.DATABASE_CREATE_INTERFACE_NULL_ERROR);

        this.databaseCreateListener = databaseCreateListener;
    }

    // ---------------------------------- QUERY ------------------------------------------- //

    /**
     * Method gets cursor with specified table columns
     * @param sqLiteDatabase	link on instance of {@link SQLiteDatabase} class
     * @param tableName         specified table name
     * @param columns           specified column(s)
     * @return                  cursor with specified table columns
     */
    public Cursor queryColumns(SQLiteDatabase   sqLiteDatabase,
                               String           tableName,
                               String...        columns) {

        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("DatabaseHandler: queryColumns(): table: " +tableName);

        return sqLiteDatabase.query(tableName,
                                    columns,
                                    null, null, null, null, null);
    }

    /**
     * Method gets cursor with specified table columns and where condition
     *
     * @param sqLiteDatabase	link on instance of {@link SQLiteDatabase} class
     * @param tableName         specified table name
     * @param columns           specified column(s)
     * @param conditionColumn   condition column
     * @param conditionValue    condition value
     * @return                  cursor with specified table columns and where condition
     */
    public Cursor queryColumns(SQLiteDatabase   sqLiteDatabase,
                               String           tableName,
                               String[]         columns,
                               String           conditionColumn,
                               String           conditionValue) {

        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("DatabaseHandler: queryColumns(): table: " +tableName);

        return sqLiteDatabase.query(tableName,
                                    columns,
                                    conditionColumn + "='" + conditionValue + "'",
                                    null, null, null, null);
    }

    /**
     * Method gets cursor with specified table columns and where condition
     *
     * @param sqLiteDatabase	link on instance of {@link SQLiteDatabase} class
     * @param tableName         specified table name
     * @param columns           specified column(s)
     * @param conditionColumn   condition column
     * @param conditionValue    condition value
     * @param orderBy           order flag (ASC/DESC)
     * @return
     */
    public Cursor queryColumns(SQLiteDatabase   sqLiteDatabase,
                               String           tableName,
                               String[]         columns,
                               String           conditionColumn,
                               String           conditionValue,
                               String           orderBy) {

        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("DatabaseHandler: queryColumns(): table: " +tableName);

        return sqLiteDatabase.query(tableName,
                                    columns,
                                    conditionColumn + "='" + conditionValue + "'",
                                    null, null, null,
                                    orderBy);
    }

    // ---------------------------------------------------------------------------------------- //

    /**
     * Method creates tables for content and EPG in SQLite Database
     *
     * @param sqLiteDatabase	link on instance of {@link SQLiteDatabase} class
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("DatabaseHandler: onCreate()");

        createTable(sqLiteDatabase, DatabaseConstants.USER_TABLE,              DatabaseConstants.USER_TABLE_COLUMNS);
        createTable(sqLiteDatabase, DatabaseConstants.BADGE_TABLE,             DatabaseConstants.BADGE_TABLE_COLUMNS);
        createTable(sqLiteDatabase, DatabaseConstants.PUBLICATION_TABLE,       DatabaseConstants.PUBLICATION_TABLE_COLUMNS);
        createTable(sqLiteDatabase, DatabaseConstants.QUIZ_ANSWER_TABLE,       DatabaseConstants.QUIZ_ANSWER_TABLE_COLUMNS);
        createTable(sqLiteDatabase, DatabaseConstants.PHOTOS_TABLE,            DatabaseConstants.PHOTOS_TABLE_COLUMNS);
        createTable(sqLiteDatabase, DatabaseConstants.USER_QUIZ_ANSWER_TABLE,  DatabaseConstants.USER_QUIZ_ANSWER_TABLE_COLUMNS);
        createTable(sqLiteDatabase, DatabaseConstants.FAVORITES_TABLE,         DatabaseConstants.FAVORITES_TABLE_COLUMNS);
        createTable(sqLiteDatabase, DatabaseConstants.COMMENTS_TABLE,          DatabaseConstants.COMMENTS_TABLE_COLUMNS);
        createTable(sqLiteDatabase, DatabaseConstants.LIKES_TABLE,             DatabaseConstants.LIKES_TABLE_COLUMNS);

        // LocLookApp.showLog("TABLES CREATED");

        if(createDataBaseSuccess)
            databaseCreateListener.onDatabaseCreateSuccess();
    }

    /**
     * Method upgrades SQLite Database.
     *
     * @param sqLiteDatabase	link on instance of {@link SQLiteDatabase} class
     * @param oldVersion 		current version of SQLite Database.
     * @param newVersion		new version of SQLite Database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase    sqLiteDatabase,
                          int               oldVersion,
                          int               newVersion) {

        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("DatabaseHandler: onUpgrade()");

        try {
            onCreate(sqLiteDatabase);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method creates specified table (if it's not exists yet)
     *
     * @param sqLiteDatabase	link on instance of {@link SQLiteDatabase} class
     * @param tableName         specified table name
     * @param columns           specified column(s)
     */
    private void createTable(SQLiteDatabase sqLiteDatabase,
                             String         tableName,
                             String         columns) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("DatabaseHandler:  createTable(): table: " + tableName);

        try {
            StringBuilder sqlQuery = new StringBuilder();
            sqlQuery.append("CREATE TABLE IF NOT EXISTS ");
            sqlQuery.append(tableName);
            sqlQuery.append(" (_ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sqlQuery.append(columns);
            sqlQuery.append(");");

            // создаем таблицу с полями
            sqLiteDatabase.execSQL(sqlQuery.toString());

            createDataBaseSuccess = true;
        }
        catch(Exception exc) {
            databaseCreateListener.onDatabaseCreateError(exc.getMessage() + ", table: " +tableName);
            createDataBaseSuccess = false;
        }
    }

    /**
     * Method inserts one row in specified table and return cursor with it
     *
     * @param sqLiteDatabase	link on instance of {@link SQLiteDatabase} class
     * @param tableName         specified table name
     * @param columnsArray      specified column(s)
     * @param dataArray         specified data for column(s)
     * @return                  cursor with data of inserted row
     */
    public Cursor insertRow(SQLiteDatabase  sqLiteDatabase,
                            String          tableName,
                            String[]        columnsArray,
                            String[]        dataArray) {

        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("DatabaseHandler: insertRow(): tableName= " +tableName);

        Cursor result = null;

        ContentValues cv = new ContentValues();

        for(int i=0; i<columnsArray.length; i++)
            cv.put(columnsArray[i],
                   dataArray[i]
            );

        if(cv.size() > 0) {
            // вставляем запись и получаем ее ID
            long rowID = sqLiteDatabase.insert(tableName,
                                               null,
                                               cv);

            // если идентификатор записи получен
            if (rowID > 0) {
                LocLookApp.showLog("row inserted, ID = " + rowID + " tableName= " + tableName);

                // получаем курсор с данными
                result = queryColumns(sqLiteDatabase,
                                      tableName,
                                      null,
                                      "_ID",
                                      String.valueOf(rowID));
            }
        }

        return result;
    }

    /**
     * Method deletes all rows in specified table
     *
     * @param sqLiteDatabase	link on instance of {@link SQLiteDatabase} class
     * @param tableName         specified table name
     * @return                  answer is all rows deleted successfully
     */
    public boolean deleteAllRows(SQLiteDatabase sqLiteDatabase,
                                 String         tableName) {

        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("DatabaseHandler: deleteAllRows(): tableName= " +tableName);

        return sqLiteDatabase.delete(tableName,
                                     null, null) > 0;
    }

    /**
     * Method deletes row by specified condition(s) from specified table
     *
     * @param sqLiteDatabase	link on instance of {@link SQLiteDatabase} class
     * @param tableName         specified table name
     * @param conditionColumn   condition column
     * @param conditionValue    condition value
     * @param withUserId        flag for using userId in where condition
     * @return                  answer is row deleted successfully
     */
    public boolean deleteRows(SQLiteDatabase    sqLiteDatabase,
                              String            tableName,
                              String            conditionColumn,
                              String            conditionValue,
                              boolean           withUserId) {

        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("DatabaseHandler: deleteRows(): tableName= " +tableName);

        StringBuilder sb = new StringBuilder(conditionColumn + "='" + conditionValue + "'");

        if(withUserId)
            sb.append(" AND USER_ID = '" +LocLookApp.appUserId+ "'");

        return sqLiteDatabase.delete(tableName,
                                     sb.toString(),
                                     null) > 0;
    }

    /**\
     *
     * @param sqLiteDatabase	link on instance of {@link SQLiteDatabase} class
     * @param tableName         specified table name
     */
    public void showAllTableData(SQLiteDatabase sqLiteDatabase,
                                 String         tableName) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp.showLog("DatabaseHandler: insertRow(): tableName= " +tableName);

        Cursor cursor = sqLiteDatabase.query(tableName,
                                             null, null, null, null, null, null);

        String[] cursorArr = cursor.getColumnNames();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            LocLookApp.showLog("----- Table: " + tableName + " -----");

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

        LocLookApp.showLog("-------------------------------------");
    }
}
