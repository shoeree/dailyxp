package net.arsargenti.dailyxp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sterling on 2015/03/14.
 */
public class ExpDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "DailyExp.db";

    public ExpDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        onCreate(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //DatabaseComms.excQueries(db, DatabaseContract.DROP_ALL()); // TODO dev only
        DatabaseComms.excQueries(db, DatabaseContract.CREATE_ALL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DatabaseComms.excQueries(db, DatabaseContract.DROP_ALL());
        DatabaseComms.excQueries(db, DatabaseContract.CREATE_ALL());
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, newVersion, oldVersion);
    }
}
