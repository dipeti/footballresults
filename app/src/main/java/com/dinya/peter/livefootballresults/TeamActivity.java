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
import com.dinya.peter.livefootballresults.utils.ResourceUtils;

import java.util.List;

public class TeamActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Player>> {

    ListView mListView;
    ArrayAdapter<Player> mAdapter;
    ImageView mLogo;
    TextView mName;
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

        mListView = (ListView) findViewById(R.id.rv_team_players);
        mAdapter = new ArrayAdapter<Player>(this,R.layout.player_list_item){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                Player player = getItem(position);
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.player_list_item, parent, false);
                }
                TextView tvJersey = (TextView) convertView.findViewById(R.id.player_item_jersey);
                TextView tvName = (TextView) convertView.findViewById(R.id.player_item_name);
                TextView tvPosition = (TextView) convertView.findViewById(R.id.player_item_position);
                TextView tvNationality = (TextView) convertView.findViewById(R.id.player_item_nationality);
                // Populate the data into the template view using the data object
                tvName.setText(player.getName());
                tvJersey.setText(String.valueOf(player.getJerseyNumber()));
                tvPosition.setText(player.getPosition());
                tvNationality.setText(player.getNationality());

                // Return the completed view to render on screen
                return convertView;


            }
        };
        mListView.setAdapter(mAdapter);
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        getLoaderManager().initLoader(5,bundle,this);
    }

    @Override
    public Loader<List<Player>> onCreateLoader(int id, Bundle args) {
        return new PlayerLoader(this, args.getLong("id"));
    }

    @Override
    public void onLoadFinished(Loader<List<Player>> loader, List<Player> data) {
        mAdapter.clear();
        if (null != data)
        mAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Player>> loader) {

    }
}
