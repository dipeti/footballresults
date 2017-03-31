package com.dinya.peter.livefootballresults.lists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinya.peter.livefootballresults.R;
import com.dinya.peter.livefootballresults.entity.Match;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private static final String TAG = MatchAdapter.class.getSimpleName();
    private List<Match> mMatches;


    public MatchAdapter(List<Match> matches) {
        mMatches = matches;
    }

    public void swap(List<Match> matches)
    {
        mMatches.addAll(matches);
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
        Match match = mMatches.get(position);
        holder.listItemHomeTeam.setText(match.getHomeTeam());
        holder.listItemHomeScore.setText(0 <= match.getHomeScore() ? String.valueOf(match.getHomeScore()) : "");
        holder.listItemAwayTeam.setText(match.getAwayTeam());
        holder.listItemAwayScore.setText(0 <= match.getHomeScore()? String.valueOf(match.getAwayScore()) : "");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String date = simpleDateFormat.format(match.getDate());

        holder.listItemDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return mMatches.size();
    }


    class MatchViewHolder extends RecyclerView.ViewHolder {

        final TextView listItemHomeTeam;
        final TextView listItemHomeScore;
        final TextView listItemAwayTeam;
        final TextView listItemAwayScore;
        final TextView listItemDate;

        MatchViewHolder(View itemView) {
            super(itemView);
            listItemHomeTeam = (TextView) itemView.findViewById(R.id.list_item_tv_home_team);
            listItemHomeScore = (TextView) itemView.findViewById(R.id.list_item_tv_home_score);
            listItemAwayTeam  = (TextView) itemView.findViewById(R.id.list_item_tv_away_team);
            listItemAwayScore = (TextView) itemView.findViewById(R.id.list_item_tv_away_score);
            listItemDate = (TextView) itemView.findViewById(R.id.list_item_tv_match_date);
        }
    }
}
