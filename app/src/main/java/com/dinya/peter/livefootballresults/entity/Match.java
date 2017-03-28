package com.dinya.peter.livefootballresults.entity;


import java.util.Date;

public class Match {
    private String homeTeam, awayTeam;
    private int homeScore, awayScore;
    private Date date;

    public Date getDate() {
        return date;
    }

    public Match(String homeTeam, String awayTeam, int homeScore, int awayScore, Date date) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.date = date;
    }

    public Match(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    @Override
    public String toString() {
        return homeTeam + " " + homeScore + " : " + awayScore + " "  + awayTeam;
    }
}
