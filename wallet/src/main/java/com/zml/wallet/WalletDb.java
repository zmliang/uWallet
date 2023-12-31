package com.zml.wallet;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class WalletDb {

    private static final String TAG = "zml";

    private static String DATABASE_NAME = "uwallet.sqlite";
    private static String TABLE_NAME = "data";
    private static final int DATABASE_VERSION = 1;

    private static DBOpenHelper mDatabaseOpenHelper = null;
    private static SQLiteDatabase mDatabase = null;

    public static boolean init(Context context) {
        if (context != null) {
            mDatabaseOpenHelper = new DBOpenHelper(context);
            mDatabase = mDatabaseOpenHelper.getWritableDatabase();
            return true;
        }
        return false;
    }

    public static void destory() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public static void setItem(String key, String value) {
        try {
            String sql = "replace into "+TABLE_NAME+"(key,value)values(?,?)";
            mDatabase.execSQL(sql, new Object[] { key, value });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getItem(String key) {
        String ret = null;
        try {
            String sql = "select value from "+TABLE_NAME+" where key=?";
            Cursor c = mDatabase.rawQuery(sql, new String[]{key});
            while (c.moveToNext()) {
                // only return the first value
                if (ret != null)
                {
                    Log.e(TAG, "The key contains more than one value.");
                    break;
                }
                int index = c.getColumnIndexOrThrow("value");

                ret = c.getString(index);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret == null ? "" : ret;
    }

    public static void removeItem(String key) {
        try {
            String sql = "delete from "+TABLE_NAME+" where key=?";
            mDatabase.execSQL(sql, new Object[] {key});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This creates/opens the database.
     */
    private static class DBOpenHelper extends SQLiteOpenHelper {

        DBOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(key TEXT PRIMARY KEY,value TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
        }
    }

}
