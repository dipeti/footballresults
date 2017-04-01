package com.dinya.peter.livefootballresults.async;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.entity.Match;
import com.dinya.peter.livefootballresults.utils.JSONParserUtils;
import com.dinya.peter.livefootballresults.utils.NetworkUtils;

import java.net.URL;
import java.util.List;


public class MatchLoader extends AsyncTaskLoader<Cursor> {
    Cursor result;
    public MatchLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(null != result){
            deliverResult(result);
        } else if(NetworkUtils.isConnected(getContext())) {
            forceLoad();
        } else{
            Toast.makeText(getContext(),"No internet connection!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void deliverResult(Cursor data) {
        result = data;
        super.deliverResult(data);
    }

    @Override
    public Cursor loadInBackground() {
//        URL url = NetworkUtils.buildUpcomingMatchesURL();
//        String response = NetworkUtils.getResponseFromURL(url);
//        ContentValues[] values = JSONParserUtils.getGames(response);
        ContentResolver resolver = getContext().getContentResolver();
//        if (values != null) {
//            for (ContentValues value : values){
//               resolver.insert(DbContract.GameEntry.CONTENT_URI_GAMES,value);
//            }
//        }
        return resolver.query(DbContract.GameEntry.CONTENT_URI_GAMES,null,null,null,null);
    }


}

