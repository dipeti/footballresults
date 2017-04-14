package com.dinya.peter.livefootballresults;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dinya.peter.livefootballresults.async.PlayerLoader;
import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.entity.Player;
import com.dinya.peter.livefootballresults.lists.PlayerAdapter;
import com.dinya.peter.livefootballresults.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeamActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Player>> {

    RecyclerView mRecyclerView;
    PlayerAdapter mAdapter;
    ImageView mLogo;
    TextView mName;
    List<Player> mPlayers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        long id = getIntent().getLongExtra("id",66);

        mLogo = (ImageView) findViewById(R.id.team_logo);
        mName = (TextView) findViewById(R.id.team_name);
        Uri uri = ContentUris.withAppendedId(DbContract.TeamEntry.CONTENT_URI_TEAMS,id);
        Cursor cursor = getContentResolver().query(uri,null, DbContract.TeamEntry._ID + " = ?",new String[]{String.valueOf(id)},null);
        if(cursor != null && cursor.moveToFirst()) {
            mName.setText(cursor.getString(cursor.getColumnIndex(DbContract.TeamEntry.COLUMN_TEAM_NAME)));
            cursor.close();
        }
        mLogo.setImageResource(ResourceUtils.getLogoResource((int)id));

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_team_players);
        mPlayers = new ArrayList<>();
       // mPlayers.add(new Player("Laci","",5,new Date(System.currentTimeMillis()),"",new Date(System.currentTimeMillis()),""));
        mAdapter = new PlayerAdapter(mPlayers);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        getLoaderManager().initLoader(5,bundle,this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Loader<List<Player>> onCreateLoader(int id, Bundle args) {
        return new PlayerLoader(this, args.getLong("id"));
    }

    @Override
    public void onLoadFinished(Loader<List<Player>> loader, List<Player> data) {
        if (null != data){
            mPlayers.addAll(data);
            mAdapter.notifyParentRangeInserted(0,data.size());
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Player>> loader) {

    }
}
