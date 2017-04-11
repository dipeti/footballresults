package com.dinya.peter.livefootballresults;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private Context mContext;
//    private final OnListFragmentInteractionListener mListener;

    public TableAdapter(Context context) {
        mContext = context;
//        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.table_list_item, parent, false);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int pos = Integer.parseInt((String) holder.mPosition.getText());
            switch (pos){
                case 1:
                case 2:
                case 3: holder.mPosition.setBackgroundColor(mContext.getResources().getColor(R.color.CL_group,null));
                    holder.mPosition.setTextColor(mContext.getResources().getColor(R.color.white,null));
                    break;
                case 4:  holder.mPosition.setBackgroundColor(mContext.getResources().getColor(R.color.CL_qualification,null));
                    holder.mPosition.setTextColor(mContext.getResources().getColor(R.color.white,null));
                    break;
                case 5:
                case 6: holder.mPosition.setBackgroundColor(mContext.getResources().getColor(R.color.EL_group,null));
                    holder.mPosition.setTextColor(mContext.getResources().getColor(R.color.white,null));
                    break;
                case 7: holder.mPosition.setBackgroundColor(mContext.getResources().getColor(R.color.EL_qualification,null));
                    holder.mPosition.setTextColor(mContext.getResources().getColor(R.color.white,null));
                    break;
                case 18:
                case 19:
                case 20: holder.mPosition.setBackgroundColor(mContext.getResources().getColor(R.color.relegation,null));
                    holder.mPosition.setTextColor(mContext.getResources().getColor(R.color.white,null)); break;
                default:holder.mPosition.setBackground(null);
                    holder.mPosition.setTextColor(mContext.getResources().getColor(android.R.color.tertiary_text_dark,null));

            }
        }

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
