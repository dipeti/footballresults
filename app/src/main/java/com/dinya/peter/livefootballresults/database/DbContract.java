package com.dinya.peter.livefootballresults.database;


import android.net.Uri;
import android.provider.BaseColumns;

public class DbContract {

    public static final String AUTHORITY = "com.dinya.peter.livefootballresults";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_GAMES = "games";
    public static final String PATH_UPCOMING_GAMES = "games/upcoming";
    public static final String PATH_FAVORITE_GAMES = "games/favorite";
    public static final String PATH_TEAMS = "teams";

    public static final class TeamEntry implements BaseColumns{
        public static final Uri CONTENT_URI_TEAMS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TEAMS).build();

        public static final String TABLE_NAME = "teams";
        public static final String COLUMN_TEAM_NAME = "teamName";
        public static final String COLUMN_TEAM_SHORT_NAME = "teamShortName";
        public static final String COLUMN_TEAM_CODE = "teamCode";
        public static final String COLUMN_TEAM_VALUE = "teamValue";

        public static final String ALIAS_TABLE_FIRST = "t1";
        public static final String ALIAS_TABLE_SECOND = "t2";

        public static final String ALIAS_HOME_TEAM = "homeTeam";
        public static final String ALIAS_AWAY_TEAM = "awayTeam";
    }

    public static final class GameEntry implements BaseColumns{
        public static final Uri CONTENT_URI_GAMES = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAMES).build();

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
}
