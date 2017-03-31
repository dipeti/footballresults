package com.dinya.peter.livefootballresults.entity;


public class Team {

    private String name;
    private String code;
    private int value;

    public Team(String name, String code, int value) {
        this.name = name;
        this.code = code;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
