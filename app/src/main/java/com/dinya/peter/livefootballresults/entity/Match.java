package com.dinya.peter.livefootballresults.entity;



public class Match {
    private String homeTeam, awayTeam;
    private int homeScore, awayScore;

    public Match(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    @Override
    public String toString() {
        return homeTeam + " " + homeScore + " : " + awayScore + " "  + awayTeam;
    }
}
