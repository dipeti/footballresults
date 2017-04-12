package com.dinya.peter.livefootballresults;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity implements TableFragment.OnListFragmentInteractionListener {

//    private static final int UPCOMING_GAMES_LOADER_ID = 1;
//    private static final int FINISHED_GAMES_LOADER_ID = 2;
    private static final String TAG = MainActivity.class.getSimpleName();



//    private SQLiteDatabase mDb;
//    private LoaderManager mLoaderManager;
    Toolbar mToolbar;
    ViewPager mViewPager;
    FragmentPagerAdapter mFragmentPagerAdapter;
    private CoordinatorLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mFragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentPagerAdapter);
        TabLayout tableLayout = (TabLayout) findViewById(R.id.tab_layout);
        tableLayout.setupWithViewPager(mViewPager);
        mContainer = (CoordinatorLayout) findViewById(R.id.cl_container);
        BackgroundSyncUtils.initialize(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
           case  R.id.action_settings:
               Intent startSettingsActivityIntent = new Intent(this,SettingsActivity.class);
               startActivity(startSettingsActivityIntent);
               return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction() {
    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private static final int COUNT = 3;

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return FinishedGamesFragment.newInstance();
                case 1:
                    return UpcomingGamesFragment.newInstance();
                case 2:
                    return TableFragment.newInstance();
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
                case 0: return "Finished";
                case 1: return "Upcoming";
                case 2: return "Table";
                default:
                    return "error";
            }
        }
    }
}
