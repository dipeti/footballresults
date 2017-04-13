package com.dinya.peter.livefootballresults.utils;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dinya.peter.livefootballresults.MainActivity;
import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.lists.MatchAdapter;
import com.dinya.peter.livefootballresults.sync.BackgroundSyncUtils;

import java.util.Arrays;


abstract public class GamesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener,
        MatchAdapter.onGameClickListener {

    protected RecyclerView mRecyclerView;
    protected TextView mEmptyView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected Toast mToast;

    protected MatchAdapter mAdapter;
    protected LoaderManager mLoaderManager;
    protected RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (0 == mAdapter.getItemCount()) {
                mRecyclerView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            }
        }
    };

    public GamesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new MatchAdapter(this);
        mAdapter.registerAdapterDataObserver(mAdapterDataObserver);
        mLoaderManager = getActivity().getLoaderManager();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }
    protected void fetchData() {
        if(!BackgroundSyncUtils.startImmediateSync(getContext()))
            mSwipeRefreshLayout.setRefreshing(false);
    }
    /**
     * ------------------
     * LoaderCallbacks
     * ------------------
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mEmptyView.setVisibility(View.INVISIBLE);
        String[] selectionArgs;
        switch (id){

            case MainActivity.UPCOMING_GAMES_LOADER_ID:
                int daysAhead = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("days_ahead", "5"));
                selectionArgs = DbContract.getDateSelectionArgs(daysAhead); // games in the upcoming 'dayDiff' days
                return new CursorLoader(getActivity(),DbContract.GameEntry.CONTENT_URI_UPCOMING_GAMES,null, null, selectionArgs,null);

            case MainActivity.FINISHED_GAMES_LOADER_ID:
                int daysPast = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("days_past", "5"));
                selectionArgs = DbContract.getDateSelectionArgs(-daysPast); // games in the past 'dayDiff' days
                return new CursorLoader(getActivity(),DbContract.GameEntry.CONTENT_URI_FINISHED_GAMES,null, null, selectionArgs,null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swap(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swap(null);
    }

    /*
     * ------------------
     * OnSharedPreferenceChangeListener
     * ------------------
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mLoaderManager.restartLoader(MainActivity.UPCOMING_GAMES_LOADER_ID,null,this);
    }

    @Override
    public void onClick(long id) {
        Uri uri = ContentUris.withAppendedId(DbContract.GameEntry.CONTENT_URI_GAMES,id);
        Cursor cursor = getContext().getContentResolver().query(uri,null, DbContract.GameEntry._ID + " = ?",new String[]{String.valueOf(id)},null);
        String homeOdds, awayOdds, drawOdds;
        if(cursor != null && cursor.moveToFirst()){
            homeOdds = cursor.getString(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_HOME_ODDS));
            awayOdds = cursor.getString(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_AWAY_ODDS));
            drawOdds = cursor.getString(cursor.getColumnIndex(DbContract.GameEntry.COLUMN_DRAW_ODDS));
            String text = "Home: " + homeOdds + " \t\t|\t\t Draw: " + drawOdds + " \t\t|\t\t Away: " + awayOdds;
            if (null != mToast ){
                mToast.cancel();
            }
            mToast = Toast.makeText(getContext(),text,Toast.LENGTH_LONG);
            mToast.show();
            cursor.close();
        }
    }
}
