package com.dinya.peter.livefootballresults;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.database.DbHelper;
import com.dinya.peter.livefootballresults.lists.MatchAdapter;
import com.dinya.peter.livefootballresults.sync.BackgroundSyncUtils;

import java.util.Arrays;



public class UpcomingGamesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = FinishedGamesFragment.class.getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    MatchAdapter mMatchAdapter;
    private LoaderManager mLoaderManager;

//    private OnFragmentInteractionListener mListener;

    public UpcomingGamesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FinishedGamesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingGamesFragment newInstance() {
        UpcomingGamesFragment fragment = new UpcomingGamesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMatchAdapter = new MatchAdapter();
        mLoaderManager = getActivity().getLoaderManager();
        mLoaderManager.initLoader(MainActivity.UPCOMING_GAMES_LOADER_ID, null, this);
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_games, container, false);

        /*
         * RecyclerView initialization
         */
        RecyclerView matchesRecyclerView = (RecyclerView) view.findViewById(R.id.rv_upcoming_games);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),linearLayoutManager.getOrientation());
        matchesRecyclerView.addItemDecoration(dividerItemDecoration);
        matchesRecyclerView.setLayoutManager(linearLayoutManager);
        matchesRecyclerView.setAdapter(mMatchAdapter);
        matchesRecyclerView.setHasFixedSize(true);

        /*
         * SwipeToRefresh initialization
         */
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh_upcoming_games);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        return view;
    }

    private void fetchData() {
        if(!BackgroundSyncUtils.startImmediateSync(getContext()));
        mSwipeRefreshLayout.setRefreshing(false);
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * ------------------
     * LoaderCallbacks
     * ------------------
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] selectionArgs;
        switch (id){
            case MainActivity.UPCOMING_GAMES_LOADER_ID:
                int daysAhead = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("days_ahead", "5"));
                selectionArgs = DbContract.getDateSelectionArgs(daysAhead); // games in the upcoming 'dayDiff' days
                Log.d(TAG, "Selection: " + Arrays.toString(selectionArgs));
                return new CursorLoader(getActivity(),DbContract.GameEntry.CONTENT_URI_UPCOMING_GAMES,null, null, selectionArgs,null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMatchAdapter.swap(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMatchAdapter.swap(null);
    }

    /*
     * ------------------
     * OnSharedPreferenceChangeListener
     * ------------------
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mLoaderManager.restartLoader(MainActivity.UPCOMING_GAMES_LOADER_ID,null,this);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
