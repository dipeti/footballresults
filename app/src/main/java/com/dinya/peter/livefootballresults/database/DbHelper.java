package com.dinya.peter.livefootballresults.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "results.db";
    private static final int DATABASE_VERSION = 18;

    public DbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TEAMS_TABLE = "CREATE TABLE " + DbContract.TeamEntry.TABLE_NAME + " (" +
                DbContract.TeamEntry._ID + " INTEGER PRIMARY KEY," + // AUTOINCREMENT is not needed, the keys provided by the WebAPI are used.
                DbContract.TeamEntry.COLUMN_TEAM_NAME + " TEXT NOT NULL, " +
                DbContract.TeamEntry.COLUMN_TEAM_SHORT_NAME + " TEXT NOT NULL, " +
                DbContract.TeamEntry.COLUMN_TEAM_CODE + " TEXT NOT NULL, " +
                DbContract.TeamEntry.COLUMN_TEAM_VALUE + " INTEGER DEFAULT 0 " +
                "); ";

        final String SQL_CREATE_GAMES_TABLE = "CREATE TABLE " + DbContract.GameEntry.TABLE_NAME + "(" +
                DbContract.GameEntry._ID + " INTEGER PRIMARY KEY,"+
                DbContract.GameEntry.COLUMN_HOME_ID + " INTEGER NOT NULL," +
                DbContract.GameEntry.COLUMN_HOME_SCORE + " INTEGER NOT NULL," +
                DbContract.GameEntry.COLUMN_AWAY_ID + " INTEGER NOT NULL," +
                DbContract.GameEntry.COLUMN_AWAY_SCORE + " INTEGER NOT NULL," +
                DbContract.GameEntry.COLUMN_DATE + " DATETIME NOT NULL," +
                DbContract.GameEntry.COLUMN_MATCH_DAY + " INTEGER NOT NULL," +
                DbContract.GameEntry.COLUMN_STATUS + " INTEGER NOT NULL," +// 0 => COMING UP ,1 => FINISHED
                DbContract.GameEntry.COLUMN_HOME_ODDS + " REAL DEFAULT 0," +
                DbContract.GameEntry.COLUMN_DRAW_ODDS + " REAL DEFAULT 0," +
                DbContract.GameEntry.COLUMN_AWAY_ODDS + " REAL DEFAULT 0," +
                DbContract.GameEntry.COLUMN_FAVORITE + " INTEGER DEFAULT 0," +// 0 => false, 1 => true
                " FOREIGN KEY (" + DbContract.GameEntry.COLUMN_HOME_ID + ") REFERENCES " +
                DbContract.TeamEntry.TABLE_NAME + "(" + DbContract.TeamEntry._ID +")," +
                " FOREIGN KEY (" + DbContract.GameEntry.COLUMN_AWAY_ID + ") REFERENCES " +
                DbContract.TeamEntry.TABLE_NAME + "(" + DbContract.TeamEntry._ID +")" +
                "); ";
        db.execSQL(SQL_CREATE_TEAMS_TABLE);
        db.execSQL(SQL_CREATE_GAMES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.GameEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DbContract.TeamEntry.TABLE_NAME);
        onCreate(db);
    }
}
