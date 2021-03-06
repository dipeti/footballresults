package com.dinya.peter.livefootballresults.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


public class ResultsContentProvider extends ContentProvider {

    /**
     * URI IDs
     */
    public static final int GAMES = 100;
    public static final int GAME_WITH_ID = 101;
    public static final int GAMES_UPCOMING = 102;
    public static final int GAMES_FINISHED = 103;
    public static final int GAMES_FAVORITE = 104;

    public static final int TEAMS = 200;
    public static final int TEAM_WITH_ID = 201;

    public static final int TABLE = 300;

    /**
     * debug TAG
     */
    private static final String TAG = ResultsContentProvider.class.getSimpleName();

    /**
     * SQL statements
     */
    private static final String GAME_SELECT_ARGS =
            DbContract.TeamEntry.ALIAS_TABLE_FIRST + "." + DbContract.TeamEntry._ID + " AS " + DbContract.TeamEntry.ALIAS_HOME_ID +  ", " +
            DbContract.TeamEntry.ALIAS_TABLE_SECOND + "." + DbContract.TeamEntry._ID + " AS " + DbContract.TeamEntry.ALIAS_AWAY_ID +  ", " +
            DbContract.TeamEntry.ALIAS_TABLE_FIRST + "." + DbContract.TeamEntry.COLUMN_TEAM_SHORT_NAME + " AS " + DbContract.TeamEntry.ALIAS_HOME_TEAM +  ", " +
            DbContract.TeamEntry.ALIAS_TABLE_SECOND + "." + DbContract.TeamEntry.COLUMN_TEAM_SHORT_NAME + " AS " + DbContract.TeamEntry.ALIAS_AWAY_TEAM + ", " +
            DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry._ID + ", " +
            DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_HOME_SCORE + ", " +
            DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_AWAY_SCORE + ", " +
            DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_DATE;

    // SELECT FROM
    private static final String BASE_SELECT_GAMES_JOINED_ON_TEAMS =
                            "SELECT " + GAME_SELECT_ARGS +
                            " FROM " + DbContract.GameEntry.TABLE_NAME +
                            " INNER JOIN " + DbContract.TeamEntry.TABLE_NAME + " AS " + DbContract.TeamEntry.ALIAS_TABLE_FIRST + " ON " +
                            DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_HOME_ID + " = " +
                            DbContract.TeamEntry.ALIAS_TABLE_FIRST + "." + DbContract.TeamEntry._ID +
                            " INNER JOIN " + DbContract.TeamEntry.TABLE_NAME + " AS " + DbContract.TeamEntry.ALIAS_TABLE_SECOND + " ON " +
                            DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_AWAY_ID + " = " +
                            DbContract.TeamEntry.ALIAS_TABLE_SECOND + "." + DbContract.TeamEntry._ID;
    // WHERE
    private static final String SELECTION_ARGS_UPCOMING_GAMES =
            " WHERE " + DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_STATUS  + " = 0 AND " +
                    DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_DATE + " <= ?"; ;
    private static final String SELECTION_ARGS_PAST_GAMES =
            " WHERE " + DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_STATUS  + " = 1 AND " +
                    DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_DATE + " >= ?";
    // ORDER BY
    private static final String ORDER_BY_DATE_ASC =" ORDER BY " + DbContract.GameEntry.TABLE_NAME+ "." + DbContract.GameEntry.COLUMN_DATE +" ASC" +  " ;";
    private static final String ORDER_BY_DATE_DESC =" ORDER BY " + DbContract.GameEntry.TABLE_NAME+ "." + DbContract.GameEntry.COLUMN_DATE +" DESC" +  " ;";
    // COMPLETE STATEMENT
    private static final String SELECT_UPCOMING_GAMES =
                    BASE_SELECT_GAMES_JOINED_ON_TEAMS +
                    SELECTION_ARGS_UPCOMING_GAMES +
                    ORDER_BY_DATE_ASC;
    private static final String SELECT_FINISHED_GAMES =
                    BASE_SELECT_GAMES_JOINED_ON_TEAMS +
                    SELECTION_ARGS_PAST_GAMES +
                    ORDER_BY_DATE_DESC;

