package com.dinya.peter.livefootballresults.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class NetworkUtils {

    /**
     * Tag
     */
    private static final String TAG = NetworkUtils.class.getSimpleName();

    /**
     * URLs
     */
    private static final String BASE_API_URL = "http://api.football-data.org";
    private static final String FIXTURES_API_PATH = "v1/competitions/426/fixtures";
    private static final String TEAMS_API_PATH = "v1/competitions/426/teams";
    private static final String TABLE_API_PATH = "v1/competitions/426/leagueTable";
    /**
     * Parameters
     */
    private static final int UPCOMING_DAYS = 7;
    private static final int PAST_DAYS = 7;
    private static final String UPCOMING_DAYS_VALUE = "n" + UPCOMING_DAYS ;
    private static final String PAST_DAYS_VALUE = "p" + PAST_DAYS ;
    private static final String TIMEFRAME_PARAM = "timeFrame";

    /**
     * Builds a URL for querying the upcoming matches
     */
    public static URL buildUpcomingMatchesURL(){
        Uri queryURi = Uri.parse(BASE_API_URL)
                .buildUpon()
                .appendEncodedPath(FIXTURES_API_PATH)
                .appendQueryParameter(TIMEFRAME_PARAM, UPCOMING_DAYS_VALUE)
                .build();
        try {
            URL url = new URL(queryURi.toString());
            Log.v(TAG, "Converted URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot convert Uri to URL: " + queryURi.toString());
            return null;
        }
    }

    public static URL buildFinishedMatchesURL(){
        Uri queryURi = Uri.parse(BASE_API_URL)
                .buildUpon()
                .appendEncodedPath(FIXTURES_API_PATH)
                .appendQueryParameter(TIMEFRAME_PARAM, PAST_DAYS_VALUE)
                .build();
        try {
            URL url = new URL(queryURi.toString());
            Log.v(TAG, "Converted URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot convert Uri to URL: " + queryURi.toString());
            return null;
        }
    }

    public static URL buildTeamsURL(){
        Uri queryURi = Uri.parse(BASE_API_URL)
                .buildUpon()
                .appendEncodedPath(TEAMS_API_PATH)
                .build();
        try {
            URL url = new URL(queryURi.toString());
            Log.v(TAG, "Converted URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot convert Uri to URL: " + queryURi.toString());
            return null;
        }
    }

    public static URL buildTableURL(){
        Uri queryURi = Uri.parse(BASE_API_URL)
                .buildUpon()
                .appendEncodedPath(TABLE_API_PATH)
                .build();
        try {
            URL url = new URL(queryURi.toString());
            Log.v(TAG, "Converted URL: " + url);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot convert Uri to URL: " + queryURi.toString());
            return null;
        }
    }

    public static String getResponseFromURL(URL url){
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException ex){
            Log.e(TAG, "Error occured during HTTP Request: " + ex.getMessage());
        }
        return jsonResponse;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection httpURLConnection = null;
        InputStream is = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("X-Auth-Token","9b027c0d47644438a70d9e3be9dcc9ee");
            httpURLConnection.connect();

            if (HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()){
                is = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(is);
            } else  {
                Log.e(TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != httpURLConnection){
                httpURLConnection.disconnect();
            }
            /*
             * Closing the input stream could throw an IOException, which is why
             * the makeHttpRequest(URL url) method signature specifies than an IOException
             * could be thrown.
             */
            if (null != is){
                is.close();
            }
        }
        return jsonResponse;
    }

    /**
     * The InputStream is an abstraction. We do not know if it is a image, website or plain text. It is just a stream of information.
     * We need to reassable the bytes into something meaningful. For text data the bytes need to be converted back into characters.
     * BufferedReader class help us.
     */
    private static String readFromStream(InputStream is) throws IOException {
        StringBuilder output = new StringBuilder();
        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    public static boolean isConnected(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (null != networkInfo && networkInfo.isConnected());
    }


}
