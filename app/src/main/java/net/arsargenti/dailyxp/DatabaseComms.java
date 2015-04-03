package net.arsargenti.dailyxp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
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
     * Normalized skill names have only the first word capitalized, while
     * all other letters on other words are lower-case.
     * @param skillName
     * @return
     */
    public static String NORMALIZE_SKILL_NAME(String skillName) {
        return skillName.substring(0,1).toUpperCase() +
                skillName.substring(1).toLowerCase();
    }

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
     * Return a LEFT OUTER JOIN of table0 with table1.
     * @param table0
     * @param table1
     * @param fieldName0
     * @param fieldName1
     * @return "table0 LEFT OUTER JOIN table1 ON table0.fieldName0 = table1.fieldName1"
     */
    public static String LEFT_OUTER_JOIN(String table0, String table1, String fieldName0, String fieldName1) {
        return  table0 + " LEFT OUTER JOIN " + table1 +
                " ON " + table0 + "." + fieldName0 +
                " = "  + table1 + "." + fieldName1;
    }

    /**
     * Get all (skill,exp) pairs, including those not yet part of this date.
     */
    public static Cursor querySkillsForDate(SQLiteDatabase db, String date) {
        String q = "SELECT " +
                NAMESP("Hist", ExpHist._ID) + ", " +
                NAMESP(Skill.TABLE_NAME, Skill.COL_NAME) + ", " +
                NAMESP("Hist", ExpHist.COL_SKILL_EXP) + " AS " + ExpHist.COL_SKILL_EXP + ", " +
                NAMESP("Hist", ExpHist.COL_DATE) + " " +
                "FROM " + Skill.TABLE_NAME + " LEFT OUTER JOIN " +
                "(SELECT * FROM " + ExpHist.TABLE_NAME + " WHERE " + NAMESP(ExpHist.TABLE_NAME, ExpHist.COL_DATE) +
                " IN ('" + date + "', NULL)) Hist ON " +
                NAMESP(Skill.TABLE_NAME, Skill._ID) + " = " + NAMESP("Hist", ExpHist.COL_SKILL_ID);
        Log.i(DatabaseComms.class.getName(), q);
        Cursor cursor = db.rawQuery(q, null);
        Log.i(DatabaseComms.class.getName(), "Count: " + cursor.getCount());
        return db.rawQuery(q, null);
    }

    /**
     * Get all (skill,exp) pairs for date from ExpHist table.
     * @param db
     * @param date
     * @return
     */
    public static boolean queryDoesSkillExistForDate(SQLiteDatabase db, String date, int skillId) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ExpHist.TABLE_NAME);
        String[] projection = {ExpHist._ID};
        String[] selectionArgs = {date, String.valueOf(skillId)};

        Log.i(DatabaseComms.class.getName(),
                queryBuilder.buildQuery(projection,
                        ExpHist.COL_DATE + " = " + selectionArgs[0] + " AND " +
                                ExpHist.COL_SKILL_ID + " = " + selectionArgs[1],
                        null, null, null, null));
        Cursor cursor = queryBuilder.query(db, projection,
                ExpHist.COL_DATE + " = ? AND " + ExpHist.COL_SKILL_ID + " = ?", selectionArgs,
                null, null, null);
        cursor.moveToFirst();
        int count = cursor.getCount();
        Log.i(DatabaseComms.class.getName(), "# entries in ExpHist table: " + count);
        return count == 1;
    }

    public static long queryAddSkillForDate(SQLiteDatabase db, String date, String skillName, Integer skillExp) {
        // First get the skill ID corresponding to the skill name.
        Cursor cursor = querySkillName(db, skillName);
        int skillId = cursor.getInt(cursor.getColumnIndex(Skill._ID));

        // If the skill doesn't exist for this date yet, then we need to add the skill to the ExpHist table.
        ContentValues values = new ContentValues();
        values.put(ExpHist.COL_SKILL_EXP, skillExp == null ? 0 : skillExp);
        if (queryDoesSkillExistForDate(db, date, skillId)) {
            // Update the skill which is already in this date.
            Log.i(DatabaseComms.class.getName(),
                    "Updating skill with id = " + skillId + " with exp = " + skillExp + " for date = " + date);
            String[] whereArgs = {date, String.valueOf(skillId)};
            return db.update(ExpHist.TABLE_NAME, values,
                    ExpHist.COL_DATE + " = ? AND " + ExpHist.COL_SKILL_ID + " = ?", whereArgs);
        } else {
            // Add the skill to this date.
            Log.i(DatabaseComms.class.getName(),
                    "Adding new skill entry with id = " + skillId + " with exp = " + skillExp + " for date = " + date);
            values.put(ExpHist.COL_DATE, date);
            values.put(ExpHist.COL_SKILL_ID, skillId);
            return db.insert(ExpHist.TABLE_NAME, null, values);
        }
    }

    public static Cursor querySkills(SQLiteDatabase db) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Skill.TABLE_NAME);
        String[] projection = { Skill._ID, Skill.COL_NAME };

        Log.i(DatabaseComms.class.getName(),
                queryBuilder.buildQuery(projection, null, null, null, null, null));
        return queryBuilder.query(db, projection, null, null, null, null, null);
    }

    /**
     * Get the skill ID corresponding to the skill name given.
     * @param db
     * @param skillName
     * @return
     */
    public static Cursor querySkillName(SQLiteDatabase db, String skillName) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Skill.TABLE_NAME);
        String[] projection = {
                Skill._ID,
                Skill.COL_NAME
        };

        // All skills should have the first letter capitalized, but no others.
        String normalizedSkillName = NORMALIZE_SKILL_NAME(skillName);

        String[] selectionArgs = {normalizedSkillName};
        Log.i(DatabaseComms.class.getName(),
                queryBuilder.buildQuery(projection,
                        Skill.COL_NAME + " = " + selectionArgs[0], null, null, null, null));
        Cursor cursor = queryBuilder.query(db, projection,
                Skill.COL_NAME + " = ?", selectionArgs, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    /**
     * A query which adds a skill to the dimension table.
     * @param db
     * @param skillName
     * @return (long) the _ID of the skill, or -1 if it already exists in the db.
     */
    public static long queryAddSkill(SQLiteDatabase db, String skillName) {
        // Get the skill ID if it already exists.
        Cursor cursor = querySkillName(db, skillName);
        int count = cursor.getCount();
        if (count == 1) {
            //return cursor.getLong(cursor.getColumnIndex(Skill._ID));
            return -1;
        } else if (count > 1) {
            throw new RuntimeException("Database inconsistent; " + String.valueOf(count) + " '" +
                    skillName + "' instances found!");
        }

        // Add the new skill and return its ID.
        ContentValues values = new ContentValues();
        values.put("name", NORMALIZE_SKILL_NAME(skillName));
        return db.insert(Skill.TABLE_NAME, null, values);
    }

    public static void queryDeleteSkill(SQLiteDatabase db, String skillName) {
        String[] whereArgs = {skillName};
        db.delete(Skill.TABLE_NAME, Skill.COL_NAME + " = ?", whereArgs);
    }

    /**
     * Executes an array of queries (String[]).
     * @param db
     * @param queries
     */
    public static void excQueries(SQLiteDatabase db, String[] queries) {
        for (String q : queries) {
            Log.i(DatabaseComms.class.getName(), q);
            db.execSQL(q);
        }
    }
}
