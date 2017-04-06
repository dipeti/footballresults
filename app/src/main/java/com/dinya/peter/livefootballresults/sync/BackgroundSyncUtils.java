package com.dinya.peter.livefootballresults.sync;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.utils.JSONParserUtils;
import com.dinya.peter.livefootballresults.utils.NetworkUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BackgroundSyncUtils {
    private static final String TAG = BackgroundSyncUtils.class.getSimpleName();

    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context){
        Log.d(TAG, "Initialized: " + sInitialized);
        if  (sInitialized) return;
        sInitialized = true;
        Thread checkForTeams = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "checkForTeams running....");
                    Cursor cursorTeams = context.getContentResolver().query(DbContract.TeamEntry.CONTENT_URI_TEAMS,null,null,null,null);
                    if (null == cursorTeams || 0 == cursorTeams.getCount()){
                        startSyncTeams(context);
                    }
                    cursorTeams.close();
                } catch (NullPointerException ex){
                    ex.printStackTrace();
                }
                Log.d(TAG, "checkForTeams finished....");
            }
        });
        Thread syncGames = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Log.d(TAG, "syncGames running....");
                    Cursor cursorGames = context.getContentResolver().query(DbContract.GameEntry.CONTENT_URI_GAMES,null,null,null,null);
                    startSyncGames(context);
                    cursorGames.close();
                } catch (NullPointerException ex){
                    ex.printStackTrace();
                }
                Log.d(TAG, "syncGames finished....");
            }
        });
        checkForTeams.start();
        /*
         * Stops the main thread. The application waits until the Teams are persisted.
         * This should only happen when the app is started for the very first time.
         */
        try {
            checkForTeams.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        syncGames.start();



    }

    synchronized private static void startSyncGames(Context context) {
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
        String result = NetworkUtils.getResponseFromURL(NetworkUtils.buildTeamsURL());
        List<ContentValues> values =  JSONParserUtils.getTeams(result);
        ContentResolver resolver = context.getContentResolver();
                if (null != values && 0 != values.size()){
                    for (ContentValues value : values){
                        resolver.insert(DbContract.TeamEntry.CONTENT_URI_TEAMS,value);
                    }
                }
    }

}
