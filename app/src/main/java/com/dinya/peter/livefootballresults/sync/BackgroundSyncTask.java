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

import java.util.List;

public class BackgroundSyncUtils {
    private static final String TAG = BackgroundSyncUtils.class.getSimpleName();



    synchronized public static void startSync(@NonNull final Context context){
        Thread syncData = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.d(TAG, "syncGames running....");
                    startSyncGames(context);
                    startSyncTable(context);
                } catch (NullPointerException ex){
                    ex.printStackTrace();
                }
                Log.d(TAG, "syncGames finished....");
            }
        });

        syncData.start();



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
            for (ContentValues value : values){
            resolver.insert(DbContract.GameEntry.CONTENT_URI_GAMES,value);
            }
        }
    }

    private static void startSyncTeams(Context context) {
        String teamResult = NetworkUtils.getResponseFromURL(NetworkUtils.buildTeamsURL());
        List<ContentValues> teamValues =  JSONParserUtils.getTeams(teamResult);
        ContentResolver resolver = context.getContentResolver();
                if (null != teamValues && 0 != teamValues.size()){
                    for (ContentValues value : teamValues){
                        resolver.insert(DbContract.TeamEntry.CONTENT_URI_TEAMS,value);
                    }
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
        }
    }

}
