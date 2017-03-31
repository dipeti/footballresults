package com.dinya.peter.livefootballresults.database;


import android.provider.BaseColumns;

public class DbContract {

    public static final class TeamEntry implements BaseColumns{
        public static final String TABLE_NAME = "teams";
        public static final String COLUMN_TEAM_NAME = "teamName";
        public static final String COLUMN_TEAM_CODE = "teamCode";
        public static final String COLUMN_TEAM_VALUE = "teamValue";
    }

    public static final class GameEntry implements BaseColumns{
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

    }

    private DbContract(){};
}
