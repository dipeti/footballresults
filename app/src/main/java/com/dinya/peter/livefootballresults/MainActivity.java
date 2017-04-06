package com.dinya.peter.livefootballresults;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.database.DbHelper;
import com.dinya.peter.livefootballresults.lists.MatchAdapter;
import com.dinya.peter.livefootballresults.sync.BackgroundSyncUtils;
import com.dinya.peter.livefootballresults.utils.NetworkUtils;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity /*implements LoaderManager.LoaderCallbacks<Cursor>*/ {

//    private static final int UPCOMING_GAMES_LOADER_ID = 1;
//    private static final int FINISHED_GAMES_LOADER_ID = 2;
    private static final String TAG = MainActivity.class.getSimpleName();

//    private RecyclerView  mMatchesRecyclerView;
//    MatchAdapter mMatchAdapter;
//    private SQLiteDatabase mDb;
//    private LoaderManager mLoaderManager;
    ViewPager mViewPager;
    FragmentPagerAdapter mFragmentPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mFragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentPagerAdapter);
        TabLayout tableLayout = (TabLayout) findViewById(R.id.tabs);
        tableLayout.setupWithViewPager(mViewPager);
        if(NetworkUtils.isConnected(this))
            BackgroundSyncUtils.initialize(this);
        else Toast.makeText(this,"No internet connection", Toast.LENGTH_LONG).show();


//        mMatchesRecyclerView = (RecyclerView) findViewById(R.id.rv_my_matches);
//        Button mButton = (Button) findViewById(R.id.bt_add_more);

//        mMatchAdapter = new MatchAdapter();
//        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        mMatchesRecyclerView.setLayoutManager(linearLayoutManager);
//        mMatchesRecyclerView.setAdapter(mMatchAdapter);
//        mMatchesRecyclerView.setHasFixedSize(true);

//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mLoaderManager.initLoader(FINISHED_GAMES_LOADER_ID,null,MainActivity.this);
//            }
//        });

//            mLoaderManager = getLoaderManager();
//            mLoaderManager.initLoader(UPCOMING_GAMES_LOADER_ID, null, this);
//            DbHelper dbHelper = new DbHelper(this);
//            mDb = dbHelper.getWritableDatabase();
//        if(NetworkUtils.isConnected(this))
//            BackgroundSyncUtils.initialize(this);
//        else Toast.makeText(this,"No internet connection", Toast.LENGTH_LONG).show();
    }
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static final int COUNT = 2;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return FinishedGamesFragment.newInstance();
                case 1:
                    return UpcomingGamesFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0: return "Finished games";
                case 1: return "Upcoming games";
                default:
                    return "error";
            }
        }
    }

    /*
     * ------------------
     * LoaderCallbacks
     * ------------------
     */
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        String[] selectionArgs;
//        switch (id){
//            case UPCOMING_GAMES_LOADER_ID:
//                selectionArgs = DbContract.getCurrentDateSelectionArgs();
//                Log.d(TAG, "Selection: " + Arrays.toString(selectionArgs));
//                return new CursorLoader(this,DbContract.GameEntry.CONTENT_URI_UPCOMING_GAMES,null, null, selectionArgs,null);
//            case FINISHED_GAMES_LOADER_ID:
//                selectionArgs = DbContract.getCurrentDateSelectionArgs();
//                Log.d(TAG, "Selection: " + Arrays.toString(selectionArgs));
//                return new CursorLoader(this,DbContract.GameEntry.CONTENT_URI_FINISHED_GAMES,null, null, selectionArgs,null);
//            default:
//                throw new RuntimeException("Loader Not Implemented: " + id);
//        }
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        mMatchAdapter.swap(data);
//    }
//
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        mMatchAdapter.swap(null);
//    }

}
