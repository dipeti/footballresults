package com.dinya.peter.livefootballresults;

import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dinya.peter.livefootballresults.entity.Match;
import com.dinya.peter.livefootballresults.lists.MatchAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView  mMatchesRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMatchesRecyclerView = (RecyclerView) findViewById(R.id.rv_my_matches);

        List<Match> matches = new ArrayList<>();
        matches.add(new Match("MAN UTD", "CHELSEA", 1, 0));
        matches.add(new Match("CHELSEA", "ARSENAL", 0, 1));
        matches.add(new Match("NEWCASTLE UTD", "CHELSEA", 1, 1));
        MatchAdapter matchAdapter = new MatchAdapter(matches);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMatchesRecyclerView.setLayoutManager(linearLayoutManager);
        mMatchesRecyclerView.setAdapter(matchAdapter);
        mMatchesRecyclerView.setHasFixedSize(true);
    }
}
