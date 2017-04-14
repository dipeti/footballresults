package com.dinya.peter.livefootballresults.entity;

import android.support.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.dinya.peter.livefootballresults.utils.JSONParserUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Player implements Parent<Player.PlayerExtraDetail>, Comparable<Player>{

    private String name;
    private String position;
    private int jerseyNumber;
    private List<PlayerExtraDetail> details;

    public Player(String name, String position, int jerseyNumber, Date dateOfBirth, String nationality, Date contractUntil, String marketValue) {
        this.name = name;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        details = new ArrayList<>();
        details.add(new PlayerExtraDetail(marketValue,contractUntil,dateOfBirth,nationality));;
    }

    @Override
    public List<PlayerExtraDetail> getChildList() {
        return details;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public int compareTo(@NonNull Player o) {
        return Integer.compare(getJerseyNumber(), o.getJerseyNumber());
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    /**
     * ExtraDetails displayed in the expanded part of the list.
     */
    public class PlayerExtraDetail{
        private String marketValue;
        private Date contractUntil;
        private Date dateOfBirth;
        private String nationality;

        PlayerExtraDetail(String marketValue, Date contractUntil, Date dateOfBirth, String nationality) {
            this.marketValue = marketValue;
            this.contractUntil = contractUntil;
            this.dateOfBirth = dateOfBirth;
            this.nationality = nationality;
        }

        public String  getFormattedDateOfBirth(){
            if (null != dateOfBirth){
                return SimpleDateFormat.getDateInstance().format(dateOfBirth);
            }
            return JSONParserUtils.NOT_AVAILABLE;
        }
        public String getMarketValue() {
            return marketValue;
        }
        public String  getFormattedContractUntil(){
            if (null != contractUntil){
                return SimpleDateFormat.getDateInstance().format(contractUntil);
            }
            return JSONParserUtils.NOT_AVAILABLE;

        }
        public String getNationality() {
            return nationality;
        }
    }
}
