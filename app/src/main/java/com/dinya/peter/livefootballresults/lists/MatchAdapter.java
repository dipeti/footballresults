package com.dinya.peter.livefootballresults.lists;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinya.peter.livefootballresults.R;
import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.database.DbHelper;
import com.dinya.peter.livefootballresults.entity.Match;
import com.dinya.peter.livefootballresults.utils.ResourceUtils;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private static final String TAG = MatchAdapter.class.getSimpleName();
    private Cursor mGames;

    public MatchAdapter() {

    }

    public void swap(Cursor games)
    {
        mGames = games;
        notifyDataSetChanged();
    }


    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /* Inflating the view */
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.match_list_item,parent,false);
        view.setFocusable(true);

        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        mGames.moveToPosition(position);
        String homeTeam = mGames.getString(mGames.getColumnIndex(DbContract.TeamEntry.ALIAS_HOME_TEAM));
        String awayTeam = mGames.getString(mGames.getColumnIndex(DbContract.TeamEntry.ALIAS_AWAY_TEAM));
        int homeScore = mGames.getInt(mGames.getColumnIndex(DbContract.GameEntry.COLUMN_HOME_SCORE));
        int awayScore = mGames.getInt(mGames.getColumnIndex(DbContract.GameEntry.COLUMN_AWAY_SCORE));
        String dateString = mGames.getString(mGames.getColumnIndex(DbContract.GameEntry.COLUMN_DATE));
        SimpleDateFormat fromFormat = new SimpleDateFormat(DbContract.SQL_DATE_FORMAT, Locale.ENGLISH);

        try {
            Date date = fromFormat.parse(dateString);
            SimpleDateFormat toFormat = new SimpleDateFormat("HH:mm - d MMM yyyy");
            dateString = toFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (homeScore > awayScore){
                holder.listItemHomeTeam.setTypeface(null, Typeface.BOLD);
                holder.listItemAwayTeam.setTypeface(null, Typeface.NORMAL);
        } else if (homeScore < awayScore){
            holder.listItemAwayTeam.setTypeface(null, Typeface.BOLD);
            holder.listItemHomeTeam.setTypeface(null, Typeface.NORMAL);
        }else {
            holder.listItemHomeTeam.setTypeface(null, Typeface.NORMAL);
            holder.listItemAwayTeam.setTypeface(null, Typeface.NORMAL);
        }
        holder.listItemHomeTeam.setText(homeTeam);
        holder.listItemHomeScore.setText(0 <= homeScore ? String.valueOf(homeScore) : "");
        holder.listItemAwayTeam.setText(awayTeam);
        holder.listItemAwayScore.setText(0 <= awayScore? String.valueOf(awayScore) : "");
        holder.listItemDate.setText(dateString);
        holder.listItemHomeLogo.setImageResource(ResourceUtils.getLogoResource(mGames.getInt(mGames.getColumnIndex(DbContract.TeamEntry.ALIAS_HOME_ID))));
        holder.listItemAwayLogo.setImageResource(ResourceUtils.getLogoResource(mGames.getInt(mGames.getColumnIndex(DbContract.TeamEntry.ALIAS_AWAY_ID))));
    }

    @Override
    public int getItemCount() {
        if (null == mGames) return 0;
        return mGames.getCount();
    }


    class MatchViewHolder extends RecyclerView.ViewHolder {

        final TextView listItemHomeTeam;
        final TextView listItemHomeScore;
        final TextView listItemAwayTeam;
        final TextView listItemAwayScore;
        final TextView listItemDate;
        final ImageView listItemHomeLogo;
        final ImageView listItemAwayLogo;

        MatchViewHolder(View itemView) {
            super(itemView);
            listItemHomeTeam = (TextView) itemView.findViewById(R.id.list_item_tv_home_team);
            listItemHomeScore = (TextView) itemView.findViewById(R.id.list_item_tv_home_score);
            listItemAwayTeam  = (TextView) itemView.findViewById(R.id.list_item_tv_away_team);
            listItemAwayScore = (TextView) itemView.findViewById(R.id.list_item_tv_away_score);
            listItemDate = (TextView) itemView.findViewById(R.id.list_item_tv_match_date);
            listItemHomeLogo = (ImageView) itemView.findViewById(R.id.list_item_iv_home_logo);
            listItemAwayLogo = (ImageView) itemView.findViewById(R.id.list_item_iv_away_logo);
        }
    }
}
