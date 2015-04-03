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
                CREATE_INDEX(ExpHist.TABLE_NAME, ExpHist.COL_DATE),
                ExpTotal.CREATE()
        };
    }

    public static String[] DROP_ALL() {
        return new String[] {
                Skill.DROP(),
                ExpHist.DROP(),
                DROP_INDEX(ExpHist.TABLE_NAME, ExpHist.COL_DATE),
                ExpTotal.DROP()
        };
    }

    public static String CREATE_INDEX(String tableName, String indexColumn) {
        return "CREATE INDEX IF NOT EXISTS " + tableName + "_" + indexColumn + "_idx ON "
                + tableName + "(" + indexColumn + ")";
    }

    public static String DROP_INDEX(String tableName, String indexColumn) {
        return "DROP INDEX IF EXISTS " + tableName + "_" + indexColumn + "_idx";
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
                    ", " + COL_SKILL_ID  + " INTEGER REFERENCES " + Skill.TABLE_NAME + "( " + Skill._ID + " ) ON DELETE CASCADE" +
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
                    ", " + COL_SKILL_ID  + " INTEGER REFERENCES " + Skill.TABLE_NAME + "( " + Skill._ID + " ) ON DELETE CASCADE" +
                    ", " + COL_SKILL_EXP + " INTEGER" +
                    " )";
        }

        public static final String DROP() {
            return "DROP TABLE IF EXISTS " + TABLE_NAME;
        }
    }

}
