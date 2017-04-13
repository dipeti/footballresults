package com.dinya.peter.livefootballresults;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dinya.peter.livefootballresults.sync.BackgroundSyncUtils;

public class MainActivity extends AppCompatActivity implements TableFragment.OnListFragmentInteractionListener {

    public static final int UPCOMING_GAMES_LOADER_ID = 1;
    public static final int FINISHED_GAMES_LOADER_ID = 2;
    public static final int TABLE_LOADER_ID = 3;
    private static final String TAG = MainActivity.class.getSimpleName();


    /**
     * Member variables
     */
    Toolbar mToolbar;
    ViewPager mViewPager;
    FragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Setting up views
         */
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mFragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentPagerAdapter);
        TabLayout tableLayout = (TabLayout) findViewById(R.id.tab_layout);
        tableLayout.setupWithViewPager(mViewPager);

        /*
         * Start syncing data from remote server at first start
         */
        //BackgroundSyncUtils.initialize(this);

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

    /**
     * Pager Adapter class
     */
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
