package com.dinya.peter.livefootballresults.async;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.entity.Match;
import com.dinya.peter.livefootballresults.entity.Player;
import com.dinya.peter.livefootballresults.utils.JSONParserUtils;
import com.dinya.peter.livefootballresults.utils.NetworkUtils;

import java.net.URL;
import java.util.List;


public class PlayerLoader extends AsyncTaskLoader<List<Player>> {
    List<Player> result;
    long mId;
    public PlayerLoader(Context context, long id) {
        super(context);
        mId = id;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(null != result){
            deliverResult(result);
        }
        forceLoad();


    }

    @Override
    public void deliverResult(List<Player> data) {
        result = data;
        super.deliverResult(data);
    }

    @Override
    public List<Player> loadInBackground() {
        if (NetworkUtils.isConnected(getContext())){
            URL url = NetworkUtils.buildPlayersForTeamURL(mId);
            String response = NetworkUtils.getResponseFromURL(url);
            List<Player> values = JSONParserUtils.getPlayers(response);
            return values;
        }
        return null;
    }





}

