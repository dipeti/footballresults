package com.dinya.peter.livefootballresults.entity;

import java.util.Date;



public class Player {
    private String name;

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public Date getContractUntil() {
        return contractUntil;
    }

    public String getMarketValue() {
        return marketValue;
    }

    private String position;
    private int jerseyNumber;
    private Date dateOfBirth;
    private String nationality;
    private Date contractUntil;
    private String marketValue;

    public Player(String name, String position, int jerseyNumber, Date dateOfBirth, String nationality, Date contractUntil, String marketValue) {
        this.name = name;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.contractUntil = contractUntil;
        this.marketValue = marketValue;
    }
}
