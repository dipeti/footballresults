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


public class ResultsContentProvider extends ContentProvider {

    public static final int GAMES = 100;
    public static final int GAME_WITH_ID = 101;
    public static final int GAMES_UPCOMING = 102;
    public static final int GAMES_FAVORITE = 103;

    public static final int TEAMS = 200;
    public static final int TEAM_WITH_ID = 201;

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
        return null;
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

        switch (match){
            case TEAMS:
                long id = db.insert(DbContract.TeamEntry.TABLE_NAME,null, values);
                if (id>0){
                    uriToReturn = ContentUris.withAppendedId(DbContract.TeamEntry.CONTENT_URI_TEAMS, id);
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
