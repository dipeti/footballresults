package com.dinya.peter.livefootballresults;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dinya.peter.livefootballresults.async.MatchLoader;
import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.database.DbHelper;
import com.dinya.peter.livefootballresults.entity.Match;
import com.dinya.peter.livefootballresults.entity.Team;
import com.dinya.peter.livefootballresults.listener.EndlessRecyclerViewScrollListener;
import com.dinya.peter.livefootballresults.lists.MatchAdapter;
import com.dinya.peter.livefootballresults.utils.JSONParserUtils;
import com.dinya.peter.livefootballresults.utils.NetworkUtils;
import com.dinya.peter.livefootballresults.utils.TestUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int UPCOMING_MATCHES_LOADER_ID = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView  mMatchesRecyclerView;
    MatchAdapter mMatchAdapter;
    private SQLiteDatabase mDb;
    private LoaderManager mLoaderManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mMatchesRecyclerView = (RecyclerView) findViewById(R.id.rv_my_matches);
        Button mButton = (Button) findViewById(R.id.bt_add_more);

        mMatchAdapter = new MatchAdapter();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMatchesRecyclerView.setLayoutManager(linearLayoutManager);
        mMatchesRecyclerView.setAdapter(mMatchAdapter);
        mMatchesRecyclerView.setHasFixedSize(true);
//        mMatchesRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                final int count = matchAdapter.getItemCount();
//                matches.addAll(JSONParserUtils.getMatches(responseJson));
//                view.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        matchAdapter.notifyItemRangeInserted(count,matches.size() - 1);
//                    }
//                });
//
//            }
//        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(DbContract.TeamEntry.CONTENT_URI_TEAMS,null,null,null,null);
                while (cursor.moveToNext()){
                    Log.d(TAG, "onCreate: " + cursor.getString(0) + " - " + cursor.getString(1));
                }
                cursor.close();
            }
        });

            mLoaderManager = getLoaderManager();
            mLoaderManager.initLoader(UPCOMING_MATCHES_LOADER_ID, null, this);
            DbHelper dbHelper = new DbHelper(this);
            mDb = dbHelper.getWritableDatabase();

        //TestUtils.insertFakeData(mDb);
            // TODO: inserts all the teams.
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String result = NetworkUtils.getResponseFromURL(NetworkUtils.buildTeamsURL());
//                ContentValues[] values =  JSONParserUtils.getTeams(result);
//                ContentResolver resolver = getContentResolver();
//                if (null != values){
//                    for (ContentValues value : values){
//                        resolver.insert(DbContract.TeamEntry.CONTENT_URI_TEAMS,value);
//                    }
//                }
//
//
//            }
//        }).start();
    }

    /**
     * ------------------
     * LoaderCallbacks
     * ------------------
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MatchLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMatchAdapter.swap(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMatchAdapter.swap(null);
    }

}
