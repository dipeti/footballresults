package com.dinya.peter.livefootballresults;

import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.dinya.peter.livefootballresults.entity.Match;
import com.dinya.peter.livefootballresults.listener.EndlessRecyclerViewScrollListener;
import com.dinya.peter.livefootballresults.lists.MatchAdapter;
import com.dinya.peter.livefootballresults.utils.JSONParserUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView  mMatchesRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mMatchesRecyclerView = (RecyclerView) findViewById(R.id.rv_my_matches);
        Button mButton = (Button) findViewById(R.id.bt_add_more);

        final List<Match> matches = JSONParserUtils.getMatches(true);
        final MatchAdapter matchAdapter = new MatchAdapter(matches);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMatchesRecyclerView.setLayoutManager(linearLayoutManager);
        mMatchesRecyclerView.setAdapter(matchAdapter);
        mMatchesRecyclerView.setHasFixedSize(true);
        mMatchesRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                final int count = matchAdapter.getItemCount();
                matches.addAll(JSONParserUtils.getMatches(false));
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        matchAdapter.notifyItemRangeInserted(count,matches.size() - 1);
                    }
                });

            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int count = matchAdapter.getItemCount();
                matches.add(new Match("MAN UTD", "CHELSEA", 1, matches.size()+1));
                matchAdapter.notifyItemRangeInserted(count,matches.size()-1);
            }
        });
    }
}
