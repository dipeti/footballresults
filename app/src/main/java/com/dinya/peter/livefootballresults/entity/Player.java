package com.dinya.peter.livefootballresults.entity;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Player implements Parent<Player.PlayerExtraDetail> {


    private String name;
    private String position;
    private int jerseyNumber;
    private String nationality;
    private List<PlayerExtraDetail> details;


    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }



    public String getNationality() {
        return nationality;
    }




    public Player(String name, String position, int jerseyNumber, Date dateOfBirth, String nationality, Date contractUntil, String marketValue) {
        this.name = name;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.nationality = nationality;
        details = new ArrayList<>();
        details.add(new PlayerExtraDetail(marketValue,contractUntil,dateOfBirth));;
    }

    @Override
    public List<PlayerExtraDetail> getChildList() {
        return details;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public class PlayerExtraDetail{
        private String marketValue;
        private Date contractUntil;
        private Date dateOfBirth;

        PlayerExtraDetail(String marketValue, Date contractUntil, Date dateOfBirth) {
            this.marketValue = marketValue;
            this.contractUntil = contractUntil;
            this.dateOfBirth = dateOfBirth;
        }

        public Date getDateOfBirth() {
            return dateOfBirth;
        }
        public String getMarketValue() {
            return marketValue;
        }
        public Date getContractUntil() {
            return contractUntil;
        }
    }
}
