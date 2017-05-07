package com.dinya.peter.livefootballresults;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.lists.TableAdapter;
import com.dinya.peter.livefootballresults.sync.BackgroundSyncUtils;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static final int UPCOMING_GAMES_LOADER_ID = 1;
    public static final int FINISHED_GAMES_LOADER_ID = 2;
    public static final int TABLE_LOADER_ID = 3;

    public static final String INTENT_EXTRA_ID = "id";

    public static final int RC_SIGN_IN = 1; // request code, flag for when we return after startActivityForResult
    private static final String TAG = MainActivity.class.getSimpleName();


    /**
     * Member variables
     */
    Toolbar mToolbar;
    ViewPager mViewPager;
    FragmentPagerAdapter mFragmentPagerAdapter;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        /*
         * Setting up views
         */
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mFragmentPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        TabLayout tableLayout = (TabLayout) findViewById(R.id.tab_layout);
        tableLayout.setupWithViewPager(mViewPager);

        /*
         * Start syncing data from remote server at first start.
         */

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if  (null == user){
                    AuthUI authUI = AuthUI.getInstance();
                    startActivityForResult(
                            authUI.createSignInIntentBuilder()
                                    .setProviders(AuthUI.EMAIL_PROVIDER,AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                } else {
                    BackgroundSyncUtils.initialize(MainActivity.this);
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            if (resultCode==RESULT_OK){
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(MainActivity.this,"Signed-in as " + user.getDisplayName(),Toast.LENGTH_SHORT).show();
                }
            } else if(resultCode == RESULT_CANCELED){
                finish();
            }
        }
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
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Pager Adapter class
     */
    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private static final int COUNT = 3;
        private Context mContext;

        MyPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
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
                case 0: return mContext.getString(R.string.page_title_first);
                case 1: return mContext.getString(R.string.page_title_second);
                case 2: return mContext.getString(R.string.page_title_third);
                default:
                    return "error";
            }
        }
    }
}
