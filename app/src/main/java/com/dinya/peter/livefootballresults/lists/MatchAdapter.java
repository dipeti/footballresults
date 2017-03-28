package com.dinya.peter.livefootballresults.lists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dinya.peter.livefootballresults.R;
import com.dinya.peter.livefootballresults.entity.Match;

import java.util.List;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private static final String TAG = MatchAdapter.class.getSimpleName();
    private List<Match> mMatches;


    public MatchAdapter(List<Match> matches) {
        mMatches = matches;
    }

    public void add(Match match){
        mMatches.add(match);
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
        holder.listItemMatchView.setText(mMatches.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return mMatches.size();
    }

    public void swapList(List<Match> matches) {
        mMatches = matches;
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {

        final TextView listItemMatchView;
        MatchViewHolder(View itemView) {
            super(itemView);
            listItemMatchView = (TextView) itemView.findViewById(R.id.tv_item_match);
        }
    }
}
