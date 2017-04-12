package com.dinya.peter.livefootballresults;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.TextView;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.lists.MatchAdapter;
import com.dinya.peter.livefootballresults.sync.BackgroundSyncUtils;

import java.util.Arrays;



public class FinishedGamesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = FinishedGamesFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    MatchAdapter mMatchAdapter;
    private LoaderManager mLoaderManager;

//    private OnFragmentInteractionListener mListener;
    private RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
    @Override
    public void onChanged() {
        if(0 == mMatchAdapter.getItemCount()){
            mRecyclerView.setVisibility(View.INVISIBLE);
            mEmptyView.setVisibility(View.VISIBLE);
        }else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.INVISIBLE);
        }
    }
};

    public FinishedGamesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FinishedGamesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FinishedGamesFragment newInstance() {
        FinishedGamesFragment fragment = new FinishedGamesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mLoaderManager = getActivity().getLoaderManager();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
        mMatchAdapter = new MatchAdapter();
        mMatchAdapter.registerAdapterDataObserver(mAdapterDataObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        mMatchAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finished_games, container, false);

        mEmptyView = (TextView) view.findViewById(R.id.tv_empty_view);
        /*
         * RecyclerView initialization
         */
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_finished_games);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mMatchAdapter);
        mRecyclerView.setHasFixedSize(true);
        /*
         * SwipeToRefresh initialization
         */
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh_finished_games);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        /**
         * Important remark:
         * Loader is started in CreateView so that it has got access to {@link mEmptyView} in  onCreateLoader.
         * Otherwise {@link mEmptyView} may flash inconveniently before the loader has finished.
         */
        mLoaderManager.initLoader(MainActivity.FINISHED_GAMES_LOADER_ID, null, this);
        mAdapterDataObserver.onChanged();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void fetchData() {
        if(!BackgroundSyncUtils.startImmediateSync(getContext()))
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
        mEmptyView.setVisibility(View.INVISIBLE);
        String[] selectionArgs;
        switch (id){
            case MainActivity.FINISHED_GAMES_LOADER_ID:
                int daysPast = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("days_past", "5"));
                selectionArgs = DbContract.getDateSelectionArgs(-daysPast); // games in the past 'dayDiff' days
                Log.d(TAG, "Selection: " + Arrays.toString(selectionArgs));
                return new CursorLoader(getActivity(),DbContract.GameEntry.CONTENT_URI_FINISHED_GAMES,null, null, selectionArgs,null);
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
        mLoaderManager.restartLoader(MainActivity.FINISHED_GAMES_LOADER_ID,null,this);
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
