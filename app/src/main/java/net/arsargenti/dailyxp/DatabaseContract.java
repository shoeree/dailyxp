package net.arsargenti.dailyxp;

import android.provider.BaseColumns;

/**
 * Created by Sterling on 2015/03/14.
 */
public class DatabaseContract {
    public DatabaseContract() {}

    public static String CREATE() {
        return         SkillName.CREATE() +
                "; " + ExpHist.CREATE() +
                "; " + ExpTotal.CREATE() +
                ";";
    }

    public static abstract class SkillName implements BaseColumns {
        public static final String TABLE_NAME = "SkillName";
        public static final String COL_SKILL_ID   = "skill_id";   // Int
        public static final String COL_SKILL_NAME = "skill_name"; // String

        public static final String CREATE() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                    _ID + " INTEGER PRIMARY KEY" +
                    ", " + COL_SKILL_ID   + " INTEGER" +
                    ", " + COL_SKILL_NAME + " TEXT" +
                    " )";
        }
    }

    public static abstract class ExpHist implements BaseColumns {
        public static final String TABLE_NAME = "ExpHist";
        public static final String COL_DATE      = "date";      // Date
        public static final String COL_SKILL_ID  = "skill_id";  // Int
        public static final String COL_SKILL_EXP = "skill_exp"; // Int

        public static final String CREATE() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                    _ID + " INTEGER PRIMARY KEY" +
                    ", " + COL_DATE      + " TEXT" +
                    ", " + COL_SKILL_ID  + " INTEGER" +
                    ", " + COL_SKILL_EXP + " INTEGER" +
                    " )";
        }
    }

    public static abstract class ExpTotal implements BaseColumns {
        public static final String TABLE_NAME = "ExpTotal";
        public static final String COL_SKILL_ID  = "skill_id";  // Int
        public static final String COL_SKILL_EXP = "skill_exp"; // Int

        public static final String CREATE() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                    _ID + " INTEGER PRIMARY KEY" +
                    ", " + COL_SKILL_ID  + " INTEGER" +
                    ", " + COL_SKILL_EXP + " INTEGER" +
                    " )";
        }
    }

}
