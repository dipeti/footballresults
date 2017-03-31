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
    private static final String SAMPLE_JSON = "{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/competitions/398/fixtures\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"}},\"count\":10,\"fixtures\":[{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147011\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/354\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/74\"}},\"date\":\"2015-10-03T11:45:00Z\",\"status\":\"FINISHED\",\"matchday\":8,\"homeTeamName\":\"Crystal Palace FC\",\"awayTeamName\":\"West Bromwich Albion FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":0},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147015\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/71\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/563\"}},\"date\":\"2015-10-03T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":8,\"homeTeamName\":\"Sunderland AFC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":2},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147014\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/68\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/338\"}},\"date\":\"2015-10-03T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":8,\"homeTeamName\":\"Norwich City FC\",\"awayTeamName\":\"Leicester City FC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":2},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147009\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/58\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/70\"}},\"date\":\"2015-10-03T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":8,\"homeTeamName\":\"Aston Villa FC\",\"awayTeamName\":\"Stoke City FC\",\"result\":{\"goalsHomeTeam\":0,\"goalsAwayTeam\":1},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147013\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/65\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/67\"}},\"date\":\"2015-10-03T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":8,\"homeTeamName\":\"Manchester City FC\",\"awayTeamName\":\"Newcastle United FC\",\"result\":{\"goalsHomeTeam\":6,\"goalsAwayTeam\":1},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147110\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/1044\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/346\"}},\"date\":\"2015-10-03T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":8,\"homeTeamName\":\"AFC Bournemouth\",\"awayTeamName\":\"Watford FC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":1},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147010\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/61\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/340\"}},\"date\":\"2015-10-03T16:30:00Z\",\"status\":\"FINISHED\",\"matchday\":8,\"homeTeamName\":\"Chelsea FC\",\"awayTeamName\":\"Southampton FC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":3},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147012\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/62\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/64\"}},\"date\":\"2015-10-04T12:30:00Z\",\"status\":\"FINISHED\",\"matchday\":8,\"homeTeamName\":\"Everton FC\",\"awayTeamName\":\"Liverpool FC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":1},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147016\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/72\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/73\"}},\"date\":\"2015-10-04T15:00:00Z\",\"status\":\"FINISHED\",\"matchday\":8,\"homeTeamName\":\"Swansea City FC\",\"awayTeamName\":\"Tottenham Hotspur FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":2},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147008\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/57\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/66\"}},\"date\":\"2015-10-04T15:00:00Z\",\"status\":\"FINISHED\",\"matchday\":8,\"homeTeamName\":\"Arsenal FC\",\"awayTeamName\":\"Manchester United FC\",\"result\":{\"goalsHomeTeam\":3,\"goalsAwayTeam\":0},\"odds\":null}]}";
    private static final String SAMPLE_JSON2 = "{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/competitions/398/fixtures\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"}},\"count\":10,\"fixtures\":[{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147005\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/73\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/64\"}},\"date\":\"2015-10-17T11:45:00Z\",\"status\":\"FINISHED\",\"matchday\":9,\"homeTeamName\":\"Tottenham Hotspur FC\",\"awayTeamName\":\"Liverpool FC\",\"result\":{\"goalsHomeTeam\":0,\"goalsAwayTeam\":0},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147001\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/62\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/66\"}},\"date\":\"2015-10-17T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":9,\"homeTeamName\":\"Everton FC\",\"awayTeamName\":\"Manchester United FC\",\"result\":{\"goalsHomeTeam\":0,\"goalsAwayTeam\":3},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/146999\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/61\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/58\"}},\"date\":\"2015-10-17T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":9,\"homeTeamName\":\"Chelsea FC\",\"awayTeamName\":\"Aston Villa FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":0},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147109\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/65\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/1044\"}},\"date\":\"2015-10-17T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":9,\"homeTeamName\":\"Manchester City FC\",\"awayTeamName\":\"AFC Bournemouth\",\"result\":{\"goalsHomeTeam\":5,\"goalsAwayTeam\":1},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147007\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/74\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/71\"}},\"date\":\"2015-10-17T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":9,\"homeTeamName\":\"West Bromwich Albion FC\",\"awayTeamName\":\"Sunderland AFC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":0},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147000\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/354\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/563\"}},\"date\":\"2015-10-17T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":9,\"homeTeamName\":\"Crystal Palace FC\",\"awayTeamName\":\"West Ham United FC\",\"result\":{\"goalsHomeTeam\":1,\"goalsAwayTeam\":3},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147003\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/340\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/338\"}},\"date\":\"2015-10-17T14:00:00Z\",\"status\":\"FINISHED\",\"matchday\":9,\"homeTeamName\":\"Southampton FC\",\"awayTeamName\":\"Leicester City FC\",\"result\":{\"goalsHomeTeam\":2,\"goalsAwayTeam\":2},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147006\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/346\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/57\"}},\"date\":\"2015-10-17T16:30:00Z\",\"status\":\"FINISHED\",\"matchday\":9,\"homeTeamName\":\"Watford FC\",\"awayTeamName\":\"Arsenal FC\",\"result\":{\"goalsHomeTeam\":0,\"goalsAwayTeam\":3},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147002\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/67\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/68\"}},\"date\":\"2015-10-18T15:00:00Z\",\"status\":\"FINISHED\",\"matchday\":9,\"homeTeamName\":\"Newcastle United FC\",\"awayTeamName\":\"Norwich City FC\",\"result\":{\"goalsHomeTeam\":6,\"goalsAwayTeam\":2},\"odds\":null},{\"_links\":{\"self\":{\"href\":\"http://api.football-data.org/v1/fixtures/147004\"},\"competition\":{\"href\":\"http://api.football-data.org/v1/competitions/398\"},\"homeTeam\":{\"href\":\"http://api.football-data.org/v1/teams/72\"},\"awayTeam\":{\"href\":\"http://api.football-data.org/v1/teams/70\"}},\"date\":\"2015-10-19T19:00:00Z\",\"status\":\"FINISHED\",\"matchday\":9,\"homeTeamName\":\"Swansea City FC\",\"awayTeamName\":\"Stoke City FC\",\"result\":{\"goalsHomeTeam\":0,\"goalsAwayTeam\":1},\"odds\":null}]}";
    private static final String TAG = JSONParserUtils.class.getSimpleName();

    private JSONParserUtils() {
    }

    public static List<Match> getMatches(String jsonString){
        ArrayList<Match> matches = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonString);
            JSONArray matchesArray = baseJsonResponse.getJSONArray("fixtures");
            for (int i = 0; i < matchesArray.length(); i++) {
                JSONObject matchJsonObject = matchesArray.getJSONObject(i);
                String homeTeam  = matchJsonObject.getString("homeTeamName");
                String awayTeam  = matchJsonObject.getString("awayTeamName");
                boolean finished = matchJsonObject.getString("status").equals("FINISHED");
                int homeScore = -1, awayScore = -1;
                if (finished)
                {
                    homeScore = matchJsonObject.getJSONObject("result").getInt("goalsHomeTeam");
                    awayScore = matchJsonObject.getJSONObject("result").getInt("goalsAwayTeam");
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
                Date date = simpleDateFormat.parse(matchJsonObject.getString("date"));
                Match match = new Match(homeTeam,awayTeam, homeScore,awayScore,date);
                matches.add(match);
            }          
        }
        catch (JSONException ex){
            Log.e("JSONParserUtil", "Problem parsing the matches JSON results: " + jsonString, ex);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return matches;
    }

    public static ContentValues[] getTeams(String jsonString){
        ContentValues values[];
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonString);
            JSONArray matchesArray = baseJsonResponse.getJSONArray("teams");
            int size = matchesArray.length();
            values = new ContentValues[size];
            for (int i = 0; i < size; i++) {
                JSONObject matchJsonObject = matchesArray.getJSONObject(i);
                String name  = matchJsonObject.getString("name");
                String code  = matchJsonObject.getString("code");
                String value = matchJsonObject.getString("squadMarketValue");
                String href = matchJsonObject.getJSONObject("_links").getJSONObject("self").getString("href");
                Uri uri = Uri.parse(href);
                int last =  uri.getPathSegments().size() - 1;
                String path = uri.getPathSegments().get(last);
                values[i] = new ContentValues();
                values[i].put(DbContract.TeamEntry.COLUMN_TEAM_NAME,name);
                values[i].put(DbContract.TeamEntry.COLUMN_TEAM_CODE,code);
                values[i].put(DbContract.TeamEntry._ID,path);
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

}