    /**
     * UriMatcher init
     */
    private static UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);// empty matcher
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_TEAMS, TEAMS);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_TEAMS + "/#", TEAM_WITH_ID);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_GAMES, GAMES);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_GAMES + "/#", GAME_WITH_ID);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_GAMES + "/"+ DbContract.PATH_UPCOMING_GAMES, GAMES_UPCOMING);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_GAMES + "/"+ DbContract.PATH_FINISHED_GAMES, GAMES_FINISHED);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_GAMES + "/"+ DbContract.PATH_FAVORITE_GAMES, GAMES_FAVORITE);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_TABLE, TABLE);
        return uriMatcher;
    }

    /**
     * Member variables
     */
    private DbHelper mDbHelper;

    /**
     * Callbacks
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursorToReturn;
        switch (match){
            case TEAMS:
                cursorToReturn = db.query(DbContract.TeamEntry.TABLE_NAME,projection,selection,selectionArgs, null, null, sortOrder);
                break;
            case TEAM_WITH_ID:
                cursorToReturn = db.query(DbContract.TeamEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case GAMES_UPCOMING:
                cursorToReturn = db.rawQuery(SELECT_UPCOMING_GAMES,selectionArgs);
                Log.d(TAG, "query: " + SELECT_UPCOMING_GAMES);
                break;
            case GAMES_FINISHED:
                cursorToReturn = db.rawQuery(SELECT_FINISHED_GAMES,selectionArgs);
                Log.d(TAG, "query: " + SELECT_FINISHED_GAMES);
                break;
            case GAME_WITH_ID:
                cursorToReturn = db.query(DbContract.GameEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case TABLE:
                cursorToReturn = db.query(DbContract.TeamEntry.TABLE_NAME,projection,selection,selectionArgs,null,null, DbContract.TeamEntry.COLUMN_TEAM_POSITION + " ASC ");
                Log.d(TAG, "query: " + SELECT_UPCOMING_GAMES);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri.toString());
        }
        /*
         * Make sure that potential listeners are getting notified. CursorLoader automatically sets the listener.
         * When data is persisted, resolver.notifyChange(uri,null); will be fired.
         * This will invoke the listener which will reload the loader.
         */
        cursorToReturn.setNotificationUri(getContext().getContentResolver(),uri);
        return cursorToReturn;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case GAMES:
                // directory
                return "vnd.android.cursor.dir" + "/" + DbContract.AUTHORITY + "/" + DbContract.PATH_GAMES;
            case GAMES_FINISHED:
                // directory
                return "vnd.android.cursor.dir" + "/" + DbContract.AUTHORITY + "/" + DbContract.PATH_FINISHED_GAMES;
            case GAMES_UPCOMING:
                // directory
                return "vnd.android.cursor.dir" + "/" + DbContract.AUTHORITY + "/" + DbContract.PATH_UPCOMING_GAMES;
            case TEAMS:
                // directory
                return "vnd.android.cursor.dir" + "/" + DbContract.AUTHORITY + "/" + DbContract.PATH_TEAMS;
            case TEAM_WITH_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" + DbContract.AUTHORITY + "/" + DbContract.PATH_TEAMS;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri uriToReturn;
        long id;
        switch (match){
            case TEAMS:
                id = db.insertWithOnConflict(DbContract.TeamEntry.TABLE_NAME,null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if (id>0){
                    uriToReturn = ContentUris.withAppendedId(DbContract.TeamEntry.CONTENT_URI_TEAMS, id);
                } else {
                   throw new SQLException("Failed to insert row: " + uri);
                }
                break;
            case GAMES:
                 id = db.insertWithOnConflict(DbContract.GameEntry.TABLE_NAME,null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if (id>0){
                    uriToReturn = ContentUris.withAppendedId(DbContract.GameEntry.CONTENT_URI_GAMES, id);
                } else {
                    throw new SQLException("Failed to insert row: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri.toString());
        }
        /*
         * to let the resolver know that the DB has changed. The Db and any associated UI needs updating.
         */
        getContext().getContentResolver().notifyChange(uri,null);
        return uriToReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowUpdated;
        int match = sUriMatcher.match(uri);
        switch (match){
            case TEAM_WITH_ID:
                rowUpdated = db.update(DbContract.TeamEntry.TABLE_NAME,values, DbContract.TeamEntry._ID + " = ?",selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        long id;
        int rowsInserted;
        switch (match){

            case TEAMS:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values){
                        id = db.insertWithOnConflict(DbContract.TeamEntry.TABLE_NAME,null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (-1 != id){
                            rowsInserted++;
                        }else {
                            throw new SQLException("Failed to insert row: " + value.toString());
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsInserted;

            case GAMES:
                db.beginTransaction();
                rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        id = db.insertWithOnConflict(DbContract.GameEntry.TABLE_NAME, null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (-1 != id) {
                            rowsInserted++;
                        } else {
                            throw new SQLException("Failed to insert row: " + value.toString());
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }








    }
}
