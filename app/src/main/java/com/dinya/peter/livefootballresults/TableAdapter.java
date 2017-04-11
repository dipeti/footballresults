package com.dinya.peter.livefootballresults;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinya.peter.livefootballresults.TableFragment.OnListFragmentInteractionListener;
import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.dummy.DummyContent.DummyItem;
import com.dinya.peter.livefootballresults.utils.ResourceUtils;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private Cursor mCursor;
//    private final OnListFragmentInteractionListener mListener;

    public TableAdapter() {
//        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(mCursor.getColumnIndex(DbContract.TeamEntry._ID));
        String teamPosition = mCursor.getString(mCursor.getColumnIndex(DbContract.TeamEntry.COLUMN_TEAM_POSITION));
        String teamName = mCursor.getString(mCursor.getColumnIndex(DbContract.TeamEntry.COLUMN_TEAM_NAME));
        String teamPoints = mCursor.getString(mCursor.getColumnIndex(DbContract.TeamEntry.COLUMN_TEAM_POINTS));
        String teamPlayedGames = mCursor.getString(mCursor.getColumnIndex(DbContract.TeamEntry.COLUMN_TEAM_PLAYED_GAMES));

        holder.mPosition.setText(teamPosition);
        holder.mPlayedGames.setText(teamPlayedGames);
        holder.mPoints.setText(teamPoints);
        holder.mTeam.setText(teamName);
        holder.mLogo.setImageResource(ResourceUtils.getLogoResource(id));

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(id);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swap(Cursor cursor)
    {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView mPosition;
        public ImageView mLogo;
        public TextView mTeam;
        public TextView mPlayedGames;
        public TextView mPoints;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTeam = (TextView) mView.findViewById(R.id.table_item_team);
            mLogo = (ImageView) mView.findViewById(R.id.table_item_logo);
            mPosition = (TextView) mView.findViewById(R.id.table_item_position);
            mPoints = (TextView) mView.findViewById(R.id.table_item_points);
            mPlayedGames = (TextView) mView.findViewById(R.id.table_item_played_games);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}