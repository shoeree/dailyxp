package net.arsargenti.dailyxp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import static net.arsargenti.dailyxp.DatabaseContract.SkillName;
import static net.arsargenti.dailyxp.DatabaseContract.ExpHist;

/**
 * Created by Sterling on 2015/03/14.
 *
 * This class has several helper methods for building parts of queries,
 * and several methods for communicating with the DailyExp database.
 * Cursor objects are returned for each of the "query" methods.
 */
public class DatabaseComms {
    /**
     * Return a field name, namespaced by table name.
     * @param table
     * @param field
     * @return "table.field"
     */
    public static String NAMESP(String table, String field) {
        return table + "." + field;
    }

    /**
     * Return a simple, single-field JOIN between table0 and table1.
     * @param table0
     * @param table1
     * @param fieldName
     * @return "table0 JOIN table1 ON table0.fieldName = table1.fieldName"
     */
    public static String JOIN(String table0, String table1, String fieldName) {
        return  table0 + " JOIN " + table1 +
                " ON " + table0 + "." + fieldName +
                " = "  + table1 + "." + fieldName;
    }

    /**
     * Get all (skill,exp) pairs for date from ExpHist table.
     * @param db
     * @param date
     * @return
     */
    public static Cursor querySkillsForDate(SQLiteDatabase db, String date) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(JOIN(ExpHist.TABLE_NAME, SkillName.TABLE_NAME, ExpHist.COL_SKILL_ID));
        String[] projection = {
                NAMESP(ExpHist.TABLE_NAME, ExpHist.COL_SKILL_ID),
                NAMESP(ExpHist.TABLE_NAME, ExpHist.COL_SKILL_EXP)
        };
        String[] selectionArgs = {date};

        return queryBuilder.query(db, projection,
                ExpHist.COL_DATE + " = ?", selectionArgs, null, null, null);
    }
}
