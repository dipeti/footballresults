package com.dinya.peter.livefootballresults.lists;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinya.peter.livefootballresults.R;
import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.utils.ResourceUtils;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private final ListItemClickListener mListener;

    public TableAdapter(Context context, ListItemClickListener listener ) {
        mContext = context;
        mListener = listener;
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
        int id = mCursor.getInt(mCursor.getColumnIndex(DbContract.TeamEntry._ID));
        String teamPosition = mCursor.getString(mCursor.getColumnIndex(DbContract.TeamEntry.COLUMN_TEAM_POSITION));
        String teamName = mCursor.getString(mCursor.getColumnIndex(DbContract.TeamEntry.COLUMN_TEAM_NAME));
        String teamPoints = mCursor.getString(mCursor.getColumnIndex(DbContract.TeamEntry.COLUMN_TEAM_POINTS));
        String teamPlayedGames = mCursor.getString(mCursor.getColumnIndex(DbContract.TeamEntry.COLUMN_TEAM_PLAYED_GAMES));

        holder.mPosition.setText(teamPosition);
        holder.mPlayedGames.setText(teamPlayedGames);
        holder.mPoints.setText(teamPoints);
        holder.mTeam.setText(teamName);
        holder.mLogo.setImageResource(ResourceUtils.getLogoResource(id));

        int pos = Integer.parseInt((String) holder.mPosition.getText());
        switch (pos){
            case 1:
            case 2:
            case 3: holder.mPosition.setBackgroundColor(ContextCompat.getColor(mContext,R.color.CL_group));
                holder.mPosition.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                break;
            case 4:  holder.mPosition.setBackgroundColor(ContextCompat.getColor(mContext, R.color.CL_qualification));
                holder.mPosition.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                break;
            case 5:
            case 6: holder.mPosition.setBackgroundColor(ContextCompat.getColor(mContext, R.color.EL_group));
                holder.mPosition.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                break;
            case 7: holder.mPosition.setBackgroundColor(ContextCompat.getColor(mContext,R.color.EL_qualification));
                holder.mPosition.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                break;
            case 18:
            case 19:
            case 20: holder.mPosition.setBackgroundColor(ContextCompat.getColor(mContext,R.color.relegation));
                holder.mPosition.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                break;
            default:holder.mPosition.setBackground(null);
                holder.mPosition.setTextColor(ContextCompat.getColor(mContext,android.R.color.tertiary_text_dark));
        }
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

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        TextView mPosition;
        ImageView mLogo;
        TextView mTeam;
        TextView mPlayedGames;
        TextView mPoints;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        int position = getAdapterPosition();
                        mCursor.moveToPosition(position);
                        long id = mCursor.getLong(mCursor.getColumnIndex(DbContract.TeamEntry._ID));
                        mListener.listItemClick(id);
                    }
                }
            });
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

    public interface ListItemClickListener{
        void listItemClick(long id);
    }
}
