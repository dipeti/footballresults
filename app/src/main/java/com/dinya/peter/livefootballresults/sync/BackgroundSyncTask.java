package com.dinya.peter.livefootballresults.sync;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.utils.JSONParserUtils;
import com.dinya.peter.livefootballresults.utils.NetworkUtils;

import java.util.Arrays;
import java.util.List;

public class BackgroundSyncTask {

    private static final String TAG = BackgroundSyncTask.class.getSimpleName();

    synchronized public static void startSync(@NonNull final Context context){
        try{
            Log.d(TAG, "syncGames running....");
            startSyncGames(context); // if the teams are not synced yet, it will start startSyncTeam();
            startSyncTable(context);
        } catch (NullPointerException ex){
            ex.printStackTrace();
        }
        Log.d(TAG, "syncGames finished....");
    }

    synchronized private static void startSyncGames(Context context) {
        Cursor cursorTeams = context.getContentResolver().query(DbContract.TeamEntry.CONTENT_URI_TEAMS,null,null,null,null);
        if (null == cursorTeams || 0 == cursorTeams.getCount()){
            startSyncTeams(context);
        }
        if (cursorTeams != null) {
            cursorTeams.close();
        }
        String upcomingResult = NetworkUtils.getResponseFromURL(NetworkUtils.buildUpcomingMatchesURL());
        String pastResult = NetworkUtils.getResponseFromURL(NetworkUtils.buildFinishedMatchesURL());
        List<ContentValues> values =  JSONParserUtils.getGames(upcomingResult);
        List<ContentValues> pastValues = JSONParserUtils.getGames(pastResult);
        if (null !=values && null != pastValues)
            values.addAll(pastValues);
        ContentResolver resolver = context.getContentResolver();
        if (null != values && 0 != values.size()){
            resolver.bulkInsert(DbContract.GameEntry.CONTENT_URI_GAMES, values.toArray(new ContentValues[values.size()]));
        }
    }

    private static void startSyncTeams(Context context) {
        String teamResult = NetworkUtils.getResponseFromURL(NetworkUtils.buildTeamsURL());
        List<ContentValues> teamValues =  JSONParserUtils.getTeams(teamResult);
        ContentResolver resolver = context.getContentResolver();
                if (null != teamValues && 0 != teamValues.size()){
                    resolver.bulkInsert(DbContract.TeamEntry.CONTENT_URI_TEAMS, teamValues.toArray(new ContentValues[teamValues.size()]));
                }
    }

    private static void startSyncTable(Context context){
        String tableResult = NetworkUtils.getResponseFromURL(NetworkUtils.buildTableURL());
        List<ContentValues> tableValues = JSONParserUtils.getTable(tableResult);
        ContentResolver resolver = context.getContentResolver();
        if (null != tableValues && 0 != tableValues.size()){
            for (ContentValues value : tableValues){
                String id = value.getAsString(DbContract.TeamEntry._ID);
                Uri uri = ContentUris.withAppendedId(DbContract.TeamEntry.CONTENT_URI_TEAMS,Long.parseLong(id));
                resolver.update(uri,value, DbContract.TeamEntry._ID + " = ?", new String[]{id});
            }
            resolver.notifyChange(DbContract.TeamEntry.CONTENT_URI_TABLE,null);
        }
    }

}
