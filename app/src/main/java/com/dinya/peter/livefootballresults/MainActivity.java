package com.dinya.peter.livefootballresults;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Match>> {

    private static final int UPCOMING_MATCHES_LOADER_ID = 1;

    private RecyclerView  mMatchesRecyclerView;
    MatchAdapter mMatchAdapter;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mMatchesRecyclerView = (RecyclerView) findViewById(R.id.rv_my_matches);
        Button mButton = (Button) findViewById(R.id.bt_add_more);

        mMatchAdapter = new MatchAdapter(new ArrayList<Match>());
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
                TestUtils.insertFakeData(mDb); }
        });

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(UPCOMING_MATCHES_LOADER_ID, null, this);
            DbHelper dbHelper = new DbHelper(this);
            mDb = dbHelper.getWritableDatabase();

        TestUtils.insertFakeData(mDb);
        Cursor cursor = getAllTeams();
    }

    /**
     * ------------------
     * LoaderCallbacks
     * ------------------
     */
    @Override
    public Loader<List<Match>> onCreateLoader(int id, Bundle args) {
      return new AsyncTaskLoader<List<Match>>(this) {
          List<Match> result;
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
      };
    }

    @Override
    public void onLoadFinished(Loader<List<Match>> loader, List<Match> data) {
        mMatchAdapter.swap(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Match>> loader) {
        mMatchAdapter.swap(new ArrayList<Match>());
    }

    public Cursor getAllTeams() {
        return mDb.query(DbContract.TeamEntry.TABLE_NAME,null,null,null,null,null, DbContract.TeamEntry.COLUMN_TEAM_NAME);
    }
}
