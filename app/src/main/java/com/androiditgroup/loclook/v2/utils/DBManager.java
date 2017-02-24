package com.androiditgroup.loclook.v2.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;

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

                    createTable(sqLiteDatabase, Constants.DataBase.USER_DATA_TABLE, Constants.DataBase.USER_DATA_TABLE_COLUMNS);
                    createTable(sqLiteDatabase, Constants.DataBase.BADGE_DATA_TABLE, Constants.DataBase.BADGE_DATA_TABLE_COLUMNS);
                    // createTable(sqLiteDatabase, Constants.DataBase.USER_AUTH_DATA_TABLE, Constants.DataBase.USER_AUTH_DATA_TABLE_COLUMNS);

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
        // Log.e("ABC", "DBManager: createUser()");

        String[] columnsArr = {"NAME", "PHONE_NUMBER", "RATE"};
        String[] dataArr    = {userName, phoneNumber, "0"};

        // int userId = insertData(Constants.DataBase.USER_DATA_TABLE, columnsArr, dataArr);
        int userId = insertData(getDataBase(), Constants.DataBase.USER_DATA_TABLE, columnsArr, dataArr);

        // Log.e("ABC", "DBManager: createUser(): userId= " +userId);

        // если идентификатор созданного пользователя получен
        if(userId > 0)
            // получаем курсор с данными пользователя
            return queryColumns(Constants.DataBase.USER_DATA_TABLE, null, "_ID", String.valueOf(userId));

        return null;
    }

    private void populateTables(SQLiteDatabase db) {
        Log.e("ABC", "DBManager: populateTables()");

        String[] badgesArr = context.getResources().getStringArray(R.array.badges);
        // String[] columnsArr = {"NAME", "IS_ENABLED"};
        String[] columnsArr = {"NAME"};

        // Log.e("ABC", "DBManager: populateTables(): badgesArr length = " +badgesArr.length);

        for(int i=0; i<badgesArr.length; i++)
            // insertData(db, Constants.DataBase.BADGE_DATA_TABLE, columnsArr, new String[] {badgesArr[i], "0"});
            insertData(db, Constants.DataBase.BADGE_DATA_TABLE, columnsArr, new String[] {badgesArr[i]});

        showAllTableData(db, Constants.DataBase.BADGE_DATA_TABLE);
    }

    // public int addRow(String tableName, String[] columnsArr, String[] dataArr) {
    public int insertData(SQLiteDatabase db, String tableName, String[] columnsArr, String[] dataArr) {
        Log.e("ABC", "DBManager: insertData(): badge name= " +dataArr[0]);

        ContentValues cv = new ContentValues();
        long rowID;

        for(int i=0; i<columnsArr.length; i++)
            cv.put(columnsArr[i], dataArr[i]);

        // вставляем запись и получаем ее ID
        rowID = db.insert(tableName, null, cv);
        Log.e("ABC", "badge(" +dataArr[0]+ ")row inserted, ID = " + rowID + " in table " +tableName);

        return (int) rowID;
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
