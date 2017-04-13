package com.dinya.peter.livefootballresults;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ScrollingTabContainerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class ViewPagerFragment extends Fragment {

    private static final String TAG = ViewPagerFragment.class.getSimpleName();

    Toolbar mToolbar;
    ViewPager mViewPager;
    FragmentPagerAdapter mFragmentPagerAdapter;

    public ViewPagerFragment() {
        // Required empty public constructor
    }


    public static ViewPagerFragment newInstance() {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        TabLayout tableLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        mFragmentPagerAdapter = new ViewPagerFragment.MyPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mFragmentPagerAdapter);
        tableLayout.setupWithViewPager(mViewPager);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case  R.id.action_settings:
                Intent startSettingsActivityIntent = new Intent(getActivity(),SettingsActivity.class);
                startActivity(startSettingsActivityIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private static final int COUNT = 3;

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem: switch");
            switch (position){
                case 0: Log.d(TAG, "getItem: " + "FinishedGamesFragment");
                    return FinishedGamesFragment.newInstance();
                case 1: Log.d(TAG, "getItem: " + "UpcomingGamesFragment");
                    return UpcomingGamesFragment.newInstance();
                case 2: Log.d(TAG, "getItem: " + "TableFragment");
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
