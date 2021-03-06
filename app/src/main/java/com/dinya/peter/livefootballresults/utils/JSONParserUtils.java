package com.dinya.peter.livefootballresults.utils;

import android.content.ContentValues;
import android.net.Uri;
import android.nfc.Tag;
import android.text.format.DateFormat;
import android.util.Log;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.database.DbHelper;
import com.dinya.peter.livefootballresults.entity.Match;
import com.dinya.peter.livefootballresults.entity.Player;

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

    public static final String NOT_AVAILABLE = "N/A";
    private static final String TAG = JSONParserUtils.class.getSimpleName();

    private JSONParserUtils() {
    }

    public static List<Player> getPlayers(String jsonString){
        List<Player> players;
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonString);
            JSONArray playersArray = baseJsonResponse.getJSONArray("players");
            int size = playersArray.length();
            players = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                JSONObject gameJsonObject = playersArray.getJSONObject(i);
                String nationality = NOT_AVAILABLE;
                int jerseyNumber = Integer.MAX_VALUE;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date dateOfBirth = null;
                Date contractUntil = null;
                String marketValue = NOT_AVAILABLE;
                String name = gameJsonObject.getString("name");
                String position = gameJsonObject.getString("position");

                if (!gameJsonObject.isNull("jerseyNumber"))
                jerseyNumber = gameJsonObject.getInt("jerseyNumber");


                if (!gameJsonObject.isNull("dateOfBirth"))
                    dateOfBirth = simpleDateFormat.parse(gameJsonObject.getString("dateOfBirth"));
                if (!gameJsonObject.isNull("nationality"))
                nationality = gameJsonObject.getString("nationality");
                if (!gameJsonObject.isNull("contractUntil"))
                contractUntil = simpleDateFormat.parse(gameJsonObject.getString("contractUntil"));
                if (!gameJsonObject.isNull("marketValue"))
                marketValue = gameJsonObject.getString("marketValue");
                players.add(new Player(name,position,jerseyNumber,dateOfBirth,nationality,contractUntil,marketValue));
            }
            return players;
        }
        catch (JSONException ex){
            Log.e("JSONParserUtil", "Problem parsing the players JSON results: " + jsonString, ex);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

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
                String homeOdds = NOT_AVAILABLE, drawOdds = NOT_AVAILABLE, awayOdds = NOT_AVAILABLE;
                if (!gameJsonObject.isNull("odds")) {
                    homeOdds = gameJsonObject.getJSONObject("odds").getString("homeWin");
                    drawOdds = gameJsonObject.getJSONObject("odds").getString("draw");
                    awayOdds = gameJsonObject.getJSONObject("odds").getString("awayWin");
                }
                String id = getId(gameJsonObject.getJSONObject("_links").getJSONObject("self"));
                String homeId = getId(gameJsonObject.getJSONObject("_links").getJSONObject("homeTeam"));
                String awayId = getId(gameJsonObject.getJSONObject("_links").getJSONObject("awayTeam"));
                values.add(new ContentValues());
                values.get(i).put(DbContract.GameEntry._ID,id);
                values.get(i).put(DbContract.GameEntry.COLUMN_HOME_ID,homeId);
                values.get(i).put(DbContract.GameEntry.COLUMN_AWAY_ID,awayId);
                values.get(i).put(DbContract.GameEntry.COLUMN_HOME_SCORE,homeScore);
                values.get(i).put(DbContract.GameEntry.COLUMN_AWAY_SCORE,awayScore);
                values.get(i).put(DbContract.GameEntry.COLUMN_DATE,new SimpleDateFormat(DbContract.SQL_DATE_FORMAT, Locale.ENGLISH).format(date));
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

    public static List<ContentValues> getTable(String jsonString){
        List<ContentValues> values;
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonString);
            JSONArray tableArray = baseJsonResponse.getJSONArray("standing");
            int size = tableArray.length();
            values = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                JSONObject tableJsonObject = tableArray.getJSONObject(i);
                String id = getId(tableJsonObject.getJSONObject("_links").getJSONObject("team"));
                String position = tableJsonObject.getString("position");
                String playedGames = tableJsonObject.getString("playedGames");
                String points = tableJsonObject.getString("points");
                String goals = tableJsonObject.getString("goals");
                String goalsAgainst = tableJsonObject.getString("goalsAgainst");
                String goalDifference = tableJsonObject.getString("goalDifference");
                String losses = tableJsonObject.getString("losses");
                String draws = tableJsonObject.getString("draws");
                String wins = tableJsonObject.getString("wins");

                values.add(new ContentValues());
                values.get(i).put(DbContract.TeamEntry._ID,id);
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_POSITION,position);
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_PLAYED_GAMES,playedGames);
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_POINTS,points);
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_GOALS,goals);
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_GOALS_AGAINST,goalsAgainst);
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_GOAL_DIFFERENCE,goalDifference);
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_WINS,wins);
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_DRAWS,draws);
                values.get(i).put(DbContract.TeamEntry.COLUMN_TEAM_LOSSES,losses);
                Log.d(TAG, "getTable: " + values.get(i).toString());
            }
            return values;
        } catch (JSONException ex){
            Log.e("JSONParserUtil", "Problem parsing the matches JSON results: " + jsonString, ex);

        }
        return null;
    }

    /**
     * The URL the data is retrieved from: "http://api.football-data.org/v1/teams/61"
     */
    private static String getId(JSONObject gameJsonObject) throws JSONException {
        String href = gameJsonObject.getString("href");
        Uri uri = Uri.parse(href);
        int last =  uri.getPathSegments().size() - 1;
        return uri.getPathSegments().get(last);
    }

}
