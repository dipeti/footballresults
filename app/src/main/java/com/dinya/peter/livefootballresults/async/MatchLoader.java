package com.dinya.peter.livefootballresults.async;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.widget.Toast;

import com.dinya.peter.livefootballresults.entity.Match;
import com.dinya.peter.livefootballresults.utils.JSONParserUtils;
import com.dinya.peter.livefootballresults.utils.NetworkUtils;

import java.net.URL;
import java.util.List;


public class MatchLoader extends AsyncTaskLoader<List<Match>> {
    List<Match> result;
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
    public void deliverResult(List<Match> data) {
        result = data;
        super.deliverResult(data);
    }

    @Override
    public List<Match> loadInBackground() {
        URL url = NetworkUtils.buildUpcomingMatchesURL();
        String response = NetworkUtils.getResponseFromURL(url);
        return JSONParserUtils.getMatches(response);
    }


}

