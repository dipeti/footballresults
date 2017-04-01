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

    public static final int GAMES = 100;
    public static final int GAME_WITH_ID = 101;
    public static final int GAMES_UPCOMING = 102;
    public static final int GAMES_FAVORITE = 103;

    public static final int TEAMS = 200;
    public static final int TEAM_WITH_ID = 201;
    private static final String TAG = ResultsContentProvider.class.getSimpleName();

    private static final String GAME_SELECT_ARGS =
            DbContract.TeamEntry.ALIAS_TABLE_FIRST + "." + DbContract.TeamEntry.COLUMN_TEAM_SHORT_NAME + " AS " + DbContract.TeamEntry.ALIAS_HOME_TEAM +  ", " +
                    DbContract.TeamEntry.ALIAS_TABLE_SECOND + "." + DbContract.TeamEntry.COLUMN_TEAM_SHORT_NAME + " AS " + DbContract.TeamEntry.ALIAS_AWAY_TEAM + ", " +
                    DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_HOME_SCORE + ", " +
                    DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_AWAY_SCORE + ", " +
                    DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_DATE;

    private static final String SELECT_GAMES_JOINED_ON_TEAMS =
                            "SELECT " + GAME_SELECT_ARGS +
                            " FROM " + DbContract.GameEntry.TABLE_NAME +
                            " INNER JOIN " + DbContract.TeamEntry.TABLE_NAME + " AS " + DbContract.TeamEntry.ALIAS_TABLE_FIRST + " ON " +
                            DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_HOME_ID + " = " +
                            DbContract.TeamEntry.ALIAS_TABLE_FIRST + "." + DbContract.TeamEntry._ID +
                            " INNER JOIN " + DbContract.TeamEntry.TABLE_NAME + " AS " + DbContract.TeamEntry.ALIAS_TABLE_SECOND + " ON " +
                            DbContract.GameEntry.TABLE_NAME + "." + DbContract.GameEntry.COLUMN_AWAY_ID + " = " +
                            DbContract.TeamEntry.ALIAS_TABLE_SECOND + "." + DbContract.TeamEntry._ID +
                                    " ORDER BY " + DbContract.GameEntry.COLUMN_DATE +" ASC" +  " ;";


    private static UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);// empty matcher
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_TEAMS, TEAMS);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_TEAMS + "/#", TEAM_WITH_ID);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_GAMES, GAMES);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_GAMES + "/#", GAME_WITH_ID);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_UPCOMING_GAMES, GAMES_UPCOMING);
        uriMatcher.addURI(DbContract.AUTHORITY, DbContract.PATH_FAVORITE_GAMES, GAMES_FAVORITE);

        return uriMatcher;
    }

    private DbHelper mDbHelper;

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
            case GAMES:
                cursorToReturn = db.rawQuery(SELECT_GAMES_JOINED_ON_TEAMS,selectionArgs);
                Log.d(TAG, "query: " + SELECT_GAMES_JOINED_ON_TEAMS);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri.toString());
        }
        cursorToReturn.setNotificationUri(getContext().getContentResolver(),uri);
        return cursorToReturn;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
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
        return 0;
    }
}
