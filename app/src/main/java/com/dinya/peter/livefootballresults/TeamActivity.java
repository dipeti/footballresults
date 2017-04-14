package com.dinya.peter.livefootballresults;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dinya.peter.livefootballresults.async.PlayerLoader;
import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.entity.Player;
import com.dinya.peter.livefootballresults.lists.PlayerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Player>> {

    private static final int PLAYERS_LOADER_ID = 1;

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private TextView mEmptyView;

    private List<Player> mPlayers;
    private PlayerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        /**
         * setting up RecyclerView
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_team_players);
        mPlayers = new ArrayList<>();
        mAdapter = new PlayerAdapter(mPlayers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        /**
         * setting up Toolbar
         */
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * Retrieving the id from the intent.
         * The id is used to query the name and value of the team.
         */
        long id = getIntent().getLongExtra(MainActivity.INTENT_EXTRA_ID,0);
        Uri uri = ContentUris.withAppendedId(DbContract.TeamEntry.CONTENT_URI_TEAMS,id);
        Cursor cursor = getContentResolver().query(
                uri,
                null,
                DbContract.TeamEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null);

        if(cursor != null && cursor.moveToFirst()) {
            getSupportActionBar().setTitle(cursor.getString(cursor.getColumnIndex(DbContract.TeamEntry.COLUMN_TEAM_NAME)));
            getSupportActionBar().setSubtitle(getString(R.string.team_value,cursor.getString(cursor.getColumnIndex(DbContract.TeamEntry.COLUMN_TEAM_VALUE))));
            cursor.close();
        }
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mEmptyView = (TextView) findViewById(R.id.tv_empty_view);

        /**
         * LoaderInit
         */
        Bundle bundle = new Bundle();
        bundle.putLong(MainActivity.INTENT_EXTRA_ID, id);
        getLoaderManager().initLoader(PLAYERS_LOADER_ID,bundle,this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /**
         * To save the states of list items (expanded/collapsed).
         */
        mAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        /**
         * To restore the states of list items (expanded/collapsed).
         */
        mAdapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Loader<List<Player>> onCreateLoader(int id, Bundle args) {
        switch (id){
            case PLAYERS_LOADER_ID:
                showProgressBar();
                return new PlayerLoader(this,args.getLong(MainActivity.INTENT_EXTRA_ID));

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    /**
     * ------------------
     * LoaderCallbacks
     * ------------------
     */
    @Override
    public void onLoadFinished(Loader<List<Player>> loader, List<Player> data) {
        if (null != data && 0 != data.size()){
                Collections.sort(data);
                mPlayers.addAll(data);
                mAdapter.notifyParentRangeInserted(0,data.size());
                showPlayerData();
        } else {
            showEmptyView();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Player>> loader) {
        mProgressBar.setVisibility(View.INVISIBLE);
        int size = mPlayers.size();
        mPlayers.clear();
        mAdapter.notifyParentRangeRemoved(0,size);
    }

    /**
     * ------------------
     * Helper methods
     * ------------------
     */
    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar(){
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void showPlayerData(){
        mEmptyView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


}
