package net.arsargenti.dailyxp;

import android.provider.BaseColumns;

/**
 * Created by Sterling on 2015/03/14.
 */
public class DatabaseContract {
    public DatabaseContract() {}

    public static String[] CREATE_ALL() {
        return new String[]{
                Skill.CREATE(),
                ExpHist.CREATE(),
                ExpTotal.CREATE()
        };
    }

    public static String[] DROP_ALL() {
        return new String[] {
                Skill.DROP(),
                ExpHist.DROP(),
                ExpTotal.DROP()
        };
    }

    public static abstract class Skill implements BaseColumns {
        public static final String TABLE_NAME = "Skill";
        public static final String COL_NAME = "name"; // String

        public static final String CREATE() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", " + COL_NAME + " TEXT" +
                    " )";
        }

        public static final String DROP() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }

    public static abstract class ExpHist implements BaseColumns {
        public static final String TABLE_NAME = "ExpHist";
        public static final String COL_DATE      = "date";      // Date
        public static final String COL_SKILL_ID  = "skill_id";  // Int
        public static final String COL_SKILL_EXP = "skill_exp"; // Int

        public static final String CREATE() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", " + COL_DATE      + " TEXT" +
                    ", " + COL_SKILL_ID  + " INTEGER" +
                    ", " + COL_SKILL_EXP + " INTEGER" +
                    " )";
        }

        public static final String DROP() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }

    public static abstract class ExpTotal implements BaseColumns {
        public static final String TABLE_NAME = "ExpTotal";
        public static final String COL_SKILL_ID  = "skill_id";  // Int
        public static final String COL_SKILL_EXP = "skill_exp"; // Int

        public static final String CREATE() {
            return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                    ", " + COL_SKILL_ID  + " INTEGER" +
                    ", " + COL_SKILL_EXP + " INTEGER" +
                    " )";
        }

        public static final String DROP() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }

}
