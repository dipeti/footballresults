package com.dinya.peter.livefootballresults.database;


import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DbContract {

    private static final String TAG = DbContract.class.getSimpleName();
    public static final String AUTHORITY = "com.dinya.peter.livefootballresults";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_GAMES = "games";
    public static final String PATH_UPCOMING_GAMES = "upcoming";
    public static final String PATH_FINISHED_GAMES = "finished";
    public static final String PATH_FAVORITE_GAMES = "favorite";
    public static final String PATH_TEAMS = "teams";
    public static final String PATH_TABLE = "table";


    public static final String SQL_DATE_FORMAT = "yyyy-MM-dd HH:mm";


    public static final class TeamEntry implements BaseColumns{
        public static final Uri CONTENT_URI_TEAMS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEAMS).build();
        public static final Uri CONTENT_URI_TABLE = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TABLE).build();

        /**
         * TEAM INFO
         */
        public static final String TABLE_NAME = "teams";
        public static final String COLUMN_TEAM_NAME = "teamName";
        public static final String COLUMN_TEAM_SHORT_NAME = "teamShortName";
        public static final String COLUMN_TEAM_CODE = "teamCode";
        public static final String COLUMN_TEAM_VALUE = "teamValue";

        /**
         * TABLE INFO
         */
        public static final String COLUMN_TEAM_POSITION = "position";
        public static final String COLUMN_TEAM_PLAYED_GAMES = "playedGames";
        public static final String COLUMN_TEAM_POINTS = "points";
        public static final String COLUMN_TEAM_GOALS = "goals";
        public static final String COLUMN_TEAM_GOALS_AGAINST = "goalsAgainst";
        public static final String COLUMN_TEAM_GOAL_DIFFERENCE = "goalDiff";
        public static final String COLUMN_TEAM_WINS = "wins";
        public static final String COLUMN_TEAM_DRAWS = "draws";
        public static final String COLUMN_TEAM_LOSSES = "loses";

        /**
         * ALIASES
         * used in SQL statements
         */
        public static final String ALIAS_TABLE_FIRST = "t1";
        public static final String ALIAS_TABLE_SECOND = "t2";

        public static final String ALIAS_HOME_ID = "homeId";
        public static final String ALIAS_AWAY_ID = "awayId";
        public static final String ALIAS_HOME_TEAM = "homeTeam";
        public static final String ALIAS_AWAY_TEAM = "awayTeam";
    }

    public static final class GameEntry implements BaseColumns{
        public static final Uri CONTENT_URI_GAMES = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAMES).build();
        public static final Uri CONTENT_URI_UPCOMING_GAMES = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAMES).appendPath(PATH_UPCOMING_GAMES).build();
        public static final Uri CONTENT_URI_FINISHED_GAMES = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAMES).appendPath(PATH_FINISHED_GAMES).build();

        public static final String TABLE_NAME = "games";
        public static final String COLUMN_HOME_ID = "homeId";
        public static final String COLUMN_AWAY_ID = "awayId";
        public static final String COLUMN_HOME_SCORE = "homeScore";
        public static final String COLUMN_AWAY_SCORE = "awayScore";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_MATCH_DAY = "matchDay";
        public static final String COLUMN_HOME_ODDS = "homeOdds";
        public static final String COLUMN_DRAW_ODDS = "drawOdds";
        public static final String COLUMN_AWAY_ODDS = "awayOdds";
        public static final String COLUMN_FAVORITE = "favorite";

        public static final String ALIAS_TABLE_FIRST = "g1";
        public static final String ALIAS_TABLE_SECOND = "g2";

    }

    private DbContract(){};

    public static String[] getDateSelectionArgs(int dayDiff){
        SimpleDateFormat sdf = new SimpleDateFormat(DbContract.SQL_DATE_FORMAT, Locale.UK);
        Calendar calendar = Calendar.getInstance();
        Log.e(TAG, "getDateSelectionArgs: " +TimeUnit.DAYS.toMillis(dayDiff));
        long dateInLong = System.currentTimeMillis()+TimeUnit.DAYS.toMillis(dayDiff);
        calendar.setTimeInMillis(dateInLong);
        String currentDate = sdf.format(calendar.getTime());
       return new String[]{currentDate};
    }
}
