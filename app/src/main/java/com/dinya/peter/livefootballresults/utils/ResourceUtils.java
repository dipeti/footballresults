package com.dinya.peter.livefootballresults.utils;

import com.dinya.peter.livefootballresults.R;

public class ResourceUtils {

    private static final int HULLCITY = 322;
    private static final int LEISTERCITY = 338;
    private static final int SOTON = 340;
    private static final int WATFORD = 346;
    private static final int MIDDLESBROUGH = 343;
    private static final int STOKE = 70;
    private static final int EVERTON = 62;
    private static final int TOTTENHAM = 73;
    private static final int CRYTALPALACE = 354;
    private static final int WESTBROM = 74;
    private static final int BURNLEY = 328;
    private static final int SWANSEA = 72;
    private static final int MANCITY = 65;
    private static final int SUNDERLAND = 71;
    private static final int BOURNEMOUTH = 1044;
    private static final int MANUTD = 66;
    private static final int ARSENAL = 57;
    private static final int LIVERPOOL = 64;
    private static final int CHELSEA = 61;
    private static final int WESTHAM = 563;
    private static final int HUDDERSFIELD = 394;
    private static final int NEWCASTLE = 67;
    private static final int BRIGHTON = 397;
    private ResourceUtils(){}

    public static int getLogoResource(int teamId){
        switch (teamId){
            case HULLCITY: return R.drawable.logo_hull_city;
            case SOTON: return R.drawable.logo_fc_southampton;
            case MIDDLESBROUGH: return R.drawable.logo_middlesbrough;
            case STOKE: return R.drawable.logo_stoke_city;
            case EVERTON: return R.drawable.logo_everton_fc;
            case TOTTENHAM: return R.drawable.logo_tottenham_hotspur;
            case WESTBROM: return R.drawable.logo_west_bromwich;
            case WATFORD: return R.drawable.logo_watford;
            case SWANSEA: return R.drawable.logo_swansea;
            case BOURNEMOUTH: return R.drawable.logo_bournemouth;
            case MANUTD: return R.drawable.logo_man_utd;
            case ARSENAL: return R.drawable.logo_arsenal_fc;
            case LIVERPOOL: return R.drawable.logo_fc_liverpool;
            case CHELSEA: return R.drawable.logo_chelsea_fc;
            case WESTHAM: return R.drawable.logo_west_ham_united_fc;
            case MANCITY: return R.mipmap.logo_man_city;
            case SUNDERLAND: return R.drawable.logo_sunderland;
            case LEISTERCITY: return R.mipmap.logo_leicester_city;
            case BURNLEY: return R.mipmap.logo_burnley;
            case CRYTALPALACE: return R.mipmap.logo_crystal_palace;
            case BRIGHTON: return R.drawable.logo_brighton;
            case NEWCASTLE: return R.drawable.logo_newcastle_united;
            case HUDDERSFIELD: return R.mipmap.logo_huddersfield;
            default: return R.mipmap.ic_launcher;
        }
    }
}
