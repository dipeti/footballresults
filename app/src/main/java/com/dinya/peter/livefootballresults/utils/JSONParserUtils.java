package com.dinya.peter.livefootballresults.utils;

import android.content.ContentValues;
import android.net.Uri;
import android.nfc.Tag;
import android.text.format.DateFormat;
import android.util.Log;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.database.DbHelper;
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

    public static List<ContentValues> getGames(String jsonString){
        List<ContentValues> values;
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonString);
            JSONArray gamesArray = baseJsonResponse.getJSONArray("fixtures");
            int size = gamesArray.length();
            values = new ArrayList<>(size);
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
                values.add(new ContentValues());
                values.get(i).put(DbContract.GameEntry._ID,id);
                values.get(i).put(DbContract.GameEntry.COLUMN_HOME_ID,homeId);
                values.get(i).put(DbContract.GameEntry.COLUMN_AWAY_ID,awayId);
                values.get(i).put(DbContract.GameEntry.COLUMN_HOME_SCORE,homeScore);
                values.get(i).put(DbContract.GameEntry.COLUMN_AWAY_SCORE,awayScore);
                values.get(i).put(DbContract.GameEntry.COLUMN_DATE,new SimpleDateFormat(DbHelper.SQL_DATE_FORMAT, Locale.ENGLISH).format(date));
                values.get(i).put(DbContract.GameEntry.COLUMN_FAVORITE, 0);
                values.get(i).put(DbContract.GameEntry.COLUMN_STATUS,isFinished);
                values.get(i).put(DbContract.GameEntry.COLUMN_MATCH_DAY,matchDay);
                values.get(i).put(DbContract.GameEntry.COLUMN_HOME_ODDS, homeOdds);
                values.get(i).put(DbContract.GameEntry.COLUMN_DRAW_ODDS, drawOdds);
                values.get(i).put(DbContract.GameEntry.COLUMN_AWAY_ODDS, awayOdds);
                Log.d(TAG, "getGames:" + values.get(i).toString());
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

    public static List<ContentValues> getTeams(String jsonString){
        List<ContentValues> values;
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonString);
            JSONArray teamsArray = baseJsonResponse.getJSONArray("teams");
            int size = teamsArray.length();
            values = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                JSONObject teamJsonObject = teamsArray.getJSONObject(i);
                String name  = teamJsonObject.getString("name");
                String shortName = teamJsonObject.getString("shortName");
                String code  = teamJsonObject.getString("code");
                String value = teamJsonObject.getString("squadMarketValue");
                String id = getId(teamJsonObject.getJSONObject("_links").getJSONObject("self"));

                values.add(new ContentValues());
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_NAME,name);
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_SHORT_NAME,shortName);
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_CODE,code);
                values.get(i).put(DbContract.TeamEntry._ID,id);
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_VALUE,value);
                Log.d(TAG, "getTeams:" + values.get(i).toString());
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
