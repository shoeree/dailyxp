package net.arsargenti.dailyxp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import static net.arsargenti.dailyxp.DatabaseContract.ExpHist;
import static net.arsargenti.dailyxp.DatabaseContract.Skill;

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
     * @param fieldName0
     * @param fieldName1
     * @return "table0 JOIN table1 ON table0.fieldName0 = table1.fieldName1"
     */
    public static String JOIN(String table0, String table1, String fieldName0, String fieldName1) {
        return  table0 + " JOIN " + table1 +
                " ON " + table0 + "." + fieldName0 +
                " = "  + table1 + "." + fieldName1;
    }

    /**
     * Get all (skill,exp) pairs for date from ExpHist table.
     * @param db
     * @param date
     * @return
     */
    public static Cursor querySkillsForDate(SQLiteDatabase db, String date) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(JOIN(ExpHist.TABLE_NAME, Skill.TABLE_NAME, ExpHist.COL_SKILL_ID, Skill._ID));
        String[] projection = {
                NAMESP(DatabaseContract.Skill.TABLE_NAME, Skill._ID),
                NAMESP(DatabaseContract.Skill.TABLE_NAME, Skill.COL_NAME),
                NAMESP(ExpHist.TABLE_NAME, ExpHist.COL_SKILL_EXP)
        };
        String[] selectionArgs = {date};

        return queryBuilder.query(db, projection,
                ExpHist.COL_DATE + " = ?", selectionArgs, null, null, null);
    }

    public static long queryAddSkill(SQLiteDatabase db, String skillName) {
        // Get the skill ID if it already exists.
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Skill.TABLE_NAME);
        String[] projection = {
                Skill._ID,
                Skill.COL_NAME
        };
        String[] selectionArgs = {skillName.toLowerCase()};
        Cursor cursor = queryBuilder.query(db, projection,
                Skill.COL_NAME + " = ?", selectionArgs, null, null, null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        if (count == 1) {
            return cursor.getLong(cursor.getColumnIndex(Skill._ID));
        } else if (count > 1) {
            throw new RuntimeException("Database inconsistent; " + String.valueOf(count) + " '" +
                    skillName + "' instances found!");
        }

        // Add the new skill and return its ID.
        ContentValues values = new ContentValues();
        values.put("name", skillName.toLowerCase());
        return db.insert(Skill.TABLE_NAME, null, values);
    }

    /**
     * Executes an array of queries (String[]).
     * @param db
     * @param queries
     */
    public static void excQueries(SQLiteDatabase db, String[] queries) {
        for (String q : queries) {
            Log.i(DatabaseComms.class.toString(), q);
            db.execSQL(q);
        }
    }
}
