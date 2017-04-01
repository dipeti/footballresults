package com.dinya.peter.livefootballresults.utils;

import android.content.ContentValues;
import android.net.Uri;
import android.nfc.Tag;
import android.text.format.DateFormat;
import android.util.Log;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.entity.Match;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;


public class JSONParserUtils {

    private static final String TAG = JSONParserUtils.class.getSimpleName();
    private JSONParserUtils() {
    }

    public static ContentValues[] getGames(String jsonString){
        ContentValues values[];
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonString);
            JSONArray gamesArray = baseJsonResponse.getJSONArray("fixtures");
            int size = gamesArray.length();
            values = new ContentValues[size];
            for (int i = 0; i < size; i++) {
                JSONObject gameJsonObject = gamesArray.getJSONObject(i);
                int isFinished = gameJsonObject.getString("status").equals("FINISHED") ? 1 : 0;
                int homeScore = -1, awayScore = -1;
                if (1 == isFinished) {
                    homeScore = gameJsonObject.getJSONObject("result").getInt("goalsHomeTeam");
                    awayScore = gameJsonObject.getJSONObject("result").getInt("goalsAwayTeam");
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                Date date = simpleDateFormat.parse(gameJsonObject.getString("date"));
                String matchDay = gameJsonObject.getString("matchday");
                String homeOdds = gameJsonObject.getJSONObject("odds").getString("homeWin");
                String drawOdds = gameJsonObject.getJSONObject("odds").getString("draw");
                String awayOdds = gameJsonObject.getJSONObject("odds").getString("awayWin");
                String id = getId(gameJsonObject.getJSONObject("_links").getJSONObject("self"));
                String homeId = getId(gameJsonObject.getJSONObject("_links").getJSONObject("homeTeam"));
                String awayId = getId(gameJsonObject.getJSONObject("_links").getJSONObject("awayTeam"));
                values[i] = new ContentValues();
                values[i].put(DbContract.GameEntry._ID,id);
                values[i].put(DbContract.GameEntry.COLUMN_HOME_ID,homeId);
                values[i].put(DbContract.GameEntry.COLUMN_AWAY_ID,awayId);
                values[i].put(DbContract.GameEntry.COLUMN_HOME_SCORE,homeScore);
                values[i].put(DbContract.GameEntry.COLUMN_AWAY_SCORE,awayScore);
                values[i].put(DbContract.GameEntry.COLUMN_DATE,date.toString());
                values[i].put(DbContract.GameEntry.COLUMN_FAVORITE, 0);
                values[i].put(DbContract.GameEntry.COLUMN_STATUS,isFinished);
                values[i].put(DbContract.GameEntry.COLUMN_MATCH_DAY,matchDay);
                values[i].put(DbContract.GameEntry.COLUMN_HOME_ODDS, homeOdds);
                values[i].put(DbContract.GameEntry.COLUMN_DRAW_ODDS, drawOdds);
                values[i].put(DbContract.GameEntry.COLUMN_AWAY_ODDS, awayOdds);
                Log.d(TAG, "getGames:" + values[i].toString());
            }
            return values;
        }
        catch (JSONException ex){
            Log.e("JSONParserUtil", "Problem parsing the matches JSON results: " + jsonString, ex);

        } catch (ParseException e) {
            e.printStackTrace();
        }
            return null;
    }

    public static ContentValues[] getTeams(String jsonString){
        ContentValues values[];
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonString);
            JSONArray teamsArray = baseJsonResponse.getJSONArray("teams");
            int size = teamsArray.length();
            values = new ContentValues[size];
            for (int i = 0; i < size; i++) {
                JSONObject teamJsonObject = teamsArray.getJSONObject(i);
                String name  = teamJsonObject.getString("name");
                String shortName = teamJsonObject.getString("shortName");
                String code  = teamJsonObject.getString("code");
                String value = teamJsonObject.getString("squadMarketValue");
                String id = getId(teamJsonObject.getJSONObject("_links").getJSONObject("self"));

                values[i] = new ContentValues();
                values[i].put(DbContract.TeamEntry.COLUMN_TEAM_NAME,name);
                values[i].put(DbContract.TeamEntry.COLUMN_TEAM_SHORT_NAME,shortName);
                values[i].put(DbContract.TeamEntry.COLUMN_TEAM_CODE,code);
                values[i].put(DbContract.TeamEntry._ID,id);
                values[i].put(DbContract.TeamEntry.COLUMN_TEAM_VALUE,value);
                Log.d(TAG, "getTeams:" + values[i].toString());
            }
            return values;
        }
        catch (JSONException ex){
            Log.e("JSONParserUtil", "Problem parsing the matches JSON results: " + jsonString, ex);

        }
        return null;
    }

    private static String getId(JSONObject gameJsonObject) throws JSONException {
        String href = gameJsonObject.getString("href");
        Uri uri = Uri.parse(href);
        int last =  uri.getPathSegments().size() - 1;
        return uri.getPathSegments().get(last);
    }

}
