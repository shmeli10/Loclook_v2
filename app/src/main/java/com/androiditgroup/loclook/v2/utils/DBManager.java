package com.androiditgroup.loclook.v2.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.androiditgroup.loclook.v2.LocLookApp;

import java.util.Arrays;

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

                    createTable(sqLiteDatabase, Constants.DataBase.USER_DATA_TABLE, Constants.DataBase.USER_DATA_TABLE_COLUMNS);
                    // createTable(sqLiteDatabase, Constants.DataBase.USER_AUTH_DATA_TABLE, Constants.DataBase.USER_AUTH_DATA_TABLE_COLUMNS);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                try {
                    // sqLiteDatabase.execSQL(Constants.DataBase.DROP_USER_DATA_TABLE);
                    // sqLiteDatabase.execSQL(Constants.DataBase.DROP_HISTORY_TABLE);
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
        // Log.d(LOG_TAG, "--- create table " + tableName + " ---");

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
     * Запрос на возвращение столбцов с нужными строками из таблицы
     *
     * @param table   Имя таблицы
     * @param columns Масив строк с именами нужных столбцов
     * @return Возвращает Cursor
     */
    public Cursor queryColumns(String table, String... columns) {
        return getDataBase().query(table, columns, null, null, null, null, null);
    }

    public Cursor queryColumns(String table, String[] column, String requestColumn, String where) {
        return getDataBase().query(table, column, requestColumn + "='" + where + "'", null, null, null, null);
    }


    /**
     * Запрос на возвращение строк из таблицы
     *
     * @param sql           SQL запрос. Щн не должен заканчиваться с ;
     * @param selectionArgs Можете включить ?s в WHERE и он будет заменятся на selectionArgs.
     * @return Возвращает строки из таблицы
     */
    public Cursor queryRows(SQLiteDatabase database, String sql, String[] selectionArgs) {
        return database.rawQuery(sql, selectionArgs);
    }

    /**
     * Запрос на создание пользователя
     * @param userName      Введенное пользователем имя
     * @param phoneNumber   Введенный пользователем номер телефона
     * @return Возвращаем идентификатор пользователя
     */
    public Cursor createUser(String userName, String phoneNumber) {

        String[] columnsArr = {"NAME", "PHONE_NUMBER", "RATE"};
        String[] dataArr    = {userName, phoneNumber, "0"};

        int userId = addRow(Constants.DataBase.USER_DATA_TABLE, columnsArr, dataArr);

        // если идентификатор созданного пользователя получен
        if(userId > 0)
            // получаем курсор с данными пользователя
            return queryColumns(Constants.DataBase.USER_DATA_TABLE, null, "_ID", String.valueOf(userId));

        return null;
    }

    public int addRow(String tableName, String[] columnsArr, String[] dataArr) {
        ContentValues cv = new ContentValues();
        long rowID;

        for(int i=0; i<columnsArr.length; i++)
            cv.put(columnsArr[i], dataArr[i]);

        // вставляем запись и получаем ее ID
        rowID = getDataBase().insert(tableName, null, cv);
        // Log.d(LOG_TAG, "row inserted, ID = " + rowID + " in table " +tableName);

        return (int) rowID;
    }

    /**
     * Used to delete all data from one table
     * @param table table you want to delete data from
     */
    public void deleteAllData(String table) {
        String delete = "DELETE FROM " + table;
        getDataBase().execSQL(delete);
    }


    /**
     * Used to delete one row from table
     * @param table table you want to delete from
     * @param column column from where to search the requested row
     * @param where data within the row that you want to delete
     */
    public void deleteRow(String table, String column, String... where) {
        String delete = "DELETE FROM " + table + " WHERE " + column + " = \"" + Arrays.toString(where).replace("[", "").replace("]", "") + "\"";
        getDataBase().execSQL(delete);
    }


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


//    /**
//     * Return the last inserted user token
//     * @return String that contains the last inserted user token
//     */
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


    /**
     * Delete all the data from the table except users login data (password and id)
     * @param table table you want to delete from
     */
    public void deleteDB(String table) {
        deleteAllData(table);
    }
}
